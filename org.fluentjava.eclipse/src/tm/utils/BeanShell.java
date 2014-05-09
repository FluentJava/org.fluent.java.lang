package tm.utils;

import bsh.EvalError;
import bsh.Interpreter;

public class BeanShell 
{	
	public Interpreter interpreter; 
	public EvalError   lastError;
	public Object 	   lastReturnValue;
	public String 	   lastScript;
	
	public BeanShell()
	{
		interpreter = new Interpreter();
	}
	public Object invokeScript(String script)
	{
		return eval(script);
	}
	public Object eval(String script) 
	{
		try 
		{
			lastError = null;
			lastReturnValue = null;
			lastScript = script;
			lastReturnValue  = interpreter.eval(script);			
		}
		catch (EvalError e) 
		{
			lastError = e;
		}
		return lastReturnValue;
	}
}
