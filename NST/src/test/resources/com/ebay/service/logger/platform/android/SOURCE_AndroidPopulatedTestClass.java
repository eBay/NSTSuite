package com.ebay.service.logger.platform.android;

import androidx.test.filters.LargeTest;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import com.ebay.nst.NSTServiceTestBase;
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
	private final String directDebitAssertionMessage
		= "Direct Debit was not successfully selected as the payment method.";
	// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
	@Rule
	public final CheckoutMixedActivityTestRule checkout = new CheckoutMixedActivityTestRule(
		new CheckoutMixedActivityFactory(), true, false, false);
	@Rule
	public final InterceptorFlowRule interceptorFlowRule = new InterceptorFlowRule(requestInterceptors);
	private final XoHubPageModel xoHubPageModel = new XoHubPageModel();
	private final PaymentSelection psp = new PaymentSelection();
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK

	@Test
	@TestCase(id = "C2121462")
	public void firstTestMethod() throws Throwable
	{

		int i = 0;

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: CreateCheckoutSessionV2
		// OPERATION INDEX: 1
		Checkout.launchActivity(getStubCheckoutIntent(getEbayContext()))
		// END OF AUTO GENERATED METHOD CODE BLOCK

		Assert.assertTrue(checkWeAreOnCheckoutHub(), "We are not where we think we are.");
		Assert.assertTrue(checkoutHubHasOneItemInCheckout(), "We do not have a single item in checkout.");

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: SetPaymentInstrument
		// OPERATION INDEX: 2
		CheckoutHub.clickPaymentMethod();
		// END OF AUTO GENERATED METHOD CODE BLOCK

		psp.paymentMethodPrimaryText(PaymentMethodType.DIRECT_DEBIT.name()).perform(click());

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: PurchaseCheckoutSession
		// OPERATION INDEX: 3
		Checkout.purchaseCheckoutSession();
		// END OF AUTO GENERATED METHOD CODE BLOCK

		Assert.assertTrue(checkThatWeCompletedCheckout(), "Checkout was not completed successfully.");
	}

	@Test
	@TestCase(id = "C2152776")
	public void secondTestMethod() throws Throwable
	{
		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: CreateCheckoutSessionV2
		// OPERATION INDEX: 1
		enterCheckoutWithItem();
		// END OF AUTO GENERATED METHOD CODE BLOCK

		Assert.assertTrue(checkWeAreOnCheckoutHub(), "We are not where we think we are.");
		Assert.assertTrue(checkoutHubHasOneItemInCheckout(), "We do not have a single item in checkout.");
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