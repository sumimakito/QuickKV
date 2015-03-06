package sumimakito.android.quickkv.database;

import java.util.*;

public interface QKVDatabase
{
	<K, V> boolean put(K k, V v);
	<K> Object get(K k);
	<K> boolean containsKey(K k);
	<V> boolean containsValue(V v);
	<K> boolean remove(K k);
	<K> boolean remove(K[] k);
	void clear();
	int size();
}
