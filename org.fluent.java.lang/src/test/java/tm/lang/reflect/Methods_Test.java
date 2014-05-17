package tm.lang.reflect;

import static org.junit.Assert.*;

import org.junit.Test;

import tm.lang.Reflection;
import tm.lang.TestObject;

public class Methods_Test 
{
	public TestObject testObject;
	public Reflection reflection ;	
	public Methods    methods;
	
	public Methods_Test()
	{
		testObject = new TestObject();
		reflection = new Reflection(testObject);
		methods	   = reflection.methods; 
	}
	
	@Test
	public void Methods_Ctor()
	{
		assertNotNull(reflection);
		assertEquals (reflection.target, testObject);
		assertEquals (reflection, methods.reflection);
	}
}
