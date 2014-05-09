package tm.eclipse.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import tm.eclipse.helpers.colors;

public class Control_Color<T extends Control>
{
	public Display display;
	public T 	   target;
	
	public Control_Color(T target)
	{
		this.target  = target;
		this.display = target.getDisplay();
	} 
	
	public Color back()
	{
		return background();
	}
	public T blue()
	{
		return background(colors.blue());
	}
	public T green()
	{
		return background(colors.green());
	}
	public T gray()
	{
		return background(colors.gray());
	}
	public T red()
	{
		return background(colors.red());
	}
	
	public T back_Green()
	{
		return background(colors.green());
	}
	public T back_Gray()
	{
		return background(colors.gray());
	}
	public Color bground()
	{
		return background();
	}
	public Color background()
	{
		return UIThreadRunnable.syncExec(display,new Result<Color>() { public Color run() 
			{
				return target.getBackground();
			}});
	}	
	public T background(final Color color)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				target.setBackground(color);
			}});
		return target;
	}
	public Color foreground()
	{
		return UIThreadRunnable.syncExec(display,new Result<Color>() { public Color run() 
			{
				return target.getForeground();
			}});
	}
	public T foreground(final Color color)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				target.setForeground(color);
			}});
		return target;
	}
	public Color text()
	{
		return foreground();
	}

	public T text_Red()
	{
		return foreground(colors.red());
	}
}
