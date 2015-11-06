package javax.module;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Sometimes handling null values (or other cases?) is so rote, that it would be better to
 * make your code more *readable* (like english) than *longer* and over-specified ("and if
 * it is null do nothing"). This class is intended to catch any such static utilities that
 * can make your code cleaner by any readable method name that might start with 'avoid'.
 *
 * Note: There are an unlimited number of array types that *could* be placed in here. The
 * most common (File & String) are first, but probably not the last. If you would like a
 * "standard library" (i.e. no dependency required) object type to be added to this class,
 * patches are welcome!
 *
 * Created by robert on 2015-11-06 11:18.
 */
public
class Avoid
{
	private static final
	Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private static final
	File[] EMPTY_FILE_ARRAY = new File[0];

	private static final
	String[] EMPTY_STRING_ARRAY = new String[0];

	private static final
	byte[] EMPTY_BYTE_ARRAY = new byte[0];

	private static final
	short[] EMPTY_SHORT_ARRAY = new short[0];

	private static final
	int[] EMPTY_INT_ARRAY = new int[0];

	private static final
	long[] EMPTY_LONG_ARRAY = new long[0];

	private static final
	float[] EMPTY_FLOAT_ARRAY = new float[0];

	private static final
	double[] EMPTY_DOUBLE_ARRAY = new double[0];

	private static final
	boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

	private static final
	char[] EMPTY_CHAR_ARRAY = new char[0];

	/**
	 * This is here primarily because I always tire of writing the same code (over and over)
	 * to handle the File::listFiles() return values, that will be null rather than an empty
	 * array. Early design decisions that haunt java, I suppose.
	 *
	 * @param files - an array of files, or null
	 * @return an array of files, never null (but may be empty!)
	 */
	public static
	File[] nullValue(File[] files)
	{
		if (files == null)
		{
			return EMPTY_FILE_ARRAY;
		}
		else
		{
			return files;
		}
	}

	/**
	 * This is here primarily because I always tire of writing the same code (over and over)
	 * to handle the File::list() return values, that will be null rather than an empty
	 * array. Early design decisions that haunt java, I suppose.
	 *
	 * @param strings - an array of strings, or null
	 * @return an array of strings, never null (but may be empty!)
	 */
	public static
	String[] nullValue(String[] strings)
	{
		if (strings == null)
		{
			return EMPTY_STRING_ARRAY;
		}
		else
		{
			return strings;
		}
	}

	public static
	Object[] nullValue(Object[] array)
	{
		if (array == null)
		{
			return EMPTY_OBJECT_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static <T>
	Collection<T> nullValue(Collection<T> collection)
	{
		if (collection == null)
		{
			return Collections.emptySet();
		}
		else
		{
			return collection;
		}
	}

	public static <T>
	List<T> nullValue(List<T> list)
	{
		if (list == null)
		{
			return Collections.emptyList();
		}
		else
		{
			return list;
		}
	}

	public static
	<T>
	Set<T> nullValue(Set<T> set)
	{
		if (set == null)
		{
			return Collections.emptySet();
		}
		else
		{
			return set;
		}
	}

	public static
	<K, V>
	Map<K, V> nullValue(Map<K, V> map)
	{
		if (map == null)
		{
			return Collections.emptyMap();
		}
		else
		{
			return map;
		}
	}

	public static
	byte[] nullValue(byte[] array)
	{
		if (array == null)
		{
			return EMPTY_BYTE_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	short[] nullValue(short[] array)
	{
		if (array == null)
		{
			return EMPTY_SHORT_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	int[] nullValue(int[] array)
	{
		if (array == null)
		{
			return EMPTY_INT_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	long[] nullValue(long[] array)
	{
		if (array == null)
		{
			return EMPTY_LONG_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	float[] nullValue(float[] array)
	{
		if (array == null)
		{
			return EMPTY_FLOAT_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	double[] nullValue(double[] array)
	{
		if (array == null)
		{
			return EMPTY_DOUBLE_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	boolean[] nullValue(boolean[] array)
	{
		if (array == null)
		{
			return EMPTY_BOOLEAN_ARRAY;
		}
		else
		{
			return array;
		}
	}

	public static
	char[] nullValue(char[] array)
	{
		if (array == null)
		{
			return EMPTY_CHAR_ARRAY;
		}
		else
		{
			return array;
		}
	}

	private
	Avoid()
	{
		throw new UnsupportedOperationException("perhaps in the future, we could take a discriminator argument, and have some instance-methods too?");
	}

	/*
	public
	T unwanted(T t)
	{
		if (discriminator.isWanted(t))
		{
			return t;
		}
		else
		{
			return discriminator.defaultValue();
		}
	}
	*/
}
