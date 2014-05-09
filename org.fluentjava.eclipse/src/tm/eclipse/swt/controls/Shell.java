package tm.eclipse.swt.controls;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.controls.Composite;
import tm.eclipse.swt.Composite_Add;
import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Self;
import tm.eclipse.swt.Control_Set;
import tm.lang.Reflection;

public class Shell extends org.eclipse.swt.widgets.Shell 
{
	public static Shell onDefaultDisplay()
	{
		final Display display = Display.getDefault();
		return UIThreadRunnable.syncExec(display,new Result<Shell>() { public Shell run()
				{
					return new Shell(display);
				}});

	}
	public Composite_Add<Shell> add;
	public Control_Get<Shell> get;
	public Control_Set<Shell> set;
	public Control_Self<Shell> self;
	public Display display;
	//public Reflection reflection;
	//public Composite composite;
	
	public Shell(Display display) 
	{
		 super(display);
		 this.display 	 = display;
		 //this.reflection = new Reflection(this);
		 //this.composite = new Composite((org.eclipse.swt.widgets.Composite)this); // need to wrap the current shell's composite into our own Composite 
		 add  = new Composite_Add<Shell>(this);
		 self = new Control_Self<Shell>(this);
		 get  = new Control_Get <Shell>(this);
		 set  = new Control_Set <Shell>(this);
	}
	
	protected void checkSubclass()
	{}
	
	@Override 
	public void close()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.super.close();

		}});
	}

	public Shell sleep(int miliseconds)
	{
		try 
		{
			Thread.sleep(miliseconds);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		return this;
	}
	public Shell show()
	{		
		return this;
	}	
	
	@Override 
	public void open()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.super.open();			
		}});
	}
	@Override 
	public void pack()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.super.pack();			
		}});
	}
	public Shell packAndOpen()
	{
		this.pack();
		this.open();
		return this;
	}
	public Shell title(final String title)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.this.setText(title);			
		}});		
		return this;
	}
	public Shell move(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.super.setLocation(x,y);			
		}});
		return this;
	}
	public Shell size(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.super.setSize(x,y);			
		}});
		return this;
	}
	public Shell location(final int x, final int y)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.super.setLocation(x,y);			
		}});
		return this;
	}
	public Shell focus()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.this.setFocus();			
		}});
		return this;
	}
	public Shell refresh()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Shell.this.layout(true);			
		}});
		return this;
	}
	public Shell clear()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			for(Control control: controls())
				control.dispose();
		}});
		return this;
	}
	public List<Control> controls()
	{
		return controls(false);
	}
	public List<Control> controls(final boolean recursive)
	{
		return UIThreadRunnable.syncExec(display,new Result<List<Control>>() { public List<Control> run() 
		{
			return self.controls(recursive); 
		}});		
	}
	public Object data()
	{
		return UIThreadRunnable.syncExec(display,new Result<Object>() { public Object run() 
		{
			return Shell.this.getData(); 
			//return Arrays.asList(Shell.this.getChildren());
			//return new Control_Self(shell)
		}});		
	}
}