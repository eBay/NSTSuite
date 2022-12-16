package com.ebay.service.logger.platforms.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class AndroidJavaFuiLoggerTest {

  private static final String cwd = System.getProperty("user.dir");

  private static final String nstplatformKey = "nstplatform";
  private static final String androidPlatformTestsLocation = "androidTestsLocation";
  private static final String whatToWriteKey = "whatToWrite";

  private static String platformTestLocation;

  private static final String addOperationsToEmptyMethodTestFile = "AndroidEmptyTestClass";
  private static final String keepEverythingTheSameTestFile = "AndroidKeepEverythingTheSameExpected";
  private static final String removeTheFirstOperationTestFile = "AndroidRemoveTheFirstOperation";
  private static final String removeTheLastOperationTestFile = "AndroidRemoveLastOperation";
  private static final String replaceMiddleOperationTestFile = "AndroidReplaceMiddleOperation";
  private static final String replaceAllOperationsTestFile = "AndroidReplaceAllOperations";
  private static final String platformFuiOutputDisabledTestFile = "AndroidPlatformFuiOutputDisabled";
  private static final String additionalOperationStatementTestFile = "AndroidAdditionalOperationStatement";
  private static final String addOperationsToSecondMethodOfEmptyStubTestFile = "AndroidAddOperationsToSecondMethod";
  
  private AndroidJavaFuiLogger logger;
  
  private NSTHttpRequest request = Mockito.mock(NSTHttpRequest.class);
  private NSTHttpResponse response = Mockito.mock(NSTHttpResponse.class);

  @BeforeMethod(alwaysRun = true)
  public void setupBeforeEachTest() {
    platformTestLocation = String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android/", cwd);

    System.clearProperty(nstplatformKey);
    System.clearProperty(androidPlatformTestsLocation);
    System.setProperty(nstplatformKey, "ANDROID");
    System.setProperty(androidPlatformTestsLocation, platformTestLocation);
    System.setProperty(whatToWriteKey, "TESTS");
    RuntimeConfigManager.getInstance().reinitialize();
    
    logger = new AndroidJavaFuiLogger();
    
    cleanupTestFileDirectory();
  }

  @AfterMethod(alwaysRun = true)
  public void afterEachTest() {

	  cleanupTestFileDirectory();
  }
  
  private void cleanupTestFileDirectory() {
	  
	    String[] fileNamesToDelete =
	            new String[] { keepEverythingTheSameTestFile, addOperationsToEmptyMethodTestFile, removeTheFirstOperationTestFile, removeTheLastOperationTestFile, replaceMiddleOperationTestFile, replaceAllOperationsTestFile, platformFuiOutputDisabledTestFile, additionalOperationStatementTestFile, addOperationsToSecondMethodOfEmptyStubTestFile };

	        for (String filename : fileNamesToDelete) {
	          File testFile = new File(platformTestLocation + filename + ".java");
	          testFile.delete();
	        }
  }

  @AfterClass(alwaysRun = true)
  public void resetAtEnd() {
	System.clearProperty(nstplatformKey);
	System.clearProperty(androidPlatformTestsLocation);
	System.clearProperty(whatToWriteKey);
  }

  @Test(groups = "unitTest")
  public void addOperationsToEmptyMethod() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(addOperationsToEmptyMethodTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(addOperationsToEmptyMethodTestFile));
    File expectedFile = new File(platformTestLocation + "SOURCE_AndroidAddOperationsToEmptyMethodExpected.java");
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(second);
    
    logger.updateFuiFile(addOperationsToEmptyMethodTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void addOperationsToSecondMethodOfEmptyStubTestFile() throws Throwable {
    File sourceFile = new File(platformTestLocation + getSourceFileName(addOperationsToSecondMethodOfEmptyStubTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(addOperationsToSecondMethodOfEmptyStubTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(addOperationsToSecondMethodOfEmptyStubTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(second);
    
    logger.updateFuiFile(addOperationsToSecondMethodOfEmptyStubTestFile, "secondTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void keepEverythingTheSame() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(keepEverythingTheSameTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(keepEverythingTheSameTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(second);
    
    logger.updateFuiFile(keepEverythingTheSameTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, sourceFile);
  }

  @Test(groups = "unitTest")
  public void removeTheFirstOperation() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(removeTheFirstOperationTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(removeTheFirstOperationTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(removeTheFirstOperationTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(first);
    
    logger.updateFuiFile(removeTheFirstOperationTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void removeTheLastOperation() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(removeTheLastOperationTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(removeTheLastOperationTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(removeTheLastOperationTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    logger.updateFuiFile(removeTheLastOperationTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void replaceTheMiddleOperation() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(replaceMiddleOperationTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(replaceMiddleOperationTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(replaceMiddleOperationTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "SetPayment");
    callData.add(second);
    
    ServiceCallCacheData third = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(third);
    
    logger.updateFuiFile(replaceMiddleOperationTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void replaceAllOperations() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(replaceAllOperationsTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(replaceAllOperationsTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(replaceAllOperationsTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "SetPayment");
    callData.add(second);
    
    ServiceCallCacheData third = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(third);
    
    logger.updateFuiFile(replaceAllOperationsTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void additionalOperationStatementAdded() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(additionalOperationStatementTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(additionalOperationStatementTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(additionalOperationStatementTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "SetPayment");
    callData.add(second);
    
    ServiceCallCacheData third = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(third);
    
    logger.updateFuiFile(additionalOperationStatementTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void attemptToModifyMethodThatDoesNotExist() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(platformFuiOutputDisabledTestFile));
    File expectedFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    logger.updateFuiFile(addOperationsToEmptyMethodTestFile, "unknownTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void platformFuiOutputDisabled() throws Throwable {

    System.clearProperty(nstplatformKey);
    System.clearProperty(androidPlatformTestsLocation);
    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    File sourceFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(platformFuiOutputDisabledTestFile));
    File expectedFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callData = new ArrayList<>();
    
    ServiceCallCacheData first = new ServiceCallCacheData(request, response, "EnterCheckout");
    callData.add(first);
    
    ServiceCallCacheData second = new ServiceCallCacheData(request, response, "PurchaseCheckout");
    callData.add(second);
    
    logger.updateFuiFile(addOperationsToEmptyMethodTestFile, "firstTestMethod", callData);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  private void compareExpectedVsActualFiles(File actual, File expected) throws IOException {

    List<String> expectedLines = Files.readAllLines(Paths.get(expected.getAbsolutePath()), Charset.forName("utf8"));
    List<String> actualLines = Files.readAllLines(Paths.get(actual.getAbsolutePath()), Charset.forName("utf8"));

    assertThat("The number of lines in each file does not match.", actualLines.size(), is(equalTo(expectedLines.size())));
    for (int i = 0; i < expectedLines.size(); i++) {
      assertThat(String.format("Line %d does not match accross files.", i + 1), actualLines.get(i), is(equalTo(expectedLines.get(i))));
    }
  }

  private String getProducedFileName(String name) {
    return String.format("%s.java", name);
  }

  private String getExpectedFileName(String name) {
    return String.format("SOURCE_%sExpected.java", name);
  }

  private String getSourceFileName(String name) {
    return String.format("SOURCE_%s.java", name);
  }
}
