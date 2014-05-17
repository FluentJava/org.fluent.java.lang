package tm.lang.reflect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tm.lang.Reflection;

/**
 * Helper class to reflect methods
 *
 */
public class Methods 
{
	public Reflection reflection;
	public boolean	  sortMethodNames;    
	
	/**
	 * Constructor should receive a Reflection object
	 * 
	 * @param reflection
	 */
	public 	Methods(Reflection reflection)
	{
		this.reflection      = reflection;
		this.sortMethodNames = true;
	}
	
	public List<Method> declared()
	{
		List<Method> methods = new ArrayList<Method>();
		for(java.lang.reflect.Method method_Obj : reflection.clazz.getDeclaredMethods())
			methods.add(new Method(reflection, method_Obj));
		return (this.sortMethodNames)
					? reflection.sort_by_Name(methods)
					: methods;
	}
	public Method declared(String methodName,Class<?> ... parameters)
	{
		try  
		{
			java.lang.reflect.Method method_Obj = reflection.clazz.getDeclaredMethod(methodName,parameters);
			if (method_Obj != null)
				return 	new Method(reflection, method_Obj);		
		}
		catch (NoSuchMethodException 	 e)  { }
		return null;
	}
	///Recursive Search for method	
	public Method declared_All(String methodName,Class<?> ... parameters )
	{
		if(reflection == null)
			return null;
		Method method = this.declared(methodName, parameters);
		if(method != null)
			return method;
				
		Reflection superClass = reflection.superClass();
		if (superClass!= null)
			return superClass.methods.declared_All(methodName, parameters);
		return null;
	}
	public <T> T invoke(Class<T> returnType, String methodName, Object ...parameters )
	{
		Object returnValue = invoke(methodName, parameters);
		if (returnValue != null  &&	returnValue.getClass() == returnType)
			return returnType.cast(returnValue);
		return null;
	}

	public Object invoke(String methodName, Object ...parameters )
	{
		
		Class<?>[] parameterClasses= new Class<?>[parameters.length];
		for(int i=0; i < parameterClasses.length; i++)					//tried to do this with List<Class<?>> but it wasn't working
		{				
			Class<?> parameterClass = parameters[i].getClass();		    //this will have probs with boxing of primitive values (see http://stackoverflow.com/questions/3925587/java-reflection-calling-constructor-with-primitive-types)
			parameterClasses[i] = parameterClass;				
		}
		Method method = method(methodName,parameterClasses);
		if (method!= null)
			return method.invoke(parameters);		
		return null;
	}
	public Object invoke_onSuperClass(String methodName, Object ...parameters )
	{		
		Reflection superClass = reflection.superClass();
		if (superClass != null)
		{
			Method method = superClass.methods.method(methodName);
			return method.invoke(method, parameters);
		}	
		return null;
	}
	
	public Method method(String methodName,Class<?> ... parameters )
	{
		//first try full match on all declared methods (with parameters)
		Method methodMatch = declared_All(methodName, parameters);
		if(methodMatch != null)
			return methodMatch;
		for(Method method : methods())
			if (method.name(methodName))
				return method;
		return null;
	}
	public List<Method> methods()
	{
		return methods(reflection.clazz);
	}	
	public List<Method> methods(Class<?> targetClass)
	{
		List<Method> methods = new ArrayList<Method>();
		for(java.lang.reflect.Method method_Obj : reflection.clazz.getMethods())
			methods.add(new Method(reflection, method_Obj));
		
		return (this.sortMethodNames)
					? reflection.sort_by_Name(methods)
					: methods;
	}
	public List<String> methods_Names()
	{
		return methods_Names(false);
	}
	public List<String> methods_Names(boolean onlyShowDeclared)
	{
		return names(onlyShowDeclared ? declared() : methods());
	}
	public List<String> methods_Names(Class<?> targetClass)
	{
		return names(methods(targetClass));
	}
	public List<String> names(List<Method> methods)
	{
		List<String> names = new ArrayList<String>();
		for(Method method : methods)			
			names.add(method.name());
		return names;
	}	
}
