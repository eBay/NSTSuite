package com.ebay.nst.schema.validation;

import java.io.File;
import java.util.Objects;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.parser.GraphQLParser;
import com.ebay.graphql.transformer.GraphQLToJsonSchema;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.nst.schema.validation.support.SchemaValidatorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GraphQLSchemaValidator implements NSTSchemaValidator {
	
	private final File schemaFile;
	private final OperationType operationType;
	private final String operationName;
	
	public enum OperationType {
		QUERY,
		MUTATION,
		SUBSCRIPTION
	}
	
	public GraphQLSchemaValidator(File schemaFile, OperationType operationType, String operationName) {
		
		Objects.requireNonNull(schemaFile, "Schema file MUST NOT be null.");
		Objects.requireNonNull(operationType, "Operation type MUST NOT be null.");
		Objects.requireNonNull(operationName, "Operation name MUST NOT be null.");
		
		this.schemaFile = schemaFile;
		this.operationType = operationType;
		this.operationName = operationName;
	}

	@Override
	public void validate(String responseBody) throws SchemaValidationException {

		GraphQLParser graphQLParser = new GraphQLParser();
		GraphQLSchema graphQLSchema = graphQLParser.parseGraphQL(schemaFile);
		GraphQLToJsonSchema converter = new GraphQLToJsonSchema(graphQLSchema);
		JsonNode rootNode = null;
		
		switch(operationType) {
		
		case QUERY:
			rootNode = converter.convertQuery(operationName);
			break;
		case MUTATION:
			rootNode = converter.convertMutation(operationName);
			break;
		case SUBSCRIPTION:
			rootNode = converter.convertSubscription(operationName);
			break;
		}

		if (rootNode == null) {
			throw new IllegalStateException(String.format("Unable to find operation type [%s] and name [%s] in the schema file [%s].", operationType, operationName, schemaFile));
		}
		
		// The raw response from the serivce will contain the response payload in the 'data' field.
		// https://spec.graphql.org/October2021/#sec-Errors.Error-result-format:~:text=raised%20field%20errors.-,Error%20result%20format,-Every%20error%20must
		// We need to extract the data payload, if it exists, for processing. Otherwise, process the entire response.
		ObjectMapper map = new ObjectMapper();
		JsonNode responseNode;
		try {
			responseNode = map.readTree(responseBody);
		} catch (JsonProcessingException e) {
			// Fold this into a schema validation exception.
			throw new SchemaValidationException(e.getMessage());
		}
		
		if (responseNode.get("data") != null) {
			responseNode = responseNode.get("data");
		}
		
		responseBody = responseNode.toString();

		SchemaValidatorUtil schemaValidator = new SchemaValidatorUtil();
		schemaValidator.validate(rootNode, responseBody);
	}

	@Override
	public String toString() {
		return "GraphQLSchemaValidator [schemaFile=" + schemaFile + ", operationType=" + operationType
				+ ", operationName=" + operationName + "]";
	}

}
