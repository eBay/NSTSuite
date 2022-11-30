package com.ebay.nst.schema.validation;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.nst.schema.validation.support.SchemaValidatorUtil;
import com.ebay.openapi.export.jsonschema.OpenApiToJsonSchema;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;

/**
 * Wrapper for:
 * https://bitbucket.org/atlassian/swagger-request-validator/src/fc8e9c84490c1c25767523fda3fb36b4728cca8f/docs/OPENAPIv3.md
 */
public class OpenApiSchemaValidator implements NSTRestSchemaValidator {
	
	public enum AllowAdditionalProperties {
		YES, NO
	}
	
	public enum StatusCode {
		_100("100"),_101("101"),_200("200"),_201("201"),_202("202"),_203("203"),_204("204"),_205("205"),_206("206"),_300("300"),_301("301"),_302("302"),_303("303"),_304("304"),_305("305"),_307("307"),_400("400"),_401("401"),_402("402"),_403("403"),_404("404"),_405("405"),_406("406"),_407("407"),_408("408"),_409("409"),_410("410"),_411("411"),_412("412"),_413("413"),_414("414"),_415("415"),_416("416"),_417("417"),_426("426"),_500("500"),_501("501"),_502("502"),_503("503"),_504("504"),_505("505");
	
		private final String value;
		
		private StatusCode(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public enum Payload {
		REQUEST(true), RESPONSE(false);
		
		private final boolean value;
		
		private Payload(boolean value) {
			this.value = value;
		}
		
		public boolean getValue() {
			return value;
		}
	}

	private final String schemaResourcePath;
	private final String schemaPath;
	private final NstRequestType requestMethod;
	private final boolean allowAdditionalProperties;
	private final StatusCode statusCode;
	private final Payload payload;

	/**
	 * Initialize the OpenAPI validator with the following values.
	 *
	 * Private constructor. Use @{link {@link Builder}} to instantiate
	 *
	 * @param schemaResourcePath        Path to the OpenAPI schema resource file.
	 * @param schemaPath                HTTP url path to load from the schema definition.
	 * @param requestMethod             Request method to apply from the schema definition.
	 * @param allowAdditionalProperties Allow properties which are not in the schema definition
	 */
	private OpenApiSchemaValidator(String schemaResourcePath, String schemaPath, NstRequestType requestMethod, AllowAdditionalProperties allowAdditionalProperties, Payload payload, StatusCode statusCode) {
		this.schemaResourcePath = Objects.requireNonNull(schemaResourcePath, "Schema resource path cannot be null.");
		this.schemaPath = Objects.requireNonNull(schemaPath, "Schema path cannot be null.");
		this.requestMethod = Objects.requireNonNull(requestMethod, "Request method cannot be null.");
		if (allowAdditionalProperties == AllowAdditionalProperties.YES) {
			this.allowAdditionalProperties = true;
		} else {
			this.allowAdditionalProperties = false;
		}
		this.payload = payload;
		this.statusCode = statusCode;
	}

	@Override
	public void validate(String responseBody) throws SchemaValidationException {

		OpenApiToJsonSchema openApiToJsonSchema = new OpenApiToJsonSchema(schemaResourcePath, schemaPath,
				requestMethod, allowAdditionalProperties, payload.getValue(), statusCode.getValue());
		openApiToJsonSchema.convert();
		JsonNode jsonSchema = openApiToJsonSchema.getJsonSchema();

		SchemaValidatorUtil schemaValidator = new SchemaValidatorUtil();
		schemaValidator.validate(jsonSchema, responseBody);
	}

	@Override
	public String toString() {
		return "OpenApiSchemaValidator [schemaResourcePath=" + schemaResourcePath + ", schemaPath=" + schemaPath
				+ ", requestMethod=" + requestMethod + ", allowAdditionalProperties=" + allowAdditionalProperties
				+ ", statusCode=" + statusCode + ", payload=" + payload + "]";
	}

	public static class Builder {
		private final String schemaResourcePath;
		private final String schemaPath;
		private final NstRequestType requestMethod;
		
		private AllowAdditionalProperties allowAdditionalProperties = AllowAdditionalProperties.YES;
		private Payload payload = Payload.RESPONSE;
		private StatusCode statusCode = StatusCode._200;

		public Builder(String schemaResourcePath, String schemaPath, NstRequestType requestMethod) {
			this.schemaResourcePath = Objects.requireNonNull(schemaResourcePath, "Schema resource path cannot be null.");
			this.schemaPath = Objects.requireNonNull(schemaPath, "Schema path cannot be null.");
			this.requestMethod = Objects.requireNonNull(requestMethod, "Request method cannot be null.");
		}

		/**
		 * Change the application of additional properties to the schema. Default is to
		 * allow additional properties.
		 * 
		 * @param allowAdditionalProperties Allow additional properties. Default is true.
		 * @return Builder instance.
		 */
		public Builder allowAdditionalProperties(AllowAdditionalProperties allowAdditionalProperties) {
			this.allowAdditionalProperties = allowAdditionalProperties;
			return this;
		}

		/**
		 * Set the payload to retrieve schema for. Default is the response payload.
		 * 
		 * @param payload Payload schema to retrieve.
		 * @return Builder instance.
		 */
		public Builder setPayload(Payload payload) {
			this.payload = payload;
			return this;
		}

		/**
		 * Set the status code to retieve schema for. Only necessary if payload is set
		 * to response (default value). Default status code is 200.
		 * 
		 * @param statusCode Status code to retrieve.
		 * @return Builder instance.
		 */
		public Builder setStatusCode(StatusCode statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		/**
		 * Create a new OpenApiSchemaValidator instance based on the builder
		 * configuration.
		 * 
		 * @return OpenApiSchemaValidator.
		 */
		public OpenApiSchemaValidator build() {
			return new OpenApiSchemaValidator(schemaResourcePath, schemaPath, requestMethod, allowAdditionalProperties,
					payload, statusCode);
		}
	}

}
