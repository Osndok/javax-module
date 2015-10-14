package javax.module.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by robert on 2015-10-13 18:58.
 */
public
class ParallelInterfaceCall<T> implements Callable
{
	private final
	Method method;

	private final
	T implementor;

	private final
	Object[] args;

	public
	ParallelInterfaceCall(Method method, T implementor, Object[] args)
	{
		this.method = method;
		this.implementor = implementor;
		this.args = args;
		this.resultList = null;
	}

	private final
	List<Object> resultList;

	public
	ParallelInterfaceCall(Method method, T implementor, Object[] args, List<Object> resultList)
	{
		this.method = method;
		this.implementor = implementor;
		this.args = args;
		this.resultList = resultList;
	}

	private
	Object result;

	private
	Throwable throwable;

	private
	boolean finished;

	@Override
	public
	Object call()
	{
		final
		Object retval;
		{
			try
			{
				retval=method.invoke(implementor, args);
			}
			catch (Throwable throwable)
			{
				synchronized (this)
				{
					this.throwable = throwable;
					this.finished = true;
					notifyAll();
					return null;
				}
			}
		}

		synchronized (this)
		{
			this.result = retval;
			this.finished = true;
			resultList.add(retval);
			notifyAll();
		}

		return retval;
	}

	public
	Object getResult()
	{
		return result;
	}

	public
	Throwable getThrowable()
	{
		return throwable;
	}

	public
	boolean isFinished()
	{
		return finished;
	}
}
