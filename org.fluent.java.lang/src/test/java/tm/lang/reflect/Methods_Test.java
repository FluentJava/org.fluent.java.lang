package tm.lang.reflect;

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
	
	
}
