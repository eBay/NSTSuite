package com.ebay.service.logger.formats;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.ebay.nst.hosts.manager.HostsManager;
import com.ebay.service.logger.ClassAndMethodName;
import com.ebay.service.logger.FormatWriterUtil;
import com.ebay.service.logger.writer.Encode;
import com.ebay.service.logger.writer.FileWriterWithEncoding;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class ErrorLogger {
	
  public static final String errorLoggerSuffix = "Response_ERROR.txt";

  public String writeFile(NSTHttpRequest request, NSTHttpResponse response, String serviceWrapperName) throws IOException {

	ClassAndMethodName classAndMethodName = FormatWriterUtil.getClassAndMethodName();
    String outputPath = FormatWriterUtil.getFileName(classAndMethodName.getClassName(), classAndMethodName.getMethodName(), 0, serviceWrapperName) + errorLoggerSuffix;
    outputPath = String.format("%s%s%s", System.getProperty("user.dir"), File.separator, outputPath);
    
    FileWriterWithEncoding writer = new FileWriterWithEncoding(outputPath, Encode.UTF_8);
    writer.write(getRequestLog(request));
    writer.write("\n\n");
    writer.write(getResponseLog(response));
    writer.close();

    return outputPath;
  }

  private String getRequestLog(NSTHttpRequest request) {

    StringBuilder requestLogBuilder = new StringBuilder();
    requestLogBuilder.append("```requestStart---\n");

    if (request != null) {

      // ----------------- Meta Block --------------------
      String concreteImplementationName = this.getClass().getSimpleName();
      String environment = "Unknown";
      try {
        environment = HostsManager.getInstance().getPoolType().name();
      } catch (Throwable e) {
        // DO NOTHING
      }

      requestLogBuilder.append("\t---- META INFO ----\n");
      requestLogBuilder.append(String.format("\tREQUEST: %s\n", concreteImplementationName));
      requestLogBuilder.append(String.format("\tENVIRONMENT: %s\n", environment));
      requestLogBuilder.append(String.format("\tREQUEST URL: %s\n", request.getUrl()));
      requestLogBuilder.append(String.format("\tTIMEOUT (Milliseconds): %d\n", request.getTimeoutInMilliseconds()));
      requestLogBuilder.append("\n");

      // ----------------- Header Block --------------------
      requestLogBuilder.append("\t---- HEADER INFO ----\n");

      Map<String, String> headers = request.getHeaders();
      for (String key : headers.keySet()) {
        String header = String.format("\t%s : %s\n", key, headers.get(key));
        requestLogBuilder.append(header);
      }
      requestLogBuilder.append("\n");

      // ----------------- Payload Block --------------------
      requestLogBuilder.append("\t---- PAYLOAD INFO ----\n");

      if (request.getPayload() != null) {
        String jsonString = request.getPayload();
        requestLogBuilder.append(String.format("\t%s\n\n", jsonString));
      } else {
        requestLogBuilder.append("\tNo request payload\n");
      }

    } else {
      requestLogBuilder.append("\tRequest is null.\n");
    }

    requestLogBuilder.append("```requestEnd---\n");
    return requestLogBuilder.toString();
  }

  private String getResponseLog(NSTHttpResponse response) {

    StringBuilder responseLogBuilder = new StringBuilder();
    responseLogBuilder.append("```responseStart---\n");

    if (response != null) {

      // ----------------- Meta Block --------------------
      String concreteImplementationName = this.getClass().getSimpleName();
      String environment = "Unknown";
      try {
        environment = HostsManager.getInstance().getPoolType().name();
      } catch (Throwable e) {
        // DO NOTHING
      }

      responseLogBuilder.append(String.format("\tREQUEST: %s\n", concreteImplementationName));
      responseLogBuilder.append(String.format("\tENVIRONMENT: %s\n", environment));
      responseLogBuilder.append(String.format("\tRESPONSE STATUS: %d\n", response.getResponseCode()));
      responseLogBuilder.append("\n");

      // ----------------- Header Block --------------------
      responseLogBuilder.append("\t---- HEADER INFO ----\n");

      Map<String, String> headers = response.getHeaders();
      for (String key : headers.keySet()) {
        String header = String.format("\t%s : %s\n", key, headers.get(key));
        responseLogBuilder.append(header);
      }
      responseLogBuilder.append("\n");

      // ----------------- Payload Block --------------------
      String jsonString = response.getPayload();
      responseLogBuilder.append(String.format("\t%s\n\n", jsonString));

    } else {
      responseLogBuilder.append("\tResponse is null.\n");
    }

    responseLogBuilder.append("```responseEnd---\n");
    return responseLogBuilder.toString();
  }


}
