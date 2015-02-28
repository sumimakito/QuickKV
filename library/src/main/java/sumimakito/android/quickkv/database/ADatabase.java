package sumimakito.android.quickkv.database;

public abstract class ADatabase
{
	public abstract void put(Object k, Object v);
	
	public abstract Object get(Object k);
	
	public abstract void remove(Object k);
}
