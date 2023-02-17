package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.TMJPBooleanCheck;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockExport;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.SmallestToLargestArrayPathComparator;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.*;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Array;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class DeveloperMockExportTest {

    DeveloperMockExport export;

    @BeforeMethod(alwaysRun = true)
    public void resetDeveloperMockExport() {
        export = new DeveloperMockExport();
    }

    @Test
    public void populateArrayPathToArraySizeMap_WithoutAnyArrayPaths() throws IOException, ClassNotFoundException {

        JsonObjectType firstObjectType = new JsonObjectType("testObject");
        firstObjectType.updatePath("", "$.first.second");
        String serializedJsonPathExecutorInstanceData = JsonBaseTypePersistence.serialize(firstObjectType);
        NodeModel objectModel = new NodeModel(null, serializedJsonPathExecutorInstanceData);

        NodeModel[] nodeModels = new NodeModel[] {objectModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> actual = export.getArrayPathToArraySizeMap();
        assertThat("ArrayPathToArraySizeMap MUST be empty.", actual, is(anEmptyMap()));
    }

    @Test
    public void populateArrayPathToArraySizeMap() throws IOException, ClassNotFoundException {

        JsonIntegerType firstType = new JsonIntegerType("testObject");
        firstType.updatePath("", "$.first.second");
        String serializedFirstType = JsonBaseTypePersistence.serialize(firstType);
        NodeModel firstModel = new NodeModel(null, serializedFirstType);

        JsonStringType secondType = new JsonStringType("testStringObject");
        secondType.updatePath("", "$.first.second.third[*].name");
        String serializedSecondType = JsonBaseTypePersistence.serialize(secondType);
        NodeModel secondModel = new NodeModel(null, serializedSecondType);

        JsonStringType thirdType = new JsonStringType("secondTestStringObject");
        thirdType.updatePath("", "$.first.second.third[5].name");
        String serializedThirdType = JsonBaseTypePersistence.serialize(thirdType);
        NodeModel thirdModel = new NodeModel(null, serializedThirdType);

        JsonStringType fourthType = new JsonStringType("secondTestStringObject");
        fourthType.updatePath("", "$.first.second.third[*].address[3].city");
        String serializedFourthType = JsonBaseTypePersistence.serialize(fourthType);
        NodeModel fourthModel = new NodeModel(null, serializedFourthType);

        JsonStringType fifthType = new JsonStringType("secondTestStringObject");
        fifthType.updatePath("", "$.first.second.third[3].name");
        String serializedFifthType = JsonBaseTypePersistence.serialize(fifthType);
        NodeModel fifthModel = new NodeModel(null, serializedFifthType);

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel, fifthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> actual = export.getArrayPathToArraySizeMap();
        TreeMap<String, Integer> expected = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expected.put("$.first.second.third", 5);
        expected.put("$.first.second.third.address", 3);
        assertThat("ArrayPathToArraySizeMap MUST match expected.", actual, is(equalTo(expected)));
    }

    @Test
    public void populateJsonMapArray() throws IOException, ClassNotFoundException, JSONException {

        JsonIntegerType firstType = new JsonIntegerType("testObject");
        firstType.updatePath("", "$.first.second");
        String serializedFirstType = JsonBaseTypePersistence.serialize(firstType);
        NodeModel firstModel = new NodeModel(null, serializedFirstType);

        JsonStringType secondType = new JsonStringType("testStringObject");
        secondType.updatePath("", "$.first.second.third[*].name");
        String serializedSecondType = JsonBaseTypePersistence.serialize(secondType);
        NodeModel secondModel = new NodeModel(null, serializedSecondType);

        JsonStringType thirdType = new JsonStringType("secondTestStringObject");
        thirdType.updatePath("", "$.first.second.third[*].address[3].city");
        String serializedThirdType = JsonBaseTypePersistence.serialize(thirdType);
        NodeModel thirdModel = new NodeModel(null, serializedThirdType);

        JsonStringType fourthType = new JsonStringType("secondTestStringObject");
        fourthType.updatePath("", "$.first.second.alternate[*].foo");
        String serializedFourthType = JsonBaseTypePersistence.serialize(fourthType);
        NodeModel fourthModel = new NodeModel(null, serializedFourthType);

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel};

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        export.populateArrayPathToArraySizeMap(validationSetModel);
        export.populateJsonMapArrays();
        Map<String, Object> actualMap = export.getJsonMap();

        // -----------------------------------------------------------------------------------
        // We know the above is working correctly because of populateArrayPathToArraySizeMap().

        // Build up the expected map.
        List<Map<String, Object>> address = new ArrayList<>(3);
        TreeMap<String, Object> addressMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        addressMap.put("address", address);

        List<TreeMap<String, Object>> thirdList = new ArrayList<>(1);
        thirdList.add(addressMap);

        List<TreeMap<String, Object>> alternateList = new ArrayList<>(1);

        TreeMap<String, Object> second = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        second.put("alternate", alternateList);
        second.put("third", thirdList);

        TreeMap<String, Object> first = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        first.put("second", second);

        TreeMap<String, Object> expectedMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expectedMap.put("first", first);

        JSONObject actual = new JSONObject(actualMap);
        JSONObject expected = new JSONObject(expectedMap);

        System.out.println("actual");
        System.out.println(actual.toString());

        System.out.println("expected");
        System.out.println(expected.toString());

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












//    @Test
//    public void testPopulateJsonModelWithBoolean() {
//        TMJPBooleanCheck check = new TMJPBooleanCheck();
//        check.setMockValue(true);
//        export.populateJsonModel(check, "$.test.foo");
//        Map<String, Object> jsonMap = export.getJson();
//        JSONObject jsonObject = new JSONObject(jsonMap);
//        assertThat(jsonObject.toString(), is(Matchers.equalTo("{\"test\":{\"foo\":true}}")));
//    }
//
//    @Test
//    public void testPopulateJsonModelWithBooleanWithOverride() {
//        TMJPBooleanCheck check = new TMJPBooleanCheck();
//        check.setMockValue(true);
//        export.populateJsonModel(check, "$.test.foo");
//
//        check.setMockValue(false);
//        export.populateJsonModel(check, "$.test.foo");
//
//        Map<String, Object> jsonMap = export.getJsonMap();
//        JSONObject jsonObject = new JSONObject(jsonMap);
//        assertThat(jsonObject.toString(), is(Matchers.equalTo("{\"test\":{\"foo\":false}}")));
//    }
}