package com.nst.tutorials.rest.runtimearguments;

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

public class RuntimeArgumentsTest implements NSTServiceTestRunner {

    @Test
    public void exampleRuntimeArgumentsTest() throws Exception {
        // Add a custom runtime argument
        RuntimeArgumentsCustomExample customRuntimeArgumentExample = new RuntimeArgumentsCustomExample();
        RuntimeConfigManager.getInstance().addRuntimeArgument(customRuntimeArgumentExample);

        String initialRuntimeArgumentValue = RuntimeConfigManager.getInstance().getRuntimeArgumentValue(customRuntimeArgumentExample.getRuntimeArgumentKey()).toString();
        System.out.printf("Initial custom runtime argument value: %s%n", initialRuntimeArgumentValue);

        // Override the custom runtime argument value
        RuntimeConfigValue<String> customRuntimeArgumentExampleValue =
                (RuntimeConfigValue<String>) RuntimeConfigManager.getInstance().getRuntimeArgument(customRuntimeArgumentExample.getRuntimeArgumentKey());
        customRuntimeArgumentExampleValue.override("modifiedValue");

        String newRuntimeArgumentValue = RuntimeConfigManager.getInstance().getRuntimeArgumentValue(customRuntimeArgumentExample.getRuntimeArgumentKey()).toString();
        System.out.printf("Post-override custom runtime argument value: %s%n", newRuntimeArgumentValue);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }
}
