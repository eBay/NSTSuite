package com.ebay.service.logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class FormatWriterUtilTest {
	
	private static final String PLATFORM_KEY = "nstplatform";
	
	private static final String IOS_MOCK_LOCATION_KEY = "iosMocksLocation";
	private static final String IOS_TEST_LOCATION_KEY = "iosTestsLocation";
	
	private static final String ANDROID_MOCK_LOCATION_KEY = "androidMocksLocation";
	private static final String ANDROID_TEST_LOCATION_KEY = "androidTestsLocation";
	
	private static final String OUTPUT_FOLDER = "formatWriterUtilTestFolder";
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		clearRuntimeArgs();
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
		clearRuntimeArgs();
		cleanupTestFileFolder();
	}
	
	private void clearRuntimeArgs() {
		
		System.clearProperty(PLATFORM_KEY);
		System.clearProperty(IOS_MOCK_LOCATION_KEY);
		System.clearProperty(IOS_TEST_LOCATION_KEY);
		System.clearProperty(ANDROID_MOCK_LOCATION_KEY);
		System.clearProperty(ANDROID_TEST_LOCATION_KEY);
		
		RuntimeConfigManager.getInstance().reinitialize();
	}
	
	// -----------------------------
	// Tests
	// -----------------------------

	public void getClassAndMethodName() throws Exception {
		ClassAndMethodName actual = FormatWriterUtil.getClassAndMethodName();
		ClassAndMethodName expected = new ClassAndMethodName("FormatWriterUtilTest", "getClassAndMethodName");
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test
	public void getClassAneMethodNameCorrectlyForTestCallingPublicMethod() throws Exception {
		ClassAndMethodName actual = doCallFormatWriterUTilToGetClassAndMethodName();
		ClassAndMethodName expected = new ClassAndMethodName("FormatWriterUtilTest", "getClassAneMethodNameCorrectlyForTestCallingPublicMethod");
		assertThat(actual, is(equalTo(expected)));
	}
	
	// Called by preceeding test method to evaluate test method name extraction from another calling method.
	public ClassAndMethodName doCallFormatWriterUTilToGetClassAndMethodName() {
		return FormatWriterUtil.getClassAndMethodName();
	}
	
	@Test
	public void getFileName() {
		String actual = FormatWriterUtil.getFileName("className", "methodName", 10, "serviceWrapperName");
		String expected = "className_methodName_10_serviceWrapperName";
		assertThat(actual, is(equalTo(expected)));
	}
	
	@DataProvider(name = "getFileNameWithNullValueValues")
	public Object[][] getFileNameWithNullValueValues() {
		return new Object[][] {
			{ null, "methodName", "serviceWrapperName" },
			{ "className", null, "serviceWrapperName" },
			{ "className", "methodName", null },
			{ null, "methodName", "serviceWrapperName" },
			{ null, null, "serviceWrapperName" },
			{ null, null, null },
			{ null, null, null },
			{ "className", null, null },
			{ "className", "methodName", null }
		};
	}
	
	@Test(dataProvider = "getFileNameWithNullValueValues", expectedExceptions = NullPointerException.class)
	public void getFileNameWithNullValues(String className, String methodName, String serviceWrapperName) {
		FormatWriterUtil.getFileName(className, methodName, 0, serviceWrapperName);
	}
	
	@Test
	public void getMockFolderAndFileNameAndroid() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "ANDROID");
		System.setProperty(ANDROID_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		String actual = FormatWriterUtil.getMockFolderAndFileName("FormatWriterUtilTest", "getMockFolderAndFileNameAndroid", 0, "ServiceWrapper");
		String expected = String.format("%s%s%s_%s_%d_%s", OUTPUT_FOLDER, File.separator, "FormatWriterUtilTest", "getMockFolderAndFileNameAndroid", 0, "ServiceWrapper");
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test
	public void getMockFolderAndFileNameIOS() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "IOS");
		System.setProperty(IOS_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		String actual = FormatWriterUtil.getMockFolderAndFileName("FormatWriterUtilTest", "getMockFolderAndFileNameIOS", 0, "ServiceWrapper");
		String expected = String.format("%s%s%s_%s_%d_%s", OUTPUT_FOLDER, File.separator, "FormatWriterUtilTest", "getMockFolderAndFileNameIOS", 0, "ServiceWrapper");
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void getMockFolderAndFileNameSite() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "SITE");
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriterUtil.getMockFolderAndFileName("FormatWriterUtilTest", "getMockFolderAndFileNameSite", 0, "ServiceWrapper");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void getMockFolderAndFileNameMWeb() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "MWEB");
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriterUtil.getMockFolderAndFileName("FormatWriterUtilTest", "getTestFolderAndFileNameAndroid", 0, "ServiceWrapper");
	}
	
	@Test
	public void getTestFolderAndFileNameAndroid() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "ANDROID");
		System.setProperty(ANDROID_TEST_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		String actual = FormatWriterUtil.getTestFolderAndFileName("FormatWriterUtilTest", "getTestFolderAndFileNameAndroid", 0, "ServiceWrapper");
		String expected = String.format("%s%s%s_%s_%d_%s", OUTPUT_FOLDER, File.separator, "FormatWriterUtilTest", "getTestFolderAndFileNameAndroid", 0, "ServiceWrapper");
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test
	public void getTestFolderAndFileNameIOS() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "IOS");
		System.setProperty(IOS_TEST_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		String actual = FormatWriterUtil.getTestFolderAndFileName("FormatWriterUtilTest", "getTestFolderAndFileNameIOS", 0, "ServiceWrapper");
		String expected = String.format("%s%s%s_%s_%d_%s", OUTPUT_FOLDER, File.separator, "FormatWriterUtilTest", "getTestFolderAndFileNameIOS", 0, "ServiceWrapper");
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void getTestFolderAndFileNameSite() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "SITE");
		RuntimeConfigManager.getInstance().reinitialize();
		FormatWriterUtil.getTestFolderAndFileName("FormatWriterUtilTest", "getOutputFolderAndFileName", 0, "ServiceWrapper");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void getTestFolderAndFileNameMWeb() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "MWEB");
		RuntimeConfigManager.getInstance().reinitialize();
		FormatWriterUtil.getTestFolderAndFileName("FormatWriterUtilTest", "getOutputFolderAndFileName", 0, "ServiceWrapper");
	}
	
	@Test
	public void getOutputFolderAndFileName() throws Exception {
		String actual = FormatWriterUtil.getOutputFolderAndFileName("output/folder/path", "FormatWriterUtilTest", "getOutputFolderAndFileName", 0, "ServiceWrapper");
		assertThat(actual, is(equalTo("output/folder/path/FormatWriterUtilTest_getOutputFolderAndFileName_0_ServiceWrapper")));
	}
	
	@DataProvider(name = "getOutputFolderAndFileNameNullInputValues")
	public Object[][] getOutputFolderAndFileNameNullInputValues() {
		return new Object[][] {
			{ null, "className", "methodName", "serviceWrapperName" },
			{ "outputFolder", null, "methodName", "serviceWrapperName" },
			{ "outputFolder", "className", null, "serviceWrapperName" },
			{ "outputFolder", "className", "methodName", null },
			{ null, null, "methodName", "serviceWrapperName" },
			{ null, null, null, "serviceWrapperName" },
			{ null, null, null, null },
			{ "outputFolder", null, null, null },
			{ "outputFolder", "className", null, null },
			{ "outputFolder", "className", "methodName", null }
		};
	}
	
	@Test(dataProvider = "getOutputFolderAndFileNameNullInputValues", expectedExceptions = NullPointerException.class)
	public void getOutputFolderAndFileNameNullInputs(String outputFolder, String className, String methodName, String serviceWrapperName) throws Exception {
		FormatWriterUtil.getOutputFolderAndFileName(outputFolder, className, methodName, 0, serviceWrapperName);
	}
	
	@Test
	public void removeMockFilesMatchingClassAndMethodNameForAndroid() throws Exception {
		
		String extraFile = "FormatWriterUtilTest_removeMockFilesMatchingClassAndMethodNameForAndr_0_TestWrapper.json";
		
		addFileToTestFileFolder(extraFile, "TestData");
		addFileToTestFileFolder("FormatWriterUtilTest_removeMockFilesMatchingClassAndMethodNameForAndroid_0_TestWrapper.json", "TestData");
		addFileToTestFileFolder("FormatWriterUtilTest_removeMockFilesMatchingClassAndMethodNameForAndroid_1_AnotherTestWrapper.json", "TestData");
		
		File[] files = getFilesInTestFolder();
		assertThat("Expect 3 test files.", files, is(arrayWithSize(3)));
		
		System.setProperty(PLATFORM_KEY, "ANDROID");
		System.setProperty(ANDROID_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriterUtil.removeMockFilesMatchingClassAndMethodName("FormatWriterUtilTest", "removeMockFilesMatchingClassAndMethodNameForAndroid");
	
		files = getFilesInTestFolder();
		assertThat("Expect 1 test file.", files, is(arrayWithSize(1)));
		assertThat(files[0].getName(), is(equalTo(extraFile)));
	}
	
	@Test
	public void removeMockFilesMatchingClassAndMethodNameForIOS() throws Exception {
		
		String extraFile = "FormatWriterUtilTest_removeMockFilesMatchingClassAndMethodNameForI_0_TestWrapper.json";
		
		addFileToTestFileFolder(extraFile, "TestData");
		addFileToTestFileFolder("FormatWriterUtilTest_removeMockFilesMatchingClassAndMethodNameForIOS_0_TestWrapper.json", "TestData");
		addFileToTestFileFolder("FormatWriterUtilTest_removeMockFilesMatchingClassAndMethodNameForIOS_1_AnotherTestWrapper.json", "TestData");
		
		File[] files = getFilesInTestFolder();
		assertThat("Expect 3 test files.", files, is(arrayWithSize(3)));
		
		System.setProperty(PLATFORM_KEY, "IOS");
		System.setProperty(IOS_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriterUtil.removeMockFilesMatchingClassAndMethodName("FormatWriterUtilTest", "removeMockFilesMatchingClassAndMethodNameForIOS");
	
		files = getFilesInTestFolder();
		assertThat("Expect 1 test file.", files, is(arrayWithSize(1)));
		assertThat(files[0].getName(), is(equalTo(extraFile)));
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void removeMockFilesMatchingClassAndMethodNameWithNullClassName() {
		
		System.setProperty(PLATFORM_KEY, "IOS");
		System.setProperty(IOS_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		System.setProperty(ANDROID_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriterUtil.removeMockFilesMatchingClassAndMethodName(null, "removeMockFilesMatchingClassAndMethodNameWithNullClassName");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void removeMockFilesMatchingClassAndMethodNameWithNullMethodName() {
		
		System.setProperty(PLATFORM_KEY, "IOS");
		System.setProperty(IOS_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		System.setProperty(ANDROID_MOCK_LOCATION_KEY, OUTPUT_FOLDER);
		
		RuntimeConfigManager.getInstance().reinitialize();
		
		FormatWriterUtil.removeMockFilesMatchingClassAndMethodName("FormatWriterUtilTest", null);
	}
	
	@Test
	public void getModifiedResponsePayload() {
		
		int responseCode = 200;
		String payload = "{ \"first\": \"value\" }";
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorize", "12345");
		
		String injectPayload = "{ \"inject\": \"payload\", \"value\": 2 }";
		
		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);
		when(response.getResponseCode()).thenReturn(responseCode);
		when(response.getHeaders()).thenReturn(headers);
		
		ResponseLoggerInjector injector = mock(ResponseLoggerInjector.class);
		when(injector.processServiceResponse(Mockito.anyString())).thenReturn(injectPayload);
		
		NSTHttpResponse actualResponse = FormatWriterUtil.getModifiedResponsePayload(response, injector);
		assertThat("Headers MUST match.", actualResponse.getHeaders(), is(equalTo(headers)));
		assertThat("Payload MUST match.", actualResponse.getPayload(), is(equalTo(injectPayload)));
		assertThat("Response code MUST match.", actualResponse.getResponseCode(), is(equalTo(responseCode)));
	}
	
	@Test
	public void getModifiedResponsePayloadWithNullInjector() {
		
		int responseCode = 200;
		String payload = "{\n  \"first\": \"value\"\n}";
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorize", "12345");
		
		NSTHttpResponse response = mock(NSTHttpResponse.class);
		when(response.getPayload()).thenReturn(payload);
		when(response.getResponseCode()).thenReturn(responseCode);
		when(response.getHeaders()).thenReturn(headers);
		
		NSTHttpResponse actualResponse = FormatWriterUtil.getModifiedResponsePayload(response, null);
		assertThat("Headers MUST match.", actualResponse.getHeaders(), is(equalTo(headers)));
		assertThat("Payload MUST match.", actualResponse.getPayload(), is(equalTo(payload)));
		assertThat("Response code MUST match.", actualResponse.getResponseCode(), is(equalTo(responseCode)));
	}
	
	@Test
	public void getModifiedResponsePayloadWithNullResponse() {
		
		ResponseLoggerInjector injector = mock(ResponseLoggerInjector.class);
		NSTHttpResponse actualResponse = FormatWriterUtil.getModifiedResponsePayload(null, injector);
		assertThat(actualResponse, is(nullValue()));
	}
	
	private File[] getFilesInTestFolder() {
		
		String currentWorkingDirectory = System.getProperty("user.dir");
		File outputFolder = new File(currentWorkingDirectory, OUTPUT_FOLDER);
		if (outputFolder.exists()) {
			return outputFolder.listFiles();
		}
		return null;
	}
	
	private void addFileToTestFileFolder(String fileName, String fileContents) throws IOException {
		
		String currentWorkingDirectory = System.getProperty("user.dir");
		
		File outputFolder = new File(currentWorkingDirectory, OUTPUT_FOLDER);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		
		File outputFile = new File(outputFolder, fileName);
		FileWriter fileWriter = new FileWriter(outputFile);
		fileWriter.write(fileContents);
		fileWriter.close();
	}
	
	private void cleanupTestFileFolder() {
		
		String currentWorkingDirectory = System.getProperty("user.dir");
		
		File outputFolder = new File(currentWorkingDirectory, OUTPUT_FOLDER);
		if (outputFolder.exists()) {
			File[] files = outputFolder.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}
}
