package tm.eclipse.api;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.console.*;

import tm.eclipse.Plugin_Config;

public class Console// extends EclipseBase 
{
	public ConsolePlugin   consolePlugin;  
	public IConsoleManager consoleManager;
	public MessageConsole  messageConsole; 
	public String  		   consoleName;  
	
	public Console()
	{ 
		consoleName = Plugin_Config.CONSOLE_NAME;  
		
		syncExec(new VoidResult() { public void run() 		
			{
				consolePlugin = ConsolePlugin.getDefault();
				consoleManager = consolePlugin.getConsoleManager();
				messageConsole = open(consoleName);
			}});
		
	}
	public Console 		  close(final String targetConsole)
	{		
		syncExec(new VoidResult() { public void run() 		
			{
				IConsole console = Console.this.get(targetConsole);
				if (console != null)
					Console.this.consoleManager.removeConsoles( new IConsole[]{ console });
			}});	
		return this;
	}
	public String		  contents()
	{		
		return null;
	}
	public String 		  contents(String targetConsole)
	{
		return null;
	}
	public List<IConsole> existing()
	{
		return Arrays.asList(consoleManager.getConsoles());  
	}
	public boolean 		  exists(String name)
	{
		return this.get(name) != null;
	}
	public MessageConsole get(String name)
	{
		for(IConsole console : existing())
			if (console.getName().equals(name))
				return (MessageConsole)console;
		return null;
	}
	public MessageConsole open(String name)
	{
		if (exists(name))
			return get(name);
		return new_MessageConsole(name);
	}
	public MessageConsole new_MessageConsole(String name)
	{
		if(this.get(name) == null)
		{
			MessageConsole newConsole = new MessageConsole(name, null);
			this.consoleManager.addConsoles(new IConsole[]{ newConsole });
			return newConsole;
		}
		return null;
	}
	public Console		  show()
	{
		return show(messageConsole);
	}
	public Console 		  show(String consoleToShow)
	{
		return show(get(consoleToShow));
	}
	public Console 		  show(MessageConsole consoleToShow)
	{
		if (messageConsole != null)
			consoleManager.showConsoleView(consoleToShow);
		return this;
	}
	public Console		  log(String message)
	{
		return write(message);
	}
	public Console 		  write(String message)
	{
		return write(message, messageConsole);
	}
	public Console 		  write(final String message, final String targetConsoleName)
	{
		return write(message, get(targetConsoleName));
	}
	public Console 		  write(final String message, final MessageConsole targetConsole)
	{
		if (message != null && targetConsole != null )
			syncExec(new VoidResult() { public void run() 		
				{
					
					MessageConsoleStream consoleStream = targetConsole.newMessageStream();
					consoleStream.println(message);
				}});
			
		return this;
	}
}
