package com.ebay.openapi.export.jsonschema;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EbayRemoveTypesTransformer extends EbaySchemaTransformer {

    private static final EbayRemoveTypesTransformer INSTANCE = new EbayRemoveTypesTransformer();
    private static final String TYPES = "types";

    public static EbayRemoveTypesTransformer getInstance() {
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

        if (schemaObject instanceof ObjectNode && schemaObject.has(TYPES)) {
            ObjectNode objNode = (ObjectNode) schemaObject;
            objNode.remove(TYPES);
        }

        applyToChildSchemas(schemaObject, child -> apply(child, context));
    }
}
