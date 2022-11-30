package com.ebay.service.logger.formats;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.service.logger.formats.filters.TextFileFilter;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class ErrorLoggerTests {

  private static final String requestPayloadContents = "{\"size\":\"large\",\"color\":\"blue\"}";
  private static final String responsePayloadContents = "{\"paid\":true,\"shipped\":false}";
  private static final String serviceWrapperName = "TestWrapper";
  private static final String currentWorkingDirectory = System.getProperty("user.dir");

  NSTHttpRequest request = Mockito.mock(NSTHttpRequest.class);
  NSTHttpResponse response = Mockito.mock(NSTHttpResponse.class);

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() throws IllegalStateException, IOException, JSONException {
    cleanup();
    
    JSONObject requestPayload = new JSONObject(requestPayloadContents);
    when(request.getPayload()).thenReturn(requestPayload.toString());
    when(request.getRequestType()).thenReturn(NstRequestType.POST);
    when(request.getUrl()).thenReturn(new URL("http://www.ebay.com"));

    JSONObject responsePayload = new JSONObject(responsePayloadContents);
    when(response.getPayload()).thenReturn(responsePayload.toString());
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() throws IllegalStateException, IOException {
    cleanup();
  }

  // --------------------------- Tests ---------------------------

  @Test(groups = "unitTest")
  public void outputPath() throws IOException {

    ErrorLogger errorLogger = new ErrorLogger();
    String path = errorLogger.writeFile(request, response, serviceWrapperName);

    assertThat("Path should exist for response case.", path, containsString(String.format("ErrorLoggerTests_outputPath_0_%sResponse_ERROR.txt", serviceWrapperName)));

    String[] paths = path.split("\n");
    assertThat("Should be a single path for the error file.", paths.length, is(equalTo(1)));
  }

  @Test(groups = "unitTest")
  public void nullRequestNullResponse() throws IOException {

    ErrorLogger errorLogger = new ErrorLogger();
    errorLogger.writeFile(null, null, serviceWrapperName);

    File outputFolder = new File(currentWorkingDirectory);
    File[] errorFiles = outputFolder.listFiles(new TextFileFilter());

    assertThat("One error file should have been written.", errorFiles.length, is(equalTo(1)));

    String fileContents = getFileContents(errorFiles[0]);
    String expectedContents = "```requestStart---\tRequest is null.```requestEnd---```responseStart---\tResponse is null.```responseEnd---";
    assertThat("Error contents should not contain request and response data.", fileContents, is(equalTo(expectedContents)));
  }

  @Test(groups = "unitTest")
  public void validRequestNullResponse() throws IOException {

    ErrorLogger errorLogger = new ErrorLogger();
    errorLogger.writeFile(request, null, serviceWrapperName);

    File outputFolder = new File(currentWorkingDirectory);
    File[] errorFiles = outputFolder.listFiles(new TextFileFilter());

    assertThat("One error file should have been written.", errorFiles.length, is(equalTo(1)));

    String fileContents = getFileContents(errorFiles[0]);
    String invalidCheckContents = "```requestStart---\tRequest is null.";
    String validCheckContents = "```requestEnd---```responseStart---\tResponse is null.```responseEnd---";
    assertThat("Error contents should not contain response data.", fileContents, containsString(validCheckContents));
    assertThat("Error contents should contain request data.", fileContents, is(not(containsString(invalidCheckContents))));
  }

  @Test(groups = "unitTest")
  public void nullRequestValidResponse() throws IOException {

    ErrorLogger errorLogger = new ErrorLogger();
    errorLogger.writeFile(null, response, serviceWrapperName);

    File outputFolder = new File(currentWorkingDirectory);
    File[] errorFiles = outputFolder.listFiles(new TextFileFilter());

    assertThat("One error file should have been written.", errorFiles.length, is(equalTo(1)));

    String fileContents = getFileContents(errorFiles[0]);
    String validCheckContents = "```requestStart---\tRequest is null.```requestEnd---```";
    String invalidCheckContents = "responseStart---\tResponse is null.```responseEnd---";
    assertThat("Error contents should not contain request data.", fileContents, containsString(validCheckContents));
    assertThat("Error contents should contain response data.", fileContents, is(not(containsString(invalidCheckContents))));
  }

  @Test(groups = "unitTest")
  public void validRequestValidResponse() throws IOException {

    ErrorLogger errorLogger = new ErrorLogger();
    errorLogger.writeFile(request, response, serviceWrapperName);

    File outputFolder = new File(currentWorkingDirectory);
    File[] errorFiles = outputFolder.listFiles(new TextFileFilter());

    assertThat("One error file should have been written.", errorFiles.length, is(equalTo(1)));

    String fileContents = getFileContents(errorFiles[0]);
    String invalidRequestContents = "```requestStart---\tRequest is null.```requestEnd---```";
    String invalidResponseContents = "responseStart---\tResponse is null.```responseEnd---";
    assertThat("Error contents should contain request data.", fileContents, is(not(containsString(invalidRequestContents))));
    assertThat("Error contents should contain response data.", fileContents, is(not(containsString(invalidResponseContents))));
  }

  // ------------------ Private Methods -----------------

  private void cleanup() throws IllegalStateException, IOException {

    File outputFolder = new File(currentWorkingDirectory);
    if (!outputFolder.exists()) {
      return;
    }

    File[] files = outputFolder.listFiles();
    for (File file : files) {
      if (file.isFile() && file.getCanonicalPath().endsWith(ErrorLogger.errorLoggerSuffix)) {
	      if (!file.delete()) {
	        throw new IllegalStateException(String.format("Unable to delete file: %s", file.getAbsoluteFile()));
	      }
      }
    }
  }

  private String getFileContents(File file) throws IOException {

    FileInputStream fileInputStream = new FileInputStream(file);
    InputStreamReader streamReader = new InputStreamReader(fileInputStream, "UTF-8");
    BufferedReader reader = new BufferedReader(streamReader);

    StringBuilder fileContents = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      fileContents.append(line);
    }

    reader.close();
    return fileContents.toString();
  }
}
