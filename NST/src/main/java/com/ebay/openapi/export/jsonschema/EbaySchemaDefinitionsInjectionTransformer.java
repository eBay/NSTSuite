package com.ebay.openapi.export.jsonschema;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

// Modified version of com.atlassian.oai.validator.schema.transform.SchemaDefinitionsInjectionTransformer.
// Modified to avoid deep copy of schema definitions.
public class EbaySchemaDefinitionsInjectionTransformer extends EbaySchemaTransformer {

    private static final EbaySchemaDefinitionsInjectionTransformer INSTANCE = new EbaySchemaDefinitionsInjectionTransformer();

    public static EbaySchemaDefinitionsInjectionTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public void apply(final JsonNode schemaObject, final SchemaTransformationContext context) {
        if (!(schemaObject instanceof ObjectNode)) {
            return;
        }

        ((ObjectNode) schemaObject).set("definitions", context.getSchemaDefinitions());
    }
}
