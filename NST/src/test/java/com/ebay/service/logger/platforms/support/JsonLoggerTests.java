package com.ebay.service.logger.platforms.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.service.logger.ServiceLoggerFileNameGenerator;
import com.ebay.service.logger.formats.filters.RequestFilenameFilter;
import com.ebay.service.logger.formats.filters.ResponseFilenameFilter;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class JsonLoggerTests {

  private static final String requestPayloadContents = "{\"size\":\"large\"}";
  private static final String responsePayloadContents = "{  \"paid\": true}";
  private static final String serviceWrapperApiName = "Foo";
  private static final String site = "US";

  String outputFolderPath = System.getProperty("user.dir") + File.separator + "testOutputFolderDeleteMe";
  NSTHttpRequest request = Mockito.mock(NSTHttpRequest.class);
  NSTHttpResponse response = Mockito.mock(NSTHttpResponse.class);
  
  private JsonLogger logger;

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() throws IllegalStateException, IOException, JSONException {
    cleanup();
    ServiceLoggerFileNameGenerator.getInstance().clear();

    when(request.getPayload()).thenReturn(requestPayloadContents);
    when(response.getPayload()).thenReturn(responsePayloadContents);
    
    logger = new JsonLogger();
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() throws IllegalStateException {
    cleanup();
  }

  // ------------------ Tests -------------------------

  @Test(groups = "unitTest")
  public void checkPathIsNotDuplicatedOverMultipleCalls() throws IOException {

    String path = logger.writeFile(request, response, serviceWrapperApiName, outputFolderPath, site);
    path = logger.writeFile(request, response, serviceWrapperApiName, outputFolderPath, site);

    assertThat("Path should exist for response case.", path, containsString("JsonLoggerTests_checkPathIsNotDuplicatedOverMultipleCalls_2_FooResponse.json"));
  }

  @Test(groups = "unitTest")
  public void checkForTwoPaths() throws IOException {

	String path = logger.writeFile(request, response, serviceWrapperApiName, outputFolderPath, site);

    assertThat("Path should exist for response case.", path, containsString("JsonLoggerTests_checkForTwoPaths_1_FooResponse.json"));
    assertThat("Path should exist for request case.", path, containsString("JsonLoggerTests_checkForTwoPaths_1_FooRequest.json"));

    String[] paths = path.split("\n");
    assertThat("Should be two paths (one for request and one for response).", paths.length, is(equalTo(2)));
  }

  @Test(groups = "unitTest", expectedExceptions = NullPointerException.class)
  public void nullRequest() throws IOException {

	logger.writeFile(null, response, serviceWrapperApiName, outputFolderPath, site);
  }
  
  @Test(groups = "unitTest", expectedExceptions = NullPointerException.class)
  public void nullResponse() throws IOException {

	logger.writeFile(request, null, serviceWrapperApiName, outputFolderPath, site);
  }
  
  @Test(groups = "unitTest", expectedExceptions = NullPointerException.class)
  public void nullServiceWrapperApiName() throws IOException {

	logger.writeFile(request, response, null, outputFolderPath, site);
  }
  
  @Test(groups = "unitTest", expectedExceptions = NullPointerException.class)
  public void nullOutputFolderPath() throws IOException {

	logger.writeFile(request, response, serviceWrapperApiName, null, site);
  }
  
  @Test(groups = "unitTest", expectedExceptions = NullPointerException.class)
  public void nullSite() throws IOException {

	logger.writeFile(request, response, serviceWrapperApiName, outputFolderPath, null);
  }

  @Test(groups = "unitTest")
  public void validRequestAndValidResponse() throws IOException {

	logger.writeFile(request, response, serviceWrapperApiName, outputFolderPath, site);

    File outputFolder = new File(outputFolderPath);
    File[] requestFiles = outputFolder.listFiles(new RequestFilenameFilter());
    File[] responseFiles = outputFolder.listFiles(new ResponseFilenameFilter());

    assertThat("Request data should have been written. Expecting file count.", requestFiles.length, is(equalTo(1)));
    assertThat("Response data should have been written. Expecting file count.", responseFiles.length, is(equalTo(1)));

    String requestPayload = getFileContents(requestFiles[0]);
    assertThat("Request payload written does not match payload expected.", requestPayload, is(equalTo(requestPayloadContents)));

    String responsePayload = getFileContents(responseFiles[0]);
    assertThat("Response payload written does not match payload expected.", responsePayload, is(equalTo(responsePayloadContents)));
  }

  // ------------------ Private Methods -----------------

  private void cleanup() throws IllegalStateException {

    File outputFolder = new File(outputFolderPath);
    if (!outputFolder.exists()) {
      return;
    }

    File[] files = outputFolder.listFiles();
    for (File file : files) {
      if (!file.delete()) {
        throw new IllegalStateException(String.format("Unable to delete file: %s", file.getAbsoluteFile()));
      }
    }

    if (!outputFolder.delete()) {
      throw new IllegalStateException(String.format("Unable to delete test output folder: %s", outputFolder.getAbsoluteFile()));
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

  // ------------------------ Inner classes -----------------

  public class ResponseInjector implements ResponseLoggerInjector {

    private static final String payload = "{\"foo\":\"bar\"}";

    @Override
    public String processServiceResponse(String rawServiceResponsePayload) {
      return payload;
    }

    public String getExpectedPayload() {
      return payload;
    }

  }
}
