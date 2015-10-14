package javax.module.impl;

import javax.module.InterfaceGateway;
import javax.module.util.ModuleKey;
import javax.module.util.SystemPropertyOrEnvironment;

/**
 * Created by robert on 2015-10-13 17:07.
 */
public abstract
class InterfaceGatewayFactory
{
	public abstract
	InterfaceGateway getInterfaceGateway(ModuleKey moduleKey);

	public static
	InterfaceGatewayFactory getInstance()
	{
		return INSTANCE;
	}

	/**
	 * @deprecated because, even though this is convenient for testing purposes, you probably should not be calling it in production or published modules.
	 * @param interfaceGatewayFactory
	 */
	@Deprecated
	public static
	void setInstance(InterfaceGatewayFactory interfaceGatewayFactory)
	{
		INSTANCE = interfaceGatewayFactory;
	}

	private static
	InterfaceGatewayFactory INSTANCE;

	static
	{
		final
		String moduleAndClassName = SystemPropertyOrEnvironment.get("InterfaceGatewayFactory");

		if (moduleAndClassName==null)
		{
			INSTANCE = new DefaultInterfaceGatewayFactory();
		}
		else
		{
			//@TODO: I seem to recall periods being unsafe for environment variables (or just 'bash'?), maybe account for that.
			final
			boolean bestEffort=SystemPropertyOrEnvironment.getBoolean("InterfaceGatewayFactory.bestEffort", false);

			try
			{
				final
				Object o = Class.forName(moduleAndClassName).newInstance();

				if (o instanceof InterfaceGatewayFactory)
				{
					INSTANCE = (InterfaceGatewayFactory) o;
				}
				else
				if (o instanceof InterfaceGateway)
				{
					INSTANCE = new ModuleAgnosticInterfaceGatewayFactory((InterfaceGateway)o);
				}
				else
				if (bestEffort)
				{
					System.err.println("WARNING: InterfaceGatewayFactory class ("+moduleAndClassName+") does not implement InterfaceGateway[Factory]: "+o);
					INSTANCE = new DefaultInterfaceGatewayFactory();
				}
				else
				{
					throw new RuntimeException("WARNING: InterfaceGatewayFactory class ("+moduleAndClassName+") does not implement InterfaceGateway[Factory]: "+o);
				}
			}
			catch (RuntimeException e)
			{
				if (bestEffort)
				{
					e.printStackTrace();
					INSTANCE = new DefaultInterfaceGatewayFactory();
				}
				else
				{
					throw e;
				}
			}
			catch (Exception e)
			{
				if (bestEffort)
				{
					e.printStackTrace();
					INSTANCE = new DefaultInterfaceGatewayFactory();
				}
				else
				{
					throw new RuntimeException(e);
				}
			}
		}
	}
}
