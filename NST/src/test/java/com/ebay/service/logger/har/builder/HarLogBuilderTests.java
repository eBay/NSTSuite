package com.ebay.service.logger.har.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.service.logger.har.Content;
import com.ebay.service.logger.har.Creator;
import com.ebay.service.logger.har.Entry;
import com.ebay.service.logger.har.Har;
import com.ebay.service.logger.har.HarRequest;
import com.ebay.service.logger.har.HarResponse;
import com.ebay.service.logger.har.Header;
import com.ebay.service.logger.har.Log;
import com.ebay.service.logger.har.PostData;
import com.ebay.service.logger.har.QueryString;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class HarLogBuilderTests {

  @Test(groups = "unitTest")
  public void convertRequestAndResponseToHar() throws Throwable {

    // Prepare request input
    Map<String, String> testRequestHeaders = new HashMap<>();
    testRequestHeaders.put("user", "bob");
    testRequestHeaders.put("job", "tester");
    testRequestHeaders.put("Content-Type", "application/json");

    NSTHttpRequest request = mock(NSTHttpRequest.class);
    when(request.getRequestType()).thenReturn(NstRequestType.POST);
    when(request.getHeaders()).thenReturn(testRequestHeaders);
    when(request.getPayload()).thenReturn("{\"foo\":\"bar\",\"shampoo\",\"is better\"}");
    when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));

    // Prepare response input
    String payload = "{\"funFact\":\"This is a mock json string\",\"and\":\"here is another\"}";

    Map<String, String> testResponseHeaders = new HashMap<>();
    testResponseHeaders.put("service", "zero");
    testResponseHeaders.put("action", "now");

    NSTHttpResponse response = mock(NSTHttpResponse.class);
    when(response.getPayload()).thenReturn(payload);
    when(response.getHeaders()).thenReturn(testResponseHeaders);
    when(response.getResponseCode()).thenReturn(200);

    // Build up the expected HAR object.
    // Start with the request.
    List<Header> harRequestHeaders = new ArrayList<>();

    Header requestHeader2 = new Header();
    requestHeader2.setName("job");
    requestHeader2.setValue("tester");
    harRequestHeaders.add(requestHeader2);

    Header requestHeader1 = new Header();
    requestHeader1.setName("user");
    requestHeader1.setValue("bob");
    harRequestHeaders.add(requestHeader1);
    
    Header requestHeader3 = new Header();
    requestHeader3.setName("Content-Type");
    requestHeader3.setValue("application/json");
    harRequestHeaders.add(requestHeader3);

    PostData postData = new PostData();
    postData.setMimeType("application/json");
    postData.setText("{\"foo\":\"bar\",\"shampoo\",\"is better\"}");

    List<QueryString> queryStrings = new ArrayList<>();

    QueryString queryString1 = new QueryString();
    queryString1.setName("param1");
    queryString1.setValue("abc");
    queryStrings.add(queryString1);

    QueryString queryString2 = new QueryString();
    queryString2.setName("param2");
    queryString2.setValue("1");
    queryStrings.add(queryString2);

    HarRequest harRequest = new HarRequest();
    harRequest.setHeaders(harRequestHeaders);
    harRequest.setMethod(NstRequestType.POST);
    harRequest.setPostData(postData);
    harRequest.setQueryString(queryStrings);
    harRequest.setUrl("http://test.com/random/endpoint?param1=abc&param2=1");

    // Build up the resonse HAR object.
    Content content = new Content();
    content.setText(payload);

    List<Header> harResponseHeaders = new ArrayList<>();

    Header responseHeadereader1 = new Header();
    responseHeadereader1.setName("service");
    responseHeadereader1.setValue("zero");
    harResponseHeaders.add(responseHeadereader1);

    Header responseHeadereader2 = new Header();
    responseHeadereader2.setName("action");
    responseHeadereader2.setValue("now");
    harResponseHeaders.add(responseHeadereader2);

    HarResponse harResponse = new HarResponse();
    harResponse.setContent(content);
    harResponse.setHeaders(harResponseHeaders);
    harResponse.setStatus(200);

    // Put all the HAR together.
    Entry entry = new Entry();
    entry.setRequest(harRequest);
    entry.setResponse(harResponse);

    List<Entry> entries = new ArrayList<>();
    entries.add(entry);

    Creator creator = new Creator();
    creator.setName("NST HAR logger");
    creator.setVersion(getClass().getPackage().getImplementationVersion());

    Log log = new Log();
    log.setVersion("1.2");
    log.setCreator(creator);
    log.setEntries(entries);

    Har expectedHar = new Har();
    expectedHar.setLog(log);

    // Now do the test
    HarLogBuilder harLogBuilder = new HarLogBuilder();
    Har actualHar = harLogBuilder.buildHarFromRequestResponse(request, response);

    assertThat(actualHar, is(equalTo(expectedHar)));
  }

  @Test(groups = "unitTest")
  public void convertNullRequestAndResponseToHar() throws Throwable {

    // Prepare response input
	String payload = "{\"funFact\":\"This is a mock json string\",\"and\":\"here is another\"}";

    Map<String, String> testResponseHeaders = new HashMap<>();
    testResponseHeaders.put("service", "zero");
    testResponseHeaders.put("action", "now");

    NSTHttpResponse response = mock(NSTHttpResponse.class);
    when(response.getPayload()).thenReturn(payload);
    when(response.getHeaders()).thenReturn(testResponseHeaders);
    when(response.getResponseCode()).thenReturn(200);

    // Build up the expected HAR object.
    // Build up the resonse HAR object.
    Content content = new Content();
    content.setText("{\"funFact\":\"This is a mock json string\",\"and\":\"here is another\"}");

    List<Header> harResponseHeaders = new ArrayList<>();

    Header responseHeadereader1 = new Header();
    responseHeadereader1.setName("service");
    responseHeadereader1.setValue("zero");
    harResponseHeaders.add(responseHeadereader1);

    Header responseHeadereader2 = new Header();
    responseHeadereader2.setName("action");
    responseHeadereader2.setValue("now");
    harResponseHeaders.add(responseHeadereader2);

    HarResponse harResponse = new HarResponse();
    harResponse.setContent(content);
    harResponse.setHeaders(harResponseHeaders);
    // harResponse.setHttpVersion(httpVersion);
    harResponse.setStatus(200);
    // harResponse.setStatusText(response.);

    // Put all the HAR together.
    Entry entry = new Entry();
    entry.setResponse(harResponse);

    List<Entry> entries = new ArrayList<>();
    entries.add(entry);

    Creator creator = new Creator();
    creator.setName("NST HAR logger");
    creator.setVersion(getClass().getPackage().getImplementationVersion());

    Log log = new Log();
    log.setVersion("1.2");
    log.setCreator(creator);
    log.setEntries(entries);

    Har expectedHar = new Har();
    expectedHar.setLog(log);

    // Now do the test
    HarLogBuilder harLogBuilder = new HarLogBuilder();
    Har actualHar = harLogBuilder.buildHarFromRequestResponse(null, response);

    assertThat(actualHar, is(equalTo(expectedHar)));
  }

  @Test(groups = "unitTest")
  public void convertRequestAndNullResponseToHar() throws Throwable {

    // Prepare request input
    Map<String, String> testRequestHeaders = new HashMap<>();
    testRequestHeaders.put("user", "bob");
    testRequestHeaders.put("job", "tester");
    testRequestHeaders.put("Content-Type", "application/json");

    NSTHttpRequest request = mock(NSTHttpRequest.class);
    when(request.getRequestType()).thenReturn(NstRequestType.POST);
    when(request.getHeaders()).thenReturn(testRequestHeaders);
    when(request.getPayload()).thenReturn("{\"foo\":\"bar\",\"shampoo\",\"is better\"}");
    when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));

    // Build up the expected HAR object.
    // Start with the request.
    List<Header> harRequestHeaders = new ArrayList<>();

    Header requestHeader2 = new Header();
    requestHeader2.setName("job");
    requestHeader2.setValue("tester");
    harRequestHeaders.add(requestHeader2);

    Header requestHeader1 = new Header();
    requestHeader1.setName("user");
    requestHeader1.setValue("bob");
    harRequestHeaders.add(requestHeader1);
    
    Header requestHeader3 = new Header();
    requestHeader3.setName("Content-Type");
    requestHeader3.setValue("application/json");
    harRequestHeaders.add(requestHeader3);

    PostData postData = new PostData();
    postData.setMimeType("application/json");
    postData.setText("{\"foo\":\"bar\",\"shampoo\",\"is better\"}");

    List<QueryString> queryStrings = new ArrayList<>();

    QueryString queryString1 = new QueryString();
    queryString1.setName("param1");
    queryString1.setValue("abc");
    queryStrings.add(queryString1);

    QueryString queryString2 = new QueryString();
    queryString2.setName("param2");
    queryString2.setValue("1");
    queryStrings.add(queryString2);

    HarRequest harRequest = new HarRequest();
    harRequest.setHeaders(harRequestHeaders);
    harRequest.setMethod(NstRequestType.POST);
    harRequest.setPostData(postData);
    harRequest.setQueryString(queryStrings);
    harRequest.setUrl("http://test.com/random/endpoint?param1=abc&param2=1");

    // Put all the HAR together.
    Entry entry = new Entry();
    entry.setRequest(harRequest);

    List<Entry> entries = new ArrayList<>();
    entries.add(entry);

    Creator creator = new Creator();
    creator.setName("NST HAR logger");
    creator.setVersion(getClass().getPackage().getImplementationVersion());

    Log log = new Log();
    log.setVersion("1.2");
    log.setCreator(creator);
    log.setEntries(entries);

    Har expectedHar = new Har();
    expectedHar.setLog(log);

    // Now do the test
    HarLogBuilder harLogBuilder = new HarLogBuilder();
    Har actualHar = harLogBuilder.buildHarFromRequestResponse(request, null);

    assertThat(actualHar, is(equalTo(expectedHar)));
  }

  @Test(groups = "unitTest")
  public void convertNullRequestAndNullResponseToHar() throws Throwable {

    // Build up the expected HAR object.
    // Put all the HAR together.
    Entry entry = new Entry();

    List<Entry> entries = new ArrayList<>();
    entries.add(entry);

    Creator creator = new Creator();
    creator.setName("NST HAR logger");
    creator.setVersion(getClass().getPackage().getImplementationVersion());

    Log log = new Log();
    log.setVersion("1.2");
    log.setCreator(creator);
    log.setEntries(entries);

    Har expectedHar = new Har();
    expectedHar.setLog(log);

    // Now do the test
    HarLogBuilder harLogBuilder = new HarLogBuilder();
    Har actualHar = harLogBuilder.buildHarFromRequestResponse(null, null);

    assertThat(actualHar, is(equalTo(expectedHar)));
  }
  
  @Test(groups = "unitTest")
  public void convertQueryParamWithMissingValue() throws Exception {
	  
	  // Prepare request input
	    Map<String, String> testRequestHeaders = new HashMap<>();
	    testRequestHeaders.put("user", "bob");
	    testRequestHeaders.put("job", "tester");
	    testRequestHeaders.put("Content-Type", "application/json");

	    NSTHttpRequest request = mock(NSTHttpRequest.class);
	    when(request.getRequestType()).thenReturn(NstRequestType.POST);
	    when(request.getHeaders()).thenReturn(testRequestHeaders);
	    when(request.getPayload()).thenReturn("{\"foo\":\"bar\",\"shampoo\",\"is better\"}");
	    when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?paramMissing=&param2=1"));

	    // Prepare response input
	    String payload = "{\"funFact\":\"This is a mock json string\",\"and\":\"here is another\"}";

	    Map<String, String> testResponseHeaders = new HashMap<>();
	    testResponseHeaders.put("service", "zero");
	    testResponseHeaders.put("action", "now");

	    NSTHttpResponse response = mock(NSTHttpResponse.class);
	    when(response.getPayload()).thenReturn(payload);
	    when(response.getHeaders()).thenReturn(testResponseHeaders);
	    when(response.getResponseCode()).thenReturn(200);

	    // Build up the expected HAR object.
	    // Start with the request.
	    List<Header> harRequestHeaders = new ArrayList<>();

	    Header requestHeader2 = new Header();
	    requestHeader2.setName("job");
	    requestHeader2.setValue("tester");
	    harRequestHeaders.add(requestHeader2);

	    Header requestHeader1 = new Header();
	    requestHeader1.setName("user");
	    requestHeader1.setValue("bob");
	    harRequestHeaders.add(requestHeader1);
	    
	    Header requestHeader3 = new Header();
	    requestHeader3.setName("Content-Type");
	    requestHeader3.setValue("application/json");
	    harRequestHeaders.add(requestHeader3);

	    PostData postData = new PostData();
	    postData.setMimeType("application/json");
	    postData.setText("{\"foo\":\"bar\",\"shampoo\",\"is better\"}");

	    List<QueryString> queryStrings = new ArrayList<>();

	    QueryString queryString1 = new QueryString();
	    queryString1.setName("paramMissing");
	    queryString1.setValue(null);
	    queryStrings.add(queryString1);

	    QueryString queryString2 = new QueryString();
	    queryString2.setName("param2");
	    queryString2.setValue("1");
	    queryStrings.add(queryString2);

	    HarRequest harRequest = new HarRequest();
	    harRequest.setHeaders(harRequestHeaders);
	    harRequest.setMethod(NstRequestType.POST);
	    harRequest.setPostData(postData);
	    harRequest.setQueryString(queryStrings);
	    harRequest.setUrl("http://test.com/random/endpoint?paramMissing=&param2=1");

	    // Build up the resonse HAR object.
	    Content content = new Content();
	    content.setText("{\"funFact\":\"This is a mock json string\",\"and\":\"here is another\"}");

	    List<Header> harResponseHeaders = new ArrayList<>();

	    Header responseHeadereader1 = new Header();
	    responseHeadereader1.setName("service");
	    responseHeadereader1.setValue("zero");
	    harResponseHeaders.add(responseHeadereader1);

	    Header responseHeadereader2 = new Header();
	    responseHeadereader2.setName("action");
	    responseHeadereader2.setValue("now");
	    harResponseHeaders.add(responseHeadereader2);

	    HarResponse harResponse = new HarResponse();
	    harResponse.setContent(content);
	    harResponse.setHeaders(harResponseHeaders);
	    // harResponse.setHttpVersion(httpVersion);
	    harResponse.setStatus(200);
	    // harResponse.setStatusText(response.);

	    // Put all the HAR together.
	    Entry entry = new Entry();
	    entry.setRequest(harRequest);
	    entry.setResponse(harResponse);

	    List<Entry> entries = new ArrayList<>();
	    entries.add(entry);

	    Creator creator = new Creator();
	    creator.setName("NST HAR logger");
	    creator.setVersion(getClass().getPackage().getImplementationVersion());

	    Log log = new Log();
	    log.setVersion("1.2");
	    log.setCreator(creator);
	    log.setEntries(entries);

	    Har expectedHar = new Har();
	    expectedHar.setLog(log);

	    // Now do the test
	    HarLogBuilder harLogBuilder = new HarLogBuilder();
	    Har actualHar = harLogBuilder.buildHarFromRequestResponse(request, response);

	    assertThat(actualHar, is(equalTo(expectedHar)));
  }
}
