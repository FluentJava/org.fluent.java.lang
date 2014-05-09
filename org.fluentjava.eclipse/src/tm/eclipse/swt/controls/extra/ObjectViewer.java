package tm.eclipse.swt.controls.extra;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;



//import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.eclipse.api.EclipseAPI;
import tm.eclipse.swt.controls.Composite;
import tm.eclipse.swt.controls.Label;
import tm.eclipse.swt.controls.Table;
import tm.eclipse.swt.controls.Text;
import tm.eclipse.ui.views.Eclipse_Panel;
import tm.lang.Reflection;

public class ObjectViewer extends Composite 
{
	public Object objectShown;
	public Label  className;
	public Label  superClassName;
	public Text  toString;
	public Table  table;	
	
	public ObjectViewer(Composite parent) 
	{
		super(parent);
		this.set.layout.grid(8);
						 	  this.add.label("Class:").bold();
		this.className 	    = this.add.label("");
							  this.add.label("SuperClass:").bold();
		this.superClassName = this.add.label("");
	 	  					  this.add.label("toString:").bold();		
		this.toString 		= this.add.text ("").set.layout.grid_Grab_Horizontal();

		this.table 			= this.add.table().fill(6);			
	}
	public static ObjectViewer add_ObjectViewer(org.eclipse.swt.widgets.Composite target)	
	{
		return add_ObjectViewer(Composite.add_Composite(target));
	}
	public static ObjectViewer add_ObjectViewer(final Composite target)
	{
		if (target == null)
			return null;		
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<ObjectViewer>() { public ObjectViewer run() 
				{
					ObjectViewer objectViewer = new ObjectViewer(target);												
					return objectViewer;						
				}});		
	}
	
	public static ObjectViewer show_ObjectViewer(Object targetObject)
	{
		Eclipse_Panel view =  EclipseAPI.current().views.create("Object Viewer");
		view.clear();
		ObjectViewer objectViewer = view.add.panel().add.objectViewer();
		view.refresh();
		objectViewer.show(targetObject);
		return objectViewer;
	}
	
	public <T> Table add_Object_Fields_as_Columns(List<T> list)
	{
		if (list.isEmpty() == false)
		{
			T first = list.get(0);
			add_Object_Fields_as_Columns(first);
		}
		return this.table;
	}
	public Table add_Object_Fields_as_Columns(Object object)
	{
		return table.columns(new tm.lang.Reflection(object).fields_Names());
	}
	public <T> Table add_Object_Getters_as_Columns(List<T> list)
	{
		if (list.isEmpty() == false)
		{
			T first = list.get(0);
			add_Object_Getters_as_Columns(first);
		}
		return this.table;
	}
	public Table add_Object_Getters_as_Columns(Object object)
	{
		return table.columns(new tm.lang.Reflection(object).getters_Names());
	}
	
	public <T> Table add_Object_Getters_Values_as_Rows(List<T> list)
	{
		for(Object listItem : list)
			add_Object_Getters_Values_as_Row(listItem);
		return this.table;
	}
	public Table add_Object_Getters_Values_as_Row(Object object)
	{
		List<String> values = new tm.lang.Reflection(object).getters_Values();
		return table.item(values);
	}
	
	public <Q> Table show(Q object)
	{
		table.columns("Name", "Value", "Type", "Declarer","Origin");
		table.column_Width(0,100);				
		table.items_Clear();
		
		if (object == null)
		{
			className.text("NULL Object");
			superClassName.text("");
			toString.text("");
			return this.table;
		}
		className.text(object.getClass().getName());
		superClassName.text(object.getClass().getSuperclass().getName());
		toString.text(object.toString());
		
		Reflection reflection = new Reflection(object);
		

		for(Field field : reflection.fields())
		{
			Object value = reflection.field_Value(field);
			if (value == null)
				value = "";
			table.add_Row(field.getName() , value, field.getType().getName(), field.getDeclaringClass(), "field");
		}
		for(Method method : reflection.methods())
		{
			if (method.getName().startsWith("get"))
			{		
			    Object value = reflection.invoke(method);
				if (value == null)
					value = "";
				table.add_Row(method.getName().substring(3), 
							  value, 
							  method.getReturnType(), 
							  method.getDeclaringClass(), 
							  "method: " + method.getName()); 
			}
		}
		return this.table.refresh();
	}
	public <Q> Table show(List<Q> list)
	{		
		if(list != null && list.size() > 0)
		{			
			Object firstItem = list.toArray()[0];
			className.text(firstItem.getClass().getName());
			superClassName.text("");
			toString.text("");//list.toString());
			this.add_Object_Getters_as_Columns(list);
			this.add_Object_Getters_Values_as_Rows(list);
		}
		return this.table.refresh();
	}

}
