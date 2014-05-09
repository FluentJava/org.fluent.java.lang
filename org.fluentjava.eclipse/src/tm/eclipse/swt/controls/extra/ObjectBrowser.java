package tm.eclipse.swt.controls.extra;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;

import tm.eclipse.api.EclipseAPI;
import tm.eclipse.swt.controls.Composite;
import tm.eclipse.swt.controls.SashForm;
import tm.eclipse.swt.controls.Tree;
import tm.eclipse.swt.controls.TreeItem;
import tm.eclipse.ui.views.Eclipse_Panel;
import tm.lang.Reflection;

public class ObjectBrowser extends Composite
{
	Tree 		 tree;
	SashForm 	 sashForm;
	ObjectViewer objectViewer;
	AfterSelect  afterSelect;
	AfterExpand  afterExpand;
	
	public ObjectBrowser(Composite parent) 
	{
		super(parent);
		
		sashForm 	 = this.add.sashForm();
		tree 		 = sashForm.add.tree();
		objectViewer = sashForm.add.objectViewer();
		
		sashForm.weights(1,2);
		
		tree.afterSelected (afterSelect = new AfterSelect());
		tree.onTreeListener(afterExpand = new AfterExpand()); 
		
		this.refresh();		
	}
	
	public class AfterSelect implements Runnable 
	{ 
		public void run() 
		{
			TreeItem selected = ObjectBrowser.this.tree.selected();
			Object   object   = selected.data();
			ObjectBrowser.this.objectViewer.show(object);			
		}
	}
	
	public class AfterExpand implements TreeListener
	{
		public void treeExpanded(TreeEvent e)
	    {			
			if (e.item instanceof TreeItem)
			{
				TreeItem rootTreeItem = (TreeItem)e.item;
				showNodeDetails(rootTreeItem);
			}	
		}

		public void treeCollapsed(TreeEvent e)
		{
		
		}
		
		public ObjectBrowser showNodeDetails(TreeItem rootTreeItem)
		{	
			Object data = rootTreeItem.data();
			if (data == null)
				return ObjectBrowser.this;
			rootTreeItem.nodes_Clear();
			Reflection reflection = new Reflection(data);
			try
			{
				//test showing all methods here
				TreeItem methods = rootTreeItem.add_Node("_methods", null);
				//for(Method method : reflection.methods())
				for(Method method : reflection.methods_Declared())
					methods.add_Node(method.getName(), method);
				
				//next show current fields
				for(java.lang.reflect.Field field : reflection.fields())
				{
					Object value = reflection.field_Value(field);
					TreeItem node = rootTreeItem.add_Node(field.getName(), value);
					if(value == null) 
						node.red();
					else if (value instanceof Boolean == false && value instanceof Long == false)
						node.add_Node("....");
				}
				
				//next show current getters (and its values)
				for(java.lang.reflect.Method method : reflection.methods())
				{
					if (method.getName().startsWith("get"))
					{
						Object value = reflection.invoke(method);
						TreeItem node = rootTreeItem.add_Node(method.getName(), value);
						if(value == null) 
							node.red();
						else if (value instanceof Boolean == false && value instanceof Long == false)
							node.add_Node("....");
					}
				}
			}
			catch(Exception ex)
			{
				tree.add_Node("EXCEPTION: " + ex.getMessage());
			}
			return ObjectBrowser.this;
		}
	}	
	
	public static ObjectBrowser add_ObjectBrowser(org.eclipse.swt.widgets.Composite target)	
	{
		return add_ObjectBrowser(Composite.add_Composite(target));
	}
	public static ObjectBrowser add_ObjectBrowser(final Composite target)
	{
		if (target == null)
			return null;		
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<ObjectBrowser>() { public ObjectBrowser run() 
				{
					ObjectBrowser objectBrowser = new ObjectBrowser(target);												
					return objectBrowser;						
				}});		
	}
	
	public static ObjectBrowser inspect(Object targetObject)
	{
		return show_ObjectBrowser(targetObject);
	}
	public static ObjectBrowser show_ObjectBrowser(Object targetObject)
	{
		Eclipse_Panel view =  EclipseAPI.current().views.create("Object Browser");
		view.clear();
		ObjectBrowser objectBrowser = view.add.panel().add.objectBrowser();
		view.refresh();
		objectBrowser.show(targetObject);
		return objectBrowser;
	}
	
	public ObjectBrowser show(Object object)
	{
		return show(object, true);
	}
	
	public ObjectBrowser show(Object object, boolean expandNode)
	{	
		if (object instanceof Collection<?>)
		{
			Collection<?> collection = (Collection<?>)object;
			for(Object item :  collection)
				show(item,false);
		}
		else if (object!= null && object.getClass().isArray())
		{
			Object[] array = (Object[])object;
			for(Object item :  array)
				show(item,false);
		}
		else
		{
			TreeItem firstNode = tree.add_Node(object); 
			
			if (expandNode)
			{
				this.objectViewer.show(object);
				this.afterExpand.showNodeDetails(firstNode);
				firstNode.expand();				
			}
			else
			{
				firstNode.add_Node("...");
				this.objectViewer.show(null);
			}
		}
		return this;
	}
	

}
