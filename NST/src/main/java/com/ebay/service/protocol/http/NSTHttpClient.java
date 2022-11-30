package com.ebay.service.protocol.http;

public interface NSTHttpClient<Request extends NSTHttpRequest, Response extends NSTHttpResponse> {

	public Response sendRequest(Request request);
	
}
