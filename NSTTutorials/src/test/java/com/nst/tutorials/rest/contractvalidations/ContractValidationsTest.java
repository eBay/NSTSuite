package com.nst.tutorials.rest.contractvalidations;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.softassert.EbaySoftAssert;
import com.nst.tutorials.rest.shared.CanadaHoliday;
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
        ContractValidationsErrorWrapper restServiceWrapper = new ContractValidationsErrorWrapper(CanadaHoliday.CANADA_DAY);
        serviceWrapperProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Test(expectedExceptions = SchemaValidationException.class)
    public void exampleRestTestWithPolymorphicSchemaValidationError() throws Exception {
        // Send a GET /api/v1/holidays/{holidayId} request and observe the polymorphic schema validation error.
        ContractValidationsPolymorphicErrorWrapper restServiceWrapper = new ContractValidationsPolymorphicErrorWrapper(CanadaHoliday.CANADA_DAY);
        serviceWrapperProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }
}
