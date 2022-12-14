package com.ebay.nst.tutorials.rest.thinmodels;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.softassert.EbaySoftAssert;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ThinModelsTest implements NSTServiceTestRunner {

    private static final EbaySoftAssert SOFT_ASSERT = new EbaySoftAssert();

    @Test
    public void exampleRestTest() throws Exception {
        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request.
        ThinModelsWrapper restServiceWrapper = new ThinModelsWrapper(CanadaHoliday.CANADA_DAY);
        JSONObject response = serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
        ThinModelsThinModel thinModel = new ThinModelsThinModel(response, SOFT_ASSERT);
        Assert.assertNotEquals(thinModel.getId(), 10, "ID must NOT be equal to 10.");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void exampleRestTestWithThinModelFailure() throws Exception {
        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request.
        ThinModelsWrapper restServiceWrapper = new ThinModelsWrapper(CanadaHoliday.CHRISTMAS_DAY);
        JSONObject response = serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
        new ThinModelsThinModel(response, SOFT_ASSERT);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return SOFT_ASSERT;
    }
}
