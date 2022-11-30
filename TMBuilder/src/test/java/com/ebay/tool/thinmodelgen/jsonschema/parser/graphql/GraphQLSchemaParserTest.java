package com.ebay.tool.thinmodelgen.jsonschema.parser.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.swing.tree.DefaultMutableTreeNode;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ebay.tool.thinmodelgen.TMBuilderRuntimeArguments;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.TMFileSingleton;
import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.GraphQLOperation;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

public class GraphQLSchemaParserTest {

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		TMBuilderRuntimeArguments.getInstance(new String[] { "-v" });
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void parseNullPath() throws Exception {

		GraphQLSchemaParser parser = new GraphQLSchemaParser();
		parser.parseSchema(null);
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void parseInvalidPath() throws Exception {
		String schemaPath = String.format("%s/bad/path/to/unknown.graphqls", System.getProperty("user.dir"));

		GraphQLSchemaParser parser = new GraphQLSchemaParser();
		parser.parseSchema(schemaPath);
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void parseUnknownOperationName() throws Exception {
		
		String schemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testgraphql/enrollment.graphqls", System.getProperty("user.dir"));

	    SchemaParserPayload payload = new SchemaParserPayload().setGraphQLOperation(GraphQLOperation.QUERY).setGraphQLOperationName("missing");
	    TMFileSingleton.getInstance().setPayload(payload);
	    
	    GraphQLSchemaParser parser = new GraphQLSchemaParser();
	    parser.parseSchema(schemaPath);
	}

	@Test
	public void parseOddScalarNode() throws Exception {

		// The schema specified was loading with a scalar node labeled in-between the
		// array and item object definition. Using this to evaluate.

		String schemaPath = String.format(
				"%s/src/test/resources/com/ebay/tool/thinmodelgen/testgraphql/oddScalarNode.graphql",
				System.getProperty("user.dir"));

		SchemaParserPayload payload = new SchemaParserPayload().setGraphQLOperation(GraphQLOperation.QUERY)
				.setGraphQLOperationName("matchingProducts(input: MatchingProductsInput!)");
		TMFileSingleton.getInstance().setPayload(payload);

		GraphQLSchemaParser parser = new GraphQLSchemaParser();
		DefaultMutableTreeNode rootNode = parser.parseSchema(schemaPath);
		
		// Path in question is rootNode > data > matchingProducts > matchingProducts[] > Scalar (should be the reference type MatchingProduct instead of Scalar)
		DefaultMutableTreeNode incorrectPresentationNode = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0).getChildAt(0).getChildAt(0);
		JsonObjectType userObject = (JsonObjectType) incorrectPresentationNode.getUserObject();
		String actualToString = userObject.toString();
		assertThat(actualToString, is(equalTo("MatchingProduct (type: object)")));
	}

	@Test
	public void parseSchemaFromSavedFile() throws Exception {

		String schemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testgraphql/enrollment.graphqls", System.getProperty("user.dir"));

	    SchemaParserPayload payload = new SchemaParserPayload().setGraphQLOperation(GraphQLOperation.QUERY).setGraphQLOperationName("isUserEnrolled(userEnrolledInput: UserEnrolledInput)");
	    TMFileSingleton.getInstance().setPayload(payload);
	    
	    GraphQLSchemaParser parser = new GraphQLSchemaParser();
	    DefaultMutableTreeNode rootTreeNode = parser.parseSchema(schemaPath);
	    
	    assertThat("Tree node MUST NOT be null.", rootTreeNode, is(notNullValue()));

	    assertThat(rootTreeNode.getChildCount(), is(equalTo(2)));
	    assertThat(((JsonObjectType)rootTreeNode.getUserObject()).getJsonPathNodeName(), is(equalTo("$")));

	    // Check for errors
	    DefaultMutableTreeNode errors = (DefaultMutableTreeNode) rootTreeNode.getChildAt(1);
	    assertThat(((JsonArrayType)errors.getUserObject()).getJsonPathNodeName(), is(equalTo("errors")));
	    assertThat(errors.getChildCount(), is(equalTo(3)));
	    
	    // Check errors fields (message, location, path)
	    DefaultMutableTreeNode message = (DefaultMutableTreeNode) errors.getChildAt(0);
	    assertThat(((JsonStringType)message.getUserObject()).getJsonPathNodeName(), is(equalTo("message")));
	    
	    DefaultMutableTreeNode location = (DefaultMutableTreeNode) errors.getChildAt(1);
	    assertThat(((JsonArrayType)location.getUserObject()).getJsonPathNodeName(), is(equalTo("locations")));
	    
	    DefaultMutableTreeNode path = (DefaultMutableTreeNode) errors.getChildAt(2);
	    assertThat(((JsonArrayType)path.getUserObject()).getJsonPathNodeName(), is(equalTo("path")));
	    
	    // Check for data
	    DefaultMutableTreeNode data = (DefaultMutableTreeNode) rootTreeNode.getChildAt(0);
	    assertThat(((JsonObjectType)data.getUserObject()).getJsonPathNodeName(), is(equalTo("data")));
	    assertThat(data.getChildCount(), is(equalTo(1)));
	    
	    // Check for isUserEnrolled
	    DefaultMutableTreeNode isUserEnrolled = (DefaultMutableTreeNode) data.getChildAt(0);
	    assertThat(((JsonObjectType)isUserEnrolled.getUserObject()).getJsonPathNodeName(), is(equalTo("isUserEnrolled")));
	    assertThat(isUserEnrolled.getChildCount(), is(equalTo(4)));
	    
	    // Check didUserPassRISK
	    DefaultMutableTreeNode didUserPassRISK = (DefaultMutableTreeNode) isUserEnrolled.getChildAt(0);
	    assertThat(((JsonBooleanType)didUserPassRISK.getUserObject()).getJsonPathNodeName(), is(equalTo("didUserPassRISK")));
	    assertThat(didUserPassRISK.getChildCount(), is(equalTo(0)));
	    
	    // Check enrollmentTime
	    DefaultMutableTreeNode enrollmentTime = (DefaultMutableTreeNode) isUserEnrolled.getChildAt(1);
	    assertThat(((JsonStringType)enrollmentTime.getUserObject()).getJsonPathNodeName(), is(equalTo("enrollmentTime")));
	    assertThat(enrollmentTime.getChildCount(), is(equalTo(0)));
	    
	    // Check enrollmentStatus
	    DefaultMutableTreeNode enrollmentStatus = (DefaultMutableTreeNode) isUserEnrolled.getChildAt(2);
	    assertThat(((JsonStringType)enrollmentStatus.getUserObject()).getJsonPathNodeName(), is(equalTo("enrollmentStatus")));
	    assertThat(enrollmentStatus.getChildCount(), is(equalTo(0)));
	    
	    // Check enrollmentLocales
	    DefaultMutableTreeNode enrollmentLocales = (DefaultMutableTreeNode) isUserEnrolled.getChildAt(3);
	    assertThat(((JsonArrayType)enrollmentLocales.getUserObject()).getJsonPathNodeName(), is(equalTo("enrollmentLocales")));
	    assertThat(enrollmentLocales.getChildCount(), is(equalTo(1)));
	    DefaultMutableTreeNode enrollmentLocalesType = (DefaultMutableTreeNode) enrollmentLocales.getChildAt(0);
	    assertThat(((JsonStringType)enrollmentLocalesType.getUserObject()).getJsonPathNodeName(), is(equalTo("enrollmentLocales")));
	    assertThat(enrollmentLocalesType.getChildCount(), is(equalTo(0)));
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void schemaWithReferenceMissing() {
		
		// ImageFeatures type definition is missing
		String schemaPath = String.format("%s/src/test/resources/com/ebay/tool/thinmodelgen/testgraphqlisolated/missingReference.graphqls", System.getProperty("user.dir"));

	    SchemaParserPayload payload = new SchemaParserPayload().setGraphQLOperation(GraphQLOperation.QUERY).setGraphQLOperationName("matchingProducts(input: MatchingProductsInput!)");
	    TMFileSingleton.getInstance().setPayload(payload);
	    
	    GraphQLSchemaParser parser = new GraphQLSchemaParser();
	    parser.parseSchema(schemaPath);
	}
}
