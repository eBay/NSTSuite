package com.ebay.runtime.event;

import org.testng.annotations.Test;

public class ObserverPayloadTest {

	@Test(expectedExceptions = NullPointerException.class)
	public void initializeWithNullClassName() {
		new ObserverPayload(null, "foo");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void initializeWithNullMethodName() {
		new ObserverPayload("foo", null);
	}
}
