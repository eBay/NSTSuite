package com.nst.tutorials.graphql;

import com.ebay.jsonpath.JPIntegerCheck;
import com.ebay.jsonpath.JPListOfStringCheck;
import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.nst.NSTServiceModelBase;
import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

public class ExampleGraphQLResponseModel extends NSTServiceModelBase {

    public ExampleGraphQLResponseModel(JSONObject jsonRoot, SoftAssert softAssert) {
        super(jsonRoot, softAssert);
    }

    @Override
    protected void validate(SoftAssert softAssert) {
        generatedValidations(softAssert);
    }

    // TMB Generated Validation Method
    private void generatedValidations(SoftAssert softAssert) {

        Map<String, JsonPathExecutor> validations = new HashMap<>();
        validations.put("$.data.explore.ssms.hits.total", new JPIntegerCheck());
        validations.put("$.data.explore.ssms.hits.edges[*].node.gene_aa_change[*]", new JPListOfStringCheck());
        validations.put("$.data.explore.ssms.hits.edges[*].node.ssm_id", new JPListOfStringCheck());
        evaluateJsonPaths(validations, softAssert);
    }
}
