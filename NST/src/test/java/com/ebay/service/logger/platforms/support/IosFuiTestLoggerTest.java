package com.ebay.service.logger.platforms.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public class IosFuiTestLoggerTest {

  private static final String cwd = System.getProperty("user.dir");

  private static final String nstplatformKey = "nstplatform";
  private static final String platformTestsLocation = "iosTestsLocation";
  private static final String whatToWriteKey = "whatToWrite";
  
  private static final String enterCheckoutApiName = "EnterCheckout";
  private static final String setPaymentApiName = "SetPayment";
  private static final String purchaseApiName = "PurchaseCheckout";

  private static final String multipleOperationsTestFile = "IosMultipleSameOperationClass";
  private static final String addOperationsToEmptyMethodTestFile = "IosEmptyTestClass";
  private static final String keepEverythingTheSameTestFile = "IosKeepEverythingTheSame";
  private static final String removeTheFirstOperationTestFile = "IosRemoveTheFirstOperation";
  private static final String removeTheLastOperationTestFile = "IosRemoveTheLastOperation";
  private static final String replaceMiddleOperationTestFile = "IosReplaceMiddleOperation";
  private static final String replaceAllOperationsTestFile = "IosReplaceAllOperations";
  private static final String platformFuiOutputDisabledTestFile = "IosPlatformFuiOutputDisabled";
  private static final String additionalOperationStatementTestFile = "IosAdditionalOperationStatement";
  private static final String addOperationsToSecondMethod = "IosAddOperationsToSecondMethod";
  
  private static String platformTestLocation;
  
  private IosFuiTestLogger logger;
  private NSTHttpRequest request = mock(NSTHttpRequest.class);
  private NSTHttpResponse response = mock(NSTHttpResponse.class);

  @BeforeMethod(alwaysRun = true)
  public void setupBeforeEachTest() {
    platformTestLocation = String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios/", cwd);
    
    System.clearProperty(nstplatformKey);
    System.clearProperty(platformTestsLocation);
    System.clearProperty(whatToWriteKey);
    System.setProperty(nstplatformKey, "IOS");
    System.setProperty(platformTestsLocation, platformTestLocation);
    System.setProperty(whatToWriteKey, "TESTS");
    RuntimeConfigManager.getInstance().reinitialize();
    
    logger = new IosFuiTestLogger();
  }

  @AfterMethod(alwaysRun = true)
  public void afterEachTest() {

    String[] fileNamesToDelete =
        new String[] { keepEverythingTheSameTestFile, addOperationsToEmptyMethodTestFile, removeTheFirstOperationTestFile, removeTheLastOperationTestFile, replaceMiddleOperationTestFile, replaceAllOperationsTestFile, platformFuiOutputDisabledTestFile, additionalOperationStatementTestFile, addOperationsToSecondMethod };

    for (String filename : fileNamesToDelete) {
      File testFile = new File(platformTestLocation + filename + ".swift");
      testFile.delete();
    }
  }

  @Test(groups = "unitTest")
  public void addMultipleSameOperations() throws Throwable {
    File sourceFile = new File(platformTestLocation + getSourceFileName(multipleOperationsTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(multipleOperationsTestFile));
    testFile.delete();
    File expectedFile = new File(platformTestLocation + getExpectedFileName(multipleOperationsTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(secondCall);
    
    ServiceCallCacheData thirdCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(thirdCall);
    
    logger.updateFuiFile(multipleOperationsTestFile, "firstTest", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void addOperationsToEmptyMethod() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(addOperationsToEmptyMethodTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(addOperationsToEmptyMethodTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(addOperationsToEmptyMethodTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(secondCall);
    
    logger.updateFuiFile(addOperationsToEmptyMethodTestFile, "firstTest", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void addOperationsToSecondMethodOfEmptyStubTestFile() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(addOperationsToSecondMethod));
    File testFile = new File(platformTestLocation + getProducedFileName(addOperationsToSecondMethod));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(addOperationsToSecondMethod));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(secondCall);
    
    logger.updateFuiFile(addOperationsToSecondMethod, "secondTest", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void additionalOperationStatementAdded() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(additionalOperationStatementTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(additionalOperationStatementTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(additionalOperationStatementTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, setPaymentApiName);
    callLog.add(secondCall);
    
    ServiceCallCacheData thirdCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(thirdCall);
    
    logger.updateFuiFile(additionalOperationStatementTestFile, "additionalOperationStatementAdded", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void attemptToModifyMethodThatDoesNotExist() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(platformFuiOutputDisabledTestFile));
    File expectedFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);

    logger.updateFuiFile(platformFuiOutputDisabledTestFile, "unknownTestMethod", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void keepEverythingTheSame() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(keepEverythingTheSameTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(keepEverythingTheSameTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(secondCall);
    
    logger.updateFuiFile(keepEverythingTheSameTestFile, "keepEverythingTheSame", callLog);

    compareExpectedVsActualFiles(testFile, sourceFile);
  }

  @Test(groups = "unitTest")
  public void platformFuiOutputDisable() throws Throwable {

    System.clearProperty(nstplatformKey);
    System.clearProperty(platformTestsLocation);
    System.setProperty(nstplatformKey, "IOS");
    RuntimeConfigManager.getInstance().reinitialize();

    File sourceFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(platformFuiOutputDisabledTestFile));
    File expectedFile = new File(platformTestLocation + getSourceFileName(platformFuiOutputDisabledTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(secondCall);
    
    logger.updateFuiFile(platformFuiOutputDisabledTestFile, "iosPlatformFuiOutputDisable", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void removeTheFirstOperation() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(removeTheFirstOperationTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(removeTheFirstOperationTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(removeTheFirstOperationTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(firstCall);

    logger.updateFuiFile(removeTheFirstOperationTestFile, "removeTheFirstOperation", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void removeTheLastOperation() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(removeTheLastOperationTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(removeTheLastOperationTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(removeTheLastOperationTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    logger.updateFuiFile(removeTheLastOperationTestFile, "removeTheLastOperation", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void replaceAllOperations() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(replaceAllOperationsTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(replaceAllOperationsTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(replaceAllOperationsTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, setPaymentApiName);
    callLog.add(secondCall);
    
    ServiceCallCacheData thirdCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(thirdCall);
    
    logger.updateFuiFile(replaceAllOperationsTestFile, "replaceAllOperations", callLog);

    compareExpectedVsActualFiles(testFile, expectedFile);
  }

  @Test(groups = "unitTest")
  public void replaceTheMiddleOperation() throws Throwable {

    File sourceFile = new File(platformTestLocation + getSourceFileName(replaceMiddleOperationTestFile));
    File testFile = new File(platformTestLocation + getProducedFileName(replaceMiddleOperationTestFile));
    File expectedFile = new File(platformTestLocation + getExpectedFileName(replaceMiddleOperationTestFile));
    Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(testFile.getAbsolutePath()));

    List<ServiceCallCacheData> callLog = new ArrayList<>();
    ServiceCallCacheData firstCall = new ServiceCallCacheData(request, response, enterCheckoutApiName);
    callLog.add(firstCall);
    
    ServiceCallCacheData secondCall = new ServiceCallCacheData(request, response, setPaymentApiName);
    callLog.add(secondCall);
    
    ServiceCallCacheData thirdCall = new ServiceCallCacheData(request, response, purchaseApiName);
    callLog.add(thirdCall);
    
    logger.updateFuiFile(replaceMiddleOperationTestFile, "replaceTheMiddleOperation", callLog);

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
    return String.format("%s.swift", name);
  }

  private String getExpectedFileName(String name) {
    return String.format("SOURCE_%sExpected.swift", name);
  }

  private String getSourceFileName(String name) {
    return String.format("SOURCE_%s.swift", name);
  }
}
