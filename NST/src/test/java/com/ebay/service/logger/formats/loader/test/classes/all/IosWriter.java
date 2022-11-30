package com.ebay.service.logger.formats.loader.test.classes.all;

import java.util.List;

import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;

public class IosWriter implements FormatWriter {

	@Override
	public Platform getPlatformAssociation() {
		return Platform.IOS;
	}

	@Override
	public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
		// TODO Auto-generated method stub
		
	}

}
