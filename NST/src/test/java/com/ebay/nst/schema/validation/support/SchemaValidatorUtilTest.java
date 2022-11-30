package com.ebay.nst.schema.validation.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.AllowAdditionalProperties;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.StatusCode;
import com.ebay.utility.ResourceParser;

public class SchemaValidatorUtilTest {
	
	@Test(groups = "unitTest")
	public void servicePassing()
			throws Throwable {
		OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder(
				"/com/ebay/schema/validation/schemavalidatorutil/schema/largeTestSchema.yaml", "/dosomething",
				NstRequestType.GET).setStatusCode(StatusCode._200).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
		validator.validate(ResourceParser.readInResourceFile(
				"/com/ebay/schema/validation/schemavalidatorutil/response/passing.json"));
	}

	@Test(groups = "unitTest")
	public void polymorphicLeafErrorMessageProcessing() throws Throwable {
		OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder(
				"/com/ebay/schema/validation/schemavalidatorutil/schema/polymorphicErrorProcessing.yaml",
				"/leafLevelPolymorphism", NstRequestType.GET).setStatusCode(StatusCode._200)
				.allowAdditionalProperties(AllowAdditionalProperties.NO).build();
		try {
			validator.validate(ResourceParser.readInResourceFile(
				"/com/ebay/schema/validation/schemavalidatorutil/response/polymorphicLeafErrorResponse.json"));
		} catch (SchemaValidationException e) {
			String actualMessage = e.getMessage();
			String expectedMessage = "Polymorphic failure encountered - specific issue identified:\n"
					+ "Unable to identify _type field. Raw error message shown below.{\n"
					+ "  \"level\" : \"error\",\n"
					+ "  \"schema\" : {\n"
					+ "    \"loadingURI\" : \"#\",\n"
					+ "    \"pointer\" : \"/properties/person/properties/details\"\n"
					+ "  },\n"
					+ "  \"instance\" : {\n"
					+ "    \"pointer\" : \"/person/details\"\n"
					+ "  },\n"
					+ "  \"domain\" : \"validation\",\n"
					+ "  \"keyword\" : \"anyOf\",\n"
					+ "  \"message\" : \"instance failed to match at least one required schema among 2\",\n"
					+ "  \"nrSchemas\" : 2,\n"
					+ "  \"reports\" : {\n"
					+ "    \"/properties/person/properties/details/anyOf/0\" : [ {\n"
					+ "      \"level\" : \"error\",\n"
					+ "      \"schema\" : {\n"
					+ "        \"loadingURI\" : \"#\",\n"
					+ "        \"pointer\" : \"/definitions/typeA\"\n"
					+ "      },\n"
					+ "      \"instance\" : {\n"
					+ "        \"pointer\" : \"/person/details\"\n"
					+ "      },\n"
					+ "      \"domain\" : \"validation\",\n"
					+ "      \"keyword\" : \"type\",\n"
					+ "      \"message\" : \"instance type (boolean) does not match any allowed primitive type (allowed: [\\\"object\\\"])\",\n"
					+ "      \"found\" : \"boolean\",\n"
					+ "      \"expected\" : [ \"object\" ]\n"
					+ "    } ],\n"
					+ "    \"/properties/person/properties/details/anyOf/1\" : [ {\n"
					+ "      \"level\" : \"error\",\n"
					+ "      \"schema\" : {\n"
					+ "        \"loadingURI\" : \"#\",\n"
					+ "        \"pointer\" : \"/definitions/typeB\"\n"
					+ "      },\n"
					+ "      \"instance\" : {\n"
					+ "        \"pointer\" : \"/person/details\"\n"
					+ "      },\n"
					+ "      \"domain\" : \"validation\",\n"
					+ "      \"keyword\" : \"type\",\n"
					+ "      \"message\" : \"instance type (boolean) does not match any allowed primitive type (allowed: [\\\"object\\\"])\",\n"
					+ "      \"found\" : \"boolean\",\n"
					+ "      \"expected\" : [ \"object\" ]\n"
					+ "    } ]\n"
					+ "  }\n"
					+ "}\n"
					+ "\n";
			assertThat(actualMessage, is(equalTo(expectedMessage)));
			return;
		}
		
		Assert.fail("Expected exception.");
	}

	@Test(groups = "unitTest")
	public void polymorphicDeepAnyOfViolation()
			throws Throwable {
		OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder(
				"/com/ebay/schema/validation/schemavalidatorutil/schema/largeTestSchema.yaml", "/dosomething",
				NstRequestType.GET).setStatusCode(StatusCode._200).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
		try {
			validator.validate(ResourceParser.readInResourceFile(
					"/com/ebay/schema/validation/schemavalidatorutil/response/deepAnyOfViolation.json"));
		} catch (SchemaValidationException e) {
			String actualMessage = e.getMessage();
			String expectedMessage = "Polymorphic failure encountered - specific issue identified:\n"
					+ "Unable to identify _type field. Raw error message shown below.{\n"
					+ "  \"level\" : \"error\",\n"
					+ "  \"schema\" : {\n"
					+ "    \"loadingURI\" : \"#\",\n"
					+ "    \"pointer\" : \"/definitions/SomethingA/properties/sports/properties/identifier\"\n"
					+ "  },\n"
					+ "  \"instance\" : {\n"
					+ "    \"pointer\" : \"/somethingA/sports/identifier\"\n"
					+ "  },\n  \"domain\" : \"validation\",\n"
					+ "  \"keyword\" : \"anyOf\",\n"
					+ "  \"message\" : \"instance failed to match at least one required schema among 2\",\n"
					+ "  \"nrSchemas\" : 2,\n"
					+ "  \"reports\" : {\n    \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/0\" : [ {\n"
					+ "      \"level\" : \"error\",\n      \"schema\" : {\n"
					+ "        \"loadingURI\" : \"#\",\n"
					+ "        \"pointer\" : \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/0\"\n"
					+ "      },\n"
					+ "      \"instance\" : {\n"
					+ "        \"pointer\" : \"/somethingA/sports/identifier\"\n"
					+ "      },\n"
					+ "      \"domain\" : \"validation\",\n"
					+ "      \"keyword\" : \"type\",\n"
					+ "      \"message\" : \"instance type (boolean) does not match any allowed primitive type (allowed: [\\\"integer\\\"])\",\n"
					+ "      \"found\" : \"boolean\",\n"
					+ "      \"expected\" : [ \"integer\" ]\n"
					+ "    } ],\n"
					+ "    \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/1\" : [ {\n"
					+ "      \"level\" : \"error\",\n"
					+ "      \"schema\" : {\n"
					+ "        \"loadingURI\" : \"#\",\n"
					+ "        \"pointer\" : \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/1\"\n"
					+ "      },\n"
					+ "      \"instance\" : {\n"
					+ "        \"pointer\" : \"/somethingA/sports/identifier\"\n"
					+ "      },\n"
					+ "      \"domain\" : \"validation\",\n"
					+ "      \"keyword\" : \"type\",\n"
					+ "      \"message\" : \"instance type (boolean) does not match any allowed primitive type (allowed: [\\\"string\\\"])\",\n"
					+ "      \"found\" : \"boolean\",\n"
					+ "      \"expected\" : [ \"string\" ]\n"
					+ "    } ]\n"
					+ "  }\n}\n\n";
			
			assertThat(actualMessage, is(equalTo(expectedMessage)));
			return;
		}
		
		Assert.fail("Expected exception.");
	}
	
	@Test(groups = "unitTest")
	public void typeMatchFail()
			throws Throwable {
		OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder(
				"/com/ebay/schema/validation/schemavalidatorutil/schema/largeTestSchema.yaml", "/dosomething",
				NstRequestType.GET).setStatusCode(StatusCode._200).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
		
		try {
			validator.validate(ResourceParser.readInResourceFile(
				"/com/ebay/schema/validation/schemavalidatorutil/response/typeMatchFail.json"));
		} catch (SchemaValidationException e) {
			String actualMessage = e.getMessage();
			
			String expectedMessage = "error: instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])\n"
					+ "    level: \"error\"\n"
					+ "    schema: {\"loadingURI\":\"#\",\"pointer\":\"/definitions/Person/properties/name\"}\n"
					+ "    instance: {\"pointer\":\"/somethingC/people/0/name\"}\n"
					+ "    domain: \"validation\"\n"
					+ "    keyword: \"type\"\n"
					+ "    found: \"integer\"\n"
					+ "    expected: [\"string\"]\n\n\n";
			
			assertThat(actualMessage, is(equalTo(expectedMessage)));
			return;
		}
		
		Assert.fail("Expected exception.");
	}
	
	@Test(groups = "unitTest")
	public void deepNullValue()
			throws Throwable {
		OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder(
				"/com/ebay/schema/validation/schemavalidatorutil/schema/largeTestSchema.yaml", "/dosomething",
				NstRequestType.GET).setStatusCode(StatusCode._200).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
		
		try {
			validator.validate(ResourceParser.readInResourceFile(
				"/com/ebay/schema/validation/schemavalidatorutil/response/deepNullValue.json"));
		} catch (SchemaValidationException e) {
			String actualMessage = e.getMessage();
			String expectedMessage = "Polymorphic failure encountered - specific issue identified:\n"
					+ "Unable to identify _type field. Raw error message shown below.{\n"
					+ "  \"level\" : \"error\",\n"
					+ "  \"schema\" : {\n"
					+ "    \"loadingURI\" : \"#\",\n"
					+ "    \"pointer\" : \"/definitions/SomethingA/properties/sports/properties/identifier\"\n"
					+ "  },\n"
					+ "  \"instance\" : {\n"
					+ "    \"pointer\" : \"/somethingA/sports/identifier\"\n"
					+ "  },\n"
					+ "  \"domain\" : \"validation\",\n"
					+ "  \"keyword\" : \"anyOf\",\n"
					+ "  \"message\" : \"instance failed to match at least one required schema among 2\",\n"
					+ "  \"nrSchemas\" : 2,\n"
					+ "  \"reports\" : {\n"
					+ "    \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/0\" : [ {\n"
					+ "      \"level\" : \"error\",\n"
					+ "      \"schema\" : {\n"
					+ "        \"loadingURI\" : \"#\",\n"
					+ "        \"pointer\" : \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/0\"\n"
					+ "      },\n"
					+ "      \"instance\" : {\n"
					+ "        \"pointer\" : \"/somethingA/sports/identifier\"\n"
					+ "      },\n"
					+ "      \"domain\" : \"validation\",\n"
					+ "      \"keyword\" : \"type\",\n"
					+ "      \"message\" : \"instance type (null) does not match any allowed primitive type (allowed: [\\\"integer\\\"])\",\n"
					+ "      \"found\" : \"null\",\n"
					+ "      \"expected\" : [ \"integer\" ]\n"
					+ "    } ],\n"
					+ "    \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/1\" : [ {\n"
					+ "      \"level\" : \"error\",\n"
					+ "      \"schema\" : {\n"
					+ "        \"loadingURI\" : \"#\",\n"
					+ "        \"pointer\" : \"/definitions/SomethingA/properties/sports/properties/identifier/anyOf/1\"\n"
					+ "      },\n"
					+ "      \"instance\" : {\n"
					+ "        \"pointer\" : \"/somethingA/sports/identifier\"\n"
					+ "      },\n"
					+ "      \"domain\" : \"validation\",\n"
					+ "      \"keyword\" : \"type\",\n"
					+ "      \"message\" : \"instance type (null) does not match any allowed primitive type (allowed: [\\\"string\\\"])\",\n"
					+ "      \"found\" : \"null\",\n"
					+ "      \"expected\" : [ \"string\" ]\n"
					+ "    } ]\n  }\n}\n\n";
			
			assertThat(actualMessage, is(equalTo(expectedMessage)));
			return;
		}
		
		Assert.fail("Expected exception.");
	}
	
	@Test(groups = "unitTest")
	public void nonPolymorphicAdditionalProperties()
			throws Throwable {
		OpenApiSchemaValidator validator = new OpenApiSchemaValidator.Builder(
				"/com/ebay/schema/validation/schemavalidatorutil/schema/largeTestSchema.yaml", "/dosomething",
				NstRequestType.GET).setStatusCode(StatusCode._200).allowAdditionalProperties(AllowAdditionalProperties.NO).build();
		
		try {
			validator.validate(ResourceParser.readInResourceFile(
				"/com/ebay/schema/validation/schemavalidatorutil/response/nonPolymorphicAdditionalProperties.json"));
		} catch (SchemaValidationException e) {
			String actualMessage = e.getMessage();
			String expectedMessage = "error: object instance has properties which are not allowed by the schema: [\"phoneNumber\"]\n"
					+ "    level: \"error\"\n"
					+ "    schema: {\"loadingURI\":\"#\",\"pointer\":\"/definitions/Person\"}\n"
					+ "    instance: {\"pointer\":\"/somethingC/people/0\"}\n"
					+ "    domain: \"validation\"\n"
					+ "    keyword: \"additionalProperties\"\n"
					+ "    unwanted: [\"phoneNumber\"]\n\n\n";
			
			assertThat(actualMessage, is(equalTo(expectedMessage)));
			return;
		}
		
		Assert.fail("Expected exception.");
	}
}
