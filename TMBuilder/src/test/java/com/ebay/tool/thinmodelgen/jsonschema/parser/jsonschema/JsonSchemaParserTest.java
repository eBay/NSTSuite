package com.ebay.tool.thinmodelgen.jsonschema.parser.jsonschema;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.swing.tree.DefaultMutableTreeNode;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.tool.thinmodelgen.TMBuilderRuntimeArguments;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

public class JsonSchemaParserTest {

  JsonSchemaParser parser = new JsonSchemaParser();

  @BeforeMethod(alwaysRun = true)
  public void setup() {
    TMBuilderRuntimeArguments.getInstance(new String[] {""});
  }

  @Test(groups = unitTest)
  public void baseAllTypesParsing() throws Throwable {

    String baseAllTypesPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testschemas/baseAllTypes.json", System.getProperty("user.dir"));

    DefaultMutableTreeNode rootNode = parser.parseSchema(baseAllTypesPath);
    assertThat("DefaultMutableTreeNode should not be null after parsing.", rootNode, is(notNullValue()));

    assertThat(rootNode.getChildCount(), is(equalTo(5)));
    assertThat(((JsonObjectType)rootNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));

    // Modules
    DefaultMutableTreeNode moduleNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
    assertThat(((JsonObjectType)moduleNode.getUserObject()).getJsonPathNodeName(), is(equalTo("modules")));
    assertThat(moduleNode.getChildCount(), is(equalTo(2)));

    // Modules - specialData
    DefaultMutableTreeNode specialDataNode = (DefaultMutableTreeNode) moduleNode.getChildAt(0);
    assertThat(((JsonObjectType)specialDataNode.getUserObject()).getJsonPathNodeName(), is(equalTo("specialData")));
    assertThat(specialDataNode.getChildCount(), is(equalTo(6)));

    // Modules - specialData - _type
    DefaultMutableTreeNode specialData_type = (DefaultMutableTreeNode) specialDataNode.getChildAt(0);
    assertThat(((JsonStringType)specialData_type.getUserObject()).getJsonPathNodeName(), is(equalTo("_type")));
    assertThat(specialData_type.getChildCount(), is(equalTo(0)));

    // Modules - specialData - nullableText
    DefaultMutableTreeNode nullableText = (DefaultMutableTreeNode) specialDataNode.getChildAt(1);
    assertThat(((JsonStringType)nullableText.getUserObject()).getJsonPathNodeName(), is(equalTo("nullableText")));
    assertThat(nullableText.getChildCount(), is(equalTo(0)));

    // Modules - specialData - enabled
    DefaultMutableTreeNode enabled = (DefaultMutableTreeNode) specialDataNode.getChildAt(2);
    assertThat(((JsonBooleanType)enabled.getUserObject()).getJsonPathNodeName(), is(equalTo("enabled")));
    assertThat(enabled.getChildCount(), is(equalTo(0)));

    // Modules - specialData - outputSelector
    DefaultMutableTreeNode outputSelector = (DefaultMutableTreeNode) specialDataNode.getChildAt(3);
    assertThat(((JsonStringType)outputSelector.getUserObject()).getJsonPathNodeName(), is(equalTo("outputSelector")));
    assertThat(((JsonStringType)outputSelector.getUserObject()).getEnumSet(), is(equalTo(new String[] {"ELIGIBILITY","DEFAULT","FULL"})));
    assertThat(outputSelector.getChildCount(), is(equalTo(0)));

    // Modules - specialData - iconSet
    DefaultMutableTreeNode iconSet = (DefaultMutableTreeNode) specialDataNode.getChildAt(4);
    assertThat(((JsonArrayType)iconSet.getUserObject()).getJsonPathNodeName(), is(equalTo("iconSet")));
    assertThat(iconSet.getChildCount(), is(equalTo(3)));

    // Modules - specialData - iconSet - name
    DefaultMutableTreeNode name = (DefaultMutableTreeNode) iconSet.getChildAt(0);
    assertThat(((JsonStringType)name.getUserObject()).getJsonPathNodeName(), is(equalTo("name")));
    assertThat(name.getChildCount(), is(equalTo(0)));

    // Modules - specialData - iconSet - accessibilityText
    DefaultMutableTreeNode accessibilityText = (DefaultMutableTreeNode) iconSet.getChildAt(1);
    assertThat(((JsonStringType)accessibilityText.getUserObject()).getJsonPathNodeName(), is(equalTo("accessibilityText")));
    assertThat(accessibilityText.getChildCount(), is(equalTo(0)));

    // Modules - specialData - iconSet - url
    DefaultMutableTreeNode url = (DefaultMutableTreeNode) iconSet.getChildAt(2);
    assertThat(((JsonStringType)url.getUserObject()).getJsonPathNodeName(), is(equalTo("url")));
    assertThat(url.getChildCount(), is(equalTo(0)));

    // Modules - specialData - regExValue
    DefaultMutableTreeNode regExValue = (DefaultMutableTreeNode) specialDataNode.getChildAt(5);
    assertThat(((JsonStringType)regExValue.getUserObject()).getJsonPathNodeName(), is(equalTo("regExValue")));
    assertThat(regExValue.getChildCount(), is(equalTo(0)));

    // Modules - summary
    DefaultMutableTreeNode summary = (DefaultMutableTreeNode) moduleNode.getChildAt(1);
    assertThat(((JsonObjectType)summary.getUserObject()).getJsonPathNodeName(), is(equalTo("summary")));
    assertThat(summary.getChildCount(), is(equalTo(2)));

    // Modules - summary - width
    DefaultMutableTreeNode width = (DefaultMutableTreeNode) summary.getChildAt(0);
    assertThat(((JsonFloatType)width.getUserObject()).getJsonPathNodeName(), is(equalTo("width")));
    assertThat(width.getChildCount(), is(equalTo(0)));

    // Modules - summary - height
    DefaultMutableTreeNode height = (DefaultMutableTreeNode) summary.getChildAt(1);
    assertThat(((JsonIntegerType)height.getUserObject()).getJsonPathNodeName(), is(equalTo("height")));
    assertThat(height.getChildCount(), is(equalTo(0)));

    // Generic 1
    DefaultMutableTreeNode generic1 = (DefaultMutableTreeNode) rootNode.getChildAt(1);
    assertThat(((JsonOneOfType)generic1.getUserObject()).getJsonPathNodeName(), is(equalTo("generics")));
    assertThat(generic1.getChildCount(), is(equalTo(1)));

    // Generic 1 - info
    DefaultMutableTreeNode info = (DefaultMutableTreeNode) generic1.getChildAt(0);
    assertThat(((JsonFloatType)info.getUserObject()).getJsonPathNodeName(), is(equalTo("info")));
    assertThat(info.getChildCount(), is(equalTo(0)));

    // Generic 2
    DefaultMutableTreeNode generic2 = (DefaultMutableTreeNode) rootNode.getChildAt(2);
    assertThat(((JsonOneOfType)generic2.getUserObject()).getJsonPathNodeName(), is(equalTo("generics")));
    assertThat(generic2.getChildCount(), is(equalTo(1)));

    // Generic 2 - value
    DefaultMutableTreeNode value = (DefaultMutableTreeNode) generic2.getChildAt(0);
    assertThat(((JsonStringType)value.getUserObject()).getJsonPathNodeName(), is(equalTo("value")));
    assertThat(value.getChildCount(), is(equalTo(0)));

    // stringValuePair
    DefaultMutableTreeNode stringValuePair = (DefaultMutableTreeNode) rootNode.getChildAt(3);
    assertThat(((JsonDictionaryType)stringValuePair.getUserObject()).getJsonPathNodeName(), is(equalTo("stringValuePair")));
    assertThat(stringValuePair.getChildCount(), is(equalTo(1)));

    // stringValuePair - key/value
    DefaultMutableTreeNode keyValue = (DefaultMutableTreeNode) stringValuePair.getChildAt(0);
    assertThat(((JsonStringType)keyValue.getUserObject()).getJsonPathNodeName(), is(equalTo("<key>")));
    assertThat(keyValue.getChildCount(), is(equalTo(0)));

    // arraySamples
    DefaultMutableTreeNode arraySamples = (DefaultMutableTreeNode) rootNode.getChildAt(4);
    assertThat(((JsonObjectType)arraySamples.getUserObject()).getJsonPathNodeName(), is(equalTo("arraySamples")));
    assertThat(arraySamples.getChildCount(), is(equalTo(3)));

    // stringArray
    DefaultMutableTreeNode stringArray = (DefaultMutableTreeNode) arraySamples.getChildAt(0);
    assertThat(((JsonArrayType)stringArray.getUserObject()).getJsonPathNodeName(), is(equalTo("stringArray")));
    assertThat(stringArray.getChildCount(), is(equalTo(1)));

    // objectArray
    DefaultMutableTreeNode objectArray = (DefaultMutableTreeNode) arraySamples.getChildAt(1);
    assertThat(((JsonArrayType)objectArray.getUserObject()).getJsonPathNodeName(), is(equalTo("objectArray")));
    assertThat(objectArray.getChildCount(), is(equalTo(2)));

    // arrayOfArrayOfString
    DefaultMutableTreeNode arrayOfArray = (DefaultMutableTreeNode) arraySamples.getChildAt(2);
    assertThat(((JsonArrayType)arrayOfArray.getUserObject()).getJsonPathNodeName(), is(equalTo("arrayOfArrayOfString")));
    assertThat(arrayOfArray.getChildCount(), is(equalTo(1)));

    // arrayOfString
    DefaultMutableTreeNode arrayOfString = (DefaultMutableTreeNode) arrayOfArray.getChildAt(0);
    assertThat(((JsonArrayType)arrayOfString.getUserObject()).getJsonPathNodeName(), is(equalTo("arrayOfArrayOfString")));
    assertThat(arrayOfString.getChildCount(), is(equalTo(1)));
  }

  @Test(enabled = false, groups = "unitTest")
  public void testBigFile() throws Throwable {

    DefaultMutableTreeNode rootNode = parser.parseSchema(String.format("%s/../native/NSTPayments/src/main/resources/com/ebay/xoneor/schema/createCheckoutSessionResponseSchema.json", System.getProperty("user.dir")));
    assertThat("DefaultMutableTreeNode should not be null after parsing.", rootNode, is(notNullValue()));
  }

  @Test(groups = unitTest)
  public void polymorphicParsing() throws Throwable {

    String polymorphicTypesPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testschemas/polymorphic.json", System.getProperty("user.dir"));

    DefaultMutableTreeNode rootNode = parser.parseSchema(polymorphicTypesPath);
    assertThat("DefaultMutableTreeNode should not be null after parsing.", rootNode, is(notNullValue()));

    assertThat(rootNode.getChildCount(), is(equalTo(6)));
    assertThat(((JsonObjectType)rootNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));

    // PolyAnyOf 1
    DefaultMutableTreeNode polyAnyOf1 = (DefaultMutableTreeNode) rootNode.getChildAt(0);
    assertThat(((JsonAnyOfType)polyAnyOf1.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOf")));
    assertThat(polyAnyOf1.getChildCount(), is(equalTo(1)));

    // PolyAnyOf 1 - info
    DefaultMutableTreeNode info = (DefaultMutableTreeNode) polyAnyOf1.getChildAt(0);
    assertThat(((JsonIntegerType)info.getUserObject()).getJsonPathNodeName(), is(equalTo("info")));
    assertThat(info.getChildCount(), is(equalTo(0)));

    // PolyAnyOf 2
    DefaultMutableTreeNode polyAnyOf2 = (DefaultMutableTreeNode) rootNode.getChildAt(1);
    assertThat(((JsonAnyOfType)polyAnyOf2.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOf")));
    assertThat(polyAnyOf2.getChildCount(), is(equalTo(1)));

    // PolyAnyOf 2 - value
    DefaultMutableTreeNode value = (DefaultMutableTreeNode) polyAnyOf2.getChildAt(0);
    assertThat(((JsonStringType)value.getUserObject()).getJsonPathNodeName(), is(equalTo("value")));
    assertThat(value.getChildCount(), is(equalTo(0)));

    // PolyAnyOf 3
    DefaultMutableTreeNode polyAnyOf3 = (DefaultMutableTreeNode) rootNode.getChildAt(2);
    assertThat(((JsonAnyOfType)polyAnyOf3.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOf")));
    assertThat(polyAnyOf3.getChildCount(), is(equalTo(1)));

    // PolyAnyOf 3 - style
    DefaultMutableTreeNode style = (DefaultMutableTreeNode) polyAnyOf3.getChildAt(0);
    assertThat(((JsonStringType)style.getUserObject()).getJsonPathNodeName(), is(equalTo("style")));
    assertThat(((JsonStringType)style.getUserObject()).getEnumSet(), is(equalTo(new String[] {"NORMAL","BOLD","ITALIC"})));
    assertThat(style.getChildCount(), is(equalTo(0)));

    // PolyAnyOf 4
    DefaultMutableTreeNode polyAnyOf4 = (DefaultMutableTreeNode) rootNode.getChildAt(3);
    assertThat(((JsonStringType)polyAnyOf4.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOf")));
    assertThat(polyAnyOf4.getChildCount(), is(equalTo(0)));

    // PolyAnyOf 5
    DefaultMutableTreeNode polyAnyOf5 = (DefaultMutableTreeNode) rootNode.getChildAt(4);
    assertThat(((JsonIntegerType)polyAnyOf5.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOf")));
    assertThat(polyAnyOf5.getChildCount(), is(equalTo(0)));

    // PolyAnyOfArray 1
    DefaultMutableTreeNode polyAnyOfArray = (DefaultMutableTreeNode) rootNode.getChildAt(5);
    assertThat(((JsonArrayType)polyAnyOfArray.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOfArray")));
    assertThat(polyAnyOfArray.getChildCount(), is(equalTo(2)));

    // PolyAnyOfArray 1 - object
    DefaultMutableTreeNode polyAnyOfObject1 = (DefaultMutableTreeNode) polyAnyOfArray.getChildAt(0);
    assertThat(((JsonAnyOfType)polyAnyOfObject1.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOfArray")));
    assertThat(polyAnyOfObject1.getChildCount(), is(equalTo(1)));

    // PolyAnyOfArray 1 - object - info
    DefaultMutableTreeNode polyAnyOfInfo = (DefaultMutableTreeNode) polyAnyOfObject1.getChildAt(0);
    assertThat(((JsonIntegerType)polyAnyOfInfo.getUserObject()).getJsonPathNodeName(), is(equalTo("info")));
    assertThat(polyAnyOfInfo.getChildCount(), is(equalTo(0)));

    // PolyAnyOfArray 2 - object
    DefaultMutableTreeNode polyAnyOfObject2 = (DefaultMutableTreeNode) polyAnyOfArray.getChildAt(1);
    assertThat(((JsonAnyOfType)polyAnyOfObject2.getUserObject()).getJsonPathNodeName(), is(equalTo("polyAnyOfArray")));
    assertThat(polyAnyOfObject2.getChildCount(), is(equalTo(1)));

    // PolyAnyOfArray 1 - object - info
    DefaultMutableTreeNode polyAnyOfValue = (DefaultMutableTreeNode) polyAnyOfObject2.getChildAt(0);
    assertThat(((JsonStringType)polyAnyOfValue.getUserObject()).getJsonPathNodeName(), is(equalTo("value")));
    assertThat(polyAnyOfValue.getChildCount(), is(equalTo(0)));
  }

  @Test(enabled = true, groups = "unitTest")
  public void testInvalidArrayParsing() throws Throwable {

    String invalidArraySchemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/jsonschema/parser/jsonschema/invalidArraySchema.json", System.getProperty("user.dir"));

    DefaultMutableTreeNode rootNode = parser.parseSchema(invalidArraySchemaPath);
    assertThat("DefaultMutableTreeNode should not be null after parsing.", rootNode, is(notNullValue()));
  }
}
