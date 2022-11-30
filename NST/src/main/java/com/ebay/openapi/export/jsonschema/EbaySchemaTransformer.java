package com.ebay.openapi.export.jsonschema;

import com.atlassian.oai.validator.schema.transform.SchemaTransformationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.util.Json;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.emptyIterator;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.Arrays;

/**
 * This class is a complete copy of SchemaTransformer from Atlassian.
 * This was copied, because, of the need to access methods with
 * restricted permissions when modifying the the transformer classes.
 * 
 * A base class for transformers that mutate the state of a parsed Swagger/OpenAPI schema object.
 * <p>
 * Provides some helpers for working with the parsed schema objects. Used to avoid mutating the
 * {@link io.swagger.v3.oas.models.media.Schema} object which can't easily be cloned.
 */
public abstract class EbaySchemaTransformer {

    protected static final String ADDITIONAL_PROPERTIES_FIELD = "additionalProperties";
    protected static final String DISCRIMINATOR_FIELD = "discriminator";
    protected static final String PROPERTIES_FIELD = "properties";
    protected static final String REQUIRED_FIELD = "required";
    protected static final String READONLY_FIELD = "readOnly";
    protected static final String WRITEONLY_FIELD = "writeOnly";
    protected static final String TYPE_FIELD = "type";
    protected static final String COMPONENTS_FIELD = "components";
    protected static final String SCHEMAS_FIELD = "schemas";
    protected static final String ALLOF_FIELD = "allOf";
    protected static final String ANYOF_FIELD = "anyOf";
    protected static final String ONEOF_FIELD = "oneOf";
    protected static final String SCHEMA_REF_FIELD = "$schema";
    protected static final String DEFINITIONS_FIELD = "definitions";
    protected static final String NULLABLE = "nullable";

    private static final String OBJECT_TYPE = "object";
    private static final String ARRAY_TYPE = "array";

    /**
     * Apply the schema transformation to the given node.
     * <p>
     * Note: Callers should assume that this will mutate the given schema object.
     * 
     * @param schemaObject Schema note to apply transformation to.
     * @param context Schema transformation to apply.
     */
    public abstract void apply(final JsonNode schemaObject, final SchemaTransformationContext context);

    /**
     * Traverse each of the schema object's 'children' and apply the given consumer.
     * <p>
     * In this context, 'children' are:
     * <ul>
     *     <li>Nested schema defined in properties</li>
     *     <li>Composed schema in 'allOf', 'oneOf' or 'anyOf' compositions</li>
     *     <li>The 'items' schema of array definitions</li>
     *     <li>Schemas defined in the 'components' section (for top-level schemas)</li>
     *     <li>Definitions</li>
     * </ul>
     * 
     * @param schemaObject Schema object whose children will be processed.
     * @param consumer Consumer to apply to schema object's children.
     */
    static void applyToChildSchemas(final JsonNode schemaObject, final Consumer<JsonNode> consumer) {
        if (isArrayDefinition(schemaObject)) {
            consumer.accept(itemsDefinition(schemaObject));
            return;
        }
        properties(schemaObject).forEachRemaining(consumer);
        allOf(schemaObject).forEachRemaining(consumer);
        anyOf(schemaObject).forEachRemaining(consumer);
        oneOf(schemaObject).forEachRemaining(consumer);
        schemaComponents(schemaObject).forEachRemaining(consumer);
        definitions(schemaObject).forEachRemaining(consumer);
        additionalProperties(schemaObject).forEachRemaining(consumer);
    }

    protected static boolean hasAllOfField(final JsonNode n) {
        return n.has(ALLOF_FIELD);
    }

    protected static boolean hasPropertiesField(final JsonNode n) {
        return n != null && n.has(PROPERTIES_FIELD);
    }

    @Nullable
    protected static JsonNode itemsDefinition(final JsonNode n) {
        return n.get("items");
    }

    protected static boolean isObjectDefinition(@Nullable final JsonNode n) {
        if (n == null) {
            return false;
        }
        final JsonNode type = n.get(TYPE_FIELD);
        return type != null && type.textValue().equalsIgnoreCase(OBJECT_TYPE);
    }

    protected static boolean isArrayDefinition(@Nullable final JsonNode n) {
        if (n == null) {
            return false;
        }
        final JsonNode type = n.get(TYPE_FIELD);
        return type != null && type.textValue() != null && type.textValue().equalsIgnoreCase(ARRAY_TYPE);
    }

    protected static void disableAdditionalProperties(final ObjectNode n) {
        n.set(ADDITIONAL_PROPERTIES_FIELD, BooleanNode.getFalse());
    }
    
    protected static Iterator<JsonNode> additionalProperties(@Nullable final JsonNode n) {
    	if (n != null) {
    		if (n.has(ADDITIONAL_PROPERTIES_FIELD)) {
    			if (!n.get(ADDITIONAL_PROPERTIES_FIELD).isBoolean()) {
    				JsonNode jn = n.get(ADDITIONAL_PROPERTIES_FIELD);
    				return Arrays.asList(jn).iterator();
    			}
    		}
    	}
    	return emptyIterator();
    	//return n != null && n.has(ADDITIONAL_PROPERTIES_FIELD) && !n.isBoolean() ? n.get(ADDITIONAL_PROPERTIES_FIELD).iterator() : emptyIterator();
    }
    
    protected static Iterator<JsonNode> definitions(@Nullable final JsonNode n) {
    	return n != null && n.has(DEFINITIONS_FIELD) ? n.get(DEFINITIONS_FIELD).iterator() : emptyIterator();
    }

    protected static Iterator<JsonNode> properties(@Nullable final JsonNode n) {
        return n != null && n.has(PROPERTIES_FIELD) ? n.get(PROPERTIES_FIELD).iterator() : emptyIterator();
    }

    protected static Iterator<JsonNode> allOf(@Nullable final JsonNode n) {
        return n != null && n.has(ALLOF_FIELD) ? n.get(ALLOF_FIELD).iterator() : emptyIterator();
    }

    protected static Iterator<JsonNode> anyOf(@Nullable final JsonNode n) {
        return n != null && n.has(ANYOF_FIELD) ? n.get(ANYOF_FIELD).iterator() : emptyIterator();
    }

    protected static Iterator<JsonNode> oneOf(@Nullable final JsonNode n) {
        return n != null && n.has(ONEOF_FIELD) ? n.get(ONEOF_FIELD).iterator() : emptyIterator();
    }

    protected static Iterator<JsonNode> schemaComponents(@Nullable final JsonNode n) {
        return n != null && n.has(COMPONENTS_FIELD) && n.get(COMPONENTS_FIELD).has(SCHEMAS_FIELD) ?
                n.get(COMPONENTS_FIELD).get(SCHEMAS_FIELD).iterator() :
                emptyIterator();
    }

    @Nullable
    protected static JsonNode property(@Nullable final JsonNode n, final String fieldName) {
        if (n == null || !n.has(PROPERTIES_FIELD)) {
            return null;
        }

        return n.get(PROPERTIES_FIELD).get(fieldName);
    }

    protected static boolean hasDiscriminatorField(@Nullable final JsonNode n) {
        return n != null && n.has(DISCRIMINATOR_FIELD);
    }

    protected static boolean hasAdditionalFieldSet(@Nullable final JsonNode n) {
        return n != null && n.has(ADDITIONAL_PROPERTIES_FIELD);
    }

    protected static boolean hasRequiredFields(@Nullable final JsonNode n) {
        return n != null && n.has(REQUIRED_FIELD);
    }

    protected static List<String> getRequiredFieldNames(@Nullable final JsonNode n) {
        if (!hasRequiredFields(n)) {
            return emptyList();
        }

        return stream(n.get(REQUIRED_FIELD).spliterator(), false)
                .map(JsonNode::textValue)
                .collect(toList());
    }

    protected static void setRequiredFieldNames(@Nullable final JsonNode n, final List<String> required) {
        if (n == null || required.isEmpty()) {
            return;
        }
        final JsonNode requiredNode = Json.mapper().convertValue(required, JsonNode.class);
        ((ObjectNode) n).set(REQUIRED_FIELD, requiredNode);
    }

    protected static boolean isReadOnly(@Nullable final JsonNode n) {
        return n != null && n.has(READONLY_FIELD) && n.get(READONLY_FIELD).booleanValue();
    }

    protected static boolean isWriteOnly(@Nullable final JsonNode n) {
        return n != null && n.has(WRITEONLY_FIELD) && n.get(WRITEONLY_FIELD).booleanValue();
    }

    protected static void setSchemaRef(@Nullable final JsonNode n, final String value) {
        if (!(n instanceof ObjectNode)) {
            return;
        }
        ((ObjectNode) n).put(SCHEMA_REF_FIELD, value);
    }

}
