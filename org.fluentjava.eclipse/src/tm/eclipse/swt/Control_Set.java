package tm.eclipse.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;







//import tm.eclipse.swt.controls.Composite;
//import tm.eclipse.swt.controls.Text;
import tm.lang.Reflection;

public class  Control_Set<T extends Control>  
{	
	public T 				target;
	public Display   		display;
	public Reflection 		reflection;
	
	public Control_Color <T> color;
	public Control_Event <T> event;
	public Control_Layout<T> layout;
	
	public Control_Set(T target)
	{
		this.target  	= target;
		this.display 	= target.getDisplay();
		this.reflection = new Reflection(this);		
		this.color 	    = new Control_Color<T>(target);
		this.event 	    = new Control_Event<T>(target);
		this.layout 	= new Control_Layout<T>(target);
	}	
	public T location(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			target.setLocation(x, y);			
		}});	
		return target;
	}

	public T size(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			target.setSize(x, y);			
		}});	
		return target;
	}
	public T toolTip(final String toolTip)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			target.setToolTipText(toolTip);			
		}});
		return target;
	}
	public T redraw(final boolean value)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			target.setRedraw(value);			
		}});	
		return target;
	}
	public T bold()
	{
		//I think this leaks font objects, so check if they can be reused 
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			FontData fontData = target.getFont().getFontData()[0];
			int style         = fontData.getStyle(); 
			Font font = new Font(display, fontData.getName(), fontData.getHeight(), style | SWT.BOLD);
			target.setFont(font);				
		}});	
		return target;
	}
		
}
