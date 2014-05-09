package tm.eclipse.helpers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.PlatformUI;

public class colors
{	
	public static Color get(final int systemColorId)
	{
		Display display = PlatformUI.getWorkbench().getDisplay();
		return UIThreadRunnable.syncExec(display, new Result<Color>() { public Color run()	
			{
				return Display.getCurrent ().getSystemColor(systemColorId);
			}});
	}
	public static Color black()
	{
		return get(SWT.COLOR_BLACK);
	}
	public static Color blue()
	{
		return get(SWT.COLOR_BLUE);
	}
	public static Color control()
	{
		return get(SWT.CONTROL);
	}	
	public static Color gray()
	{
		return get(SWT.COLOR_GRAY);
	}
	public static Color green()
	{
		return get(SWT.COLOR_GREEN);
	}
	public static Color darkGreen()
	{
		return get(SWT.COLOR_DARK_GREEN);
	}
	public static Color darkRed()
	{
		return get(SWT.COLOR_DARK_RED);
	}
	public static Color red()
	{
		return get(SWT.COLOR_RED);
	}
	public static Color white()
	{
		return get(SWT.COLOR_WHITE);
	}
}
