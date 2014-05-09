package tm.eclipse.groovy.plugins;

//import java.io.File;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IPath;
import org.fluentjava.eclipse.PluginResources;

public class GroovyPlugins 
{
	PluginResources pluginResources = new PluginResources();
	
	public String get_PluginScript_Path(String scriptPath)
	{		
		return pluginResources.get_Resource_Saved_on_TempFolder(scriptPath);
	}
	
	public String get_PluginScript_Code(String scriptPath)
	{
		String path = get_PluginScript_Path(scriptPath);
		if (path == null)
			return null;		
		try 
		{	
			URL url = new URL("file:///" + path); // just using file:// works on OSx but not on windows
			//URI uri = new URI(path);
			String fileContents = IOUtils.toString(url);
			return fileContents;
		}
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		return null;
	}
/*	public File[] get_FilesInPlugin()
	{
		File file = get_PluginBaseFolderPath().toFile();
		return file.listFiles();		
	}*/
	public IPath get_PluginBaseFolderPath()
	{
		return pluginResources.get_Path_To_Resource_in_TempFolder("TM_Plugins");							  
	}
	public Object execute_PluginScript(String scriptPath)
	{
		String pluginScript = get_PluginScript_Code(scriptPath);
		if (pluginScript == null)
			return null;
		return new GroovyExecution().executeScript(pluginScript);
	}
		
}
