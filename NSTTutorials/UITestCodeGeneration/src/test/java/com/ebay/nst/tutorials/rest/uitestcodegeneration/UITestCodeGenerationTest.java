package com.ebay.nst.tutorials.rest.uitestcodegeneration;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.GenericServiceWrapper;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class UITestCodeGenerationTest implements NSTServiceTestRunner {

    private static final String GENERATED_MOCK_PATH = RuntimeConfigManager.getInstance().getIosMocksLocation() + "/UITestCodeGenerationTest_exampleUITestCodeGenerationTest_%d_GenericServiceWrapper.har";

    @BeforeClass
    // Remove any prior mocks that were generated when running the test method below.
    public void removeExistingMocks() {
        File existingMock1 = new File(String.format(GENERATED_MOCK_PATH, 0));
        File existingMock2 = new File(String.format(GENERATED_MOCK_PATH, 1));
        File[] files = new File[]{existingMock1, existingMock2};

        for (File file : files) {
            if (file.exists()) {
                file.delete();
                System.out.printf("Removed existing mock at: %s%n", file.getPath());
            }
        }
    }

    @Test
    // Example output is placed in the same directory as this file (mockgenerationtutorial).
    public void exampleUITestCodeGenerationTest() throws Exception {
        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request to trigger the ENTRY column being used in the nstToFuiMappingsIos.csv.
        GenericServiceWrapper restServiceWrapper = new GenericServiceWrapper(CanadaHoliday.CANADA_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);

        // Send a GET /api/v1/holidays/{holidayId} request to trigger the NAVIGATE column being used in the nstToFuiMappingsIos.csv.
        restServiceWrapper = new GenericServiceWrapper(CanadaHoliday.CHRISTMAS_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @AfterClass
    // This assertion was added to ensure that mock generation output is successful in this tutorial.
    // This is not a required part of writing an NST test.
    public void ensureMockIsGeneratedSuccessfully() {
        File existingMock1 = new File(String.format(GENERATED_MOCK_PATH, 0));
        File existingMock2 = new File(String.format(GENERATED_MOCK_PATH, 1));
        File[] files = new File[]{existingMock1, existingMock2};

        for (File file : files) {
            Assert.assertTrue(file.exists(), String.format("Expected mock to be generated at path: %s", file.getPath()));
        }
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }

}
