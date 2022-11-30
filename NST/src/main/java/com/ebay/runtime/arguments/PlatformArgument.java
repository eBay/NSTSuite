package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class PlatformArgument implements RuntimeConfigValue<Platform> {

	public static final String KEY = "nstplatform";
	private Platform platform;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public Platform getRuntimeArgumentValue() {
		return platform;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		platform = argumentValue == null ? Platform.IOS : Platform.getPlatformFrom(argumentValue);
	}

	@Override
	public Platform override(Platform value) {
		platform = value;
		return platform;
	}
}
