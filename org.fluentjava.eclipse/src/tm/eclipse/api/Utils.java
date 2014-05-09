package tm.eclipse.api;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.eclipse.ui.ide.FileStoreEditorInput;

import tm.eclipse.swt.Control_Self;

public class Utils 
{
	public EclipseAPI 	eclipse;
	public Display    	display;
	public SecureRandom secureRandom;
	
	public Utils()
	{
		this(EclipseAPI.current());
	}
	public Utils(EclipseAPI eclipse)
	{
		this.eclipse      = eclipse;
		this.display 	  = eclipse.display;
		this.secureRandom = new SecureRandom();
	}
	
	public static int sleep(int miliseconds)
	{
		try 
		{
			Thread.sleep(miliseconds);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		return miliseconds;
	}
	// file utils
	public boolean dir_Create(File path)
	{
		return path.mkdirs();
	}
	public boolean dir_Create(String path)
	{
		File userPath = home_File(path);
		return dir_Create(userPath);
	}
	public String file_Contents(String virtualPath)
	{
		File userPath = home_File(virtualPath);
		return file_Contents(userPath);
	}
	public String file_Contents(File file)
	{
		try {
			return FileUtils.readFileToString(file);
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	public String file_Contents(IPath path)
	{
		return file_Contents(path.toFile());
	}
	public File file_Save(String virtualPath, String data)
	{
		File userPath = home_File(virtualPath);
		return file_Save(userPath, data);
	}
	public File home_File(String virtualPath) 
	{
		File realFile = new File(virtualPath);
		if (realFile.exists())
			return realFile;
		if (virtualPath != null)			
			return mapPath(new File(home(),virtualPath));
		return null;
	}
	public File file_Save(File file,String data)
	{
		try 
		{
			FileUtils.writeStringToFile(file, data);
		} catch (IOException e) 
		{
			e.printStackTrace();			
		}
		return file;
	}
	public Collection<File> files()
	{
		return files(new File(home()),false);
	}
	public Collection<File> files(File path, boolean recursive, String ... extensionFilter)
	{
		return FileUtils.listFiles(path, extensionFilter.length ==0 ? null : extensionFilter, recursive);		
	}
	public Collection<File> files(String path)
	{
		File userPath = home_File(path);
		return files(userPath, false);
	}
	public  List<String> filesAndDirs()
	{
		return filesAndDirs(new File(home()));
	}
	public List<String> filesAndDirs(File path)
	{
		String[] result = path.list();	
		return (result != null) 
					? Arrays.asList(result)
					: new ArrayList<String>();
	}
	public  List<String> filesAndDirs(String path)
	{
		File userPath = home_File(path);		
		return filesAndDirs(userPath);
	}
	public String home()
	{
		return System.getProperty("user.home");
	}
	public File mapPath(File path)
	{		
		try 
		{
			return new File(path.getCanonicalPath());
		}
		catch (IOException e) 
		{
			return null;
		}
	}
	public String mapPath(String path)
	{
		try 
		{
			return new File(path).getCanonicalPath();
		} 
		catch (IOException e) 
		{
			return null;
		}
	}
	
	public int random_Int()
	{
		return secureRandom.nextInt();
	}
	public long random_Long()
	{
		return secureRandom.nextLong();
	}
	public String random_String()
	{
		return String.format("%d",Math.abs(random_Int()));
	}
	public Process exec(String processPath, String arguments)
	{
		try 
		{
			//return Runtime.getRuntime().exec(processPath, arguments);  //didn't work
			return new ProcessBuilder(processPath, arguments).start();
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}	
	public IFileSystem localFileSystem()
	{
		return EFS.getLocalFileSystem();
	}
	public IFileStore path_to_IFile(String path)
	{
		return file_to_IFileStore(home_File(path));
	}
	public IFileStore file_to_IFileStore(File file)
	{
		return localFileSystem().fromLocalFile(file);
	}
	public FileStoreEditorInput file_to_FileStoreEditorInput(File file) 
	{
		IFileStore fileStore = file_to_IFileStore(file);
		return new FileStoreEditorInput(fileStore);		
	}

	public List<Control> controls(Composite composite)
	{
		return controls(composite, false);
	}
	public List<Control> controls(Composite composite, boolean recursive)
	{
		return new Control_Self<Composite>(composite).controls(recursive);		
	}
	public <T1> T1 		 control(Composite composite, Class<T1> clazz)	
	{
		return control(composite, false, clazz);
	}
	public <T1> T1 		 control(Composite composite, boolean recursive, Class<T1> clazz)
	{
		return new Control_Self<Composite>(composite).control(recursive, clazz);
	}
	public <T1> List<T1> controls(Composite composite, Class<T1> clazz)	
	{
		return controls(composite, false, clazz);
	}
	public <T1> List<T1> controls(Composite composite, boolean recursive, Class<T1> clazz)
	{
		return new Control_Self<Composite>(composite).controls(recursive, clazz);
	}
}
