package javax.module;

/**
 * Created by robert on 2015-11-06 11:05.
 */
public
class First
{
	private
	First()
	{
		throw new UnsupportedOperationException();
	}

	public static
	Integer nullIndex(Object... objects)
	{
		final
		int l=objects.length;

		for (int i=0; i<l; i++)
		{
			if (objects[i]==null)
			{
				return i;
			}
		}

		return null;
	}

	/**
	 * This function behaves much like the 'coalesce' function found in SQL.
	 * @return the first argument (from left to right) that is not null, or null if they are all null
	 */
	public static <T>
	T notNull(T... ts)
	{
		for (T t : ts)
		{
			if (t!=null)
			{
				return t;
			}
		}

		return null;
	}
}
