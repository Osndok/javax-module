package javax.module.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-10-20 12:03.
 */
public
class InterfaceFallbackTest
{
	interface A
	{
		void voidFunction(String s);
		int primitiveReturn();
		Integer objectReturn();
	}

	private static
	int FULL_CALLS;

	private static final
	A FULL = new A()
	{
		@Override
		public
		void voidFunction(String s)
		{
			System.err.println("FULL: voidFunction(" + s + ")");
			FULL_CALLS++;
		}

		@Override
		public
		int primitiveReturn()
		{
			System.err.println("FULL: primitiveReturn()");
			FULL_CALLS++;
			return 1234;
		}

		@Override
		public
		Integer objectReturn()
		{
			System.err.println("FULL: objectReturn()");
			FULL_CALLS++;
			return new Integer(1234);
		}
	};

	private static
	int THROWS_CALLS;

	private static final
	A THROWS = new A()
	{
		@Override
		public
		void voidFunction(String s)
		{
			THROWS_CALLS++;
			throw new AssertionError();
		}

		@Override
		public
		int primitiveReturn()
		{
			THROWS_CALLS++;
			throw new AssertionError();
		}

		@Override
		public
		Integer objectReturn()
		{
			THROWS_CALLS++;
			throw new AssertionError();
		}
	};

	private static
	int EMPTY_CALLS;

	private static final
	A EMPTY = new A()
	{
		@Override
		public
		void voidFunction(String s)
		{
			System.err.println("EMPTY: voidFunction("+s+")");
			EMPTY_CALLS++;
		}

		@Override
		public
		int primitiveReturn()
		{
			System.err.println("EMPTY: primitiveReturn()");
			EMPTY_CALLS++;
			return 0;
		}

		@Override
		public
		Integer objectReturn()
		{
			System.err.println("EMPTY: objectReturn()");
			EMPTY_CALLS++;
			return null;
		}
	};

	@Test
	public
	void testBasicFallback()
	{
		final
		A a=new InterfaceFallback<A>(EMPTY, FULL).get(A.class);

		final
		int moreEmptyCalls=EMPTY_CALLS;

		assertEquals(a.objectReturn(), new Integer(1234));
		assertEquals(EMPTY_CALLS, 1+moreEmptyCalls);
	}

	@Test
	public
	void testThrownImplementation()
	{
		final
		A a=new InterfaceFallback<A>(THROWS, FULL).get(A.class);

		final
		int moreThrowsCalls=THROWS_CALLS;

		final
		int moreFullCalls=FULL_CALLS;

		a.voidFunction("after throw");
		assertEquals(a.primitiveReturn(), 1234);
		assertEquals(a.objectReturn(), new Integer(1234));

		assertEquals(THROWS_CALLS, 3 + moreThrowsCalls);
		assertEquals(FULL_CALLS, 3+moreFullCalls);
	}
}