package tm.eclipse.ui.views;
//import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.codehaus.groovy.control.CompilationFailedException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.*;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swt.SWT;
import org.fluentjava.eclipse.Activator;

import tm.eclipse.Plugin_Config;
import tm.eclipse.api.EclipseAPI;
import tm.eclipse.groovy.plugins.GroovyExecution;
import tm.utils.BeanShell;
import tm.utils.CollectionToString;


public class SimpleEditor extends ViewPart 
{
	public static final String ID = "g2.scripts.views.SimpleEditor";
	public Display			display;		
	public SashForm   		sashForm;			
	public Composite   		composite;	
	public StyledText 		styledText_Code;
	public StyledText 		styledText_Result;
	public ToolItem 		execute_Button;
	public ToolItem			stop_Button;
	public Thread		    executionThread;
	public GroovyExecution	groovyExecution;
	public BeanShell		beanShell;
	public String           lastExecutedScript;
	public boolean			executeSync;	
	public boolean			executeUIThread;	
	
	public SimpleEditor() 
	{
		display 	= PlatformUI.getWorkbench().getDisplay();
		executeSync = false;  									// default to async execution
		executeUIThread = true;									// on the UI Thread
	}

	public void createPartControl(Composite _composite) 
	{
		composite  = _composite;
		buildGui();
	}
	public SimpleEditor buildGui()
	{	
		composite.setLayout(new FormLayout());
		
		//toolbar
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);//SWT.FLAT | SWT.RIGHT);
		FormData fd_toolBar = new FormData();
		fd_toolBar.left = new FormAttachment(0);
		fd_toolBar.right = new FormAttachment(100);
		fd_toolBar.top = new FormAttachment(0);
		fd_toolBar.bottom = new FormAttachment(0, 22);
		//toolBar.setBackground(new Color(Display.getCurrent (),255,255,255));		
		toolBar.setLayoutData(fd_toolBar);
		
		//sashForm
		//sashForm = new SashForm(composite, SWT.VERTICAL);
		sashForm = new SashForm(composite, SWT.HORIZONTAL);
		FormData fd_sashForm = new FormData();
		fd_sashForm.right = new FormAttachment(toolBar, 0, SWT.RIGHT);
		fd_sashForm.left = new FormAttachment(0);
		fd_sashForm.bottom = new FormAttachment(100, 0);
		fd_sashForm.top = new FormAttachment(0, 25);
		sashForm.setLayoutData(fd_sashForm);
		
		//groovy and result styleText
		Group grpGroovyCode = new Group(sashForm, SWT.NONE);
		grpGroovyCode.setText("Groovy Code");
		grpGroovyCode.setLayout(new FillLayout(SWT.HORIZONTAL));		
		Group grpExecutionResult = new Group(sashForm, SWT.NONE);
		grpExecutionResult.setText("Execution result");
		grpExecutionResult.setLayout(new FillLayout(SWT.HORIZONTAL));
		sashForm.setWeights(new int[] {2, 1});
		
	    styledText_Code   = new StyledText(grpGroovyCode, SWT.BORDER);	   
	    styledText_Result = new StyledText(grpExecutionResult, SWT.BORDER  | SWT.H_SCROLL | SWT.V_SCROLL);
	    	    
	    styledText_Result.setBackground(new Color(Display.getCurrent (),200,200,255));
	    styledText_Result.setWordWrap(true);
	    

	    //toolbar buttons
	    execute_Button = new ToolItem(toolBar, SWT.NONE);
	    execute_Button.setImage(Activator.getImageDescriptor("/images/pngs/script_go.png").createImage());
	    
	    execute_Button.setText("Execute");
	    stop_Button = new ToolItem(toolBar, SWT.NONE);
	    stop_Button.setImage(Activator.getImageDescriptor("/images/pngs/stop.png").createImage());
	    stop_Button.setText("Stop");
		stop_Button.setEnabled(false);
		
	    
	    execute_Button.addListener(SWT.Selection, new Listener() 
	    	{ 
	    		public void handleEvent(Event event) 
				{
					compileAndExecuteCode_ASync();
				} });
		
	    stop_Button.addListener(SWT.Selection, new Listener() 
	    	{ 
	    		@SuppressWarnings("deprecation")
				public void handleEvent(Event event) 
				{
					if (executionThread != null && executionThread.isAlive())
					{
					//	styledText_Result.setText("...stoping execution...");
					//	styledText_Result.setBackground(new Color(Display.getCurrent (),255,220,220));				
						executionThread.stop();										
					}					
					showExecutionStoppedMessage();	
				} });
	    
	    //default value
	    
		styledText_Code.setText( "openArticle('Cross-Site Scripting')\n" +
				      			 "//openArticle('SQL Injection')\n" + 
					  			 "return eclipseAPI;" + 
				      			 "\n" + 
					  			 "//Use code below to execute a *.groovy file opened on an code Editor \n" + 
				      			 "return groovy.execute_GroovyEditor()");
		return this;		
	}

	public void setFocus() 
	{
	//	viewer.getControl().setFocus();
	}
	
	public Object 		compileAndExecuteCode(String codeToExecute)
	{
		if(codeToExecute==null)
			return null;
		set_ScriptToExecute(codeToExecute);			
		return (executeSync) ? compileAndExecuteCode_Sync()
						 	 : compileAndExecuteCode_ASync();
	}
	public Object 		compileAndExecuteCode_Sync()
	{
		compileAndExecuteCode_ASync();
		try 
		{
			executionThread.join(Plugin_Config.MAX_WAIT_FOR_SYNC_GROOVY_EXECUTION);
		}
		catch (InterruptedException e) 
		{
			showExecutionStoppedMessage();
		}
		return groovyExecution.returnValue;
	}
	public SimpleEditor compileAndExecuteCode_ASync() 
	{
		if (get_ScriptToExecute().contains("//Config:BeanShell"))
			return handle_BeanShellExecution();
		return handle_GroovyExecution();
	}
	public SimpleEditor handle_BeanShellExecution() 
	{ 
		prepareUIForExecution();
		executionThread = new Thread(new Runnable() { public void run() 
		{		
			beanShell = new BeanShell();
			beanShell.eval(get_ScriptToExecute());
			
			syncExec(new VoidResult() { public void run()
			{
				Object result = beanShell.lastReturnValue;
				String asString = new CollectionToString(result).asString();
				styledText_Result.setText(result != null ?  asString
		                 								 : "NULL return value");
				stop_Button.setEnabled(false);
				execute_Button.setEnabled(true);
				styledText_Result.setBackground(new Color(Display.getCurrent (),220,255,220));
			}});
		
		}});
		executionThread.start();
		return this;
	}
	public SimpleEditor handle_GroovyExecution() 
	{		
		groovyExecution = new GroovyExecution();
		groovyExecution.binding.setVariable("composite" , composite);
		groovyExecution.binding.setVariable("view"		, this);
		groovyExecution.executeOnUIThread = executeUIThread;		
		
		prepareUIForExecution();
		
		executionThread = new Thread(new Runnable() { public void run() 
			{								
				executeScript_and_ShowResult();
			}});
		
		executionThread.start();
		return this;
	}	
	public SimpleEditor executeScript_and_ShowResult()
	{
		groovyExecution.executeScript(lastExecutedScript);		
		showExecutionResult();
		return this;
	}
	public SimpleEditor showExecutionResult()
	{
		//ensure that we are back in the UI thread
		//Startup.eclipseApi.display.asyncExec(new Runnable() { public void run()
		syncExec(new VoidResult() { public void run()
			{
				Exception exception = groovyExecution.executionException;
				Object result = groovyExecution.returnValue;			
				if (exception == null)
				{			
					String asString = new CollectionToString(result).asString();
					styledText_Result.setText(result != null ?  asString
			                 								 : "NULL return value");			
				}
				else if (exception instanceof CompilationFailedException) 
				{					
					styledText_Result.setText("COMPILATION ERROR:" + exception.getMessage());
				}
				else
				{
					String message = exception.getMessage();
					styledText_Result.setText("GENERIC ERROR:" + message + " : " + exception.toString());
				}
				stop_Button.setEnabled(false);
				execute_Button.setEnabled(true);
				styledText_Result.setBackground(new Color(Display.getCurrent (),220,255,220));
			}});
		return this;
	}
	
	public SimpleEditor showExecutionStoppedMessage()
	{
		syncExec(new VoidResult() { public void run()
			{
				styledText_Result.setText("... execution stopped... ");
				stop_Button.setEnabled(false);
				execute_Button.setEnabled(true);				
				styledText_Result.setBackground(new Color(Display.getCurrent (),255,220,220));
			}});		
		return this;
	}
	public SimpleEditor prepareUIForExecution()
	{
		syncExec(new VoidResult() {public void run()
			{
				lastExecutedScript = styledText_Code.getText();
				styledText_Result.setText(" ... executing script with size: " + lastExecutedScript.length());
				
				stop_Button.setEnabled(true);
				execute_Button.setEnabled(false);
				styledText_Result.setBackground(new Color(Display.getCurrent (),220,220,255));
			}});
		return this;
	}
	public boolean		waitForExecutionComplete()
	{
		if(executionThread != null && executionThread.isAlive())
		{
			try 
			{
				executionThread.join(Plugin_Config.MAX_WAIT_FOR_SYNC_GROOVY_EXECUTION);
				return true;
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	public String       get_ScriptToExecute()
	{			
		return syncExec(new Result<String>() { public String run() 			
			{
				return  styledText_Code.getText();
			}});			
	}
	public String       set_ScriptToExecute(final String value)
	{	
		return syncExec(new Result<String>() { public String run() 			
			{
				styledText_Code.setText(value);				
				return  styledText_Code.getText();
			}});				
	}	

	public SimpleEditor close() 
	{		
		return EclipseAPI.current().views.close(this);					
	}
}