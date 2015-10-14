package javax.module.impl;

import javax.module.InterfaceGateway;
import javax.module.util.ModuleKey;

/**
 * Created by robert on 2015-10-13 17:29.
 */
class ModuleAgnosticInterfaceGatewayFactory extends InterfaceGatewayFactory
{
	private final
	InterfaceGateway interfaceGateway;

	public
	ModuleAgnosticInterfaceGatewayFactory(InterfaceGateway interfaceGateway)
	{
		this.interfaceGateway=interfaceGateway;
	}

	@Override
	public
	InterfaceGateway getInterfaceGateway(ModuleKey moduleKey)
	{
		return interfaceGateway;
	}
}
