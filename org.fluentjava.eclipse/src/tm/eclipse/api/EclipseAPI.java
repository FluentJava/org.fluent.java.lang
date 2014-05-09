package tm.eclipse.api;

import static tm.eclipse.helpers.log.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.fluentjava.eclipse.EclipsePartEvents;

import tm.eclipse.helpers.Images;
import tm.eclipse.swt.Mouse;
import tm.eclipse.swt.controls.extra.ObjectBrowser;

public class EclipseAPI 
{
	private static EclipseAPI        current;
	public  static EclipsePartEvents partEvents;	
	public HashMap<String,Object> 	objects;
	public List<String>     	  	extraGroovyJars;
	
	public IWorkbenchWindow activeWorkbenchWindow;	
	public IWorkbenchPage	activeWorkbenchPage;
	public IWorkbench 		workbench;	
	public Display	 		display;
	public Shell   	    	shell;
	public IWorkspace   	workspace;
	
	public Images			images;
	public Plugin			plugin;
	public Console			console;
	public Menus			menus;
	public Panels		    panelFactory;
	public Views			views;
	public Editors		    editors;
	public Mouse			mouse;
	public Platform			platform;
	public Registry		    registry;
	public Utils		    utils; 
	public boolean			ready;
	
	static 
	{
		// this will be called on Eclipse startup on a separate thread
		//PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() { @Override public void run() 
			//{								
			//}});	
	}
	public EclipseAPI() 
	{	
		display = PlatformUI.getWorkbench().getDisplay();
		if (SWTUtils.isUIThread(display))
			setup();
		else		
		//UIThreadRunnable.syncExec(display, new VoidResult() { public void run()
			display.syncExec(new Runnable() { public void run() 
				{
					setup();
				}});
	}	
	public static EclipseAPI current()
	{
		if (current == null)
			current = new EclipseAPI();
		return current;
	}
	public EclipseAPI       setup()
	{
		objects 		= new HashMap<String,Object>();
		extraGroovyJars = new ArrayList<String>();
		 
		captureEclipseObjects();
		images		 = new Images();
		mouse 		 = new Mouse(shell);
		menus  		 = new Menus(workbench);
		panelFactory = new Panels(workbench);
		views  		 = new Views(EclipseAPI.this);
		editors  	 = new Editors(EclipseAPI.this);
		plugin 	 	 = new Plugin(EclipseAPI.this);
		console 	 = new Console();
		platform 	 = new Platform(EclipseAPI.this);
		registry 	 = new Registry(EclipseAPI.this);
		utils 	     = new Utils(EclipseAPI.this);

		setEclipsePartEvents();
		ready = true;
		return this;
	}
	public EclipseAPI       alert(String message)
	{		
		MessageDialog.openInformation(shell,"Message",message);
		return this;
	}
	public EclipseAPI 		asyncExec(final Runnable callback)
	{
		UIThreadRunnable.asyncExec(display, new VoidResult() { public void run()	
			{
				callback.run();
			}});
		return this;
	}			
	public EclipseAPI 		captureEclipseObjects()
	{		
		try
		{
			workbench 			  = PlatformUI.getWorkbench();
//			display    			  = workbench.getDisplay();				
			activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
			activeWorkbenchPage	  = activeWorkbenchWindow.getActivePage();
			shell 	   			  = activeWorkbenchWindow.getShell();		
			workspace  			  = ResourcesPlugin.getWorkspace();
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
		
		return this;
	}
	public IEditorReference editor(String editorTitle)
	{
		return editors.get(editorTitle);
	}
	public EclipseAPI       log(String message)
	{		
		//info(message);
		console.log(message);
		return this;
	}	
	public String 			ping()
	{
		return "Pong ...";
	}	
	public EclipseAPI 		setEclipsePartEvents()
	{
		if(partEvents==null)
		{ 			
			partEvents = new EclipsePartEvents();

			IPartService partService = workbench.getActiveWorkbenchWindow().getPartService();
			partService.addPartListener(partEvents);
				
		}
		return this;
	}	
	public ObjectBrowser 	show(Object object)
	{
		return ObjectBrowser.show_ObjectBrowser(object);
	}
	public <T> T 			syncExec(final Callable<T> callback)
	{
		return UIThreadRunnable.syncExec(display, new Result<T>() { public T run()	
			{
				try {
					return callback.call();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					return null;
				}
			}});		
	}
	public EclipseAPI 		syncExec(final Runnable runnable)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()	
			{
				runnable.run();
			}});
		return this;
	}
	public IViewReference   view(String viewIdTitleOrPartName)
	{
		return views.get(viewIdTitleOrPartName);
	}

	//helpers
	public IWorkbench 	    workbench()
	{
		return workbench;
	}
}

