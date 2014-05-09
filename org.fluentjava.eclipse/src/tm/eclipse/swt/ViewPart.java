package tm.eclipse.swt;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.swt.controls.Button;
import tm.eclipse.swt.controls.Composite;
import tm.lang.Reflection;

public abstract class ViewPart extends org.eclipse.ui.part.ViewPart
{
	public Display					display;
	public Composite 				composite;
	public Mouse 					mouse;
	public Composite_Add<Composite> add;
	public Control_Get  <Composite> get;
	public Control_Set  <Composite> set;
	//public Control_Self <Composite> self;
	public Reflection   		    reflection;
	
	@Override
	public void createPartControl(org.eclipse.swt.widgets.Composite parent) 
	{		
		composite  = new Composite(parent);
		display    = composite.display;
		mouse 	   = composite.mouse;
		add   	   = composite.add;
		set   	   = composite.set;
		get   	   = composite.get;
		//self  	   = composite.self;
		reflection = new Reflection(this); 
	}

	@Override
	public void setFocus() { }
	
	public void title(final String title)
	{
		UIThreadRunnable.syncExec(composite.getDisplay(), new VoidResult() { public void run()
			{
				ViewPart.super.setPartName(title);
			}});
		
	}
	
	public ViewPart clear()
	{
		composite.self.clear();
		return this;			
	}
	public ViewPart close()
	{		
		UIThreadRunnable.syncExec(composite.getDisplay(), new VoidResult() { public void run() 
			{
				ViewPart.this.getSite().getPage().hideView(ViewPart.this);
			}});
		return this;
	}
	public ViewPart close_InSeconds(final int seconds)
	{
		return close_InMiliSeconds(seconds * 1000);
	}
	public ViewPart close_InMiliSeconds(final int miliSeconds)
	{
		Thread thread_toClose = new Thread(new Runnable() { public void run()
			{		
				try 
				{
					Thread.sleep(miliSeconds);
				}
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				close();		
			}});
		thread_toClose.start();		
		return this;
	}
	public List<Control> controls()
	{
		return composite.self.controls();	
	}	
	public <T> T 		 control(Class<T> clazz)
	{
		return composite.self.control(clazz);	
	}
	
	public Layout 		 layout()
	{
		return UIThreadRunnable.syncExec(composite.getDisplay(), new Result<Layout>() { public Layout run()
			{
				return composite.getLayout();
			}});
	}
	
	public ViewPart layout(final Layout newLayout)
	{
		UIThreadRunnable.syncExec(composite.getDisplay(), new VoidResult() { public void run()
			{
				composite.setLayoutData(null);
				composite.setLayout(newLayout);
				composite.layout(true);	
			}});
		return this;
	}
	
/*	public Eclipse_Panel layout_Fill(final Layout newLayout)
	{
		return layout(new FillLayout());
	}
	public Eclipse_Panel layout_Form(final Layout newLayout)
	{
		return layout(new FormLayout());
	}
	public Eclipse_Panel layout_Grid(final Layout newLayout)
	{
		return layout(new GridLayout());
	}
	public Eclipse_Panel layout_Row(final Layout newLayout)
	{
		return layout(new RowLayout());
	}	*/
	public ViewPart sleep(int miliseconds)
	{
		try 
		{
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) 
		{ 
			e.printStackTrace();
		}
		return this;
	}
	public ViewPart refresh()
	{
		UIThreadRunnable.syncExec(composite.getDisplay(), new VoidResult() { public void run() 
					{
						composite.layout(true);
					}});
		return this;
	}
	public ToolBarManager toolBar_Manager()
	{
		IToolBarManager toolBarManager = this.getViewSite().getActionBars().getToolBarManager();
		if(toolBarManager instanceof ToolBarManager)
			return (ToolBarManager)toolBarManager;
		return null;
	}
	public ViewPart toolBar_Clear()
	{
		toolBar_Manager().removeAll();		
		return this;
	}
	public List<IContributionItem> toolBar_Items()
	{
		return Arrays.asList(toolBar_Manager().getItems());			  
	}
	public ToolItem toolBar_add_Button(String text)
	{
		return toolBar_add_Button(text,null,null);
	}
	public ToolItem toolBar_add_Button(Image image)
	{
		return toolBar_add_Button(null,image,null);
	}
	public ToolItem toolBar_add_Button(final String text, final Image image)
	{
		return toolBar_add_Button(text,image,null);
	}
	public ToolItem toolBar_add_Button(final String text, final Runnable runnable)
	{
		return toolBar_add_Button(text, null, runnable);
	}
	public ToolItem toolBar_add_Button(final Image image, final Runnable runnable)
	{
		return toolBar_add_Button(null, image, runnable);
	}	
	public ToolItem toolBar_add_Button(final String text, final Image image, final Runnable runnable)
	{
		return UIThreadRunnable.syncExec(composite.getDisplay(), new Result<ToolItem>() { public ToolItem run() 
			{						
				//rewrite this so that we dont need to use the AddToolItem to get a reference to the new ToolItem added
				class AddToolItem 
				{
					public ToolItem toolItem;
					public void run(ToolBar parent)
					{
						toolItem = new ToolItem(parent, SWT.PUSH);
						if (text != null)
							toolItem.setText(text);
						if (image != null)
							toolItem.setImage(image);
						
						if (runnable != null)
							toolItem.addListener(SWT.Selection, new Listener() { public void handleEvent(Event event) 
								{
									runnable.run();
								}});
					}
				}
				final AddToolItem addToolItem = new AddToolItem(); 
				IContributionItem contributionItem = new ContributionItem() 
					{						
						@Override
						public void fill(ToolBar parent, int index) 
						{
							addToolItem.run(parent);
					    }
					};
					toolBar_Manager().add(contributionItem);
					toolBar_Update();
					
					return addToolItem.toolItem;
					
					// this doesn't percists after toolBar_Update(); 
					/*
					ToolBar toolbar = toolBar_Manager().getControl();
					ToolItem toolItem = new ToolItem(toolbar, SWT.PUSH);
					if (text != null)
						toolItem.setText(text + "213");
					if (image != null)
						toolItem.setImage(image);*/
					//the code below shows how to add using contribution
					
				/*IContributionItem contributionItem = new ControlContribution("view.part.contribution2") 
					{				
						protected Control createControl(org.eclipse.swt.widgets.Composite parent) 
						{ 
							Composite composite = new Composite(parent);
							composite.add.label(text + "aaaa");
							composite.add.image(image);
							return composite;
						}
					};*/					
			}});
	}
		
	public ViewPart toolBar_Update()
	{
		UIThreadRunnable.syncExec(composite.getDisplay(), new VoidResult() { public void run() 
			{
			toolBar_Manager().update(true);
			}});
		return this;
	}
}
