package com.ebay.service.logger.call.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.runtime.event.AfterTestMethodObserver;
import com.ebay.runtime.event.ObserverPayload;
import com.ebay.runtime.event.TestExecutionEventManager;
import com.ebay.service.logger.ClassAndMethodName;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.FormatWriterUtil;
import com.ebay.service.logger.WhatToWrite;
import com.ebay.service.logger.formats.loader.CustomLoggerFormatManager;

public class ServiceCallCacheManager implements AfterTestMethodObserver {

	private static ServiceCallCacheManager instance;
	private Map<String, List<ServiceCallCacheData>> cacheMap = new HashMap<>();
	private CustomLoggerFormatManager customLoggerFormatManager = CustomLoggerFormatManager.getInstance();

	private ServiceCallCacheManager() {
		TestExecutionEventManager.getInstance().addAfterTestMethodObserver(this);
	}

	/**
	 * Get the singleton instance.
	 * 
	 * @return Singleton instance.
	 */
	public static ServiceCallCacheManager getInstance() {

		if (instance == null) {
			synchronized (ServiceCallCacheManager.class) {
				if (instance == null) {
					instance = new ServiceCallCacheManager();
				}
			}
		}

		return instance;
	}

	/**
	 * Reset to the default CustomLoggerFormatManager.
	 */
	protected void resetCustomLoggerFormatManager() {
		customLoggerFormatManager = CustomLoggerFormatManager.getInstance();
	}

	/**
	 * Setter injection for CustomLoggerFormatManager.
	 * 
	 * @param customLoggerFormatManager Injected instance to use.
	 */
	protected void setCustomLoggerFormatManager(CustomLoggerFormatManager customLoggerFormatManager) {
		this.customLoggerFormatManager = customLoggerFormatManager;
	}

	/**
	 * Clear the cache.
	 */
	public void clearCache() {
		cacheMap.clear();
	}

	/**
	 * Get the cached data.
	 * 
	 * @return Cached data. Key is <pre>&lt;Class name&gt;_&lt;method name&gt;</pre>.
	 */
	public Map<String, List<ServiceCallCacheData>> getCacheData() {
		return Collections.unmodifiableMap(cacheMap);
	}

	/**
	 * Add a call to the call cache. Stores each call in the sequence it was
	 * received.
	 * 
	 * @param cacheData Call data to add to the cache.
	 */
	public synchronized void addCallToCache(ServiceCallCacheData cacheData) {
		String key = getKey();
		List<ServiceCallCacheData> orderedCache = cacheMap.get(key);
		if (orderedCache == null) {
			orderedCache = new ArrayList<>();
		}
		orderedCache.add(cacheData);
		cacheMap.put(key, orderedCache);
	}

	@Override
	public void notifyAfterTestMethodObserver(ObserverPayload payload) {
		
		Objects.requireNonNull(payload, "Payload MUST NOT be null.");

		String key = getKey(payload);
		List<ServiceCallCacheData> cache;

		// Check if logging is enabled - WhatToWrite list does NOT contain NONE
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();

		if (!whatToWrite.contains(WhatToWrite.NONE)) {

			// Get call sequence from map
			synchronized (this) {
				cache = cacheMap.get(key);
			}

			if (cache != null) {

				// Process writing of data through the platform specific writer
				Platform platform = RuntimeConfigManager.getInstance().getPlatform();
				FormatWriter formatWriter = customLoggerFormatManager.getFormatWriter(platform);

				// If requested, write the mocks
				if (whatToWrite.contains(WhatToWrite.MOCKS)) {
					formatWriter.writeMocks(cache, payload.getClassName(), payload.getMethodName());
				}

				// If requested, write the tests
				if (whatToWrite.contains(WhatToWrite.TESTS)) {
					formatWriter.updateTests(cache, payload.getClassName(), payload.getMethodName());
				}
			}
		}

		// Clear cache of call sequence
		synchronized (this) {
			cacheMap.remove(key);
		}
	}

	protected String getKey(ObserverPayload payload) {
		return String.format("%s_%s", payload.getClassName(), payload.getMethodName());
	}

	protected String getKey() {
		ClassAndMethodName classAndMethodName = FormatWriterUtil.getClassAndMethodName();
		return String.format("%s_%s", classAndMethodName.getClassName(), classAndMethodName.getMethodName());
	}
}
