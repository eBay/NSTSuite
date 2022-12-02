package com.nst.tutorials.rest.mockgeneration;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.RuntimeConfigValue;
import com.ebay.runtime.arguments.WhatToWriteArguments;
import com.ebay.service.logger.WhatToWrite;
import com.ebay.softassert.EbaySoftAssert;
import com.nst.tutorials.rest.CanadaHoliday;
import com.nst.tutorials.rest.servicewrappers.ServiceWrappersWrapper;
import com.nst.tutorials.rest.shared.GenericServiceWrapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class MockGenerationTest implements NSTServiceTestRunner {

    @Test
    public void exampleMockGenerationTest() throws Exception {
        /**
         * Equivalent of setting the `whatToWrite` runtime argument in the TestNG pom.xml.
         * Example output is placed in the same directory as this file (mockgeneration).
         */
        RuntimeConfigValue<List<WhatToWrite>> whatToWriteArguments = (RuntimeConfigValue<List<WhatToWrite>>) RuntimeConfigManager.getInstance().getRuntimeArgument(WhatToWriteArguments.KEY);
        List<WhatToWrite> whatToWriteOverride = new ArrayList<>();
        whatToWriteOverride.add(WhatToWrite.MOCKS);
        whatToWriteArguments.override(whatToWriteOverride);

        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request.
        GenericServiceWrapper restServiceWrapper = new GenericServiceWrapper(CanadaHoliday.CANADA_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }
}
