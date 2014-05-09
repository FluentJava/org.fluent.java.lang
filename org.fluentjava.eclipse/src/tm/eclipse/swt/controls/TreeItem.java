package tm.eclipse.swt.controls;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.helpers.colors;
import tm.eclipse.swt.Control_Self;
import tm.eclipse.swt.Control_Set;
import tm.lang.Reflection;



public class TreeItem extends org.eclipse.swt.widgets.TreeItem
{
	public Tree tree;		
	public TreeItem(Tree tree, int style) 
	{
		super(tree, style);
		this.tree = tree;		
	}
	public TreeItem(TreeItem treeItem, int style) 
	{
		super(treeItem,style);
		tree = treeItem.tree;		
	}
	
	public static TreeItem newInstance(final Tree tree, final Object text, final Object data)
	{		
		return UIThreadRunnable.syncExec(tree.display,new Result<TreeItem>() { public TreeItem run()
		{
			TreeItem treeItem = new TreeItem(tree,SWT.NONE);
			return treeItem.set(text,data);
		}});	
	}
	public static TreeItem newInstance(final TreeItem parentTreeItem, final Object text, final Object data)
	{		
		return UIThreadRunnable.syncExec(parentTreeItem.tree.display,new Result<TreeItem>() { public TreeItem run()
		{
			TreeItem treeItem = new TreeItem(parentTreeItem,SWT.NONE);
			return treeItem.set(text,data);
		}});	
	}
	public TreeItem set(final Object text, final Object data)
	{
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{				
				TreeItem.this.setData(data);
				TreeItem.this.setText(text == null ? "" : text.toString());							
				
			}});
		return this;
	}
	public TreeItem add_Node(final Object node)
	{
		return add_Node(node, node);
	}
	public TreeItem add_Node(final Object node, final Object data)
	{		
		return TreeItem.newInstance(this,node, data);
	}
	public TreeItem add_Nodes(final Object...nodes)
	{
		return add_Nodes(Arrays.asList(nodes));
		
	}
	public TreeItem add_Nodes(final List<Object> nodes)
	{
		for(Object node : nodes)
			add_Node(node);
		return this;
	}
	
	public List<TreeItem> nodes()
	{
		return UIThreadRunnable.syncExec(tree.display,new Result<List<TreeItem>>() { public List<TreeItem> run()
			{
				List<TreeItem> nodes = new ArrayList<TreeItem>();
				for(Object treeItem : TreeItem.this.getItems())
					if (treeItem instanceof TreeItem)
						nodes.add((TreeItem)treeItem);
				return nodes;
			}});
	}
	
	public TreeItem nodes_Clear()
	{
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{			
				for(Object treeItem : TreeItem.this.getItems())
					if (treeItem instanceof TreeItem)
						((TreeItem)treeItem).dispose();		
			}});
		return this;
	}
 	public TreeItem expand()
	{		
		return expanded(true);
	}
	public TreeItem colapse()
	{
		return expanded(false);
	} 
	public TreeItem expanded(final boolean expanded)
	{
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{			
				// this will triger the visual expansion of the treenode 
				TreeItem.this.setExpanded(expanded);
			
				// we also need to invoke this internal method so that the Tree's onTreeListener event fires
/*				Reflection reflection = new Reflection(TreeItem.this);				
				Method method = reflection.method("sendExpand", boolean.class, boolean.class);
				reflection.invoke(method, expanded,false);
*/
 														
			}});
		return this;
	}
		
	public TreeItem image(final ImageData imageData)
	{
		Image image = new Image(tree.display,imageData); 
		return image(image);
	}
	
	
	public TreeItem image(final Image image)
	{			
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{	
				TreeItem.this.setImage(image);		
			}});
		
		return this;		
	}
	public TreeItem select()
	{
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{			
				TreeItem.this.tree.setSelection(TreeItem.this);
			}});
		return this;
	};
	public Object data()
	{
		return UIThreadRunnable.syncExec(tree.display,new Result<Object>() { public Object run()
			{			
				return TreeItem.this.getData();
			}});		
	};
	public String text()
	{
		return UIThreadRunnable.syncExec(tree.display,new Result<String>() { public String run()
			{			
				return TreeItem.this.getText();
			}});		
	};
	public TreeItem red()
	{
		return foreground(colors.red());
	}
	public TreeItem gray()
	{
		return foreground(colors.gray());
	}
	public TreeItem background(final Color color)
	{
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{			
				TreeItem.this.setBackground(color);
			}});
		return this;
	}
	public TreeItem foreground(final Color color)
	{
		UIThreadRunnable.syncExec(tree.display,new VoidResult() { public void run()
			{			
				TreeItem.this.setForeground(color);
			}});
		return this;
	}
	protected void checkSubclass()
	{}
}
