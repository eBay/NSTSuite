package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.google.gson.Gson;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.*;

public class JsonMapStructureProcessorTest {

    private JsonMapStructureProcessor processor = new JsonMapStructureProcessor();
    private Gson gson = new Gson();

    @Test
    public void testGetJsonMapForValidations() {
    }

    @Test
    public void testProcessNodeModels() {
    }

    @Test
    public void testProcessJsonBaseTypeCheckPaths() {
    }

    @Test
    public void setupJsonMapNonArrayPath() {
        String[] path = new String[] {"$", "foo", "bar"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", new Object());
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapNonArrayPathWithSiblingTrees() {

        // First path
        String[] path = new String[] {"$", "foo", "bar"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        // Second path
        path = new String[] {"$", "foo", "fuzzy", "wuzzy"};
        traversedPath = new ArrayList<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        // Define expected
        HashMap<String, Object> fuzzy = new HashMap<>();
        fuzzy.put("wuzzy", new Object());
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", new Object());
        foo.put("fuzzy", fuzzy);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapWithArrayInPath() {
        String[] path = new String[] {"$", "foo[*]", "bar"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 1);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        HashMap<String, Object> fooObject = new HashMap<>();
        fooObject.put("bar", new Object());
        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        fooList.add(fooObject);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapWithMultiIndexArrayInPath() {
        String[] path = new String[] {"$", "foo[*]", "bar"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 3);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        HashMap<String, Object> fooObject = new HashMap<>();
        fooObject.put("bar", new Object());
        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        fooList.add(fooObject);
        fooList.add(fooObject);
        fooList.add(fooObject);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapWithTwoArraysInPath() {
        String[] path = new String[] {"$", "foo[*]", "bar[*]", "end"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 1);
        treeMap.put("$.foo.bar", 1);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        HashMap<String, Object> barObject = new HashMap<>();
        barObject.put("end", new Object());
        List<HashMap<String, Object>> barList = new ArrayList<HashMap<String, Object>>();
        barList.add(barObject);
        HashMap<String, Object> fooObject = new HashMap<>();
        fooObject.put("bar", barList);
        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        fooList.add(fooObject);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapWithArrayAtEndOfPath() {
        String[] path = new String[] {"$", "foo[*]"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 1);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapWithTwoArraysAtEndOfPath() {
        String[] path = new String[] {"$", "foo[*]", "bar[*]"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 1);
        treeMap.put("$.foo.bar", 1);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        List<HashMap<String, Object>> barList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> fooObject = new HashMap<>();
        fooObject.put("bar", barList);
        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        fooList.add(fooObject);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void multipleIterationsOfSameJsonMapPassedBackToSetupJsonMapWithArrays() {
        // First array path
        String[] path = new String[] {"$", "foo[*]", "bar[*]", "end"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 1);
        treeMap.put("$.foo.bar", 1);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        // Second array path
        path = new String[] {"$", "foo[*]", "who[*]", "fan"};
        traversedPath = new ArrayList<>();
        treeMap.put("$.foo", 1);
        treeMap.put("$.foo.bar", 1);
        treeMap.put("$.foo.who", 1);
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        // Setup expected
        HashMap<String, Object> barObject = new HashMap<>();
        barObject.put("end", new Object());
        List<HashMap<String, Object>> barList = new ArrayList<HashMap<String, Object>>();
        barList.add(barObject);

        HashMap<String, Object> whoObject = new HashMap<>();
        whoObject.put("fan", new Object());
        List<HashMap<String, Object>> whoList = new ArrayList<HashMap<String, Object>>();
        whoList.add(whoObject);

        HashMap<String, Object> fooObject = new HashMap<>();
        fooObject.put("bar", barList);
        fooObject.put("who", whoList);
        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        fooList.add(fooObject);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }
}