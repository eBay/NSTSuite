package com.ebay.runtime.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ebay.runtime.RuntimeConfigValue;
import com.ebay.service.logger.WhatToWrite;

public class WhatToWriteArguments implements RuntimeConfigValue<List<WhatToWrite>> {

	public static final String KEY = "whatToWrite";
	private List<WhatToWrite> whatToWrite = new ArrayList<>();
	
	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public List<WhatToWrite> getRuntimeArgumentValue() {
		return Collections.unmodifiableList(whatToWrite);
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {
		whatToWrite = argumentValue == null ? Arrays.asList(WhatToWrite.NONE) : WhatToWrite.fromString(argumentValue);
	}

	@Override
	public List<WhatToWrite> override(List<WhatToWrite> value) {
		whatToWrite = new ArrayList<>(value);
		return Collections.unmodifiableList(whatToWrite);
	}

}
