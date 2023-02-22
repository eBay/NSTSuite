package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.*;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.SmallestToLargestArrayPathComparator;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.*;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeveloperMockExportTest {

    DeveloperMockExport export;

    @BeforeMethod(alwaysRun = true)
    public void resetDeveloperMockExport() {
        export = new DeveloperMockExport();
    }

    @Test
    public void populateArrayPathToArraySizeMap_WithoutAnyArrayPaths() throws IOException, ClassNotFoundException {

        NodeModel objectModel = getListOfStringNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel[] nodeModels = new NodeModel[] {objectModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> actual = export.getArrayPathToArraySizeMap();
        assertThat("ArrayPathToArraySizeMap MUST be empty.", actual, is(anEmptyMap()));
    }

    @Test
    public void populateArrayPathToArraySizeMap() throws IOException, ClassNotFoundException {

        NodeModel firstModel = getListOfIntegerNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel secondModel = getListOfStringNodeModel("testStringObject", "$.first.second.third[*].name", new ArrayList<>());
        NodeModel thirdModel = getListOfStringNodeModel("secondTestStringObject", "$.first.second.third[5].name", new ArrayList<>());
        NodeModel fourthModel = getListOfStringNodeModel("thirdTestStringObject", "$.first.second.third[*].address[3].city", new ArrayList<>());
        NodeModel fifthModel = getListOfStringNodeModel("fourthTestStringObject", "$.first.second.third[3].name", new ArrayList<>());

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel, fifthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> actual = export.getArrayPathToArraySizeMap();
        TreeMap<String, Integer> expected = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expected.put("$.first.second.third", 6);
        expected.put("$.first.second.third.address", 4);
        assertThat("ArrayPathToArraySizeMap MUST match expected.", actual, is(equalTo(expected)));
    }

    @Test
    public void populateArrayPathToArraySizeMapUsingLargerListOfMockValues() throws IOException, ClassNotFoundException {

        NodeModel firstModel = getListOfIntegerNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel secondModel = getListOfStringNodeModel("testStringObject", "$.first.second.third[2].name", new ArrayList<>());
        NodeModel thirdModel = getListOfStringNodeModel("secondTestStringObject", "$.first.second.third[*].name", new ArrayList<>(Arrays.asList("one", "two", "three", "four")));

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> actual = export.getArrayPathToArraySizeMap();
        TreeMap<String, Integer> expected = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expected.put("$.first.second.third", 4);
        assertThat("ArrayPathToArraySizeMap MUST match expected.", actual, is(equalTo(expected)));
    }

    @Test
    public void populateJsonMapArray() throws IOException, ClassNotFoundException, JSONException {

        NodeModel firstModel = getListOfIntegerNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel secondModel = getListOfStringNodeModel("testStringObject", "$.first.second.third[*].name", new ArrayList<>());
        NodeModel thirdModel = getListOfStringNodeModel("secondTestStringObject", "$.first.second.third[*].address[3].city", new ArrayList<>());
        NodeModel fourthModel = getListOfStringNodeModel("thirdTestStringObject", "$.first.second.alternate[*].foo", new ArrayList<>());

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        Map<String, Object> actualMap = export.getJsonMap();

        // -----------------------------------------------------------------------------------
        // We know the above is working correctly because of populateArrayPathToArraySizeMap().

        // Build up the expected map.
        List<Map<String, Object>> address = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            address.add(new HashMap<>());
        }
        TreeMap<String, Object> addressMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        addressMap.put("address", address);

        List<TreeMap<String, Object>> thirdList = new ArrayList<>();
        thirdList.add(addressMap);

        List<TreeMap<String, Object>> alternateList = new ArrayList<>();
        alternateList.add(new TreeMap<>());

        TreeMap<String, Object> second = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        second.put("alternate", alternateList);
        second.put("third", thirdList);

        TreeMap<String, Object> first = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        first.put("second", second);

        TreeMap<String, Object> expectedMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expectedMap.put("first", first);

        JSONObject actual = new JSONObject(actualMap);
        JSONObject expected = new JSONObject(expectedMap);

        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getCoreValidationSet() throws IOException {

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getValidationSetName()).thenReturn("FOO");

        ValidationSetModel coreValidationSetModel = Mockito.mock(ValidationSetModel.class);
        when(coreValidationSetModel.getValidationSetName()).thenReturn(ExportConstants.CORE_VALIDATION_SET);

        List<ValidationSetModel> validations = new ArrayList<>();
        validations.add(validationSetModel);
        validations.add(coreValidationSetModel);

        ValidationSetModel actual = export.getCoreValidationSet(validations);
        assertThat(actual.getValidationSetName(), is(equalTo(ExportConstants.CORE_VALIDATION_SET)));
        assertThat(validations.size(), is(equalTo(1)));
        assertThat(validations.get(0).getValidationSetName(), is(equalTo("FOO")));
    }

    @DataProvider(name = "doPathStepsContainArrayData")
    public Object[][] doPathStepsContainArrayData() {
        return new Object[][] {
                { new String[] {}, false },
                { null, false },
                { new String[] { "one", "two", "three"}, false },
                { new String[] { "one[1]", "two", "three"}, true },
                { new String[] { "one", "two", "three[*]"}, true },
        };
    }

    @Test(dataProvider = "doPathStepsContainArrayData")
    public void doPathStepsContainArray(String[] pathSteps, boolean expected) {
        boolean actual = export.doPathStepsContainArray(pathSteps);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void jsonMapWithOneArrayAndWildCardPathHasAsManyNodesAsMockValues() throws Exception {

        NodeModel firstModel = getListOfStringNodeModel("testStringObject", "$.first.second.third[*].name", Arrays.asList("bob", "nancy", "jan"));
        NodeModel[] nodeModels = new NodeModel[] {firstModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":{\"third\":[{\"name\":\"bob\"},{\"name\":\"nancy\"},{\"name\":\"jan\"}]}}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithTwoArraysAndWildCardPathsHasAsManyNodesAsMockValues() throws Exception {

        NodeModel firstModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].third[*].name", Arrays.asList("bob", "nancy", "jan"));
        NodeModel[] nodeModels = new NodeModel[] {firstModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"name\":\"bob\"},{\"name\":\"nancy\"},{\"name\":\"jan\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithOneArrayAndWildCardPlusExplicitPathHasAllWildcardMockValuesPlusExplicitMockValueOverride() throws Exception {

        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[0].third[1].name", "Henry");
        NodeModel secondModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].third[*].name", Arrays.asList("bob", "nancy", "jan"));
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"name\":\"bob\"},{\"name\":\"Henry\"},{\"name\":\"jan\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithOneArrayAndWildCardPlusExplicitPathHasAllWildcardMockValuesPlusExplicitMockValueOverrideWhenExplicitPathComesSecond() throws Exception {

        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[0].third[1].name", "Henry");
        NodeModel secondModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].third[*].name", Arrays.asList("bob", "nancy", "jan"));
        NodeModel[] nodeModels = new NodeModel[] {secondModel, firstModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"name\":\"bob\"},{\"name\":\"Henry\"},{\"name\":\"jan\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithOneWildCardArrayIndexWithExplicitChildIndexAndListOfMockValuesPopulatesExplicitNodeInEachParentIndex() throws Exception {

        NodeModel firstModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].third[1].name", Arrays.asList("bob"));
        NodeModel[] nodeModels = new NodeModel[] {firstModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{},{\"name\":\"bob\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithOneArrayAndWildCardPlusExplicitPathToDifferentNodeUnderSameLeafParentHasAllMockValuesUsedPlusOneExplicitLeafNode() throws Exception {

        NodeModel firstModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].name", Arrays.asList("bob", "nancy", "george"));
        NodeModel secondModel = getListOfIntegerNodeModel("testStringObject", "$.first.second[1].age", Arrays.asList(54));
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"name\":\"bob\"},{\"name\":\"nancy\",\"age\":54},{\"name\":\"george\"}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithOneArrayAndTwoExplicitIndexesToSameLeafPropertyName() throws Exception {

        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[1].name", "bob");
        NodeModel secondModel = getStringNodeModel("testStringObject", "$.first.second[0].name", "nancy");
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"name\":\"nancy\"},{\"name\":\"bob\"}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithTwoArraysWithExplicitIndexesInAllCasesToSameLeafPropertyName() throws Exception {

        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[1].third[0].name", "bob");
        NodeModel secondModel = getStringNodeModel("testStringObject", "$.first.second[0].third[1].name", "nancy");
        NodeModel thirdModel = getStringNodeModel("testStringObject", "$.first.second[0].third[0].name", "George");
        NodeModel fourthModel = getStringNodeModel("testStringObject", "$.first.second[1].third[1].name", "Linda");
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"name\":\"George\"},{\"name\":\"nancy\"}]},{\"third\":[{\"name\":\"bob\"},{\"name\":\"Linda\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithTwoArraysWithExplicitIndexesInAllCasesToDifferentLeafPropertyNames() throws Exception {
        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[1].third[0].name", "bob");
        NodeModel secondModel = getIntegerNodeModel("testStringObject", "$.first.second[0].third[1].age", 54);
        NodeModel thirdModel = getBooleanNodeModel("testStringObject", "$.first.second[0].third[0].student", true);
        NodeModel fourthModel = getDoubleNodeModel("testStringObject", "$.first.second[1].third[1].gpa", 4.1);
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"student\":true},{\"age\":54}]},{\"third\":[{\"name\":\"bob\"},{\"gpa\":4.1}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithTwoArraysWithExplicitIndexesInAllCasesToSameLeafPropertyNamePlusWildcardMockValuesForSameLeafProperty() throws Exception {

        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[1].third[0].name", "bob");
        NodeModel secondModel = getStringNodeModel("testStringObject", "$.first.second[0].third[1].name", "nancy");
        NodeModel thirdModel = getStringNodeModel("testStringObject", "$.first.second[0].third[0].name", "George");
        NodeModel fourthModel = getStringNodeModel("testStringObject", "$.first.second[1].third[1].name", "Linda");
        NodeModel fifthModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].third[*].name", Arrays.asList("DO", "NOT"));
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"name\":\"George\"},{\"name\":\"nancy\"}]},{\"third\":[{\"name\":\"bob\"},{\"name\":\"Linda\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void jsonMapWithTwoArraysWithExplicitIndexesInAllCasesButOneToSameLeafPropertyNamePlusWildcardMockValuesForSameLeafProperty() throws Exception {

        NodeModel firstModel = getStringNodeModel("testStringObject", "$.first.second[1].third[0].name", "bob");
        NodeModel secondModel = getStringNodeModel("testStringObject", "$.first.second[0].third[1].name", "nancy");
        NodeModel thirdModel = getStringNodeModel("testStringObject", "$.first.second[1].third[1].name", "Linda");
        NodeModel fourthModel = getListOfStringNodeModel("testStringObject", "$.first.second[*].third[*].name", Arrays.asList("USE", "NOT"));
        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"third\":[{\"name\":\"USE\"},{\"name\":\"nancy\"}]},{\"third\":[{\"name\":\"bob\"},{\"name\":\"Linda\"}]}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void booleanMocks() throws Exception {

        NodeModel firstModel = getListOfBooleanNodeModel("testStringObject", "$.first.second[*].on", Arrays.asList(true, false, true));
        NodeModel[] nodeModels = new NodeModel[] {firstModel,};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"on\":true},{\"on\":false},{\"on\":true}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void integerMocks() throws Exception {

        NodeModel firstModel = getListOfIntegerNodeModel("testStringObject", "$.first.second[*].val", Arrays.asList(1,2,3));
        NodeModel[] nodeModels = new NodeModel[] {firstModel,};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"val\":1},{\"val\":2},{\"val\":3}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void doubleMocks() throws Exception {

        NodeModel firstModel = getListOfDoubleNodeModel("testStringObject", "$.first.second[*].val", Arrays.asList(1.5,2.4,3.3));
        NodeModel[] nodeModels = new NodeModel[] {firstModel,};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":[{\"val\":1.5},{\"val\":2.4},{\"val\":3.3}]}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    @Test
    public void populateJsonMapWithMixedPathsAndValues() throws IOException, ClassNotFoundException {

        NodeModel firstModel = getListOfStringNodeModel("testStringObject", "$.first.second.third[*].name", Arrays.asList("bob", "nancy", "jan"));
        NodeModel secondModel = getListOfStringNodeModel("secondTestStringObject", "$.first.second.third[*].address[3].city", Arrays.asList("Chicago", "New York", "Portland"));
        NodeModel thirdModel = getListOfIntegerNodeModel("thirdTestStringObject", "$.first.second.alternate[*].foo", Arrays.asList(1, 2, 3));

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        export.populateJsonMapWithMockValues(validationSetModel);
        Map<String, Object> actualMap = export.getJsonMap();
        JSONObject jsonObject = new JSONObject(actualMap);
        String actualJson = jsonObject.toString();
        String expectedJson = "{\"first\":{\"second\":{\"third\":[{\"address\":[{},{},{},{\"city\":\"Chicago\"}],\"name\":\"bob\"},{\"address\":[{},{},{},{\"city\":\"New York\"}],\"name\":\"nancy\"},{\"address\":[{},{},{},{\"city\":\"Portland\"}],\"name\":\"jan\"}],\"alternate\":[{\"foo\":1},{\"foo\":2},{\"foo\":3}]}}}";
        assertThat(actualJson, is(equalTo(expectedJson)));
    }

    // ---------------------------------

    private NodeModel getStringNodeModel(String nodeName, String jsonPath, String mockValue) throws IOException {
        JsonStringType type = new JsonStringType(nodeName);
        type.updatePath("", jsonPath);
        TMJPStringCheck typeCheck = new TMJPStringCheck();
        typeCheck.setMockValue(mockValue);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getListOfStringNodeModel(String nodeName, String jsonPath, List<String> mockValues) throws IOException {
        JsonStringType type = new JsonStringType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfStringCheck typeCheck = new TMJPListOfStringCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getIntegerNodeModel(String nodeName, String jsonPath, int mockValues) throws IOException {
        JsonIntegerType type = new JsonIntegerType(nodeName);
        type.updatePath("", jsonPath);
        TMJPIntegerCheck typeCheck = new TMJPIntegerCheck();
        typeCheck.setMockValue(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getListOfIntegerNodeModel(String nodeName, String jsonPath, List<Integer> mockValues) throws IOException {
        JsonIntegerType type = new JsonIntegerType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfIntegerCheck typeCheck = new TMJPListOfIntegerCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getListOfBooleanNodeModel(String nodeName, String jsonPath, List<Boolean> mockValues) throws IOException {
        JsonBooleanType type = new JsonBooleanType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfBooleanCheck typeCheck = new TMJPListOfBooleanCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getBooleanNodeModel(String nodeName, String jsonPath, boolean mockValues) throws IOException {
        JsonBooleanType type = new JsonBooleanType(nodeName);
        type.updatePath("", jsonPath);
        TMJPBooleanCheck typeCheck = new TMJPBooleanCheck();
        typeCheck.setMockValue(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getListOfDoubleNodeModel(String nodeName, String jsonPath, List<Double> mockValues) throws IOException {
        JsonFloatType type = new JsonFloatType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfDoubleCheck typeCheck = new TMJPListOfDoubleCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    private NodeModel getDoubleNodeModel(String nodeName, String jsonPath, double mockValues) throws IOException {
        JsonFloatType type = new JsonFloatType(nodeName);
        type.updatePath("", jsonPath);
        TMJPDoubleCheck typeCheck = new TMJPDoubleCheck();
        typeCheck.setMockValue(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }
}