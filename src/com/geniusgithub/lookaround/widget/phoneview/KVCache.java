package com.geniusgithub.lookaround.widget.phoneview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

abstract class KVCache<K, V> {
	@SuppressWarnings("unused")
	private static final String TAG = "KVCache";
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;

	private static final int CACHE_MAX_SIZE = 10 * 1024 * 1024; // 10MB

	// private final byte[] lock = new byte[0];

	// private final Map<String, SoftReference<V>> mMemCache;
	final LruCache<String, V> mMemCache;

	protected final Context mContext;

	/**
	 * 0 means permanent.
	 */
	private long mDiskCacheTimeout = 0;

	public KVCache(Context context) {
		// mMemCache = new HashMap<String, SoftReference<V>>();
		mMemCache = new LruCache<String, V>(CACHE_MAX_SIZE) {

			@Override
			protected int sizeOf(String key, V value) {
				if (value instanceof Bitmap) {
					return ((Bitmap) value).getRowBytes()
							* ((Bitmap) value).getHeight();
				}
				return 1;
			}

		};
		// Avoid memory leak (maybe).
		mContext = context.getApplicationContext();
	}

	/**
	 * Change the key to String using its' 'toString()' method then return the
	 * MD5 value. Different keys' value are unique.
	 * 
	 * @param key
	 * @return
	 */
	protected String getHash(K key) {
		return String.valueOf(key.toString().hashCode());
		// try {
		// MessageDigest digest = MessageDigest.getInstance("MD5");
		// digest.update(key.toString().getBytes());
		// return new BigInteger(digest.digest()).toString(16);
		// } catch (NoSuchAlgorithmException ex) {
		// return key.toString();
		// }
	}

	/**
	 * Read cached object from memory cache.
	 * 
	 * @param key
	 *            which is an identify of a cached object.
	 * @return return the cached object if found, otherwise null will be
	 *         returned.
	 */
	protected V fromMemCache(K key) {
		if (key == null) {
			return null;
		}
		final String hashKey = getHash(key);
		return mMemCache.get(hashKey);
	}

	/**
	 * Get an object from cache system.
	 * 
	 * <p>
	 * First we find the cached object in memory and returning it if found
	 * successfully. Otherwise we find the file associated with the key in local
	 * disk and read object from the file.
	 * </p>
	 * 
	 * @param key
	 *            the key used to find the cached object.
	 * @return return the cached object if successfully, otherwise null.
	 * @throws IOException
	 */
	public V get(K key) throws IOException {
		V value = fromMemCache(key);
		if (value != null) {
			return value;
		}

		final String hashKey = getHash(key);
		File f = new File(mContext.getCacheDir(), hashKey);
		boolean timeout = (mDiskCacheTimeout <= 0) ? false : f.lastModified()
				+ mDiskCacheTimeout < new Date().getTime();
		if (timeout) {
			f.delete();
		}

		if (f.exists()) {
			FileInputStream fis = new FileInputStream(f);
			value = fromLocalDisk(key, fis);
			fis.close();
		}

		if (value != null) {
			mMemCache.put(hashKey, value);
		}

		return value;
	}

	/**
	 * Put value in cache with the specified key.
	 * 
	 * <p>
	 * We use two level of caches heer. First we put the object in memory
	 * cache(Using SoftReference). Second we put the object in local file.
	 * </p>
	 * 
	 * @param key
	 *            the key associated with the cached value.
	 * @param value
	 *            the object which will be put into cache system.
	 * @throws IOException
	 */
	public void put(K key, V value) throws IOException {
		if (key == null || value == null) {
			return;
		}

		final String hashKey = getHash(key);

		// put the object in memory cache.
		mMemCache.put(hashKey, value);

		// put the object in local disk cache.
		File f = new File(mContext.getCacheDir(), hashKey);
		if (f.exists() || f.createNewFile()) {
			FileOutputStream fos = new FileOutputStream(f);
			toLocalDisk(key, value, fos);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * Judge whether there is a cached object in memory associated with the
	 * specified key.
	 * 
	 * @param key
	 * @return return true if there has the cached object, otherwise false.
	 */
	public boolean containsInMem(K key) {
		return !(mMemCache.get(getHash(key)) == null);
	}

	/**
	 * Judge whether there is a cached object associated with the specified key.
	 * 
	 * @param key
	 * @return return true if there has the cached object, otherwise false.
	 */
	public boolean contains(K key) {
		return containsInMem(key)
				|| new File(mContext.getCacheDir(), getHash(key)).exists();
	}

	/**
	 * Clear all memory cache objects.
	 * 
	 * @return true if success, otherwise false.
	 */
	boolean clearAllMemCache() {
		mMemCache.evictAll();
		return mMemCache.size() == 0;
	}

	/**
	 * Clear specified memory cache object associated with the key.
	 * 
	 * @param key
	 * @return true if success, otherwise false.
	 */
	public boolean clearMemCache(K key) {
		return mMemCache.remove(getHash(key)) != null;
	}

	/**
	 * Clear the cached object associated with the specified key.
	 * 
	 * <p>
	 * <em>Attention:</em> this method will clear the disk cache and memory
	 * cache.
	 * </p>
	 * 
	 * @param key
	 * @return true if clear successful, otherwise false.
	 */
	public boolean clear(K key) {
		File f = new File(mContext.getCacheDir(), getHash(key));
		if (!f.exists()) {
			return true;
		}

		return f.delete() && clearMemCache(key);
	}

	public boolean clearAll() {
		return clearAllMemCache();
	}

	/**
	 * Clear all the cached object in local disk.
	 * 
	 * <p>
	 * <em>Attention:</em> this method will clear all disk cache and all memory
	 * cache.
	 * </p>
	 * 
	 * @return true if clear successful, otherwise false.
	 */
	public boolean clearMemAndLocal() {
		boolean success = true;

		for (File cacheFile : mContext.getCacheDir().listFiles()) {
			if (!cacheFile.delete()) {
				success = false;
			}
		}

		return success && clearAllMemCache();
	}

	/**
	 * Set cache timeout.
	 * 
	 * @param cacheTimeout
	 */
	// TODO maybe separate disk cache and memory cache later.
	public void setCacheTimeout(long cacheTimeout) {
		mDiskCacheTimeout = cacheTimeout;
	}

	/**
	 * Implement this to do the actual disk reading. Do not close the
	 * InputStream, it will be closed for you.
	 * 
	 * @param key
	 *            the key used to identify the object which will be cached.
	 * @param is
	 *            the stream from which we read the cached object.
	 * @return
	 */
	protected abstract V fromLocalDisk(K key, InputStream is)
			throws IOException;

	/**
	 * Implement this to do the actual disk writing. Do not close the
	 * OutputStream, it will be closed for you.
	 * 
	 * @param key
	 *            the key used to identify the object which will be cached.
	 * @param value
	 *            the object will be cached.
	 * @param os
	 *            the stream which we put value into.
	 */
	protected abstract void toLocalDisk(K key, V value, OutputStream os);

}