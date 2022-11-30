package com.ebay.tool.thinmodelgen.jsonschema.parser.openapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import com.ebay.tool.thinmodelgen.gui.TMGuiConstants;
import com.ebay.tool.thinmodelgen.jsonschema.parser.NodeParsingStackLogger;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

public class OpenApiNodeParser {

  private static final String stringPrimitiveLabel = "String primitive";
  private static final String integerPrimitiveLabel = "Integer primitive";
  private static final String booleanPrimitiveLabel = "Boolean primitive";

  private NodeParsingStackLogger stackLogger;

  public OpenApiNodeParser(NodeParsingStackLogger stackLogger) {
    this.stackLogger = Objects.requireNonNull(stackLogger, "NodeParsingStackLogger instance MUST NOT be null.");
  }

  public List<DefaultMutableTreeNode> routeParser(String fieldName, Schema<?> schema) throws IOException {

    stackLogger.push(fieldName);

    /*
     *
     * OpenAPI Schema is the base type for each model. Each type has
     * standard keys:
     *
     * type, properties
     *
     * for polymorphic cases, type and properties will not be present and
     * instead we will have:
     *
     * anyOf, oneOf
     *
     * Processing:
     *
     * Extract the type, to decide which parser to use below. For, type ==
     * Object, we will consume the properties as the next set of child fields to
     * parse.
     *
     * If type is not present, then we need to handle anyOf or oneOf cases. Each
     * OpenAPI object in the anyOf or oneOf array will get the same name as the
     * field that defines the anyOf or oneOf array plus an identifier for the
     * anyOf or oneOf case and an index.
     *
     */

    if (schema == null) {
      stackLogger.pop();
      return new ArrayList<>();
    }

    List<DefaultMutableTreeNode> nodes = null;
    String typeName = schema.getType();

    if (schema instanceof ComposedSchema) {

      ComposedSchema composedSchema = (ComposedSchema) schema;

      if (composedSchema.getAnyOf() != null) {
        nodes = parseAnyOfCase(fieldName, schema);
      } else if (composedSchema.getOneOf() != null) {
        nodes = parseOneOfCase(fieldName, schema);
      }

    } else if (typeName == null || typeName.equalsIgnoreCase("object")) {
      // Composite models will have typeName set to null. Parse these as an object.
      nodes = parseObject(fieldName, schema);
    } else if (typeName.equalsIgnoreCase("array")) {
      nodes = parseArray(fieldName, schema);
    } else if (typeName.equalsIgnoreCase("string")) {
      nodes = parseString(fieldName, schema);
    } else if (typeName.equalsIgnoreCase("boolean")) {
      nodes = parseBoolean(fieldName, schema);
    } else if (typeName.equalsIgnoreCase("integer")) {
      nodes = parseInteger(fieldName, schema);
    } else if (typeName.equalsIgnoreCase("number")) {
      nodes = parseNumber(fieldName, schema);
    } else {
      throw new IOException(String.format("Unable to parse unknown case. %s", fieldName));
    }

    stackLogger.pop();
    return nodes;
  }

  public List<DefaultMutableTreeNode> parseString(String fieldName, Schema<?> schema) {

    String[] enums = null;
    @SuppressWarnings("unchecked")
    List<String> enumValues = (List<String>) schema.getEnum();
    if (enumValues != null) {
      enums = enumValues.toArray(new String[0]);
    }
    return Arrays.asList(new DefaultMutableTreeNode(new JsonStringType(fieldName, enums)));
  }

  public List<DefaultMutableTreeNode> parseBoolean(String fieldName, Schema<?> jsonObject) {
    return Arrays.asList(new DefaultMutableTreeNode(new JsonBooleanType(fieldName)));
  }

  public List<DefaultMutableTreeNode> parseInteger(String fieldName, Schema<?> jsonObject) {
    return Arrays.asList(new DefaultMutableTreeNode(new JsonIntegerType(fieldName)));
  }

  public List<DefaultMutableTreeNode> parseNumber(String fieldName, Schema<?> jsonObject) {
    return Arrays.asList(new DefaultMutableTreeNode(new JsonFloatType(fieldName)));
  }

  public List<DefaultMutableTreeNode> parseOneOfCase(String fieldName, Schema<?> schema) throws IOException {

    // We obtain a schema with a single field: oneOf
    // which is a JsonArray of JsonObjects.
    // When we route each object through it will come
    // back as a JsonObject type and we will need
    // to convert it to a oneOf type.

    // The anyOf/oneOf cases are why each parser method must
    // return a list of nodes. Otherwise we could expect to always
    // return a single node.

    ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();
    ComposedSchema composedSchema = (ComposedSchema) schema;

    @SuppressWarnings("rawtypes")
    List<Schema> oneOfs = composedSchema.getOneOf();

    for (Schema<?> oneOf : oneOfs) {

      String newFieldName = String.format("%s", fieldName);

      try {
        List<DefaultMutableTreeNode> newNodes = routeParser(newFieldName, oneOf);

        for (DefaultMutableTreeNode newNode : newNodes) {
          if (newNode.getUserObject() instanceof JsonObjectType) {
            if (TMGuiConstants.DICTIONARY_KEY.equals(fieldName)) {
              newNode.setUserObject(new JsonOneOfDictionaryType(fieldName));
            } else {
              newNode.setUserObject(new JsonOneOfType(fieldName));
            }
          }
          nodes.add(newNode);
        }

      } catch (IOException e) {
        throw new IOException(String.format("%s.%s", newFieldName, e.getMessage()));
      }
    }

    return nodes;
  }

  public List<DefaultMutableTreeNode> parseAnyOfCase(String fieldName, Schema<?> schema) throws IOException {

    // We obtain a JsonObject with a single field: anyOf
    // which is a JsonArray of JsonObjects.
    // When we route each object through it will come
    // back as a JsonObject type and we will need
    // to convert it to a oneOf type.

    // The anyOf/oneOf cases are why each parser method must
    // return a list of nodes. Otherwise we could expect to always
    // return a single node.

    ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();

    ComposedSchema composedSchema = (ComposedSchema) schema;

    @SuppressWarnings("rawtypes")
    List<Schema> anyOfs = composedSchema.getAnyOf();

    for (Schema<?> anyOf : anyOfs) {

      String newFieldName = String.format("%s", fieldName);

      try {
        List<DefaultMutableTreeNode> newNodes = routeParser(newFieldName, anyOf);

        for (DefaultMutableTreeNode newNode : newNodes) {
          if (newNode.getUserObject() instanceof JsonObjectType) {
            if (TMGuiConstants.DICTIONARY_KEY.equals(fieldName)) {
              newNode.setUserObject(new JsonAnyOfDictionaryType(fieldName));
            } else {
              newNode.setUserObject(new JsonAnyOfType(fieldName));
            }
          }
          nodes.add(newNode);
        }

      } catch (IOException e) {
        throw new IOException(String.format("%s.%s", newFieldName, e.getMessage()));
      }
    }

    return nodes;
  }

  public List<DefaultMutableTreeNode> parseArray(String fieldName, Schema<?> schema) throws IOException {

    ArraySchema array = (ArraySchema) schema;

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new JsonArrayType(fieldName));

    Schema<?> items = array.getItems();

    @SuppressWarnings("rawtypes")
    Map<String, Schema> properties = items.getProperties();

    if (properties != null) {

      Set<String> keys = properties.keySet();

      for (String key : keys) {

        Schema<?> obj = properties.get(key);
        try {
          List<DefaultMutableTreeNode> newNodes = routeParser(key, obj);
          for (DefaultMutableTreeNode newNode : newNodes) {
            node.add(newNode);
          }
        } catch (IOException e) {
          throw new IOException(String.format("%s.%s", fieldName, e.getMessage()));
        }
      }

    } else {

      try {
        List<DefaultMutableTreeNode> newNodes = routeParser(fieldName, items);
        for (DefaultMutableTreeNode newNode : newNodes) {

          JsonBaseType nodeType = (JsonBaseType) newNode.getUserObject();

          // If it is a ComposedSchema (anyOf/oneOf) and a primitive type, or just a
          // primitive type skip it in the JSON path.
          // Rename it to the primitive type.
          if (items instanceof ComposedSchema) {
              if (nodeType instanceof JsonStringType) {
                nodeType.setSkipInJsonPath();
                nodeType.setPresentationName(stringPrimitiveLabel);
              } else if (nodeType instanceof JsonIntegerType) {
                nodeType.setSkipInJsonPath();
                nodeType.setPresentationName(integerPrimitiveLabel);
              } else if (nodeType instanceof JsonBooleanType) {
                nodeType.setSkipInJsonPath();
                nodeType.setPresentationName(booleanPrimitiveLabel);
              }
          } else if (items instanceof StringSchema) {
            nodeType.setSkipInJsonPath();
            nodeType.setPresentationName(stringPrimitiveLabel);
          } else if (items instanceof IntegerSchema) {
            nodeType.setSkipInJsonPath();
            nodeType.setPresentationName(integerPrimitiveLabel);
          } else if (items instanceof BooleanSchema) {
            nodeType.setSkipInJsonPath();
            nodeType.setPresentationName(booleanPrimitiveLabel);
          }

          node.add(newNode);
        }
      } catch (IOException e) {
        throw new IOException(String.format("%s.%s", fieldName, e.getMessage()));
      }
    }

    return Arrays.asList(node);
  }

  public List<DefaultMutableTreeNode> parseObject(String fieldName, Schema<?> schema) throws IOException {

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new JsonObjectType(fieldName));

    @SuppressWarnings("rawtypes")
    Map<String, Schema> properties = schema.getProperties();

    if (properties == null) {
      // This is a key value pair map
      node = new DefaultMutableTreeNode(new JsonDictionaryType(fieldName));
      Schema<?> additionalProperties = (Schema<?>) schema.getAdditionalProperties();
      String key = TMGuiConstants.DICTIONARY_KEY;

      try {
        List<DefaultMutableTreeNode> newNodes = routeParser(key, additionalProperties);
        for (DefaultMutableTreeNode newNode : newNodes) {
          node.add(newNode);
        }
      } catch (IOException e) {
        throw new IOException(String.format("%s.%s", key, e.getMessage()));
      }

    } else {
      Set<String> keys = properties.keySet();

      for (String key : keys) {

        Schema<?> obj = properties.get(key);
        try {
          List<DefaultMutableTreeNode> newNodes = routeParser(key, obj);
          for (DefaultMutableTreeNode newNode : newNodes) {
            node.add(newNode);
          }
        } catch (IOException e) {
          throw new IOException(String.format("%s.%s", key, e.getMessage()));
        }
      }
    }

    return Arrays.asList(node);
  }
}
