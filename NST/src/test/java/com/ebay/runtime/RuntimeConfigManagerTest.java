package com.ebay.runtime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.nst.hosts.manager.HostsManager;
import com.ebay.nst.hosts.manager.PoolType;
import com.ebay.runtime.arguments.DisableConsoleLog;
import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.WhatToWrite;

public class RuntimeConfigManagerTest {

	private static final String ANDROID_MOCKS_LOCATION = "androidMocksLocation";
	private static final String ANDROID_TESTS_LOCATION = "androidTestsLocation";
	private static final String CLIENT_VERSION = "clientVersion";
	private static final String DISABLE_CONSOLE_LOGGING = "disableLogToConsole";
	private static final String IOS_MOCKS_LOCATION = "iosMocksLocation";
	private static final String IOS_TESTS_LOCATION = "iosTestsLocation";
	private static final String NST_PLATFORM = "nstplatform";
	private static final String POOL_TYPE = "testExeEnv";
	private static final String SCHEMA_VALIDATION = "schemavalidation";
	private static final String SERVICE_VERSION = "serviceVersion";
	private static final String SITE = "site";
	private static final String WHAT_TO_WRITE = "whatToWrite";
	private static final String CUSTOM_LOGGERS_PACKAGE = "customLoggersPackage";

	@BeforeMethod(alwaysRun = true)
	@AfterMethod(alwaysRun = true)
	public void resetBeforeAndAfterEachTest() {
		clearSystemProperties();
	}

	@AfterClass(alwaysRun = true)
	public void resetAtEnd() throws IllegalStateException, URISyntaxException, IOException {
		clearSystemProperties();
		HostsManager.getInstance().reinitialize();
		RuntimeConfigManager.getInstance().reinitialize();
	}
	
	private void clearSystemProperties() {
		System.clearProperty(ANDROID_MOCKS_LOCATION);
		System.clearProperty(ANDROID_TESTS_LOCATION);
		System.clearProperty(CLIENT_VERSION);
		System.clearProperty(DISABLE_CONSOLE_LOGGING);
		System.clearProperty(IOS_MOCKS_LOCATION);
		System.clearProperty(IOS_TESTS_LOCATION);
		System.clearProperty(NST_PLATFORM);
		System.clearProperty(POOL_TYPE);
		System.clearProperty(SCHEMA_VALIDATION);
		System.clearProperty(SERVICE_VERSION);
		System.clearProperty(SITE);
		System.clearProperty(WHAT_TO_WRITE);
		System.clearProperty(CUSTOM_LOGGERS_PACKAGE);
		System.clearProperty(CustomArgument.KEY);
	}
	
	@Test(groups = "unitTest")
	public void confirmClearingOfProperties() {
		
		System.setProperty(NST_PLATFORM, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.ANDROID)));
		
		System.clearProperty(NST_PLATFORM);
		RuntimeConfigManager.getInstance().reinitialize();
		platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.IOS)));
	}

	// ANDROID_MOCKS_LOCATION

	@Test(groups = "unitTest")
	public void androidMocksLocationNotSet() {
		System.clearProperty(ANDROID_MOCKS_LOCATION);
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getAndroidMocksLocation();
		assertThat(whereToWrite, is(nullValue()));
	}

	@Test(groups = "unitTest")
	public void path() {
		String path = "/Users/test/path/to/write/to";
		System.setProperty(ANDROID_MOCKS_LOCATION, path);
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getAndroidMocksLocation();
		assertThat(whereToWrite, is(equalTo(path)));
	}

	@Test(groups = "unitTest")
	public void androidTestsLocationNotSet() {
		System.setProperty(NST_PLATFORM, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getAndroidMocksLocation();
		assertThat(whereToWrite, is(nullValue()));
	}

	// ANDROID_TESTS_LOCATION

	@Test(groups = "unitTest")
	public void androidFuiPathNotSpecified() {
		System.clearProperty(ANDROID_TESTS_LOCATION);
		RuntimeConfigManager.getInstance().reinitialize();
		String path = RuntimeConfigManager.getInstance().getAndroidTestsLocation();
		assertThat(path, is(nullValue()));
	}

	@Test(groups = "unitTest")
	public void androidFuiPathSpecified() {
		System.setProperty(ANDROID_TESTS_LOCATION, "../andr_core/src/test/");
		RuntimeConfigManager.getInstance().reinitialize();
		String path = RuntimeConfigManager.getInstance().getAndroidTestsLocation();
		assertThat(path, is(equalTo("../andr_core/src/test/")));
	}

	// Disable console logging tests

	@Test(groups = "unitTest")
	public void noDisableConsoleLogValuesSpecified() {
		RuntimeConfigManager.getInstance().reinitialize();
		List<DisableConsoleLog> values = RuntimeConfigManager.getInstance().getDisableConsoleLogValues();
		assertThat(values, is(equalTo(Arrays.asList(DisableConsoleLog.NONE))));
	}

	@Test(groups = "unitTest")
	public void singleDisableConsoleLogValueSpecified() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();
		List<DisableConsoleLog> values = RuntimeConfigManager.getInstance().getDisableConsoleLogValues();
		assertThat(values, is(equalTo(Arrays.asList(DisableConsoleLog.RESPONSE_PAYLOAD))));
	}

	@Test(groups = "unitTest")
	public void multipleDisableConsoleLogValuesSpecified() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "RESPONSE_PAYLOAD|SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();
		List<DisableConsoleLog> values = RuntimeConfigManager.getInstance().getDisableConsoleLogValues();
		assertThat(new HashSet<>(values),
				is(equalTo(new HashSet<>(Arrays.asList(DisableConsoleLog.RESPONSE_PAYLOAD, DisableConsoleLog.SERVICE_CONFIG)))));
	}

	@Test(groups = "unitTest")
	public void multipleDisableConsoleLogValuesDoNotIncludeDuplicates() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "RESPONSE_PAYLOAD|RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();
		List<DisableConsoleLog> values = RuntimeConfigManager.getInstance().getDisableConsoleLogValues();
		assertThat(values, is(equalTo(Arrays.asList(DisableConsoleLog.RESPONSE_PAYLOAD))));
	}

	@Test(groups = "unitTest")
	public void unknownDisableConsoleLogValue() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "FOO");
		RuntimeConfigManager.getInstance().reinitialize();
		List<DisableConsoleLog> values = RuntimeConfigManager.getInstance().getDisableConsoleLogValues();
		assertThat(values.isEmpty(), is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void unknownDisableConsoleLogValueAmongstMultipleValues() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "FOO|RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();
		List<DisableConsoleLog> values = RuntimeConfigManager.getInstance().getDisableConsoleLogValues();
		assertThat(values, is(equalTo(Arrays.asList(DisableConsoleLog.RESPONSE_PAYLOAD))));
	}

	@Test(groups = "unitTest")
	public void checkForDisableConsoleLogValueSet() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();
		boolean disabled = RuntimeConfigManager.getInstance()
				.isConsoleLogDisabledFor(DisableConsoleLog.RESPONSE_PAYLOAD);
		assertThat(disabled, is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void checkForDisableConsoleLogValueNotSet() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();
		boolean disabled = RuntimeConfigManager.getInstance().isConsoleLogDisabledFor(DisableConsoleLog.SERVICE_CONFIG);
		assertThat(disabled, is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void checkForDisableConsoleLogValueNotSetToNoneWhenValuesSpecified() {
		System.setProperty(DISABLE_CONSOLE_LOGGING, "RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();
		boolean disabled = RuntimeConfigManager.getInstance().isConsoleLogDisabledFor(DisableConsoleLog.NONE);
		assertThat(disabled, is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void checkForDisableConsoleLogValueSetToNoneWhenNoValuesSpecified() {
		RuntimeConfigManager.getInstance().reinitialize();
		boolean disabled = RuntimeConfigManager.getInstance().isConsoleLogDisabledFor(DisableConsoleLog.NONE);
		assertThat(disabled, is(equalTo(true)));
	}

	// IOS_MOCKS_LOCATION

	@Test(groups = "unitTest")
	public void whereToWriteNotSet() {
		System.clearProperty(IOS_MOCKS_LOCATION);
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getIosMocksLocation();
		assertThat(whereToWrite, is(nullValue()));
	}

	@Test(groups = "unitTest")
	public void whereToWriteSpecified() {
		String path = "/Users/test/path/to/write/to";
		System.setProperty(IOS_MOCKS_LOCATION, path);
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getIosMocksLocation();
		assertThat(whereToWrite, is(equalTo(path)));
	}

	@Test(groups = "unitTest")
	public void whereToWriteIosWithoutIosPlatformSet() {
		String path = "/Users/ios/path/to/platform";
		System.setProperty(IOS_MOCKS_LOCATION, path);
		System.setProperty(NST_PLATFORM, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getIosMocksLocation();
		assertThat(whereToWrite, is(equalTo(path)));
	}

	@Test(groups = "unitTest")
	public void whereToWriteIosWithoutIosPathSpecified() {
		System.setProperty(NST_PLATFORM, "IOS");
		RuntimeConfigManager.getInstance().reinitialize();
		String whereToWrite = RuntimeConfigManager.getInstance().getIosMocksLocation();
		assertThat(whereToWrite, is(nullValue()));
	}

	// IOS_TESTS_LOCATION

	@Test(groups = "unitTest")
	public void iosFuiPathNotSpecified() {
		System.clearProperty(IOS_TESTS_LOCATION);
		RuntimeConfigManager.getInstance().reinitialize();
		String path = RuntimeConfigManager.getInstance().getIosTestsLocation();
		assertThat(path, is(nullValue()));
	}

	@Test(groups = "unitTest")
	public void iosFuiPathSpecified() {
		System.setProperty(IOS_TESTS_LOCATION, "../ios_core/src/test/");
		RuntimeConfigManager.getInstance().reinitialize();
		String path = RuntimeConfigManager.getInstance().getIosTestsLocation();
		assertThat(path, is(equalTo("../ios_core/src/test/")));
	}

	// Platform Key

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void platformKeySetToInvalid() {
		System.setProperty(NST_PLATFORM, "FOO");
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.IOS)));
	}

	@Test(groups = "unitTest")
	public void platformKeyNotSet() {
		System.clearProperty(NST_PLATFORM);
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.IOS)));
	}

	@Test(groups = "unitTest")
	public void platformKeySetToIOS() {
		System.setProperty(NST_PLATFORM, "ios");
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.IOS)));
	}

	@Test(groups = "unitTest")
	public void platformKeySetToIOSAlternateCaps() {
		System.setProperty(NST_PLATFORM, "IoS");
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.IOS)));
	}

	@Test(groups = "unitTest")
	public void platformKeySetToANDROID() {
		System.setProperty(NST_PLATFORM, "android");
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.ANDROID)));
	}

	@Test(groups = "unitTest")
	public void platformKeySetToMWEB() {
		System.setProperty(NST_PLATFORM, "mweb");
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.MWEB)));
	}

	@Test(groups = "unitTest")
	public void platformKeySetToSITE() {
		System.setProperty(NST_PLATFORM, SITE);
		RuntimeConfigManager.getInstance().reinitialize();
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.SITE)));
	}

	@Test(groups = "unitTest")
	public void platformKeyOverride() {
		System.setProperty(NST_PLATFORM, "ios");
		RuntimeConfigManager.getInstance().reinitialize();
		RuntimeConfigManager.getInstance().overridePlatform(Platform.ANDROID);
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		assertThat(platform, is(equalTo(Platform.ANDROID)));
	}

	// POOL_TYPE

	@Test(groups = "unitTest")
	public void defaultPoolType() throws Exception {
		HostsManager.getInstance().reinitialize();
		RuntimeConfigManager.getInstance().reinitialize();
		PoolType poolType = RuntimeConfigManager.getInstance().getPoolType();
		assertThat(poolType, is(equalTo(PoolType.QA)));
	}

	@Test(groups = "unitTest")
	public void poolTypeRuntimeArgumentSet() throws Exception {
		System.setProperty(POOL_TYPE, "PROD");
		HostsManager.getInstance().reinitialize();
		RuntimeConfigManager.getInstance().reinitialize();
		RuntimeConfigManager.getInstance().reinitialize();
		PoolType poolType = RuntimeConfigManager.getInstance().getPoolType();
		assertThat(poolType, is(equalTo(PoolType.PROD)));
	}

	// SCHEMA_VALIDATION

	@Test(groups = "unitTest")
	public void schemaValidationOff() {
		System.setProperty(SCHEMA_VALIDATION, "false");
		RuntimeConfigManager.getInstance().reinitialize();
		boolean validateSchema = RuntimeConfigManager.getInstance().validateSchema();
		assertThat(validateSchema, is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void schemaValidationOn() {
		System.setProperty(SCHEMA_VALIDATION, "true");
		RuntimeConfigManager.getInstance().reinitialize();
		boolean validateSchema = RuntimeConfigManager.getInstance().validateSchema();
		assertThat(validateSchema, is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void schemaValidationNotSpecified() {
		System.clearProperty(SCHEMA_VALIDATION);
		RuntimeConfigManager.getInstance().reinitialize();
		boolean validateSchema = RuntimeConfigManager.getInstance().validateSchema();
		assertThat(validateSchema, is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void schemaValidationNonBooleanFlagValue() {
		System.setProperty(SCHEMA_VALIDATION, "BLAH");
		RuntimeConfigManager.getInstance().reinitialize();
		boolean validateSchema = RuntimeConfigManager.getInstance().validateSchema();
		assertThat(validateSchema, is(equalTo(false)));
	}

	// WHAT_TO_WRITE

	@Test(groups = "unitTest")
	public void whatToWriteNotSet() {
		System.clearProperty(WHAT_TO_WRITE);
		RuntimeConfigManager.getInstance().reinitialize();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		assertThat(whatToWrite, is(equalTo(Arrays.asList(WhatToWrite.NONE))));
	}

	@Test(groups = "unitTest")
	public void whatToWriteSetToNone() {
		System.setProperty(WHAT_TO_WRITE, "NONE");
		RuntimeConfigManager.getInstance().reinitialize();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		assertThat(whatToWrite, is(equalTo(Arrays.asList(WhatToWrite.NONE))));
	}

	@Test(groups = "unitTest")
	public void whatToWriteSetToNoneWithRandomCase() {
		System.setProperty(WHAT_TO_WRITE, "MoCkS");
		RuntimeConfigManager.getInstance().reinitialize();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		assertThat(whatToWrite, is(equalTo(Arrays.asList(WhatToWrite.MOCKS))));
	}

	@Test(groups = "unitTest")
	public void whatToWriteSetToMocks() {
		System.setProperty(WHAT_TO_WRITE, "MOCKS");
		RuntimeConfigManager.getInstance().reinitialize();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		assertThat(whatToWrite, is(equalTo(Arrays.asList(WhatToWrite.MOCKS))));
	}

	@Test(groups = "unitTest")
	public void whatToWriteSetToTests() {
		System.setProperty(WHAT_TO_WRITE, "TESTS");
		RuntimeConfigManager.getInstance().reinitialize();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		assertThat(whatToWrite, is(equalTo(Arrays.asList(WhatToWrite.TESTS))));
	}

	@Test(groups = "unitTest")
	public void whatToWriteSetToTestsAndMocks() {
		System.setProperty(WHAT_TO_WRITE, "MOCKS|TESTS");
		RuntimeConfigManager.getInstance().reinitialize();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		Set<WhatToWrite> whatToWriteSet = new HashSet<>(whatToWrite);
		assertThat(whatToWriteSet, is(equalTo(new HashSet<>(Arrays.asList(WhatToWrite.MOCKS, WhatToWrite.TESTS)))));
	}
	
	// CUSTOM_LOGGERS_PACKAGE
	
	@Test
	public void customLoggersPackageDefaultValue() {
		
		RuntimeConfigManager.getInstance().reinitialize();
		String actualPackageValue = RuntimeConfigManager.getInstance().getCustomLoggerFormatPackage();
		assertThat(actualPackageValue, is(equalTo("com.ebay.custom.loggers")));
	}
	
	@Test
	public void customLoggersPackageSet() {
		
		String packageValue = "com.ebay.test";
		System.setProperty(CUSTOM_LOGGERS_PACKAGE, packageValue);
		RuntimeConfigManager.getInstance().reinitialize();
		String actualPackageValue = RuntimeConfigManager.getInstance().getCustomLoggerFormatPackage();
		assertThat(actualPackageValue, is(equalTo(packageValue)));
	}
	
	// Custom argument override
	
	@Test
	public void customArgumentOverride() {
		
		System.setProperty(CustomArgument.KEY, "true");
		RuntimeConfigManager.getInstance().addRuntimeArgument(new CustomArgument());

		CustomArgument argument = (CustomArgument) RuntimeConfigManager.getInstance().getRuntimeArgument(CustomArgument.KEY);
		Boolean runtimeValue = argument.getRuntimeArgumentValue();
		assertThat(runtimeValue, is(equalTo(true)));
		
		Boolean overrideValue = argument.override(false);
		assertThat(overrideValue, is(equalTo(false)));
		
		argument = (CustomArgument) RuntimeConfigManager.getInstance().getRuntimeArgument(CustomArgument.KEY);
		runtimeValue = argument.getRuntimeArgumentValue();
		assertThat(runtimeValue, is(equalTo(false)));
		
		RuntimeConfigManager.getInstance().reinitialize();
		argument = (CustomArgument) RuntimeConfigManager.getInstance().getRuntimeArgument(CustomArgument.KEY);
		runtimeValue = argument.getRuntimeArgumentValue();
		assertThat(runtimeValue, is(equalTo(true)));
	}
	
	class CustomArgument implements RuntimeConfigValue<Boolean> {
		
		public static final String KEY = "CUSTOM_ARGUMENT";
		private boolean value;

		@Override
		public Boolean getRuntimeArgumentValue() {
			return value;
		}

		@Override
		public Boolean override(Boolean value) {
			
			this.value = (value == null) ? false : value;
			return this.value;
		}

		@Override
		public String getRuntimeArgumentKey() {
			return KEY;
		}

		@Override
		public void parseRuntimeArgument(String argumentValue) {
			
			this.value = Boolean.valueOf(argumentValue);
		}
		
	}
}
