package com.ebay.jsonpath;

import java.io.Serializable;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPDoubleCheck implements JsonPathExecutor, Serializable {

  /**
   * Serializable ID.
   */
  private static final long serialVersionUID = -202076040776990133L;

  private Double expectedValue = null;

  /**
   * Run the baseline checks for a double - not null.
   */
  public JPDoubleCheck() {

  }

  /**
   * Make sure the double is equal to the value specified.
   *
   * @param value
   *          Expected value.
   * @return Current instance.
   */
  public JPDoubleCheck isEqualTo(Double value) {
    this.expectedValue = value;
    return this;
  }

  /**
   * Get the expected value.
   *
   * @return Expected value.
   */
  public Double getExpectedValue() {
    return expectedValue;
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    Double value = null;

    try {
      Object parsedValue = documentContext.read(jsonPath);
      if (parsedValue != null) {

        if (parsedValue instanceof Integer) {
          int intValue = (int) parsedValue;
          value = Double.valueOf(intValue);
        } else {
          value = documentContext.read(jsonPath);;
        }

      }
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("unable to be cast to double. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, "with null double."));

    if (value == null) {
      return;
    }

    if (expectedValue != null) {
      softAssert.assertEquals(value, expectedValue, AssertMessageBuilder.build(jsonPath, "with values that do not match."));
    }
  }
}
