package com.ebay.jsonpath;

import java.io.Serializable;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPBooleanCheck implements JsonPathExecutor, NullCheck, Serializable {

  /**
   * Serialized ID.
   */
  private static final long serialVersionUID = 888395578771081432L;

  private Boolean expectedValue = null;
  private boolean isNull = false;

  /**
   * Run the baseline checks for a boolean - not null.
   */
  public JPBooleanCheck() {

  }

  /**
   * Make sure the boolean is equal to the value specified.
   *
   * @param value
   *          Expected value.
   * @return Current instance.
   */
  public JPBooleanCheck isEqualTo(Boolean value) {
    this.expectedValue = value;
    return this;
  }

  /**
   * Get the expected value.
   *
   * @return Expected value.
   */
  public Boolean getExpectedValue() {
    return expectedValue;
  }

  @Override
  public void checkIsNull(boolean mustBeNull) {
    isNull = mustBeNull;
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    Boolean value = null;

    try {
      value = documentContext.read(jsonPath);
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("unable to be cast to boolean. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (isNull) {
      softAssert.assertNull(value, AssertMessageBuilder.build(jsonPath, "with non-null boolean"));
    } else {
      softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, "with null boolean"));
    }

    if (value == null) {
      return;
    }

    if (expectedValue != null) {
      softAssert.assertEquals(value, expectedValue, AssertMessageBuilder.build(jsonPath, "with values that do not match"));
    }
  }
}
