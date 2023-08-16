package com.ebay.jsonpath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPListOfStringCheck implements JsonPathExecutor, NullCheck, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -8362908851075235101L;

  private Integer exactLength = null;

  private Integer minLength = null;

  private Integer maxLength = null;

  private List<String> expectedValues = null;

  private List<String> containsValues = null;

  private String allExpectedValue = null;

  private List<String> limitedToValues = null;

  private boolean isNull = false;

  /**
   * Run baseline checks for a list of strings - list is not null and indexes
   * are not null or empty strings.
   */
  public JPListOfStringCheck() {

  }

  /**
   * Make sure each value in the list of Strings equal the specified value.
   *
   * @param value
   *          Expected String value to match against all values.
   * @return Current instance.
   */
  public JPListOfStringCheck hasAllValuesEqualTo(String value) {
    this.allExpectedValue = value;
    return this;
  }

  /**
   * Make sure the list has the specified length.
   *
   * @param length
   *          Size of the array expected.
   * @return Current instance.
   */
  public JPListOfStringCheck hasLength(int length) {
    this.exactLength = length;
    return this;
  }

  /**
   * Make sure the list has at least the minimum number of indexes.
   *
   * @param length
   *          Minimum size of the list.
   * @return Current instance.
   */
  public JPListOfStringCheck hasMinLength(int length) {
    this.minLength = length;
    return this;
  }

  /**
   * Make sure the list has no more than the maximum number of indexes.
   *
   * @param length
   *          Maximum size of the list.
   * @return Current instance.
   */
  public JPListOfStringCheck hasMaxLength(int length) {
    this.maxLength = length;
    return this;
  }

  /**
   * Make sure the list of Strings equals (index value and order) the specified
   * values.
   *
   * @param values
   *          Expected values to match.
   * @return Current instance.
   */
  public JPListOfStringCheck isEqualTo(List<String> values) {

    if (values == null) {
      this.expectedValues = null;
    } else {
      this.expectedValues = new ArrayList<>();
      for (String value : values) {
        this.expectedValues.add(value);
      }
    }
    return this;
  }

  /**
   * Make sure the list of String contains the specified values.
   *
   * @param values
   *          Values that the list of String is expected to contain.
   * @return Current instance.
   */
  public JPListOfStringCheck contains(List<String> values) {

    if (values == null) {
      this.containsValues = null;
    } else {
      this.containsValues = new ArrayList<>();
      for (String value : values) {
        this.containsValues.add(value);
      }
    }
    return this;
  }

  /**
   * Make sure the list of String is only contains values found in the specfied
   * values.
   *
   * @param values
   *          Values the list of String list limited to.
   * @return Current instance.
   */
  public JPListOfStringCheck isLimitedToValues(List<String> values) {
    this.limitedToValues = values;
    return this;
  }

  /**
   * Get the values the list of values found is limited to.
   *
   * @return Values that the result set is limited to.
   */
  public List<String> getLimitedToValues() {
    return limitedToValues;
  }

  /**
   * Get the exact length expected for the list of results.
   *
   * @return Exact length expected for the list of results.
   */
  public Integer getHasLengthValue() {
    return exactLength;
  }

  /**
   * Get the minimum length expected for the list of results.
   *
   * @return Minimum length expected for the list of results.
   */
  public Integer getMinLength() {
    return minLength;
  }

  /**
   * Get the maximum length expected for the list of results.
   *
   * @return Maximum length expected for the list of results.
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Get the expected values (size, order and value) for the list of results.
   *
   * @return Expected values.
   */
  public List<String> getIsEqualToValues() {
    return expectedValues;
  }

  /**
   * Get the values that the list of results should contain.
   *
   * @return Values the list is expected to contain.
   */
  public List<String> getContainsValues() {
    return containsValues;
  }

  /**
   * Get the value all results are expected to be equal to.
   *
   * @return Value all results are expected to be equal to.
   */
  public String getAllExpectedValue() {
    return allExpectedValue;
  }

  @Override
  public void checkIsNull(boolean mustBeNull) {
    isNull = mustBeNull;
  }

  @Override
  public boolean isNullExpected() {
    return isNull;
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    List<String> values = null;

    try {
      values = documentContext.read(jsonPath);
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("as it was unable to be cast to list of String. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (!isNull) {
      softAssert.assertNotNull(values, AssertMessageBuilder.build(jsonPath, "because the path does not exist"));
    }

    if (values == null) {
      return;
    }

    for (int i = 0; i < values.size(); i++) {

      String value = null;
      try {
        value = values.get(i);
      } catch (ClassCastException e) {
        softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("as it was unable to be cast to Integer. Original message: %s", e.getMessage())));
        continue;
      }

      if (isNull) {
        softAssert.assertNull(value, AssertMessageBuilder.build(jsonPath, String.format("because the path does exist on index %d of the list of strings", i)));
      } else {
        softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, String.format("with null value on index %d of the list of strings", i)));
        if (value != null) {
          softAssert.assertTrue(!value.isEmpty(), AssertMessageBuilder.build(jsonPath, String.format("with empty value on index %d of the list of strings", i)));
        }
      }
    }

    if (allExpectedValue != null) {
      for (String value : values) {
        softAssert.assertEquals(value, allExpectedValue, AssertMessageBuilder.build(jsonPath, String.format("because path value %s did not equal expected value %s", value, allExpectedValue)));
      }
    }

    if (exactLength != null) {
      softAssert.assertEquals(values.size(), exactLength.intValue(), AssertMessageBuilder.build(jsonPath, "because path did not return expected number of results"));
    }

    if (minLength != null) {
      softAssert
          .assertTrue(
              values.size() >= minLength.intValue(),
              AssertMessageBuilder.build(jsonPath, String.format("because path did not contain the minimum number of expected results %d. Found %d", minLength, values.size())));
    }

    if (maxLength != null) {
      softAssert
          .assertTrue(
              values.size() <= maxLength.intValue(),
              AssertMessageBuilder.build(jsonPath, String.format("because path exceeded the maximum number of expected results %d. Found %d", maxLength, values.size())));
    }

    if (expectedValues != null) {
      softAssert.assertTrue(values.equals(expectedValues), AssertMessageBuilder.build(jsonPath, String.format("because path values %s does not equal expected values %s", values, expectedValues)));
    }

    if (containsValues != null) {
      softAssert
          .assertTrue(values.containsAll(containsValues), AssertMessageBuilder.build(jsonPath, String.format("because path values %s does not contain all of the values %s", values, containsValues)));
    }

    if (limitedToValues != null) {
      softAssert.assertTrue(limitedToValues.containsAll(values), AssertMessageBuilder.build(jsonPath, String.format("because path values %s is not limited to the values %s", values, limitedToValues)));
    }
  }
}
