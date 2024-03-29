package com.ebay.service.logger.platforms;

import java.io.IOException;
import java.util.List;

import org.testng.Reporter;

import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.FormatWriterUtil;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.har.Har;
import com.ebay.service.logger.har.builder.HarLogBuilder;
import com.ebay.service.logger.writer.Encode;
import com.ebay.service.logger.writer.FileWriterWithEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HarLogger implements FormatWriter {

	@Override
	public Platform getPlatformAssociation() {
		return null;
	}

	@Override
	public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
		
		FormatWriterUtil.removeMockFilesMatchingClassAndMethodName(testClassName, testMethodName);
		int sequenceCounter = 1;

		for (ServiceCallCacheData call : calls) {

			String fullFilePath = FormatWriterUtil.getMockFolderAndFileName(testClassName, testMethodName, sequenceCounter++, call.getServiceCallName()) + ".har";
			
			HarLogBuilder harLogBuilder = new HarLogBuilder();
			Har har = harLogBuilder.buildHarFromRequestResponse(call.getRequest(), call.getResponse());

			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			String harJson = gson.toJson(har);

			FileWriterWithEncoding writer;
			try {
				writer = new FileWriterWithEncoding(fullFilePath, Encode.UTF_8);
				writer.write(harJson);
				writer.close();
			} catch (IOException e) {
				throw new IllegalStateException("Error writing mocks to file: " + fullFilePath, e);
			}

			Reporter.log(String.format("Wrote har: %s", fullFilePath), true);
		}
	}

	@Override
	public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
		return; // Har writer does NOT update tests.
	}
}
