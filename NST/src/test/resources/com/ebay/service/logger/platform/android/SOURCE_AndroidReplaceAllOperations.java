package com.ebay.service.logger.platform.android;

import androidx.test.filters.LargeTest;

import com.test.DummyObject;
import com.test.CreateDummyObject;
import com.test.PurchaseDummyObject;
import org.json.JSONObject;
import org.junit.Assert;
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
	CreateDummyObject createDummyObject = new CreateDummyObject();
	PurchaseDummyObject purchaseDummyObject = new PurchaseDummyObject();
	DummyObject dummyObject = new DummyObject();
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK
	
	private final String assertionMessage
		= "Test case was unsuccessful.";

	@Test
	@TestCase(id = "C2121462")
	public void firstTestMethod() throws Throwable
	{

		// Special sauce comment
		int i = 0;

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: CreateDummyThing
		// OPERATION INDEX: 1
		BOGUS JUNK
		// END OF AUTO GENERATED METHOD CODE BLOCK

		validate(createDummyObject);

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: DummyThing
		// OPERATION INDEX: 2
		dummyObject.doSomethingStupid();
		dummyObject.doAnotherStupidThing();
		// END OF AUTO GENERATED METHOD CODE BLOCK

		validate(dummyObject);

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: PurchaseDummyThing
		// OPERATION INDEX: 3
		MORE BOGUS JUNK
		// END OF AUTO GENERATED METHOD CODE BLOCK

		validate(purchaseDummyObject);
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