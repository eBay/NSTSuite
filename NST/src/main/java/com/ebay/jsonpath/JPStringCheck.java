package com.ebay.jsonpath;

import java.io.Serializable;
import java.util.List;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPStringCheck implements JsonPathExecutor, NullCheck<JPStringCheck>, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -1639139966099790541L;

  private String matchesPattern = null;

  private String expectedValue = null;

  private List<String> equalsOneOfValues = null;

  private String containsValue = null;

  private Integer exactLength = null;

  private Integer minimumNumberOfCharacters = null;

  private Integer maximumNumberOfCharacters = null;

  private boolean nullExpected = false;

  /**
   * Run the baseline checks for a String - not null and not empty.
   */
  public JPStringCheck() {

  }

    /**
     * Make sure the String matches the regex pattern specified.
     *
     * @param pattern
     *          Expected regex pattern value.
     * @return Current instance.
     */
    public JPStringCheck matchesPattern(String pattern) {
        this.matchesPattern = pattern;
        return this;
    }

  /**
   * Make sure the String is equal to one of the values specified.
   *
   * @param values
   *          List of expected values.
   * @return Current instance.
   */
  public JPStringCheck isEqualToOneOf(List<String> values) {
    this.equalsOneOfValues = values;
    return this;
  }

  /**
   * Make sure the String is equal to the value specified.
   *
   * @param value
   *          Expected value.
   * @return Current instance.
   */
  public JPStringCheck isEqualTo(String value) {
    this.expectedValue = value;
    return this;
  }

  /**
   * Make sure the String contains the specified value.
   *
   * @param value
   *          Value we expect the String to contain.
   * @return Current instance.
   */
  public JPStringCheck contains(String value) {
    this.containsValue = value;
    return this;
  }

  /**
   * Make sure the String has the exact length specified.
   *
   * @param length
   *          Expected length of the string.
   * @return Current instance.
   */
  public JPStringCheck hasLength(int length) {
    this.exactLength = length;
    return this;
  }

  /**
   * Make sure the String has at least the minimum number of characters
   * specified (inclusive).
   *
   * @param length
   *          Minimum number of characters expected.
   * @return Current instance.
   */
  public JPStringCheck hasMinimumNumberOfCharacters(int length) {
    this.minimumNumberOfCharacters = length;
    return this;
  }

  /**
   * Make sure the String has no more than the maximum number of characters
   * specified (inclusive).
   *
   * @param length
   *          Maximum number of characters expected.
   * @return Current instance.
   */
  public JPStringCheck hasMaximumNumberOfCharacters(int length) {
    this.maximumNumberOfCharacters = length;
    return this;
  }

  /**
   * Get the equals to one of expected values.
   * @return Expected equals to one of values.
   */
  public List<String> getIsEqualToOneOfExpectedValue() {
    return equalsOneOfValues;
  }

  /**
   * Get the expected value.
   * @return Expected value.
   */
  public String getIsEqualToExpectedValue() {
    return expectedValue;
  }

  /**
   * Get the value the String must contain.
   * @return Contains value.
   */
  public String getContainsValue() {
    return containsValue;
  }

  /**
   * Get the expected length of the string.
   * @return Expected length of the string.
   */
  public Integer getHasLengthExpectedValue() {
    return exactLength;
  }

  /**
   * Get the minimum expected length of the string.
   * @return Minimum expected length.
   */
  public Integer getMinimumNumberOfCharacters() {
    return minimumNumberOfCharacters;
  }

  /**
   * Get the maximum expected length of the string.
   * @return Maximum expected length.
   */
  public Integer getMaximumNumberOfCharacters() {
    return maximumNumberOfCharacters;
  }

    /**
     * Get the regex pattern the String must match.
     * @return Expected regex pattern.
     */
    public String getMatchesPattern() {
        return matchesPattern;
    }

  @Override
  public JPStringCheck checkIsNull(boolean mustBeNull) {
    nullExpected = mustBeNull;
    return this;
  }

  @Override
  public boolean isNullExpected() {
    return nullExpected;
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    String value = null;

    try {
      value = documentContext.read(jsonPath);
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("unable to be cast to String. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (isNullExpected()) {
      softAssert.assertNull(value, AssertMessageBuilder.build(jsonPath, "with non-null string"));
    } else {
      softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, "with null string."));
    }

    if (value == null) {
      return;
    }

    // In the event that the user enters an empty string
    // as the expected value we do not want to report an issue.
    if (expectedValue == null) {
      softAssert.assertTrue(!value.isEmpty(), AssertMessageBuilder.build(jsonPath, "with empty string."));
    } else {
      softAssert.assertEquals(value, expectedValue, AssertMessageBuilder.build(jsonPath, "with unexpected actual values."));
    }

    if (equalsOneOfValues != null && !equalsOneOfValues.isEmpty()) {
      softAssert.assertTrue(equalsOneOfValues.contains(value), AssertMessageBuilder.build(jsonPath, String.format("because '%s' was not one of the expected values '%s'.", value, equalsOneOfValues.toString())));
    }

    if (matchesPattern != null) {
        softAssert.assertTrue(value.matches(matchesPattern), AssertMessageBuilder.build(jsonPath, String.format("because '%s' does not match expected pattern: '%s'.", value, matchesPattern)));
    }

    if (containsValue != null) {
      softAssert.assertTrue(value.contains(containsValue), AssertMessageBuilder.build(jsonPath, String.format("because '%s' does not contain the string '%s'.", value, containsValue)));
    }

    if (exactLength != null) {
      softAssert.assertTrue(value.length() == exactLength, AssertMessageBuilder.build(jsonPath, String.format("because '%s' is not the expected length of %d characters.", value, exactLength)));
    }

    if (minimumNumberOfCharacters != null) {
      softAssert
          .assertTrue(value.length() >= minimumNumberOfCharacters, AssertMessageBuilder.build(jsonPath, String.format("because '%s' has fewer than %d characters.", value, minimumNumberOfCharacters)));
    }

    if (maximumNumberOfCharacters != null) {
      softAssert
          .assertTrue(value.length() <= maximumNumberOfCharacters, AssertMessageBuilder.build(jsonPath, String.format("because '%s' has more than %d characters.", value, maximumNumberOfCharacters)));
    }
  }
}
