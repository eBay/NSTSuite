package com.ebay.nst.tutorials.rest.runtimearguments;

import com.ebay.nst.NSTServiceTestRunner;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.RuntimeConfigValue;
import com.ebay.runtime.arguments.IosMocksLocationArgument;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RuntimeArgumentsTest implements NSTServiceTestRunner {

    @Test
    public void exampleOverrideExistingRuntimeArgumentTest() {
        String initialRuntimeArgumentValue = RuntimeConfigManager.getInstance().getIosMocksLocation();
        System.out.printf("Initial IosMocksLocation runtime argument value: %s%n", initialRuntimeArgumentValue);

        // Override the IosMocksLocation existing runtime argument value
        String modifiedValue = "modifiedValue";
        RuntimeConfigValue<String> iosMocksLocationConfigValue =
                (RuntimeConfigValue<String>) RuntimeConfigManager.getInstance().getRuntimeArgument(IosMocksLocationArgument.KEY);
        String newRuntimeArgumentvalue = iosMocksLocationConfigValue.override(modifiedValue);

        Assert.assertEquals(RuntimeConfigManager.getInstance().getIosMocksLocation(), newRuntimeArgumentvalue);
        System.out.printf("Post-modification IosMocksLocation runtime argument value: %s%n", newRuntimeArgumentvalue);
    }

    @Test
    public void exampleOverrideCustomRuntimeArgumentTest() {
        // Add a custom runtime argument
        RuntimeArgumentsCustomExample customRuntimeArgumentExample = new RuntimeArgumentsCustomExample();
        RuntimeConfigManager.getInstance().addRuntimeArgument(customRuntimeArgumentExample);

        String initialRuntimeArgumentValue = RuntimeConfigManager.getInstance().getRuntimeArgumentValue(RuntimeArgumentsCustomExample.KEY).toString();
        System.out.printf("Initial custom runtime argument value: %s%n", initialRuntimeArgumentValue);

        // Override the custom runtime argument value
        String modifiedValue = "modifiedValue";
        RuntimeConfigValue<String> customRuntimeArgumentExampleValue =
                (RuntimeConfigValue<String>) RuntimeConfigManager.getInstance().getRuntimeArgument(RuntimeArgumentsCustomExample.KEY);
        String newCustomRuntimeArgumentvalue = customRuntimeArgumentExampleValue.override(modifiedValue);

        Assert.assertEquals(RuntimeConfigManager.getInstance().getRuntimeArgumentValue(RuntimeArgumentsCustomExample.KEY), newCustomRuntimeArgumentvalue);
        System.out.printf("Post-modification custom runtime argument value: %s%n", newCustomRuntimeArgumentvalue);
    }

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }
}
