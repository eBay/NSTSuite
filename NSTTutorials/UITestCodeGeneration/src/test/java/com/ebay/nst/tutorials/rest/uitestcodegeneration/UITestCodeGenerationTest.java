package com.ebay.nst.tutorials.rest.uitestcodegeneration;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.nst.NSTServiceWrapperProcessor;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UITestCodeGenerationTest implements NSTServiceTestRunner {

    private static final String GENERATED_MOCK_PATH = RuntimeConfigManager.getInstance().getIosMocksLocation() + "/UITestCodeGenerationTest_exampleUITestCodeGenerationTest_%d_UITestCodeGenerationWrapper.har";

    @Test
    // Example output is placed in the same directory as this file (mockgenerationtutorial).
    public void exampleUITestCodeGenerationTest() throws Exception {
        // Remove any prior mocks that were generated when running the test.
        File existingMock1 = new File(String.format(GENERATED_MOCK_PATH, 0));
        File existingMock2 = new File(String.format(GENERATED_MOCK_PATH, 1));
        File[] files = new File[]{existingMock1, existingMock2};

        for (File file : files) {
            if (file.exists()) {
                file.delete();
                System.out.printf("Removed existing mock at: %s%n", file.getPath());
            }
        }

        NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

        // Send a GET /api/v1/holidays/{holidayId} request to trigger the ENTRY column being used in the nstToFuiMappingsIos.csv.
        UITestCodeGenerationWrapper restServiceWrapper = new UITestCodeGenerationWrapper(CanadaHoliday.CANADA_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);

        // Send a GET /api/v1/holidays/{holidayId} request to trigger the NAVIGATE column being used in the nstToFuiMappingsIos.csv.
        restServiceWrapper = new UITestCodeGenerationWrapper(CanadaHoliday.CHRISTMAS_DAY);
        serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    }

    @AfterClass
    // This assertion was added to ensure that mock generation output is successful in this tutorial.
    // This is not a required part of writing an NST test.
    public void ensureMockIsGeneratedSuccessfully() throws IOException {
        File swiftFileWithOutput = new File(RuntimeConfigManager.getInstance().getIosMocksLocation() + "/UITestCodeGenerationTest.swift");
        StringBuilder fileReaderOutput = new StringBuilder();
        try (FileReader fileReader = new FileReader(swiftFileWithOutput)) {
            int i;
            while ((i = fileReader.read()) != -1) {
                fileReaderOutput.append((char) i);
            }
        }

        // Ensure that the output exists.
        String expectedOutput = "// CORRESPONDING API CALL: UITestCodeGenerationWrapper";
        Assert.assertTrue(fileReaderOutput.toString().contains(expectedOutput));

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
