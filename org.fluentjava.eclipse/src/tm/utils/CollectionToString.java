package tm.utils;

import java.util.Collection;
import java.util.Map;

//Question: is there an existing method that already does this?
public class CollectionToString 
{
	public Object target;	
	
	public CollectionToString(Object _target)
	{
		target = _target;
	}
	public boolean isCollection()
	{		
		if (target == null)
			return false;
		if (target instanceof Collection<?> || target instanceof Map || target.getClass().isArray())
			return true;
		return false;
	}
	public Object[] asArray()
	{
		try
		{
			if(isCollection())
			{
				if(target.getClass().isArray())			
					return (Object[])target;
				if(target instanceof Collection<?>)			
					return ((Collection<?>)target).toArray();			
				if(target instanceof Map)			
					return ((Map<?,?>)target).entrySet().toArray();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;	
	}
	public String asString()
	{
		if(isCollection())
		{
			String result = "";
			Object[] asArray = asArray();
			if (asArray != null)
			{
				for(Object item : asArray())
					result += item + "\n";
				return result;
			}
		}
		if(target != null)
			return target.toString();
		return null;
	}
}
