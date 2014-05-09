package tm.eclipse.ui.editors;
import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

//based on code from http://wiki.eclipse.org/FAQ_How_do_I_open_an_editor_on_something_that_is_not_a_file%3F
public class StringInput implements IStorageEditorInput 
{
    private IStorage storage;
    public StringInput(IStorage storage)                    { this.storage = storage;   }
    public boolean              exists()                    { return true;              }
    public ImageDescriptor      getImageDescriptor()        { return null;              }
    public String               getName()                   { return storage.getName(); }
    public IPersistableElement  getPersistable()            { return null;              }
    public IStorage             getStorage()                { return storage;           }
    public String               getToolTipText()            { return "String-based file: " + storage.getName(); }
    @SuppressWarnings("rawtypes")
    public Object               getAdapter( Class adapter)  { return null;              }
 }