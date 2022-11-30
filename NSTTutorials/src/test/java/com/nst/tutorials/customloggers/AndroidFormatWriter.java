package com.nst.tutorials.customloggers;

import com.ebay.nst.resourcefile.reader.ResourceFileReader;
import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.util.PlatformApiToFuiMappingKeys;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidFormatWriter implements FormatWriter {

    private HashMap<String, String> serviceWrapperToAndroidWrapperMap = new HashMap<>();

    public AndroidFormatWriter() throws IOException {
        init();
    }

    private void init() throws IOException {
        // Read in the resource file for the service, endpoint and host mappings.
        ResourceFileReader resourceFileReader = new ResourceFileReader();
        List<Map<String, String>> hostTableData = resourceFileReader.readFixedColumnDataFromCsv("nstToFuiMappingsAndroid.csv");

        for (Map<String, String> hostData : hostTableData) {

            String apiName = hostData.get(PlatformApiToFuiMappingKeys.API.name());
            String platformRequestType = hostData.get(PlatformApiToFuiMappingKeys.PLATFORM_REQUEST_TYPE.name());

            if (apiName.isEmpty() || platformRequestType.isEmpty()) {
                continue;
            }

            serviceWrapperToAndroidWrapperMap.put(apiName, platformRequestType);
        }
    }

    @Override
    public Platform getPlatformAssociation() {
        return Platform.ANDROID;
    }

    @Override
    public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
        System.out.println("WRITE ANDROID MOCKS");
    }

    @Override
    public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
        System.out.println("UPDATE ANDROID TESTS");
    }
}
