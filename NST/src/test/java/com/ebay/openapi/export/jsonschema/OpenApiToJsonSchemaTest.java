package com.ebay.openapi.export.jsonschema;

import static com.ebay.utility.ResourceParser.readInResourceJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.JsonNode;

import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.utility.ResourceParser;

public class OpenApiToJsonSchemaTest {

	@Test(groups = "unitTest")
	public void yamlWithNoRefsInPath() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithNoRefsInPath.yaml", "/WithNoRefsInPath");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withNoRefsInPathGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithSchemaPlusRefInPath() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithSchemaPlusRefInPath.yaml", "/WithSchemaPlusRefInPath");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withSchemaPlusRefInPathGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithRefs() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/SimpleRef.yaml", "/SimpleRef");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/simpleRefGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithAllOf() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/AllOf.yaml", "/AllOf");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/allOfGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithAllOfDisallowAdditionalProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/AllOfNoAdditional.yaml", "/AllOfNoAdditional", false);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/allOfNoAddtPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithOneOf() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithOneOf.yaml", "/WithOneOf");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withOneOfGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void secondaryYamlWithOneOf() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/schema/validation/schemas/oneOfValidation.yaml", "/test");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/schema/validation/schemas/oneOfValidation.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithOneOfOnProperty() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithOneOfOnProperty.yaml", "/WithOneOfOnProperty");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withOneOfOnPropertyGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithOneOfComposites() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithOneOfComposites.yaml", "/WithOneOfComposites", false);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withOneOfCompositesNoAdditionalPropsGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithAnyOf() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithAnyOf.yaml", "/WithAnyOf");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withAnyOfGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithAnyOfComposites() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithAnyOfComposites.yaml", "/WithAnyOfComposites", false);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withAnyOfCompositesNoAdditionalPropsGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithAnyOfOnProperty() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithAnyOfOnProperty.yaml", "/WithAnyOfOnProperty");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withAnyOfOnPropertyGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithArray() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithArray.yaml", "/WithArray");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withArrayGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	/**
	 * Evaluate integer types with the following properties:
	 *
	 * multipleOf
	 * maximum
	 * exclusiveMaximum
	 * minimum
	 * exclusiveMinimum
	 *
	 * https://json-schema.org/understanding-json-schema/reference/numeric.html
	 * @throws Exception
	 */
	@Test(groups = "unitTest")
	public void yamlWithIntegerProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithIntegerProperties.yaml", "/WithIntegerProperties");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withIntegerPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema.toPrettyString(), is(equalTo(expectedJsonSchema.toPrettyString())));
	}

	/**
	 * Evaluate String types with the following properties:
	 *
	 * maxLength
	 * minLength
	 * pattern (This string SHOULD be a valid regular expression, according to the Ecma-262 Edition 5.1 regular expression dialect)
	 * enum
	 *
	 * https://json-schema.org/understanding-json-schema/reference/string.html
	 * @throws Exception
	 */
	@Test(groups = "unitTest")
	public void yamlWithStringsAndProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithStringsAndProperties.yaml", "/WithStringsAndProperties");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withStringsAndPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	/**
	 * Evaluate objects with required properties.
	 * @throws Exception
	 */
	@Test(groups = "unitTest")
	public void yamlWithRequiredProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/ObjectWithRequiredProperties.yaml", "/ObjectWithRequiredProperties");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/objectWithRequiredPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	/**
	 * Evaluate objects with the following properties:
	 *
	 * maxProperties
	 * minProperties
	 *
	 * https://json-schema.org/understanding-json-schema/reference/object.html
	 * @throws Exception
	 */
	@Test(groups = "unitTest")
	public void yamlWithObjectProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/ObjectWithProperties.yaml", "/ObjectWithProperties");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/objectWithPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	/**
	 * Evaluate arrays with the following properties:
	 *
	 * uniqueItems
	 * minItems
	 * maxItems
	 *
	 * https://json-schema.org/understanding-json-schema/reference/array.html
	 * @throws Exception
	 */
	@Test(groups = "unitTest")
	public void yamlWithArrayProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithArrayProperties.yaml", "/WithArrayProperties");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withArrayPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	@Test(groups = "unitTest")
	public void yamlWithNullableAdditionalProperties() throws Exception {

		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/NullableAdditionalProperties.yaml", "/NullableAdditionalProperties");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/nullableAdditionalPropertiesGetResponseSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest")
	public void testYamlWith400ResponseSchema() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/With400ResponseStatus.yaml", "/test", NstRequestType.GET, true, false, "400");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/with400ResponseStatus.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest")
	public void testYamlWith400ResposneSchemaAndAdditionalPropertiesFalse() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/With400ResponseStatus.yaml", "/test", NstRequestType.GET, false, false, "400");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/with400ResponseStatusAndAdditionalPropertiesFalse.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest", expectedExceptions = IllegalStateException.class)
	public void testYamlWithUnmatchedStatusCode() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/With400ResponseStatus.yaml", "/test", NstRequestType.GET, true, false, "200");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/with400ResponseStatus.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest", expectedExceptions = IllegalStateException.class)
	public void testYamlWithNullStatusCode() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/With400ResponseStatus.yaml", "/test", NstRequestType.GET, true, false, null);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/with400ResponseStatus.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest")
	public void testRequestSchema() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithRequestSchema.yaml", "/test", NstRequestType.POST, true, true, null);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withRequestSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest")
	public void testRequestSchemaIgnoresStatusCode() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithRequestSchema.yaml", "/test", NstRequestType.POST, true, true, "200");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withRequestSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest")
	public void testRequestInlineSchema() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithRequestInlineSchema.yaml", "/test", NstRequestType.POST, true, true, null);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withRequestInlineSchema.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest")
	public void testRequestSchemaWithoutAdditionalProperties() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/WithRequestSchema.yaml", "/test", NstRequestType.POST, false, true, null);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/withRequestSchemaWithoutAdditionalProperties.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test(groups = "unitTest", expectedExceptions = IllegalStateException.class)
	public void testRequestSchemaForGetMethodWithoutRequestSchema() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/With400ResponseStatus.yaml", "/test", NstRequestType.GET, true, true, null);
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/with400ResponseStatus.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}
	
	@Test
	public void testNullableAllOfReference() throws Exception {
		JsonNode actualJsonSchema = doConversion("/com/ebay/openapi/export/jsonschema/sourceyaml/NullableAllOfReference.yaml", "/test", NstRequestType.GET, false, false, "200");
		JsonNode expectedJsonSchema = readInResourceJson("/com/ebay/openapi/export/jsonschema/expectedjson/nullableAllOfReference.json");
		assertThat("JSON schemas MUST match.", actualJsonSchema, is(equalTo(expectedJsonSchema)));
	}

	private JsonNode doConversion(String resourceFilePath, String requestPath) {
		return doConversion(resourceFilePath, requestPath, true);
	}

	private JsonNode doConversion(String resourceFilePath, String requestPath, boolean allowAdditionalProperties) {
		return doConversion(resourceFilePath, requestPath, NstRequestType.GET, allowAdditionalProperties, false, "200");
	}
	
	private JsonNode doConversion(String resourceFilePath, String requestPath, NstRequestType requestType, boolean allowAdditionalProperties, boolean useRequest, String statusCode) {
		String yamlFilePath = ResourceParser.getResourceFilePath(resourceFilePath);
	    OpenApiToJsonSchema openapi2schema = new OpenApiToJsonSchema(yamlFilePath, requestPath, requestType, allowAdditionalProperties, useRequest, statusCode);
	    openapi2schema.convert();
	    return openapi2schema.getJsonSchema();
	}
}
