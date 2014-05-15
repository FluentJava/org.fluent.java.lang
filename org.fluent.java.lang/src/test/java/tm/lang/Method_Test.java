package tm.lang;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import tm.lang.reflect.Method;

public class Method_Test 
{
	public TestObject testObject;
	public Reflection reflection ;		
	
	public Method_Test() 
	{
		testObject = new TestObject();
		reflection = new Reflection(testObject);		 
	}
	
	@Test 
	public void Method_Ctor()
	{		
		Method method = reflection.method("static_Method");
		assertNotNull(method);
		assertNotNull(method.method_Obj);
		assertNotNull(method.reflection);
		assertNull   (method.lastInvocationException);
		assertFalse  (method.logInvocationExeption);
		assertEquals (method.reflection, reflection);
	}
	
	@Test 
	public void isStatic()
	{
		Method static_Method = reflection.method("static_Method");
		assertTrue (static_Method.isStatic  ());
		assertFalse(static_Method.isInstance());
		
		Method instance_Method = reflection.method("instance_Method");
		assertFalse(instance_Method.isStatic  ());
		assertTrue (instance_Method.isInstance());
	}
	@Test 
	public void isInstance()
	{
		//covered by: isStatic()
	}
	@Test 
	public void invoke()
	{		
		String echoValue = "Hello World"; 
		//instance invocation
		Method instance_Method = reflection.method("instance_Method");
		Method instance_Echo   = reflection.method("instance_Echo");
		
		assertNotNull(instance_Method);
		assertNotNull(instance_Echo);
		assertEquals (instance_Method.invoke()		   , TestObject.instance_Return);
		assertEquals (instance_Echo.  invoke(echoValue), echoValue);
		
		//static invocation
		Method static_Method = reflection.method("static_Method");
		Method static_Echo   = reflection.method("static_Echo");
		
		assertNotNull(static_Method);
		assertNotNull(static_Echo);
		assertEquals (static_Method.invoke()		 , TestObject.static_Return);
		assertEquals (static_Echo.  invoke(echoValue), echoValue);  
		
		//test nulls and exception handing
		assertNull(new Method(null,null).invoke());
		assertNull(static_Echo.invoke(0));			// static_Echo parameter is String
		static_Echo.logInvocationExeption = true;	// ideally we should capture the log to see if it did really happened (and the moment the logs go to System.out)
		assertNull(static_Echo.invoke(0));			// static_Echo parameter is String
	}
	
	@Test
	public void invoke_No_Catch() throws Exception
	{	
		//instance invocation
		try
		{
			Method instance_Echo = reflection.method("instance_Echo");			
			assertNotNull(instance_Echo);
			instance_Echo.invoke_No_Catch(0);			// instance_Echo parameter is String
			fail("instance_Echo.invoke_No_Catch should had thrown exception");
		}
		catch(Exception ex) {}
		//static invocation
		try
		{
			Method static_Echo = reflection.method("static_Echo");			
			assertNotNull(static_Echo);
			static_Echo.invoke_No_Catch(0);			// static_Echo parameter is String
			fail("static_Echo.invoke_No_Catch should had thrown exception");
		}
		catch(Exception ex) {}
		
		//test nulls
		assertNull(new Method(null,null).invoke_No_Catch());
	}
	
	@Test
	public void name()
	{
		String name = "static_Method";
		Method method = reflection.method(name);
		assertEquals(method.name(), name);
		assertTrue  (method.name(name));
		assertFalse (method.name(null));
		assertFalse (method.name(name + "AA"));
		assertNull  (new Method(null,null).name());
		assertFalse (new Method(null,null).name(null));
	}
	@Test 
	public void _toString()
	{
		String name = "static_Method";
		Method method = reflection.method(name);
		assertEquals(method.toString(),   name);
		assertNull  (new Method(null,null).toString());
	}
}
