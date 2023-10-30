package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.jsonpath.TMJPListOfDoubleCheck;
import com.ebay.jsonpath.TMJPStringCheck;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.PathNode;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import javax.validation.Valid;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class JsonMapStructureProcessorTest {

    private JsonMapStructureProcessor processor = new JsonMapStructureProcessor();
    private Gson gson = new GsonBuilder().serializeNulls().create();;

    @Test
    public void testGetJsonMapForValidationsCoreAndNullCustomValues() throws Exception {

        JsonBooleanType jsonBooleanType = new JsonBooleanType("Foo");
        jsonBooleanType.updateCheckForPath("$.foo.bar", new TMJPStringCheck());
        String booleanSerializedType = JsonBaseTypePersistence.serialize(jsonBooleanType);

        PathNode[] booleanPathNodes = new PathNode[4];
        booleanPathNodes[0] = new PathNode("$", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        booleanPathNodes[1] = new PathNode("foo", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        booleanPathNodes[3] = new PathNode("bar", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType", 0);

        NodeModel booleanNodeModel = new NodeModel(booleanPathNodes, booleanSerializedType);
        NodeModel[] coreNodeModel = new NodeModel[] {booleanNodeModel};
        ValidationSetModel coreValidationSet = new ValidationSetModel("core", coreNodeModel);

        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());

        Map actualJsonMap = processor.getJsonMapForValidations(treeMap, coreValidationSet, null);

        // Define expected
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testGetJsonMapForValidationsCoreAndCustom() throws Exception {

        JsonBooleanType jsonBooleanType = new JsonBooleanType("Foo");
        jsonBooleanType.updateCheckForPath("$.foo.bar", new TMJPStringCheck());
        String booleanSerializedType = JsonBaseTypePersistence.serialize(jsonBooleanType);

        PathNode[] booleanPathNodes = new PathNode[4];
        booleanPathNodes[0] = new PathNode("$", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        booleanPathNodes[1] = new PathNode("foo", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        booleanPathNodes[3] = new PathNode("bar", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType", 0);

        NodeModel booleanNodeModel = new NodeModel(booleanPathNodes, booleanSerializedType);

        JsonStringType stringType = new JsonStringType("Foo");
        stringType.updateCheckForPath("$.foo.fuzzy", new TMJPStringCheck());
        String stringSerializedType = JsonBaseTypePersistence.serialize(stringType);

        PathNode[] stringPathNodes = new PathNode[4];
        stringPathNodes[0] = new PathNode("$", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        stringPathNodes[1] = new PathNode("foo", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        stringPathNodes[3] = new PathNode("fuzzy", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType", 0);

        NodeModel stringNodeModel = new NodeModel(stringPathNodes, stringSerializedType);

        NodeModel[] coreNodeModel = new NodeModel[] {booleanNodeModel};
        NodeModel[] customNodeModel = new NodeModel[] {stringNodeModel};

        ValidationSetModel coreValidationSet = new ValidationSetModel("core", coreNodeModel);
        ValidationSetModel customValidationSet = new ValidationSetModel("custom", customNodeModel);

        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());

        Map actualJsonMap = processor.getJsonMapForValidations(treeMap, coreValidationSet, customValidationSet);

        // Define expected
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
        foo.put("fuzzy", null);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testGetJsonMapForValidationsCoreOnly() throws Exception {

        JsonBooleanType jsonBooleanType = new JsonBooleanType("Foo");
        jsonBooleanType.updateCheckForPath("$.foo.bar", new TMJPStringCheck());
        String booleanSerializedType = JsonBaseTypePersistence.serialize(jsonBooleanType);

        PathNode[] booleanPathNodes = new PathNode[4];
        booleanPathNodes[0] = new PathNode("$", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        booleanPathNodes[1] = new PathNode("foo", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
        booleanPathNodes[3] = new PathNode("bar", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType", 0);

        NodeModel booleanNodeModel = new NodeModel(booleanPathNodes, booleanSerializedType);

        NodeModel[] coreNodeModel = new NodeModel[] {booleanNodeModel};

        ValidationSetModel coreValidationSet = new ValidationSetModel("core", coreNodeModel);

        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());

        Map actualJsonMap = processor.getJsonMapForValidations(treeMap, coreValidationSet, coreValidationSet);

        // Define expected
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testProcessNodeModels() throws Exception {

        JsonBooleanType jsonBooleanType = new JsonBooleanType("Foo");
        jsonBooleanType.updateCheckForPath("$.foo.bar", new TMJPStringCheck());

        TMJPListOfDoubleCheck listOfDoubleCheck = new TMJPListOfDoubleCheck();
        listOfDoubleCheck.setMockValues(Arrays.asList(2.0, 1.0));
        jsonBooleanType.updateCheckForPath("$.foo.fuzzy[*].wuzzy", listOfDoubleCheck);
        String serializedType = JsonBaseTypePersistence.serialize(jsonBooleanType);
        NodeModel nodeModel = mock(NodeModel.class);
        when(nodeModel.getSerializedUserObject()).thenReturn(serializedType);

        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo.fuzzy", 2);
        HashMap<String, Object> actualJsonMap = new HashMap<>();

        processor.processNodeModels(new NodeModel[] {nodeModel}, treeMap, actualJsonMap);

        // Define expected
        HashMap<String, Object> fuzzyMap = new HashMap<>();
        fuzzyMap.put("wuzzy", null);
        List<Map<String, Object>> fuzzyList = new ArrayList<>();
        fuzzyList.add(fuzzyMap);
        fuzzyList.add(fuzzyMap);
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
        foo.put("fuzzy", fuzzyList);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testProcessJsonBaseTypeCheckPaths() {

        JsonBaseType jsonBaseType = mock(JsonBaseType.class);
        when(jsonBaseType.getSavedPathsForNode()).thenReturn(new String[] {"$.foo.bar", "$.foo.fuzzy[*].wuzzy"});

        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo.fuzzy", 2);
        HashMap<String, Object> actualJsonMap = new HashMap<>();

        processor.processJsonBaseTypeCheckPaths(jsonBaseType, treeMap, actualJsonMap);

        // Define expected
        HashMap<String, Object> fuzzyMap = new HashMap<>();
        fuzzyMap.put("wuzzy", null);
        List<Map<String, Object>> fuzzyList = new ArrayList<>();
        fuzzyList.add(fuzzyMap);
        fuzzyList.add(fuzzyMap);
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
        foo.put("fuzzy", fuzzyList);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", foo);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapNonArrayPath() {
        String[] path = new String[] {"$", "foo", "bar"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
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
        fuzzy.put("wuzzy", null);
        HashMap<String, Object> foo = new HashMap<>();
        foo.put("bar", null);
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
        fooObject.put("bar", null);
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
        fooObject.put("bar", null);
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
        barObject.put("end", null);
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

        List<Object> fooList = new ArrayList<>();
        fooList.add(null);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setupJsonMapWithArrayAtEndOfPathAndMultipleIndexes() {
        String[] path = new String[] {"$", "foo[*]"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 3);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        List<Object> fooList = new ArrayList<>();
        fooList.add(null);
        fooList.add(null);
        fooList.add(null);
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
        barList.add(null);
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
        barObject.put("end", null);
        List<HashMap<String, Object>> barList = new ArrayList<HashMap<String, Object>>();
        barList.add(barObject);

        HashMap<String, Object> whoObject = new HashMap<>();
        whoObject.put("fan", null);
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

    @Test
    public void multipleAppendingToTraversedStepsList() throws Exception {

        // True path this was happening for: $.modules.summary.total.amount.textSpans[1].styles[0]
        // TextSpans array was size 3. Styles size 1.

        // First array path
        String[] path = new String[] {"$", "foo[1]", "bar[0]"};
        List<String> traversedPath = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        treeMap.put("$.foo", 3);
        treeMap.put("$.foo.bar", 1);
        HashMap<String, Object> actualJsonMap = new HashMap<>();
        processor.setupJsonMap(path, traversedPath, treeMap, actualJsonMap);

        // Setup expected
        List<Object> firstBarList = new ArrayList<>();
        firstBarList.add(null);

        List<Object> secondBarList = new ArrayList<>();
        secondBarList.add(null);

        List<Object> thirdBarList = new ArrayList<>();
        thirdBarList.add(null);

        HashMap<String, Object> firstFooObject = new HashMap<>();
        firstFooObject.put("bar", firstBarList);

        HashMap<String, Object> secondFooObject = new HashMap<>();
        secondFooObject.put("bar", secondBarList);

        HashMap<String, Object> thirdFooObject = new HashMap<>();
        thirdFooObject.put("bar", thirdBarList);

        List<HashMap<String, Object>> fooList = new ArrayList<HashMap<String, Object>>();
        fooList.add(firstFooObject);
        fooList.add(secondFooObject);
        fooList.add(thirdFooObject);
        HashMap<String, Object> expectedJsonMap = new HashMap<>();
        expectedJsonMap.put("foo", fooList);

        String actual = gson.toJson(actualJsonMap);
        String expected = gson.toJson(expectedJsonMap);

        assertThat(actual, is(equalTo(expected)));
    }
}