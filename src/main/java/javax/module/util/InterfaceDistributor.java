package javax.module.util;

/**
 * Created by robert on 2015-10-13 17:36.
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Implements a basic fan-out mechanism to "combine" two or more implementation of an interface
 * into one. This class should be thread safe, provided the implementations it wraps are also
 * thread-safe.
 *
 * @param <T>
 */
public
class InterfaceDistributor<T> implements InvocationHandler
{
	private final
	ExecutorService executor;

	public
	InterfaceDistributor(ExecutorService executor)
	{
		this.executor = executor;
	}

	public
	InterfaceDistributor()
	{
		this.executor = null;
	}

	public
	enum Mode
	{
		BEST_EFFORT,
		REQUIRED,
		SERIAL //<-- implementation currently depends on this being last.
	}

	private final
	Map<Mode, List<T>> implementations = new HashMap<Mode, List<T>>();

	public
	void add(Mode mode, T implementation)
	{
		if (mode == null)
		{
			throw new NullPointerException();
		}

		final
		List<T> list;
		{
			synchronized (implementations)
			{
				if (implementations.containsKey(mode))
				{
					list = implementations.get(mode);
				}
				else
				{
					list = new CopyOnWriteArrayList<T>();
					implementations.put(mode, list);
				}
			}
		}

		list.add(implementation);
	}

	public
	boolean remove(T implementation)
	{
		boolean retval=false;

		synchronized (implementations)
		{
			for (List<T> ts : implementations.values())
			{
				if (ts.remove(implementation))
				{
					retval=true;
				}
			}

		}

		return retval;
	}

	public
	T asInterface(Class<T> aClass)
	{
		return Convert.objectToInterface(this, aClass);
	}

	@Override
	public
	Object invoke(Object proxyObject, Method interfaceMethod, Object[] arguments) throws Throwable
	{
		boolean allAreBestEffort=true;

		final
		Collection<Future<?>> pendingTasks=new ArrayList<Future<?>>();

		final
		Map<Mode, Collection> results = new HashMap<Mode, Collection>();
		{
			for (Mode mode : Mode.values())
			{
				final
				List<T> implementors = implementations.get(mode);

				if (implementors!=null && !implementors.isEmpty())
				{
					final
					List<Object> resultList = new CopyOnWriteArrayList<Object>();

					results.put(mode, resultList);

					if (mode==Mode.SERIAL)
					{
						allAreBestEffort=false;

						for (T implementor : implementors)
						{
							waitForAllResults(pendingTasks);
							execute(interfaceMethod, implementor, arguments, mode, resultList, pendingTasks);
						}
					}
					else
					if (mode==Mode.BEST_EFFORT)
					{
						//Not order-dependent, so we can go ahead and execute them all in parallel now...
						for (T implementor : implementors)
						{
							try
							{
								execute(interfaceMethod, implementor, arguments, mode, resultList, pendingTasks);
							}
							catch (Throwable t)
							{
								bestEffortCaught(t, interfaceMethod, implementor, arguments);
							}
						}
					}
					else
					{
						allAreBestEffort=false;

						//Not order-dependent, so we can go ahead and execute them all in parallel now...
						for (T implementor : implementors)
						{
							execute(interfaceMethod, implementor, arguments, mode, resultList, pendingTasks);
						}
					}
				}
			}
		}

		if (allAreBestEffort && interfaceMethod.getReturnType().equals(Void.TYPE))
		{
			//We can return early, and possibly free the VM from a tangled mess of futures.
			return null;
		}
		else
		{
			waitForAllResults(pendingTasks);
		}

		return combineResults(interfaceMethod, results);
	}

	protected
	void bestEffortCaught(Throwable t, Method method, T implementor, Object[] args)
	{
		t.printStackTrace();
	}

	private
	void waitForAllResults(Collection<Future<?>> pendingTasks) throws ExecutionException, InterruptedException
	{
		for (Future<?> future : pendingTasks)
		{
			future.get();
		}

		pendingTasks.clear();
	}

	private
	void execute(Method method, T implementor, Object[] args, Mode mode, List<Object> resultList, Collection<Future<?>> pendingTasks) throws InvocationTargetException, IllegalAccessException
	{
		if (executor==null)
		{
			resultList.add(method.invoke(implementor, args));
		}
		else
		{
			pendingTasks.add(executor.submit(new ParallelInterfaceCall<T>(method, implementor, args, resultList)));
		}
	}

	/**
	 * The default implementation will pick out the 'last' non-null result (e.g. from the last serial call).
	 *
	 * @param interfaceMethod
	 * @param results
	 * @return
	 * @throws Throwable
	 */
	protected
	Object combineResults(Method interfaceMethod, Map<Mode,Collection> results) throws Throwable
	{
		Object retval=null;

		for (Collection collection : results.values())
		{
			for (Object o : collection)
			{
				if (o != null)
				{
					retval=o;
				}
			}
		}

		return retval;
	}

}
