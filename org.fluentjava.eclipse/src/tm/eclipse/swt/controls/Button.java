package tm.eclipse.swt.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;

import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Set;
import tm.lang.Reflection;


public class Button extends org.eclipse.swt.widgets.Button
{	
	public Composite    		parent;
	public Display 				display;
	public Reflection 			reflection;
	public Control_Get<Button>  get;
	public Control_Set<Button>  set;
	
	public Button(Composite parent, int style) 
	{		
		super(parent, style);
		this.parent 	 = parent;
		this.display     = parent.getDisplay();			// we need to store this in case there are multiple ones
		this.reflection  = new Reflection(this);
		this.set 		 = new Control_Set<Button>(this);
		this.get 		 = new Control_Get<Button>(this);
	}
	
	public static Button add(Composite target)
	{
		return add_Button(target);
	}
	public static Button add(Composite target, String text)
	{
		return add_Button(target).text(text);
	}
	
	public static Button add_Button(final Composite target)
	{
		return add_Button(target, SWT.PUSH);
	}
	public static Button add_Button(final Composite target, final int style)
	{	
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Button>() { public Button run() 
					{
						Button button = new Button(target,style);
						target.layout(true);
						return button;
					}});
		
	}
	public static Button add_Button(final Composite target, final int style, String text)
	{		
		Button button = add_Button(target, style);
		button.setText(text);
		return button;
	}
	public static Button add_Button(final Composite target, String text)
	{
		return add_Button(target, SWT.None, text);
	}

	public static Button add_CheckBox(final Composite target)
	{
		return add_Button(target, SWT.CHECK);
	}	
	
	
	protected void checkSubclass()
	{}
	
	
	//extra methods
	
	public Button click()
	{		
		this.reflection.invoke_onSuperClass("click");
		return this;
	}
	public Button disable()
	{
		return enabled(false);
	}	
	public Boolean disabled()
	{
		return enabled() == false;
	}	
	public Button enable()
	{
		return enabled(true);
	}
	
	public Boolean enabled()
	{
		return UIThreadRunnable.syncExec(display,new Result<Boolean>() { public Boolean run() 
		{
			return Button.super.isEnabled();			
		}});
	}
	public Button enabled(final boolean value)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Button.super.setEnabled(value);			
		}});
		return this;
	}
	public String getText()
	{
		return UIThreadRunnable.syncExec(display,new Result<String>() { public String run() 
		{
			return Button.super.getText();			
		}});		
	}
	public Button onClick(final Runnable runnable)
	{
		if (runnable != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
				{
					Button.this.addListener(SWT.Selection, new Listener() { public void handleEvent(Event event) 
					{
						runnable.run();
					}});
				}});
	
		return this; 
	}
	public Composite parent()
	{
		return parent;
	}
	public Button image(final Image image)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Button.super.setImage(image);			
		}});
		return this;
	}	
	public void   setText(final String text)
	{
		if (text!=null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Button.super.setText(text);
				Button.this.getParent().layout(true);  // need to see if there is a better to way to refresh the contents				
			}});
	}
	public String text()
	{
		return getText();
	}
	public Button text(String text)
	{
		setText(text);
		return this;
	}
	
	public Button select()
	{
		return select(true);
	}
	public Button select(final boolean selected)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Button.this.setSelection(selected);			
		}});
		return this;
	}
	public Button unselect()
	{
		return select(false);
	}
}
