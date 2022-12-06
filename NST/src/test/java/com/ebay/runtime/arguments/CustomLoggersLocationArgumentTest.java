package com.ebay.runtime.arguments;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CustomLoggersLocationArgumentTest {
	
	private CustomLoggersLocationArgument logger;
	
	@BeforeMethod(alwaysRun = true)
	public void reset() {
		logger = new CustomLoggersLocationArgument();
	}
	
	@Test
	public void getKey() {
		String key = logger.getRuntimeArgumentKey();
		assertThat(key, is(equalTo(CustomLoggersLocationArgument.KEY)));
	}

	@Test
	public void defaultPackageReturned() {
		String value = logger.getRuntimeArgumentValue();
		assertThat(value, is(equalTo("com.ebay.custom.loggers")));
	}
	
	@Test
	public void customPackageReturned() {
		String input = "com.ebay.test";
		logger.parseRuntimeArgument(input);
		String value = logger.getRuntimeArgumentValue();
		assertThat(value, is(equalTo(input)));
	}
	
	@Test
	public void overridePackage() {
		String input = "com.ebay.test";
		String override = "com.ebay.override";
		logger.parseRuntimeArgument(input);
		logger.override(override);
		String value = logger.getRuntimeArgumentValue();
		assertThat(value, is(equalTo(override)));
	}
}
