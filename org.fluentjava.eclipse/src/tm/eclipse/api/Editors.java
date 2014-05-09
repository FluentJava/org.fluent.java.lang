package tm.eclipse.api;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.registry.EditorDescriptor;

import tm.eclipse.api.JavaCompilationUnit;
import tm.eclipse.ui.editors.StringInput;
import tm.eclipse.ui.editors.StringStorage;

public class Editors 
{
	public EclipseAPI eclipse;
	
	public Editors(EclipseAPI eclipse)
	{
		this.eclipse = eclipse;
	}
		
	public IEditorPart active()
	{				
		return eclipse.activeWorkbenchPage.getActiveEditor();
	}	
	public JavaCompilationUnit active_JavaCompilationUnit()
	{				
		return javaCompilationUnit(active());
	}
	
	public IEditorReference 		get(String title)
	{
		return reference(title);
	}
	public List<IEditorDescriptor>  available()
	{
		return eclipse.registry.editors();
	}
	public List<String>           	available_Ids()
	{
		return eclipse.registry.editors_Ids();
	}
	public List<String>           	available_Labels()
	{
		return eclipse.registry.editors_Labels();
	}
	public List<String>             ids()
	{
		List<String> ids = new ArrayList<String>();
		for(IEditorReference view : this.list())
			ids.add(view.getId());
		return  ids;
	}
	public JavaCompilationUnit 		javaCompilationUnit(IEditorPart editorPart)
	{
		return JavaCompilationUnit.newFromEditorPart(editorPart);
	}
	public JavaCompilationUnit 		javaCompilationUnit(String methodName)
	{
		for(JavaCompilationUnit javaCompilationUnit : javaCompilationUnits())
			if(javaCompilationUnit.name().equals(methodName))
					return javaCompilationUnit;
		return null;
	}
	public List<JavaCompilationUnit> javaCompilationUnits()
	{
		return JavaCompilationUnit.newFromEditorReferences(Editors.this.list());
	}
	public List<IEditorReference> list()
	{
		return references();
	}
	public List<IEditorReference> opened()
	{
		return references();
	}
	
	public IEditorPart open(IEditorDescriptor editorDescriptor)
	{
		return open(editorDescriptor.getId());
	}
	public IEditorPart open(String idOrLabel)
	{				
		return open_String_in_Editor(eclipse.registry.editor(idOrLabel));		
	}
	public IEditorPart open_String_in_TextEditor(String text)
	{
		return open_String_in_Editor("org.eclipse.ui.DefaultTextEditor", text);
	}
	public IEditorPart open_String_in_Editor(final IEditorDescriptor editorDescriptor)
	{
		if (editorDescriptor != null)
		{
			String text = String.format("This is an editor for '%s' with id '%s'", editorDescriptor.getLabel(), editorDescriptor.getId());
			return open_String_in_Editor(editorDescriptor, text);
		}
		return null;
	}
	public IEditorPart open_String_in_Editor(final IEditorDescriptor editorDescriptor, final String text)
	{
		return open_String_in_Editor(editorDescriptor.getId(), text);
	}
	public IEditorPart open_String_in_Editor(final String editorId, final String text)
	{
		return syncExec(new Result<IEditorPart>() { public IEditorPart run()
			{
				StringStorage storage = new StringStorage(text,false);
				StringInput input     = new StringInput(storage);		   
				try 
				{
					return eclipse.activeWorkbenchPage.openEditor(input, editorId);

					//IDE.openEditor can also be used
					//IDE.openEditor(_page, input, _editor.id);  
				}
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
				return null;
			}});		
	}
	
	public IEditorPart open_File_in_TextEditor(File file)
	{
		return open("org.eclipse.ui.DefaultTextEditor", file);
	}
	public IEditorPart open_File_in_Editor(String editorId, String fileToOpen)
	{
		return open(editorId, eclipse.utils.home_File(fileToOpen));
	}
	public IEditorPart open(IEditorDescriptor editorDescriptor, String fileToOpen)
	{
		return open(editorDescriptor, eclipse.utils.home_File(fileToOpen));
	}
	public IEditorPart open(IEditorDescriptor editorDescriptor, File file)
	{
		return open(editorDescriptor.getId(), file);
	}
	public IEditorPart open(final String editorId, final File file)
	{
		return syncExec(new Result<IEditorPart>() { public IEditorPart run ()  
			{
				FileStoreEditorInput editorInput = eclipse.utils.file_to_FileStoreEditorInput(file);
				try 
				{
					return eclipse.activeWorkbenchPage.openEditor(editorInput, editorId);
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
					return null;
				}
				
			}});
	}
	
	public List<String>           partNames()
	{
		List<String> ids = new ArrayList<String>();
		for(IEditorReference view : this.list())
			ids.add(view.getPartName());
		return  ids;
	}
	public IEditorReference 	  reference(String title)
	{
		for(IEditorReference editor : references())
			if(editor.getTitle().equals(title))
					return editor;
		return null;
	}
	
	public List<IEditorReference> references()
	{
		return syncExec(new Result<List<IEditorReference>>() { public List<IEditorReference> run ()  
			{
				return Arrays.asList(eclipse.activeWorkbenchPage.getEditorReferences());
			}});
	}
	public IEditorPart show(final IEditorPart editorPart)
	{
		return syncExec(new Result<IEditorPart>() { public IEditorPart run ()  
			{				
				eclipse.activeWorkbenchPage.activate(editorPart);
				return editorPart;
			}});
	}
	
	public IEditorPart show(final IEditorReference editorReference)
	{
		return syncExec(new Result<IEditorPart>() { public IEditorPart run ()  
			{
				IEditorPart editorPart = editorReference.getEditor(true);
				return show(editorPart);
			}});
	}
	
	public List<String>           titles()
	{
		List<String> titles = new ArrayList<String>();
		for(IEditorReference view : this.list())
			titles.add(view.getTitle());
		return  titles;
	}	
}