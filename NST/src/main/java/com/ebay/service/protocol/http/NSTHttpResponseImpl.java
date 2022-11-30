package com.ebay.service.protocol.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NSTHttpResponseImpl implements NSTHttpResponse {

	private int responseCode;
	private String payload;
	private Map<String, String> headers = new HashMap<>();

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String getPayload() {
		return payload;
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	public void setHeaders(Map<String, String> headers) {
		if (headers == null) {
			this.headers.clear();
			return;
		}
		
		this.headers = new HashMap<>(headers);
	}

	@Override
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

}
