package com.ebay.nst.schema.validation;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.utility.ResourceParser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.testng.annotations.Test;

public class OpenApiSchemaValidatorTest {

    @Test
    public void schemaValidatePassWithRelativePathRefs() throws Exception {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);

        OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/schema/root.yaml", "/test", NstRequestType.GET)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO).setParseOptions(parseOptions)
                .build();

        String testResponsePayload = ResourceParser.readInResourceFile("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/json/validResponse.json");

        validator.validate(testResponsePayload);
    }

    @Test
    public void schemaValidatePassWithAllLocalReferences() throws Exception {

        OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/schema/singleRootDocumentWithReferences.yaml", "/test", NstRequestType.GET)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO)
                .build();

        String testResponsePayload = ResourceParser.readInResourceFile("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/json/validResponse.json");

        validator.validate(testResponsePayload);
    }

    @Test
    public void schemaValidatePassEverythingInline() throws Exception {

        OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/schema/singleRootDocumentInlined.yaml", "/test", NstRequestType.GET)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO)
                .build();

        String testResponsePayload = ResourceParser.readInResourceFile("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/json/validResponse.json");

        validator.validate(testResponsePayload);
    }

    @Test(expectedExceptions = SchemaValidationException.class)
    public void schemaValidateFailWithRelativePathRefs() throws Exception {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);

        OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/schema/root.yaml", "/test", NstRequestType.GET)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO).setParseOptions(parseOptions)
                .build();

        String testResponsePayload = ResourceParser.readInResourceFile("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/json/invalidResponse.json");

        validator.validate(testResponsePayload);
    }

    @Test(expectedExceptions = SchemaValidationException.class)
    public void schemaValidateFailWithAllLocalReferences() throws Exception {

        OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/schema/singleRootDocumentWithReferences.yaml", "/test", NstRequestType.GET)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO)
                .build();

        String testResponsePayload = ResourceParser.readInResourceFile("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/json/invalidResponse.json");

        validator.validate(testResponsePayload);
    }

    @Test(expectedExceptions = SchemaValidationException.class)
    public void schemaValidateFailEverythingInline() throws Exception {

        OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/schema/singleRootDocumentInlined.yaml", "/test", NstRequestType.GET)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO)
                .build();

        String testResponsePayload = ResourceParser.readInResourceFile("/com/ebay/nst/schema/validation/OpenApiSchemaValidatorTest/json/invalidResponse.json");

        validator.validate(testResponsePayload);
    }
}