package tm.eclipse.swt.controls;

//import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.Composite_Add;
import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Self;
import tm.eclipse.swt.Mouse;
import tm.eclipse.swt.Control_Set;
import tm.lang.Reflection;

public class Composite extends org.eclipse.swt.widgets.Composite
{	
	public org.eclipse.swt.widgets.Composite parent;
	public Display 							 display;
	public Mouse 							 mouse;
	public Composite_Add<Composite> 		 add;
	public Control_Get  <Composite>   		 get;
	public Control_Set  <Composite>   		 set;
	public Control_Self <Composite>	  		 self;
	
	public Composite(org.eclipse.swt.widgets.Composite parent)
	{				
		this(parent, SWT.NONE);		
	}
	public Composite(org.eclipse.swt.widgets.Composite parent, int style) 
	{
		super(parent, style);
		this.parent  = parent;
		this.display = parent.getDisplay(); 
		mouse 		 = new Mouse(this.getShell());
		add   		 = new Composite_Add<Composite>(this);
		set   		 = new Control_Set<Composite>(this);
		get   		 = new Control_Get<Composite>(this);
		self   		 = new Control_Self<Composite>(this);
		
		Layout layout = this.getLayout();		
		if (layout == null)
		{
			setLayout(new FillLayout());
		}
	}
	
	public static Composite add_Composite(final org.eclipse.swt.widgets.Composite target)
	{	
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Composite>() { public Composite run() 
					{
						Composite composite = new Composite(target);
						target.layout(true);
						return composite;
					}});
		
	}
	
	public Composite refresh()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Composite.this.parent.layout(true);
			}});
		return this;
		
	}


}
