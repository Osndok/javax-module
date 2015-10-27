package javax.module.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Given two implementations of the same interface, this mechanism creates a third implementation that
 * wraps the other two; using the first as a 'primary' implementation, and the second as a 'fallback'
 * in case the other one returns null or throws an exception.
 */
public
class InterfaceFallback <T> implements InvocationHandler
{
	private final
	T primary;

	private final
	T fallback;

	public
	InterfaceFallback(T primary, T fallback)
	{
		if (primary==null || fallback==null)
		{
			throw new NullPointerException();
		}

		this.primary = primary;
		this.fallback = fallback;
	}

	public
	T get(Class<T> aClass)
	{
		return Convert.objectToInterface(this, aClass);
	}

	@Override
	public
	Object invoke(Object o, Method method, Object[] objects) throws Throwable
	{
		if (method.getReturnType().equals(Void.TYPE))
		{
			//System.err.println("void return type");
			//System.err.flush();
			try
			{
				method.invoke(primary, objects);
				return null;
			}
			catch (InvocationTargetException e)
			{
				handle(e.getTargetException());
			}
			catch (Throwable t2)
			{
				handle(t2);
			}

			method.invoke(fallback, objects);
			return null;
		}
		else
		{
			//System.err.println("non-void return type: "+method.getReturnType());
			//System.err.flush();
			Object retval=null;

			try
			{
				retval=method.invoke(primary, objects);
			}
			catch (InvocationTargetException e)
			{
				handle(e.getTargetException());
			}
			catch (Throwable t2)
			{
				handle(t2);
			}

			if (retval==null)
			{
				retval=method.invoke(fallback, objects);
			}

			return retval;
		}
	}

	private
	void handle(Throwable t)
	{
		try
		{
			final
			Thread thread = Thread.currentThread();

			Thread.UncaughtExceptionHandler handler = thread.getUncaughtExceptionHandler();

			if (handler == null)
			{
				handler = Thread.getDefaultUncaughtExceptionHandler();
			}

			handler.uncaughtException(thread, t);
		}
		catch (Throwable t2)
		{
			t.printStackTrace();
			t2.printStackTrace();
		}
	}
}
