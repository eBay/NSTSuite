package com.ebay.service.logger.har.builder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class HarLogBuilder {

  /**
   * Build the har file from the request and response.
   * @param request Request to record in har file.
   * @param response Response to record in har file.
   * @return Har instance of the request and response.
   */
  public Har buildHarFromRequestResponse(NSTHttpRequest request, NSTHttpResponse response) {

    HarRequest harRequest = null;
    HarResponse harResponse = null;

    // Build up the request HAR object.
    if (request != null) {
      harRequest = new HarRequest();

      List<Header> harRequestHeaders = new ArrayList<>();
      Map<String, String> requestHeaders = request.getHeaders();
      if (requestHeaders != null) {
        Set<String> requestHeaderKeys = requestHeaders.keySet();
        for (String requestHeader : requestHeaderKeys) {
          Header header = new Header();
          header.setName(requestHeader);
          header.setValue(requestHeaders.get(requestHeader));
          harRequestHeaders.add(header);
        }
      }

      PostData postData = new PostData();

      String contentType = request.getHeaders().get("Content-Type");
      if (contentType != null) {
        postData.setMimeType(contentType);
      }

      Object payload = request.getPayload();
      if (payload != null) {
        postData.setText(payload.toString());
      }

      List<QueryString> queryStrings = new ArrayList<>();

      URL endpointUrl = request.getUrl();

      if (endpointUrl != null) {
        String endpoint = endpointUrl.toString();
        if (endpoint.contains("?")) {
          String paramString = endpoint.substring(endpoint.indexOf("?") + 1);
          if (paramString != null && !paramString.isEmpty()) {
        	  String[] params = paramString.split("&");
              for (String param : params) {
                String[] keyValuePair = param.split("=");
                QueryString queryString = new QueryString();
                queryString.setName(keyValuePair[0]);
                if (keyValuePair.length == 2) {
                	queryString.setValue(keyValuePair[1]);
                }
                queryStrings.add(queryString);
              }
          }
        }
      }

      harRequest.setHeaders(harRequestHeaders);
      // harRequest.setHttpVersion(httpVersion);
      harRequest.setMethod(request.getRequestType());
      harRequest.setPostData(postData);
      harRequest.setQueryString(queryStrings);
      harRequest.setUrl(request.getUrl());
    }

    // Build up the response HAR object.
    if (response != null) {
      harResponse = new HarResponse();

      Content content = new Content();
      String responseText = response.getPayload();
      if (responseText != null && !responseText.isEmpty()) {
        content.setText(responseText);
      }

      Map<String, String> responseHeaders = response.getHeaders();
      List<Header> harResponseHeaders = new ArrayList<>();
      if (responseHeaders != null) {
        Set<String> responseHeaderKeys = responseHeaders.keySet();
        for (String responseHeader : responseHeaderKeys) {
          Header header = new Header();
          header.setName(responseHeader);
          header.setValue(responseHeaders.get(responseHeader));
          harResponseHeaders.add(header);
        }
      }

      harResponse.setContent(content);
      harResponse.setHeaders(harResponseHeaders);
      // harResponse.setHttpVersion(httpVersion);
      harResponse.setStatus(response.getResponseCode());
      // harResponse.setStatusText(response.);
    }

    // Put all the HAR together.
    Entry entry = new Entry();
    if (harRequest != null) {
      entry.setRequest(harRequest);
    }
    if (harResponse != null) {
      entry.setResponse(harResponse);
    }

    List<Entry> entries = new ArrayList<>();
    entries.add(entry);

    Creator creator = new Creator();
    creator.setName("NST HAR logger");
    creator.setVersion(getClass().getPackage().getImplementationVersion());

    Log log = new Log();
    log.setVersion("1.2");
    log.setCreator(creator);
    log.setEntries(entries);

    Har har = new Har();
    har.setLog(log);

    return har;
  }
}
