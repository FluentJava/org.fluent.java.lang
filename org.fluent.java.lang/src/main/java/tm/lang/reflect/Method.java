package tm.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import tm.lang.Reflection;

public class Method 
{
	public java.lang.reflect.Method method_Obj;
	public Reflection 			    reflection;
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
