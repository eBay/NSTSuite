package com.nst.tutorials.rest.mockgeneration;

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
import org.testng.Reporter;

import java.io.IOException;
import java.util.List;

public class MockGenerationCustomFormatWriter implements FormatWriter {

    @Override
    public Platform getPlatformAssociation() {
        return Platform.IOS;
    }

    @Override
    public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
        FormatWriterUtil.removeMockFilesMatchingClassAndMethodName(testClassName, testMethodName);
        int sequenceCounter = 0;

        for (ServiceCallCacheData call : calls) {

            String fullFilePath = FormatWriterUtil.getMockFolderAndFileName(testClassName, testMethodName, sequenceCounter++, call.getServiceCallName()) + ".json";

            HarLogBuilder harLogBuilder = new HarLogBuilder();
            Har har = harLogBuilder.buildHarFromRequestResponse(call.getRequest(), call.getResponse());

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String harResponseJson = gson.toJson(har.getLog().getEntries().get(0).getResponse().getContent().getText());

            // Format JSON file
            harResponseJson = harResponseJson.replace("\\", "");
            harResponseJson = harResponseJson.replaceFirst("\"", "");
            harResponseJson = harResponseJson.substring(0, harResponseJson.length() - 1);

            FileWriterWithEncoding writer;
            try {
                writer = new FileWriterWithEncoding(fullFilePath, Encode.UTF_8);
                writer.write(harResponseJson);
                writer.close();
            } catch (IOException e) {
                throw new IllegalStateException("Error writing mocks to file: " + fullFilePath, e);
            }

            Reporter.log(String.format("Wrote JSON: %s", fullFilePath), true);
        }
    }

    @Override
    public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
        System.out.println("Update your UI test code here, if applicable.");
    }

}
