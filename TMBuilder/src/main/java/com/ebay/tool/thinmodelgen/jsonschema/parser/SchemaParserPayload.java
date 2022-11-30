package com.ebay.tool.thinmodelgen.jsonschema.parser;

import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.GraphQLOperation;

public class SchemaParserPayload {

	// Related to OpenAPI schema
	private String path;
	private SchemaParserRequestMethod method;
	private String responseCode;
	private String contentType;

	// Related to GraphQL schema
	private GraphQLOperation graphQLOperation;
	private String graphQLOperationName;

	public String getPath() {
		return path;
	}

	public SchemaParserPayload setPath(String path) {
		this.path = path;
		return this;
	}

	public SchemaParserRequestMethod getMethod() {
		return method;
	}

	public SchemaParserPayload setMethod(SchemaParserRequestMethod method) {
		this.method = method;
		return this;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public SchemaParserPayload setResponseCode(String responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public SchemaParserPayload setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public GraphQLOperation getGraphQLOperation() {
		return graphQLOperation;
	}

	public SchemaParserPayload setGraphQLOperation(GraphQLOperation graphQLOperation) {
		this.graphQLOperation = graphQLOperation;
		return this;
	}

	public String getGraphQLOperationName() {
		return graphQLOperationName;
	}

	public SchemaParserPayload setGraphQLOperationName(String graphQLOperationName) {
		this.graphQLOperationName = graphQLOperationName;
		return this;
	}

}
