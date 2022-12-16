package com.ebay.service.logger.platforms;

import java.util.List;

import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.support.AndroidJavaFuiLogger;

public class AndroidLogger implements FormatWriter {
	
	private HarLogger harLogger = new HarLogger();
	private AndroidJavaFuiLogger fuiLogger = new AndroidJavaFuiLogger();

	@Override
	public Platform getPlatformAssociation() {
		return Platform.ANDROID;
	}

	@Override
	public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
		
		harLogger.writeMocks(calls, testClassName, testMethodName);
	}

	@Override
	public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {

		fuiLogger.updateFuiFile(testClassName, testMethodName, calls);
	}

}
