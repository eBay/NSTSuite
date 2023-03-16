package com.ebay.runtime;

import com.ebay.nst.hosts.manager.PoolType;
import com.ebay.runtime.arguments.*;
import com.ebay.service.logger.WhatToWrite;
import org.testng.ITestContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeConfigManager {

	private ITestContext testContext = null;

	private Map<String, RuntimeConfigValue<?>> arguments = new ConcurrentHashMap<>();

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

	/**
	 * For the cases where a user is running their tests in parallel we need the RuntimeConfigurationManager singleton
	 * to be a unique instance per thread. Using ThreadLocal gives us the ability to have a unique instance per thread.
	 * https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html
	 *
	 * This ensures that overrides or reinitialize operations will NOT have bleed over into the other test threads.
	 */
	private static final ThreadLocal<RuntimeConfigManager> threadId = new ThreadLocal<RuntimeConfigManager>() {
		@Override
		protected RuntimeConfigManager initialValue() {
			return new RuntimeConfigManager();
		}
	};

	public static RuntimeConfigManager getInstance() {
		return threadId.get();
	}

	/**
	 * Set the TestNG test context for use when reinitializing the RuntimeConfigManager.
	 * Setting this will utilize any TestNG parameters that are set.
	 * @param testContext The TestNG test context object.
	 */
	public void setTestContext(ITestContext testContext) {
		this.testContext = testContext;
	}

	/**
	 * Clear the current instance of ITestContext that would be used to parse runtime parameters.
	 */
	public void clearTestContext() {
		this.testContext = null;
	}

	/**
	 * Add a custom runtime config option. This will call reinitialize() to have the
	 * manager pickup those runtime values, if specified by the user.
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
			reinitialize();
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
	 *
	 * Note that if the TestNG test context is set, then the RuntimeConfigManager will utilize any parameters
	 * defined in the TestNG test suite XML file. However, any runtime parameters defined at the system level will
	 * override these parameters / take precedence.
	 */
	public void reinitialize() {
		init();

		if (testContext != null) {
			for (String key : arguments.keySet()) {
				String parameter = testContext.getCurrentXmlTest().getParameter(key);
				RuntimeConfigValue platformValue = RuntimeConfigManager.getInstance().getRuntimeArgument(key);

				if (parameter != null && !parameter.isEmpty()) {
					platformValue.parseRuntimeArgument(parameter);
				}

				String value = System.getProperty(key);
				if (value != null && !value.isEmpty()) {
					platformValue.parseRuntimeArgument(value);
				}
			}
		}
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
		String runtimeArgumentKey;
		// Avoid concurrent modification exception on 'arguments' collection
		// by updating a new map and then reinitializing 'arguments' with the
		// contents of the new map.
		Map<String, RuntimeConfigValue<?>> argumentValues = new HashMap<>();
		for (Entry<String, RuntimeConfigValue<?>> entry : arguments.entrySet()) {

			RuntimeConfigValue<?> entryValue = entry.getValue();
			runtimeArgumentKey = entryValue.getRuntimeArgumentKey();

			value = System.getProperty(runtimeArgumentKey);
			entryValue.parseRuntimeArgument(value);

			argumentValues.put(runtimeArgumentKey, entryValue);
		}
		arguments.clear();
		arguments.putAll(argumentValues);
	}
}
