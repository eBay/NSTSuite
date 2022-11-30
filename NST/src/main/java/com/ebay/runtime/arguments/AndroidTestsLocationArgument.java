package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class AndroidTestsLocationArgument implements RuntimeConfigValue<String> {
	
	public static final String KEY = "androidTestsLocation";
	private String androidTestsLocation;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public String getRuntimeArgumentValue() {
		return androidTestsLocation;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		androidTestsLocation = argumentValue;
	}

	@Override
	public String override(String value) {
		androidTestsLocation = value;
		return androidTestsLocation;
	}

}
