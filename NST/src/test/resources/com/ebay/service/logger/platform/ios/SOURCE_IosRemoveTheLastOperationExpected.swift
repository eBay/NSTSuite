//
// eBay
//
// Created by <author> on 11/15/19.
// Copyright 2019 eBay. All rights reserved.
//

import EnterCheckoutSession
import PurchaseCheckoutSession
import XCTest

class SimpleCheckoutExampleTest : ServiceTestBase {

	// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
	let enterCheckoutSession = EnterCheckoutSession()
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK

	func test_removeTheLastOperation() {
		
		// AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS
		inject(responses: [
			"CheckoutModule.EnterCheckoutRequest_1": "IosFuiTestLoggerTest_removeTheLastOperation_1_EnterCheckout.har"
			])
		// END OF AUTO GENERATED MOCK INJECTION CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: EnterCheckout
		// OPERATION INDEX: 1
		enterCheckoutSession.enterCheckout()
		// END OF AUTO GENERATED METHOD CODE BLOCK

		enterCheckoutSession.validate()

	}

}
