package com.ebay.jsonpath;

public class DoubleUtility {

  public static String removeTrailingZeros(Double value) {

    String val = Double.toString(value);
    val = val.replaceAll("\\.[0-9]+0+$", "");
    return val;
  }
}
