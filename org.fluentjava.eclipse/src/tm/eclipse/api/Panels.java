package tm.eclipse.api;

import static tm.eclipse.helpers.log.*;
import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.UUID;

import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.StringUtils;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.swt.widgets.Composite;
import tm.eclipse.Plugin_Config;
import tm.eclipse.ui.views.DefaultPart_WebBrowser;
import tm.eclipse.ui.views.Eclipse_Panel;

public class Panels  extends EclipseBase
{		
	Eclipse_Panel panel;
	
	public Panels(IWorkbench workbench)
	{
		super(workbench);	
	}

	public Eclipse_Panel open_Panel()
	{
		return open_Panel("");
	}
	public Eclipse_Panel open_Panel(final String panelId)
	{	
		return syncExec(new Result<Eclipse_Panel>() { public Eclipse_Panel run() 
			{				
				try 
				{	
					String secondaryId = (StringUtils.isEmptyOrNull(panelId))
											? UUID.randomUUID().toString()
											: panelId;
					Eclipse_Panel panel = (Eclipse_Panel)activePage.showView(Eclipse_Panel.ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE);
					panel.title(panelId);
					return panel;
				} catch (PartInitException e) 
				{
					e.printStackTrace();
					return null;
				}				
			}});
	}
	
	public Composite refresh(final Composite composite)
	{				
		//workbench.getDisplay().asyncExec(new Runnable() { public void run() 
		return syncExec(new Result<Composite>() { public Composite run()
	 		{
				composite.layout();
				return composite; 
	        }});
		
	}
	
	public DefaultPart_WebBrowser open_Url_in_WebBrowser(final String browserId, final String urlToOpen)
	{	
		return syncExec(new Result<DefaultPart_WebBrowser>() { public DefaultPart_WebBrowser run() 
			{
				try 
				{
					IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
					DefaultPart_WebBrowser webBrowserPart = (DefaultPart_WebBrowser)page.showView(DefaultPart_WebBrowser.ID, browserId, IWorkbenchPage.VIEW_ACTIVATE);
					webBrowserPart.browser.setUrl(urlToOpen);
					webBrowserPart.setName(urlToOpen);
					return webBrowserPart;			
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					return null;
				}		
			}});
	}
	public DefaultPart_WebBrowser open_Html_in_WebBrowser(String htmlToShow)
	{		
		return open_Html_in_WebBrowser(Plugin_Config.SWT_DEFAULT_BROWSER_ID, Plugin_Config.SWT_DEFAULT_BROWSER_TITLE, htmlToShow);
	}
	public DefaultPart_WebBrowser open_Html_in_WebBrowser(final String browserId, final String title, final String htmlToShow)
	{	
		return syncExec(new Result<DefaultPart_WebBrowser>() { public DefaultPart_WebBrowser run() 
			{
				try 
				{	
					if (activePage == null)				
					{
						error("in open_Html_in_WebBrowser, activePage was null, which usually means that the caller was not on the UI thread");
						return null;
					}
					IViewPart viewPart = activePage.showView(DefaultPart_WebBrowser.ID, browserId, IWorkbenchPage.VIEW_ACTIVATE);
					
					DefaultPart_WebBrowser webBrowserPart = (DefaultPart_WebBrowser)viewPart;		
					webBrowserPart.setName(title);			
					webBrowserPart.browser.setText(htmlToShow);
					return webBrowserPart;						
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					return null;
				}				
			}});		
	}

}
