//
// eBay
//
// Created by <author> on 11/15/19.
// Copyright 2019 eBay. All rights reserved.
//

import One
import Two
import Three
import XCTest

class SimpleCheckoutExampleTest : ServiceTestBase {
	
	// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
	let one = One()
	let two = Two()
	let three = Three()
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK
	
	func test_replaceAllOperations() {
	
		// AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS
		inject(responses: [
			"CheckoutModule.EnterCheckoutRequest_1": "IosFuiTestLoggerTest_replaceAllOperations_1_One.har",
			"CheckoutModule.SetPaymentRequest_2": "IosFuiTestLoggerTest_replaceAllOperations_2_Two.har",
			"CheckoutModule.PurchaseCheckoutRequest_3": "IosFuiTestLoggerTest_replaceAllOperations_3_ThreeSession.har"
			])
		// END OF AUTO GENERATED MOCK INJECTION CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: One
		// OPERATION INDEX: 1
		one.enterCheckout()
		// END OF AUTO GENERATED METHOD CODE BLOCK
		
		one.validateHub()
		
		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: Two
		// OPERATION INDEX: 2
		two.navigateTo()
		// END OF AUTO GENERATED METHOD CODE BLOCK
		
		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: Three
		// OPERATION INDEX: 3
		three.completePurchase()
		// END OF AUTO GENERATED METHOD CODE BLOCK
		
		three.extraSpecificValidation()
	}
	
	func test_secondTest() {
		testRailCaseId = "2027566"
		XCTAssert(false, "Unimplemented test.")
	}
	
	func test_thirdTest() {
		testRailCaseId = "2027567"
		XCTAssert(false, "Unimplemented test.")
	}
	
	
}
