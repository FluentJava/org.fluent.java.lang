package tm.eclipse.swt.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

public class ToolItem extends org.eclipse.swt.widgets.ToolItem
{
	public Display 			display;
	public ToolBar 		   	toolBar;
	public Thread           onClick_ASync_Thread;
	
	public ToolItem(ToolBar parent, int style) 
	{
		super(parent, style);
		this.toolBar = parent;
		this.display = parent.getDisplay();
	}
	
	public ToolItem onClick(final Runnable runnable)
	{
		if (runnable != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					ToolItem.this.addListener(SWT.Selection, new Listener() { public void handleEvent(Event event) 
					{
						runnable.run();
					}});
				}});

		return this;
	}
	
	public ToolItem onClick_ASync(final Runnable runnable)
	{
		if (runnable != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					ToolItem.this.addListener(SWT.Selection, new Listener() { public void handleEvent(Event event) 
					{
						onClick_ASync_Thread = new Thread(runnable);
						onClick_ASync_Thread.start();
						//runnable.run();
					}});
				}});

		return this;
	}
	
	protected void checkSubclass()
	{}

}
