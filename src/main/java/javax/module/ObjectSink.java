package javax.module;

/**
 * Created by robert on 2015-10-14 16:03.
 */
public
interface ObjectSink<T>
{
	void receiveObject(T t);
}
