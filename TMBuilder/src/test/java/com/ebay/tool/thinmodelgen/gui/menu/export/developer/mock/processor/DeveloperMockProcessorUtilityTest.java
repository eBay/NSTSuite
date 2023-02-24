package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.jsonpath.*;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.*;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import io.swagger.util.Json;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.constraints.Null;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class DeveloperMockProcessorUtilityTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testConvertNodeModelToJsonBaseTypeWithNullNodeModel() throws Exception {
        DeveloperMockProcessorUtility.convertNodeModelToJsonBaseType(null);
    }

    @Test
    public void testConvertNodeModelToJsonBaseType() throws Exception {
        JsonBooleanType expected = new JsonBooleanType("Foo");
        String serializedType = JsonBaseTypePersistence.serialize(expected);
        NodeModel nodeModel = mock(NodeModel.class);
        when(nodeModel.getSerializedUserObject()).thenReturn(serializedType);
        JsonBaseType actual = DeveloperMockProcessorUtility.convertNodeModelToJsonBaseType(nodeModel);
        assertThat(actual, is(equalTo(expected)));
    }

    @DataProvider(name = "jsonPathStepArrayTestData")
    public Object[][] jsonPathStepArrayTestData() {
        return new Object[][] {
                {null, false},
                {"", false},
                {"$", false},
                {"$.foo", false},
                {"$.foo[", true},
                {"$.foo[]", true},
                {"$.foo[1]", true},
                {"$.foo[*]", true},
                {"$.foo[*].bar[*]", true},
        };
    }

    @Test(dataProvider = "jsonPathStepArrayTestData")
    public void testIsJsonPathStepAnArray(String path, boolean expected) throws Exception {
        boolean actual = DeveloperMockProcessorUtility.isJsonPathStepAnArray(path);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetPrimitiveTypeForCheckWithNullJsonBaseType() throws Exception {
        DeveloperMockProcessorUtility.getPrimitiveTypeForCheck(null, "");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetPrimitiveTypeForCheckWithNullPath() throws Exception {
        DeveloperMockProcessorUtility.getPrimitiveTypeForCheck(new JsonBooleanType("Foo"), null);
    }

    @DataProvider(name = "primitiveTypeTestData")
    public Object[][] getPrimitiveTypeTestData() {

        JsonBooleanType booleanType = new JsonBooleanType("Foo");
        String booleanTypePath = "$.foo";
        booleanType.updateCheckForPath(booleanTypePath, new TMJPBooleanCheck());

        JsonBooleanType booleanTypeArray = new JsonBooleanType("Foo");
        String booleanTypeArrayPath = "$.foo[*]";
        booleanTypeArray.updateCheckForPath(booleanTypeArrayPath, new TMJPListOfBooleanCheck());

        JsonFloatType floatType = new JsonFloatType("Foo");
        String floatTypePath = "$.foo";
        floatType.updateCheckForPath(floatTypePath, new TMJPDoubleCheck());

        JsonFloatType floatTypeArray = new JsonFloatType("Foo");
        String floatTypeArrayPath = "$.foo[*]";
        floatTypeArray.updateCheckForPath(floatTypeArrayPath, new TMJPListOfDoubleCheck());

        JsonIntegerType intType = new JsonIntegerType("Foo");
        String intTypePath = "$.foo";
        intType.updateCheckForPath(intTypePath, new TMJPIntegerCheck());

        JsonIntegerType intTypeArray = new JsonIntegerType("Foo");
        String intTypeArrayPath = "$.foo[*]";
        intTypeArray.updateCheckForPath(intTypeArrayPath, new TMJPListOfIntegerCheck());

        JsonStringType stringType = new JsonStringType("Foo");
        String stringTypePath = "$.foo";
        stringType.updateCheckForPath(stringTypePath, new TMJPStringCheck());

        JsonStringType stringTypeArray = new JsonStringType("Foo");
        String stringTypeArrayPath = "$.foo[*]";
        stringTypeArray.updateCheckForPath(stringTypeArrayPath, new TMJPListOfStringCheck());

        return new Object[][] {
                {booleanType, booleanTypePath, DeveloperMockProcessorUtility.PrimitiveType.BOOLEAN},
                {booleanTypeArray, booleanTypeArrayPath, DeveloperMockProcessorUtility.PrimitiveType.BOOLEAN},
                {floatType, floatTypePath, DeveloperMockProcessorUtility.PrimitiveType.DOUBLE},
                {floatTypeArray, floatTypeArrayPath, DeveloperMockProcessorUtility.PrimitiveType.DOUBLE},
                {intType, intTypePath, DeveloperMockProcessorUtility.PrimitiveType.INTEGER},
                {intTypeArray, intTypeArrayPath, DeveloperMockProcessorUtility.PrimitiveType.INTEGER},
                {stringType, stringTypePath, DeveloperMockProcessorUtility.PrimitiveType.STRING},
                {stringTypeArray, stringTypeArrayPath, DeveloperMockProcessorUtility.PrimitiveType.STRING},
        };
    }
    @Test(dataProvider = "primitiveTypeTestData")
    public void testGetPrimitiveTypeForCheck(JsonBaseType baseType, String path, DeveloperMockProcessorUtility.PrimitiveType expected) throws Exception {
        DeveloperMockProcessorUtility.PrimitiveType actual = DeveloperMockProcessorUtility.getPrimitiveTypeForCheck(baseType, path);
        assertThat(actual, is(equalTo(expected)));
    }
}