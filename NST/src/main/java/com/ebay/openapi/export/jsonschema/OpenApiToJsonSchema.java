package com.ebay.openapi.export.jsonschema;

import java.util.*;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.ebay.nst.NstRequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.*;

/**
 * This class leverages the Atlassian swagger-request-validator project to
 * complete the conversion to JSON schema. Much of this code comes from the
 * Atlassian project but is not accessible via public facing methods.
 * Selectively extracting the functionality needed affords us the ability to
 * execute the conversion from OpenAPI to JSON schema with a Java based
 * solution. Credit is given to the authors/maintainers of the Atlassian
 * swagger-request-validator for this solution.
 *
 * https://bitbucket.org/atlassian/swagger-request-validator/src/master/swagger-request-validator-core/src/main/java/com/atlassian/oai/validator/schema/SchemaValidator.java
 */
public class OpenApiToJsonSchema {
	
	private static final String MEDIA_TYPE = "application/json";

	private final String openApiSpecFilePath;
	private final String requestPath;
	private final NstRequestType requestMethod;
	private volatile ObjectNode schemaObject;
	private final boolean allowAdditionalProperties;
	private final List<EbaySchemaTransformer> transformers;
	private final boolean useRequestModel;
	private final String statusCode;
	private final ParseOptions parseOptions;

	/**
	 * Convert an OpenAPI yaml spec to JSON schema. Call 'convert()' to begin the
	 * operation.
	 *
	 * @param openApiSpecFilePath       Path to yaml spec to convert.
	 * @param requestPath               The API request path to evaluate in the schema.
	 * @param requestMethod             Request method corresponding to the requestPath to
	 *                                  evaluate.
	 * @param allowAdditionalProperties when false, validation will fail if properties are
	 *                                  present which are not defined in the schema
	 * @param useRequestModel 			True to use the request model path, false to use the
	 * 									response model path.
	 * @param statusCode				Response code to use.
	 */
	public OpenApiToJsonSchema(String openApiSpecFilePath, String requestPath, NstRequestType requestMethod, boolean allowAdditionalProperties, boolean useRequestModel, String statusCode) {
		this(openApiSpecFilePath, requestPath, requestMethod, allowAdditionalProperties, useRequestModel, statusCode, new ParseOptions());
	}

	/**
	 * Convert an OpenAPI yaml spec to JSON schema. Call 'convert()' to begin the
	 * operation.
	 *
	 * @param openApiSpecFilePath       Path to yaml spec to convert.
	 * @param requestPath               The API request path to evaluate in the schema.
	 * @param requestMethod             Request method corresponding to the requestPath to
	 *                                  evaluate.
	 * @param allowAdditionalProperties when false, validation will fail if properties are
	 *                                  present which are not defined in the schema
	 * @param useRequestModel 			True to use the request model path, false to use the
	 * 									response model path.
	 * @param statusCode				Response code to use.
	 * @param parseOptions				Swagger parsing options to use.
	 */
	public OpenApiToJsonSchema(String openApiSpecFilePath, String requestPath, NstRequestType requestMethod, boolean allowAdditionalProperties, boolean useRequestModel, String statusCode, ParseOptions parseOptions) {
		this.openApiSpecFilePath = openApiSpecFilePath;
		this.requestPath = requestPath;
		this.requestMethod = requestMethod;
		this.allowAdditionalProperties = allowAdditionalProperties;
		this.useRequestModel = useRequestModel;
		this.statusCode = statusCode;
		this.transformers = createTransformers();
		if (parseOptions != null) {
			this.parseOptions = parseOptions;
		} else {
			this.parseOptions = new ParseOptions();
		}
	}

	/**
	 * Get the serialized JSON schema. Call this after the 'convert()' operation
	 * completes.
	 *
	 * @return Serialized JSON schema.
	 */
	public synchronized String getSerializedJsonSchema() {
		if (schemaObject != null) {
			// Serialize the JSON schema
			return schemaObject.toPrettyString();
		}

		return null;
	}

	/**
	 * Get the JSON schema node.
	 *
	 * @return JSON schema node.
	 */
	public synchronized JsonNode getJsonSchema() {
		return schemaObject;
	}

	/**
	 * Perform the conversion.
	 */
	public void convert() {

		SwaggerParseResult parseResult = new OpenAPIV3Parser().readLocation(openApiSpecFilePath, null, parseOptions);
		if (parseResult == null || parseResult.getOpenAPI() == null) {
			throw new IllegalStateException("Unable to read yaml file from path: " + openApiSpecFilePath);
		}

		OpenAPI openApi = parseResult.getOpenAPI();
		if (openApi == null) {
			throw new IllegalStateException("OpenAPI model not present in the file: " + openApiSpecFilePath);
		}

		PathItem pathItem = openApi.getPaths().get(requestPath);
		if (pathItem == null) {
			throw new IllegalStateException(
					"Request path [" + requestPath + "] is not present in file: " + openApiSpecFilePath);
		}

		Operation operation = null;
		switch (requestMethod) {
		case GET:
			operation = pathItem.getGet();
			break;
		case POST:
			operation = pathItem.getPost();
			break;
		case PUT:
			operation = pathItem.getPut();
			break;
		case DELETE:
			operation = pathItem.getDelete();
			break;
		default:
			throw new IllegalStateException("Request method [" + requestMethod + "] is not supported.");
		}

		if (operation == null) {
			throw new IllegalStateException("Request method [" + requestMethod + "] is not present for path ["
					+ requestPath + "] in file: " + openApiSpecFilePath);
		}
		
		// Get either the request or response as request. If getting the response, get the response for the specified status code.
		Schema<?> schema = null;
		
		if (useRequestModel) {
			
			RequestBody requestBody = operation.getRequestBody();
			if (requestBody == null) {
				throw new IllegalStateException("Request body is not defined for request method [" + requestMethod + "] and path [" + requestPath + "] in file: " + openApiSpecFilePath);
			}

			MediaType mediaType = requestBody.getContent().get(MEDIA_TYPE);
			if (mediaType == null) {
				throw new IllegalStateException(
						"Media type [application/json] is not present for request body of request method ["
								+ requestMethod + "] and path [" + requestPath + "] in file: " + openApiSpecFilePath);
			}
	
			schema = mediaType.getSchema();
			if (schema == null) {
				throw new IllegalStateException(
						"Schema not found for media type [application/json] for request body of request method ["
								+ requestMethod + "] and path [" + requestPath + "] in file: " + openApiSpecFilePath);
			}
			
		} else {
			
			ApiResponses responses = operation.getResponses();
			ApiResponse response = responses.get(statusCode);
			if (response == null) {
				throw new IllegalStateException(String.format("Status code [%s] is not present for request method [%s] and path [%s] in file: %s", statusCode, requestMethod, requestPath, openApiSpecFilePath));
			}
			
		//added below check to fix NPE
		       if(response.getContent() != null) {
				MediaType mediaType = response.getContent().get(MEDIA_TYPE);
				if (mediaType == null) {
					throw new IllegalStateException(
							"Media type [application/json] is not present for status code [200] and request method ["
									+ requestMethod + "] and path [" + requestPath + "] in file: " + openApiSpecFilePath);
				}else{
					schema = mediaType.getSchema();
				}
			}
			
			if (schema == null) {
				throw new IllegalStateException(
						"Schema not found for media type [application/json] and status code [200] and request method ["
								+ requestMethod + "] and path [" + requestPath + "] in file: " + openApiSpecFilePath);
			}
		}

		@SuppressWarnings("rawtypes")
		Map<String, Schema> typeDefinitions = Optional.ofNullable(openApi.getComponents())
				.map(Components::getSchemas)
				.orElseGet(HashMap::new);


		if (!allowAdditionalProperties) {
			// Collapse allOf schemas into inline definitions
			typeDefinitions = new EbayAllOfInliner(typeDefinitions).inlineSchemas();
		}

		// Rewrite all references to the JSON schema specific `definition`
		// instead of `components/schemas` path. Do this for definitions
		// and for the response schemas.
		for (String key : typeDefinitions.keySet()) {
			prepareSchemaModels(typeDefinitions.get(key));
		}

		prepareSchemaModels(schema);

		// Using Atlassian code here - extract the ref definitions
		// These will be used to provide ref definitions for the JSON schema
		final JsonNode definitions = Json.mapper().convertValue(typeDefinitions, JsonNode.class);

		// Convert to an ObjectNode.
		// If the schema is inlined in the reponse object (responseRef == null)
		// then process the response schema directly. Otherwise, get the schema
		// from the root ref.
		String responseRef = schema.get$ref();
		if (responseRef == null) {
			schemaObject = Json.mapper().convertValue(schema, ObjectNode.class);
		} else {
			String rootType = responseRef.substring(responseRef.lastIndexOf("/") + 1);
			schemaObject = Json.mapper().convertValue(definitions.get(rootType), ObjectNode.class);
		}

		// Using Atlassian code here (with modifitications) - convert to
		// JSON schema. Use the transformers defined at the root of this
		// class to complete the modifications.
		final SchemaTransformationContext transformationContext = SchemaTransformationContext.create().forRequest(false)
				.forResponse(true).withAdditionalPropertiesValidation(true).withDefinitions(definitions).build();

		transformers.forEach(t -> t.apply(schemaObject, transformationContext));
	}

	/**
	 * Prepare the schema model during conversion to JSON schema. This involves
	 * removing unsupported notations and making necessary changes per the JSON
	 * schema standard. This is a recursive operation that evaluates the entire
	 * schema tree descending from the root schema passed in from the external
	 * caller.
	 *
	 * @param schema Schema to update.
	 */
	@SuppressWarnings("rawtypes")
	private void prepareSchemaModels(Schema<?> schema) {

		// Remove unsupported notations
		schema.setDeprecated(null);
		schema.setExample(null);
		schema.setFormat(null);

		// Process: properties, additionalProperties, array items, allOf, oneOf, anyOf
		Map<String, Schema> properties = schema.getProperties();
		if (properties != null) {
			for (String key : properties.keySet()) {
				prepareSchemaModels(properties.get(key));
			}
		}

		Object additionalProperties = schema.getAdditionalProperties();
		if (additionalProperties instanceof Schema<?>) {
			Schema<?> additionalProps = (Schema<?>) additionalProperties;
			if (additionalProps != null) {
				prepareSchemaModels(additionalProps);
			}
		}

		if (schema instanceof ArraySchema) {
			ArraySchema arraySchema = (ArraySchema) schema;
			prepareSchemaModels(arraySchema.getItems());
		}

		if (schema instanceof ComposedSchema) {
			ComposedSchema composedSchema = (ComposedSchema) schema;
			List<Schema> allOf = composedSchema.getAllOf();
			if (allOf != null) {
				for (Schema a : allOf) {
					prepareSchemaModels(a);
				}
			}

			List<Schema> anyOf = composedSchema.getAnyOf();
			if (anyOf != null) {
				for (Schema a : anyOf) {
					prepareSchemaModels(a);
				}
			}

			List<Schema> oneOf = composedSchema.getOneOf();
			if (oneOf != null) {
				for (Schema a : oneOf) {
					prepareSchemaModels(a);
				}
			}
		}

		// JSON schema puts reference models under 'definitions'.
		// Replace OpenAPI 'components/schemas' with 'definitions' as
		// part of the conversion to JSON schema.
		String ref = schema.get$ref();
		if (ref != null) {
			ref = ref.replace("components/schemas", "definitions");
			schema.set$ref(ref);
		}
	}

	/**
	 * Transformations applied to the schema before validation.
	 *
	 * Order is important here - the mutations from one transformation are passed
	 * through to the subsequent transformers.
	 */
	private List<EbaySchemaTransformer> createTransformers() {
		List<EbaySchemaTransformer> transformers = new ArrayList<>(Arrays.asList(
				EbaySchemaDefinitionsInjectionTransformer.getInstance(),
				EbaySchemaRefInjectionTransformer.getInstance(),
				EbayRemoveExampleSetFlagTransformer.getInstance(),
				EbayNullableTransformer.getInstance(),
				EbayRemoveTypesTransformer.getInstance()));
		if (!allowAdditionalProperties) {
			transformers.add(EbayAdditionalPropertiesInjectionTransformer.getInstance());
		}
		transformers.add(EbayRequiredFieldTransformer.getInstance());
		return transformers;
	}
}
