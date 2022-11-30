package com.ebay.service.protocol.http;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ebay.nst.NstRequestType;

public class NSTHttpRequestImpl implements NSTHttpRequest {

	private NstRequestType requestType;
	private URL url;
	private Map<String, String> headers;
	private String payload;
	private int timeout;
	private boolean trustAllCerts;

	private NSTHttpRequestImpl(Builder builder) {
		this.requestType = builder.requestType;
		this.url = builder.url;
		this.headers = new HashMap<>(builder.headers);
		this.payload = builder.payload;
		this.timeout = builder.timeout;
		this.trustAllCerts = builder.trustAllCerts;
	}

	@Override
	public NstRequestType getRequestType() {
		return requestType;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	@Override
	public String getPayload() {
		return payload;
	}

	@Override
	public int getTimeoutInMilliseconds() {
		return timeout;
	}

	@Override
	public boolean getTrustAllCerts() {
		return trustAllCerts;
	}

	/**
	 * Build a NSTHttpRequestImpl instance. Default timeout is 0 (indefinite) and
	 * trust all certs defaults to true.
	 */
	public static class Builder {

		private NstRequestType requestType;
		private URL url;
		private Map<String, String> headers = new HashMap<>();
		private String payload;
		private int timeout = 0;
		private boolean trustAllCerts = true;
		
		public Builder(URL url, NstRequestType requestType) {
			this.url = Objects.requireNonNull(url, "URL MUST NOT be null.");;
			this.requestType = Objects.requireNonNull(requestType, "Request type MUST NOT be null.");
		}

		public Builder addHeader(String key, String value) {
			this.headers.put(key, value);
			return this;
		}

		public Builder setHeaders(Map<String, String> headers) {
			Objects.requireNonNull(headers, "Headers MUST NOT be null.");
			this.headers = new HashMap<>(headers);
			return this;
		}

		public Builder setPayload(String payload) {
			this.payload = payload;
			return this;
		}

		public Builder setTimeoutMilliseconds(int timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder setTrusAllCerts(boolean trustAllCerts) {
			this.trustAllCerts = trustAllCerts;
			return this;
		}

		public NSTHttpRequestImpl build() {
			return new NSTHttpRequestImpl(this);
		}
	}
}
