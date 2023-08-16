package com.ebay.jsonpath;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPJsonArrayCheck implements JsonPathExecutor, NullCheck, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -37782752660517274L;

  private Integer expectedLength = null;

  private Integer minLength;

  private Integer maxLength;
  private boolean isNull = false;

  /**
   * Set the exact length the JSON array should have.
   *
   * @param length
   *          Exact expected length of the JSON array.
   * @return Current instance.
   */
  public JPJsonArrayCheck hasLength(int length) {
    this.expectedLength = length;
    return this;
  }

  /**
   * Set the minimum length for the JSON array.
   *
   * @param length
   *          Minimum length for the JSON array.
   * @return Current instance.
   */
  public JPJsonArrayCheck hasMinLength(int length) {
    this.minLength = length;
    return this;
  }

  /**
   * Set the maximum length for the JSON array.
   *
   * @param length
   *          Maximum length for the JSON array.
   * @return Current instance.
   */
  public JPJsonArrayCheck hasMaxLength(int length) {
    this.maxLength = length;
    return this;
  }

  /**
   * Get the expected length of the JSON array.
   *
   * @return Expected length of the JSON array.
   */
  public Integer getExpectedLength() {
    return expectedLength;
  }

  /**
   * Get the minimum length for the JSON array.
   *
   * @return Minimum length for the JSON array.
   */
  public Integer getMinLength() {
    return minLength;
  }

  /**
   * Get the maximum length for the JSON array.
   *
   * @return Maximum length for the JSON array.
   */
  public Integer getMaxLength() {
    return maxLength;
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

    List<Map<String, Object>> values = null;

    try {
      values = documentContext.read(jsonPath);
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("unable to be cast to JSONArray. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (isNull) {
      softAssert.assertNull(values, AssertMessageBuilder.build(jsonPath, "because the path does exist"));
    } else {
      softAssert.assertNotNull(values, AssertMessageBuilder.build(jsonPath, "because the path does not exist"));
    }

    if (values == null) {
      return;
    }

    if (expectedLength != null) {
      softAssert.assertEquals(values.size(), expectedLength.intValue(), AssertMessageBuilder.build(jsonPath, "because array did not contain the expected number of indexes"));
    }

    if (minLength != null) {
      softAssert
          .assertTrue(
              values.size() >= minLength.intValue(),
              AssertMessageBuilder.build(jsonPath, String.format("because array did not contain the minimum number of indexes %d. Found %d", minLength, values.size())));
    }

    if (maxLength != null) {
      softAssert
          .assertTrue(
              values.size() <= maxLength.intValue(),
              AssertMessageBuilder.build(jsonPath, String.format("because array exceeded the maximum number of indexes %d. Found %d", maxLength, values.size())));
    }
  }
}
