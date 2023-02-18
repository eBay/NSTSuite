package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.*;

public class DeveloperMockValueLooperTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void getNextMockValueFromSingleMockValueInitializeAsNull() {
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(null);
    }
    @Test
    public void getNextMockValueFromSingleMockValue() {
        String expected = "TestValue";
        DeveloperMockValue value = Mockito.mock(DeveloperMockValue.class);
        Mockito.when(value.getMockValue()).thenReturn(expected);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(value);
        Object next = looper.getNextMockValue();
        assertThat(next, is(equalTo(expected)));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getNextMockValueFromSingleMockValueWithNullValue() {
        DeveloperMockValue value = Mockito.mock(DeveloperMockValue.class);
        Mockito.when(value.getMockValue()).thenReturn(null);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(value);
    }

    @Test
    public void getNextMockValueFromSingleMockValueLoopingTest() {
        String expected = "TestValue";
        DeveloperMockValue value = Mockito.mock(DeveloperMockValue.class);
        Mockito.when(value.getMockValue()).thenReturn(expected);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(value);
        Object next = looper.getNextMockValue();
        assertThat(next, is(equalTo(expected)));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(expected)));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(expected)));
    }

    @Test
    public void getNextMockValueFromMultipleMockValues() {
        List<String> values = Arrays.asList("one", "two", "three");
        DeveloperMockListOfValues value = Mockito.mock(DeveloperMockListOfValues.class);
        Mockito.when(value.getMockValues()).thenReturn(values);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(value);
        Object next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(0))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(1))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(2))));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getNextMockValueFromMultipleMockValuesWithNullList() {
        DeveloperMockListOfValues value = Mockito.mock(DeveloperMockListOfValues.class);
        Mockito.when(value.getMockValues()).thenReturn(null);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(value);
    }

    @Test
    public void getNextMockValueFromMultipleMockValuesLoopingTest() {
        List<String> values = Arrays.asList("one", "two", "three");
        DeveloperMockListOfValues value = Mockito.mock(DeveloperMockListOfValues.class);
        Mockito.when(value.getMockValues()).thenReturn(values);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(value);
        Object next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(0))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(1))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(2))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(0))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(1))));
        next = looper.getNextMockValue();
        assertThat(next, is(equalTo(values.get(2))));
    }
}