package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class IosTestsLocationArgument implements RuntimeConfigValue<String> {
	
	public static final String KEY = "iosTestsLocation";
	private String iosTestsLocation;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public String getRuntimeArgumentValue() {
		return iosTestsLocation;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		iosTestsLocation = argumentValue;
	}

	@Override
	public String override(String value) {
		iosTestsLocation = value;
		return iosTestsLocation;
	}

}
