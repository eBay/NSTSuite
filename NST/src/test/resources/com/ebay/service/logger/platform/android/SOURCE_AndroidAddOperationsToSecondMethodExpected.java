package com.ebay.service.logger.platform.android;

import com.test.EnterCheckoutSession;
import com.test.PurchaseCheckoutSession;
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

	// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
	EnterCheckoutSession enterCheckoutSession = new EnterCheckoutSession();
	PurchaseCheckoutSession purchaseCheckoutSession = new PurchaseCheckoutSession();
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK

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

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: EnterCheckout
		// OPERATION INDEX: 1
		enterCheckoutSession.enterCheckout();
		// END OF AUTO GENERATED METHOD CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: PurchaseCheckout
		// OPERATION INDEX: 2
		purchaseCheckoutSession.completePurchase();
		// END OF AUTO GENERATED METHOD CODE BLOCK

	}
}
