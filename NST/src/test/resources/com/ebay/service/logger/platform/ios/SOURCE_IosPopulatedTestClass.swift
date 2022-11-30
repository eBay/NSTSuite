//
//  SOURCE_IosPopulatedTestClass.swift
//  UITests
//
//  Author:
//  Copyright © 2019 eBay. All rights reserved.
//
import XCTest
import ModularUITestSupport

class AddAccount: UITestCase {
    
    var form = FormPageModel()
    
    var page = PageModel()
    
    // AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS
    let xoHubPageModel = XoHubPageModel()
    let pspPageModel = PspPageModel()
    let xoSuccessPageModel = XoSuccessPageModel()
    // END OF AUTO GENERATED MEMBER FIELD CODE BLOCK
    
    
    func testExample() throws {
        
        // AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS
        inject(responses: [
            "PaymentsModule.PPGetWalletListRequest_1": "AddABankAccount_addFirstBankAccount_1_WalletListV2.har",
            "PaymentsModule.WalletAddPaymentDetailsRequest_2": "AddABankAccount_addFirstBankAccount_2_GetBlankPaymentOptionV2.har",
            "PaymentsModule.WalletAddPaymentInstrumentRequest_3": "AddABankAccount_addFirstBankAccount_3_SavePaymentOptionV2.har"
            ])
        // END OF AUTO GENERATED MOCK INJECTION CODE BLOCK
        
        // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
        // CORRESPONDING API CALL: CreateCheckoutSessionV2
        // OPERATION INDEX: 1
        launchWalletEntry()
        // END OF AUTO GENERATED METHOD CODE BLOCK
        
        // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
        // CORRESPONDING API CALL: SetPayment
        // OPERATION INDEX: 2
        page.addPayoutOption.tap()
        // END OF AUTO GENERATED METHOD CODE BLOCK
        
        addBankAccount(withAcountNumber: "123456123456", withRoutingNumber: "021000021")
        XCTAssertEqual(page.notificationText, "You've added a checking account.", "Notification does not indicate a successful addition of user's bank account.")
    }
    
    func testAddAnotherBankAccountDeletesExisting() throws {
        testRailCaseId = "8675309"
        
        // AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS
        inject(responses: [
            "PaymentsModule.PPGetWalletListRequest_1": "AddABankAccount_addAnotherBankAccountDeletesExisting_4_WalletOptionsV2.har",
            "PaymentsModule.WalletAddPaymentDetailsRequest_2": "AddABankAccount_addAnotherBankAccountDeletesExisting_5_GetBlankPaymentOptionV2.har",
            "PaymentsModule.WalletAddPaymentInstrumentRequest_3": "AddABankAccount_addAnotherBankAccountDeletesExisting_6_SavePaymentOptionV2.har"
            ])
        // END OF AUTO GENERATED MOCK INJECTION CODE BLOCK
        
        // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
        // CORRESPONDING API CALL: CreateCheckoutSessionV2
        // OPERATION INDEX: 1
        launchWalletEntry()
        // END OF AUTO GENERATED METHOD CODE BLOCK
        
        validateWalletEntry()
        
        // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS
        // CORRESPONDING API CALL: SetPayment
        // OPERATION INDEX: 2
        tapAddPayoutAccount()
        // END OF AUTO GENERATED METHOD CODE BLOCK
        
        let changePayoutMethodAlert = app.alerts["Change payout method"].waitToExist()
        changePayoutMethodAlert.buttons["Continue"].waitToExist().tap()
        addBankAccount(withAcountNumber: "123456123456", withRoutingNumber: "021000021")
        XCTAssertEqual(page.notificationText, "You've added a checking account.", "Notification does not indicate a successful addition of user's bank account.")
    }
    
    func tapAddPayoutAccount() {
        app.element(containing: "_BankAccount").waitToExist().tap()
    }
    
    func addBankAccount(withAcountNumber: String, withRoutingNumber: String) {
        
        form.bankNickName.paste(text: "EvilBank")
        form.accountUserName.paste(text: "BankVictim")
        form.routingNumber.paste(text: withRoutingNumber)
        form.accountNumber.paste(text: withAcountNumber)
        try! form.tapAddButton()
    }
}