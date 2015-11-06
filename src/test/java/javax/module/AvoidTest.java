package javax.module;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-11-06 11:42.
 */
public
class AvoidTest
{

	@Test
	public
	void testNullValue() throws Exception
	{
		assertNotNull(Avoid.nullValue((Object[])null));
	}

	@Test
	public
	void testNullValue0() throws Exception
	{
		assertNotNull(Avoid.nullValue((File[])null));
	}

	@Test
	public
	void testNullValue1() throws Exception
	{
		assertNotNull(Avoid.nullValue((String[])null));
	}

	@Test
	public
	void testNullValue2() throws Exception
	{
		assertNotNull(Avoid.nullValue((byte[])null));
	}

	@Test
	public
	void testNullValue3() throws Exception
	{
		assertNotNull(Avoid.nullValue((short[])null));
	}

	@Test
	public
	void testNullValue4() throws Exception
	{
		assertNotNull(Avoid.nullValue((int[])null));
	}

	@Test
	public
	void testNullValue5() throws Exception
	{
		assertNotNull(Avoid.nullValue((long[])null));
	}

	@Test
	public
	void testNullValue6() throws Exception
	{
		assertNotNull(Avoid.nullValue((float[])null));
	}

	@Test
	public
	void testNullValue7() throws Exception
	{
		assertNotNull(Avoid.nullValue((double[])null));
	}

	@Test
	public
	void testNullValue8() throws Exception
	{
		assertNotNull(Avoid.nullValue((boolean[])null));
	}

	@Test
	public
	void testNullValue9() throws Exception
	{
		assertNotNull(Avoid.nullValue((char[])null));
	}

	@Test
	public
	void testNullValue10() throws Exception
	{
		assertNotNull(Avoid.nullValue((Collection)null));
	}

	@Test
	public
	void testNullValue11() throws Exception
	{
		assertNotNull(Avoid.nullValue((List)null));
	}

	@Test
	public
	void testNullValue12() throws Exception
	{
		assertNotNull(Avoid.nullValue((Set)null));
	}

	@Test
	public
	void testNullValue13() throws Exception
	{
		assertNotNull(Avoid.nullValue((Map)null));
	}
}