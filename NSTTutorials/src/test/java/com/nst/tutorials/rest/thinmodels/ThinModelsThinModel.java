package com.nst.tutorials.rest.thinmodels;

import com.ebay.jsonpath.JPIntegerCheck;
import com.ebay.jsonpath.JPStringCheck;
import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.nst.NSTServiceModelBase;
import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

public class ThinModelsThinModel extends NSTServiceModelBase {

    public ThinModelsThinModel(JSONObject jsonRoot, SoftAssert softAssert) {
        super(jsonRoot, softAssert);
    }

    @Override
    protected void validate(SoftAssert softAssert) {

        generatedValidations(softAssert);
    }

    public Integer getId() {
        return readJsonPath("$.holiday.id");
    }

    // TMB Generated Validation Method
    private void generatedValidations(SoftAssert softAssert) {

        Map<String, JsonPathExecutor> validations = new HashMap<>();
        validations.put("$.holiday.id", new JPIntegerCheck().isEqualTo(15));
        validations.put("$.holiday.nameEn", new JPStringCheck().isEqualTo("Canada Day"));
        evaluateJsonPaths(validations, softAssert);
    }
}
