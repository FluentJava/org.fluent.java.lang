package tm.eclipse.swt.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotBrowser;
import org.eclipse.ui.part.ViewPart;

import tm.lang.Reflection;


public class Browser extends org.eclipse.swt.browser.Browser
{			
	public Display 		 	display;
	public Composite     	parent;
	public ViewPart		 	viewPart;
	public SWTBotBrowser 	swtBotBrowser;
	public Reflection		reflection;
	public LocationListener locationListener;
	
	public Browser(Composite parent, int style) 
	{				
		super(parent, style);
		display         = parent.getDisplay();
		this.parent     = parent;
		swtBotBrowser   = new SWTBotBrowser(this);
		this.reflection = new Reflection(this);
	} 
	public static Browser add_Browser(final Composite target)
	{
		return add_Browser(target, null);
	}
	public static Browser add_Browser(final Composite target, final ViewPart _viewPart)
	{	
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Browser>() { public Browser run() 
					{
						Browser browser = new Browser(target,SWT.None);
						browser.viewPart   = _viewPart;
						target.layout(true);
						return browser;
					}});
		
	}
	
	//need to overwrite this, or the version in browser.checkSubClass will throw an exception
	protected void checkSubclass()
	{
	
	}
	
	public String getText()
	{			
		// this call to SWTUtils.isUIThread() is not pretty but seems to do the trick 
		// the problem was calling super.setText(text) inside the IThreadRunnable.syncExec
		if(SWTUtils.isUIThread())
			return super.getText();
		return UIThreadRunnable.syncExec(display,new Result<String>() { public String run() 
									{
										return getText();  		
									}});		
	}

	public boolean setText(final String text)
	{			
		return UIThreadRunnable.syncExec(display,new Result<Boolean>() { public Boolean run() 
									{
										return Browser.super.setText(text);
									}});		
	}
	public Browser open(String url)
	{
		swtBotBrowser.setUrl(url);
		swtBotBrowser.waitForPageLoaded();
		return this;
	}
	
	/*public List<LocationListener> locationListeners()
	{
		Object webBrowser = reflection.field_Value("webBrowser");  
		if (webBrowser != null)
		{
			
		}
		//LocationListener[] locationListeners = browser.lo
		return null;
	}*/
	public Browser onLocationChange(final LocationListener locationListener)
	{		
		this.locationListener = locationListener;
		UIThreadRunnable.syncExec(display, new VoidResult() { public void run()
			{				
				Browser.this.addLocationListener(Browser.this.locationListener);
			}});
		return this;
	}
}
