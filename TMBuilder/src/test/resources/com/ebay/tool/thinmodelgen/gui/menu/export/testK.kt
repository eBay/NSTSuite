package com.ebay.tool.thinmodelgen.gui.menu.export

import com.ebay.jsonpath.JPStringCheck
import com.ebay.jsonpath.JsonPathExecutor
import com.ebay.nst.NSTServiceModelBase
import org.json.JSONObject
import org.testng.asserts.SoftAssert

class TestKotlin(jsonRoot: JSONObject, softAssert: SoftAssert) :
    NSTServiceModelBase(jsonRoot, softAssert) {

    override fun validate(softAssert: SoftAssert) {
        generatedValidations(softAssert)
    }

    // TMB Generated Validation Method
    private fun generatedValidations(softAssert: SoftAssert) {
        val validations: MutableMap<String, JsonPathExecutor> = HashMap()
        validations["$.modules.banners._type"] = JPStringCheck()
        evaluateJsonPaths(validations, softAssert)
    }
}