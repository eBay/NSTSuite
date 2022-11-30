package com.ebay.runtime.arguments;

public enum DisableConsoleLog {
	NONE, RESPONSE_PAYLOAD, SERVICE_CONFIG;

	public static DisableConsoleLog fromValue(String value) {
		for (DisableConsoleLog enumValue : DisableConsoleLog.values()) {
			if (enumValue.name().equalsIgnoreCase(value)) {
				return enumValue;
			}
		}
		return NONE;
	}
}
