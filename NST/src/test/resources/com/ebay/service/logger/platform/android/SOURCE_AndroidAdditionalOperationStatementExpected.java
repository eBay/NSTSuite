package com.ebay.service.logger.platform.android;

import com.test.EnterCheckoutSession;
import com.test.SetPayment;
import com.test.PurchaseCheckoutSession;
import androidx.test.filters.LargeTest;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import com.ebay.test.utils.testrail.TestCase;

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
	SetPayment setPayment = new SetPayment();
	PurchaseCheckoutSession purchaseCheckoutSession = new PurchaseCheckoutSession();
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK
	
	private final String assertionMessage
		= "Test case was unsuccessful.";

	@Test
	@TestCase(id = "C2152776Edit")
	public void firstTestMethod() throws Throwable
	{

		// Special sauce comment
		int i = 0;

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: EnterCheckout
		// OPERATION INDEX: 1
		enterCheckoutSession.enterCheckout();
		// END OF AUTO GENERATED METHOD CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: SetPayment
		// OPERATION INDEX: 2
		setPayment.navigateTo();
		validate(setPayment);
		// END OF AUTO GENERATED METHOD CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: PurchaseCheckout
		// OPERATION INDEX: 3
		purchaseCheckoutSession.completePurchase();
		// END OF AUTO GENERATED METHOD CODE BLOCK

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
