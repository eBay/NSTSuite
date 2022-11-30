package com.ebay.openapi.export.jsonschema;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Copy of com.atlassian.oai.validator.schema.transform.RequiredFieldTransformer.
 * Wholesale copy for the sake of having a common base class.
 */
public class EbayRequiredFieldTransformer extends EbaySchemaTransformer {

	private static final EbayRequiredFieldTransformer INSTANCE = new EbayRequiredFieldTransformer();

    public static EbayRequiredFieldTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public void apply(final JsonNode schemaObject, final SchemaTransformationContext context) {
        if (schemaObject == null) {
            return;
        }

        if (!(context.isRequest() || context.isResponse())) {
            return;
        }

        if (hasRequiredFields(schemaObject)) {
            final List<String> adjustedRequired = getRequiredFieldNames(schemaObject)
                    .stream()
                    .filter(fieldName ->
                            isNotReadOnlyInRequest(context, schemaObject, fieldName) ||
                                    isNotWriteOnlyInResponse(context, schemaObject, fieldName)
                    )
                    .collect(toList());

            setRequiredFieldNames(schemaObject, adjustedRequired);
        }

        applyToChildSchemas(schemaObject, child -> apply(child, context));
    }

    private boolean isNotWriteOnlyInResponse(final SchemaTransformationContext context,
                                             final JsonNode schemaObject,
                                             final String fieldName) {
        return context.isResponse() && !isWriteOnly(property(schemaObject, fieldName));
    }

    private boolean isNotReadOnlyInRequest(final SchemaTransformationContext context,
                                           final JsonNode schemaObject,
                                           final String fieldName) {
        return context.isRequest() && !isReadOnly(property(schemaObject, fieldName));
    }

}
