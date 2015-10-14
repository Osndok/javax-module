package javax.module;

/**
 * This is most basic mechanism to acquire a dynamic interface at runtime, but for now it only acts as
 * a concentrator in that it reduces the work of 'providing all the interfaces' to 'providing one meta-interface'.
 * Roughly equivalent to Tapestry's "Registry" or "ObjectLocator" service. It is expected that the underlying
 * implementation of this interface might even dispatch calls outside of the JVM, if appropriate.
 */
public
interface InterfaceGateway
{
	<T>
	T getInterface(Class<T> interfaceClass);

	<T>
	void noticeImplementation(Class<T> interfaceClass, T implementation);
}
