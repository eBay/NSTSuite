package com.ebay.service.logger.platforms.support;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.ebay.service.logger.ServiceLoggerFileNameGenerator;
import com.ebay.service.logger.writer.Encode;
import com.ebay.service.logger.writer.FileWriterWithEncoding;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

/**
 * Write only the JSON payload to file as *Request.json and/or *Response.json depending on which instances are provided.
 */
public class JsonLogger {

  private String loggedPaths = "";

  public String writeFile(NSTHttpRequest request, NSTHttpResponse response, String serviceWrapperApiName, String outputFolder, String site) throws IOException {

	Objects.requireNonNull(request, "NSTHttpRequest MUST NOT be null.");
	Objects.requireNonNull(response, "NSTHttpResponse MUST NOT be null.");
	Objects.requireNonNull(serviceWrapperApiName, "Service wrapper API name MUST NOT be null.");
	Objects.requireNonNull(outputFolder, "Output folder MUST NOT be null.");
	Objects.requireNonNull(site, "Site MUST NOT be null.");
	  
    loggedPaths = "";
    String filenameRoot = "";

    // Only capture once so the index number looks correct across both the request and response files.
    if (request != null || response != null) {
      filenameRoot = getOutputFolderAndFileName(outputFolder, serviceWrapperApiName, site);
    }

    if (request != null) {

      String requestFilePath = filenameRoot + "Request.json";
      addLoggedPath(requestFilePath);

      FileWriterWithEncoding writer = new FileWriterWithEncoding(requestFilePath, Encode.UTF_8);
      String requestPayload = request.getPayload();
      writer.write(requestPayload);
      writer.close();
    }

    if (response != null) {

      String responseFilePath = filenameRoot + "Response.json";
      addLoggedPath(responseFilePath);

      FileWriterWithEncoding writer = new FileWriterWithEncoding(responseFilePath, Encode.UTF_8);
      String payload = response.getPayload();
      writer.write(payload);
      writer.close();

    }

    return getLoggedPaths();
  }

  private String getLoggedPaths() {
    return loggedPaths;
  }

  private void addLoggedPath(String path) {
    if (loggedPaths.equals("")) {
      loggedPaths = path;
    } else {
      loggedPaths = loggedPaths + System.lineSeparator() + path;
    }
  }
  
  private String getOutputFolderAndFileName(String outputFolder, String serviceWrapperApiName, String site) throws IOException {

    return String.format("%s%s%s", outputFolder, File.separator, getFileName(serviceWrapperApiName));
  }
  
  private String getFileName(String serviceWrapperApiName) throws IOException {

    String fileName = ServiceLoggerFileNameGenerator.getInstance().getCallingClassAndMethodSignature(serviceWrapperApiName);
    if (fileName == null) {
      throw new IOException("Unable to generate file name.");
    }

    return fileName;
  }
}
