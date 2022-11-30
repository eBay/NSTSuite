package com.ebay.service.logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum WhatToWrite {
	NONE, MOCKS, TESTS;

	/**
	 * Convert a string of values, each separated by | into an list of enum values.
	 * Values not matched to enum values will be ignored.
	 * 
	 * @param value Enum values, separated by |, to parse.
	 * @return Parsed enum values.
	 */
	public static List<WhatToWrite> fromString(String value) {

		Set<WhatToWrite> parsedValues = new HashSet<>();

		if (value != null) {
			String[] values = value.split("\\|");
			for (String v : values) {
				for (WhatToWrite enumValue : WhatToWrite.values()) {
					if (enumValue.name().equalsIgnoreCase(v.trim())) {
						parsedValues.add(enumValue);
						break;
					}
				}
			}
		}
		
		if (parsedValues.isEmpty()) {
			parsedValues.add(WhatToWrite.NONE);
		}

		return new ArrayList<>(parsedValues);
	}
}
