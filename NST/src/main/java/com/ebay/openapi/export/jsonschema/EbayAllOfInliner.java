package com.ebay.openapi.export.jsonschema;

import java.util.*;
import java.util.Map.Entry;

import io.swagger.v3.oas.models.media.*;
import org.apache.commons.collections.*;
import org.apache.commons.lang3.StringUtils;

import com.ebay.nst.schema.validation.support.SchemaValidationException;

@SuppressWarnings("rawtypes")
class EbayAllOfInliner {

    private final Map<String, Schema> inlineSchemas = new HashMap<>();
    private final Map<String, ComposedSchema> allOfSchemas = new HashMap<>();

    EbayAllOfInliner(Map<String, Schema> typeDefinitions) {
        for (Entry<String, Schema> entry : typeDefinitions.entrySet()) {
            Schema schema = entry.getValue();
            if (isAllOf(schema)) {
                allOfSchemas.put(entry.getKey(), (ComposedSchema) schema);
            } else {
                inlineSchemas.put(entry.getKey(), schema);
            }
        }
    }

    /**
     * Convert allOf ComposedSchemas to inlined ObjectSchemas, copying the attributes from the $ref type onto the
     * object definition
     *
     * @return map of inlined schemas
     */
    Map<String, Schema> inlineSchemas() {
        while (!allOfSchemas.isEmpty()) {
            Iterator<Entry<String, ComposedSchema>> iter = allOfSchemas.entrySet().iterator();

            while (iter.hasNext()) {
                Entry<String, ComposedSchema> entry = iter.next();
                String key = entry.getKey();
                ComposedSchema schema = entry.getValue();
                Schema inlineSchema = null;

                Schema refSchema = findRefSchema(schema);
                if (refSchema != null) {
                    inlineSchema = findInlineSchemaDefinition(refSchema);
                    if (inlineSchema == null) {
                        continue;
                    }
                }

                ObjectSchema objectSchema = findObjectSchema(schema);
                if (inlineSchema != null) {
                    // copy the $ref schema attributes onto the simple object schema
                    appendSchemaAttributes(inlineSchema, objectSchema);
                }
                inlineSchemas.put(key, objectSchema);

                iter.remove();
            }
        }

        return inlineSchemas;
    }

    /**
     * @param schema schema object
     * @return map key for the referenced schema
     */
    private String getRefKey(Schema schema) {
        return schema.get$ref().substring(schema.get$ref().lastIndexOf('/') + 1);
    }

    /**
     * @param schema schema object
     * @return true if the schema object has a non-empty allOf
     */
    private boolean isAllOf(Schema schema) {
        if (schema instanceof ComposedSchema) {
            ComposedSchema composedSchema = (ComposedSchema) schema;
            return CollectionUtils.isNotEmpty(composedSchema.getAllOf());
        }
        return false;
    }

    /**
     * @param schema schema object
     * @return $ref object contained in allOf, if any
     */
    private Schema findRefSchema(ComposedSchema schema) {
        return schema.getAllOf().stream()
                     .filter(s -> StringUtils.isNotEmpty(s.get$ref()))
                     .findFirst()
                     .orElse(null);
    }

    /**
     * @param schema schema object
     * @return object definition contained in allOf, or a new instance
     */
    private ObjectSchema findObjectSchema(ComposedSchema schema) {
        ObjectSchema consolidatedSchema = (ObjectSchema) schema.getAllOf().stream()
                                    .filter(s -> s instanceof ObjectSchema)
                                    .findFirst()
                                    .orElse(new ObjectSchema());
        
        consolidatedSchema.setNullable(schema.getNullable());

        return consolidatedSchema;
    }

    /**
     * Find the inline Schema definition referenced by the passed Schema
     *
     * @param refSchema Schema with a $ref
     * @return the inline definition for $ref
     */
    private Schema findInlineSchemaDefinition(Schema refSchema) {
        Schema inlineSchema = refSchema;
        do {
            String refKey = getRefKey(inlineSchema);
            inlineSchema = inlineSchemas.get(refKey);
            if (inlineSchema == null && allOfSchemas.get(refKey) == null) {
                // a schema is referenced which is not in inlineSchemas or allOfSchemas, thus will never resolve
                throw new SchemaValidationException("Invalid $ref: " + refKey);
            }
        } while (inlineSchema != null && StringUtils.isNotEmpty(inlineSchema.get$ref()));
        return inlineSchema;
    }

    /**
     * Copy properties and required attributes from one schema on to another
     *
     * @param source schema with attributes to copy
     * @param target destination schema
     */
    @SuppressWarnings("unchecked")
    private void appendSchemaAttributes(Schema source, ObjectSchema target) {
        if (MapUtils.isNotEmpty(source.getProperties())) {
            if (target.getProperties() == null) {
                target.setProperties(new HashMap<>());
            }
            target.getProperties().putAll(source.getProperties());
        }
        if (CollectionUtils.isNotEmpty(source.getRequired())) {
            if (target.getRequired() == null) {
                target.setRequired(new ArrayList<>(source.getRequired()));
            } else {
                target.getRequired().addAll(source.getRequired());
            }
        }
    }
}
