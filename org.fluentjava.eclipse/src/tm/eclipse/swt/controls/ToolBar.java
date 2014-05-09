package tm.eclipse.swt.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.eclipse.helpers.Images;
import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Set;

public class ToolBar extends org.eclipse.swt.widgets.ToolBar
{
	public Display 			   	display;
	public Composite 		   	target;
	public Control_Set<ToolBar> set;
	public Control_Get<ToolBar> get;
	
	public ToolBar(Composite parent, int style) 
	{
		super(parent, style);
		this.display = parent.getDisplay();			// we need to store this in case there are multiple ones
		this.target  = parent;	
		this.set     = new Control_Set<ToolBar>(this);
		this.get     = new Control_Get<ToolBar>(this);
	}
	public static ToolBar add_ToolBar(final Composite target)
	{	
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<ToolBar>() { public ToolBar run() 
					{
						ToolBar toolBar = new ToolBar(target,SWT.FLAT | SWT.WRAP | SWT.RIGHT);
						return toolBar;
					}});
		
	}
	public ToolItem add_Button(String text)
	{
		return add_Button(text, (Image)null);
	}
	public ToolItem add_Button(Image image)
	{
		return add_Button(null, image);
	}
	public ToolItem add_Button(String text, String imageKey)
	{
		return add_Button(text, Images.get(imageKey));
	}
		public ToolItem add_Button(String text, Image image)
	{
		return add_ToolItem(text,image);
	}

	public ToolItem add_ToolItem(final String text)
	{
		return add_ToolItem(text, null);
	}
	public ToolItem add_ToolItem(final String text, final Image image)
	{
		return UIThreadRunnable.syncExec(display,new Result<ToolItem>() { public ToolItem run() 
			{
				ToolItem toolItem = new ToolItem(ToolBar.this, SWT.PUSH);
				if (text!= null)
					toolItem.setText(text);			
				if (image != null)
					toolItem.setImage(image);
				return toolItem;
			}});
	}
	protected void checkSubclass()
	{}
	
}
