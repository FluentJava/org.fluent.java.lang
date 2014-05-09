package tm.eclipse.helpers;

import groovy.inspect.swingui.ObjectBrowser;

public class debug 
{
	public static Object inspect(Object target)
	{
		ObjectBrowser.inspect(target);
		return target;
	}
}

