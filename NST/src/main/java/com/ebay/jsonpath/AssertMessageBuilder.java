package com.ebay.jsonpath;

public class AssertMessageBuilder {

  public static String build(String jsonPath, String assertDescription) {

    return String.format("Path %s failed %s.", jsonPath, assertDescription);
  }
}
