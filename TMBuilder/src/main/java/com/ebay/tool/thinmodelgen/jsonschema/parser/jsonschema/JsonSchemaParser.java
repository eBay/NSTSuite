package com.ebay.tool.thinmodelgen.jsonschema.parser.jsonschema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import com.ebay.tool.thinmodelgen.gui.TMGuiConstants;
import com.ebay.tool.thinmodelgen.jsonschema.parser.NodeParsingStackLogger;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParser;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonAnyOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonArrayType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfDictionaryType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonOneOfType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import com.ebay.utility.unzip.UnzipUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonSchemaParser implements SchemaParser {

  private NodeParsingStackLogger stackLogger = new NodeParsingStackLogger();

  @Override
  public DefaultMutableTreeNode parseSchema(String schemaPath) {

    File jsonSchemaFile;
    List<String> unzippedFilePaths = null;

    if (schemaPath.endsWith(".zip")) {

      try {
        unzippedFilePaths = UnzipUtil.unzipFile(schemaPath);
      } catch (IOException e) {
    	throw new IllegalStateException(e);
      }

      if (unzippedFilePaths.size() != 1) {
        UnzipUtil.cleanupExtractedFiles(unzippedFilePaths);
        System.out.println(String.format("Zip file [%s] does not contain a single json file. Unable to determine correct JSON file to parse.", schemaPath));
        return new DefaultMutableTreeNode();
      }
      schemaPath = unzippedFilePaths.get(0);
    }

    jsonSchemaFile = new File(schemaPath);
    FileReader fileReader = null;

    try {
      fileReader = new FileReader(jsonSchemaFile);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
      return new DefaultMutableTreeNode();
    }

    JsonObject json = JsonParser.parseReader(fileReader).getAsJsonObject();
    UnzipUtil.cleanupExtractedFiles(unzippedFilePaths);

    try {
      stackLogger.push("$");
      List<DefaultMutableTreeNode> newNodes = routeParser("$", json);
      if (newNodes.size() != 1) {
        DefaultMutableTreeNode collapsedNode = new DefaultMutableTreeNode();

        for (DefaultMutableTreeNode node : newNodes) {
          collapsedNode.add(node);
        }

        return collapsedNode;
      }
      return newNodes.get(0);
    } catch (IOException e) {
      System.out.println(String.format("$.%s", e.getMessage()));
      return new DefaultMutableTreeNode();
    }
  }

  private List<DefaultMutableTreeNode> routeParser(String fieldName, JsonObject jsonObject) throws IOException {

    stackLogger.push(fieldName);

    /*
     *
     * JSON Schema encapsulates each field in a JSON object. Each object has
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
     * JSON object in the anyOf or oneOf array will get the same name as the
     * field that defines the anyOf or oneOf array plus an identifier for the
     * anyOf or oneOf case and an index.
     *
     */

    if (jsonObject == null) {
      stackLogger.pop();
      return new ArrayList<>();
    }

    List<DefaultMutableTreeNode> nodes = null;
    JsonElement type = jsonObject.get("type");

    if (type != null) {

      String typeName = null;

      if (type.isJsonArray()) {

        JsonArray typeArray = type.getAsJsonArray();

        if (typeArray.size() > 2) {
          throw new IOException(String.format("%s has a type array with more than the expected two indexes.", fieldName));
        }

        for (int i = 0; i < typeArray.size(); i++) {
          String tmp = typeArray.get(i).getAsString();
          if (!tmp.equalsIgnoreCase("null")) {
            typeName = tmp;
            break;
          }
        }

      } else {
        typeName = type.getAsString();
      }

      if (typeName.equalsIgnoreCase("object")) {
        nodes = parseObject(fieldName, jsonObject);
      } else if (typeName.equalsIgnoreCase("array")) {
        nodes = parseArray(fieldName, jsonObject);
      } else if (typeName.equalsIgnoreCase("string")) {
        nodes = parseString(fieldName, jsonObject);
      } else if (typeName.equalsIgnoreCase("boolean")) {
        nodes = parseBoolean(fieldName, jsonObject);
      } else if (typeName.equalsIgnoreCase("integer")) {
        nodes = parseInteger(fieldName, jsonObject);
      } else if (typeName.equalsIgnoreCase("number")) {
        nodes = parseNumber(fieldName, jsonObject);
      } else {
        throw new IOException(String.format("Unable to parse unknown JSON type. %s", typeName));
      }

    } else if (jsonObject.get("anyOf") != null) {
      nodes = parseAnyOfCase(fieldName, jsonObject);
    } else if (jsonObject.get("oneOf") != null) {
      nodes = parseOneOfCase(fieldName, jsonObject);
    } else if (jsonObject.get("additionalProperties") != null) {
      nodes = parseObject(fieldName, jsonObject);
    } else {
      throw new IOException(String.format("Unable to parse unknown JSON polymorphic case. %s", fieldName));
    }

    stackLogger.pop();
    return nodes;
  }

  private List<DefaultMutableTreeNode> parseString(String fieldName, JsonObject jsonObject) {

    String[] enums = null;

    if (jsonObject.has("enum")) {
      JsonArray enumArray = jsonObject.getAsJsonArray("enum");
      enums = new String[enumArray.size()];
      for (int i = 0; i < enumArray.size(); i++) {
        JsonElement element = enumArray.get(i);
        if (element == null || element.isJsonNull()) {
          continue;
        }
        enums[i] = element.getAsString();
      }
    }

    return Arrays.asList(new DefaultMutableTreeNode(new JsonStringType(fieldName, enums)));
  }

  private List<DefaultMutableTreeNode> parseBoolean(String fieldName, JsonObject jsonObject) {
    return Arrays.asList(new DefaultMutableTreeNode(new JsonBooleanType(fieldName)));
  }

  private List<DefaultMutableTreeNode> parseInteger(String fieldName, JsonObject jsonObject) {
    return Arrays.asList(new DefaultMutableTreeNode(new JsonIntegerType(fieldName)));
  }

  private List<DefaultMutableTreeNode> parseNumber(String fieldName, JsonObject jsonObject) {
    return Arrays.asList(new DefaultMutableTreeNode(new JsonFloatType(fieldName)));
  }

  private List<DefaultMutableTreeNode> parseOneOfCase(String fieldName, JsonObject jsonObject) throws IOException {

    // We obtain a JsonObject with a single field: oneOf
    // which is a JsonArray of JsonObjects.
    // When we route each object through it will come
    // back as a JsonObject type and we will need
    // to convert it to a oneOf type.

    // The anyOf/oneOf cases are why each parser method must
    // return a list of nodes. Otherwise we could expect to always
    // return a single node.

    ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();
    JsonArray jsonArray = jsonObject.getAsJsonArray("oneOf");

    for (int i = 0; i < jsonArray.size(); i++) {

      JsonObject jsonArrayIndex = jsonArray.get(i).getAsJsonObject();
      String newFieldName = String.format("%s", fieldName);

      try {
        List<DefaultMutableTreeNode> newNodes = routeParser(newFieldName, jsonArrayIndex);

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

  private List<DefaultMutableTreeNode> parseAnyOfCase(String fieldName, JsonObject jsonObject) throws IOException {

    // We obtain a JsonObject with a single field: anyOf
    // which is a JsonArray of JsonObjects.
    // When we route each object through it will come
    // back as a JsonObject type and we will need
    // to convert it to a oneOf type.

    // The anyOf/oneOf cases are why each parser method must
    // return a list of nodes. Otherwise we could expect to always
    // return a single node.

    ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();
    JsonArray jsonArray = jsonObject.getAsJsonArray("anyOf");

    for (int i = 0; i < jsonArray.size(); i++) {

      JsonObject jsonArrayIndex = jsonArray.get(i).getAsJsonObject();
      String newFieldName = String.format("%s", fieldName);

      try {
        List<DefaultMutableTreeNode> newNodes = routeParser(newFieldName, jsonArrayIndex);

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

  private List<DefaultMutableTreeNode> parseArray(String fieldName, JsonObject jsonObject) throws IOException {

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new JsonArrayType(fieldName));

    JsonObject items = jsonObject.getAsJsonObject("items");
    if (items == null) {
      return Arrays.asList(node);
    }

    JsonObject properties = items.getAsJsonObject("properties");

    if (properties != null) {

      Set<String> keys = properties.keySet();

      for (String key : keys) {

        JsonObject obj = properties.getAsJsonObject(key);
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
      JsonElement itemType = items.get("type");
      if (itemType != null && itemType.getAsString().equals("string")) {
        List<DefaultMutableTreeNode> newNodes = parseString(fieldName, jsonObject);
        for (DefaultMutableTreeNode newNode : newNodes) {
          node.add(newNode);
        }
      } else if (itemType != null && itemType.getAsString().equals("integer")) {
        List<DefaultMutableTreeNode> newNodes = parseInteger(fieldName, jsonObject);
        for (DefaultMutableTreeNode newNode : newNodes) {
          node.add(newNode);
        }
      } else if (itemType != null && itemType.getAsString().equals("boolean")) {
        List<DefaultMutableTreeNode> newNodes = parseBoolean(fieldName, jsonObject);
        for (DefaultMutableTreeNode newNode : newNodes) {
          node.add(newNode);
        }
      } else {
          try {
            List<DefaultMutableTreeNode> newNodes = routeParser(fieldName, items);
            for (DefaultMutableTreeNode newNode : newNodes) {
              node.add(newNode);
            }
          } catch (IOException e) {
            throw new IOException(String.format("%s.%s", fieldName, e.getMessage()));
          }
      }
    }

    return Arrays.asList(node);
  }

  private List<DefaultMutableTreeNode> parseObject(String fieldName, JsonObject jsonObject) throws IOException {

    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new JsonObjectType(fieldName));

    JsonObject properties = jsonObject.getAsJsonObject("properties");

    if (properties == null) {
      // This is a key value pair map
      node = new DefaultMutableTreeNode(new JsonDictionaryType(fieldName));
      JsonObject additionalProperties = jsonObject.getAsJsonObject("additionalProperties");
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

        JsonObject obj = properties.getAsJsonObject(key);
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
