package tm.eclipse.swt;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.eclipse.swt.controls.Composite;

public class Control_Get<T extends Control> 
{	
	public T target;
	public Display   display;
	
	public Control_Get(T target)
	{
		this.target  = target;
		this.display = target.getDisplay();
	}
	
	public Layout layout()
	{
		return UIThreadRunnable.syncExec(display,new Result<Layout>() { public Layout run() 
		{
			if (target instanceof Composite)
			{
				Composite composite = (Composite)target;
				return composite.getLayout();				
			}
			return null;
		}});
	}
	
	public String toolTip()
	{
		return UIThreadRunnable.syncExec(display,new Result<String>() { public String run() 
		{
			return target.getToolTipText();			
		}});		
	}
}
