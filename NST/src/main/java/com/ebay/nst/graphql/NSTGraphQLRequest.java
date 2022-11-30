package com.ebay.nst.graphql;

import java.util.HashMap;
import java.util.Map;

import com.ebay.nst.NSTRequest;

/**
 * GraphQL requests may be sent using the HTTP protocol via POST methods.
 * This class defines the request payload for using HTTP protocol to 
 * interact with GraphQL services. The query field MUST be populated.
 * The other fields are optional.
 */
public class NSTGraphQLRequest implements NSTRequest {

	private String query;
	private String operationName;
	private Map<String, Object> variables;
	private String extensions;

	/**
	 * Example query: https://graphql.github.io/graphql-over-http/draft/#sec-POST.Example
	 * @param query Query to send.
	 */
	public NSTGraphQLRequest(String query) {
		this.query = query;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
	
	public void addVariable(String key, Object value) {
		if (this.variables == null) {
			this.variables = new HashMap<>();
		}
		variables.put(key, value);
	}

	public String getExtensions() {
		return extensions;
	}

	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}

	public String getQuery() {
		return query;
	}
	
	protected void setQuery(String query) {
		this.query = query;
	}
}
