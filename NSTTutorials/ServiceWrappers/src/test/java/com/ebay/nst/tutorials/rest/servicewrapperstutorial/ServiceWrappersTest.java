package com.ebay.nst.tutorials.rest.servicewrapperstutorial;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.annotations.Test;

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
