package com.ebay.runtime.arguments;

public enum Platform {
	IOS, ANDROID, ANDROID_KOTLIN, MWEB, SITE;

	public static Platform getPlatformFrom(String value) {

		for (Platform platformValue : Platform.values()) {
			if (platformValue.name().equalsIgnoreCase(value)) {
				return platformValue;
			}
		}

		throw new IllegalArgumentException(
				String.format("Platform %s is not recognized. Please specify a valid platform.", value));
	}
}
