package tm.eclipse.groovy.plugins;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.eclipse.Plugin_Config;
import tm.eclipse.api.EclipseAPI;
import tm.eclipse.helpers.*;

public class GroovyExecution 
{
	public Binding	    		 binding;
	public CompilerConfiguration configuration;
	public EclipseAPI			 eclipseApi;	
	public GroovyShell 			 groovyShell;
	public ImportCustomizer 	 importCustomizer;
	public Object				 returnValue;
	public String 				 scriptToExecute;
	public Runnable			     onExecutionComplete;
	public Exception			 executionException;	
	public boolean 				 executeOnUIThread;
	
	public GroovyExecution() 
	{
		this(new ArrayList<String>());
		
	}
	public GroovyExecution(List<String> _addToClassPath)
	{		
		eclipseApi = EclipseAPI.current();		
		setBindingVariablesValues();
		setCompilerConfiguration();	
		setGroovyShell();
		addLocalVariablesToBinding();
	}
	
	public void 	       addLocalVariablesToBinding()
	{
		binding.setVariable("binding"		  , binding			);
		binding.setVariable("configuration"	  , configuration	);			
		binding.setVariable("importCustomizer", importCustomizer);
		binding.setVariable("groovyShell"	  , groovyShell		);
		
		//binding.setVariable("eclipseAPI"      , EclipseAPI.current());
		binding.setVariable("eclipse"         , EclipseAPI.current());  // I think this one is better
		//binding.setVariable("teammentorAPI"   , TeamMentorAPI.class);  //TODO: find way to register this from the TM plugin
		
		binding.setVariable("eclipseUI"       , EclipseUI.class);
		binding.setVariable("images"          , Images.class);
		binding.setVariable("colors"          , colors.class);		
		binding.setVariable("groovy"          , GroovyExecution.class);
		binding.setVariable("log"             , tm.eclipse.helpers.log.class);		
		binding.setVariable("form"            , tm.eclipse.swt.controls.extra.Form.class);
	}	
	public GroovyShell     setGroovyShell()
	{
		groovyShell = new GroovyShell(getClass().getClassLoader(),binding,configuration);		
		addRefsToGroovyShell(eclipseApi.extraGroovyJars);
		return groovyShell;
	}
	public GroovyShell     addRefToGroovyShell(String refToAdd)
	{
		if (refToAdd!=null)
		{
			try
			{
				URL url = new URL("file:///" + refToAdd);							
				groovyShell.getClassLoader().addURL(url);
			}
			catch(MalformedURLException ex)				// I can't see to be able to trigger this, even with crazy values like: url = new URL("bb$%aa !@$%^&*()_+{}[]|\"'?/><,.;'\\~`?|");
			{
				ex.printStackTrace();
			}
		}
		return groovyShell;
	}
	public GroovyShell     addRefsToGroovyShell(List<String> refsToAdd)
	{
		for(String refToAdd : refsToAdd)
			addRefToGroovyShell(refToAdd);		
		return groovyShell;
	}
	public Object 	       executeScript(String scriptText)
	{					
		scriptToExecute = scriptText;
		if (Plugin_Config.AUTOSAVE_GROOVY_SCRIPTS)
			eclipseApi.plugin.save_SavedScript(scriptText); //save script to SaveScripts folder
		return executeScript();		
	}
	public Object          executeScript()
	{		
		executionException = null;
		returnValue = null;
		setExecuteOptionsBasedOnCodeReferences();					// do this at the last minute so that we have access to the final state of the objects
		try 
		{		
			returnValue = (executeOnUIThread)
								? syncExec(new Result<Object>() { public Object run()
										{
											return groovyShell.evaluate(scriptToExecute);
										}})
				 				: groovyShell.evaluate(scriptToExecute);
		} 
		catch (Exception ex) 
		{					
			executionException = ex;
		}		
		if(onExecutionComplete != null)
			onExecutionComplete.run();
		return returnValue;
	}
	public Object 	       executeScript_Sync()   // this is the default
	{
		return executeScript();		
	}
	public Thread 	       executeScript_ASync()
	{
		Thread thread = new Thread(new Runnable() { @Override public void run() 
			{
				executeScript();
			}});
		thread.start();
		return thread;
	}
	public Object 	       executeScript_UIThread_Sync()
	{
		eclipseApi.display.syncExec(new Runnable() { @Override public void run() 
			{
				executeScript();				
			}} );
		return returnValue;
	}
	public GroovyExecution executeScript_UIThread_ASync()
	{
		eclipseApi.display.asyncExec(new Runnable() { @Override public void run() 
		{
			executeScript();				
		}} );
		return this;
	}
	public URL[]           get_GroovyShell_ClassLoader_Urls()
	{
		if (groovyShell != null)
			return groovyShell.getClassLoader().getURLs();			// (handle UnitTest exception)  java.lang.LinkageError: loader constraint violation:
		return null;
	}	
	
	public  GroovyExecution setExecuteOptionsBasedOnCodeReferences() 
	{
		//this should be done via AST, but text search is a good start:
		if (scriptToExecute.contains("//Config:UIThread_False"))
			executeOnUIThread = false; 
		if (scriptToExecute.contains("//Config:UIThread_True"))
			executeOnUIThread = true;
		return this;		
	}
	public Binding 	       setBindingVariablesValues() 
	{			
		binding = new Binding();				
		//TeamMentorAPI.mapGroovyBindings(binding);   //TODO: find way to register this from the TM plugin
		return binding;	
	}
	public void 	       setCompilerConfiguration()
	{
		configuration    = new CompilerConfiguration();		
		importCustomizer = new ImportCustomizer();		
		importCustomizer.addStarImports("tm.eclipse.ui");
		importCustomizer.addStarImports("tm.eclipse.api");		
		importCustomizer.addStarImports("tm.eclipse.groovy.plugins");
		importCustomizer.addStaticStars("tm.eclipse.groovy.plugins.GroovyExecution");
		
		configuration.addCompilationCustomizers(importCustomizer );			
	}	
	public GroovyExecution addRefsFor_SWTBot()
	{
		String eclipseDir = "/Users/plugin/_Dev/eclipses/Kepler/plugins";
		String[] extraRefs = new String[] { eclipseDir + "/org.eclipse.swtbot.eclipse.finder_2.1.2.201311041021.jar" ,
					 						eclipseDir + "/org.eclipse.swtbot.swt.finder_2.1.2.201311041021.jar" ,
					 						eclipseDir + "/org.apache.log4j_1.2.15.v201012070815.jar"  ,
					 						eclipseDir + "/org.hamcrest.core_1.3.0.v201303031735.jar", 
					 						eclipseDir + "/org.hamcrest.library_1.3.0.v201305281000.jar"};
		
		addRefsToGroovyShell(Arrays.asList(extraRefs));
		return this;
	}
	public GroovyExecution addRefsFor_JUnit()
	{
		String eclipseDir = "/Users/plugin/_Dev/eclipses/Kepler/plugins";
		String[] extraRefs = new String[] { eclipseDir + "/org.eclipse.swtbot.eclipse.finder_2.1.2.201311041021.jar" ,
					 						eclipseDir + "/org.eclipse.swtbot.swt.finder_2.1.2.201311041021.jar" ,
					 						eclipseDir + "org.apache.log4j_1.2.15.v201012070815.jar"  ,
					 						eclipseDir + "org.hamcrest.core_1.3.0.v201303031735.jar", 
					 						eclipseDir + "/org.hamcrest.library_1.3.0.v201305281000.jar"};
		
		addRefsToGroovyShell(Arrays.asList(extraRefs));
		return this;
	}
	public Object 		   executeScript_With_SWTbot(String swtBotScript)
	{
		addRefsFor_SWTBot();
		scriptToExecute = "import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;\n" + 
						  "def bot = new SWTWorkbenchBot();\n\n" + 
				          swtBotScript;

		return executeScript();
	}
	public Object          executeScript_With_JUnit(String jUnitScript)
	{
		scriptToExecute = "import org.junit.runner.JUnitCore;\n" +
						  "import org.junit.runner.Result;\n" +
						  "import org.junit.runner.notification.Failure;\n" +
						  "import tm.eclipse.groovy.plugins.*;\n\n" + 
						  jUnitScript;

		return executeScript();
	}	
	public Object          execute_JUnit_Test(String jUnitClassName)
	{
		addRefsFor_JUnit();
		scriptToExecute = "import org.junit.runner.JUnitCore;\n" + 
						  "import org.junit.runner.Result;\n" +
						  "import org.junit.runner.notification.Failure;\n\n" + 
  						  "def jUnitResult = JUnitCore.runClasses("+ jUnitClassName+".class);\n" +
				  		  "def failures = jUnitResult.getFailures();\n" +
				  		  "\n" + 
		 
				  		  "return failures.collect { '\\n - ' + it  }          + '\\n' + \n" +
			              "'\\n  runCount    : ' + jUnitResult.runCount +\n" +
			              "'\\n  failureCount: ' + jUnitResult.failureCount +\n" +
			              "'\\n  runTime      : ' + jUnitResult.runTime;\n";		
		return executeScript();		 
	}	
	
	@Override
	public String          toString()
	{
		if (executionException == null)
			return "GroovyExecution OK: " + returnValue;
		return "GroovyExecution ERROR: " + executionException.toString();
	}
	
	/*
	 * 
	 */
	public static String 		execute_GroovyEditor_Code(String title)
	{
		String groovyCode = (title == null)
								? "def activeEditor = eclipse.activeWorkbenchPage.getActiveEditor();\n" 
								: "def title = '"+ title +  "';             					    \n" +  
								  "def editor = eclipseUI.editor(title);						    \n" +
								  "if (editor == null)                  							\n" +
								  "  return 'ERROR: No editor found with title: ' + title;			\n" +
								  "def activeEditor = editor.getPart(true)							\n";
		return  groovyCode + 
		  	   "if (activeEditor.class.name == \"org.codehaus.groovy.eclipse.editor.GroovyEditor\") \n" +
		  	   "{ 																					\n" + 
		  	   "		def source = activeEditor.groovyCompilationUnit.source; 					\n" + 
		  	   "      return new GroovyExecution().executeScript(source); 							\n" +
		  	   "} 																					\n" + 
		  	   "return 'ERROR: Current editor is not a groovy editor' 								\n";
	}

	public static Object 		execute_GroovyEditor(String title)
	{
		String scriptToExecute = execute_GroovyEditor_Code(title);
		return new GroovyExecution().executeScript(scriptToExecute);	
	}
	
	public static GroovyExecution execute_SWTBot(String swtBotScriptToExecute)
	{
		GroovyExecution groovyExecution = new GroovyExecution();
		
		groovyExecution.executeScript_With_SWTbot(swtBotScriptToExecute);
		return groovyExecution;
	}
	public static GroovyExecution execute_SWTBot_Junit(String jUnitClassName)
	{
		return execute_SWTBot_Junit(jUnitClassName, null);
	}
	public static GroovyExecution execute_SWTBot_Junit(final String jUnitClass, String extraBuildPathRef)
	{
		final GroovyExecution groovyExecution=  new GroovyExecution();
		groovyExecution.addRefsFor_SWTBot();
		groovyExecution.addRefToGroovyShell(extraBuildPathRef);

		Thread thread = new Thread( new Runnable() { public void run()  
			{
				groovyExecution.execute_JUnit_Test(jUnitClass);

			if (groovyExecution.executionException != null)
				EclipseAPI.current().log("JUnit Execution Error: " +  groovyExecution.executionException.toString().replace("\n"," "));
			else
				EclipseAPI.current().log("JUnit Result: " + groovyExecution.returnValue.toString().replace("\n"," "));
			}});
		thread.start();
		return groovyExecution;
	}
	/*public static GroovyExecution dev_Execute_JUnitTest()
	{		
		String jUnitTestClass = "tm.eclipse.ui.TeamMentor_Menu_Test";
		return dev_Execute_JUnitTest(jUnitTestClass);
	}
	public static GroovyExecution dev_Execute_JUnitTest(String jUnitTestClass)
	{
		String binFolder = "//Users//plugin//_Dev//git//TeamMentor_Eclipse_Plugin//TeamMentor.Eclipse.UxTests//bin//";
		return dev_Execute_JUnitTest(binFolder, jUnitTestClass); 
	}
	public static GroovyExecution dev_Execute_JUnitTest(String binFolder, String jUnitTestClass)
	{
		GroovyExecution groovyExecution = new GroovyExecution();
		groovyExecution.addRefToGroovyShell(binFolder);
		groovyExecution.execute_JUnit_Test(jUnitTestClass);
		return groovyExecution;
	}*/
	public static Object 		  dev_Execute_TM_JUnit()
	{
		return dev_Execute_TM_JUnit("tm.utils.Network_Test");
	}
	public static Object 		  dev_Execute_TM_JUnit(String jUnitClassName)
	{
		GroovyExecution groovyExecution = new GroovyExecution();
		try {
			URL binFolder_TMCode   = GroovyExecution.class.getProtectionDomain().getCodeSource().getLocation();
			URL binFolder_TMJUnit;
		
			binFolder_TMJUnit = new URL(binFolder_TMCode, "../../TeamMentor.Eclipse.UxTests/bin/");
				 	
			groovyExecution.addRefToGroovyShell(binFolder_TMJUnit.getPath());

			groovyExecution.execute_JUnit_Test(jUnitClassName);
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}			
		return groovyExecution;
	}	
	public static void inspect_Object(Object target)
	{
		groovy.inspect.swingui.ObjectBrowser.inspect(target);
	}
}