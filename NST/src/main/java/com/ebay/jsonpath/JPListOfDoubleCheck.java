package com.ebay.jsonpath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPListOfDoubleCheck implements JsonPathExecutor, NullCheck<JPListOfDoubleCheck>, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5097244534267868074L;

  private Integer exactLength = null;

  private Integer minLength = null;

  private Integer maxLength = null;

  private List<Double> expectedValues = null;

  private List<Double> containsValues = null;

  private Double allExpectedValue = null;
  private boolean nullExpected = false;

  /**
   * Run baseline checks for a list of Doubles - list is not null and indexes
   * are not null.
   */
  public JPListOfDoubleCheck() {

  }

  /**
   * Make sure each value in the list of Doubles equal the specified value.
   *
   * @param value
   *          Expected Doubles value to match against all values.
   * @return Current instance.
   */
  public JPListOfDoubleCheck hasAllValuesEqualTo(Double value) {
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
  public JPListOfDoubleCheck hasLength(Integer length) {
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
  public JPListOfDoubleCheck hasMinLength(Integer length) {
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
  public JPListOfDoubleCheck hasMaxLength(Integer length) {
    this.maxLength = length;
    return this;
  }

  /**
   * Make sure the list of Doubles equals (index value and order) the specified
   * values.
   *
   * @param values
   *          Expected values to match.
   * @return Current instance.
   */
  public JPListOfDoubleCheck isEqualTo(List<Double> values) {

    if (values == null) {
      this.expectedValues = null;
    } else {
      this.expectedValues = new ArrayList<>();
      for (Double value : values) {
        this.expectedValues.add(value);
      }
    }
    return this;
  }

  /**
   * Make sure the list of Doubles contains the specified values.
   *
   * @param values
   *          Values that the list of Doubles is expected to contain.
   * @return Current instance.
   */
  public JPListOfDoubleCheck contains(List<Double> values) {

    if (values == null) {
      this.containsValues = null;
    } else {
      this.containsValues = new ArrayList<>();
      for (Double value : values) {
        this.containsValues.add(value);
      }
    }

    return this;
  }

  /**
   * Get the expected length of the list.
   * @return Expected length of the list.
   */
  public Integer getHasLength() {
    return exactLength;
  }

  /**
   * Get the minimum length of the list.
   * @return Minimum length of the list.
   */
  public Integer getMinLength() {
    return minLength;
  }

  /**
   * Get the maximum length of the list.
   * @return Maximum length of the list.
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Get the expected list of values.
   * @return Expected list of values.
   */
  public List<Double> getIsEqualToValues() {
    return expectedValues;
  }

  /**
   * Get the list of values that the resulting list must contain.
   * @return List of values that the resulting list must contain.
   */
  public List<Double> getContainsValues() {
    return containsValues;
  }

  /**
   * Get the value all results are expected to be equal to.
   * @return Value all results are expected to be equal to.
   */
  public Double getAllExpectedValue() {
    return allExpectedValue;
  }

  @Override
  public JPListOfDoubleCheck checkIsNull(boolean mustBeNull) {
    nullExpected = mustBeNull;
    return this;
  }

  @Override
  public boolean isNullExpected() {
    return nullExpected;
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    List<Double> values = null;

    try {

      List<Object> parsedValues = documentContext.read(jsonPath);

      if (parsedValues != null) {

        boolean containsIntegerValues = false;
        for (Object obj : parsedValues) {
          if (obj instanceof Integer) {
            containsIntegerValues = true;
            break;
          }
        }

        // Check if it is a list of integers - this is the one special case we need to handle.
        if (containsIntegerValues) {

          values = new ArrayList<>();

          for (Object obj : parsedValues) {
            if (obj instanceof Integer) {
              int intValue = (int) obj;
              values.add(Double.valueOf(intValue));
            } else {
              double doubleValue = (double) obj;
              values.add(Double.valueOf(doubleValue));
            }
          }

        } else {
          values = documentContext.read(jsonPath);
        }
      }

    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("as it was unable to be cast to list of Double. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (!isNullExpected()) {
      softAssert.assertNotNull(values, AssertMessageBuilder.build(jsonPath, "because the path does not exist."));
    }

    if (values == null) {
      return;
    }

    for (int i = 0; i < values.size(); i++) {

      Double value = null;
      try {
        value = values.get(i);
      } catch (ClassCastException e) {
        softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("as it was unable to be cast to Double. Original message: %s", e.getMessage())));
        continue;
      }

      if (isNullExpected()) {
        softAssert.assertNull(value, AssertMessageBuilder.build(jsonPath, String.format("with NON null value on index %d of the list of Doubles", i)));
      } else {
        softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, String.format("with null value on index %d of the list of Doubles", i)));
      }
    }

    if (allExpectedValue != null) {
      for (Double value : values) {
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
  }
}
