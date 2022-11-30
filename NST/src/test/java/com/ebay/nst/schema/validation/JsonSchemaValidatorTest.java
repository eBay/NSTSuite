package com.ebay.nst.schema.validation;

import org.testng.annotations.Test;

import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.utility.ResourceParser;

public class JsonSchemaValidatorTest {

  @Test(groups = "unitTest")
  public void evaluateEnumPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/enumValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void evaluateEnumFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/enumValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/evaluateEnumFail.json"));
  }

  @Test(groups = "unitTest")
  public void minMaxPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/minMaxValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/minMaxPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void minFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/minMaxValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/minFail.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void maxFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/minMaxValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/maxFail.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void typeCheckFailNumber() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/typeCheckValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/typeCheckFailNumber.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void typeCheckFailObject() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/typeCheckValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/typeCheckFailObject.json"));
  }

  @Test(groups = "unitTest")
  public void requiredFieldPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/requiredFieldValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/requiredFieldPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void requiredFieldFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/requiredFieldValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/requiredFieldFail.json"));
  }

  @Test(groups = "unitTest")
  public void nonNullableObjectPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/nullValueValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nonNullableObjectPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void nonNullableObjectFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/nullValueValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nonNullableObjectFail.json"));
  }

  @Test(groups = "unitTest")
  public void nullableObjectNullPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/nullValueValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nullableObjectNullPass.json"));
  }

  @Test(groups = "unitTest")
  public void nullableStringNullPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/nullValueValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/nullableStringNullPass.json"));
  }

  @Test(groups = "unitTest")
  public void regexPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/regexValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/regexPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void regexFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/regexValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/regexFail.json"));
  }

  @Test(groups = "unitTest")
  public void additionalPropertiesPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/additionalPropertiesValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/additionalPropertiesPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void additionalPropertiesFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/additionalPropertiesValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/additionalPropertiesFail.json"));
  }

  @Test(groups = "unitTest")
  public void arrayValidationPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/arrayValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/arrayValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void arrayValidationFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/arrayValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/arrayValidationFail.json"));
  }

  @Test(groups = "unitTest")
  public void allOfValidationPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/allOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationPass.json"));
  }

  @Test(groups = "unitTest")
  public void allOfValidationPassWithPartialModelData() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/allOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationPassWithPartialModelData.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void allOfValidationFailWithExtraProperty() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/allOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationFailWithExtraProperty.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void allOfValidationFailWithWrongType() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/allOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/allOfValidationFailWithWrongType.json"));
  }

  @Test(groups = "unitTest")
  public void oneOfValidationPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/oneOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/oneOfValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void oneOfValidationFailUnknownType() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/oneOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/oneOfValidationFailUnknownType.json"));
  }

  @Test(groups = "unitTest")
  public void anyOfValidationPassWithAllFields() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/anyOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationPassWithAllFields.json"));
  }

  @Test(groups = "unitTest")
  public void anyOfValidaitonPassWithOneField() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/anyOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationPassWithOneField.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void anyOfValidationFailWithUnknownField() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/anyOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/anyOfValidationFailWithUnknownField.json"));
  }

  @Test(groups = "unitTest")
  public void mapWithStandardTypeValidationPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/mapWithStandardTypeValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithStandardTypeValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void mapWithStandardTypeValidationFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/mapWithStandardTypeValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithStandardTypeValidationFail.json"));
  }

  @Test(groups = "unitTest")
  public void mapWithAnyOfValidationPass() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/mapWithAnyOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithAnyOfValidationPass.json"));
  }

  @Test(groups = "unitTest", expectedExceptions = SchemaValidationException.class)
  public void mapWithAnyOfValidationFail() throws Throwable {
    JsonSchemaValidator validator = new JsonSchemaValidator("/com/ebay/schema/validation/schemas/mapWithAnyOfValidation.json");
    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/responses/mapWithAnyOfValidationFail.json"));
  }
}
