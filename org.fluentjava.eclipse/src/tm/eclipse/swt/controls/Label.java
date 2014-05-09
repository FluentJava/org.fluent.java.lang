package tm.eclipse.swt.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Set;

public class Label extends org.eclipse.swt.widgets.Label
{
	public Display 			   display;
	public Composite 		   target;
	public Control_Set<Label>  set;
	public Control_Get<Label> get;
	
	public Label(Composite parent, int style) 
	{
		super(parent, style);
		this.display = parent.getDisplay();			// we need to store this in case there are multiple ones
		this.target  = parent;	
		this.set     = new Control_Set<Label>(this);
		this.get     = new Control_Get<Label>(this);
	}
	
	public static Label add_Label(Composite target)
	{
		return add_Label(target, SWT.None);
	}
	public static Label add_Label(Composite target, String text)
	{
		return add_Label(target, SWT.None, text);
	}	
	public static Label add_Label(final Composite target, final int style)
	{	
		if (target == null)
			return null;		
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Label>() { public Label run() 
					{
						Label label = new Label(target,style);
						return label.refresh();						
					}});
		
	}
	public static Label add_Label(final Composite target, final int style, String text)
	{
		return add_Label(target,style).text(text);		
	}
	
	public Label bold()
	{
		return set.bold();
	}
	
	public Label image(final Image image)
	{
		if (image != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Label.super.setImage(image);					
			}});
		return this;
	}
	@Override
	public void setText(final String text)
	{
		if (text != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Label.super.setText(text);	
				Label.this.refresh();
			}});
	}
	public Label toolTip(final String text)
	{
		if (text != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Label.super.setToolTipText(text);					
			}});
		return this;
	}
	@Override
	public String getText()
	{
		return UIThreadRunnable.syncExec(display,new Result<String>() { public String run() 
		{
			return Label.super.getText();			
		}});		
	}	
	public Label refresh()
	{
		if (target != null && display != null)			
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				target.layout(true);
			}});
					
		return this;
	}
	public String text()
	{
		return getText();
	}
	public Label text(String text)
	{
		setText(text);
		return this;
	}
	protected void checkSubclass()
	{}
}
