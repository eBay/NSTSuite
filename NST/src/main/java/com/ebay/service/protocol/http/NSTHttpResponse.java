package com.ebay.service.protocol.http;

import java.util.Map;

public interface NSTHttpResponse {

	/**
	 * Get the response code.
	 * 
	 * @return Response code.
	 */
	public int getResponseCode();

	/**
	 * Get the response payload.
	 * 
	 * @return Response payload.
	 */
	public String getPayload();

	/**
	 * Get the response headers.
	 * 
	 * @return Response headers.
	 */
	public Map<String, String> getHeaders();
}
