package com.ebay.runtime.arguments;

/**
 * SERVICE_CONFIG may be used by adopters to turn off service configuration logging on their end.
 * NSTest has no way to query adopter service configurations.
 *
 * RESPONSE_PAYLOAD disables all response logging by NSTest.
 */
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
