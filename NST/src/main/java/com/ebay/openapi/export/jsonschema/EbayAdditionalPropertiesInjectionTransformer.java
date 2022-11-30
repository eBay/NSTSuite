package com.ebay.openapi.export.jsonschema;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Modified version of com.atlassian.oai.validator.schema.transform.AdditionalPropertiesInjectionTransformer.
 * Omitting additionalProperties being set on allOf models.
 */
public class EbayAdditionalPropertiesInjectionTransformer extends EbaySchemaTransformer {

    private static final EbayAdditionalPropertiesInjectionTransformer INSTANCE = new EbayAdditionalPropertiesInjectionTransformer();

    public static EbayAdditionalPropertiesInjectionTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public void apply(final JsonNode schemaObject, final SchemaTransformationContext context) {
        if (schemaObject == null || !context.isAdditionalPropertiesValidationEnabled()) {
            return;
        }

        if (!containsAllOf(schemaObject) && !hasAdditionalFieldSet(schemaObject) && !hasDiscriminatorField(schemaObject) && hasPropertiesField(schemaObject)) {
            disableAdditionalProperties((ObjectNode) schemaObject);
        }

        applyToChildSchemas(schemaObject, child -> apply(child, context));
    }

    private boolean containsAllOf(JsonNode schemaObject) {
    	
    	JsonNode allOfField = schemaObject.get(ALLOF_FIELD);
    	if (allOfField == null) {
    		return false;
    	}
    	return true;
    }
}
