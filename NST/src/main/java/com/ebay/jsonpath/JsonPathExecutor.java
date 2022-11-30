package com.ebay.jsonpath;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;

public interface JsonPathExecutor {

  /**
   * Execute the given jsonPath against the provided DocumentContext and record any validations against the SoftAssert instance.
   * @param jsonPath JsonPath to evaluate.
   * @param softAssert SoftAssert instance to perform checks against.
   * @param documentContext Json document to evaluate the JsonPath against. You likely want this instance to return null if the node cannot be found.
   */
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext);
}
