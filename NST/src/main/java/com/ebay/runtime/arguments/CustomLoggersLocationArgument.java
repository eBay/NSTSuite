package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class CustomLoggersLocationArgument implements RuntimeConfigValue<String> {
	
	public static final String KEY = "customLoggersPackage";
	
	private static final String DEFAULT_LOGGER_PACKAGE = "com.ebay.custom.loggers";
	private String packageWithCustomLoggers;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public String getRuntimeArgumentValue() {
		return (packageWithCustomLoggers == null) ? DEFAULT_LOGGER_PACKAGE : packageWithCustomLoggers;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		packageWithCustomLoggers = argumentValue;
	}

	@Override
	public String override(String value) {
		packageWithCustomLoggers = value;
		return packageWithCustomLoggers;
	}

	
}
