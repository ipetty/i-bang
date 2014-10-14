package net.ipetty.ibang.android.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Caches {

	private static final Map<Class, Map> cache = new HashMap<Class, Map>();
	private static final Long expireTime = 3 * 60 * 1000l; // 3分钟过期
	private static final int maxCacheSize = 50; // 缓存数上限

	/**
	 * 获取缓存容器map
	 */
	private static <K extends Object, V extends Object> Map<K, V> getStore(Class clazz) {
		Map map = cache.get(clazz);
		if (map == null) {
			map = new HashMap(maxCacheSize);
			cache.put(clazz, map);
		}
		return map;
	}

	/**
	 * 缓存
	 */
	public static <K extends Object, V extends Object> void cache(K key, V value) {
		if (value != null) {
			Entry<K, V> entry = new Entry<K, V>(key, value, System.currentTimeMillis() + expireTime);
			getStore(value.getClass()).put(key, entry);
		}
	}

	/**
	 * 从缓存中获取对象
	 */
	public static <K extends Object, V extends Object> V get(Class clazz, K key) {
		Map store = getStore(clazz);
		if (!store.containsKey(key)) {
			return null;
		}

		Entry<K, V> entry = (Entry<K, V>) store.get(key);
		// 如果过期
		if (System.currentTimeMillis() > entry.getExpireOn()) {
			store.remove(key);
			return null;
		}
		return entry.getValue();
	}

	/**
	 * 删除缓存对象
	 */
	public static <K extends Object> void remove(Class clazz, K key) {
		getStore(clazz).remove(key);
	}

	/**
	 * 缓存中保存的数据对象
	 */
	public static class Entry<K extends Object, V extends Object> {
		private K key;
		private V value;
		private Long expireOn; // 记录插入时间戳（毫秒数）

		public Entry() {
			super();
		}

		public Entry(K key, V value, Long expireOn) {
			super();
			this.key = key;
			this.value = value;
			this.expireOn = expireOn;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public Long getExpireOn() {
			return expireOn;
		}

		public void setExpireOn(Long expireOn) {
			this.expireOn = expireOn;
		}
	}

}
