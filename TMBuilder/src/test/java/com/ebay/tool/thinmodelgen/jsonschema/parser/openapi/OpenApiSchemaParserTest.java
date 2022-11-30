package com.ebay.tool.thinmodelgen.jsonschema.parser.openapi;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import org.mockito.Mockito;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ebay.tool.thinmodelgen.TMBuilderRuntimeArguments;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.TMFileSingleton;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserRequestMethod;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

import io.swagger.v3.oas.models.media.Schema;

public class OpenApiSchemaParserTest {

  // Test missing parameters in the payload

  @BeforeSuite(alwaysRun = true)
  public void beforeSuite() {
    TMBuilderRuntimeArguments.getInstance(new String[] {"-v"});
  }

  @Test(groups = unitTest)
  public void parseComplexModel() {

    String schemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.yaml", System.getProperty("user.dir"));

    SchemaParserPayload payload = new SchemaParserPayload().setPath("/test").setMethod(SchemaParserRequestMethod.GET).setResponseCode("200").setContentType("application/json");
    TMFileSingleton.getInstance().setPayload(payload);

    OpenApiSchemaParser parser = new OpenApiSchemaParser();
    DefaultMutableTreeNode rootNode = parser.parseSchema(schemaPath);

    assertThat("Tree node MUST NOT be null.", rootNode, is(notNullValue()));

    assertThat(rootNode.getChildCount(), is(equalTo(11)));
    assertThat(((JsonObjectType)rootNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));
    
    // --------------------
    // Check for oneOf
    
    DefaultMutableTreeNode oneOf = (DefaultMutableTreeNode) rootNode.getChildAt(3);
    assertThat(((JsonObjectType)oneOf.getUserObject()).getJsonPathNodeName(), is(equalTo("OneOfComposition")));
    assertThat(oneOf.getChildCount(), is(equalTo(3)));
    
    DefaultMutableTreeNode item1 = (DefaultMutableTreeNode) oneOf.getChildAt(0);
    assertThat(((JsonOneOfType) item1.getUserObject()).getJsonPathNodeName(), is(equalTo("oneOfModel")));
    assertThat(((DefaultMutableTreeNode) item1.getChildAt(0)).getUserObject(), is(instanceOf(JsonIntegerType.class)));
    
    DefaultMutableTreeNode item2 = (DefaultMutableTreeNode) oneOf.getChildAt(1);
    assertThat(((JsonOneOfType) item2.getUserObject()).getJsonPathNodeName(), is(equalTo("oneOfModel")));
    assertThat(((DefaultMutableTreeNode) item2.getChildAt(0)).getUserObject(), is(instanceOf(JsonStringType.class)));

    DefaultMutableTreeNode item3 = (DefaultMutableTreeNode) oneOf.getChildAt(2);
    assertThat(((JsonOneOfType) item3.getUserObject()).getJsonPathNodeName(), is(equalTo("oneOfModel")));
    assertThat(((DefaultMutableTreeNode) item3.getChildAt(0)).getUserObject(), is(instanceOf(JsonBooleanType.class)));

    // --------------------
    // Check for anyOf
    DefaultMutableTreeNode anyOf = (DefaultMutableTreeNode) rootNode.getChildAt(5);
    assertThat(((JsonObjectType)anyOf.getUserObject()).getJsonPathNodeName(), is(equalTo("AnyOfComposition")));
    assertThat(anyOf.getChildCount(), is(equalTo(6)));
    
    item1 = (DefaultMutableTreeNode) anyOf.getChildAt(0);
    assertThat(((JsonStringType) item1.getUserObject()).getJsonPathNodeName(), is(equalTo("anyOfModel")));
    assertThat(((JsonStringType) item1.getUserObject()), is(instanceOf(JsonStringType.class)));

    item2 = (DefaultMutableTreeNode) anyOf.getChildAt(1);
    assertThat(((JsonIntegerType) item2.getUserObject()).getJsonPathNodeName(), is(equalTo("anyOfModel")));
    assertThat(((JsonIntegerType) item2.getUserObject()), is(instanceOf(JsonIntegerType.class)));

    item3 = (DefaultMutableTreeNode) anyOf.getChildAt(2);
    assertThat(((JsonBooleanType) item3.getUserObject()).getJsonPathNodeName(), is(equalTo("anyOfModel")));
    assertThat(((JsonBooleanType) item3.getUserObject()), is(instanceOf(JsonBooleanType.class)));

    DefaultMutableTreeNode item4 = (DefaultMutableTreeNode) anyOf.getChildAt(3);
    assertThat(((JsonAnyOfType) item4.getUserObject()).getJsonPathNodeName(), is(equalTo("anyOfModel")));
    assertThat(((DefaultMutableTreeNode) item4.getChildAt(0)).getUserObject(), is(instanceOf(JsonIntegerType.class)));
    
    DefaultMutableTreeNode item5 = (DefaultMutableTreeNode) anyOf.getChildAt(4);
    assertThat(((JsonAnyOfType) item5.getUserObject()).getJsonPathNodeName(), is(equalTo("anyOfModel")));
    assertThat(((DefaultMutableTreeNode) item5.getChildAt(0)).getUserObject(), is(instanceOf(JsonStringType.class)));

    DefaultMutableTreeNode item6 = (DefaultMutableTreeNode) anyOf.getChildAt(5);
    assertThat(((JsonAnyOfType) item6.getUserObject()).getJsonPathNodeName(), is(equalTo("anyOfModel")));
    assertThat(((DefaultMutableTreeNode) item6.getChildAt(0)).getUserObject(), is(instanceOf(JsonBooleanType.class)));
    
    // --------------------
    // Check for allOf
    
    DefaultMutableTreeNode allOf = (DefaultMutableTreeNode) rootNode.getChildAt(4);
    assertThat(((JsonObjectType) allOf.getUserObject()).getJsonPathNodeName(), is(equalTo("AllOfComposition")));
    assertThat(allOf.getChildCount(), is(equalTo(2)));
    
    item1 = (DefaultMutableTreeNode) allOf.getChildAt(0);
    assertThat(((JsonIntegerType) item1.getUserObject()).getJsonPathNodeName(), is(equalTo("intField")));    
    
    item2 = (DefaultMutableTreeNode) allOf.getChildAt(1);
    assertThat(((JsonIntegerType) item2.getUserObject()).getJsonPathNodeName(), is(equalTo("value")));

  }

  @Test(groups = unitTest, expectedExceptions = NullPointerException.class)
  public void parseSchemaWithNullPath() {

    OpenApiSchemaParser parser = new OpenApiSchemaParser();
    parser.parseSchema(null);
  }

  @Test(groups = unitTest, expectedExceptions = IllegalArgumentException.class)
  public void parseSchemaWithInvalidPath() {

    OpenApiSchemaParser parser = new OpenApiSchemaParser();
    parser.parseSchema("/foo.yaml");
  }

  @Test(groups = unitTest)
  public void parserSchemaCallsParseObject() throws IOException {

    String schemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.yaml", System.getProperty("user.dir"));

    SchemaParserPayload payload = new SchemaParserPayload().setPath("/test").setMethod(SchemaParserRequestMethod.GET).setResponseCode("200").setContentType("application/json");
    TMFileSingleton.getInstance().setPayload(payload);

    OpenApiNodeParser nodeParser = Mockito.mock(OpenApiNodeParser.class);
    OpenApiSchemaParser parser = new OpenApiSchemaParser(nodeParser);
    parser.parseSchema(schemaPath);
    Mockito.verify(nodeParser, times(1)).routeParser(Mockito.anyString(), Mockito.any(Schema.class));
  }

  @Test(groups = unitTest)
  public void parserReturnsEmptyNodeListMustReturnEmptyTreeNode() throws IOException {

    String schemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.yaml", System.getProperty("user.dir"));

    SchemaParserPayload payload = new SchemaParserPayload().setPath("/test").setMethod(SchemaParserRequestMethod.GET).setResponseCode("200").setContentType("application/json");
    TMFileSingleton.getInstance().setPayload(payload);

    OpenApiNodeParser nodeParser = Mockito.mock(OpenApiNodeParser.class);
    Mockito.when(nodeParser.parseObject(Mockito.anyString(), Mockito.any(Schema.class))).thenReturn(new ArrayList<DefaultMutableTreeNode>());
    OpenApiSchemaParser parser = new OpenApiSchemaParser(nodeParser);
    DefaultMutableTreeNode node = parser.parseSchema(schemaPath);
    assertThat("Child count must be empty when the number of nodes returned is 0.", node.getChildCount(), is(equalTo(0)));
  }

}
