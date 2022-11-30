package com.ebay.service.logger.call.cache;

import java.util.Objects;

import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class ServiceCallCacheData {

	private NSTHttpRequest request;
	private NSTHttpResponse response;
	private String serviceCallName;

	public ServiceCallCacheData(NSTHttpRequest request, NSTHttpResponse response, String serviceCallName) {
		this.request = Objects.requireNonNull(request, "Request MUST NOT be null");
		this.response =  Objects.requireNonNull(response, "Response MUST NOT be null");
		this.serviceCallName =  Objects.requireNonNull(serviceCallName, "serviceCallName MUST NOT be null");
	}

	public NSTHttpRequest getRequest() {
		return request;
	}

	public NSTHttpResponse getResponse() {
		return response;
	}

	public String getServiceCallName() {
		return serviceCallName;
	}

}
