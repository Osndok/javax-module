package javax.module.util;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.Properties;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-10-08 01:12.
 */
public
class PropertiesFileAdapterTest
{
	private static final
	File TEST_FILE = new File("src/test/resources/test.props");

	@Test
	public
	void testPropertyFileAdapter() throws Exception
	{
		final
		Object o = new PropertiesFileAdapter(TEST_FILE);

		final
		FromTheFile fromFile = Convert.objectToInterface(o, FromTheFile.class);

		assertEquals(fromFile.getAlpha(), "The first letter of the greek alphabet");
		assertEquals(fromFile.integerPrimitive(), 123);
		assertEquals(fromFile.getInteger(), new Integer(123));
		assertEquals(fromFile.getBoolean(), Boolean.TRUE);
		assertNull(fromFile.getDNE());
	}

	@Test
	void testDefaults() throws Exception
	{
		final
		String defaultValue="VALUE-"+hashCode();

		final
		Properties defaults=new Properties();
		{
			defaults.put("DeFaUlT", defaultValue);
		}

		final
		Object o = new PropertiesFileAdapter(new File("/path/to/somewhere/that/does/not/exist/please"), defaults);

		final
		FromTheFile fromFile = Convert.objectToInterface(o, FromTheFile.class);

		assertNull(fromFile.getAlpha());
		assertNull(fromFile.getInteger());
		assertNull(fromFile.getBoolean());
		assertEquals(fromFile.getDefault(), defaultValue);

		try
		{
			fromFile.integerPrimitive();
			throw new AssertionError();
		}
		catch (IllegalStateException e)
		{
			System.err.println("good: "+e.toString());
			assertTrue(e.getMessage().contains("primitive"));
		}
	}

	interface FromTheFile
	{
		String getAlpha();
		int integerPrimitive();
		Integer getInteger();
		Boolean getBoolean();
		Boolean getDNE();
		String getDefault();
		PropEnum getEnumValue();
		PropEnum getEnumDNE();
	}

	enum PropEnum
	{
		VALUE1,
		VALUE2,
		VALUE3
	}

	@Test
	public
	void testEnumUsage() throws Exception
	{
		final
		Object o = new PropertiesFileAdapter(TEST_FILE);

		final
		FromTheFile fromFile = Convert.objectToInterface(o, FromTheFile.class);

		assertEquals(fromFile.getEnumValue(), PropEnum.VALUE2);
		assertNull(fromFile.getEnumDNE());
	}

	@Test
	public
	void testBadStringParsingFormat() throws Exception
	{
		final
		Properties defaults = new Properties();
		{
			defaults.put("integer", "not-an-integer");
		}

		final
		Object o = new PropertiesFileAdapter(new File("/path/to/somewhere/that/does/not/exist/please"), defaults);

		final
		FromTheFile fromFile = Convert.objectToInterface(o, FromTheFile.class);

		try
		{
			Integer integer = fromFile.getInteger();
			throw new AssertionError();
		}
		catch (NumberFormatException e)
		{
			System.err.println("good: "+e);
			assertTrue(e.getMessage().contains("getInteger()"));
		}
	}
}