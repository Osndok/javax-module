package javax.module.impl;

import javax.module.InterfaceGateway;
import javax.module.util.Convert;
import javax.module.util.ModuleKey;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * TODO: allow for the exchange of a previously-provided interface to be exchanged for a new one, like in Tapestry.
 *
 * Created by robert on 2015-10-13 17:12.
 */
class DefaultInterfaceGatewayFactory extends InterfaceGatewayFactory
{
	@Override
	public
	InterfaceGateway getInterfaceGateway(ModuleKey moduleKey)
	{
		return new DefaultInterfaceGateway(moduleKey);
	}

	private final
	Map<Class, Object> implementationsByInterface = new WeakHashMap<Class, Object>();

	private
	class DefaultInterfaceGateway implements InterfaceGateway
	{
		private final
		ModuleKey moduleKey;

		public
		DefaultInterfaceGateway(ModuleKey moduleKey)
		{
			this.moduleKey = moduleKey;
		}

		@Override
		public <T>
		T getInterface(Class<T> interfaceClass)
		{
			final
			Object existing=implementationsByInterface.get(interfaceClass);

			if (existing==null)
			{
				System.err.println("WARNING: "+moduleKey+" requested "+interfaceClass+" without a pre-existing implementation registered (live interface switching does not work in this version)");
				return Convert.objectToInterface(DEFAULT_INTERFACE_HANDLER, interfaceClass);
			}
			else
			{
				return (T)existing;
			}
		}

		@Override
		public
		<T> void noticeImplementation(Class<T> interfaceClass, T implementation)
		{
			implementationsByInterface.put(interfaceClass, implementation);
		}
	}

	private final
	InvocationHandler DEFAULT_INTERFACE_HANDLER = new InvocationHandler()
	{
		@Override
		public
		Object invoke(Object o, Method method, Object[] objects) throws Throwable
		{
			if (method.getReturnType().equals(Void.TYPE))
			{
				//They are not requesting anything from us, so it's probably benign enough to just log the call as a warning.
				//This lets us write against 'notification' interfaces without ever actually writing an implementation.
				System.err.println("WARNING: DefaultInterfaceGatewayFactory did not have an implementation for: "+method.getClass()+"."+method.getName()+" when the interface was originally requested");
				return null;
			}
			else
			{
				throw new UnsupportedOperationException("unimplemented, or implementation not available at getInterface() time");
			}
		}
	};
}
