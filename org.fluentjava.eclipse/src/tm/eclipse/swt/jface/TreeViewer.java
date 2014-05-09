package tm.eclipse.swt.jface;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import tm.eclipse.swt.controls.Tree;
import tm.eclipse.swt.controls.TreeItem;

public class TreeViewer extends org.eclipse.jface.viewers.TreeViewer
{
	public Display   display;
	public Composite target;
	public Tree      tree;
		
	public TreeViewer(Composite parent)
	{		
		this(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);		
	}
	public TreeViewer(Composite parent, int style) 
	{		
		this(new Tree(parent, style));				
	}	
	public TreeViewer(Tree tree)
	{
		super(tree);
		this.tree = tree;
		this.target  = tree.getParent();
		this.display = this.target.getDisplay();		
	}
	
	public static TreeViewer add_TreeViewer(final Composite target)
	{				
		if (target == null)
			return null;		
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<TreeViewer>() { public TreeViewer run() 
					{						
						return new TreeViewer(target);						
					}});
	}
	
	public TreeViewer add_Nodes(final Object...nodes)
	{
		tree.add_Nodes(nodes);
		return this;
	}
	public TreeViewer add_Nodes(final List<Object> nodes)
	{
		tree.add_Nodes(nodes);
		return this;
	}
	
	public TreeItem add_Node(final Object node)
	{
		return tree.add_Node(node);
	}
	public TreeItem add_Node(final Object node, final Object data)
	{				
		return tree.add_Node(node, data);		
	}
	
	public List<TreeItem> nodes() 
	{
		return tree.nodes();					
	}
	
	public TreeViewer onDoubleClick(final Runnable runnable)
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
			{	
				TreeViewer.this.addDoubleClickListener(new IDoubleClickListener() {
				  @Override
				  public void doubleClick(DoubleClickEvent event) 
				  {
					  UIThreadRunnable.syncExec(display,new VoidResult() { public void run()
						{		
						  runnable.run();
						}});
				  }});
			}});
		return this;
	}
	public TreeViewer onSelection(final Runnable runnable)
	{
		tree.onSelection(runnable);		
		return this;
	}

	public TreeItem selected()
	{
		return tree.selected();
	}
}
