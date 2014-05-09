package org.fluentjava.eclipse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IPath;

public class PluginResources  
{	
	public Activator plugin;
	
	public PluginResources()
	{
		plugin = Activator.plugin;
	}
	
	public IPath get_Plugin_TempPath()
	{		
		return plugin.getStateLocation();
	}
	
	public String get_Resource_Saved_on_TempFolder(String resourceName)
	{
		File file = get_Path_To_Resource_in_TempFolder(resourceName).toFile();
		
		//for now override
		//if (file.exists())			
	//		return file.getAbsolutePath();
		
		InputStream inputStream = get_Resource_Stream(resourceName);
		if (inputStream != null)
		{		
			try 
			{
				
				FileUtils.copyInputStreamToFile(inputStream, file);
				return file.getAbsolutePath();
			}
			catch (IOException e) 
			{
				e.printStackTrace();				
			}	
			if (file.exists())
				return file.getAbsolutePath();
		}

		return null;	
	}
	public IPath get_Path_To_Resource_in_TempFolder(String resourceName)
	{
		return get_Plugin_TempPath().append(resourceName);
	}
	public InputStream get_Resource_Stream(String resourceName) 
	{
		try 
		{
			if (has_Resource(resourceName))
			{	
				return plugin.getBundle()
							 .getEntry(resourceName)
							 
							 .openStream();
			}
			return null;
		} 
		catch (IOException e) 
		{
			return null;
		}			
	}
	
	public boolean has_Resource(String resourceName)
	{
		URL url = plugin.getBundle().getEntry(resourceName);
		return url != null;
	}
}
