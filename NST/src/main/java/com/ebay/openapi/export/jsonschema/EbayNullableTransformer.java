package com.ebay.openapi.export.jsonschema;

import java.util.Arrays;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class EbayNullableTransformer extends EbaySchemaTransformer {

	private static final EbayNullableTransformer INSTANCE = new EbayNullableTransformer();

    public static EbayNullableTransformer getInstance() {
        return INSTANCE;
    }
	
	@Override
	public void apply(JsonNode schemaObject, SchemaTransformationContext context) {

		if (schemaObject instanceof ObjectNode && schemaObject.has(NULLABLE) && schemaObject.has(TYPE_FIELD)) {
			
			ObjectNode objNode = (ObjectNode) schemaObject;
			objNode.remove(NULLABLE);
			
			TextNode typeField = (TextNode) schemaObject.get(TYPE_FIELD);
			String typeValue = typeField.textValue();
			
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode nullableType = mapper.createArrayNode();
			nullableType.addAll(Arrays.asList(new TextNode(typeValue), new TextNode("null")));
			objNode.set(TYPE_FIELD, nullableType);
		}
		
		applyToChildSchemas(schemaObject, child -> apply(child, context));
	}

}
