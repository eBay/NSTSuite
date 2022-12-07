package com.ebay.nst.tutorials.graphql;

import com.ebay.nst.NSTServiceWrapperProcessor;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class NSTGraphQLExample {

    @Test
    public void exampleGraphQLTest() throws Exception {

        // Setup
        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send CDC request.
        ExampleGraphQLRequest request = new ExampleGraphQLRequest();
        ExampleGraphQLServiceWrapper serviceWrapper = new ExampleGraphQLServiceWrapper(request);
        JSONObject response = serviceProcessor.sendRequestAndGetJSONResponse(serviceWrapper);

        // Evaluate the response using the thin model.
        new ExampleGraphQLResponseModel(response, new SoftAssert());
    }
}
