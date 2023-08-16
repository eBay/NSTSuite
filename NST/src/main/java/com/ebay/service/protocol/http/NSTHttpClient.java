package com.ebay.service.protocol.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface NSTHttpClient<Request extends NSTHttpRequest, Response extends NSTHttpResponse> {

	/**
	 * Send the request and return the response. Uses the default Charset specified by the implementation.
	 * @param request Request to send.
	 * @return Response model.
	 */
	public Response sendRequest(Request request);

	/**
	 * Send the request and return the response.
	 * @param request Request to send.
	 * @param readResponseCharSet Character set to use when parsing the response payload. Recommend sourcing from java.nio.charset.StandardCharsets.
	 * @return Response model.
	 */
	public Response sendRequest(Request request, Charset readResponseCharSet);
	
}
