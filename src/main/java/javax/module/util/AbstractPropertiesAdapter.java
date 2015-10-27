package javax.module.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * Created by robert on 2015-10-08 01:07.
 */
public abstract
class AbstractPropertiesAdapter implements InvocationHandler
{
	private final
	String contextBit;

	protected final
	Properties lowerCasedDefaults;

	protected
	AbstractPropertiesAdapter(String contextBit, Properties mixedCaseDefaults)
	{
		this.contextBit = contextBit;

		if (mixedCaseDefaults==null)
		{
			this.lowerCasedDefaults=null;
		}
		else
		{
			this.lowerCasedDefaults = new Properties();

			for (String key : mixedCaseDefaults.stringPropertyNames())
			{
				lowerCasedDefaults.setProperty(key.toLowerCase(), mixedCaseDefaults.getProperty(key));
			}
		}
	}

	@Override
	public
	Object invoke(Object o, Method method, Object[] objects) throws Throwable
	{
		final
		String methodName = method.getName();

		final
		Object[] context = new Object[]{contextBit, ": ", methodName};

		final
		boolean readMode;

		final
		String propertyName;
		{
			final
			boolean capitalMatch=methodName.length()>3 && Character.isUpperCase(methodName.charAt(3));

			if (capitalMatch && methodName.startsWith("get"))
			{
				readMode=true;
				propertyName=methodName.substring(3);
			}
			else
			if (capitalMatch && methodName.startsWith("set"))
			{
				readMode=false;
				propertyName=methodName.substring(3);
			}
			else
			{
				readMode=true;
				propertyName=methodName;
			}
		}

		if (readMode)
		{
			if (objects!=null && objects.length>0)
			{
				throw new UnsupportedOperationException("PropertiesFileAdapter cannot handle read methods that take arguments: "+methodName);
			}

			final
			String key=propertyName.toLowerCase();

			//All the string parsing can be expensive if an interface is used repeatedly... cache the introspection.
			//NB: we must cache intentionally-null property values too.
			if (isCacheValid() && cache.containsKey(key))
			{
				/*
				BUG?: looks like we traded caching-nulls to a race condition... but there is a bigger 'race' below,
				and I don't think we are too concerned with thread-safety at the moment.
				 */
				return cache.get(key);

				/*
				if (cacheHit!=null && isCacheValid())
				{
					return cacheHit;
				}
				*/
			}

			//TODO: check for void return type? what would that even mean?!
			final
			Class<?> returnType = method.getReturnType();

			final
			String stringValue = getPropertyForLowerCaseKey(key);

			if (stringValue==null)
			{
				//System.err.println(String.format("get(%s) -> %s", propertyName, stringValue));
				if (returnType.isPrimitive())
				{
					throw new IllegalStateException(propertyName + " cannot be null/empty/missing due to primitive return type");
				}

				if (methodIsMarkedWithNotNullAnnotation(method))
				{
					throw new MissingResourceException("no property value for key (in "+getDiagnosticIdentifier()+"): "+key,
														  method.getDeclaringClass().toString(),
														  key);
				}
			}

			final
			Object retval=Convert.stringToBasicObject(stringValue, returnType, context);

			cache.put(key, retval);

			return retval;
		}
		else
		{
			throw new UnsupportedOperationException("write methods are not yet implemented (in this version)");
		}
	}

	private
	boolean methodIsMarkedWithNotNullAnnotation(Method method)
	{
		Boolean hasNotNullAnnotation=methodAnnotationCache.get(method);

		if (hasNotNullAnnotation==null)
		{
			hasNotNullAnnotation=reflectOnPresenceOfNotNullAnnotation(method);
			methodAnnotationCache.put(method, hasNotNullAnnotation);
		}

		return hasNotNullAnnotation;
	}

	private
	boolean reflectOnPresenceOfNotNullAnnotation(Method method)
	{
		for (Annotation annotation : method.getAnnotations())
		{
			final
			Class aClass = annotation.annotationType();

			final
			String name = aClass.getSimpleName().toLowerCase();

			if (name.startsWith("notnull") || name.startsWith("nonnull") || name.startsWith("required"))
			{
				return true;
			}

			if (name.contains("nullable") || name.contains("optional"))
			{
				return false;
			}
		}

		return false;
	}

	protected final
	Map<String, Object> cache = new HashMap<String, Object>();

	protected final
	Map<Method, Boolean> methodAnnotationCache = new HashMap<Method, Boolean>();

	protected abstract
	String getPropertyForLowerCaseKey(String key);

	//Kinda the same as 'contextBit'?
	protected abstract
	String getDiagnosticIdentifier();

	protected abstract
	boolean isCacheValid();
}
