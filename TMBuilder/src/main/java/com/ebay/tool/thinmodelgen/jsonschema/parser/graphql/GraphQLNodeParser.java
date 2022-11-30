package com.ebay.tool.thinmodelgen.jsonschema.parser.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.swing.tree.DefaultMutableTreeNode;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.types.GraphQLEnum;
import com.ebay.graphql.types.GraphQLList;
import com.ebay.graphql.types.GraphQLList.Dimensionality;
import com.ebay.graphql.types.GraphQLObject;
import com.ebay.graphql.types.GraphQLReference;
import com.ebay.graphql.types.GraphQLScalar;
import com.ebay.graphql.types.GraphQLScalar.GraphQLScalarValue;
import com.ebay.graphql.types.GraphQLType;
import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.GraphQLOperation;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

public class GraphQLNodeParser {
	
	private GraphQLSchema schema;
	
	public GraphQLNodeParser(GraphQLSchema schema) {
		Objects.requireNonNull(schema, "Schema must not be null.");
		this.schema = schema;
	}

	public List<DefaultMutableTreeNode> routeParser(GraphQLOperation graphQLOperation, String graphQLOperationName) {
		
		Objects.requireNonNull(graphQLOperation, "GraphQL operation must not be null.");
		Objects.requireNonNull(graphQLOperationName, "GraphQL operation name must not be null.");
		
		GraphQLType operationRoot;
		
		switch (graphQLOperation) {
		case MUTATION:
			operationRoot = schema.getMutations().get(graphQLOperationName);
			break;
		case QUERY:
			operationRoot = schema.getQuerys().get(graphQLOperationName);
			break;
		case SUBSCRIPTION:
			operationRoot = schema.getSubscriptions().get(graphQLOperationName);
			break;
		default:
			throw new RuntimeException("Unknown GraphQL operation: " + graphQLOperation);
		}
		
		if (operationRoot == null) {
			throw new NullPointerException(String.format("GraphQL %s '%s' was not found.", graphQLOperation, graphQLOperationName));
		}
		
		return getTree(operationRoot, graphQLOperationName);
	}
	
	protected List<DefaultMutableTreeNode> getTree(GraphQLType type, String graphQLOperationName) {
		
		graphQLOperationName = stripOffParameters(graphQLOperationName);
		
		DefaultMutableTreeNode errors = new DefaultMutableTreeNode(new JsonArrayType("errors"));
		errors.add(new DefaultMutableTreeNode(new JsonStringType("message")));
		
		DefaultMutableTreeNode locations = new DefaultMutableTreeNode(new JsonArrayType("locations"));
		locations.add(new DefaultMutableTreeNode(new JsonIntegerType("line")));
		locations.add(new DefaultMutableTreeNode(new JsonIntegerType("column")));
		errors.add(locations);
		
		DefaultMutableTreeNode pathStep = new DefaultMutableTreeNode(new JsonStringType("step"));
		JsonBaseType baseType = (JsonBaseType) pathStep.getUserObject();
		baseType.setSkipInJsonPath();
		
		DefaultMutableTreeNode path = new DefaultMutableTreeNode(new JsonArrayType("path"));
		path.add(pathStep);
		errors.add(path);
		
		DefaultMutableTreeNode operation = parseTree(graphQLOperationName, type);
		
		DefaultMutableTreeNode data = new DefaultMutableTreeNode(new JsonObjectType("data"));
		data.add(operation);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new JsonObjectType("$"));
		root.add(data);
		root.add(errors);
		
		List<DefaultMutableTreeNode> treeRoot = new ArrayList<>();
		treeRoot.add(root);
		
		return treeRoot;
	}
	
	protected DefaultMutableTreeNode parseTree(String fieldName, GraphQLType type) {
		
		if (type instanceof GraphQLScalar) {
			GraphQLScalar graphQLScalar = (GraphQLScalar) type;
			GraphQLScalarValue value = graphQLScalar.getScalarValue();
			switch (value) {
			case BOOLEAN:
				return new DefaultMutableTreeNode(new JsonBooleanType(fieldName));
			case FLOAT:
				return new DefaultMutableTreeNode(new JsonFloatType(fieldName));
			case INT:
				return new DefaultMutableTreeNode(new JsonIntegerType(fieldName));
			case ID:
			case STRING:
			default:
				return new DefaultMutableTreeNode(new JsonStringType(fieldName));
			}
		} else if (type instanceof GraphQLEnum) {
			GraphQLEnum graphQLEnum = (GraphQLEnum) type;
			List<String> enumValues = graphQLEnum.getEnumValues();
			return new DefaultMutableTreeNode(new JsonStringType(fieldName, enumValues.toArray(new String[enumValues.size()])));
		} else if (type instanceof GraphQLList) {
			GraphQLList graphQLList = (GraphQLList) type;
			GraphQLType graphQLType = graphQLList.getType();
			Dimensionality dimensionality = graphQLList.getDimensionality();
			
			DefaultMutableTreeNode elementType = parseTree(fieldName, graphQLType);
			JsonBaseType baseType = (JsonBaseType) elementType.getUserObject();
			baseType.setSkipInJsonPath();
			
			if (graphQLType instanceof GraphQLObject) {
				baseType.setPresentationName("Object");
			} else if (graphQLType instanceof GraphQLEnum) {
				baseType.setPresentationName("Enum");
			} else if (graphQLType instanceof GraphQLReference) {
				baseType.setPresentationName(((GraphQLReference) graphQLType).getReferenceTypeName());
			} else {
				baseType.setPresentationName("Scalar");
			}
			
			DefaultMutableTreeNode arrayRoot = new DefaultMutableTreeNode(new JsonArrayType(fieldName));
			if (dimensionality == Dimensionality.MULTI) {
				DefaultMutableTreeNode innerArray = new DefaultMutableTreeNode(new JsonArrayType(fieldName));
				innerArray.add(elementType);
				arrayRoot.add(innerArray);
			} else {
				arrayRoot.add(elementType);
			}
			
			return arrayRoot;
		} else if (type instanceof GraphQLObject) {
			GraphQLObject graphQLObject = (GraphQLObject) type;
			DefaultMutableTreeNode objectNode = new DefaultMutableTreeNode(new JsonObjectType(fieldName));
			Map<String, GraphQLType> fields = graphQLObject.getFields();
			for (Entry<String, GraphQLType> field : fields.entrySet()) {
				objectNode.add(parseTree(stripOffParameters(field.getKey()), field.getValue()));
			}
			return objectNode;
		} else if (type instanceof GraphQLReference) {
			GraphQLReference reference = (GraphQLReference) type;
			GraphQLType referenceType = schema.getTypes().get(reference.getReferenceTypeName());
			return parseTree(fieldName, referenceType);
		} else {
			if (type != null) {
				throw new IllegalStateException(String.format("Unknown GraphQL type [%s] for fieldName [%s].", type.getClass().getName(), fieldName));
			} else {
				throw new IllegalStateException(String.format("GraphQL type is null for fieldName [%s].", fieldName));
			}
		}
	}
	
	private String stripOffParameters(String fieldName) {
		int indexOfOpeningParenthesis = fieldName.indexOf("(");
		if (indexOfOpeningParenthesis >= 0) {
			return fieldName.substring(0, indexOfOpeningParenthesis);
		}
		return fieldName;
	}
}
