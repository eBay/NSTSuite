package com.ebay.service.logger.har;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import org.json.JSONException;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.formats.filters.HarFilenameFilter;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.logger.platforms.HarLogger;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class HarLoggerTests {

	private static final String REQUEST_PAYLOAD_CONTENTS = "{\"size\":\"large\",\"color\":\"blue\"}";
	private static final String RESPONSE_PAYLOAD_CONTENTS = "{\"paid\":true,\"shipped\":false}";
	private static final String SERVICE_WRAPPER_NAME = "TestWrapper";
	
	private static final String SECOND_REQUEST_PAYLOAD_CONTENTS = "{\"height\":\"tall\",\"age\":16}";
	private static final String SECOND_RESPONSE_PAYLOAD_CONTENTS = "{\"registered\":true,\"canVote\":false}";
	private static final String SECOND_SERVICE_WRAPPER_NAME = "SecondTestWrapper";

	private static final String PLATFORM_KEY = "nstplatform";
	private static final String IOS_MOCKS_LOCATION_KEY = "iosMocksLocation";
	private static final String WHAT_TO_WRITE_KEY = "whatToWrite";

	String outputFolderPath = System.getProperty("user.dir") + File.separator + "testOutputFolderDeleteMe";
	NSTHttpRequest request = Mockito.mock(NSTHttpRequest.class);
	NSTHttpResponse response = Mockito.mock(NSTHttpResponse.class);
	
	NSTHttpRequest secondRequest = Mockito.mock(NSTHttpRequest.class);
	NSTHttpResponse secondResponse = Mockito.mock(NSTHttpResponse.class);

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() throws IllegalStateException, IOException, JSONException {
		cleanup();

		// First
		when(request.getPayload()).thenReturn(REQUEST_PAYLOAD_CONTENTS);
		when(request.getRequestType()).thenReturn(NstRequestType.POST);
		when(request.getUrl()).thenReturn(new URL("http://www.ebay.com"));

		when(response.getPayload()).thenReturn(RESPONSE_PAYLOAD_CONTENTS);
		
		// Second
		when(secondRequest.getPayload()).thenReturn(SECOND_REQUEST_PAYLOAD_CONTENTS);
		when(secondRequest.getRequestType()).thenReturn(NstRequestType.PUT);
		when(secondRequest.getUrl()).thenReturn(new URL("http://www.other.com"));

		when(secondResponse.getPayload()).thenReturn(SECOND_RESPONSE_PAYLOAD_CONTENTS);

		clearSystemProperties();

		System.setProperty(PLATFORM_KEY, "IOS");
		System.setProperty(IOS_MOCKS_LOCATION_KEY, outputFolderPath);
		System.setProperty(WHAT_TO_WRITE_KEY, "MOCKS");
		RuntimeConfigManager.getInstance().reinitialize();
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws IllegalStateException {
		cleanup();
		clearSystemProperties();
	}

	private void clearSystemProperties() {
		System.clearProperty(PLATFORM_KEY);
		System.clearProperty(IOS_MOCKS_LOCATION_KEY);
		System.clearProperty(WHAT_TO_WRITE_KEY);
		RuntimeConfigManager.getInstance().reinitialize();
	}

	// --------------------------- Tests ---------------------------

	@Test(groups = "unitTest")
	public void singleCall() throws IOException {

		File expectedOutputFile = new File(outputFolderPath,
				String.format("HarLoggerTests_singleCall_0_%s.har", SERVICE_WRAPPER_NAME));
		assertThat(expectedOutputFile, is(not(anExistingFile())));

		ServiceCallCacheData cacheData = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);

		HarLogger logger = new HarLogger();
		logger.writeMocks(Arrays.asList(cacheData), "HarLoggerTests", "singleCall");

		assertThat(expectedOutputFile, is(anExistingFile()));

		String fileContents = getFileContents(expectedOutputFile);

		String expectedFileContents = "{\"log\":{\"version\":\"1.2\",\"creator\":{\"name\":\"NST HAR logger\"},\"entries\":[{\"request\":{\"method\":\"POST\",\"url\":\"http://www.ebay.com\",\"headers\":[],\"queryString\":[],\"postData\":{\"text\":\"{\\\"size\\\":\\\"large\\\",\\\"color\\\":\\\"blue\\\"}\"}},\"response\":{\"status\":0,\"headers\":[],\"content\":{\"text\":\"{\\\"paid\\\":true,\\\"shipped\\\":false}\"}}}]}}";
		assertThat(fileContents, is(equalTo(expectedFileContents)));
	}

	@Test(groups = "unitTest")
	public void multipleCalls() throws IOException {

		File firstExpectedOutputFile = new File(outputFolderPath,
				String.format("HarLoggerTests_multipleCalls_0_%s.har", SERVICE_WRAPPER_NAME));
		assertThat(firstExpectedOutputFile, is(not(anExistingFile())));
		
		File secondExpectedOutputFile = new File(outputFolderPath,
				String.format("HarLoggerTests_multipleCalls_1_%s.har", SECOND_SERVICE_WRAPPER_NAME));
		assertThat(secondExpectedOutputFile, is(not(anExistingFile())));
		
		// ----------
		// Assemble call data
		
		ServiceCallCacheData firstCacheData = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheData secondCacheData = new ServiceCallCacheData(secondRequest, secondResponse, SECOND_SERVICE_WRAPPER_NAME);

		HarLogger logger = new HarLogger();
		logger.writeMocks(Arrays.asList(firstCacheData, secondCacheData), "HarLoggerTests", "multipleCalls");

		File outputFolder = new File(outputFolderPath);
		File[] harFiles = outputFolder.listFiles(new HarFilenameFilter());

		assertThat("Har files should have been written. ", harFiles.length, is(equalTo(2)));

		String firstFileContents = getFileContents(firstExpectedOutputFile);

		String firstExpectedFileContents = "{\"log\":{\"version\":\"1.2\",\"creator\":{\"name\":\"NST HAR logger\"},\"entries\":[{\"request\":{\"method\":\"POST\",\"url\":\"http://www.ebay.com\",\"headers\":[],\"queryString\":[],\"postData\":{\"text\":\"{\\\"size\\\":\\\"large\\\",\\\"color\\\":\\\"blue\\\"}\"}},\"response\":{\"status\":0,\"headers\":[],\"content\":{\"text\":\"{\\\"paid\\\":true,\\\"shipped\\\":false}\"}}}]}}";
		assertThat(firstFileContents, is(equalTo(firstExpectedFileContents)));
		
		String secondFileContents = getFileContents(secondExpectedOutputFile);

		String secondExpectedFileContents = "{\"log\":{\"version\":\"1.2\",\"creator\":{\"name\":\"NST HAR logger\"},\"entries\":[{\"request\":{\"method\":\"PUT\",\"url\":\"http://www.other.com\",\"headers\":[],\"queryString\":[],\"postData\":{\"text\":\"{\\\"height\\\":\\\"tall\\\",\\\"age\\\":16}\"}},\"response\":{\"status\":0,\"headers\":[],\"content\":{\"text\":\"{\\\"registered\\\":true,\\\"canVote\\\":false}\"}}}]}}";
		assertThat(secondFileContents, is(equalTo(secondExpectedFileContents)));
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
			throw new IllegalStateException(
					String.format("Unable to delete test output folder: %s", outputFolder.getAbsoluteFile()));
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
			return "\"response\":{\"status\":0,\"headers\":[],\"content\":{\"text\":\"{\\\"foo\\\":\\\"bar\\\"}\"}}}]}}";
		}

	}
}
