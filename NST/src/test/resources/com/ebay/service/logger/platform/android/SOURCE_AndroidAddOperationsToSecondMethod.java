package com.ebay.service.logger.platform.android;

import androidx.test.filters.LargeTest;

import com.ebay.AndroidBaseClass;
import com.ebay.test.utils.testrail.TestCase;
import org.junit.Test;

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
	@Test
	@TestCase(id = "C2152776Edit")
	@Ignore("Unimplemented test")
	public void firstTestMethod() throws Throwable
	{
		// TODO: implement test
	}

	@Test
	@TestCase(id = "C2152776Edit")
	@Ignore("Unimplemented test")
	public void secondTestMethod() throws Throwable
	{
		// TODO: implement test
	}
}
