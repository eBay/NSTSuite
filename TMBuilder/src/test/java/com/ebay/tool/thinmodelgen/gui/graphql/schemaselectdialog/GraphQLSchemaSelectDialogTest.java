package com.ebay.tool.thinmodelgen.gui.graphql.schemaselectdialog;

import org.testng.annotations.Test;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.types.GraphQLScalar;
import com.ebay.graphql.types.GraphQLScalar.GraphQLScalarValue;

public class GraphQLSchemaSelectDialogTest {

	  // Enable this test method to manually evaluate changes to the GraphQL
	  // selection dialog. Otherwise, leave this disabled.
	  @Test(enabled = false)
	  public void showDialog() {
	    GraphQLSchema schema = new GraphQLSchema();
	    schema.addQuery("testQuery", new GraphQLScalar(GraphQLScalarValue.STRING));
	    schema.addQuery("secondQuery", new GraphQLScalar(GraphQLScalarValue.STRING));
	    schema.addMutation("testMutation", new GraphQLScalar(GraphQLScalarValue.STRING));
	    GraphQLSchemaSelectDialog dialog = new GraphQLSchemaSelectDialog(schema, null);
	    dialog.getPayload();
	  }
}
