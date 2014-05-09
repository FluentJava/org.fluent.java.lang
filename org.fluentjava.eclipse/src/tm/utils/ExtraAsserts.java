package tm.utils;

import static org.junit.Assert.*;

public class ExtraAsserts 
{
	public static void assertIsClass(Object object, Class<?> clazz)
	{		
		assertEquals(object.getClass(),clazz);
	}
	
	/* the assertNotEquals does not exist on the JUnit that is on Indigo
	 * 
	public static void assertIsNotClass(Object object, Class<?> clazz)
	{		
		assertNotEquals(object.getClass(),clazz);
	}*/
	
	//Need to figure out a better way to do this
	/*@SuppressWarnings("unchecked")
	public static <K extends Class<?>> void assertIsClass(Object object)
	{
		try
		{		
			K castedObject = (K)object;		
			assertNotNull(castedObject);
		}
		catch(ClassCastException ex)
		{
			fail(ex.getMessage());
		}		
	}*/
}
