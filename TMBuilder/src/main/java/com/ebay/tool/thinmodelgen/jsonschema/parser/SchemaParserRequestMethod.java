package com.ebay.tool.thinmodelgen.jsonschema.parser;

public enum SchemaParserRequestMethod {
  GET,
  POST,
  PUT,
  DELETE;

  public static SchemaParserRequestMethod getEnumForValue(String value) {

    for (SchemaParserRequestMethod method : SchemaParserRequestMethod.values()) {
      if (method.name().equalsIgnoreCase(value)) {
        return method;
      }
    }

    throw new IllegalArgumentException(String.format("Value [%s] does not match any of the possible enum values.", value));
  }
}
