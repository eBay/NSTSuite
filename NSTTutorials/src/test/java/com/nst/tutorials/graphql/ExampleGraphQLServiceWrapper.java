package com.nst.tutorials.graphql;

import com.ebay.nst.graphql.NSTGraphQLRequest;
import com.ebay.nst.graphql.NSTGraphQLServiceWrapper;
import com.ebay.nst.schema.validation.GraphQLSchemaValidator;
import com.ebay.nst.schema.validation.GraphQLSchemaValidator.OperationType;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExampleGraphQLServiceWrapper implements NSTGraphQLServiceWrapper {

    private ExampleGraphQLRequest request;

    public ExampleGraphQLServiceWrapper(ExampleGraphQLRequest request) {
        this.request = request;
    }

    @Override
    public String getServiceName() {
        return "cdc";
    }

    @Override
    public String getEndpointPath() {
        return "/v0/graphql";
    }

    @Override
    public GraphQLSchemaValidator getSchemaValidator() {

        URL url = this.getClass().getResource("/com/ebay/schema/example/graphql/cdcschema.graphql");

        try {
            File schemaFile = new File(url.toURI());
            return new GraphQLSchemaValidator(schemaFile, OperationType.QUERY, "explore");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, String> getAdditionalHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        return headers;
    }

    @Override
    public NSTGraphQLRequest getRequest() {
        return request;
    }

}
