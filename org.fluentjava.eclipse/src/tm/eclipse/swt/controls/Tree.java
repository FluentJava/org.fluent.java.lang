package tm.eclipse.swt.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.Control_Self;
import tm.eclipse.swt.Control_Set;

public class Tree extends org.eclipse.swt.widgets.Tree
{		
	public Display display;
	public Control_Set <Tree>        set;
	public Control_Self<Tree>	    self;
	
	public Tree(Composite parent, int style) 
	{		
		super(parent, style);
		this.display = parent.getDisplay();			// we need to store this in case there are multiple ones
		this.set  = new Control_Set <Tree>(this);
		this.self = new Control_Self<Tree>(this);
	}	
	
	public static Tree add_Tree(final Composite target)
	{
		return add_Tree(target, SWT.None);
	}
	public static Tree add_Tree(final Composite target, final int style)
	{	
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Tree>() { public Tree run() 
					{
						Tree tree = new Tree(target, style);
						target.layout(true);
						return tree;
					}});
		
	}
	public Tree add_Nodes(final Object...nodes)
	{
		return add_Nodes(Arrays.asList(nodes));
		
	}
	public Tree add_Nodes(final List<Object> nodes)
	{
		for(Object node : nodes)
			add_Node(node);
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
	public TreeItem add_Node(final Object node, final Object data, Image image)
	{		  
		return TreeItem.newInstance(this,node, data).image(image);
	}
	public TreeItem add_Node(final Object node, final Object data, boolean addDummyNode)
	{		
		TreeItem treeItem = add_Node(node,data);
		if (addDummyNode)
			treeItem.add_Node("...");
		return treeItem;		
	}
	public Tree fill()
	{
		return set.layout.grid_Fill();	
	}
	public Object data(final int index)
	{
		return UIThreadRunnable.syncExec(display,new Result<Object>() { public Object run()
		{
			TreeItem node = node(index);
			if(node != null)
				return node.getData();
			return null;				
		}});
	}
	public TreeItem node(final int index)
	{
		return UIThreadRunnable.syncExec(display,new Result<TreeItem>() { public TreeItem run()
			{
				if (index < 0 || index >= getItemCount())
					return null;
				return (TreeItem)Tree.this.getItem(index);				
			}});	
	} 
	public List<TreeItem> nodes()
	{
		return get_Nodes();
	}
	public Tree nodes_Clear()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
			{			
				for(Object treeItem : Tree.this.getItems())
					if (treeItem instanceof TreeItem)
						((TreeItem)treeItem).dispose();		
			}});
		return this;
	}
	public List<TreeItem> get_Nodes()
	{
		return UIThreadRunnable.syncExec(display,new Result<List<TreeItem>>() { public List<TreeItem> run()
			{
				// need to cast org.eclipse.swt.widgets.TreeItem into TreeItem , ie we can't just do: return Arrays.asList(Tree.this.getItems());
				List<TreeItem> treeItems = new ArrayList<TreeItem>(); 
 				for(Object treeItem : Tree.this.getItems())
 					if(treeItem instanceof TreeItem)
 						treeItems.add((TreeItem)treeItem);
 				return treeItems;
			}});	
	} 
	
	public Tree afterSelected(final Runnable runnable)
	{
		return onSelection(runnable);
	}
	public Tree onSelection(final Runnable runnable)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
			{	
				Tree.this.addSelectionListener(new SelectionAdapter() {
					  @Override
					  public void widgetSelected(SelectionEvent e) 
					  {
						  UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
							{		
							  runnable.run();
							}});
					  }});
			}});
		return this;
	}
	
	public Tree onTreeListener(final TreeListener treeListener)
	{			
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
			{	
				Tree.this.addTreeListener(treeListener);		
			}});
		
		return this;		
	}	
	public Tree select(final int index)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
			{
				TreeItem treeItem = node(index);
				if (treeItem!= null)
					setSelection(treeItem);
			}});
		return this;
	}
	public TreeItem selected()
	{
		return UIThreadRunnable.syncExec(display,new Result<TreeItem>() { public TreeItem run()
			{
				List<TreeItem> items = selection();
				if (items!=null && items.size() >0)
					return items.get(0);
				return null;
			}});		
	}
	public List<TreeItem> selection()
	{
		return UIThreadRunnable.syncExec(display,new Result<List<TreeItem>>() { public List<TreeItem> run()
			{
				List<TreeItem> selectedItems = new ArrayList<TreeItem>();
				for(Object treeItem : Tree.this.getSelection())
					if(treeItem instanceof TreeItem)
						selectedItems.add((TreeItem)treeItem);
				return selectedItems;
			}});	
	}
	protected void checkSubclass()
	{}
}
