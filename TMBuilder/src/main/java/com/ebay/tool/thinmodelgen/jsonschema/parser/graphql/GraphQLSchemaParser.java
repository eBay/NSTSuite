package com.ebay.tool.thinmodelgen.jsonschema.parser.graphql;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.swing.tree.DefaultMutableTreeNode;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.parser.GraphQLParser;
import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.gui.graphql.schemaselectdialog.GraphQLSchemaSelectDialog;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.TMFileSingleton;
import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.GraphQLOperation;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParser;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;

public class GraphQLSchemaParser implements SchemaParser {

	@Override
	public DefaultMutableTreeNode parseSchema(String schemaPath) {
		
		schemaPath = Objects.requireNonNull(schemaPath, "Schema path MUST NOT be null.");

		File schemaFile = new File(schemaPath);
		if (!schemaFile.exists()) {
			throw new IllegalStateException(String.format("Schema path [%s] does not exist.", schemaPath));
		}
		
		GraphQLParser graphQLParser = new GraphQLParser();
		GraphQLSchema schema = graphQLParser.parseGraphQL(schemaFile);
		
		SchemaParserPayload payload = TMFileSingleton.getInstance().getPayload();
		
	    // Check the payload parameters
	    if (payload == null) {
	      // Prompt the user to select.
	      GraphQLSchemaSelectDialog schemaSelectDialog = new GraphQLSchemaSelectDialog(schema, MainWindow.getInstance());
	      payload = schemaSelectDialog.getPayload();
	      TMFileSingleton.getInstance().setPayload(payload);
	    }
		
		GraphQLOperation graphQLOperation = Objects.requireNonNull(payload.getGraphQLOperation(), "GraphQL operation must be specified.");
		String graphQLOperationName = Objects.requireNonNull(payload.getGraphQLOperationName(), "GraphQL operation name must be specified.");
		
		GraphQLNodeParser nodeParser = new GraphQLNodeParser(schema);
		List<DefaultMutableTreeNode> rootNodes = nodeParser.routeParser(graphQLOperation, graphQLOperationName);
		return rootNodes.get(0);
	}

}
