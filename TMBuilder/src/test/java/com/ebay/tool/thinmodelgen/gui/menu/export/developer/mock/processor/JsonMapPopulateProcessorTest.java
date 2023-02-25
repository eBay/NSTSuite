package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockListOfValues;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValueLooper;
import com.google.gson.Gson;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class JsonMapPopulateProcessorTest {

    private Gson gson = new Gson();

    @Test
    public void popluateJsonMapFromValidationSet() {

    }

    @Test
    public void popluateJsonMapLeafWithInteger() {
        DeveloperMockValue mockValue = mock(DeveloperMockValue.class);
        when(mockValue.getMockValue()).thenReturn(1);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo", "bar"};

        Map<String, Object> foo = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("foo", foo);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        Map<String, Object> expectedFoo = new HashMap<>();
        expectedFoo.put("bar", 1);
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", foo);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void popluateJsonMapLeafWithBoolean() {
        DeveloperMockValue mockValue = mock(DeveloperMockValue.class);
        when(mockValue.getMockValue()).thenReturn(true);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo", "bar"};

        Map<String, Object> foo = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("foo", foo);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        Map<String, Object> expectedFoo = new HashMap<>();
        expectedFoo.put("bar", true);
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", foo);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void popluateJsonMapLeafWithDouble() {
        DeveloperMockValue mockValue = mock(DeveloperMockValue.class);
        when(mockValue.getMockValue()).thenReturn(12.8);
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo", "bar"};

        Map<String, Object> foo = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("foo", foo);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        Map<String, Object> expectedFoo = new HashMap<>();
        expectedFoo.put("bar", 12.8);
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", foo);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void popluateJsonMapLeafWithString() {
        DeveloperMockValue mockValue = mock(DeveloperMockValue.class);
        when(mockValue.getMockValue()).thenReturn("GOOD");
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo", "bar"};

        Map<String, Object> foo = new HashMap<>();
        Map<String, Object> root = new HashMap<>();
        root.put("foo", foo);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        Map<String, Object> expectedFoo = new HashMap<>();
        expectedFoo.put("bar", "GOOD");
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", foo);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void popluateJsonMapLeafAtEndOfMultiArrayPath() {
        DeveloperMockListOfValues mockValue = mock(DeveloperMockListOfValues.class);
        when(mockValue.getMockValues()).thenReturn(Arrays.asList("ONE", "TWO", "THREE"));
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo[*]", "bar[*]", "leaf"};

        Map<String, Object> firstBar = new HashMap<>();
        firstBar.put("leaf", null);
        Map<String, Object> secondBar = new HashMap<>();
        secondBar.put("leaf", null);
        Map<String, Object> thirdBar = new HashMap<>();
        thirdBar.put("leaf", null);
        List<Map<String, Object>> barList = new ArrayList<>();
        barList.add(firstBar);
        barList.add(secondBar);
        barList.add(thirdBar);
        Map<String, Object> foo = new HashMap<>();
        foo.put("bar", barList);
        List<Map<String, Object>> fooList = new ArrayList<>();
        fooList.add(foo);
        Map<String, Object> root = new HashMap<>();
        root.put("foo", fooList);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        Map<String, Object> expectedFirstBar = new HashMap<>();
        expectedFirstBar.put("leaf", "ONE");
        Map<String, Object> expectedSecondBar = new HashMap<>();
        expectedSecondBar.put("leaf", "TWO");
        Map<String, Object> expectedThirdBar = new HashMap<>();
        expectedThirdBar.put("leaf", "THREE");
        List<Map<String, Object>> expectedBarList = new ArrayList<>();
        expectedBarList.add(expectedFirstBar);
        expectedBarList.add(expectedSecondBar);
        expectedBarList.add(expectedThirdBar);
        Map<String, Object> expectedFoo = new HashMap<>();
        expectedFoo.put("bar", expectedBarList);
        List<Map<String, Object>> expectedFooList = new ArrayList<>();
        expectedFooList.add(expectedFoo);
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", expectedFooList);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void populateJsonArrayLeaf() {
        DeveloperMockListOfValues mockValue = mock(DeveloperMockListOfValues.class);
        when(mockValue.getMockValues()).thenReturn(Arrays.asList("ONE", "TWO", "THREE"));
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo[*]"};

        List<Object> fooList = new ArrayList<>();
        fooList.add(null);
        fooList.add(null);
        fooList.add(null);
        Map<String, Object> root = new HashMap<>();
        root.put("foo", fooList);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        List<Object> expectedFooList = new ArrayList<>();
        expectedFooList.add("ONE");
        expectedFooList.add("TWO");
        expectedFooList.add("THREE");
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", expectedFooList);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void populateJsonArrayLeafAtEndOfMultiArrayPath() {
        DeveloperMockListOfValues mockValue = mock(DeveloperMockListOfValues.class);
        when(mockValue.getMockValues()).thenReturn(Arrays.asList("ONE", "TWO", "THREE"));
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValue);

        String[] jsonPathSteps = new String[] {"$", "foo[*]", "bar[*]"};

        List<Object> firstBarList = new ArrayList<>();
        firstBarList.add("ONE");
        firstBarList.add("TWO");
        firstBarList.add("THREE");
        List<Object> secondBarList = new ArrayList<>();
        secondBarList.add("ONE");
        secondBarList.add("TWO");
        secondBarList.add("THREE");
        Map<String, Object> firstFoo = new HashMap<>();
        firstFoo.put("bar", firstBarList);
        Map<String, Object> secondFoo = new HashMap<>();
        secondFoo.put("bar", secondBarList);
        List<Map<String, Object>> fooList = new ArrayList<>();
        fooList.add(firstFoo);
        fooList.add(secondFoo);
        Map<String, Object> root = new HashMap<>();
        root.put("foo", fooList);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        List<Object> expectedFirstBarList = new ArrayList<>();
        expectedFirstBarList.add("ONE");
        expectedFirstBarList.add("TWO");
        expectedFirstBarList.add("THREE");
        Map<String, Object> expectedFirstFoo = new HashMap<>();
        expectedFirstFoo.put("bar", expectedFirstBarList);

        List<Object> expectedsecondBarList = new ArrayList<>();
        expectedsecondBarList.add("ONE");
        expectedsecondBarList.add("TWO");
        expectedsecondBarList.add("THREE");
        Map<String, Object> expectedSecondFoo = new HashMap<>();
        expectedSecondFoo.put("bar", expectedsecondBarList);

        List<Map<String, Object>> expectedFooList = new ArrayList<>();
        expectedFooList.add(expectedFirstFoo);
        expectedFooList.add(expectedSecondFoo);
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", expectedFooList);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void populateJsonArrayLeafAtEndOfMultiArrayPathAndIncludeExplicitPath() {
        DeveloperMockListOfValues mockValues = mock(DeveloperMockListOfValues.class);
        when(mockValues.getMockValues()).thenReturn(Arrays.asList("ONE", "TWO", "THREE"));
        DeveloperMockValueLooper looper = new DeveloperMockValueLooper(mockValues);

        String[] jsonPathSteps = new String[] {"$", "foo[*]", "bar[*]"};

        List<Object> firstBarList = new ArrayList<>();
        firstBarList.add("ONE");
        firstBarList.add("TWO");
        firstBarList.add("THREE");
        List<Object> secondBarList = new ArrayList<>();
        secondBarList.add("ONE");
        secondBarList.add("TWO");
        secondBarList.add("THREE");
        Map<String, Object> firstFoo = new HashMap<>();
        firstFoo.put("bar", firstBarList);
        Map<String, Object> secondFoo = new HashMap<>();
        secondFoo.put("bar", secondBarList);
        List<Map<String, Object>> fooList = new ArrayList<>();
        fooList.add(firstFoo);
        fooList.add(secondFoo);
        Map<String, Object> root = new HashMap<>();
        root.put("foo", fooList);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        jsonPathSteps = new String[] {"$", "foo[1]", "bar[2]"};
        DeveloperMockValue mockValue = mock(DeveloperMockValue.class);
        when(mockValue.getMockValue()).thenReturn("APPLE");
        looper = new DeveloperMockValueLooper(mockValue);

        JsonMapPopulateProcessor.populateJsonModel(looper, jsonPathSteps, root);

        // Expected
        List<Object> expectedFirstBarList = new ArrayList<>();
        expectedFirstBarList.add("ONE");
        expectedFirstBarList.add("TWO");
        expectedFirstBarList.add("THREE");
        Map<String, Object> expectedFirstFoo = new HashMap<>();
        expectedFirstFoo.put("bar", expectedFirstBarList);

        List<Object> expectedsecondBarList = new ArrayList<>();
        expectedsecondBarList.add("ONE");
        expectedsecondBarList.add("TWO");
        expectedsecondBarList.add("APPLE");
        Map<String, Object> expectedSecondFoo = new HashMap<>();
        expectedSecondFoo.put("bar", expectedsecondBarList);

        List<Map<String, Object>> expectedFooList = new ArrayList<>();
        expectedFooList.add(expectedFirstFoo);
        expectedFooList.add(expectedSecondFoo);
        Map<String, Object> expectedRoot = new HashMap<>();
        expectedRoot.put("foo", expectedFooList);

        String actual = gson.toJson(root);
        String expected = gson.toJson(expectedRoot);

        assertThat(actual, is(equalTo(expected)));
    }
}