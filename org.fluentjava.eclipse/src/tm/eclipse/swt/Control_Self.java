package tm.eclipse.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.lang.Reflection;

public class Control_Self  <T extends Control> extends Reflection
{
	public Display 		display;
	public T 	   		target;
	public Composite 	composite;
	//public Reflection 	reflection;
	
	public Control_Self(T target)
	{
		super(target);
		this.target  = target;
		this.display = target.getDisplay();
		if (target instanceof Composite)
			composite = (Composite)target;
	}
	
	public T clear()
	{				
		UIThreadRunnable.syncExec(composite.getDisplay(), new VoidResult() { public void run()
			{
				for(Control control : controls())
					control.dispose();
			}});
		
		return target;
	}
	public List<Control> controls()
	{
		return controls(false);
	}
	public List<Control> controls(final boolean recursive)
	{
		return controls(composite, recursive);
	}
	public List<Control> controls(final Composite targetComposite , final boolean recursive) 
	{
		if (targetComposite == null)
			return new ArrayList<Control>();
		
		return UIThreadRunnable.syncExec(composite.getDisplay(), new Result<List<Control>>() { public List<Control> run() 
			{				
				Control[]     controls_Array       = targetComposite.getChildren();					 // get array
				List<Control> controls_NonEditable = Arrays.asList(controls_Array);  			 // convert to list
				List<Control> controls 			   = new ArrayList<Control>(controls_NonEditable); // convert to a list that can be changed (add or remove elements). previous lines are the same as doing List<Control> controls = new ArrayList<Control>(Arrays.asList(composite.getChildren()));
				
				if (recursive)
					for(Control childControl : controls_Array)	
					{
						if (childControl instanceof Composite)
						controls.addAll(controls((Composite)childControl, recursive));
					}
				return controls;
				
			}});
	}
	public <T1> List<T1> controls(Class<T1> clazz)
	{
		return controls(composite, clazz);
	}
	public <T1> List<T1> controls(boolean recursive, Class<T1> clazz)
	{
		return controls(composite, recursive, clazz);
	}
	public <T1> List<T1> controls(Composite targetComposite  , Class<T1> clazz)
	{
		return controls(composite, false, clazz);
	}
	public <T1> List<T1> controls(Composite targetComposite  , boolean recursive, Class<T1> clazz)
	{
		List<Control> controls = controls(targetComposite, recursive);
		List<T1> filtered = new ArrayList<T1>();
		for(Control control : controls)
			if (control.getClass() == clazz)
				filtered.add(clazz.cast(control));
		return filtered;
	}
	public <T1> T1 		 control(Class<T1> clazz)
	{
		return control(composite, clazz);
	}
	public <T1> T1 		 control(boolean recursive, Class<T1> clazz)
	{
		return control(composite, recursive, clazz);
	}
	public <T1> T1 		 control(Composite targetComposite  , Class<T1> clazz)
	{
		return control(composite, false, clazz);
	}
	public <T1> T1 		 control(Composite targetComposite  , boolean recursive, Class<T1> clazz)
	{
		List<Control> controls = controls(targetComposite, recursive);
		for(Control control : controls)
			if (control.getClass() == clazz)
				return clazz.cast(control);
		return null;
	}
}
