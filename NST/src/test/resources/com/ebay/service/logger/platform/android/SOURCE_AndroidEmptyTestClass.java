package com.ebay.service.logger.platform.android;

import androidx.test.filters.LargeTest;

import com.ebay.CreateCheckoutSession;
import com.ebay.PurchaseCheckoutSession;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import com.ebay.nst.NSTServiceTestBase;

/**
 * AndroidEmptyTestClass.java
 * <p>
 * eBay Created by <author> on 07/16/19
 * <p>
 * Copyright 2019 eBay. All rights reserved.
 */
@LargeTest
@PreconditionUser
@PreconditionCountry(siteId = EbaySite.SITE_ID.US)
public class AndroidEmptyTestClass extends AndroidBaseClass
{
	private final String assertionMessage
		= "Option was not successfully selected.";

	@Test
	@TestCase(id = "C2152776Edit")
	@Ignore("Unimplemented test.")
	public void firstTestMethod() throws Throwable
	{
		// TODO: implement test
	}

	@Test
	@TestCase(id = "C2152776Edit")
	@Ignore("Unimplemented test.")
	public void secondTestMethod() throws Throwable
	{
		// TODO: implement test
	}

	/**
	 * Simple private method for evaluation purposes.,
	 */
	private void doSomething()
	{
		int b = 0;
		b++;
	}
}
