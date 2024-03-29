package com.ebay.nst;

import java.util.List;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.event.ObserverPayload;
import com.ebay.runtime.event.TestExecutionEventManager;
import com.ebay.service.logger.call.cache.ServiceCallCacheManager;
import com.ebay.softassert.EbaySoftAssert;

public interface NSTServiceTestRunner extends IHookable {

	/**
	 * Get the soft assert instance used in the test. Will call assertAll() on this
	 * at then of the test method execution.
	 * 
	 * @return Soft assert instance or null if not used.
	 */
	public EbaySoftAssert getSoftAssert();

	/**
	 * Overriding IHookable's run method to make sure softAssert.assertAll() is
	 * called after each test method is run without having to remember to add it to
	 * each test method.
	 */
	@Override
	public default void run(IHookCallBack hookCallback, ITestResult iTestResult) {

		String startTestCase = "TestNG:Start %s";
		String endTestCase = "TestNG:End %s";

		Reporter.setCurrentTestResult(iTestResult);

		String methodName = iTestResult.getMethod().getMethodName();
		String classAndPackage = iTestResult.getInstanceName();
		String[] classAndPackageList = classAndPackage.split("\\.");
		String className = classAndPackageList[classAndPackageList.length - 1];

		Reporter.log(String.format(startTestCase, methodName), true);

		ObserverPayload payload = new ObserverPayload(className, methodName);
		TestExecutionEventManager.getInstance().notifyBeforeTestMethodObserver(payload);

		hookCallback.runTestMethod(iTestResult);

		// Clear all runtime overrides.
		// This needs to occur after the test method executes to enable the use of
		// @beforeMethod to setup overrides before teach test method executes.
		RuntimeConfigManager.getInstance().reinitialize();

		// The latest updates to TestNG seem to have modified the ITestResult status handling.
		// These are set for the timeout variants of invokeMethod*(). runTestMethod() calls
		// invokeMethod() and captures the Throwable, if thrown, but does not modify
		// the ITestResult status. If no exception was thrown we will set the status to SUCCESS
		// here and carry on.
		Throwable throwable = iTestResult.getThrowable();
		if (throwable == null) {
			iTestResult.setStatus(ITestResult.SUCCESS);
		} else {
			iTestResult.setStatus(ITestResult.FAILURE);
		}

		if (iTestResult.getStatus() == ITestResult.SUCCESS) {
			try {
				EbaySoftAssert softAssert = getSoftAssert();
				if (softAssert != null) {
					softAssert.assertAll();
				}
			} catch (Throwable t) {
				iTestResult.setThrowable(t);
				iTestResult.setStatus(ITestResult.FAILURE);
			}
		}

		// If the test passed write mocks/tests
		if (iTestResult.getStatus() == ITestResult.SUCCESS) {
			ServiceCallCacheManager.getInstance().notifyAfterTestMethodObserver(payload);
		}

		Reporter.log(String.format(endTestCase, methodName), true);

		// Prepare the reporter log data for output to test reports.
		// Plumb this into a test attribute for consumption by the test rail writer.
		List<String> output = Reporter.getOutput(iTestResult);
		StringBuilder reporterLog = new StringBuilder();
		reporterLog.append(methodName);
		reporterLog.append("\n");

		for (String value : output) {
			reporterLog.append(value);
			reporterLog.append("\n");
		}

		iTestResult.setAttribute("CONSOLE_LOG", reporterLog.toString());
	}
}
