package tm.eclipse.api;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

public class EclipseBase 
{	
	public  IWorkbench 		 workbench;
	public  Display 		 display;
	public IWorkbenchPage    activePage;
	public IWorkbenchWindow  workbenchWindow;
	
	public EclipseBase(IWorkbench _workbench)
	{
		workbench = _workbench;
		display = workbench.getDisplay(); 
		display.syncExec(new Runnable() { public void run()
			{
				workbenchWindow = workbench.getActiveWorkbenchWindow();
				activePage 		= workbenchWindow.getActivePage();
			}});
	}	
}
