package com.nst.tutorials.rest.servicewrappers;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.PlatformArgument;
import com.ebay.runtime.arguments.WhatToWriteArguments;
import com.ebay.service.logger.WhatToWrite;
import com.ebay.softassert.EbaySoftAssert;
import com.nst.tutorials.rest.CanadaHoliday;
import org.testng.annotations.Test;

import java.util.Arrays;

public class ServiceWrappersTest implements NSTServiceTestRunner {

    @Test
    public void exampleRestTest() throws Exception {
        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request.
        ServiceWrappersWrapper restServiceWrapper = new ServiceWrappersWrapper(CanadaHoliday.CANADA_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }
}
