package com.ebay.runtime.arguments;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.*;

public class UseNstDefaultMockLoggerTest {

    private UseNstDefaultMockLogger logger = new UseNstDefaultMockLogger();

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void overrideNotPermitted() {
        logger.override(true);
    }

    @DataProvider(name = "testValues")
    public Object[][] testValues() {
        return new Object[][] {
                { null, false },
                { "true", true },
                { "false", false },
                { "yes", false },
                { "no", false }
        };
    }

    @Test(dataProvider = "testValues")
    public void parseValuesAsExpected(String input, boolean expected) {
        logger.parseRuntimeArgument(input);
        Boolean actual = logger.getRuntimeArgumentValue();
        assertThat(actual, is(equalTo(expected)));
    }
}