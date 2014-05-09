package tm.eclipse.api;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import tm.eclipse.ui.views.Eclipse_Panel;
import tm.eclipse.ui.views.DefaultPart_WebBrowser;

public class Eclipse_Window extends EclipseAPI
{
	public Eclipse_Panel panel;
	public String    panelId; 
	
	public Eclipse_Window() throws PartInitException 
	{
		panelId = String.valueOf(Math.abs(new Random().nextInt()));
		createWindow();
	}
	public Eclipse_Window(String _panelId) throws PartInitException
	{		
		panelId = _panelId;		
		createWindow();
	}
	
	private Eclipse_Window createWindow() throws PartInitException
	{
		panel = (Eclipse_Panel)activeWorkbenchPage.showView(Eclipse_Panel.ID, panelId, IWorkbenchPage.VIEW_ACTIVATE);
		title(panelId);
		return this;
	}
	public String title()
	{
		return panel.getPartName();
	}
	public Eclipse_Window title(String title)
	{
		panel.title(title);
		return this;		
	}

	public List<Control> children()
	{
		return Arrays.asList(panel.composite.getChildren());
	}
	
	public Browser add_WebBrowser()
	{
		Browser browser  = new Browser(panel.composite,SWT.BORDER);
	    
	    browser.setText(DefaultPart_WebBrowser.DEFAULT_HTML);	    
	    panel.composite.layout();									//redraw doesn't work
	    return browser;
	}
	
	public Tree add_TreeView()
	{
		Tree tree  = (Tree)new TreeViewer(panel.composite).getTree();
		panel.composite.layout();
	    return tree;
	}
	public Eclipse_Window clear()
	{		
		for(Control control : children())
		{
			control.dispose();
		}
		return this;
	}
		
	
	public static TreeItem add_Node(Tree tree, String text)
	{
		TreeItem treeItem = new TreeItem(tree, SWT.NONE);
		treeItem.setText(text);
		return treeItem;
	}
	
	
	
}
