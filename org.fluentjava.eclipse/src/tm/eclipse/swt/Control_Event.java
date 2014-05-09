package tm.eclipse.swt;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.lang.Reflection;


public class Control_Event <T extends Control>
{
	public Display 		display;
	public T 	   		target;
	public Reflection 	reflection;
	
	public Control_Event(T target)
	{
		this.target  	= target;
		this.display 	= target.getDisplay();
		this.reflection = new Reflection(target);
	}

	public T onClick(final Runnable runnable)
	{
		return onSelection(runnable);
	}
	public T onSelection(final Runnable runnable)
	{
		if (runnable != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					target.addListener(SWT.Selection, new Listener() { public void handleEvent(Event event) 
					{
						runnable.run();
					}});
				}});
		return target;
	}
	public T onMouseEnter(final Runnable runnable)
	{
		return mouseTrackListener(new MouseTrackListener() 
						{
							public void mouseHover(MouseEvent e) { }
							public void mouseExit (MouseEvent e) { }							
							public void mouseEnter(MouseEvent e) { runnable.run(); }
						});		
		/*if (runnable != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					target.addMouseTrackListener( new MouseTrackListener() 
						{
							public void mouseHover(MouseEvent e) { }
							public void mouseExit (MouseEvent e) { }							
							public void mouseEnter(MouseEvent e) { runnable.run(); }
						});
				}});
		return target;*/
	}
	public T mouseTrackListener(final MouseTrackListener mouseTrackListener)
	{
		if (mouseTrackListener != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					target.addMouseTrackListener(mouseTrackListener);					
				}});
		return target;
	}
	
	
	public T onChange(final Runnable runnable)
	{
		if (runnable != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					target.addListener(SWT.CHANGED, new Listener() { public void handleEvent(Event event) 
					{
						new Thread(runnable).start();
						//runnable.run();
					}});
				}});
		return target;
	}
	public T click()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
			{
				Method method = reflection.method("sendSelectionEvent", int.class);
				reflection.invoke(method, org.eclipse.swt.SWT.Selection);
			}});
		return target;		
	}
}
