package com.ebay.service.protocol.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;

public class NSTHttpRequestImplTest {
	
	private URL url;
	
	@BeforeClass
	public void beforeClass() throws Exception {
		url = new URL("http://www.ebay.com");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void nullUrl() {
		new NSTHttpRequestImpl.Builder(null, NstRequestType.GET);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void nullRequestType() throws Exception {
		new NSTHttpRequestImpl.Builder(url, null);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void nullUrlAndRequestType() {
		new NSTHttpRequestImpl.Builder(null, null);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void nullHeaders() throws Exception {
		new NSTHttpRequestImpl.Builder(url, NstRequestType.GET).setHeaders(null);
	}

	@Test
	public void withDefaults() throws Exception {
		NSTHttpRequestImpl actual = new NSTHttpRequestImpl.Builder(url, NstRequestType.GET).build();
		assertThat(actual.getPayload(), is(nullValue()));
		assertThat(actual.getHeaders(), is(anEmptyMap()));
		assertThat(actual.getRequestType(), is(equalTo(NstRequestType.GET)));
		assertThat(actual.getTimeoutInMilliseconds(), is(equalTo(0)));
		assertThat(actual.getTrustAllCerts(), is(equalTo(true)));
		assertThat(actual.getUrl(), is(equalTo(url)));
	}
	
	@Test
	public void overrideDefaults() {
		NSTHttpRequestImpl actual = new NSTHttpRequestImpl.Builder(url, NstRequestType.GET).setTimeoutMilliseconds(10).setTrusAllCerts(false).build();
		assertThat(actual.getTimeoutInMilliseconds(), is(equalTo(10)));
		assertThat(actual.getTrustAllCerts(), is(equalTo(false)));
	}
	
	@Test
	public void fullySpecified() throws MalformedURLException {
		
		String payload = "payload";
		Map<String, String> headers = new HashMap<>();
		headers.put("foo", "bar");
		NstRequestType requestType = NstRequestType.POST;
		
		NSTHttpRequestImpl actual = new NSTHttpRequestImpl.Builder(url, requestType).setPayload(payload).addHeader("foo", "bar").setTimeoutMilliseconds(10).setTrusAllCerts(false).build();
		assertThat(actual.getPayload(), is(equalTo(payload)));
		assertThat(actual.getHeaders(), is(equalTo(headers)));
		assertThat(actual.getRequestType(), is(equalTo(requestType)));
		assertThat(actual.getTimeoutInMilliseconds(), is(equalTo(10)));
		assertThat(actual.getTrustAllCerts(), is(equalTo(false)));
		assertThat(actual.getUrl(), is(equalTo(url)));
	}
	
	@Test
	public void addMultipleHeaders() {

		Map<String, String> headers = new HashMap<>();
		headers.put("foo", "bar");
		headers.put("second", "value");
		
		NSTHttpRequestImpl actual = new NSTHttpRequestImpl.Builder(url, NstRequestType.DELETE).addHeader("foo", "bar").addHeader("second", "value").build();
		assertThat(actual.getHeaders(), is(equalTo(headers)));
	}
	
	@Test
	public void setHeaders() {
		
		Map<String, String> headers = new HashMap<>();
		headers.put("foo", "bar");
		headers.put("second", "value");
		
		NSTHttpRequestImpl actual = new NSTHttpRequestImpl.Builder(url, NstRequestType.GET).addHeader("test", "override").setHeaders(headers).build();
		assertThat(actual.getHeaders(), is(equalTo(headers)));
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void getHeadersUnmodifiable() {
		
		NSTHttpRequestImpl actual = new NSTHttpRequestImpl.Builder(url, NstRequestType.GET).build();
		actual.getHeaders().put("test", "value");
	}
}
