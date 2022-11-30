package com.ebay.service.logger;

import java.util.List;

import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;

public interface FormatWriter {

	/**
	 * Get the platform associated with the logger. Return null to associate it with
	 * all platforms.
	 * 
	 * @return Platform association or null to associate with all platforms.
	 */
	public Platform getPlatformAssociation();

	/**
	 * Write the mocks to file.
	 * 
	 * @param calls          Ordered list of calls that were made during execution
	 *                       of the test method.
	 * @param testClassName  Name of the test class containing the test method that
	 *                       was executed.
	 * @param testMethodName Name of the test method that was executed.
	 */
	public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName);

	/**
	 * Update the tests with statements to drive UI automation.
	 * 
	 * @param calls          Ordered list of calls that were made during execution
	 *                       of the test method.
	 * @param testClassName  Name of the test class containing the test method that
	 *                       was executed.
	 * @param testMethodName Name of the test method that was executed.
	 */
	public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName);

}
