package com.ebay.nst.graphql;

import java.util.HashMap;
import java.util.Map;

import com.ebay.nst.NSTServiceWrapper;
import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.GraphQLSchemaValidator;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.ebay.utility.service.ServiceUtil;
import com.google.gson.Gson;

public interface NSTGraphQLServiceWrapper extends NSTServiceWrapper<GraphQLSchemaValidator> {
	
	/*
	 * This MUST always return POST for GraphQL requests.
	 */
	@Override
	default NstRequestType getRequestType() {
		return NstRequestType.POST;
	}

	/*
	 * Handles the preparation of a GraphQL request. Please use the default
	 * implementation instead of implementing your own method as this implementation
	 * follows the GraphQL guidance for transmitting requests over HTTP.
	 */
	@Override
	default NSTHttpRequest prepareRequest() {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> additionalHeaders = getAdditionalHeaders();
		if (additionalHeaders != null) {
			headers.putAll(additionalHeaders);
		}
		headers.put("Accept", "application/graphql-response+json");
		headers.put("Content-Type", "application/json");
		Gson gson = new Gson();
		String payload = gson.toJson(getRequest());
		return new NSTHttpRequestImpl.Builder(ServiceUtil.getUrl(this), NstRequestType.POST).setHeaders(headers).setPayload(payload).build();
	}

	/**
	 * Because prepareRequest provides a default implementation that we MUST use to
	 * send GrpahQL requests we need a way to add additional headers to the request
	 * for Authentication, etc. Implement this method and return the Map of headers
	 * needed.
	 * 
	 * @return Additional headers to apply when prepareRequest() is called.cdiugfjdbenl
	 */
	Map<String, String> getAdditionalHeaders();
	
	/**
	 * Get the request to be sent.
	 * 
	 * @return NSTGraphQLRequest instance to send in the request.
	 */
	NSTGraphQLRequest getRequest();

}
