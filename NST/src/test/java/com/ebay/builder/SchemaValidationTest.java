package com.ebay.builder;

import java.net.URL;

import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.JsonSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator;
import com.ebay.utility.ResourceParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class SchemaValidationTest {

    private final String SCHEMA_PATH = "/schema/testSchema.json";
    private final String VALID_RESPONSE_PATH = "response/testSchema_ValidResponse.json";
    private final String INVALID_RESPONSE_PATH = "response/testSchema_InValidResponse.json";

    @Test(expectedExceptions = ProcessingException.class, groups = "unitTest")
    public void validateInvalidResponseWithFilePath() throws Exception {
        new SchemaValidation.Construct()
                .setResponse(convertStringFromFile(INVALID_RESPONSE_PATH))
                .setSchemaValidator(new JsonSchemaValidator(SCHEMA_PATH))
                .finishConstruct().validate();
    }

    @Test(expectedExceptions = ProcessingException.class, groups = "unitTest")
    public void validateInvalidResponseWithNode() throws Exception {
        JsonNode schemaNode = JsonLoader.fromResource(SCHEMA_PATH);
        new SchemaValidation.Construct()
                .setResponse(convertStringFromFile(INVALID_RESPONSE_PATH))
                .setSchemaValidator(new JsonSchemaValidator(schemaNode))
                .finishConstruct().validate();
    }

    @Test(expectedExceptions = InvalidSchemaValidationException.class, groups = "unitTest")
    public void emptyResponse() throws Exception {
        JsonNode schemaNode = JsonLoader.fromResource(SCHEMA_PATH);
        new SchemaValidation.Construct()
                .setResponse("")
                .setSchemaValidator(new JsonSchemaValidator(schemaNode))
                .finishConstruct().validate();
    }

    @Test(expectedExceptions = NullPointerException.class, groups = "unitTest")
    public void nullSchemaNode() throws Exception {
        new SchemaValidation.Construct()
                .setResponse("{}")
                .setSchemaValidator(null)
                .finishConstruct().validate();
    }

    @Test(expectedExceptions = NullPointerException.class, groups = "unitTest")
    public void nullNstSchemaValidator() throws Exception {
      String response = ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumFail.json");
      new SchemaValidation.Construct().setResponse(response).setSchemaValidator(null).finishConstruct().validate();
    }

    @Test(expectedExceptions = InvalidSchemaValidationException.class, groups = "unitTest")
    public void noResponseSpecified() throws Exception {
      OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/enumValidation.yaml", "/test", NstRequestType.GET).build();
      new SchemaValidation.Construct().setSchemaValidator(validator).finishConstruct().validate();
    }

    @Test(expectedExceptions = InvalidSchemaValidationException.class, groups = "unitTest")
    public void noNstSchemaValidatorSpecified() throws Exception {
      String response = ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumFail.json");
      new SchemaValidation.Construct().setResponse(response).finishConstruct().validate();
    }

    @Test(expectedExceptions = NullPointerException.class, groups = "unitTest")
    public void nullResponse() throws Exception {
        new SchemaValidation.Construct()
                .setResponse(null)
                .setSchemaValidator(new JsonSchemaValidator(SCHEMA_PATH))
                .finishConstruct().validate();
    }

    @Test(groups = "unitTest")
    public void validateValidResponseWithFilePath() throws Exception {
        new SchemaValidation.Construct()
                .setResponse(convertStringFromFile(VALID_RESPONSE_PATH))
                .setSchemaValidator(new JsonSchemaValidator(SCHEMA_PATH))
                .finishConstruct().validate();
    }

    @Test(groups = "unitTest")
    public void validateValidResponseWithNode() throws Exception {
        JsonNode schemaNode = JsonLoader.fromResource(SCHEMA_PATH);
        new SchemaValidation.Construct()
                .setResponse(convertStringFromFile(VALID_RESPONSE_PATH))
                .setSchemaValidator(new JsonSchemaValidator(schemaNode))
                .finishConstruct().validate();
    }

    @Test(groups = "unitTest", expectedExceptions = ProcessingException.class)
    public void failedNstSchemaValidator() throws Exception {

      String response = ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumFail.json");
      OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/enumValidation.yaml", "/test", NstRequestType.GET).build();
      new SchemaValidation.Construct().setResponse(response).setSchemaValidator(validator).finishConstruct().validate();

    }

    private String convertStringFromFile(String filePath) throws Exception {
        URL url = Resources.getResource(filePath);
        return Resources.toString(url, Charsets.UTF_8);
    }


}
