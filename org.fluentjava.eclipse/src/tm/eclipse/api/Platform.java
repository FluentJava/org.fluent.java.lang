package tm.eclipse.api;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.results.Result;

public class Platform
{
	public EclipseAPI eclipse;
	public Display    display;
	
	public Platform(EclipseAPI eclipse)
	{
		this.eclipse = eclipse;
		this.display = eclipse.display;
	}
	
	@SuppressWarnings("restriction")
	public org.eclipse.core.internal.runtime.InternalPlatform internalPlatform()
	{
		return org.eclipse.core.internal.runtime.InternalPlatform.getDefault();
	}
	
	public Shell shell(final String title)
	{
		return syncExec(display,new Result<Shell>() { public Shell run ()  
			{				
				for(Shell shell : shells())
					if(shell.getText().equals(title))
						return shell;
				return null;
			}});
	}
	public List<Shell> shells()
	{
		return syncExec(new Result<List<Shell>>() { public List<Shell> run ()  
			{				
				return Arrays.asList(display.getShells());
			}});
	}

	@SuppressWarnings("restriction")
	public String OS() 
	{
		return internalPlatform().getOS();
	}
}
