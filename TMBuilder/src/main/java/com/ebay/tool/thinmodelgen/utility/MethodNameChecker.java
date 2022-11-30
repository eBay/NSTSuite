package com.ebay.tool.thinmodelgen.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameChecker {

  public static boolean isValueValidMethodName(String value) {
    if (value == null || value.isEmpty() || value.contains(" ")) {
      return false;
    }

    Pattern pattern = Pattern.compile("[$&+,:;=?@#|/'<>.^*()%!-01234567890]");
    Matcher matcher = pattern.matcher(value);

    return !matcher.find();
  }

}
