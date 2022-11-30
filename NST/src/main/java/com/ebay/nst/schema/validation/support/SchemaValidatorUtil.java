package com.ebay.nst.schema.validation.support;

import java.io.IOException;
import java.util.Objects;

import org.testng.Reporter;

import com.ebay.utility.timestamp.TimeStamp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class SchemaValidatorUtil {

	public void validate(JsonNode schemaNode, String responseBody) {

		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

		try {
			final JsonSchema jsonSchema = factory.getJsonSchema(schemaNode);
			final JsonNode responseNode = JsonLoader.fromString(responseBody);
			Reporter.log(String.format("Start json validation at %s", TimeStamp.getCurrentTime()), true);
			ProcessingReport processingReport = jsonSchema.validateUnchecked(responseNode, true);
			Reporter.log(String.format("Finish json validation at %s", TimeStamp.getCurrentTime()), true);

			if (processingReport.isSuccess()) {
				Reporter.log("Validate response against schema: Success", true);
			} else {
				Reporter.log("Validate response against schema: Failed", true);

				StringBuilder sb = new StringBuilder();
				for (ProcessingMessage processingMessage : processingReport) {

					JsonNode processingMessageJson = processingMessage.asJson();

					String logMessage = processingMessage.toString();

					// Check for polymorphic error reports and process those to extract the specific
					// issue.
					JsonNode keywordJsonNode = processingMessageJson.get("keyword");
					if (keywordJsonNode != null && (keywordJsonNode.asText().equals("anyOf") || keywordJsonNode.asText().equals("oneOf"))) {

						String polymorphicMessage = null;

						if (processingMessageJson instanceof ObjectNode) {
							polymorphicMessage = processPolymorphicObjectErrors((ObjectNode) processingMessageJson,
									schemaNode, responseBody);
						} else if (processingMessageJson instanceof ArrayNode) {
							polymorphicMessage = processPolymorphicArrayErrors((ArrayNode) processingMessageJson,
									schemaNode, responseBody);
						}

						// Doing this as a defensive mechanism. If the evaluated json is not an
						// ObjectNode or
						// an ArrayNode we want to fall through and log the message in it's current
						// state
						// so we don't lose it. Change the header accordingly for this case.
						if (polymorphicMessage != null) {
							logMessage = polymorphicMessage;
							sb.append("Polymorphic failure encountered - specific issue identified:\n");
						} else {
							sb.append(String.format("Polymorphic investigation failed for unknown type [%s].",
									processingMessageJson.getClass().getName()));
						}
					}

					// Log whatever message we end up with.
					sb.append(logMessage);
					sb.append("\n\n");
				}
				throw new SchemaValidationException(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			Reporter.log(String.format("IOException from schemaResourcePath. %s", e.getMessage()), true);
			throw new SchemaValidationException(e.getMessage());
		} catch (ProcessingException e) {
			e.printStackTrace();
			Reporter.log(String.format("ProcessingException from getJsonSchema. %s", e.getMessage()), true);
			throw new SchemaValidationException(e.getMessage());
		}
	}

	/**
	 * Process polymorphic errors of ArrayNode type.
	 * 
	 * @param processingMessageJson ArrayNode instance to process.
	 * @param schemaNode            Schema tree used for validation.
	 * @param responseBody          Response body compared against the schema.
	 * @return Error message to show to user.
	 * @throws JsonMappingException    Pass through.
	 * @throws JsonProcessingException Pass through.
	 */
	protected String processPolymorphicArrayErrors(ArrayNode processingMessageJson, JsonNode schemaNode,
			String responseBody) throws JsonMappingException, JsonProcessingException {

		Objects.requireNonNull(processingMessageJson, "ProcessingMessageJson MUST NOT be null.");

		StringBuilder errorMessage = new StringBuilder();

		String message = null;
		JsonNode indexedNode;

		for (int i = 0; i < processingMessageJson.size(); i++) {

			indexedNode = processingMessageJson.get(i);

			if (indexedNode instanceof ArrayNode) {
				message = processPolymorphicArrayErrors((ArrayNode) indexedNode, schemaNode, responseBody);
			} else if (indexedNode instanceof ObjectNode) {
				message = processPolymorphicObjectErrors((ObjectNode) indexedNode, schemaNode, responseBody);
			} else {
				// Log whatever the message is, because, we don't know how to process it.
				// This case should NEVER occur.
				message = processingMessageJson.toPrettyString();
			}

			if (message != null) {
				errorMessage.append(message);
			}
		}

		return errorMessage.toString();
	}

	/**
	 * Process polymorphic erros of ObjectNode type.
	 * 
	 * @param processingMessageJson ObjectNode instance to process.
	 * @param schemaNode            Schema tree used for validation.
	 * @param responseBody          Response body compared against the schema.
	 * @return Error message to show to user.
	 * @throws JsonMappingException    Pass through.
	 * @throws JsonProcessingException Pass through.
	 */
	protected String processPolymorphicObjectErrors(ObjectNode processingMessageJson, JsonNode schemaNode,
			String responseBody) throws JsonMappingException, JsonProcessingException {
		
		Objects.requireNonNull(processingMessageJson, "ProcessingMessageJson MUST NOT be null.");

		StringBuilder errorMessage = new StringBuilder();

		TextNode keywordNode = (TextNode) processingMessageJson.get("keyword");
		String keyword = keywordNode.asText();

		// If we are accidently processing a type that is not polymorphic bail 
		// and return the error message. We are done!
		if (!keyword.equals("anyOf") && !keyword.equals("oneOf")) {
			errorMessage.append(processingMessageJson.toPrettyString());
			return errorMessage.toString();
		}

		// Take the response and walk to the point of the fork of the anyOf/oneOf.
		// This is done by following the instance path.
		ObjectNode instanceNode = (ObjectNode) processingMessageJson.get("instance");
		String instancePath = ((TextNode) instanceNode.get("pointer")).asText();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootResponse = mapper.readTree(responseBody);

		String[] pathSteps = instancePath.split("/");
		JsonNode pathNode = rootResponse;
		String pathStep;
		
		// The first index will always be an empty string. Skip it.
		for (int i = 1; i < pathSteps.length; i++) {
			pathStep = pathSteps[i];

			if (pathNode instanceof ObjectNode) {
				pathNode = pathNode.get(pathStep);
			} else if (pathNode instanceof ArrayNode) {
				pathNode = pathNode.get(Integer.parseInt(pathStep));
			}
		}
		
		// AnyOf/OneOf types will have an _type field that identifies the serialized type.
		// Look for that _type field on the current node of the response.
		// Use that to find the schema definition matching that type to continue.
		// If we don't find a value for _type then return the current error message.
		JsonNode _typeNode = pathNode.get("_type");

		if (_typeNode == null) {
			errorMessage.append("Unable to identify _type field. Raw error message shown below.");
			errorMessage.append(processingMessageJson.toPrettyString());
			return errorMessage.toString();
		}
		
		String _type = ((TextNode) _typeNode).asText();

		// Using the schema, find the index matching the _type value in the anyOf/oneOf definition.
		ObjectNode errorSchemaNode = (ObjectNode) processingMessageJson.get("schema");
		String schemaPath = ((TextNode) errorSchemaNode.get("pointer")).asText();
		
		String[] schemaPathSteps = schemaPath.split("/");
		JsonNode schemaPathStepNode = schemaNode;
		String schemaPathStep;
		
		// The first index will always be an empty string. Skip it.
		for (int i = 1; i < schemaPathSteps.length; i++) {
			schemaPathStep = schemaPathSteps[i];
			schemaPathStepNode = schemaPathStepNode.get(schemaPathStep);
		}

		// Assemble the report node key to follow based on the response type and index of the schema
		// that applies to the response.
		String reportNodeKey = schemaPath;

		// Pluck off the anyOf or oneOf
		if (schemaPathStepNode.get("anyOf") != null) {
			schemaPathStepNode = schemaPathStepNode.get("anyOf");
			reportNodeKey += "/anyOf/";
		} else if (schemaPathStepNode.get("oneOf") != null) {
			schemaPathStepNode = schemaPathStepNode.get("oneOf");
			reportNodeKey += "/oneOf/";
		} else {
			errorMessage.append("Unable to isolate anyOf/oneOf. Raw error message shown below.");
			errorMessage.append(processingMessageJson.toPrettyString());
			return errorMessage.toString();
		}

		// Match _type to the reference (case insensitive)
		ObjectNode reportRoot = (ObjectNode) processingMessageJson.get("reports");
		JsonNode refNode;
		boolean typeMatched = false;
		
		for (int i = 0; i < schemaPathStepNode.size(); i++) {
			refNode = schemaPathStepNode.get(i).get("$ref");

			if (refNode == null) {
				errorMessage.append(String.format("AnyOf/OneOf reference type [index: %d] was null. Raw error message shown below.", i));
				errorMessage.append(processingMessageJson.toPrettyString());
				return errorMessage.toString();
			}

			String refPath = refNode.asText();
			refPath = refPath.substring(refPath.lastIndexOf("/") + 1);

			if (refPath.equalsIgnoreCase(_type)) {

				JsonNode newReportNode = reportRoot.get(reportNodeKey + i);
				String message = "";
				if (newReportNode instanceof ArrayNode) {
					message = processPolymorphicArrayErrors((ArrayNode) newReportNode, schemaNode, responseBody);
				} else if (newReportNode instanceof ObjectNode) {
					message = processPolymorphicObjectErrors((ObjectNode) newReportNode, schemaNode, responseBody);
				} else {
					errorMessage.append(String.format("_type match is not not an Array or Object type [%s]. Raw error message shown below.", newReportNode.getClass().getName()));
					errorMessage.append(processingMessageJson.toPrettyString());
					return errorMessage.toString();
				}
				
				errorMessage.append(message);
				typeMatched = true;
				break;
			}
		}

		// Prevent error from falling through the crack if there are no _type matches.
		if (!typeMatched) {
			errorMessage.append(String.format("Unable to match _type [%s] to a polymorphic reference. Raw error message shown below.", _type));
			errorMessage.append(processingMessageJson.toPrettyString());
			return errorMessage.toString();
		}

		return errorMessage.toString();
	}
}
