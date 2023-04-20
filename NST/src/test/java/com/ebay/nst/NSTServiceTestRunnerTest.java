package com.ebay.nst;

import com.ebay.nst.hosts.manager.PoolType;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.runtime.arguments.PlatformArgument;
import com.ebay.runtime.arguments.PoolTypeArgument;
import com.ebay.softassert.EbaySoftAssert;
import org.testng.annotations.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class NSTServiceTestRunnerTest implements NSTServiceTestRunner {

    @Override
    public EbaySoftAssert getSoftAssert() {
        return null;
    }

    // Do this as cleanup - not part of the test evaluations
    @AfterClass
    public void doLast() {
        RuntimeConfigManager.getInstance().reinitialize();
    }

    @Test
    public void testCaseOverridePoolType() {

        PoolType poolType = RuntimeConfigManager.getInstance().getPoolType();
        assertThat(poolType, is(equalTo(PoolType.QA)));
        PoolTypeArgument poolTypeArgument = (PoolTypeArgument) RuntimeConfigManager.getInstance().getRuntimeArgument(PoolTypeArgument.KEY);
        poolTypeArgument.override(PoolType.PROD);
        poolType = RuntimeConfigManager.getInstance().getPoolType();
        assertThat(poolType, is(equalTo(PoolType.PROD)));
    }

    // Follows up after testCaseOverridePoolType() has run and confirms we are reset to QA poo.
    // No other tests should use the PoolType argument besides these two test cases.
    @Test(dependsOnMethods = { "testCaseOverridePoolType" })
    public void testCaseOverridePoolTypeVerifyReset() {
        PoolType poolType = RuntimeConfigManager.getInstance().getPoolType();
        assertThat(poolType, is(equalTo(PoolType.QA)));
    }

    @BeforeMethod
    public void beforeMethodOverride() {
        Platform platform = RuntimeConfigManager.getInstance().getPlatform();
        assertThat(platform, is(equalTo(Platform.IOS)));
        PlatformArgument argument = (PlatformArgument) RuntimeConfigManager.getInstance().getRuntimeArgument(PlatformArgument.KEY);
        argument.override(Platform.ANDROID);
    }

    @Test
    public void testBeforeMethodOverride() {
        Platform platform = RuntimeConfigManager.getInstance().getPlatform();
        assertThat(platform, is(equalTo(Platform.ANDROID)));
    }

    @Test
    public void testBeforeMethodOverrideSecondTime() {
        Platform platform = RuntimeConfigManager.getInstance().getPlatform();
        assertThat(platform, is(equalTo(Platform.ANDROID)));
    }
}