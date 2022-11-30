package com.ebay.tool.thinmodelgen.utility;

import com.ebay.jsonpath.JPBooleanCheck;
import com.ebay.jsonpath.JPDoubleCheck;
import com.ebay.jsonpath.JPIntegerCheck;
import com.ebay.jsonpath.JPJsonArrayCheck;
import com.ebay.jsonpath.JPJsonObjectCheck;
import com.ebay.jsonpath.JPListOfBooleanCheck;
import com.ebay.jsonpath.JPListOfDoubleCheck;
import com.ebay.jsonpath.JPListOfIntegerCheck;
import com.ebay.jsonpath.JPListOfStringCheck;
import com.ebay.jsonpath.JPStringCheck;
import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.jsonpath.TMJPBooleanCheck;
import com.ebay.jsonpath.TMJPDoubleCheck;
import com.ebay.jsonpath.TMJPIntegerCheck;
import com.ebay.jsonpath.TMJPJsonArrayCheck;
import com.ebay.jsonpath.TMJPJsonObjectCheck;
import com.ebay.jsonpath.TMJPListOfBooleanCheck;
import com.ebay.jsonpath.TMJPListOfDoubleCheck;
import com.ebay.jsonpath.TMJPListOfIntegerCheck;
import com.ebay.jsonpath.TMJPListOfStringCheck;
import com.ebay.jsonpath.TMJPStringCheck;

public class JPCheckConversion {

  /**
   * Converts the JPCheck* type to TMJPCheck* type to preserve backwards
   * compatibility.
   *
   * @param jpExecutor
   *          Source to convert.
   * @return Converted model, or original model if no match for conversion
   *         found.
   */
  public static JsonPathExecutor convertCheck(JsonPathExecutor jpExecutor) {

    if (jpExecutor instanceof JPBooleanCheck) {
      return new TMJPBooleanCheck((JPBooleanCheck) jpExecutor);
    } else if (jpExecutor instanceof JPDoubleCheck) {
      return new TMJPDoubleCheck((JPDoubleCheck) jpExecutor);
    } else if (jpExecutor instanceof JPIntegerCheck) {
      return new TMJPIntegerCheck((JPIntegerCheck) jpExecutor);
    } else if (jpExecutor instanceof JPJsonArrayCheck) {
      return new TMJPJsonArrayCheck((JPJsonArrayCheck) jpExecutor);
    } else if (jpExecutor instanceof JPJsonObjectCheck) {
      return new TMJPJsonObjectCheck((JPJsonObjectCheck) jpExecutor);
    } else if (jpExecutor instanceof JPListOfBooleanCheck) {
      return new TMJPListOfBooleanCheck((JPListOfBooleanCheck) jpExecutor);
    } else if (jpExecutor instanceof JPListOfDoubleCheck) {
      return new TMJPListOfDoubleCheck((JPListOfDoubleCheck) jpExecutor);
    } else if (jpExecutor instanceof JPListOfIntegerCheck) {
      return new TMJPListOfIntegerCheck((JPListOfIntegerCheck) jpExecutor);
    } else if (jpExecutor instanceof JPListOfStringCheck) {
      return new TMJPListOfStringCheck((JPListOfStringCheck) jpExecutor);
    } else if (jpExecutor instanceof JPStringCheck) {
      return new TMJPStringCheck((JPStringCheck) jpExecutor);
    }

    return jpExecutor;
  }
}
