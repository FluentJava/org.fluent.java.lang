package tm.eclipse.swt.controls;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.Control_Set;
import tm.eclipse.swt.Control_Self;

public class Text extends org.eclipse.swt.widgets.Text
{	
	public Display 			 		display;
	public Control_Set<Text>        set;
	public Control_Self<Text>	    self;
	
	public Text(Composite parent, int style) 
	{		
		super(parent, style);
		display = parent.getDisplay();			// we need to store this in case there are multiple ones
		set     = new Control_Set<Text>(this);
		self    = new Control_Self<Text>(this);
	}
	
    public ControlDecoration controlDecoration;
	public static Text add_Text(final Composite target)
	{
		return add_Text(target, SWT.BORDER);
	}
	public static Text add_Text(final Composite target, int style)
	{
		return add_Text(target,style, null);
	}
	
	public static Text add_Text(final Composite target, final int style, final String textText)
	{	
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Text>() { public Text run() 
					{
						Text text = new Text(target,style);
						target.layout(true);
						if (textText != null)
							text.setText(textText);
						return text;
					}});
		
	}	
	public static Text add_Text(final Composite target, String text)
	{
		return add_Text(target, SWT.BORDER, text);
	}
	public static Text add_Text_MultiLine(final Composite target, String text)
	{
		return add_Text(target, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL, text);
	}
	
	public static Text add_Text_Search(final Composite target)
	{
		return add_Text(target, SWT.SEARCH | SWT.ICON_SEARCH | SWT.CANCEL | SWT.BORDER);
	}	
	
	
	protected void checkSubclass()
	{}
		
	public Text controlDecoration_Set(final String text, final boolean errorImage)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				String imageId = (errorImage) ? FieldDecorationRegistry.DEC_ERROR : FieldDecorationRegistry.DEC_INFORMATION;
				Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(imageId).getImage();
				ControlDecoration controlDecoration = new ControlDecoration(Text.this,SWT.TOP | SWT.LEFT);
				Text.this.controlDecoration = controlDecoration;
				controlDecoration.setDescriptionText(text);
				controlDecoration.setImage(image);
				controlDecoration.setShowOnlyOnFocus(false);
				controlDecoration.setMarginWidth(2);
				controlDecoration.hide();
			}});
		return this;
	}
	public Text controlDecoration_Show()
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				if (Text.this.controlDecoration != null);
					Text.this.controlDecoration.show();
			}});
		return this;
	}
	public Text controlDecoration_Hide()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				if (Text.this.controlDecoration != null);
					Text.this.controlDecoration.hide();
			}});
		return this;
	}		
	public Text controlDecoration_Validate_Url(final String message)
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Text.this.controlDecoration_Set(message, true);
				Text.this.addModifyListener(new ModifyListener() 
					{			
					    public void modifyText(ModifyEvent e) 
						{
					    	try
							{
							 	URL url = new URL(Text.this.text());
								if (url.getHost().equals("") == false)
								{									
									Text.this.controlDecoration_Hide();	
									return;
								}
							} 
						    catch (MalformedURLException ex)  { }
						 
							Text.this.controlDecoration_Show();
						}
					});
			}});
		return this;
	}
	public Text disable()
	{
		return enabled(false);
	}
	public Boolean disabled()
	{
		return enabled() == false;
	}
	public Text enable()
	{
		return enabled(true);
	}
	public Boolean enabled()
	{
		return UIThreadRunnable.syncExec(display,new Result<Boolean>() { public Boolean run() 
		{
			return Text.super.isEnabled();			
		}});
	}
	public Text enabled(final boolean value)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Text.super.setEnabled(value);			
		}});
		return this;
	}
	
	@Override
	public String getText()
	{
		return UIThreadRunnable.syncExec(display,new Result<String>() { public String run() 
		{
			return Text.super.getText();			
		}});		
	}
	public boolean isEmpty()
	{
		return this.getText().equals("");
	}
	public Text fill()
	{
		return fill(1);
	}
	public Text fill(int horizontalSpan)
	{
		set.layout.grid_Grab_All(horizontalSpan,1);
		return this;
	}
		/*return layout_Fill(true,true);
	}
	public Text layout_Fill(final boolean grabExcessHorizontalSpace, final boolean grabExcessVerticalSpace)
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			GridData gridData = new GridData(SWT.FILL , SWT.FILL, grabExcessHorizontalSpace,grabExcessVerticalSpace);
			Text.this.setLayoutData(gridData);	
			Text.this.getParent().layout(true);
		}});
		return this;
	}*/
	@Override
	public void setText(final String text)
	{
		if (text != null)
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				Text.super.setText(text);			
			}});
	}	

	public String text()
	{
		return getText();
	}
	public Text text(String text)
	{
		setText(text);
		return this;
	}
	public int width()
	{
		return UIThreadRunnable.syncExec(display,new Result<Integer>() { public Integer run() 
		{
			return Text.super.getSize().x;			
		}});
	}
	public Text width(final int width)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Point size = Text.this.getSize();
			size.x = width;			
			Text.this.setSize(size);			
		}});
		return this;
	}
	
	/*public int height()
	{
		return UIThreadRunnable.syncExec(display,new Result<Integer>() { public Integer run() 
		{
			return Text.super.getSize().y;			
		}});
	}
	public Text height(final int height)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Point size = Text.this.getSize();
			size.y = height;			
			Text.this.setSize(size);			
		}});
		return this;
	}*/
}
