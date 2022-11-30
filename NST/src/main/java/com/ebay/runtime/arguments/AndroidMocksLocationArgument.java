package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class AndroidMocksLocationArgument implements RuntimeConfigValue<String> {
	
	public static final String KEY = "androidMocksLocation";
	private String androidMocksLocation;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public String getRuntimeArgumentValue() {
		return androidMocksLocation;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		androidMocksLocation = argumentValue;
	}

	@Override
	public String override(String value) {
		androidMocksLocation = value;
		return androidMocksLocation;
	}

}
