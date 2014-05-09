package tm.eclipse.swt.controls;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.Composite_Add;
import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Set;

public class SashForm extends org.eclipse.swt.custom.SashForm
{
	public Display 			   		display;
	public Composite_Add<Composite >  add;
	public Control_Set<SashForm>  	set;
	public Control_Get<SashForm> 	get;
	
	public SashForm(Composite parent, int style) 
	{
		super(parent, style);
		this.display = parent.getDisplay();			// we need to store this in case there are multiple ones		
		this.add     = new Composite_Add<Composite >((Composite)this);
		this.set     = new Control_Set<SashForm>(this);
		this.get     = new Control_Get<SashForm>(this);
	}
	public static SashForm add_SashForm(Composite target)
	{
		return add_SashForm(target, SWT.HORIZONTAL);
	}	
	public static SashForm add_SashForm(final Composite target, final int style)
	{	
		if (target == null)
			return null;		
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<SashForm>() { public SashForm run() 
					{
						SashForm sashForm = new SashForm(target,style);
						return sashForm;						
					}});
		
	}
	public SashForm width(final int width)
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				SashForm.this.setSashWidth(width);
			}});
		return this;
	}
	public int[] weights()
	{		
		return UIThreadRunnable.syncExec(display,new Result<int[]>() { public int[] run() 
			{
				return SashForm.this.getWeights();				
			}});		
	}
	public SashForm weights(final int ...weights)
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				SashForm.this.setWeights(weights);
				
			}});
		return this;
	}
	public SashForm horizontal()
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				SashForm.this.setOrientation(SWT.HORIZONTAL);
			}});
		return this;
	}
	public SashForm vertical()
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				SashForm.this.setOrientation(SWT.VERTICAL);
			}});
		return this;
	}
	protected void checkSubclass()
	{}
}
