//
// eBay
//
// Created by <author> on 11/15/19.
// Copyright 2019 eBay. All rights reserved.
//

import EnterCheckoutSession
import SetPayment
import PurchaseCheckoutSession
import XCTest

class SimpleCheckoutExampleTest : ServiceTestBase {

	// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
	let enterCheckoutSession = EnterCheckoutSession()
	let setPayment = SetPayment()
	let purchaseCheckoutSession = PurchaseCheckoutSession()
	// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK

	func test_additionalOperationStatementAdded() {
		
		// AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS
		inject(responses: [
			"CheckoutModule.EnterCheckoutRequest_1": "IosAdditionalOperationStatement_additionalOperationStatementAdded_1_EnterCheckout.har",
			"CheckoutModule.SetPaymentRequest_2": "IosAdditionalOperationStatement_additionalOperationStatementAdded_2_SetPayment.har",
			"CheckoutModule.PurchaseCheckoutRequest_3": "IosAdditionalOperationStatement_additionalOperationStatementAdded_3_PurchaseCheckout.har"
			])
		// END OF AUTO GENERATED MOCK INJECTION CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: EnterCheckout
		// OPERATION INDEX: 1
		enterCheckoutSession.enterCheckout()
		// END OF AUTO GENERATED METHOD CODE BLOCK

		enterCheckoutSession.validate()

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: SetPayment
		// OPERATION INDEX: 2
		setPayment.navigateTo()
		validate(setPayment)
		// END OF AUTO GENERATED METHOD CODE BLOCK

		// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
		// CORRESPONDING API CALL: PurchaseCheckout
		// OPERATION INDEX: 3
		purchaseCheckoutSession.completePurchase()
		// END OF AUTO GENERATED METHOD CODE BLOCK

		purchaseCheckoutSession.extraSpecificValidation()
	}

}
