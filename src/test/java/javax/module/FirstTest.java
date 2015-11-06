package javax.module;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-11-06 11:11.
 */
public
class FirstTest
{
	@Test
	public
	void testNullIndex() throws Exception
	{
		final
		Integer zero = new Integer(0);

		assertEquals(First.nullIndex(), null);
		assertEquals(First.nullIndex(null, zero, 1), zero);
		assertEquals(First.nullIndex(zero, 1, 2, 3), null);
		assertEquals(First.nullIndex(zero, 1, 2, null), new Integer(3));
	}

	@Test
	public
	void testNotNull() throws Exception
	{
		assertNull(First.notNull());
		assertEquals(First.notNull("a" , "b" , "c" ), "a" );
		assertEquals(First.notNull(null, "b" , "c" ), "b" );
		assertEquals(First.notNull(null, null, "c" ), "c" );
		assertEquals(First.notNull(null, null, null), null);
	}
}