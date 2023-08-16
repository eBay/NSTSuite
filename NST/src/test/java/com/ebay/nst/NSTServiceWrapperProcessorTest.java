package com.ebay.nst;

import com.ebay.nst.schema.validation.NSTSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.call.cache.ServiceCallCacheManager;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpClient;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.ebay.service.protocol.http.NSTHttpResponse;
import com.ebay.service.protocol.http.NSTHttpResponseImpl;

import org.apache.http.HttpException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.fail;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NSTServiceWrapperProcessorTest {

	private static final String OUTPUT_FOLDER = "testOutputFolderDeleteMe";

	private static final String SERVICE_VERSION = "serviceVersion";
	private static final String SCHEMA_VALIDATION = "schemavalidation";
	private static final String SITE = "site";
	private static final String NST_PLATFORM = "nstplatform";
	private static final String CLIENT_VERSION = "clientVersion";
	private static final String DISABLE_LOG_TO_CONSOLE = "disableLogToConsole";
	private static final String WHAT_TO_WRITE = "whatToWrite";
	private static final String ANDROID_MOCKS_LOCATION = "androidMocksLocation";
	private static final String ANDROID_TESTS_LOCATION = "androidTestsLocation";
	private static final String IOS_MOCKS_LOCAITON = "iosMocksLocation";
	private static final String IOS_TESTS_LOCATION = "iosTestsLocation";
	private NSTServiceWrapperProcessor processor;
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeMethod(alwaysRun = true)
	@AfterMethod(alwaysRun = true)
	public void resetBeforeAndAfterEachTest() {
		processor = null;

		System.clearProperty(SERVICE_VERSION);
		System.clearProperty(SCHEMA_VALIDATION);
		System.clearProperty(SITE);
		System.clearProperty(NST_PLATFORM);
		System.clearProperty(CLIENT_VERSION);
		System.clearProperty(DISABLE_LOG_TO_CONSOLE);
		System.clearProperty(WHAT_TO_WRITE);
		System.clearProperty(ANDROID_MOCKS_LOCATION);
		System.clearProperty(ANDROID_TESTS_LOCATION);
		System.clearProperty(IOS_MOCKS_LOCAITON);
		System.clearProperty(IOS_TESTS_LOCATION);
		
		ServiceCallCacheManager.getInstance().clearCache();
	}

	@AfterMethod(alwaysRun = true)
	public void resetConsoleLoggingToStdOut() {
		System.setOut(standardOut);
	}

	@AfterClass(alwaysRun = true)
	public void cleanupTestFiles() throws IllegalStateException {
		cleanup(OUTPUT_FOLDER);
	}

	class TestServiceWrapper implements NSTServiceWrapper<NSTSchemaValidator> {

		private String uniqueServiceWrapperName;

		TestServiceWrapper(String uniqueServiceWrapperName) {
			this.uniqueServiceWrapperName = uniqueServiceWrapperName;
		}
		
		@Override
		public String getServiceName() {
			return null;
		}

		@Override
		public NstRequestType getRequestType() {
			return null;
		}
		
		@Override
		public String getUniqueServiceWrapperName() {
			return uniqueServiceWrapperName;
		}

		@Override
		public String getEndpointPath() {
			return null;
		}
		
		@Override
		public NSTHttpRequest prepareRequest() {
			return null;
		}

		@Override
		public NSTSchemaValidator getSchemaValidator() {
			return null;
		}

	}

	// Defined class with this name so alignment with one of the already defined
	// service wrapper mappings will match for writing mocks, etc.
	class GetAddressFields implements NSTServiceWrapper<NSTSchemaValidator> {

		@Override
		public String getServiceName() {
			return null;
		}

		@Override
		public NstRequestType getRequestType() {
			return null;
		}

		@Override
		public String getEndpointPath() {
			return null;
		}

		@Override
		public NSTHttpRequest prepareRequest() {
			return null;
		}

		@Override
		public NSTSchemaValidator getSchemaValidator() {
			return null;
		}

	}

	@Test(groups = "unitTest")
	public void schemaValidationOnByDefault() {
		processor = new NSTServiceWrapperProcessor();
		assertThat(processor.isSchemaValidationDisabled(), is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void disableSchemaValidation() {
		processor = new NSTServiceWrapperProcessor();
		processor.disableSchemaValidation();
		assertThat(processor.isSchemaValidationDisabled(), is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void enableSchemaValidation() {
		processor = new NSTServiceWrapperProcessor();
		processor.disableSchemaValidation();
		processor.resetDisableSchemaValidation();
		assertThat(processor.isSchemaValidationDisabled(), is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void requestResponseLoggingOnByDefault() {
		processor = new NSTServiceWrapperProcessor();
		assertThat(processor.isRequestResponseLoggingDisabled(), is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void disableRequestResposneLogging() {
		processor = new NSTServiceWrapperProcessor();
		processor.disableRequestResponseLogging();
		assertThat(processor.isRequestResponseLoggingDisabled(), is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void enableRequestResponseLogging() {
		processor = new NSTServiceWrapperProcessor();
		processor.disableRequestResponseLogging();
		processor.resetDisableRequestResponseLogging();
		assertThat(processor.isRequestResponseLoggingDisabled(), is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void confirmSuccessOnByDefault() {
		processor = new NSTServiceWrapperProcessor();
		assertThat(processor.isConfirmSuccessDisabled(), is(equalTo(false)));
	}

	@Test(groups = "unitTest")
	public void disableConfirmSuccess() {
		processor = new NSTServiceWrapperProcessor();
		processor.disableConfirmSuccess();
		assertThat(processor.isConfirmSuccessDisabled(), is(equalTo(true)));
	}

	@Test(groups = "unitTest")
	public void enableConfirmSuccess() {
		processor = new NSTServiceWrapperProcessor();
		processor.disableConfirmSuccess();
		processor.resetConfirmSuccess();
		assertThat(processor.isConfirmSuccessDisabled(), is(equalTo(false)));
	}
	
	@Test(groups = "unitTest")
	public void testSendRequestAndGetJsonResponse() throws Exception {

		System.setProperty(DISABLE_LOG_TO_CONSOLE, "SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();

		String jsonString = "{\"nodeName\":\"pmtinssvcclientid\",\"createTime\":\"2022-02-13T05:48:35.002Z\",\"createdBy\":\"samsvc\",\"nodePath\":\"/xoqesvc2/pmtinssvcclientid\",\"nodeType\":\"object\",\"version\":0,\"value\":\"99999.65f\"}";

		NSTHttpRequest request = mock(NSTHttpRequest.class);
		when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));
		when(request.getRequestType()).thenReturn(NstRequestType.POST);

		NSTServiceWrapper<NSTSchemaValidator> nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getServiceName()).thenReturn("checkout");
		when(nstServiceWrapper.getEndpointPath()).thenReturn("/foo");
		when(nstServiceWrapper.getRequestType()).thenReturn(NstRequestType.valueOf("POST"));
		when(nstServiceWrapper.prepareRequest()).thenReturn(request);
		when(nstServiceWrapper.getServiceDetails()).thenReturn(null);

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(jsonString);
		when(response.getResponseCode()).thenReturn(200);
		
		NSTHttpClient<NSTHttpRequest, NSTHttpResponse> client = mock(NSTHttpClient.class);
		when(client.sendRequest(Mockito.any(NSTHttpRequest.class), Mockito.any(Charset.class))).thenReturn(response);

		processor = new NSTServiceWrapperProcessor(client);
		JSONObject actualJsonObject = processor.sendRequestAndGetJSONResponse(nstServiceWrapper);
		assertThat(actualJsonObject.toString(), is(equalTo(jsonString)));
	}
	
	@Test
	public void logCallDetailsSkippedIfLoggingIsDisabledOnProcessor() throws Exception {
		
		String serviceWrapperName = "TestWrapper";
		
		String outputFolderPath = System.getProperty("user.dir") + File.separator + OUTPUT_FOLDER;
		System.setProperty(WHAT_TO_WRITE, "MOCKS");
		System.setProperty(NST_PLATFORM, "IOS");
		System.setProperty(IOS_MOCKS_LOCAITON, outputFolderPath);
		System.setProperty(DISABLE_LOG_TO_CONSOLE, "SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();
		
		NSTHttpRequest request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		NSTHttpResponse response = new NSTHttpResponseImpl();

		NSTServiceWrapper<NSTSchemaValidator> nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getResponseLoggerInjector()).thenReturn(null);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn(serviceWrapperName);
		
		processor = new NSTServiceWrapperProcessor();
		processor.disableRequestResponseLogging();
		processor.logCallDetails(nstServiceWrapper, request, response);
		
		Map<String, List<ServiceCallCacheData>> cacheData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Map size MUST be 0", cacheData.size(), is(equalTo(0)));

		cleanup(outputFolderPath);
	}
	
	@Test
	public void logCallDetailsSkippedIfWhatToWriteContainsNone() throws Exception {
		
		String serviceWrapperName = "TestWrapper";
		
		String outputFolderPath = System.getProperty("user.dir") + File.separator + OUTPUT_FOLDER;
		System.setProperty(WHAT_TO_WRITE, "MOCKS|TESTS|NONE");
		System.setProperty(NST_PLATFORM, "IOS");
		System.setProperty(IOS_MOCKS_LOCAITON, outputFolderPath);
		System.setProperty(DISABLE_LOG_TO_CONSOLE, "SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();
		
		NSTHttpRequest request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		NSTHttpResponse response = new NSTHttpResponseImpl();

		NSTServiceWrapper<NSTSchemaValidator> nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getResponseLoggerInjector()).thenReturn(null);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn(serviceWrapperName);
		
		processor = new NSTServiceWrapperProcessor();
		processor.logCallDetails(nstServiceWrapper, request, response);
		
		Map<String, List<ServiceCallCacheData>> cacheData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Map size MUST be 0", cacheData.size(), is(equalTo(0)));

		cleanup(outputFolderPath);
	}
	
	@Test
	public void logCallDetailsSkippedIfAlwaysDisableRequestResponseLoggingIsTrueOnServiceWrapper() throws Exception {
		
		String serviceWrapperName = "TestWrapper";
		
		String outputFolderPath = System.getProperty("user.dir") + File.separator + OUTPUT_FOLDER;
		System.setProperty(WHAT_TO_WRITE, "MOCKS");
		System.setProperty(NST_PLATFORM, "IOS");
		System.setProperty(IOS_MOCKS_LOCAITON, outputFolderPath);
		System.setProperty(DISABLE_LOG_TO_CONSOLE, "SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();
		
		NSTHttpRequest request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		NSTHttpResponse response = new NSTHttpResponseImpl();

		NSTServiceWrapper<NSTSchemaValidator> nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getResponseLoggerInjector()).thenReturn(null);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn(serviceWrapperName);
		when(nstServiceWrapper.alwaysDisableRequestResponseLogging()).thenReturn(true);
		
		processor = new NSTServiceWrapperProcessor();
		processor.logCallDetails(nstServiceWrapper, request, response);
		
		Map<String, List<ServiceCallCacheData>> cacheData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Map size MUST be 0", cacheData.size(), is(equalTo(0)));

		cleanup(outputFolderPath);
	}
	
	@Test
	public void logCallDetails() throws Exception {
		
		String key = "NSTServiceWrapperProcessorTest_logCallDetails";
		String serviceWrapperName = "TestWrapper";
		
		String outputFolderPath = System.getProperty("user.dir") + File.separator + OUTPUT_FOLDER;
		System.setProperty(WHAT_TO_WRITE, "MOCKS");
		System.setProperty(NST_PLATFORM, "IOS");
		System.setProperty(IOS_MOCKS_LOCAITON, outputFolderPath);
		System.setProperty(DISABLE_LOG_TO_CONSOLE, "SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();
		
		NSTHttpRequest request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		NSTHttpResponse response = new NSTHttpResponseImpl();

		NSTServiceWrapper<NSTSchemaValidator> nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getResponseLoggerInjector()).thenReturn(null);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn(serviceWrapperName);
		
		processor = new NSTServiceWrapperProcessor();
		processor.logCallDetails(nstServiceWrapper, request, response);
		
		Map<String, List<ServiceCallCacheData>> cacheData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Map size MUST be 1", cacheData.size(), is(equalTo(1)));
		MatcherAssert.assertThat(cacheData.keySet(), contains(key));
		
		List<ServiceCallCacheData> callData = cacheData.get(key);
		assertThat("Call data MUST contain ONLY one call.", callData.size(), is(equalTo(1)));
		
		ServiceCallCacheData data = callData.get(0);
		assertThat("Requests MUST match.", data.getRequest(), is(equalTo(request)));
		assertThat("Response MUST match.", data.getResponse(), is(equalTo(response)));
		assertThat(data.getServiceCallName(), is(equalTo(serviceWrapperName)));

		cleanup(outputFolderPath);
	}
	
	@Test
	public void logCallDetailsWithResponseInjection() throws Exception {
		
		String key = "NSTServiceWrapperProcessorTest_logCallDetailsWithResponseInjection";
		String serviceWrapperName = "TestWrapper";
		
		String jsonString = "{\"nodeName\":\"pmtinssvcclientid\",\"nodeType\":\"object\",\"nodePath\":"
				+ "\"/xoqesvc2/pmtinssvcclientid\",\"version\":0,\"createTime\":\"2022-02-13T05:48:35.002Z\","
				+ "\"createdBy\":\"samsvc\",\"value\":\"99999.65f\"}";
		
		String injectValue = "INJECT_VALUE";
		
		String outputFolderPath = System.getProperty("user.dir") + File.separator + OUTPUT_FOLDER;
		System.setProperty(WHAT_TO_WRITE, "MOCKS");
		System.setProperty(NST_PLATFORM, "IOS");
		System.setProperty(IOS_MOCKS_LOCAITON, outputFolderPath);
		System.setProperty(DISABLE_LOG_TO_CONSOLE, "SERVICE_CONFIG");
		RuntimeConfigManager.getInstance().reinitialize();
		
		NSTHttpRequest request = new NSTHttpRequestImpl.Builder(new URL("http://www.ebay.com"), NstRequestType.GET).build();
		NSTHttpResponseImpl response = new NSTHttpResponseImpl();
		response.setPayload(jsonString);
		
		ResponseLoggerInjector injector = mock(ResponseLoggerInjector.class);
		when(injector.processServiceResponse(Mockito.anyString())).thenReturn(injectValue);

		NSTServiceWrapper<NSTSchemaValidator> nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getResponseLoggerInjector()).thenReturn(null);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn(serviceWrapperName);
		when(nstServiceWrapper.getResponseLoggerInjector()).thenReturn(injector);
		
		processor = new NSTServiceWrapperProcessor();
		processor.logCallDetails(nstServiceWrapper, request, response);
		
		Map<String, List<ServiceCallCacheData>> cacheData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Map size MUST be 1", cacheData.size(), is(equalTo(1)));
		assertThat(cacheData.keySet(), contains(key));
		
		List<ServiceCallCacheData> callData = cacheData.get(key);
		assertThat("Call data MUST contain ONLY one call.", callData.size(), is(equalTo(1)));
		
		ServiceCallCacheData data = callData.get(0);
		assertThat("Requests MUST match.", data.getRequest(), is(equalTo(request)));
		assertThat("Response MUST NOT match - copy is made and copy's payload MUST be different.", data.getResponse(), is(not(equalTo(response))));
		assertThat("Original response MUST NOT have the payload changed.", response.getPayload(), is(equalTo(jsonString)));
		assertThat("Call data response MUST have updated payload.", data.getResponse().getPayload(), is(equalTo(injectValue)));
		assertThat(data.getServiceCallName(), is(equalTo(serviceWrapperName)));

		cleanup(outputFolderPath);
	}
	
	@Test(groups = "unitTest")
	public void testConfirmSuccessDoesNotThrowHttpExceptionIfConfirmSuccessDisabled()
			throws HttpException, IOException, IllegalStateException {

		NSTHttpRequest request = mock(NSTHttpRequest.class);
		NSTHttpResponse response = mock(NSTHttpResponse.class);

		when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));
		when(response.getResponseCode()).thenReturn(500);
		when(response.getPayload()).thenReturn("");

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		processor = new NSTServiceWrapperProcessor();
		processor.disableConfirmSuccess();
		processor.confirmSuccess(nstServiceWrapper, request, response);
	}

	@Test(groups = "unitTest")
	public void testConfirmSuccessIsSuccessfulWith200() {
		try {
			NSTHttpRequest request = mock(NSTHttpRequest.class);
			NSTHttpResponse response = mock(NSTHttpResponse.class);

			when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));
			when(response.getResponseCode()).thenReturn(200);

			NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
			processor = new NSTServiceWrapperProcessor();
			processor.confirmSuccess(nstServiceWrapper, request, response);
		} catch (Exception e) {
			fail("Should not have thrown HttpException exception");
		}
	}
	
	@Test(groups = "unitTest")
	public void testConfirmSuccessIsSuccessfulWith299() {
		try {
			NSTHttpRequest request = mock(NSTHttpRequest.class);
			NSTHttpResponse response = mock(NSTHttpResponse.class);

			when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));
			when(response.getResponseCode()).thenReturn(299);

			NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
			processor = new NSTServiceWrapperProcessor();
			processor.confirmSuccess(nstServiceWrapper, request, response);
		} catch (Exception e) {
			fail("Should not have thrown HttpException exception");
		}
	}
	
	@Test(groups = "unitTest", expectedExceptions = IllegalStateException.class)
	public void testConfirmSuccessIsFailureWith199() throws Exception {
		NSTHttpRequest request = mock(NSTHttpRequest.class);
		NSTHttpResponse response = mock(NSTHttpResponse.class);

		when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));
		when(response.getResponseCode()).thenReturn(199);

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn("TestServiceWrapper");
		
		processor = new NSTServiceWrapperProcessor();
		processor.confirmSuccess(nstServiceWrapper, request, response);
	}
	
	@Test(groups = "unitTest", expectedExceptions = IllegalStateException.class)
	public void testConfirmSuccessIsFailureWith300() throws Exception {
		NSTHttpRequest request = mock(NSTHttpRequest.class);
		NSTHttpResponse response = mock(NSTHttpResponse.class);

		when(request.getUrl()).thenReturn(new URL("http://test.com/random/endpoint?param1=abc&param2=1"));
		when(response.getResponseCode()).thenReturn(300);

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getUniqueServiceWrapperName()).thenReturn("TestServiceWrapper");
		
		processor = new NSTServiceWrapperProcessor();
		processor.confirmSuccess(nstServiceWrapper, request, response);
	}
	
	@Test(groups = "unitTest")
	public void schemaValidationOffAtServiceWrapperByReturningNullSchemaValidator() {
		
		System.setProperty(SCHEMA_VALIDATION, String.valueOf(true));
		RuntimeConfigManager.getInstance().reinitialize();
		
		String payload = "{ \"test\": \"val\" }";

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getSchemaValidator()).thenReturn(null);

		processor = new NSTServiceWrapperProcessor();
		processor.schemaValidation(nstServiceWrapper, response);
		verify(response, times(0)).getPayload();
	}
	
	@Test(groups = "unitTest")
	public void schemaValidationOffAtServiceWrapperViaInterface() {
		
		System.setProperty(SCHEMA_VALIDATION, String.valueOf(true));
		RuntimeConfigManager.getInstance().reinitialize();

		String payload = "{ \"test\": \"val\" }";

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getSchemaValidator()).thenReturn(new OpenApiSchemaValidator.Builder("resource_path", "api_path", NstRequestType.GET).build());
		when(nstServiceWrapper.alwaysDisableSchemaValidation()).thenReturn(true);

		processor = new NSTServiceWrapperProcessor();
		processor.schemaValidation(nstServiceWrapper, response);
		verify(response, times(0)).getPayload();
	}

	@Test(groups = "unitTest")
	public void schemaValdiationTurnedOffGlobally() {

		System.setProperty(SCHEMA_VALIDATION, String.valueOf(false));
		RuntimeConfigManager.getInstance().reinitialize();

		String payload = "{ \"test\": \"val\" }";

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		NSTSchemaValidator validator = mock(NSTSchemaValidator.class);
		doNothing().when(validator).validate(Mockito.anyString());

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getSchemaValidator()).thenReturn(validator);

		processor = new NSTServiceWrapperProcessor();
		processor.schemaValidation(nstServiceWrapper, response);
		verify(response, times(0)).getPayload();
	}

	@Test(groups = "unitTest")
	public void schemaValidationOffAtProcessor() {

		System.setProperty(SCHEMA_VALIDATION, String.valueOf(true));
		RuntimeConfigManager.getInstance().reinitialize();

		String payload = "{ \"test\": \"val\" }";

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		NSTSchemaValidator validator = mock(NSTSchemaValidator.class);
		doNothing().when(validator).validate(Mockito.anyString());

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getSchemaValidator()).thenReturn(validator);

		processor = new NSTServiceWrapperProcessor();
		processor.disableSchemaValidation();
		processor.schemaValidation(nstServiceWrapper, response);
		verify(validator, times(0)).validate(Mockito.anyString());
	}

	@Test(groups = "unitTest")
	public void schemaValidationOn() {

		String payload = "{ \"test\": \"val\" }";

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		NSTSchemaValidator validator = mock(NSTSchemaValidator.class);
		doNothing().when(validator).validate(Mockito.anyString());

		NSTServiceWrapper nstServiceWrapper = mock(NSTServiceWrapper.class);
		when(nstServiceWrapper.getSchemaValidator()).thenReturn(validator);

		processor = new NSTServiceWrapperProcessor();
		processor.schemaValidation(nstServiceWrapper, response);
		verify(validator, times(1)).validate(Mockito.anyString());
	}
	
	@Test(groups = "unitTest")
	public void getServiceWrapperName() {

		TestServiceWrapper nstServiceWrapper = new TestServiceWrapper(null);
		processor = new NSTServiceWrapperProcessor();
		String forcedServiceFileName = processor.getServiceWrapperName(nstServiceWrapper);
		assertThat(forcedServiceFileName, is(equalTo("TestServiceWrapper")));
	}
	
	@Test(groups = "unitTest")
	public void getServiceWrapperNameWithUniqueWrapperName() {
		String uniqueServiceWrapperName = "uniqueServiceWrapperName";
		TestServiceWrapper nstServiceWrapper = new TestServiceWrapper(uniqueServiceWrapperName);
		processor = new NSTServiceWrapperProcessor();
		String forcedServiceFileName = processor.getServiceWrapperName(nstServiceWrapper);
		assertThat(forcedServiceFileName, is(equalTo(uniqueServiceWrapperName)));
	}
	
	@Test
	public void sendRequest() throws Exception {
		
		NSTHttpClient<NSTHttpRequest, NSTHttpResponse> client = mock(NSTHttpClient.class);
		when(client.sendRequest(Mockito.any(NSTHttpRequest.class), Mockito.any(Charset.class))).thenReturn(new NSTHttpResponseImpl());
		
		NSTHttpRequest request = mock(NSTHttpRequest.class);
		
		processor = new NSTServiceWrapperProcessor(client);
		processor.sendRequest(request);
		
		verify(client, times(1)).sendRequest(Mockito.any(NSTHttpRequest.class), eq(StandardCharsets.UTF_8));
	}

	@Test
	public void sendRequestWithAlternateCharacterSetUsedToParseResponse() throws Exception {

		NSTHttpClient<NSTHttpRequest, NSTHttpResponse> client = mock(NSTHttpClient.class);
		when(client.sendRequest(Mockito.any(NSTHttpRequest.class), Mockito.any(Charset.class))).thenReturn(new NSTHttpResponseImpl());

		NSTHttpRequest request = mock(NSTHttpRequest.class);

		processor = new NSTServiceWrapperProcessor(client);
		processor.setResponseParsingCharset(StandardCharsets.ISO_8859_1);
		processor.sendRequest(request);

		verify(client, times(1)).sendRequest(Mockito.any(NSTHttpRequest.class), eq(StandardCharsets.ISO_8859_1));
	}

	@Test
	public void logResponseDetailsToConsoleWhenLoggingEnabled() {
		System.setOut(new PrintStream(outputStreamCaptor));

		String payload = "{ \"Foo\": \"bar\" }";

		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn(null);

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, response);
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString(payload));
	}

	@Test
	public void doNotLogResponseDetailsToConsoleWhenLoggingDisabled() {
		System.setOut(new PrintStream(outputStreamCaptor));
		System.setProperty(DISABLE_LOG_TO_CONSOLE, "RESPONSE_PAYLOAD");
		RuntimeConfigManager.getInstance().reinitialize();

		String payload = "{ \"Foo\": \"bar\" }";

		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn(null);

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);

		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, response);
		assertThat("Console contains unexpected output.", outputStreamCaptor.toString().trim(), not(containsString(payload)));
	}

	@Test
	public void logResponseDetailsToConsoleWithNullResponse() {
		System.setOut(new PrintStream(outputStreamCaptor));
		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn(null);
		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, null);
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Response was null - skipping logging response details."));
	}

	@Test
	public void logResponseDetailsToConsoleWithNullResponseHeaders() {
		System.setOut(new PrintStream(outputStreamCaptor));
		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn(null);

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getHeaders()).thenReturn(null);

		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, response);
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Response does not contain headers."));
	}

	@Test
	public void logResponseDetailsToConsoleWithNullResponsePayload() {
		System.setOut(new PrintStream(outputStreamCaptor));
		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn(null);

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getHeaders()).thenReturn(null);
		when(response.getPayload()).thenReturn(null);

		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, response);
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Response does not contain a payload."));
	}

	@Test
	public void logResponseDetailsToConsoleWithResponsePayloadAndHeaders() {
		System.setOut(new PrintStream(outputStreamCaptor));
		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn(null);

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application-json");
		headers.put("Accept", "application-json");

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getHeaders()).thenReturn(headers);
		when(response.getPayload()).thenReturn("{ \"Foo\" : \"Bar\" }");

		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, response);
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Response Headers:"));
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Content-Type : application-json"));
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Accept : application-json"));
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Response Payload:"));
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("{ \"Foo\" : \"Bar\" }"));
	}

	@Test
	public void logResponseDetailsToConsoleWithCustomServiceWrapperLog() {
		System.setOut(new PrintStream(outputStreamCaptor));
		NSTServiceWrapper serviceWrapper = mock(NSTServiceWrapper.class);
		when(serviceWrapper.getServiceWrapperConsoleOutput(Mockito.any(NSTHttpResponse.class))).thenReturn("Service Wrapper Console Output");

		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getHeaders()).thenReturn(null);
		when(response.getPayload()).thenReturn(null);

		processor = new NSTServiceWrapperProcessor();
		processor.logResponseDetailsToConsole(serviceWrapper, response);
		assertThat("Console does not contain expected output.", outputStreamCaptor.toString().trim(), containsString("Service Wrapper Console Output"));
	}

	// ----------------------------------
	// Private methods
	// ----------------------------------

	private void cleanup(String outputFolderPath) throws IllegalStateException {
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
		
		String cwd = System.getProperty("user.dir");
		File currentDirectory = new File(cwd);
		
		files = currentDirectory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("_ERROR.txt");
			}
		});

		for (File file : files) {
		    file.delete();
		}
	}
}
