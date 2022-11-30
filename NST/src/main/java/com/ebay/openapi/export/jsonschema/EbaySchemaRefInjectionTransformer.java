package com.ebay.openapi.export.jsonschema;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Modified version of com.atlassian.oai.validator.schema.transform.SchemaRefInjectionTransformer.
 * Changing the schema ref to JSON schema draft 4.
 */
public class EbaySchemaRefInjectionTransformer extends EbaySchemaTransformer {

    private static final EbaySchemaRefInjectionTransformer INSTANCE = new EbaySchemaRefInjectionTransformer();

    public static EbaySchemaRefInjectionTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public void apply(final JsonNode schemaObject, final SchemaTransformationContext context) {
        setSchemaRef(schemaObject, "http://json-schema.org/draft-04/schema#");
    }
}
