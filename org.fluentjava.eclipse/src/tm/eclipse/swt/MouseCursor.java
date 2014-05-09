package tm.eclipse.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.eclipse.api.Utils;

public class MouseCursor 
{
	public Display display;
	public Shell   shell;
	
	public MouseCursor(Shell shell)
	{
		this.shell = shell;
		this.display= shell.getDisplay();
	}
	public Control control()
	{
		return UIThreadRunnable.syncExec(display, new Result<Control>() { public Control run()		
		{
			return MouseCursor.this.display.getCursorControl();
		}});	
	}
	public Point location()
	{
		return UIThreadRunnable.syncExec(display, new Result<Point>() { public Point run()		
		{
			return MouseCursor.this.display.getCursorLocation();
		}});	
	}
	public MouseCursor location(Point location)
	{
		return location(location.x, location.y);
	}
	public MouseCursor location(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()		
		{
			MouseCursor.this.display.setCursorLocation(x, y); 
		}});	
		return this;
	}
	public MouseCursor move_To(int x, int y)
	{
		return location(x, y);
	}
	public MouseCursor move_By(int x, int y)
	{
		Point currentLocation = location();
		return location(currentLocation.x + x, currentLocation.y + y);
	}
	public MouseCursor move_Right(int x)
	{
		return move_By(x,0);
	}
	public MouseCursor move_Left(int x)
	{
		return move_By(-x,0);
	}
	public MouseCursor move_Up(int y)
	{
		return move_By(0,-y);
	}
	public MouseCursor move_Down(int y)
	{
		return move_By(0,y);
	}
	public Cursor current()
	{
		return UIThreadRunnable.syncExec(display, new Result<Cursor>() { public Cursor run()		
		{
			return MouseCursor.this.shell.getCursor();
		}});	
	}
	
	public MouseCursor arrow()
	{
		return cursor(systemCursor_Arrow());
	}
	public MouseCursor cross()
	{
		return cursor(systemCursor_Cross());
	}
	public MouseCursor hand()
	{
		return cursor(systemCursor_Hand());
	}
	public MouseCursor help()
	{
		return cursor(systemCursor_Help());
	}	
	public MouseCursor cursor(final Cursor newCursor)
	{
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()	
		{
			MouseCursor.this.shell.setCursor(newCursor);
		}});	
		return this;
	}
	public Cursor systemCursor_Arrow()
	{
		return systemCursor(SWT.CURSOR_ARROW);
	}
	public Cursor systemCursor_Cross()
	{
		return systemCursor(SWT.CURSOR_CROSS);
	}
	public Cursor systemCursor_Hand()
	{
		return systemCursor(SWT.CURSOR_HAND);
	}
	public Cursor systemCursor_Help()
	{
		return systemCursor(SWT.CURSOR_HELP);
	}
	public Cursor systemCursor_Wait()
	{
		return systemCursor(SWT.CURSOR_WAIT);
	}
	public Cursor systemCursor(final int id)
	{
		return UIThreadRunnable.syncExec(display, new Result<Cursor>() { public Cursor run()		
		{
			return MouseCursor.this.display.getSystemCursor(id);
		}});	
	}

	public MouseCursor sleep(int miliseconds)
	{
		Utils.sleep(miliseconds);
		return this;
	}
}
