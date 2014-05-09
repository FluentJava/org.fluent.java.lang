package tm.eclipse.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;

public class JavaCompilationUnit
{		
	public ICompilationUnit iCompilationUnit;
	
	public JavaCompilationUnit(ICompilationUnit iCompilationUnit)
	{
		this.iCompilationUnit = iCompilationUnit;				
	}
	
	public static <T extends IEditorReference>JavaCompilationUnit newFromEditorReference(final T editorReference)
	{		
		return UIThreadRunnable.syncExec(new Result<JavaCompilationUnit>() { public JavaCompilationUnit run ()  
			{	
				if(editorReference!= null && editorReference.getId().equals("org.eclipse.jdt.ui.CompilationUnitEditor"))	
				{
					IEditorPart editorPart   = editorReference.getEditor(false);   // false = restore
					return newFromEditorPart(editorPart);					
				}
				return null;
			}});
	}
	
	public static <T extends IEditorPart>JavaCompilationUnit newFromEditorPart(final T editorPart)
	{		
		return UIThreadRunnable.syncExec(new Result<JavaCompilationUnit>() { public JavaCompilationUnit run ()  
			{					
				if (editorPart!= null)
				{
					IEditorInput editorInput = editorPart.getEditorInput();
					IJavaElement iJavaElement = JavaUI.getEditorInputJavaElement(editorInput);
					if(iJavaElement instanceof  ICompilationUnit)
						return new JavaCompilationUnit((ICompilationUnit)iJavaElement);
				}
				return null;
			}});
	}
	
	
	public static List<JavaCompilationUnit> newFromEditorReferences(final List<IEditorReference> editorReferences)
	{		
		
		List<JavaCompilationUnit> compilationUnitEditors = new ArrayList<JavaCompilationUnit>();
		for(IEditorReference editorReference : editorReferences)
		{
			JavaCompilationUnit compilationUnitEditor = newFromEditorReference(editorReference);
			if (compilationUnitEditor != null)
				compilationUnitEditors.add(compilationUnitEditor);
		}				
		return compilationUnitEditors;	
	}
	
	@SuppressWarnings("restriction")
	public org.eclipse.jdt.internal.core.CompilationUnit compilationUnit()
	{
		if (iCompilationUnit instanceof org.eclipse.jdt.internal.core.CompilationUnit)
			return (org.eclipse.jdt.internal.core.CompilationUnit)iCompilationUnit;
		return null;
	}
	public String name()
	{
		return iCompilationUnit.getElementName();
	}
	public IPath path()
	{
		return iCompilationUnit.getPath();
	}
	public String source()
	{
		try 
		{
			return iCompilationUnit.getSource();
		} 
		catch (JavaModelException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	public File file()
	{
		return iCompilationUnit.getPath().toFile();
	}
	public List<String> method_Names()
	{
		List<String> names = new ArrayList<String>();
		for(IMethod method : methods())
			names.add(method.getElementName());
		return names;
	}
	public IMethod method(String name)
	{
		for(IMethod method : methods())
			if (method.getElementName().equals(name))
				return method;
		return null;
	}
	public String method_Source(String name)
	{
		IMethod method = method(name);
		if (method != null)
			try 
			{
				return method.getSource();
			}
			catch (JavaModelException e) 
			{
				e.printStackTrace();
			}
		return null;
	}
	public List<IMethod> methods()
	{	
		List<IMethod> methods = new ArrayList<IMethod>();
		for(IType type : types())
			try 
			{						
				for(IMethod method : type.getMethods())
					methods.add(method);
			} 
			catch (JavaModelException e) 
			{
				e.printStackTrace();
			}
		return methods;		
	}
	public List<IType> types()
	{		
		try 
		{
			return  Arrays.asList(iCompilationUnit.getAllTypes());
		} 
		catch (JavaModelException e) 
		{
			e.printStackTrace();
			return new ArrayList<IType>();
		}
	}
	
	
}