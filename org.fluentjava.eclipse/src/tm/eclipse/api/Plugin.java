package tm.eclipse.api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.fluentjava.eclipse.Activator;

public class Plugin 
{
	public EclipseAPI eclipse;
	public Display    display;
	public Activator  current;
	
	public Plugin(EclipseAPI eclipse)
	{
		this.eclipse = eclipse;
		this.display = eclipse.display;
		this.current = Activator.getDefault();
	}
	
	public IPath tempDir()
	{
		return UIThreadRunnable.syncExec(display, new Result<IPath>() { public IPath run() 
			{					
				return current.getStateLocation();
			}});
	}
	
	
	public Collection<File> savedScripts()
	{
		File path 		    = tempDir_SavedScripts().toFile();
		String[] extensions = new String[] { "txt"};
		boolean  recursive  = false;
		return FileUtils.listFiles(path, extensions, recursive);
		
	}
	public IPath tempDir_SavedScripts()
	{
		Calendar now = Calendar.getInstance();
		IPath savedScripts = tempDir().append("SavedScripts")
									  .append(String.format("%s_%s_%s", now.get(Calendar.YEAR), 
											  							now.get(Calendar.MONTH), 
											  							now.get(Calendar.DAY_OF_MONTH)));
		if (savedScripts.toFile().exists()==false)
			savedScripts.toFile().mkdirs();
		return savedScripts;
	}
	public File save_SavedScript(String contents)
	{		
		Calendar now = Calendar.getInstance();		
		String tmpFileName = String.format("%s.txt", now.getTimeInMillis());
		return save_SavedScript(tmpFileName, contents);
	}
	public File save_SavedScript(String fileName, String contents) 
	{		
		try 
		{
			IPath target = tempDir_SavedScripts().append(fileName);
			File file = target.toFile();
			FileUtils.writeStringToFile(file, contents);
			return file;
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}		
	}
	public String load_SavedScript(String fileName)
	{
		
		try 
		{
			IPath target = tempDir_SavedScripts().append(fileName);
			File file = target.toFile();
			return FileUtils.readFileToString(file);
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}		
	}
}
