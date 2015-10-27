package javax.module.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by robert on 2015-10-08 00:26.
 */
public
class PropertiesFileAdapter extends AbstractPropertiesAdapter
{
	private final
	File file;

	public
	PropertiesFileAdapter(File file)
	{
		super(file.toString(), null);
		this.file = file;
	}

	public
	PropertiesFileAdapter(File file, Properties defaults)
	{
		super(file.toString(), defaults);
		this.file = file;
	}

	private
	long lastReadModTime;

	private
	Properties currentValues;

	@Override
	protected
	String getPropertyForLowerCaseKey(String key)
	{
		//System.err.println("get: "+key);

		final
		long startModTime = file.lastModified();

		if (startModTime != lastReadModTime)
		{
			try
			{
				final
				Properties propertiesReadFromFile;
				{
					propertiesReadFromFile = readAllProperties();
				}

				if (file.lastModified() == startModTime)
				{
					lastReadModTime = startModTime;
					currentValues = propertiesReadFromFile;
					cache.clear();
				}
				else
				{
					System.err.println("file changed as it was being read: " + file);

					if (currentValues == null)
					{
						//We need *something* to go on... partial is better than nothing?
						currentValues = propertiesReadFromFile;
					}
				}
			}
			catch (IOException e)
			{
				System.err.println(String.format("Unable to read '%s' from properties file: %s ; %s", key, file, e));
			}
		}

		if (currentValues==null)
		{
			currentValues=new Properties();
		}

		final
		String currentValue=currentValues.getProperty(key);

		if (currentValue==null && lowerCasedDefaults !=null)
		{
			return lowerCasedDefaults.getProperty(key);
		}
		else
		{
			return currentValue;
		}
	}

	@Override
	protected
	boolean isCacheValid()
	{
		return (file.lastModified() == lastReadModTime);
	}

	private
	Properties readAllProperties() throws IOException
	{
		System.err.println("Reading properties file: "+file);

		final
		Properties properties=new Properties();
		{
			InputStream inputStream = null;

			try
			{
				inputStream = new FileInputStream(file);

				properties.load(inputStream);
			}
			finally
			{
				if (inputStream!=null)
				{
					inputStream.close();
				}
			}
		}

		final
		Properties lowerCased=new Properties();
		{
			//Convert entries to accomidate lower-case contract.
			for (String key : properties.stringPropertyNames())
			{
				final
				String lower = key.toLowerCase();

				//System.err.println("setProperty("+lower+", "+properties.getProperty(key)+");");
				lowerCased.setProperty(lower, properties.getProperty(key));
			}
		}

		return lowerCased;
	}

	@Override
	protected
	String getDiagnosticIdentifier()
	{
		return "properties file: "+file;
	}
}
