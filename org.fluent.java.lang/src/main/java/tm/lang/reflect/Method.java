package tm.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import tm.lang.Reflection;

/**
 * Helper class for individual Methods.
 * 
 * This expands the normal java.lang.reflect.Method class with extra helper methods 
 * like for example isStatic.
 * 
 * The invocation defaults to setAccessible(true) so that private methods can be easy executed
 * 
 * The original java.lang.reflect.Method is available as the method_Obj field 
 *
 * 
 */
public class Method 
{
	/**
	 * This is the Java's Method obj which we are wrapping
	 */
	public java.lang.reflect.Method method_Obj;
	/**
	 * Used to contain information about the method's class and live object
	 */
	public Reflection 			    reflection;
	/**
	 * Contains null if no exception was thrown by the invoke method. This value is not touched if 
	 * {@link #invoke_No_Catch} method is used for invocation
	 */
	public Exception  			    lastInvocationException;
	public boolean    				logInvocationExeption;
		
	public Method(Reflection reflection, java.lang.reflect.Method method)
	{
		this.reflection = reflection;
		this.method_Obj = method;
	}
	
	public Object invoke(Object ...arguments)
	{
		if(method_Obj == null)
			return null;
		try 
		{
			lastInvocationException = null;
			method_Obj.setAccessible(true);	
			if (isStatic())
				return method_Obj.invoke(null, arguments);
			else
				return method_Obj.invoke(reflection.target,arguments);
		} 
		catch(Exception ex)
		{
			lastInvocationException = ex;
			if(logInvocationExeption)
				ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Use this method if you want the invocation to be executed without a surrounding catch
	 * 
	 * @param arguments
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object invoke_No_Catch(Object ...arguments) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		if(method_Obj == null)
			return null;
		method_Obj.setAccessible(true);	
		if (isStatic())
			return method_Obj.invoke(null, arguments);
		else
			return method_Obj.invoke(reflection.target,arguments);
	}
	
	public String name()
	{		
		return (method_Obj != null) 
					? method_Obj.getName()
					: null;
	}
	public boolean name(String name)
	{
		return (method_Obj != null && name != null && name != "") 
				? this.name().equals(name)
				: false;
	
	}
	public boolean isStatic()
	{
		return Modifier.isStatic(method_Obj.getModifiers());
	}
	public boolean isInstance()
	{
		return isStatic() == false;
	}
	
	@Override
	public String toString()
	{
		return name();
	}
}
