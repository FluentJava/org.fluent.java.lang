package tm.eclipse.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import tm.eclipse.ui.views.utils.IconsViewer;

public class Images 
{
	public static List<String> 	 	 sharedImages_Names;
	public static Map<String,String> javaPluginImages_Names ;	
	
	static
	{		
		sharedImages_Names 	   = mapNames_SharedImages();
		javaPluginImages_Names = mapNames_JavaPluginImages();
	}
	public static List<String> list()
	{	
		return names();
	}	
	public static List<String> names()
	{				
		List<String> names = new ArrayList<String>();		
		names.addAll(sharedImages_Names);
		names.addAll(javaPluginImages_Names.keySet());
		return names;
	}
	public static List<String> mapNames_SharedImages()
	{
		List<String> names = new ArrayList<String>();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		for(Field field : sharedImages.getClass().getFields())
			if(field.getName().contains("MASK") == false && field.getName().contains("DND") == false)
				names.add(field.getName());
		return names;
	}
	
	@SuppressWarnings("restriction")
	public static Map<String,String> mapNames_JavaPluginImages()
	{
		Map<String,String> names = new HashMap<String,String>();		
		for(Field field : org.eclipse.jdt.internal.ui.JavaPluginImages.class.getFields())
			if(field.getName().startsWith("IMG"))
				try 
				{
					names.put(field.getName(),(String)field.get(null));
				} 
				catch (IllegalArgumentException e) { e.printStackTrace();} 
				catch (IllegalAccessException e){ e.printStackTrace();}
		
		return names;
	}

	public static Image get(int index)
	{		
		List<String> names = names();
		if(names.size() > index)
			return  get(names.get(index)); //sharedImages.getImage(names.get(index));
		return null;
	}
	@SuppressWarnings("restriction")
	public static Image get(String symbolicName)
	{	
		if (sharedImages_Names.contains(symbolicName))
		{
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			return sharedImages.getImage(symbolicName);
		}
		if (javaPluginImages_Names.containsKey(symbolicName))
		{
			String imagePath = javaPluginImages_Names.get(symbolicName);
 			return org.eclipse.jdt.internal.ui.JavaPluginImages.get(imagePath);
		}
		return null;
	}
	
	public static IconsViewer view()
	{
		return new IconsViewer();
	}
}
