package tm.lang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tm.lang.reflect.Method;
import tm.lang.reflect.Methods;

public class Reflection  
{
	public Object 		 target;
	public Class<?>  	 clazz;	
	public Methods		 methods;
	
	public Reflection(Object target) 
	{
		 this(target, target.getClass());
	}
	
	public Reflection(Object target, Class<?> clazz)
	{
		this.target  = target;
		this.clazz   = clazz;
		this.methods = new Methods      (this);
	}
	 
	public static Reflection reflection(Object target)
	{
		Reflection reflection = new Reflection(target);		
		return reflection;
	}
		
	public Method method(String methodName)
	{
		return methods.method(methodName);
	}
	
	
	public Field field(String fieldName)
	{
		
		try 
		{
			return clazz.getDeclaredField(fieldName);
		} 
		catch (SecurityException    e)  {	e.printStackTrace(); } 
		catch (NoSuchFieldException e)  { 	e.printStackTrace(); }
		return null;
	}
	public Object field_Value(String fieldName)
	{		
		return field_Value(field(fieldName));
	}
	public <T> T field_Value(String fieldName, Class<T> returnType)
	{
		Object value = field_Value(fieldName);
		if (value.getClass().equals(returnType))
			return returnType.cast(value);
		return null;
	}
	public Object field_Value(Field field)
	{
		if (field!= null)
		{
			field.setAccessible(true);
			try 
			{
				return field.get(target);
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	public List<Field> fields()
	{
		List<Field> fields = Arrays.asList(clazz.getDeclaredFields()); 
		return sort_by_Name(fields);
	}
	public List<Object> fields_Values()
	{
		return fields_Values(fields());
	}
	public List<Object> fields_Values(List<Field> fields)
	{
		List<Object> values = new ArrayList<Object>();
		for(Field field : fields)
			values.add(field_Value(field));
		return values;
	}
	
	public List<String> fields_Names()
	{
		List<String> names = new ArrayList<String>();
		for(Field field : fields())			
			names.add(field.getName());
		return names;	
	}

	public List<String> getters_Names()
	{
		List<String> getters = new ArrayList<String>();
		for(String methodName : methods.methods_Names())
			if (methodName.startsWith("get"))
					getters.add(methodName.substring(3));
		return getters;
	}
	public List<String> getters_Values()
	{
		List<String> getters = new ArrayList<String>();
		for(String methodName : methods.methods_Names())
			if (methodName.startsWith("get"))
			{
				Object result = methods.invoke(methodName);				
				getters.add(result == null ? null : result.toString());					
			}
		return getters;
	}	
	
	
	//Utils
	
	public List<String> sort(List<String> list)
	{
		Collections.sort(list);
		return list;
	}
	//TODO this is throwing a lot of fieldNameNotFound exceptions
	public <T> List<T> sort_by_Name(List<T> list)
	{		
		//Collections.sort(list, new Comparator_FieldToStringValue<T>("name"));
		Collections.sort(list, new Comparator_FieldToStringValue<T>("name"));
		return list;		
	}	
	
	public Reflection superClass()
	{
		Class<?> superClass  = clazz.getSuperclass();	
		return  (superClass != null) 
					? new Reflection(this, superClass)
					: null;
	}	

	
	//TODO move to another class
	public static class Comparator_FieldToStringValue<T> implements Comparator<T>
	{
		public String fieldToSortBy;
		public Comparator_FieldToStringValue(String fieldToSortBy)
		{
			this.fieldToSortBy = fieldToSortBy;
		}
	
		public int compare(T object1, T object2)
		{
			try
			{
				String value1 = new Reflection(object1).field_Value(fieldToSortBy).toString();
				String value2 = new Reflection(object2).field_Value(fieldToSortBy).toString();
				return value1.compareTo(value2);
			}
			catch(Exception ex)
			{
				return 0;
			}
		}
	}
}
