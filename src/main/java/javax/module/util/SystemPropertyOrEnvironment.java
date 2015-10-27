package javax.module.util;

import java.util.Map;
import java.util.Properties;

/**
 * Created by robert on 4/8/15.
 */
public
class SystemPropertyOrEnvironment extends AbstractPropertiesAdapter
{
	private static final
	String DIAGNOSTIC_MESSAGE = "system properties and environment variables";

	public static
	String get(String key)
	{
		final
		String prop = System.getProperty(key);

		if (prop == null)
		{
			return System.getenv(key);
		}
		else
		{
			return prop;
		}
	}

	public static
	String get(String key, String _default)
	{
		final
		String prop=System.getProperty(key);

		if (prop==null)
		{
			final
			String env=System.getenv(key);

			if (env==null)
			{
				return _default;
			}
			else
			{
				return env;
			}
		}
		else
		{
			return prop;
		}
	}

	public static
	boolean getBoolean(String key, boolean _default)
	{
		final
		String s=get(key);

		if (s==null)
		{
			return _default;
		}
		else
		{
			try
			{
				return Convert.stringToBooleanPrimitive(s);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
				return _default;
			}
		}
	}

	public
	SystemPropertyOrEnvironment()
	{
		super(DIAGNOSTIC_MESSAGE, null);
	}

	public
	SystemPropertyOrEnvironment(Properties mixedCaseDefaults)
	{
		super(DIAGNOSTIC_MESSAGE, mixedCaseDefaults);
	}

	@Override
	protected
	String getPropertyForLowerCaseKey(String desiredLowerCaseKey)
	{
		//(1) Check system properties.
		for (String key : System.getProperties().stringPropertyNames())
		{
			if (desiredLowerCaseKey.equalsIgnoreCase(key))
			{
				return System.getProperty(key);
			}
		}

		//(2) Check environment variables.
		for (Map.Entry<String, String> me : System.getenv().entrySet())
		{
			final
			String key=me.getKey();

			if (desiredLowerCaseKey.equalsIgnoreCase(key))
			{
				return me.getValue();
			}
		}

		return null;
	}

	@Override
	protected
	String getDiagnosticIdentifier()
	{
		return DIAGNOSTIC_MESSAGE;
	}

	@Override
	protected
	boolean isCacheValid()
	{
		return true;
	}
}
