package org.fluentjava.eclipse;

import groovy.lang.Closure;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

import tm.eclipse.api.EclipseAPI;

public class EclipsePartEvents implements IPartListener2
{
	public static boolean LOG_Part_Opened 	 = false;
	public static boolean LOG_Part_Activated = false;
	
	Closure<IWorkbenchPartReference> Part_Activated;
	Closure<IWorkbenchPartReference> Part_Opened;
	
	@Override
	public void partActivated(IWorkbenchPartReference arg0) 
	{
		if(LOG_Part_Opened)
		{
			EclipseAPI.current().log("Part Activated: " + arg0.getId());
		}
		if (Part_Activated!=null)		
		{
			Part_Activated.call(arg0);
		}		
	}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) 
	{
		if(LOG_Part_Opened)
		{
			EclipseAPI.current().log("Part Opened: " + arg0.getId());
		}
		if (Part_Opened!=null)
		{
			Part_Opened.call(arg0);			
		}		
	}

	@Override	public void partBroughtToTop(IWorkbenchPartReference arg0) { }
	@Override	public void partClosed(IWorkbenchPartReference arg0) { }
	@Override	public void partDeactivated(IWorkbenchPartReference arg0) { }
	@Override	public void partHidden(IWorkbenchPartReference arg0) { }
	@Override	public void partInputChanged(IWorkbenchPartReference arg0) { }
	@Override	public void partVisible(IWorkbenchPartReference arg0) { }

}
