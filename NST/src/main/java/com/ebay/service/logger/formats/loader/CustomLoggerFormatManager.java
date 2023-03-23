package com.ebay.service.logger.formats.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.platforms.AndroidLogger;
import com.ebay.service.logger.platforms.HarLogger;
import com.ebay.service.logger.platforms.IosLogger;

public class CustomLoggerFormatManager {

	private static CustomLoggerFormatManager instance;
	private Map<Platform, FormatWriter> platformWriters = new HashMap<>();

	private CustomLoggerFormatManager() {

		init();
	}

	/**
	 * Get the singleton instance.
	 * 
	 * @return Singleton instance.
	 */
	public static CustomLoggerFormatManager getInstance() {
		if (instance == null) {
			synchronized (CustomLoggerFormatManager.class) {
				if (instance == null) {
					instance = new CustomLoggerFormatManager();
				}
			}
		}

		return instance;
	}

	/**
	 * Reinitialize the manager.
	 * 
	 * @return Current instance.
	 */
	protected CustomLoggerFormatManager reinitialize() {
		init();
		return this;
	}

	/**
	 * Get the format writer for the platform tests are currently executing as.
	 * 
	 * @return Platform specific logger.
	 */
	public FormatWriter getFormatWriter() {
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		return platformWriters.get(platform);
	}

	/**
	 * Get a logger by platform.
	 * 
	 * @param platform Platform to get logger for.
	 * @return Associated logger.
	 */
	public FormatWriter getFormatWriter(Platform platform) {
		return platformWriters.get(platform);
	}

	/**
	 * Get all of the registered platform loggers.
	 * 
	 * @return All registerd platform loggers.
	 */
	public Map<Platform, FormatWriter> getPlatformLoggers() {
		return Collections.unmodifiableMap(platformWriters);
	}

	private void init() {

		// Default is always the har logger.
		for (Platform platform : Platform.values()) {
			platformWriters.put(platform, new HarLogger());
		}
		
		// Add the Android and iOS loggers.
		platformWriters.put(Platform.IOS, new IosLogger());
		platformWriters.put(Platform.ANDROID, new AndroidLogger());

		String customLoggerPackage = RuntimeConfigManager.getInstance().getCustomLoggerFormatPackage();
		if (customLoggerPackage == null) {
			return;
		}

		Reflections reflections = new Reflections(customLoggerPackage);
		Set<Class<? extends FormatWriter>> loggers = reflections.getSubTypesOf(FormatWriter.class);

		for (Class<? extends FormatWriter> logger : loggers) {

			Constructor<?>[] constructors = logger.getDeclaredConstructors();

			for (Constructor<?> constructor : constructors) {
				if (constructor.getGenericParameterTypes().length == 0) {
					try {
						constructor.setAccessible(true);
						FormatWriter formatWriter = (FormatWriter) constructor.newInstance();
						platformWriters.put(formatWriter.getPlatformAssociation(), formatWriter);
						break;
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace();
						break;
					} catch (InvocationTargetException e) {
						e.getTargetException().printStackTrace();
						e.printStackTrace();
						break;
					}
				}
			}
		}
	}
}
