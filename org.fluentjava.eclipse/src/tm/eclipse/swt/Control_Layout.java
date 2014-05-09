package tm.eclipse.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

public class Control_Layout<T extends Control>
{
	public Display display;
	public T 	   target;
	
	public Control_Layout(T target)
	{
		this.target  = target;
		this.display = target.getDisplay();
	}
	
	public T to(final Layout layout)
	{
		return layout(layout);
	}
	
	public T layout(final Layout layout)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			if (target instanceof Composite)
			{
				Composite composite = (Composite)target;
				composite.setLayout(layout);
			}
		}});
		return target;
	}
	public T grid()
	{
		return grid(1,false);
	}
	public T grid(int numColumns)
	{
		return grid(numColumns,false);
	}
	public T grid(int numColumns, boolean makeColumnsEqualWidth)
	{		
		return layout(new GridLayout(numColumns, makeColumnsEqualWidth));
	}
	public T fill()
	{
		return fill(true);
	}
	public T fill(boolean horizontalLayout)
	{
		int alignment = (horizontalLayout) ? SWT.HORIZONTAL : SWT.VERTICAL;
		return layout(new FillLayout(alignment));
	}
	public T row()
	{
		return row(true);
	}
	public T row(boolean horizontalLayout)
	{
		int alignment = (horizontalLayout) ? SWT.HORIZONTAL : SWT.VERTICAL;
		return layout(new RowLayout(alignment));
	}

	public T grid_Fill()
	{
		return grid_Grab_All();
	}
	public T grid_Grab()
	{
		return grid_Grab_All();
	}
	public T grid_Grab_All()
	{		
		return grid_GrabExcess(true,true);
	}
	public T grid_Grab_All(final int horizontalSpan, final int verticalSpan)
	{				
		return grid_GrabExcess(true,true, horizontalSpan, verticalSpan);
	}
	public T grid_Grab_Horizontal()
	{
		return grid_GrabExcess(true,false);
	}
	public T grid_Grab_Vertical()
	{
		return grid_GrabExcess(false,true);
	}
	public T grid_GrabExcess(boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace)
	{
		return grid_GrabExcess(grabExcessHorizontalSpace, grabExcessVerticalSpace, 1,1);
	}
	public T grid_GrabExcess(final boolean grabExcessHorizontalSpace, final boolean grabExcessVerticalSpace, final int horizontalSpan, final int verticalSpan)
	{		
		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{			
			GridData gridData = new GridData(SWT.FILL , SWT.FILL, grabExcessHorizontalSpace,grabExcessVerticalSpace,horizontalSpan, verticalSpan);
			target.setLayoutData(gridData);	
//			target.layout(true);
		}});
		return target;
	}

	/*public T rowLayout()
	{
		return this.layout_Row();
	}
	public T fillLayout()
	{	
		return this.layout_Fill();
	}*/

}
