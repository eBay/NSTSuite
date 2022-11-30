package com.ebay.runtime.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ebay.runtime.RuntimeConfigValue;

public class DisableConsoleLogArgument implements RuntimeConfigValue<List<DisableConsoleLog>> {

	public static final String KEY = "disableLogToConsole";
	private List<DisableConsoleLog> disableLogToConsole;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public List<DisableConsoleLog> getRuntimeArgumentValue() {
		return disableLogToConsole;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		
		Set<DisableConsoleLog> parsedValues = new HashSet<>();

		if (argumentValue != null) {
			String[] values = argumentValue.split("\\|");
			for (String v : values) {
				for (DisableConsoleLog enumValue : DisableConsoleLog.values()) {
					if (enumValue.name().equalsIgnoreCase(v.trim())) {
						parsedValues.add(enumValue);
						break;
					}
				}
			}
		} else {
			parsedValues.add(DisableConsoleLog.NONE);
		}

		disableLogToConsole = new ArrayList<>(parsedValues);
	}

	@Override
	public List<DisableConsoleLog> override(List<DisableConsoleLog> value) {
		disableLogToConsole = value;
		return Collections.unmodifiableList(disableLogToConsole);
	}

}
