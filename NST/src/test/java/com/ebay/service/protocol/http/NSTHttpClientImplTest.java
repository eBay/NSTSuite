package com.ebay.service.protocol.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;

public class NSTHttpClientImplTest {
	
	private NSTHttpClientImpl implementation = new NSTHttpClientImpl();
	private HttpURLConnection connection;
	private URL url;
	
	private static final String payload = "{ \"test\": \"payload\" }";
	private static final int timeout = 100;
	private static final String firstHeaderKey = "first";
	private static final List<String> firstHeaderValues = Arrays.asList("one", "two");
	private static final String firstHeaderValuesExpected = "one two";
	private static final String secondHeaderKey = "second";
	private static final List<String> secondHeaderValues = Arrays.asList("apple");
	private static final String secondHeaderValuesExpected = "apple";
	
	@BeforeMethod
	public void beforeEachParseResponseTest() throws IOException {
		
		// Mocked connection
		
		InputStream inputStream = mock(InputStream.class);
		when(inputStream.read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(-1);
		
		OutputStream outputStream = mock(OutputStream.class);
		doNothing().when(outputStream).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		
		Map<String, List<String>> headers = new HashMap<>();
		headers.put(firstHeaderKey, firstHeaderValues);
		headers.put(secondHeaderKey, secondHeaderValues);
		
		connection = Mockito.mock(HttpURLConnection.class);
		when(connection.getResponseCode()).thenReturn(200);
		when(connection.getInputStream()).thenReturn(inputStream);
		when(connection.getHeaderFields()).thenReturn(headers);
		when(connection.getOutputStream()).thenReturn(outputStream);
		doNothing().when(connection).disconnect();
		
		// Mocked URL
		
		url = mock(URL.class);
		when(url.openConnection()).thenReturn(connection);
	}
	
	// -------------------------------
	// sendRequst() test cases
	// -------------------------------
	
	@Test(expectedExceptions = NullPointerException.class)
	public void nullRequest() throws Exception {
		implementation.sendRequest(null);
	}
	
	@Test
	public void put() throws Exception {
		
		NstRequestType requestType = NstRequestType.PUT;
		
		NSTHttpRequestImpl request = new NSTHttpRequestImpl.Builder(url, requestType).addHeader(secondHeaderKey, secondHeaderValuesExpected).setPayload(payload).setTimeoutMilliseconds(timeout).build();
		implementation.sendRequest(request);
		
		verify(connection, times(1)).addRequestProperty(secondHeaderKey, secondHeaderValuesExpected);
		verify(connection, times(1)).setConnectTimeout(timeout);
		verify(connection, times(1)).setRequestMethod(requestType.name());
		verify(connection, times(1)).setDoOutput(true);
		verify(connection, times(1)).getOutputStream();
		verify(connection, times(0)).connect();
		verify(connection, times(1)).disconnect();
	}
	
	@Test
	public void post() throws Exception {
		
		NstRequestType requestType = NstRequestType.POST;
		
		NSTHttpRequestImpl request = new NSTHttpRequestImpl.Builder(url, requestType).addHeader(secondHeaderKey, secondHeaderValuesExpected).setPayload(payload).setTimeoutMilliseconds(timeout).build();
		implementation.sendRequest(request);
		
		verify(connection, times(1)).addRequestProperty(secondHeaderKey, secondHeaderValuesExpected);
		verify(connection, times(1)).setConnectTimeout(timeout);
		verify(connection, times(1)).setRequestMethod(requestType.name());
		verify(connection, times(1)).setDoOutput(true);
		verify(connection, times(1)).getOutputStream();
		verify(connection, times(0)).connect();
		verify(connection, times(1)).disconnect();
	}
	
	@Test
	public void get() throws Exception {
		
		NstRequestType requestType = NstRequestType.GET;
		
		NSTHttpRequestImpl request = new NSTHttpRequestImpl.Builder(url, requestType).addHeader(secondHeaderKey, secondHeaderValuesExpected).setPayload(payload).setTimeoutMilliseconds(timeout).build();
		implementation.sendRequest(request);
		
		verify(connection, times(1)).addRequestProperty(secondHeaderKey, secondHeaderValuesExpected);
		verify(connection, times(1)).setConnectTimeout(timeout);
		verify(connection, times(1)).setRequestMethod(requestType.name());
		verify(connection, times(0)).setDoOutput(Mockito.anyBoolean());
		verify(connection, times(0)).getOutputStream();
		verify(connection, times(1)).connect();
		verify(connection, times(1)).disconnect();
	}
	
	@Test
	public void delete() throws Exception {
		
		NstRequestType requestType = NstRequestType.DELETE;
		
		NSTHttpRequestImpl request = new NSTHttpRequestImpl.Builder(url, requestType).addHeader(secondHeaderKey, secondHeaderValuesExpected).setPayload(payload).setTimeoutMilliseconds(timeout).build();
		implementation.sendRequest(request);
		
		verify(connection, times(1)).addRequestProperty(secondHeaderKey, secondHeaderValuesExpected);
		verify(connection, times(1)).setConnectTimeout(timeout);
		verify(connection, times(1)).setRequestMethod(requestType.name());
		verify(connection, times(1)).setDoOutput(true);
		verify(connection, times(1)).getOutputStream();
		verify(connection, times(0)).connect();
		verify(connection, times(1)).disconnect();
	}
	
	// -------------------------------
	// parseResponse() test cases
	// -------------------------------

	@Test
	public void nullConnection() throws Exception {
	
		NSTHttpResponseImpl actual = (NSTHttpResponseImpl) implementation.parseResponse(null);
		assertThat(actual.getPayload(), is(nullValue()));
		assertThat(actual.getHeaders(), is(anEmptyMap()));
		assertThat(actual.getResponseCode(), is(equalTo(0)));
	}
	
	@Test
	public void nullConnectionHeaderFields() throws Exception {
		
		when(connection.getHeaderFields()).thenReturn(null);
		NSTHttpResponseImpl actual = (NSTHttpResponseImpl) implementation.parseResponse(connection);
		assertThat(actual.getHeaders(), is(anEmptyMap()));
	}
	
	@Test
	public void fullResponse() throws Exception {
		
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put(firstHeaderKey, firstHeaderValuesExpected);
		expectedHeaders.put(secondHeaderKey, secondHeaderValuesExpected);
	
	    InputStream targetStream = new ByteArrayInputStream(payload.getBytes());
	    when(connection.getInputStream()).thenReturn(targetStream);
	    NSTHttpResponseImpl actual = (NSTHttpResponseImpl) implementation.parseResponse(connection);
	    assertThat(actual.getPayload(), is(equalTo(payload)));
		assertThat(actual.getHeaders(), is(equalTo(expectedHeaders)));
		assertThat(actual.getResponseCode(), is(equalTo(200)));
	}
}
