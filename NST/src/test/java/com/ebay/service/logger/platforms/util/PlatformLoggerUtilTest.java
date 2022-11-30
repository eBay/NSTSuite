package com.ebay.service.logger.platforms.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformMethodModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.ebay.service.protocol.http.NSTHttpResponseImpl;

public class PlatformLoggerUtilTest {
	
	private static final String cwd = System.getProperty("user.dir");
	private static final String PLATFORM_KEY = "nstplatform";
	private static final String ANDROID_PLATFORM_TEST_LOCATION_KEY = "androidTestsLocation";
	private static final String IOS_PLATFORM_TEST_LOCATION_KEY = "iosTestsLocation";
	
	@BeforeMethod(alwaysRun = true)
	public void setup() {
		cleanup();
	}
	
	@AfterMethod(alwaysRun = true)
	public void teardown() {
		cleanup();
	}
	
	private void cleanup() {
		System.clearProperty(PLATFORM_KEY);
		System.clearProperty(ANDROID_PLATFORM_TEST_LOCATION_KEY);
		System.clearProperty(IOS_PLATFORM_TEST_LOCATION_KEY);
		RuntimeConfigManager.getInstance().reinitialize();
	}
	
	// ------------------------------------
	// Tests
	// ------------------------------------

	@Test
	public void getTestFileAndroid() throws Exception {
		
	    System.setProperty(PLATFORM_KEY, "ANDROID");
	    System.setProperty(ANDROID_PLATFORM_TEST_LOCATION_KEY, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android", cwd));
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("SOURCE_AndroidEmptyTestClass");
		assertThat(testClassFile, is(notNullValue()));
	}

	@Test
	public void getTestFileAndroidMissing() throws Exception {

		System.setProperty(PLATFORM_KEY, "ANDROID");
	    System.setProperty(ANDROID_PLATFORM_TEST_LOCATION_KEY, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android", cwd));
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("AndroidFileMissing");
		assertThat(testClassFile, is(nullValue()));
	}

	@Test
	public void getTestFileAndroidLocationNotSpecifiedAsRuntimeArgument() throws Exception {

		System.setProperty(PLATFORM_KEY, "ANDROID");
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("SOURCE_AndroidEmptyTestClass");
		assertThat(testClassFile, is(nullValue()));
	}

	@Test
	public void getTestFileIos() throws Exception {

		System.setProperty(PLATFORM_KEY, "IOS");
	    System.setProperty(IOS_PLATFORM_TEST_LOCATION_KEY, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios", cwd));
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("SOURCE_IosEmptyTestClass");
		assertThat(testClassFile, is(notNullValue()));
	}

	@Test
	public void getTestFileIosMissing() throws Exception {

		System.setProperty(PLATFORM_KEY, "IOS");
	    System.setProperty(IOS_PLATFORM_TEST_LOCATION_KEY, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios", cwd));
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("IosFileMissing");
		assertThat(testClassFile, is(nullValue()));
	}

	@Test
	public void getTestFileIosLocaitonNotSpecifiedAsRuntimeArgument() throws Exception {

		System.setProperty(PLATFORM_KEY, "IOS");
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("SOURCE_IosEmptyTestClass");
		assertThat(testClassFile, is(nullValue()));
	}

	@Test
	public void getTestFileMweb() throws Exception {

		System.setProperty(PLATFORM_KEY, "MWEB");
	    System.setProperty(IOS_PLATFORM_TEST_LOCATION_KEY, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/mweb", cwd));
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("SOURCE_MWebEmptyTestClass");
		assertThat(testClassFile, is(nullValue()));
	}

	@Test
	public void getTestFileSite() throws Exception {

		System.setProperty(PLATFORM_KEY, "SITE");
	    System.setProperty(IOS_PLATFORM_TEST_LOCATION_KEY, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/site", cwd));
	    RuntimeConfigManager.getInstance().reinitialize();

		File testClassFile = PlatformLoggerUtil.getTestFile("SOURCE_SiteEmptyTestClass");
		assertThat(testClassFile, is(nullValue()));
	}

	@Test
	public void updateOperationsWithNewFileModelAndNoCallLogData() throws Exception {
		
		// Setup existing operations
		GeneralPlatformFileModel fileModel = new GeneralPlatformFileModel();
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = new ArrayList<>();
		
		// Setup expected operations
		GeneralPlatformFileModel expectedFileModel = new GeneralPlatformFileModel();
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}

	@Test
	public void updateOperationsWithNewFileModelAndOneCallLog() throws Exception {
		
		String serviceWrapperName = "testServiceCallName";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = new GeneralPlatformFileModel();
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperName));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperName));
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}

	@Test
	public void updateOperationsWithNewFileModelAndTwoCallLogs() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";
		String serviceWrapperNameB = "secondServiceCall";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = new GeneralPlatformFileModel();
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}

	@Test
	public void updateOperationsWithExistingFileModelAndNoCallLogData() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA));
		fileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = new ArrayList<>();
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = new GeneralPlatformFileModel();
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}

	@Test
	public void updateOperationsWithExistingFileModelAndOneCallLog_NothingChanging() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA));
		fileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA));
		expectedFileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE\n");
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}
	
	@Test
	public void updateOperationsWithExistingFileModelAndOneCallLog_CallChanging() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList("GarbageServiceName"));
		fileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA));
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}

	@Test
	public void updateOperationsWithExistingFileModelAndTwoCallLogs_FirstCallNotChanging() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";
		String serviceWrapperNameB = "secondServiceCall";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA));
		fileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		expectedFileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE\n");
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}
	
	@Test
	public void updateOperationsWithExistingFileModelAndTwoCallLogs_SecondCallNotChanging() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";
		String serviceWrapperNameB = "secondServiceCall";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList("GarbageFirstServiceWrapper", serviceWrapperNameB));
		fileModel.getMethodContents().getMethodOperations().get(1).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		expectedFileModel.getMethodContents().getMethodOperations().get(1).addCustomBlockLine("CUSTOM LINE\n");
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}
	
	@Test
	public void updateOperationsWithExistingFileModelAndTwoCallLogs_FirstCallChanging() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";
		String serviceWrapperNameB = "secondServiceCall";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList("GarbageServiceName"));
		fileModel.getMethodContents().getMethodOperations().get(0).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}
	
	@Test
	public void updateOperationsWithExistingFileModelAndTwoCallLogs_SecondCallChanging() throws Exception {

		String serviceWrapperNameA = "testServiceCallName";
		String serviceWrapperNameB = "secondServiceCall";

		// Setup existing operations
		GeneralPlatformFileModel fileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA, "GarbageServiceName"));
		fileModel.getMethodContents().getMethodOperations().get(1).addCustomBlockLine("CUSTOM LINE");
		
		// Setup new operations
		List<ServiceCallCacheData> callLogData = generateCallLog(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Setup expected operations		
		GeneralPlatformFileModel expectedFileModel = generatePlatformFileModel(Arrays.asList(serviceWrapperNameA, serviceWrapperNameB));
		
		// Perform update and check actual against expected
		PlatformLoggerUtil.updatedOperations(fileModel, callLogData);
		
		assertThat(fileModel, is(equalTo(expectedFileModel)));
	}

	@Test
	public void getImportStatementsFromEmptyCallLog() throws Exception {

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();

		List<String> actual = PlatformLoggerUtil.getImportStatements(operations);

		assertThat(actual, is(empty()));
	}

	@Test
	public void getImportStatementsFromCallLog() throws Exception {

		GeneralPlatformOperationModel firstModel = new GeneralPlatformOperationModel();
		firstModel.setServiceWrapperApiName("EnterCheckout");

		List<GeneralPlatformOperationModel> operations = Arrays.asList(firstModel);

		List<String> actual = PlatformLoggerUtil.getImportStatements(operations);

		List<String> expected = Arrays.asList("import EnterCheckoutSession");

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void getImportStatementsFromCallLogWithRedundantImportCases() throws Exception {

		GeneralPlatformOperationModel firstModel = new GeneralPlatformOperationModel();
		firstModel.setServiceWrapperApiName("EnterCheckout");

		GeneralPlatformOperationModel secondModel = new GeneralPlatformOperationModel();
		secondModel.setServiceWrapperApiName("EnterCheckout");

		List<GeneralPlatformOperationModel> operations = Arrays.asList(firstModel, secondModel);

		List<String> actual = PlatformLoggerUtil.getImportStatements(operations);

		List<String> expected = Arrays.asList("import EnterCheckoutSession");

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void getMemeberVariableStatementsFromEmptyCallLog() throws Exception {

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();

		List<String> actual = PlatformLoggerUtil.getMemberVariableStatements(operations);

		assertThat(actual, is(empty()));
	}

	@Test
	public void getMemberVariableStatementsFromCallLog() throws Exception {

		GeneralPlatformOperationModel firstModel = new GeneralPlatformOperationModel();
		firstModel.setServiceWrapperApiName("EnterCheckout");

		List<GeneralPlatformOperationModel> operations = Arrays.asList(firstModel);

		List<String> actual = PlatformLoggerUtil.getMemberVariableStatements(operations);

		List<String> expected = Arrays.asList("let enterCheckoutSession = EnterCheckoutSession()");

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void getMemberVariableStatementsFromCallLogWithRedundantMemberVariableCases() throws Exception {

		GeneralPlatformOperationModel firstModel = new GeneralPlatformOperationModel();
		firstModel.setServiceWrapperApiName("EnterCheckout");

		GeneralPlatformOperationModel secondModel = new GeneralPlatformOperationModel();
		secondModel.setServiceWrapperApiName("EnterCheckout");

		List<GeneralPlatformOperationModel> operations = Arrays.asList(firstModel, secondModel);

		List<String> actual = PlatformLoggerUtil.getMemberVariableStatements(operations);

		// We do not expect to see redundant member variable initialization just because
		// the service wrapper is used mulitple times.
		List<String> expected = Arrays.asList("let enterCheckoutSession = EnterCheckoutSession()");

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void getMethodBlockStatementsWithEmptyOperationList() throws Exception {

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();

		String actual = PlatformLoggerUtil.getMethodBlockStatements(operations, "  ");
		String expected = "";
		assertThat(actual, is(equalTo(expected)));
	}

	@Test()
	public void getMethodBlockStatementsWithUnknownServiceMappingDefinition() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();

		GeneralPlatformOperationModel operation = new GeneralPlatformOperationModel();
		operation.setServiceWrapperApiName("TestServiceWrapperApiName");
		operation.addCustomBlockLine("    System.out.println(\"foo\");");
		operation.addCustomBlockLine("    int b = 0;");
		operation.addCustomBlockLine("    b++;");

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();
		operations.add(operation);

		String actual = PlatformLoggerUtil.getMethodBlockStatements(operations, "  ");
		String expected = "  // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS\n"
				+ "  // CORRESPONDING API CALL: TestServiceWrapperApiName\n"
				+ "  // OPERATION INDEX: 1\n"
				+ "  // Undefined mapping for : TestServiceWrapperApiName\n"
				+ "  // END OF AUTO GENERATED METHOD CODE BLOCK\n"
				+ "    System.out.println(\"foo\");\n"
				+ "    int b = 0;\n"
				+ "    b++;\n";

		assertThat(actual, is(equalTo(expected)));
	}

	@Test()
	public void getMethodBlockStatementsWithSingleStatement() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();

		GeneralPlatformOperationModel operation = new GeneralPlatformOperationModel();
		operation.setServiceWrapperApiName("EnterCheckout");
		operation.addCustomBlockLine("    System.out.println(\"foo\");");
		operation.addCustomBlockLine("    int b = 0;");
		operation.addCustomBlockLine("    b++;");

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();
		operations.add(operation);

		String actual = PlatformLoggerUtil.getMethodBlockStatements(operations, "  ");
		String expected = "  // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS\n"
				+ "  // CORRESPONDING API CALL: EnterCheckout\n"
				+ "  // OPERATION INDEX: 1\n"
				+ "  enterCheckoutSession.enterCheckout();\n"
				+ "  // END OF AUTO GENERATED METHOD CODE BLOCK\n"
				+ "    System.out.println(\"foo\");\n"
				+ "    int b = 0;\n"
				+ "    b++;\n";

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void getMethodBlockStatementsWithMultipleStatements() throws Exception {
		
		System.setProperty(PLATFORM_KEY, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();

		GeneralPlatformOperationModel firstOperation = new GeneralPlatformOperationModel();
		firstOperation.setServiceWrapperApiName("EnterCheckout");
		firstOperation.addCustomBlockLine("    System.out.println(\"foo\");");
		firstOperation.addCustomBlockLine("    int b = 0;");
		firstOperation.addCustomBlockLine("    b++;");
		
		GeneralPlatformOperationModel secondOperation = new GeneralPlatformOperationModel();
		secondOperation.setServiceWrapperApiName("FooServiceWrapperApiName");
		secondOperation.addCustomBlockLine("    System.out.println(\"bar\");");
		secondOperation.addCustomBlockLine("    int c = 0;");
		secondOperation.addCustomBlockLine("    c++;");

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();
		operations.add(firstOperation);
		operations.add(secondOperation);

		String actual = PlatformLoggerUtil.getMethodBlockStatements(operations, "  ");
		String expected = "  // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS\n"
				+ "  // CORRESPONDING API CALL: EnterCheckout\n"
				+ "  // OPERATION INDEX: 1\n"
				+ "  enterCheckoutSession.enterCheckout();\n"
				+ "  // END OF AUTO GENERATED METHOD CODE BLOCK\n"
				+ "    System.out.println(\"foo\");\n"
				+ "    int b = 0;\n"
				+ "    b++;\n"
				+ "  // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS\n"
				+ "  // CORRESPONDING API CALL: FooServiceWrapperApiName\n"
				+ "  // OPERATION INDEX: 2\n"
				+ "  // Undefined mapping for : FooServiceWrapperApiName\n"
				+ "  // END OF AUTO GENERATED METHOD CODE BLOCK\n"
				+ "    System.out.println(\"bar\");\n"
				+ "    int c = 0;\n"
				+ "    c++;\n";
		
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void getMethodBlockStatementsWithNullOperations() throws Exception {

		String actual = PlatformLoggerUtil.getMethodBlockStatements(null, "  ");
		assertThat(actual, is(equalTo("")));
	}

	@Test
	public void getMethodBlockStatementsWithNullIndentValue() throws Exception {

		System.setProperty(PLATFORM_KEY, "ANDROID");
		RuntimeConfigManager.getInstance().reinitialize();

		GeneralPlatformOperationModel operation = new GeneralPlatformOperationModel();
		operation.setServiceWrapperApiName("EnterCheckout");
		operation.addCustomBlockLine("    System.out.println(\"foo\");");
		operation.addCustomBlockLine("    int b = 0;");
		operation.addCustomBlockLine("    b++;");

		List<GeneralPlatformOperationModel> operations = new ArrayList<>();
		operations.add(operation);

		String actual = PlatformLoggerUtil.getMethodBlockStatements(operations, null);
		String expected = "  // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS\n"
				+ "  // CORRESPONDING API CALL: EnterCheckout\n"
				+ "  // OPERATION INDEX: 1\n"
				+ "  enterCheckoutSession.enterCheckout();\n"
				+ "  // END OF AUTO GENERATED METHOD CODE BLOCK\n"
				+ "    System.out.println(\"foo\");\n"
				+ "    int b = 0;\n"
				+ "    b++;\n";

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void stringLeadInContainsNewline_NullString() throws Exception {
		boolean result = PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace(null);
		assertThat("Null string.", result, is(equalTo(false)));
	}

	@Test
	public void stringLeadInContainsNewline_NoNewline() throws Exception {
		boolean result = PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace("ABC123%@@@");
		assertThat("String without newline character.", result, is(equalTo(false)));
	}

	@Test
	public void stringLeadInContainsNewline_NewlineCharacterAfterLetters() throws Exception {
		boolean result = PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace("ABC\n");
		assertThat("String with newline after alpha characters.", result, is(equalTo(false)));
	}

	@Test
	public void stringLeadInContainsNewline_NewlineCharacterAfterNumbers() throws Exception {
		boolean result = PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace("1234!@#$%.,\n");
		assertThat("String with newline after alpha numeric characters.", result, is(equalTo(false)));
	}

	@DataProvider(name = "mixedWhitespaceGenerator")
	public Object[][] mixedWhitespaceGenerator() throws Exception {
		return new String[][] { { "\n" }, { " \n" }, { "\t\n" }, { " \t\n" }, { "\t \n" }, { " \t\nABC" },
				{ " \t\n123!@#$%%%%" }, { "\n\n\n\n" }, { " \nABC\n123\n" }, { "\r" }, { "\r\n" }, { "\n\r" },
				{ " \t\r" }, { " \r" } };
	}

	@Test(dataProvider = "mixedWhitespaceGenerator")
	public void stringLeadInContainsNewline_NewlineMixedInWithWhitespace(String value) throws Exception {
		boolean result = PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace(value);
		assertThat("String with newline leadin character.", result, is(equalTo(true)));
	}
	
	// -------------------------------------
	// Test data setup methods
	
	private List<ServiceCallCacheData> generateCallLog(List<String> serviceWrapperNames) throws MalformedURLException {
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		NSTHttpRequest request = new NSTHttpRequestImpl.Builder(new URL("https://www.ebay.com"), NstRequestType.GET).build();
		NSTHttpResponseImpl response = new NSTHttpResponseImpl();
		response.setResponseCode(200);
		response.setHeaders(headers);
		response.setPayload("{ \"test\": \"payload\" }");

		List<ServiceCallCacheData> callLogData = new ArrayList<>();
		
		for (String serviceWrapperName : serviceWrapperNames) {
			ServiceCallCacheData firstCallLog = new ServiceCallCacheData(request, response, serviceWrapperName);
			callLogData.add(firstCallLog);
		}
		
		return callLogData;
	}
	
	private GeneralPlatformFileModel generatePlatformFileModel(List<String> serviceWrapperNames) {
		
		List<GeneralPlatformOperationModel> operations = new ArrayList<>();
		for (String serviceWrapperName : serviceWrapperNames) {
			GeneralPlatformOperationModel operation = new GeneralPlatformOperationModel();
			operation.setServiceWrapperApiName(serviceWrapperName);
			operations.add(operation);
		}
		
		GeneralPlatformMethodModel methodModel = new GeneralPlatformMethodModel();
		methodModel.setMethodOperations(operations);
		
		GeneralPlatformFileModel expectedFileModel = new GeneralPlatformFileModel();
		expectedFileModel.setMethodContents(methodModel);
		
		return expectedFileModel;
	}
}
