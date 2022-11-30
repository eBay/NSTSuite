package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class IosMocksLocationArgument implements RuntimeConfigValue<String> {
	
	public static final String KEY = "iosMocksLocation";
	 private String iosMocksLocaiton;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public String getRuntimeArgumentValue() {
		return iosMocksLocaiton;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		iosMocksLocaiton = argumentValue;
	}

	@Override
	public String override(String value) {
		iosMocksLocaiton = value;
		return iosMocksLocaiton;
	}

}
