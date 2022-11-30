package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class SchemaValidationArgument implements RuntimeConfigValue<Boolean> {

	public static final String KEY = "schemavalidation";
	private boolean validateSchema = true;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public Boolean getRuntimeArgumentValue() {
		return validateSchema;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
	    validateSchema = argumentValue == null ? true : Boolean.parseBoolean(argumentValue);

	}

	@Override
	public Boolean override(Boolean value) {
		
		if (value == null || value == false) {
			validateSchema = false;
		} else {
			validateSchema = true;
		}
		return validateSchema;
	}
}
