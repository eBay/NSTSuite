package com.ebay.jsonpath;

import java.io.Serializable;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPIntegerCheck implements JsonPathExecutor, NullCheck, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5372890880704649745L;

  private Integer expectedValue = null;
  private boolean isNull = false;

  /**
   * Run the baseline checks for an integer - not null.
   */
  public JPIntegerCheck() {

  }

  /**
   * Make sure the integer is equal to the specified value.
   *
   * @param value
   *          Expected value.
   * @return Current instance.
   */
  public JPIntegerCheck isEqualTo(Integer value) {
    this.expectedValue = value;
    return this;
  }

  /**
   * Get the expected value.
   *
   * @return Expected value.
   */
  public Integer getExpectedValue() {
    return expectedValue;
  }

  @Override
  public void checkIsNull(boolean mustBeNull) {
    isNull = mustBeNull;
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    Integer value = null;

    try {
      value = documentContext.read(jsonPath);
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("unable to be cast to integer. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (isNull) {
      softAssert.assertNull(value, AssertMessageBuilder.build(jsonPath, "with non-null integer"));
    } else {
      softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, "with null integer"));
    }

    if (value == null) {
      return;
    }

    if (expectedValue != null) {
      softAssert.assertEquals(value, expectedValue, AssertMessageBuilder.build(jsonPath, "with values that do not match"));
    }
  }
}
