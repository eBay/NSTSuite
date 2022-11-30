package com.ebay.service.logger.call.cache;

import java.net.URL;

import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.ebay.service.protocol.http.NSTHttpResponseImpl;

public class ServiceCallCacheDataTest {

	@Test(expectedExceptions = NullPointerException.class)
	public void initializeWithNullRequest() {
		new ServiceCallCacheData(null, new NSTHttpResponseImpl(), "foo");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void initializeWithNullResponse() throws Exception {
		NSTHttpRequestImpl request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		new ServiceCallCacheData(request , null, "foo");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void initializeWithNullServiceCallName() throws Exception {
		NSTHttpRequestImpl request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		new ServiceCallCacheData(request, new NSTHttpResponseImpl(), null);
	}
}
