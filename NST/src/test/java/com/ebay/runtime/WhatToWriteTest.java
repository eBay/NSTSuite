package com.ebay.runtime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.service.logger.WhatToWrite;

public class WhatToWriteTest {
	
	@DataProvider(name = "conversionTestValues")
	public Object[][] getConversionTestValues() {
		
		return new Object[][] {
			{ null, new HashSet<>(Arrays.asList(WhatToWrite.NONE)) },
			{ "NONE", new HashSet<>(Arrays.asList(WhatToWrite.NONE)) },
			{ "none",new HashSet<>( Arrays.asList(WhatToWrite.NONE)) },
			{ "NON", new HashSet<>(Arrays.asList(WhatToWrite.NONE)) },
			{ "MOCKS", new HashSet<>(Arrays.asList(WhatToWrite.MOCKS)) },
			{ "TESTS", new HashSet<>(Arrays.asList(WhatToWrite.TESTS)) },
			{ "NONEMOCKSTESTS", new HashSet<>(Arrays.asList(WhatToWrite.NONE)) },
			{ "||||", new HashSet<>(Arrays.asList(WhatToWrite.NONE)) },
			{ "|NONE|", new HashSet<>(Arrays.asList(WhatToWrite.NONE)) },
			{ "NONE|MOCKS|TESTS", new HashSet<>(Arrays.asList(WhatToWrite.NONE, WhatToWrite.MOCKS, WhatToWrite.TESTS)) },
			{ "NONE | MOCKS | TESTS", new HashSet<>(Arrays.asList(WhatToWrite.NONE, WhatToWrite.MOCKS, WhatToWrite.TESTS)) },
			{ "NONE|TESTS", new HashSet<>(Arrays.asList(WhatToWrite.NONE, WhatToWrite.TESTS)) },
			{ "NONE|MOCKS", new HashSet<>(Arrays.asList(WhatToWrite.NONE, WhatToWrite.MOCKS)) },
			{ "MOCKS|TESTS", new HashSet<>(Arrays.asList( WhatToWrite.MOCKS, WhatToWrite.TESTS)) },
		};
	}

	@Test(dataProvider = "conversionTestValues")
	public void convertStringToEnumValues(String inputString, Set<WhatToWrite> expected) {
	
		List<WhatToWrite> actual = WhatToWrite.fromString(inputString);
		Set<WhatToWrite> actualSet = new HashSet<WhatToWrite>(actual);
		assertThat(actualSet, is(equalTo(expected)));
	}
}
