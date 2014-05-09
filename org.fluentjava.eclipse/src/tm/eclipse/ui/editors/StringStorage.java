package tm.eclipse.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

//based on code from http://wiki.eclipse.org/FAQ_How_do_I_open_an_editor_on_something_that_is_not_a_file%3F

public class StringStorage implements IStorage 
{
  public String  string;
  public boolean readOnly;
  
  public StringStorage(String input) 
  {
    this(input, true);
  }
  public StringStorage(String input, boolean readOnly) 
  {
    this.string = input;
    this.readOnly = readOnly;
  }
 
  public InputStream getContents() throws CoreException 
  {
    return new ByteArrayInputStream(string.getBytes());
  }
 
  public IPath getFullPath() 
  {
    return null;
  }
   
public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) 
{
    return null;
  }
 
  public String getName() 
  {
    int len = Math.min(5, string.length());
    return string.substring(0, len).concat("..."); //$NON-NLS-1$
  }
 
  public boolean isReadOnly() 
  {	  
    return readOnly;
  }
  public StringStorage readOnly(boolean readOnly)
  {
	  this.readOnly = readOnly;
	  return this;
  }
}