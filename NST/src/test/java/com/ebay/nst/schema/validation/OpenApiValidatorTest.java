package com.ebay.nst.schema.validation;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.AllowAdditionalProperties;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.Payload;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.StatusCode;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.utility.ResourceParser;
import org.testng.annotations.Test;

public class OpenApiValidatorTest {

  @Test(groups = "unitTest")
  public void evaluateEnumPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/enumValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void evaluateEnumFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/enumValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumFail.json"));
  }

  @Test(groups = "unitTest")
  public void minMaxPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/minMaxValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/minMaxPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void minFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/minMaxValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/minFail.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void maxFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/minMaxValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/maxFail.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void typeCheckFailNumber() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/typeCheckValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/typeCheckFailNumber.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void typeCheckFailObject() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/typeCheckValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/typeCheckFailObject.json"));
  }

  @Test(groups = "unitTest")
  public void requiredFieldPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/requiredFieldValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/requiredFieldPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void requiredFieldFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/requiredFieldValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/requiredFieldFail.json"));
  }

  @Test(groups = "unitTest")
  public void nonNullableObjectPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/nullValueValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nonNullableObjectPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void nonNullableObjectFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/nullValueValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nonNullableObjectFail.json"));
  }

  @Test(groups = "unitTest")
  public void nullableObjectNullPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/nullValueValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nullableObjectNullPass.json"));
  }

  @Test(groups = "unitTest")
  public void nullableStringNullPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/nullValueValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nullableStringNullPass.json"));
  }

  @Test(groups = "unitTest")
  public void regexPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/regexValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/regexPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void regexFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/regexValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/regexFail.json"));
  }

  @Test(groups = "unitTest")
  public void additionalPropertiesPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/additionalPropertiesValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/additionalPropertiesPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void additionalPropertiesFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/additionalPropertiesValidation.yaml", "/test", NstRequestType.GET).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/additionalPropertiesFail.json"));
  }

  @Test(groups = "unitTest")
  public void arrayValidationPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/arrayValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/arrayValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void arrayValidationFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/arrayValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/arrayValidationFail.json"));
  }

  @Test(groups = "unitTest")
  public void allOfValidationPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/allOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationPass.json"));
  }

  @Test(groups = "unitTest")
  public void allOfValidationPassWithPartialModelData() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/allOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationPassWithPartialModelData.json"));
  }

  @Test(groups = "unitTest")
  public void allOfValidationAllowUnknownFieldsWhenEnabled() throws Throwable  {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/allOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationFailWithExtraProperty.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void allOfValidationFailWithExtraProperty() throws Throwable  {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/allOfValidation.yaml", "/test", NstRequestType.GET)
        .allowAdditionalProperties(AllowAdditionalProperties.NO)
        .build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationFailWithExtraProperty.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void allOfValidationFailWithWrongType() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/allOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationFailWithWrongType.json"));
  }

  @Test(groups = "unitTest")
  public void oneOfValidationPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/oneOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/oneOfValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void oneOfValidationFailUnknownType() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/oneOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/oneOfValidationFailUnknownType.json"));
  }

  @Test(groups = "unitTest")
  public void anyOfValidationPassWithAllFields() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/anyOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationPassWithAllFields.json"));
  }

  @Test(groups = "unitTest")
  public void anyOfValidationPassWithOneField() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/anyOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationPassWithOneField.json"));
  }

  @Test(groups = "unitTest")
  public void anyOfValidationAllowUnknownFieldsWhenEnabled() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/anyOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationFailWithUnknownField.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void anyOfValidationFailWithUnknownField() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/anyOfValidation.yaml", "/test", NstRequestType.GET)
        .allowAdditionalProperties(AllowAdditionalProperties.NO)
        .build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationFailWithUnknownField.json"));
  }

  @Test(groups = "unitTest")
  public void anyOfValidationWithCompositeTypesAndNoAdditionalProperties() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/anyOfCompositesValidation.yaml", "/test", NstRequestType.GET)
        .allowAdditionalProperties(AllowAdditionalProperties.NO)
        .build();
    validator.validate(ResourceParser
        .readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationPassWithCompositeTypesNoAdditionalProperties.json"));
  }

  @Test(groups = "unitTest")
  public void mapWithStandardTypeValidationPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/mapWithStandardTypeValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithStandardTypeValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void mapWithStandardTypeValidationFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/mapWithStandardTypeValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithStandardTypeValidationFail.json"));
  }

  @Test(groups = "unitTest")
  public void mapWithAnyOfValidationPass() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/mapWithAnyOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithAnyOfValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void mapWithAnyOfValidationFail() throws Throwable {
    OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/mapWithAnyOfValidation.yaml", "/test", NstRequestType.GET).build();
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithAnyOfValidationFail.json"));
  }
  
  @Test(groups = "unitTest")
  public void requestModelValidationPass() throws Throwable {
	  OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/requestSchema.yaml", "/test", NstRequestType.POST).setPayload(Payload.REQUEST).build();
	    validator.validate(ResourceParser
	        .readInResourceFile("/com/ebay/schema/validation/responses/requestSchemaPass.json"));
  }
  
  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void requestModelValidationFail() throws Throwable {
	  OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/requestSchema.yaml", "/test", NstRequestType.POST).setPayload(Payload.REQUEST).build();
	    validator.validate(ResourceParser
	        .readInResourceFile("/com/ebay/schema/validation/responses/requestSchemaFail.json")); 
  }
  
  @Test(groups = "unitTest")
  public void responseModelWithAlternateStatusCode() throws Throwable {
	  OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/responseSchemaWith400StatusCode.yaml", "/test", NstRequestType.GET).setStatusCode(StatusCode._400).build();
	    validator.validate(ResourceParser
	        .readInResourceFile("/com/ebay/schema/validation/responses/responseSchema400Pass.json"));
  }
  
  @Test(groups = "unitTest")
  public void responseModelWithoutAdditionalPropertiesPass() throws Throwable {
	  OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/responseSchemaWith400StatusCode.yaml", "/test", NstRequestType.GET).setStatusCode(StatusCode._400).allowAdditionalProperties(AllowAdditionalProperties.YES).build();
	    validator.validate(ResourceParser
	        .readInResourceFile("/com/ebay/schema/validation/responses/responseSchema400Pass.json"));
  }
  
  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void responseModelWithoutAdditionalPropertiesFail() throws Throwable {
	  OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder("/com/ebay/schema/validation/schemas/responseSchemaWith400StatusCode.yaml", "/test", NstRequestType.GET).setStatusCode(StatusCode._400).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
	    validator.validate(ResourceParser
	        .readInResourceFile("/com/ebay/schema/validation/responses/responseSchema400AdditionalPropertiesFail.json"));
  }
}
