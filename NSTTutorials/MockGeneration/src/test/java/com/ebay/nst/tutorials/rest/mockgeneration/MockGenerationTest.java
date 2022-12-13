package com.ebay.nst.tutorials.rest.mockgeneration;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.GenericServiceWrapper;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.File;

public class MockGenerationTest implements NSTServiceTestRunner {

    private static final String GENERATED_MOCK_PATH = RuntimeConfigManager.getInstance().getIosMocksLocation() + "/MockGenerationTest_exampleMockGenerationTest_0_GenericServiceWrapper.json";

    @Test
    public void exampleMockGenerationTest() throws Exception {
        // Remove any prior mocks that were generated when running the test.
        File existingMock = new File(GENERATED_MOCK_PATH);
        if (existingMock.exists()) {
            existingMock.delete();
            System.out.printf("Removed existing mock at: %s%n", GENERATED_MOCK_PATH);
        }

        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request.
        // Example output is placed in the same directory as this file (mockgenerationtutorial).
        GenericServiceWrapper restServiceWrapper = new GenericServiceWrapper(CanadaHoliday.CANADA_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @AfterClass
    // This assertion was added to ensure that mock generation output is successful in this tutorial.
    // This is not a required part of writing an NST test.
    public void ensureMockIsGeneratedSuccessfully() {
        File existingMock = new File(GENERATED_MOCK_PATH);
        Assert.assertTrue(existingMock.exists(), String.format("Expected mock to be generated at path: %s", existingMock.getPath()));
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }

}
