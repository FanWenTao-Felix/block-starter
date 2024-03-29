package com.cs.http.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/**
 * Http Cache 服务
 *
 * @blame csz
 */
public class HttpCacheService implements InitializingBean {
	private final BlockHttpCacheProperties properties;
	private final CacheManager cacheManager;
	private Cache cache;

	public HttpCacheService(BlockHttpCacheProperties properties, CacheManager cacheManager) {
		this.properties = properties;
		this.cacheManager = cacheManager;
	}

	public boolean get(String key) {
		Boolean result = cache.get(key, Boolean.class);
		return result != null && Boolean.TRUE.equals(result);
	}

	public void set(String key) {
		cache.put(key, Boolean.TRUE);
	}

	public void remove(String key) {
		cache.evict(key);
	}

	public void clear() {
		cache.clear();
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(cacheManager, "cacheManager must not be null!");
		String cacheName = properties.getCacheName();
		this.cache = cacheManager.getCache(cacheName);
		Assert.notNull(this.cache, "HttpCacheCache cacheName: " + cacheName + " is not config.");
	}
}
