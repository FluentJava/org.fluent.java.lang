package tm.eclipse.ui.views.utils;

import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.api.EclipseAPI;
import tm.eclipse.helpers.Images;
import tm.eclipse.swt.controls.Composite;
import tm.eclipse.swt.controls.Label;
import tm.eclipse.swt.controls.Text;
//import tm.eclipse.ui.Startup;
import tm.eclipse.ui.views.Eclipse_Panel;

public class IconsViewer 
{
	EclipseAPI    eclipse; 
	Eclipse_Panel view;
	Text 		  hoveredImage;
	Composite     panel;
	Text 		  filter;
	
	public IconsViewer()
	{
		eclipse = EclipseAPI.current();
		createGui();
	}
	public IconsViewer createGui()
	{
		view = eclipse.views.create("Images Embeded in Eclipse");
		view.clear().set.layout.grid(2);		
					
		filter = view.add.text("Filter: ","")
				     .set.layout.grid_Grab_Horizontal();
		view.add.label("Hovered image:");
		hoveredImage = view.add.text()
				   .set.layout.grid_Grab_Horizontal();

		panel = view.add.panel()
					.set.layout.row()
					.set.layout.grid_Grab_All(2,1);
		
		ShowImages showImages = new ShowImages();
		filter.set.event.onChange(new ShowImages());
		showImages.run();
		return this;
	}
	
	public class ShowImages implements Runnable
	{
		public void run()
		{
			//panel.set.redraw(false);
			synchronized (panel) 
			{					
				
				//improves performance if we do it all inside the UI Thread
				UIThreadRunnable.syncExec(eclipse.display,new VoidResult() { public void run() 
					{
						panel.self.clear();
						String filterText = filter.text().toUpperCase();
						for(String name : Images.names())
						{
							if(filterText.equals("") ||  name.contains(filterText))
							{
								final Label image = panel.add.image(Images.get(name),name);
				
								image.set.event.onMouseEnter(new java.lang.Runnable() { public void run() 
										{
											hoveredImage.text(image.get.toolTip());
										}});
							}
						}
						panel.add.label("");
					}});
				//panel.set.redraw(true);
				view.refresh();
			}
			
			
		}
	}
}
