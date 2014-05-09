package tm.eclipse.swt.controls.extra;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;

import tm.eclipse.swt.controls.Browser;
import tm.eclipse.swt.controls.Button;
import tm.eclipse.swt.controls.Shell;
import tm.eclipse.swt.controls.Text;
import tm.eclipse.swt.controls.Tree;
import tm.lang.Reflection;

public class Form 
{
	public Display display;
	public Shell   shell;
	public Thread  thread_toClose;
	public Reflection reflection;
	
	public Form()
	{
		display = calculateDisplay();
		display.syncExec(new Runnable() { public void run() 
					{
						shell = new Shell(display);		
					}});
		reflection = new Reflection(this);
	}
	
	public static Display calculateDisplay() 
	{
		//SWTUtils.display() does it a bit different, but the version below will create a display when it doesn't find one
		if (Display.getCurrent() != null)
			return Display.getCurrent() ;
		if (Display.getDefault() != null)
			return Display.getDefault() ;
		return new Display ();
	}
	public Form show()
	{
		display.syncExec(new Runnable() { public void run() 
					{
						shell.open();
					}});
		return this;
	}
	public Form layout_Fill()
	{
		display.syncExec(new Runnable() { public void run() 
					{
						shell.setLayout(new FillLayout());
					}});
		return this;
	}
	
	
	public Form waitForClose()
	{
		display.syncExec(new Runnable() { public void run() 
					{
						while (!shell.isDisposed()) 
						{
							if (!display.readAndDispatch())
								display.sleep();
						}
					}});
		return this;
	}
	
	public Form close()
	{
		display.syncExec(new Runnable() { public void run() 
					{
						shell.dispose();
					}});
		return this;
	}
		
	public Form close_InSeconds(final int seconds)
	{
		return close_InMiliSeconds(seconds * 1000);
	}
	public Form close_InMiliSeconds(final int miliSeconds)
	{
		thread_toClose = new Thread(new Runnable() { public void run()
		{		
			try 
			{
				Thread.sleep(miliSeconds);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			display.syncExec(new Runnable() { public void run() 
						{
							shell.dispose();
						}});			
			}
		});
		thread_toClose.start();		
		return this;
	}
	
	public List<Control> controls()
	{
		
		Control[] controls = UIThreadRunnable.syncExec(display, new ArrayResult<Control>() { public Control[] run()	
								{ 
									return shell.getChildren();
								}});
		return Arrays.asList(controls);
	}
	
	public <T> T control(Class<T> clazz)
	{
		for(Control control : controls())
			if (control.getClass() == clazz)
				return clazz.cast(control);
		return null;		
	}
	public Form 		wait_Seconds(final int seconds)
	{
		return wait_MiliSeconds(seconds * 1000);
	}
	public Form 		wait_MiliSeconds(final int miliSeconds)
	{
		final Semaphore semaphore = new Semaphore(1);
		Thread thread = new Thread(new Runnable() { public void run()
			{		
				try 
				{
					Thread.sleep(miliSeconds);
					semaphore.release();
				}
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}					
			}
		});
		thread.start();
		while (semaphore.availablePermits() ==1) 
		{
			display.syncExec(new Runnable() { public void run() 
						{
							if (!display.readAndDispatch())
								try 
								{
									Thread.sleep(100);
								}
								catch (InterruptedException e) 
								{
				
									e.printStackTrace();
								}
						}});
		}		
		return this;
	}
		
	public Browser   add_Browser()
	{
		return Browser.add_Browser(this.shell);
	}
	public Button      add_Button()
	{
		return Button.add_Button(this.shell);
	}
	public Button      add_Button(String text)
	{
		return Button.add_Button(this.shell, text);
	}
	public Text      add_Text()
	{
		return Text.add_Text(this.shell);
	}
	public Text      add_Text_Search()
	{
		return Text.add_Text_Search(this.shell);
	}
	public Tree      add_Tree()
	{
		return Tree.add_Tree(this.shell);
	}
	
	public Form refresh()
	{
		display.syncExec(new Runnable() { public void run() 
					{
						shell.layout(true);
					}});
		return this;
	}
	public static Form popupWindow()
	{
		return new Form().layout_Fill()
						    .show();
	}
}
