package com.ebay.tool.thinmodelgen.jsonschema.parser.openapi;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.tool.thinmodelgen.jsonschema.parser.NodeParsingStackLogger;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

public class OpenApiNodeParserTest {

  // There is nothing special about this path. It is used to accumulate the path
  // for validation reporting. We simply need a value to pass in for these
  // tests.
  private static final String testParserPath = "test";

  private NodeParsingStackLogger stackLogger;

  @BeforeMethod(alwaysRun = true)
  public void beforeEachTest() {
    stackLogger = Mockito.mock(NodeParsingStackLogger.class);
  }

  @Test(groups = unitTest, expectedExceptions = NullPointerException.class)
  public void initializeWithNullParser() {
    new OpenApiNodeParser(null);
  }

  @Test(groups = unitTest)
  public void routeParserWithNullSchemaReturnsEmptyList() throws IOException {
    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, null);
    assertThat("List must be empty.", treeNodes.size(), is(equalTo(0)));
  }

  @Test(groups = unitTest)
  @SuppressWarnings("rawtypes")
  public void routeParserComposedScheamaAnyOf() throws IOException {

    StringSchema stringSchema = new StringSchema();
    stringSchema.setType("string");

    IntegerSchema integerSchema = new IntegerSchema();
    integerSchema.setType("integer");

    HashMap<String, Schema> oneOfObjectProperties = new HashMap<>();
    oneOfObjectProperties.put("foo", stringSchema);

    ObjectSchema objectSchema = new ObjectSchema();
    objectSchema.setType("object");
    objectSchema.setProperties(oneOfObjectProperties);

    ComposedSchema composedSchema = new ComposedSchema();
    composedSchema.addAnyOfItem(stringSchema);
    composedSchema.addAnyOfItem(integerSchema);
    composedSchema.addAnyOfItem(objectSchema);

    HashMap<String, Schema> rootObjectProperties = new HashMap<>();
    rootObjectProperties.put("root", composedSchema);

    ObjectSchema rootSchema = new ObjectSchema();
    rootSchema.setProperties(rootObjectProperties);

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, rootSchema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode rootNode = treeNodes.get(0);
    assertThat("Parsed object schema must have three children.", rootNode.getChildCount(), is(equalTo(3)));

    Object userObject = rootNode.getUserObject();
    assertThat("User object MUST be a JsonObjectType.", userObject, is(instanceOf(JsonObjectType.class)));

    DefaultMutableTreeNode anyOfStringNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
    assertThat("AnyOf string schema must NOT have children.", anyOfStringNode.getChildCount(), is(equalTo(0)));

    Object anyOfStringUserObject = anyOfStringNode.getUserObject();
    assertThat("AnyOf string schema MUST be a JsonStringType.", anyOfStringUserObject, is(instanceOf(JsonStringType.class)));

    DefaultMutableTreeNode anyOfIntegerNode = (DefaultMutableTreeNode) rootNode.getChildAt(1);
    assertThat("AnyOf integer node must NOT have children.", anyOfIntegerNode.getChildCount(), is(equalTo(0)));

    Object anyOfIntegerUserObject = anyOfIntegerNode.getUserObject();
    assertThat("AnyOf integer node MUST be a JsonIntegerType.", anyOfIntegerUserObject, is(instanceOf(JsonIntegerType.class)));

    DefaultMutableTreeNode anyOfObjectNode = (DefaultMutableTreeNode) rootNode.getChildAt(2);
    assertThat("AnyOf object node must have one child.", anyOfObjectNode.getChildCount(), is(equalTo(1)));

    Object anyOfObjectUserObject = anyOfObjectNode.getUserObject();
    assertThat("AnyOf object node MUST be a JsonAnyOfType.", anyOfObjectUserObject, is(instanceOf(JsonAnyOfType.class)));

    DefaultMutableTreeNode anyOfObjectProperty = (DefaultMutableTreeNode) anyOfObjectNode.getChildAt(0);
    assertThat("AnyOf Object's property must NOT have children.", anyOfObjectProperty.getChildCount(), is(equalTo(0)));

    Object anyOfObjectPropertyUserObject = anyOfObjectProperty.getUserObject();
    assertThat("AnyOf Object's property MUST be a JsonStringType.", anyOfObjectPropertyUserObject, is(instanceOf(JsonStringType.class)));
  }

  @SuppressWarnings("rawtypes")
  @Test(groups = unitTest)
  public void routeParserComposedSchemaOneOf() throws IOException {

    StringSchema stringSchema = new StringSchema();
    stringSchema.setType("string");

    IntegerSchema integerSchema = new IntegerSchema();
    integerSchema.setType("integer");

    HashMap<String, Schema> oneOfObjectProperties = new HashMap<>();
    oneOfObjectProperties.put("foo", stringSchema);

    ObjectSchema objectSchema = new ObjectSchema();
    objectSchema.setType("object");
    objectSchema.setProperties(oneOfObjectProperties);

    ComposedSchema composedSchema = new ComposedSchema();
    composedSchema.addOneOfItem(stringSchema);
    composedSchema.addOneOfItem(integerSchema);
    composedSchema.addOneOfItem(objectSchema);

    HashMap<String, Schema> rootObjectProperties = new HashMap<>();
    rootObjectProperties.put("root", composedSchema);

    ObjectSchema rootSchema = new ObjectSchema();
    rootSchema.setProperties(rootObjectProperties);

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, rootSchema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode rootNode = treeNodes.get(0);
    assertThat("Parsed object schema must have three children.", rootNode.getChildCount(), is(equalTo(3)));

    Object userObject = rootNode.getUserObject();
    assertThat("User object MUST be a JsonObjectType.", userObject, is(instanceOf(JsonObjectType.class)));

    DefaultMutableTreeNode oneOfStringNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
    assertThat("OneOf string schema must NOT have children.", oneOfStringNode.getChildCount(), is(equalTo(0)));

    Object oneOfStringUserObject = oneOfStringNode.getUserObject();
    assertThat("OneOf string schema MUST be a JsonStringType.", oneOfStringUserObject, is(instanceOf(JsonStringType.class)));

    DefaultMutableTreeNode oneOfIntegerNode = (DefaultMutableTreeNode) rootNode.getChildAt(1);
    assertThat("One of integer node must NOT have children.", oneOfIntegerNode.getChildCount(), is(equalTo(0)));

    Object oneOfIntegerUserObject = oneOfIntegerNode.getUserObject();
    assertThat("One of integer node MUST be a JsonIntegerType.", oneOfIntegerUserObject, is(instanceOf(JsonIntegerType.class)));

    DefaultMutableTreeNode oneOfObjectNode = (DefaultMutableTreeNode) rootNode.getChildAt(2);
    assertThat("One of object node must have one child.", oneOfObjectNode.getChildCount(), is(equalTo(1)));

    Object oneOfObjectUserObject = oneOfObjectNode.getUserObject();
    assertThat("One of object node MUST be a JsonOneOfType.", oneOfObjectUserObject, is(instanceOf(JsonOneOfType.class)));

    DefaultMutableTreeNode oneOfObjectProperty = (DefaultMutableTreeNode) oneOfObjectNode.getChildAt(0);
    assertThat("One of Object's property must NOT have children.", oneOfObjectProperty.getChildCount(), is(equalTo(0)));

    Object oneOfObjectPropertyUserObject = oneOfObjectProperty.getUserObject();
    assertThat("One of Object's property MUST be a JsonStringType.", oneOfObjectPropertyUserObject, is(instanceOf(JsonStringType.class)));
  }

  @SuppressWarnings("rawtypes")
  @Test(groups = unitTest)
  public void routeParserObjectSchema() throws IOException {

    StringSchema stringSchema = new StringSchema();
    stringSchema.setType("string");

    HashMap<String, Schema> properties = new HashMap<>();
    properties.put("first", stringSchema);

    ObjectSchema schema = new ObjectSchema();
    schema.setType("object");
    schema.setProperties(properties);

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, schema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed object schema must have one children.", treeNode.getChildCount(), is(equalTo(1)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonObjectType.", userObject, is(instanceOf(JsonObjectType.class)));

    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) treeNode.getChildAt(0);
    assertThat("Parsed object schema must NOT have children.", childNode.getChildCount(), is(equalTo(0)));

    Object childUserObject = childNode.getUserObject();
    assertThat("Child object MUST be a JsonStringType.", childUserObject, is(instanceOf(JsonStringType.class)));
  }

  @Test(groups = unitTest)
  public void routeParseNullSchema() throws Exception {
    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser("test", null);
    assertThat("List must be empty.", treeNodes.size(), is(equalTo(0)));
  }

  @Test(groups = unitTest)
  public void routeParseNullSchemaType() throws Exception {

    StringSchema stringSchema = new StringSchema();
    stringSchema.setType(null);

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, stringSchema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed object schema must NOT have children.", treeNode.getChildCount(), is(equalTo(0)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonDictionaryType.", userObject, is(instanceOf(JsonDictionaryType.class)));
  }

  @Test(groups = unitTest, expectedExceptions = IOException.class)
  public void routeParseThrowsExceptionForUnknownSchemaTypeName() throws Exception {

    StringSchema stringSchema = new StringSchema();
    stringSchema.setType("UNKNOWN");

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    nodeParser.routeParser("test", stringSchema);
  }

  @Test(groups = unitTest)
  public void routeParserArraySchema() throws IOException {

    StringSchema stringSchema = new StringSchema();
    stringSchema.setType("string");

    ArraySchema arraySchema = new ArraySchema();
    arraySchema.setItems(stringSchema);

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, arraySchema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed array schema must have one child.", treeNode.getChildCount(), is(equalTo(1)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonArrayType.", userObject, is(instanceOf(JsonArrayType.class)));

    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) treeNode.getChildAt(0);
    assertThat("Parsed object schema must NOT have children.", childNode.getChildCount(), is(equalTo(0)));

    Object childUserObject = childNode.getUserObject();
    assertThat("Child object MUST be a JsonStringType.", childUserObject, is(instanceOf(JsonStringType.class)));
  }

  @Test(groups = unitTest)
  public void routeParserStringSchema() throws IOException {

    StringSchema schema = new StringSchema();
    schema.setType("string");

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, schema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed string schema must NOT have any children.", treeNode.getChildCount(), is(equalTo(0)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonStringType.", userObject, is(instanceOf(JsonStringType.class)));

    JsonStringType jsonStringType = (JsonStringType) userObject;
    assertThat("JsonStringType must not have any enums.", jsonStringType.getEnumSet(), is(emptyArray()));
  }

  @Test(groups = unitTest)
  public void routeParserStringSchemaWithEnum() throws IOException {

    StringSchema schema = new StringSchema();
    schema.setType("string");
    schema.setEnum(Arrays.asList("one", "two", "three"));

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, schema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed string schema must NOT have any children.", treeNode.getChildCount(), is(equalTo(0)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonStringType.", userObject, is(instanceOf(JsonStringType.class)));

    JsonStringType jsonStringType = (JsonStringType) userObject;
    assertThat("JsonStringType must have matching enums.", jsonStringType.getEnumSet(), is(equalTo(new String[] { "one", "two", "three" })));
  }

  @Test(groups = unitTest)
  public void routeParserBooleanSchema() throws IOException {

    BooleanSchema schema = new BooleanSchema();
    schema.setType("boolean");

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, schema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed boolean schema must NOT have any children.", treeNode.getChildCount(), is(equalTo(0)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonBooleanType.", userObject, is(instanceOf(JsonBooleanType.class)));
  }

  @Test(groups = unitTest)
  public void routeParserIntegerSchema() throws IOException {

    IntegerSchema schema = new IntegerSchema();
    schema.setType("integer");

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, schema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed integer schema must NOT have any children.", treeNode.getChildCount(), is(equalTo(0)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonIntegerType.", userObject, is(instanceOf(JsonIntegerType.class)));
  }

  @Test(groups = unitTest)
  public void routeParserNumberSchema() throws IOException {

    NumberSchema schema = new NumberSchema();
    schema.setType("number");

    OpenApiNodeParser nodeParser = new OpenApiNodeParser(stackLogger);
    List<DefaultMutableTreeNode> treeNodes = nodeParser.routeParser(testParserPath, schema);
    assertThat("List must have one entry.", treeNodes.size(), is(equalTo(1)));

    DefaultMutableTreeNode treeNode = treeNodes.get(0);
    assertThat("Parsed number schema must NOT have any children.", treeNode.getChildCount(), is(equalTo(0)));

    Object userObject = treeNode.getUserObject();
    assertThat("User object MUST be a JsonFloatType.", userObject, is(instanceOf(JsonFloatType.class)));
  }
}
