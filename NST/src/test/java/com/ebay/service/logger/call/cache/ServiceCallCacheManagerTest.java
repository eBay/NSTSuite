package com.ebay.service.logger.call.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.runtime.event.ObserverPayload;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.formats.loader.CustomLoggerFormatManager;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class ServiceCallCacheManagerTest {
	
	private static final String WHAT_TO_WRITE = "whatToWrite";
	
	private static final String SERVICE_WRAPPER_NAME = "TestWrapper";
	private static final String SECOND_SERVICE_WRAPPER_NAME = "SecondWrapper";
	
	private NSTHttpRequest request;
	private NSTHttpResponse response;
	
	private NSTHttpRequest secondRequest;
	private NSTHttpResponse secondResponse;
	
	@BeforeMethod(alwaysRun = true)
	private void beforeEachMethod() {
		ServiceCallCacheManager.getInstance().clearCache();
		request = mock(NSTHttpRequest.class);
		response = mock(NSTHttpResponse.class);
		
		secondRequest = mock(NSTHttpRequest.class);
		secondResponse = mock(NSTHttpResponse.class);
		
		System.clearProperty(WHAT_TO_WRITE);
		RuntimeConfigManager.getInstance().reinitialize();
	}
	
	@AfterMethod(alwaysRun = true)
	private void afterEachMethod() {
		ServiceCallCacheManager.getInstance().clearCache();
		ServiceCallCacheManager.getInstance().resetCustomLoggerFormatManager();
		
		System.clearProperty(WHAT_TO_WRITE);
		RuntimeConfigManager.getInstance().reinitialize();
	}

	@Test
	public void addCallToCacheAndGetBackCall() throws Exception {
		
		String expectedKey = "ServiceCallCacheManagerTest_addCallToCacheAndGetBackCall";
		
		ServiceCallCacheData data = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(data);
		
		Map<String, List<ServiceCallCacheData>> cachedData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Cache MUST contain only one key.", cachedData.size(), is(equalTo(1)));
		
		String[] keys = cachedData.keySet().toArray(new String[0]);
		assertThat(keys[0], is(equalTo(expectedKey)));
		
		List<ServiceCallCacheData> actualData = cachedData.get(expectedKey);
		assertThat("Call data must match expected size.", actualData.size(), is(equalTo(1)));
		assertThat(actualData.get(0).getRequest(), is(sameInstance(request)));
		assertThat(actualData.get(0).getResponse(), is(sameInstance(response)));
		assertThat(actualData.get(0).getServiceCallName(), is(equalTo(SERVICE_WRAPPER_NAME)));
	}
	
	@Test
	public void addMultipleCallsToCacheAndGetBackAll() throws Exception {
		
		String expectedKey = "ServiceCallCacheManagerTest_addMultipleCallsToCacheAndGetBackAll";
		
		ServiceCallCacheData first = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheData second = new ServiceCallCacheData(secondRequest, secondResponse, SECOND_SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(first);
		ServiceCallCacheManager.getInstance().addCallToCache(second);
		
		Map<String, List<ServiceCallCacheData>> cachedData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Cache MUST contain only one key.", cachedData.size(), is(equalTo(1)));
		
		String[] keys = cachedData.keySet().toArray(new String[0]);
		assertThat(keys[0], is(equalTo(expectedKey)));
		
		List<ServiceCallCacheData> actualData = cachedData.get(expectedKey);
		assertThat("Call data must match expected size.", actualData.size(), is(equalTo(2)));
		assertThat(actualData.get(0).getRequest(), is(sameInstance(request)));
		assertThat(actualData.get(0).getResponse(), is(sameInstance(response)));
		assertThat(actualData.get(0).getServiceCallName(), is(equalTo(SERVICE_WRAPPER_NAME)));
		assertThat(actualData.get(1).getRequest(), is(sameInstance(secondRequest)));
		assertThat(actualData.get(1).getResponse(), is(sameInstance(secondResponse)));
		assertThat(actualData.get(1).getServiceCallName(), is(equalTo(SECOND_SERVICE_WRAPPER_NAME)));
	}
	
	@Test
	public void clearCache() throws Exception {
		
		String expectedKey = "ServiceCallCacheManagerTest_clearCache";
		
		ServiceCallCacheData first = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(first);
		
		ServiceCallCacheManager.getInstance().clearCache();
		
		ServiceCallCacheData second = new ServiceCallCacheData(secondRequest, secondResponse, SECOND_SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(second);
		
		Map<String, List<ServiceCallCacheData>> cachedData = ServiceCallCacheManager.getInstance().getCacheData();
		assertThat("Cache MUST contain only one key.", cachedData.size(), is(equalTo(1)));
		
		String[] keys = cachedData.keySet().toArray(new String[0]);
		assertThat(keys[0], is(equalTo(expectedKey)));
		
		List<ServiceCallCacheData> actualData = cachedData.get(expectedKey);
		assertThat("Call data must match expected size.", actualData.size(), is(equalTo(1)));
		assertThat(actualData.get(0).getRequest(), is(sameInstance(secondRequest)));
		assertThat(actualData.get(0).getResponse(), is(sameInstance(secondResponse)));
		assertThat(actualData.get(0).getServiceCallName(), is(equalTo(SECOND_SERVICE_WRAPPER_NAME)));
	}
	
	@Test
	public void writeCacheMockAndTest() throws Exception {
		
		System.setProperty(WHAT_TO_WRITE, "MOCKS|TESTS");
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriter formatWriter = mock(FormatWriter.class);
		doNothing().when(formatWriter).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		doNothing().when(formatWriter).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		
		CustomLoggerFormatManager customLoggerFormatManager = Mockito.mock(CustomLoggerFormatManager.class);
		Mockito.when(customLoggerFormatManager.getFormatWriter(Mockito.any(Platform.class))).thenReturn(formatWriter);
		
		ServiceCallCacheManager.getInstance().setCustomLoggerFormatManager(customLoggerFormatManager);
		
		ServiceCallCacheData data = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(data);
		
		ObserverPayload observerPayload = new ObserverPayload("ServiceCallCacheManagerTest", "writeCacheMockAndTest");
		ServiceCallCacheManager.getInstance().notifyAfterTestMethodObserver(observerPayload);
		
		Mockito.verify(formatWriter, Mockito.times(1)).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(formatWriter, Mockito.times(1)).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void writeCacheMock() throws Exception {
		
		System.setProperty(WHAT_TO_WRITE, "MOCKS");
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriter formatWriter = mock(FormatWriter.class);
		doNothing().when(formatWriter).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		doNothing().when(formatWriter).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		
		CustomLoggerFormatManager customLoggerFormatManager = Mockito.mock(CustomLoggerFormatManager.class);
		Mockito.when(customLoggerFormatManager.getFormatWriter(Mockito.any(Platform.class))).thenReturn(formatWriter);
		
		ServiceCallCacheManager.getInstance().setCustomLoggerFormatManager(customLoggerFormatManager);
		
		ServiceCallCacheData data = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(data);
		
		ObserverPayload observerPayload = new ObserverPayload("ServiceCallCacheManagerTest", "writeCacheMock");
		ServiceCallCacheManager.getInstance().notifyAfterTestMethodObserver(observerPayload);
		
		Mockito.verify(formatWriter, Mockito.times(1)).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(formatWriter, Mockito.times(0)).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void writeCacheTest() throws Exception {
		
		System.setProperty(WHAT_TO_WRITE, "TESTS");
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriter formatWriter = mock(FormatWriter.class);
		doNothing().when(formatWriter).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		doNothing().when(formatWriter).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		
		CustomLoggerFormatManager customLoggerFormatManager = Mockito.mock(CustomLoggerFormatManager.class);
		Mockito.when(customLoggerFormatManager.getFormatWriter(Mockito.any(Platform.class))).thenReturn(formatWriter);
		
		ServiceCallCacheManager.getInstance().setCustomLoggerFormatManager(customLoggerFormatManager);
		
		ServiceCallCacheData data = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(data);
		
		ObserverPayload observerPayload = new ObserverPayload("ServiceCallCacheManagerTest", "writeCacheTest");
		ServiceCallCacheManager.getInstance().notifyAfterTestMethodObserver(observerPayload);
		
		Mockito.verify(formatWriter, Mockito.times(0)).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(formatWriter, Mockito.times(1)).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void doNotWriteCacheIfWhatToWriteIsNull() {
		
		System.setProperty(WHAT_TO_WRITE, "NONE");
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriter formatWriter = mock(FormatWriter.class);
		doNothing().when(formatWriter).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		doNothing().when(formatWriter).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		
		CustomLoggerFormatManager customLoggerFormatManager = Mockito.mock(CustomLoggerFormatManager.class);
		Mockito.when(customLoggerFormatManager.getFormatWriter(Mockito.any(Platform.class))).thenReturn(formatWriter);
		
		ServiceCallCacheManager.getInstance().setCustomLoggerFormatManager(customLoggerFormatManager);
		
		ServiceCallCacheData data = new ServiceCallCacheData(request, response, SERVICE_WRAPPER_NAME);
		ServiceCallCacheManager.getInstance().addCallToCache(data);
		
		ObserverPayload observerPayload = new ObserverPayload("ServiceCallCacheManagerTest", "doNotWriteCacheIfWhatToWriteIsNull");
		ServiceCallCacheManager.getInstance().notifyAfterTestMethodObserver(observerPayload);
		
		Mockito.verify(formatWriter, Mockito.times(0)).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(formatWriter, Mockito.times(0)).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void doNotWriteCacheIfCacheIsEmpty() {
		
		System.setProperty(WHAT_TO_WRITE, "MOCKS|TESTS");
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriter formatWriter = mock(FormatWriter.class);
		doNothing().when(formatWriter).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		doNothing().when(formatWriter).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		
		CustomLoggerFormatManager customLoggerFormatManager = Mockito.mock(CustomLoggerFormatManager.class);
		Mockito.when(customLoggerFormatManager.getFormatWriter(Mockito.any(Platform.class))).thenReturn(formatWriter);
		
		ServiceCallCacheManager.getInstance().setCustomLoggerFormatManager(customLoggerFormatManager);
		
		ObserverPayload observerPayload = new ObserverPayload("ServiceCallCacheManagerTest", "doNotWriteCacheIfCacheIsEmpty");
		ServiceCallCacheManager.getInstance().notifyAfterTestMethodObserver(observerPayload);
		
		Mockito.verify(formatWriter, Mockito.times(0)).writeMocks(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(formatWriter, Mockito.times(0)).updateTests(Mockito.anyList(), Mockito.anyString(), Mockito.anyString());
	}
}
