package com.ebay.tool.thinmodelgen.jsonschema.parser.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.types.GraphQLEnum;
import com.ebay.graphql.types.GraphQLList;
import com.ebay.graphql.types.GraphQLObject;
import com.ebay.graphql.types.GraphQLReference;
import com.ebay.graphql.types.GraphQLScalar;
import com.ebay.graphql.types.GraphQLList.Dimensionality;
import com.ebay.graphql.types.GraphQLScalar.GraphQLScalarValue;
import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.GraphQLOperation;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

public class GraphQLNodeParserTest {

	@Test(expectedExceptions = NullPointerException.class)
	public void routeParserWithNullSchema() throws Exception {
		new GraphQLNodeParser(null);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void routeParserWithNullOperation() throws Exception {
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		parser.routeParser(null, "foo");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void routeParserWithNullOperationName() throws Exception {
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		parser.routeParser(GraphQLOperation.MUTATION, null);
	}
	
	@Test
	public void routeParserQuery() throws Exception {
		
		GraphQLObject graphQLObject = new GraphQLObject();
		graphQLObject.addField("one", new GraphQLScalar(GraphQLScalarValue.STRING));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addQuery("test", graphQLObject);
		
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		List<DefaultMutableTreeNode> rootNodes = parser.routeParser(GraphQLOperation.QUERY, "test");
		
		assertThat("Tree node MUST NOT be null.", rootNodes, is(notNullValue()));

	    assertThat(rootNodes.size(), is(equalTo(1)));
	    
	    DefaultMutableTreeNode rootNode = rootNodes.get(0);
	    
	    assertThat(((JsonObjectType)rootNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));

	    // Check for data
	    DefaultMutableTreeNode data = (DefaultMutableTreeNode) rootNode.getChildAt(0);
	    assertThat(((JsonObjectType)data.getUserObject()).getJsonPathNodeName(), is(equalTo("data")));
	    assertThat(data.getChildCount(), is(equalTo(1)));
	    
	    // Check for errors
	    DefaultMutableTreeNode errors = (DefaultMutableTreeNode) rootNode.getChildAt(1);
	    assertThat(((JsonArrayType)errors.getUserObject()).getJsonPathNodeName(), is(equalTo("errors")));
	    assertThat(errors.getChildCount(), is(equalTo(3)));
	    
	    // Check for root object is represented by its fields.
	    DefaultMutableTreeNode obj = (DefaultMutableTreeNode) data.getChildAt(0);
	    assertThat(((JsonObjectType)obj.getUserObject()).getJsonPathNodeName(), is(equalTo("test")));
	    assertThat(obj.getChildCount(), is(equalTo(1)));
	    
	    // Check for the string type
	    DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) obj.getChildAt(0);
	    assertThat(((JsonStringType)leaf.getUserObject()).getJsonPathNodeName(), is(equalTo("one")));
	    assertThat(leaf.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void routeParserMutation() throws Exception {
		
		GraphQLObject graphQLObject = new GraphQLObject();
		graphQLObject.addField("one", new GraphQLScalar(GraphQLScalarValue.STRING));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addMutation("test", graphQLObject);
		
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		List<DefaultMutableTreeNode> rootNodes = parser.routeParser(GraphQLOperation.MUTATION, "test");
		
		assertThat("Tree node MUST NOT be null.", rootNodes, is(notNullValue()));

	    assertThat(rootNodes.size(), is(equalTo(1)));
	    
	    DefaultMutableTreeNode rootNode = rootNodes.get(0);
	    
	    assertThat(((JsonObjectType)rootNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));

	    // Check for data
	    DefaultMutableTreeNode data = (DefaultMutableTreeNode) rootNode.getChildAt(0);
	    assertThat(((JsonObjectType)data.getUserObject()).getJsonPathNodeName(), is(equalTo("data")));
	    assertThat(data.getChildCount(), is(equalTo(1)));
	    
	    // Check for errors
	    DefaultMutableTreeNode errors = (DefaultMutableTreeNode) rootNode.getChildAt(1);
	    assertThat(((JsonArrayType)errors.getUserObject()).getJsonPathNodeName(), is(equalTo("errors")));
	    assertThat(errors.getChildCount(), is(equalTo(3)));
	    
	    // Check for root object is represented by its fields.
	    DefaultMutableTreeNode obj = (DefaultMutableTreeNode) data.getChildAt(0);
	    assertThat(((JsonObjectType)obj.getUserObject()).getJsonPathNodeName(), is(equalTo("test")));
	    assertThat(obj.getChildCount(), is(equalTo(1)));
	    
	    // Check for the string type
	    DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) obj.getChildAt(0);
	    assertThat(((JsonStringType)leaf.getUserObject()).getJsonPathNodeName(), is(equalTo("one")));
	    assertThat(leaf.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void routeParserSubscription() throws Exception {
		
		GraphQLObject graphQLObject = new GraphQLObject();
		graphQLObject.addField("one", new GraphQLScalar(GraphQLScalarValue.STRING));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addSubscription("test", graphQLObject);
		
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		List<DefaultMutableTreeNode> rootNodes = parser.routeParser(GraphQLOperation.SUBSCRIPTION, "test");
		
		assertThat("Tree node MUST NOT be null.", rootNodes, is(notNullValue()));

	    assertThat(rootNodes.size(), is(equalTo(1)));
	    
	    DefaultMutableTreeNode rootNode = rootNodes.get(0);
	    
	    assertThat(((JsonObjectType)rootNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));

	    // Check for data
	    DefaultMutableTreeNode data = (DefaultMutableTreeNode) rootNode.getChildAt(0);
	    assertThat(((JsonObjectType)data.getUserObject()).getJsonPathNodeName(), is(equalTo("data")));
	    assertThat(data.getChildCount(), is(equalTo(1)));
	    
	    // Check for errors
	    DefaultMutableTreeNode errors = (DefaultMutableTreeNode) rootNode.getChildAt(1);
	    assertThat(((JsonArrayType)errors.getUserObject()).getJsonPathNodeName(), is(equalTo("errors")));
	    assertThat(errors.getChildCount(), is(equalTo(3)));
	    
	    // Check for root object is represented by its fields.
	    DefaultMutableTreeNode obj = (DefaultMutableTreeNode) data.getChildAt(0);
	    assertThat(((JsonObjectType)obj.getUserObject()).getJsonPathNodeName(), is(equalTo("test")));
	    assertThat(obj.getChildCount(), is(equalTo(1)));
	    
	    // Check for the string type
	    DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) obj.getChildAt(0);
	    assertThat(((JsonStringType)leaf.getUserObject()).getJsonPathNodeName(), is(equalTo("one")));
	    assertThat(leaf.getChildCount(), is(equalTo(0)));
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void routeParserQueryUnmatched() throws Exception {

		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		parser.routeParser(GraphQLOperation.QUERY, "test");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void routeParserMutationUnmatched() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		parser.routeParser(GraphQLOperation.MUTATION, "test");
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void routeParserSubscriptionUnmatched() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		parser.routeParser(GraphQLOperation.SUBSCRIPTION, "test");
	}
	
	@Test
	public void parseTreeForBoolean() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("boolean", new GraphQLScalar(GraphQLScalarValue.BOOLEAN));
		assertThat(((JsonBooleanType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("boolean")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForFloat() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("float", new GraphQLScalar(GraphQLScalarValue.FLOAT));
		assertThat(((JsonFloatType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("float")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForInteger() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("integer", new GraphQLScalar(GraphQLScalarValue.INT));
		assertThat(((JsonIntegerType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("integer")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForID() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("ID", new GraphQLScalar(GraphQLScalarValue.ID));
		assertThat(((JsonStringType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("ID")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForString() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("String", new GraphQLScalar(GraphQLScalarValue.STRING));
		assertThat(((JsonStringType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("String")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForEnum() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("Enum", new GraphQLEnum());
		assertThat(((JsonStringType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("Enum")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForArrayOfObjects() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("ArrayOfObjects", new GraphQLList(new GraphQLObject(), Dimensionality.SINGLE));
		assertThat(((JsonArrayType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("ArrayOfObjects")));
	    assertThat(result.getChildCount(), is(equalTo(1)));
	    assertThat(((DefaultMutableTreeNode) result.getChildAt(0)).getUserObject(), is(Matchers.instanceOf(JsonObjectType.class)));
	}
	
	@Test
	public void parseTreeForArrayOfEnums() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("ArrayOfEnums", new GraphQLList(new GraphQLEnum(), Dimensionality.SINGLE));
		assertThat(((JsonArrayType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("ArrayOfEnums")));
	    assertThat(result.getChildCount(), is(equalTo(1)));
	    assertThat(((DefaultMutableTreeNode) result.getChildAt(0)).getUserObject(), is(Matchers.instanceOf(JsonStringType.class)));
	}
	
	@Test
	public void parseTreeForArrayOfScalars() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("ArrayOfScalars", new GraphQLList(new GraphQLScalar(GraphQLScalarValue.FLOAT), Dimensionality.SINGLE));
		assertThat(((JsonArrayType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("ArrayOfScalars")));
	    assertThat(result.getChildCount(), is(equalTo(1)));
	    assertThat(((DefaultMutableTreeNode) result.getChildAt(0)).getUserObject(), is(Matchers.instanceOf(JsonFloatType.class)));
	}
	
	@Test
	public void parseTreeForObject() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("Obj", new GraphQLObject());
		assertThat(((JsonObjectType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("Obj")));
	    assertThat(result.getChildCount(), is(equalTo(0)));
	}
	
	@Test
	public void parseTreeForReference() throws Exception {
		
		GraphQLSchema schema = new GraphQLSchema();
		GraphQLObject refObject = new GraphQLObject();
		refObject.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		schema.addType("foo", refObject);
		GraphQLNodeParser parser = new GraphQLNodeParser(schema);
		DefaultMutableTreeNode result = parser.parseTree("Ref", new GraphQLReference("foo"));
		assertThat(((JsonObjectType)result.getUserObject()).getJsonPathNodeName(), is(equalTo("Ref")));
	    assertThat(result.getChildCount(), is(equalTo(1)));
	    assertThat(((DefaultMutableTreeNode) result.getChildAt(0)).getUserObject(), is(Matchers.instanceOf(JsonStringType.class)));
	}
}
