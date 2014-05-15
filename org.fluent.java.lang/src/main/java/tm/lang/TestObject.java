package tm.lang;

public class TestObject 
{
	public static String instance_Return = "Hello World";
	public static String static_Return   = "This is a static string";
	
	public String instance_Method()
	{
		return TestObject.instance_Return;
	}
	
	public String instance_Echo(String value)
	{
		return value;
	}	
	public static String static_Method()
	{
		return static_Return;
	}	

	public static String static_Echo(String value)
	{
		return value;
	}
}
