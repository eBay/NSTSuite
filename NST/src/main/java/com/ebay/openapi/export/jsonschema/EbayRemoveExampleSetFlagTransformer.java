package com.ebay.openapi.export.jsonschema;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EbayRemoveExampleSetFlagTransformer extends EbaySchemaTransformer {

	private static final EbayRemoveExampleSetFlagTransformer INSTANCE = new EbayRemoveExampleSetFlagTransformer();

	public static EbayRemoveExampleSetFlagTransformer getInstance() {
		return INSTANCE;
	}

	@Override
	public void apply(JsonNode schemaObject, SchemaTransformationContext context) {

		if (schemaObject == null) {
			return;
		}

		if (!(context.isRequest() || context.isResponse())) {
			return;
		}

		if (schemaObject instanceof ObjectNode && schemaObject.has("exampleSetFlag")) {
			((ObjectNode) schemaObject).remove("exampleSetFlag");
		}

		applyToChildSchemas(schemaObject, child -> apply(child, context));
	}
}
