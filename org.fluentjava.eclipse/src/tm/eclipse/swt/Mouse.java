package tm.eclipse.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;


public class Mouse 
{
	public static int GLIDE_DELAY_MILISECONDS = 10;
	public static int GLIDE_DELAY_STEP  	  = 5;
	public static int MOUSE_CLICK_DELAY 	  = 300;
	
	public Shell        shell;
	public Display 		display;
	public MouseCursor  cursor; 
	
	//public Composite target;
	
	public Mouse(Shell shell)
	{
		this.shell 	 = shell;
		this.display = shell.getDisplay();
		this.cursor  = new MouseCursor(shell);
	}
	
	public Mouse delay(int value)
	{
		GLIDE_DELAY_MILISECONDS  = value;
		return this;
	}
	public Mouse click()
	{
		click(SWT.MouseDown,1);		
		sleep(MOUSE_CLICK_DELAY);
		click(SWT.MouseUp,1);		
 		return this;
	}
	
	public Mouse click(final int type, final int button)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()
		{
			Event event  = new Event();
			event.type   = type;
			event.button = button;
			display.post(event);
		}});	
		return this;
	}
	public Mouse click(Control control)
	{
		return click(control,true); 
	}
	public Mouse click(Control control, boolean glideToControl)
	{
		if (glideToControl)
			glide(control);			
		else
			move(control);
		click();
		return this;
	}
	public Mouse glide(final Control control)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()
			{
				Point location = location_Desktop(control);
				Point size = control.getSize();
				int targetX = location.x + size.x /2; // point to the middle x of the control
				int targetY = location.y + size.y /2; // point to the middle y of the control
				glide(targetX, targetY);
			}});
		return this;
	}
	public Mouse glide(Point point)
	{
		return glide(point.x, point.y);
	}
	public Mouse glide(final int x, final int y)
	{
		Point currentLocation;
		do 
		{			
			currentLocation = location();
			int newX = currentLocation.x;
			int newY = currentLocation.y;
			
			if (newX != x)
				newX = (newX > x) ? newX - 1 : newX  + 1;
			if (newY != y)
				newY = (newY > y) ? newY - 1 : newY  + 1;
			
			move(newX, newY);
			if(GLIDE_DELAY_STEP > 0 && (newX+newY) % GLIDE_DELAY_STEP == 0)
				sleep(GLIDE_DELAY_MILISECONDS);
			
		} while (currentLocation.x != x  || currentLocation.y != y);
		return this;
	}
	public Mouse move(final Control control)
	{
		return move(location_Desktop(control));
	}
	public Mouse move(Point point)
	{
		return move(point.x, point.y);
	}
	public Mouse move(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()
			{
				Event event = new Event();
				event.type = SWT.MouseMove;		
				event.x = x;
				event.y = y;
				display.post(event);				
			}});
		return this;
	}
	public Mouse moveBy(int x, int y)
	{
		Point curentLocation =  location();
		return move(curentLocation.x + x, curentLocation.y + y);		
	}
	public Mouse sleep(int miliseconds)
	{
		try { Thread.sleep(miliseconds); } catch (InterruptedException e) { e.printStackTrace(); }
		return this;
	}
	public Point location()
	{
		return UIThreadRunnable.syncExec(display, new Result<Point>() { public Point run()
			{
				return display.getCursorLocation();
			}});
	}
	
	public Point location_Desktop(final Control control)
	{
		return UIThreadRunnable.syncExec(display, new Result<Point>() { public Point run()
		{
			Point location = control.toControl(1,1);
			location.x = Math.abs(location.x);	// no idea why, but I was having negative values here
			location.y = Math.abs(location.y);	// no idea why, but I was having negative values here
			return location;
		}});
	}
	public Point location_Relative(final Control control)
	{
		return UIThreadRunnable.syncExec(display, new Result<Point>() { public Point run()
			{
				return control.getLocation();
			}});
	}
	
	//keyboard event
	public Mouse text(final String text)
	{
		return keyPress(text);
	}
	public Mouse keyPress(final String text)
	{
		return keyPress(text.toCharArray());
	}
	public Mouse keyPress(final char[] characters)
	{
		for(char character : characters)
			keyPress(character);
		return this;
	}
	public Mouse keyPress(final char character)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()
		{
			 Event event = new Event();
		     event.type = SWT.KeyDown;
		     event.character = character;		      
		     display.post(event);
		}});	
		return this;
	}
	 
}
