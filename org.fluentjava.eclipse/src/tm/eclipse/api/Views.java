package tm.eclipse.api;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.swtbot.swt.finder.results.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.IViewDescriptor;

import tm.eclipse.helpers.EclipseUI;
import tm.eclipse.ui.views.Eclipse_Panel;

public class Views //extends EclipseBase
{		
	public EclipseAPI eclipse;
	
	public Views(EclipseAPI eclipse)
	{
		this.eclipse = eclipse;
	}
	
	public List<IViewReference> references()
	{
		return syncExec(new Result<List<IViewReference>>() { public List<IViewReference> run ()  
			{
				IViewReference[] items = eclipse.activeWorkbenchPage.getViewReferences();
				return Arrays.asList(items);
				}});
	}
	
	public IViewReference get(final String viewIdTitleOrPartName)
	{
		return reference(viewIdTitleOrPartName);
	}
	public IViewReference reference(final String viewIdTitleOrPartName) 
	{
		return syncExec(new Result<IViewReference>() { public IViewReference run ()  
		{	
			//search using findViewReference
			{
				IViewReference viewReference =  eclipse.activeWorkbenchPage.findViewReference(viewIdTitleOrPartName);
				if (viewReference!=null)
					return viewReference;
			}
			//search by Id
			for(IViewReference viewReference : references())
			{
				String id = viewReference.getId(); 
				if (id.equals(viewIdTitleOrPartName))  
					return viewReference;
			}
			//search by title
			for(IViewReference viewReference : references())
			{
				String title = viewReference.getTitle(); 
				if (title.equals(viewIdTitleOrPartName))  
					return viewReference;				
			}			
			//search by partname
			for(IViewReference viewReference : references())
			{
				String partName = viewReference.getPartName(); 
				if (partName.equals(viewIdTitleOrPartName))  
					return viewReference;				
			}
			
			return null;					
		}});
	}
	
	/*public static IViewReference 		 get(String idPartNameTitle)
	{
		
		for(IViewReference editor : get_Views())
			if(editor.getId().equals(idPartNameTitle) || editor.getPartName().equals(idPartNameTitle)|| editor.getTitle().equals(idPartNameTitle))
					return editor;
		return null;
	}	*/
	public IViewPart 			 open(final String mainId, final String secundaryId)
	{
		return syncExec(new Result<IViewPart>() { public IViewPart run() 
		{
			// first try to use the activeWorkbenchPage showView (which will create a new instance of the provided ID if needed)
			try 
			{
				
				return eclipse.activeWorkbenchPage.showView(mainId, secundaryId, IWorkbenchPage.VIEW_ACTIVATE);
			} 
			catch (PartInitException e) 
			{				
				System.out.println("[open_View] Could not find view: " + mainId);
				return null;
			}
		}});
	}
	public IViewPart 			 open(final String idTitleOrPartName)
	{
		IViewPart viewPart = syncExec(new Result<IViewPart>() { public IViewPart run() 
			{
				// first try to use the activeWorkbenchPage showView (which will create a new instance of the provided ID if needed)
				try 
				{
					return eclipse.activeWorkbenchPage.showView(idTitleOrPartName, null, IWorkbenchPage.VIEW_ACTIVATE);
				} 
				catch (PartInitException e)  { }
				
				//then look at the existing viewReferences and get the view from there (this will not active it)
				IViewReference reference = reference(idTitleOrPartName);
				if (reference!= null)
					return reference.getView(true);
				
				//finally look at the views registered in this eclipse
				for(IViewDescriptor viewDescriptor : available())
					if (viewDescriptor.getId().contains(idTitleOrPartName) || viewDescriptor.getLabel().contains(idTitleOrPartName))
						return open(viewDescriptor.getId());
				return null;
			}});
		return viewPart;
		
		/*try
		{
			return activePage().showView(viewId);
		}
		catch(Exception ex)
		{
			System.out.println("[open_View] Could not find view: " + viewId);
		}
		return null;*/
	}
	public Eclipse_Panel 		 add(String viewID)
	{
		return create(viewID);
	}
	public Class<EclipseUI>		 close_View(String viewIdPartNameTitle)
	{
		eclipse.views.close(reference(viewIdPartNameTitle));		
		return EclipseUI.class;
	}
	public <T extends IViewReference>T close(final T viewToClose)
	{
		if (viewToClose!=null)			 
			close(open(viewToClose.getId()));
		return viewToClose;
	}	
	
	public <T extends IViewPart>T close(final T viewToClose)
	{
		if (viewToClose != null)
		{
			syncExec(new VoidResult() { public void run() 
				{			
					EclipseAPI.current().activeWorkbenchPage.hideView(viewToClose);
				}});
		}
		return viewToClose;
	}	

	public List<IViewReference>   get_Views()
	{
		return Arrays.asList(eclipse.activeWorkbenchPage.getViewReferences());
	}
	public boolean				  hasView(String viewId)
	{
		return view(viewId) != null;
	}
	public IViewReference 		  view(String title)
	{
		return reference(title);
	}	
	public List<IViewReference>   list()
	{
		return get_Views();
	}
	public List<String>           ids()
	{
		List<String> ids = new ArrayList<String>();
		for(IViewReference view : this.list())
			ids.add(view.getId());
		return  ids;
	}
	public List<String>           partNames()
	{
		List<String> ids = new ArrayList<String>();
		for(IViewReference view : this.list())
			ids.add(view.getPartName());
		return  ids;
	}
	public List<String>           titles()
	{
		List<String> titles = new ArrayList<String>();
		for(IViewReference view : this.list())
			titles.add(view.getTitle());
		return  titles;
	}
	public  Eclipse_Panel 		  create(String viewId)
	{
		return eclipse.panelFactory.open_Panel(viewId);
	}

	public List<IViewDescriptor>  available()
	{
		return eclipse.registry.views();
	}
	public List<String>           available_Ids()
	{
		return eclipse.registry.views_Ids();
	}
	public List<String>           available_Labels()
	{
		return eclipse.registry.views_Labels();
	}
}
