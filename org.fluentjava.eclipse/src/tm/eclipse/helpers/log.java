package tm.eclipse.helpers;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

public class log 
{
	public static String MessageTitle = "TeamMentor Plugin";
	
	public static String cancel(String message)
	{		
		StatusManager manager = StatusManager.getManager();
		manager.handle(new Status(Status.CANCEL, MessageTitle, message));				
		return message;
	}
	public static String error(String message)
	{		
		StatusManager manager = StatusManager.getManager();
		manager.handle(new Status(Status.ERROR, MessageTitle, message));				
		return message;
	}
	public static String info(String message)
	{		
		StatusManager manager = StatusManager.getManager();
		manager.handle(new Status(Status.INFO, MessageTitle, message));				
		return message;
	}
	public static String ok(String message)
	{		
		StatusManager manager = StatusManager.getManager();
		manager.handle(new Status(Status.OK, MessageTitle, message));				
		return message;
	}
	public static String warning(String message)
	{		
		StatusManager manager = StatusManager.getManager();
		manager.handle(new Status(Status.WARNING, MessageTitle, message));				
		return message;
	}	
}
