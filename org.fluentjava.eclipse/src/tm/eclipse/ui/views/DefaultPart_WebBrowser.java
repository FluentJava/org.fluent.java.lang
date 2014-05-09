package tm.eclipse.ui.views;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import tm.eclipse.swt.controls.Browser;

public class DefaultPart_WebBrowser extends ViewPart 
{
	public static final String ID = "tm.eclipse.ui.views.DefaultPart_WebBrowser";
	
	public static final String DEFAULT_HTML = "<html>" + 
    										  "<link href='http://getbootstrap.com/dist/css/bootstrap.css' rel='stylesheet'>" + 
    										  "<h1 class='alert alert-success'>Empty Browser</h1>Humm, it looks like your Eclipse crashed and left this window behind." +
    										 "<br/><br/>Please close this window" +
    										  "</html>";
	public  Browser browser;
	
	public void createPartControl(Composite parent) 
	{
		//MyEditor myEditor = new MyEditor();
		//myEditor.createPartControl(parent);
		
		//SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
	    //sashForm.setLayout(new RowLayout());
		
	    browser  = new Browser(parent,SWT.BORDER);
	    
	    browser.setText(DEFAULT_HTML);	    
	    
	    //browser.webBrowser.setCookie("Session=9e78f231-106b-4f73-a10f-22ab9ebee435","https://teammentor.net");
	    
	    //webBrowser.setUrl("https://teammentor.net/article/7d647e95-e47f-42e3-bb84-fd0dd727245c");
	    //webBrowser.setUrl("http://www.google.com");
	}
	public void setFocus() 
	{
	}
	public void setName(String title)
	{
		setPartName(title);
	}
}