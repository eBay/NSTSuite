package com.ebay.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ebay.nst.hosts.manager.PoolType;
import com.ebay.runtime.arguments.AndroidMocksLocationArgument;
import com.ebay.runtime.arguments.AndroidTestsLocationArgument;
import com.ebay.runtime.arguments.CustomLoggersLocationArgument;
import com.ebay.runtime.arguments.DisableConsoleLog;
import com.ebay.runtime.arguments.DisableConsoleLogArgument;
import com.ebay.runtime.arguments.IosMocksLocationArgument;
import com.ebay.runtime.arguments.IosTestsLocationArgument;
import com.ebay.runtime.arguments.Platform;
import com.ebay.runtime.arguments.PlatformArgument;
import com.ebay.runtime.arguments.PoolTypeArgument;
import com.ebay.runtime.arguments.SchemaValidationArgument;
import com.ebay.runtime.arguments.WhatToWriteArguments;
import com.ebay.service.logger.WhatToWrite;

public class RuntimeConfigManager {

	// Singleton instance
	private static RuntimeConfigManager instance = null;

	Map<String, RuntimeConfigValue<?>> arguments = new HashMap<>();

	private RuntimeConfigManager() {
		arguments.put(AndroidMocksLocationArgument.KEY, new AndroidMocksLocationArgument());
		arguments.put(AndroidTestsLocationArgument.KEY, new AndroidTestsLocationArgument());
		arguments.put(DisableConsoleLogArgument.KEY, new DisableConsoleLogArgument());
		arguments.put(IosMocksLocationArgument.KEY, new IosMocksLocationArgument());
		arguments.put(IosTestsLocationArgument.KEY, new IosTestsLocationArgument());
		arguments.put(PlatformArgument.KEY, new PlatformArgument());
		arguments.put(PoolTypeArgument.KEY, new PoolTypeArgument());
		arguments.put(SchemaValidationArgument.KEY, new SchemaValidationArgument());
		arguments.put(WhatToWriteArguments.KEY, new WhatToWriteArguments());
		arguments.put(CustomLoggersLocationArgument.KEY, new CustomLoggersLocationArgument());

		init();
	}

	public static RuntimeConfigManager getInstance() {

		if (instance == null) {
			synchronized (RuntimeConfigManager.class) {
				if (instance == null) {
					instance = new RuntimeConfigManager();
				}
			}
		}

		return instance;
	}

	/**
	 * Add a custom runtime config option. You MUST call reinitialize() after the
	 * last runtime argument is added to have the manager pickup those runtime
	 * values, if specified by the user.
	 * 
	 * If the runtime argument already exists the new one is ignored. Overriding
	 * existing runtime arguments is NOT allowed.
	 * 
	 * Retrieve the runtime argument value via: getRuntimeArgumentValue(). Default
	 * values are retrievable via the RuntimeConfigManager getter methods.
	 * 
	 * @param value Runtime config option.
	 */
	public void addRuntimeArgument(RuntimeConfigValue<?> value) {
		if (!arguments.containsKey(value.getRuntimeArgumentKey())) {
			arguments.put(value.getRuntimeArgumentKey(), value);
		}
	}

	/**
	 * Get a runtime argument by it's key.
	 * 
	 * @param key Key associated with the runtime argument.
	 * @return Runtime argument instance, or null if not found.
	 */
	public RuntimeConfigValue<?> getRuntimeArgument(String key) {
		return arguments.get(key);
	}

	/**
	 * Get a runtime config value. Use this to retrieve custom runtime config
	 * values. Default config options are accessible via the RuntimeConfigManager
	 * getter methods.
	 * 
	 * @param runtimeargumentKey Runtime argument key.
	 * @return Runtime argument value, or null if not set.
	 */
	public Object getRuntimeArgumentValue(String runtimeargumentKey) {
		return arguments.get(runtimeargumentKey).getRuntimeArgumentValue();
	}

	/**
	 * Reset the runtime arguments to the default/command line values specified.
	 * Call this after internally overriding values or to pickup custom arguments
	 * added via addRuntimeArgument().
	 */
	public void reinitialize() {
		init();
	}

	/**
	 * Returns runtime argument for the environment/pool type targeted. This changes
	 * the host lookup at runtime.
	 * 
	 * @return Environment/pool type targeted.
	 */
	public PoolType getPoolType() {
		return (PoolType) arguments.get(PoolTypeArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Check if schema validation should occur.
	 * 
	 * @return True if schema validation should occur, false otherwise.
	 */
	public boolean validateSchema() {
		Boolean validateSchema = (Boolean) arguments.get(SchemaValidationArgument.KEY).getRuntimeArgumentValue();
		return (validateSchema == null) ? true : validateSchema;
	}

	/**
	 * Get the platform to run as.
	 * 
	 * @return Platform to run as.
	 */
	public Platform getPlatform() {
		return (Platform) arguments.get(PlatformArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Override, for the duration of the consuming test case, the platform the
	 * service calls should be run as.
	 * 
	 * @param platform Platform override.
	 */
	public void overridePlatform(Platform platform) {
		((PlatformArgument) arguments.get(PlatformArgument.KEY)).override(platform);
	}

	/**
	 * Get what to write with regards to request and response logging.
	 * 
	 * @return What to write.
	 */
	@SuppressWarnings("unchecked")
	public List<WhatToWrite> getWhatToWrite() {
		return (List<WhatToWrite>) arguments.get(WhatToWriteArguments.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Path to the iOS FUI tests location with the test classes to update.
	 * 
	 * @return Path to iOS FUI tests, or null if not specified.
	 */
	public String getIosTestsLocation() {
		return (String) arguments.get(IosTestsLocationArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Path to the iOS mock response location where iOS mocks are to be written.
	 * 
	 * @return Path to iOS mock response location, or null if not specified.
	 */
	public String getIosMocksLocation() {
		return (String) arguments.get(IosMocksLocationArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Path to the Android FUI tests location with the test classes to update.
	 * 
	 * @return Path to Android FUI tests, or null if not specified.
	 */
	public String getAndroidTestsLocation() {
		return (String) arguments.get(AndroidTestsLocationArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Path to the Android mock response location where Android mocks are to be
	 * written.
	 * 
	 * @return Path to Android mock resposne location, or null if not specified
	 */
	public String getAndroidMocksLocation() {
		return (String) arguments.get(AndroidMocksLocationArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Get all of the disable console log values specified at runtime.
	 * 
	 * @return Disable consol log values.
	 */
	@SuppressWarnings("unchecked")
	public List<DisableConsoleLog> getDisableConsoleLogValues() {
		return (List<DisableConsoleLog>) arguments.get(DisableConsoleLogArgument.KEY).getRuntimeArgumentValue();
	}

	/**
	 * Check if the specific disable console log value is set.
	 * 
	 * @param value Value to check for.
	 * @return True if it is set, false otherwise.
	 */
	public boolean isConsoleLogDisabledFor(DisableConsoleLog value) {
		return getDisableConsoleLogValues().contains(value);
	}

	/**
	 * Get the custom logger format package for loading custom loggers.
	 * 
	 * @return Custom logger format package or null if not set.
	 */
	public String getCustomLoggerFormatPackage() {
		return (String) arguments.get(CustomLoggersLocationArgument.KEY).getRuntimeArgumentValue();
	}

	private void init() {

		String value;
		for (Entry<String, RuntimeConfigValue<?>> entry : arguments.entrySet()) {
			value = System.getProperty(entry.getValue().getRuntimeArgumentKey());
			entry.getValue().parseRuntimeArgument(value);
		}
	}
}
