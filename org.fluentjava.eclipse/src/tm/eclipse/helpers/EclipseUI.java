package tm.eclipse.helpers;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.*;
import org.eclipse.ui.dialogs.PreferencesUtil;

import tm.eclipse.api.EclipseAPI;
import tm.eclipse.swt.controls.Browser;
import tm.eclipse.swt.controls.extra.Form;
import tm.eclipse.ui.views.Eclipse_Panel;

public class EclipseUI 
{
	public static EclipseAPI eclipse;
	
	static
	{
		eclipse = EclipseAPI.current();
	}
		
	public static void 					 asyncExec(final Runnable runnable)
	{
		UIThreadRunnable.asyncExec(new VoidResult() { public void run()
			{
				runnable.run();
			}});
		
	}	

	
	public static String 				 messageBox(final String message)
	{
		return messageBox(message, "");
	}
    public static String 				 messageBox(final String message, final String title)
    {
    	syncExec(new Runnable() { public void run() 
    		{
    			MessageBox messageBox = new MessageBox(eclipse.shell);
    			messageBox.setMessage(message);
    			messageBox.setText(title);
    			messageBox.open();
    		}});
    	return message;
    }
	public static Eclipse_Panel 		 new_Panel()
	{
		return eclipse.panelFactory.open_Panel();
	}
	public static Eclipse_Panel 		 new_Panel(String panelId)
	{
		return eclipse.panelFactory.open_Panel(panelId);
	}
	public static Browser			 new_Browser()
	{
		return new_Browser(null);
	}
	public static Browser			 new_Browser(String browserId)
	{
		return new_Panel(browserId).add.browser();
	}

	public static PreferenceDialog 		 open_PreferencesDialog(final String id)
	{		
		return UIThreadRunnable.syncExec(new Result<PreferenceDialog>() { public PreferenceDialog run() 
				{
					PreferenceDialog preferenceDialog = PreferencesUtil.createPreferenceDialogOn(eclipse.workbench.getActiveWorkbenchWindow().getShell(), id, null, null);
					preferenceDialog.open();
					return preferenceDialog;		
				}});
		
	}
	
	public static Form 				 popupWindow()
	{
		return Form.popupWindow();
	}
	public static void 					 syncExec(final Runnable runnable)
	{
		UIThreadRunnable.syncExec(new VoidResult() { public void run()
			{
				runnable.run();
			}});
		
	}
	
	
}
