//
// eBay
//
// Created by <author> on 11/15/19.
// Copyright 2019 eBay. All rights reserved.
//

import PurchaseCheckoutSession
import EnterCheckoutSession
import XCTest

class SimpleCheckoutExampleTest : ServiceTestBase {

	// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
	let purchaseCheckoutSession = PurchaseCheckoutSession()
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK

	func test_removeTheFirstOperation() {
		
		// AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS
		inject(responses: [
			"CheckoutModule.PurchaseCheckoutRequest_1": "IosFuiTestLoggerTest_removeTheFirstOperation_1_PurchaseCheckout.har"
			])
		// END OF AUTO GENERATED MOCK INJECTION CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: PurchaseCheckout
		// OPERATION INDEX: 1
		purchaseCheckoutSession.enterCheckoutSuccess()
		// END OF AUTO GENERATED METHOD CODE BLOCK

		purchaseCheckoutSession.extraSpecificValidation()
	}

}
