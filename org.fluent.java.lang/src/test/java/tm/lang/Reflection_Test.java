package tm.lang;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Reflection_Test 
{
	Reflection reflection;
	TestObject testObject;
	
	public Reflection_Test()
	{
		testObject = new TestObject();
		reflection = new Reflection(testObject);
	}
	
	@Test 
	public void Reflection_Ctor()
	{  				
		assertNotNull(reflection);
		assertNotNull(reflection.target);
		assertNotNull(reflection.clazz);
		assertNotNull(reflection.methods);
		assertNotNull(reflection.methods.reflection);
		assertEquals (reflection.clazz 		       , TestObject.class);
		assertEquals (reflection.target		       , testObject);		
		assertEquals (reflection.methods.reflection, reflection);
	}
}
