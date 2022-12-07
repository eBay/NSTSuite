package com.ebay.nst.tutorials.rest.contractvalidations;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.annotations.Test;

public class ContractValidationsTest implements NSTServiceTestRunner {

    private final NSTServiceWrapperProcessor serviceWrapperProcessor = new NSTServiceWrapperProcessor();

    @Test
    public void exampleRestTest() throws Exception {
        // Send a GET /api/v1/holidays/{holidayId} request.
        ContractValidationsWrapper restServiceWrapper = new ContractValidationsWrapper(CanadaHoliday.CANADA_DAY);
        serviceWrapperProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Test(expectedExceptions = SchemaValidationException.class)
    public void exampleRestTestWithSchemaValidationError() throws Exception {
        // Send a GET /api/v1/holidays/{holidayId} request and observe the schema validation error.
        // Remove the expectedExceptions annotation to see the error in console.
        ContractValidationsErrorWrapper restServiceWrapper = new ContractValidationsErrorWrapper(CanadaHoliday.CANADA_DAY);
        serviceWrapperProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Test(expectedExceptions = SchemaValidationException.class)
    public void exampleRestTestWithPolymorphicSchemaValidationError() throws Exception {
        // Send a GET /api/v1/holidays/{holidayId} request and observe the polymorphic schema validation error.
        // Remove the expectedExceptions annotation to see the error in console.
        ContractValidationsPolymorphicErrorWrapper restServiceWrapper = new ContractValidationsPolymorphicErrorWrapper(CanadaHoliday.CANADA_DAY);
        serviceWrapperProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }
}
