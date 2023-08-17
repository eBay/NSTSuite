package com.ebay.jsonpath;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;

public class JPJsonObjectCheck implements JsonPathExecutor, NullCheck<JPJsonObjectCheck>, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 7567402389400120218L;

  private Integer expectedNumberOfKeys = null;
  private List<String> expectedKeys = null;
  private List<String> containsKeys = null;
  private LinkedHashMap<String, Object> expectedMap = null;
  private HashMap<String, Object> containsMap = null;
  private List<String> doesNotContainKeys = null;
  private boolean isNull = false;

  /**
   * Make sure the object has the specified number of keys.
   *
   * @param numberOfKeys
   *          Number of keys to check for.
   * @return Current instance.
   */
  public JPJsonObjectCheck hasNumberOfKeys(Integer numberOfKeys) {
    this.expectedNumberOfKeys = numberOfKeys;
    return this;
  }

  /**
   * Make sure the object keys are equal to the expected set of keys, in order
   * and name.
   *
   * @param expectedKeys
   *          List of expected keys in order.
   * @return Current instance.
   */
  public JPJsonObjectCheck keysAreEqualTo(List<String> expectedKeys) {
    this.expectedKeys = expectedKeys;
    return this;
  }

  /**
   * Make sure the object contains the expected set of keys, in any order.
   *
   * @param containsKeys
   *          List of keys that must be present.
   * @return Current instance.
   */
  public JPJsonObjectCheck keysContain(List<String> containsKeys) {
    this.containsKeys = containsKeys;
    return this;
  }

  /**
   * Make sure the object is equal to the mapping of key/value pairs specified.
   *
   * @param expectedMap
   *          Expected key/value pairs for the object.
   * @return Current instance.
   */
  public JPJsonObjectCheck isEqualTo(LinkedHashMap<String, Object> expectedMap) {
    this.expectedMap = expectedMap;
    return this;
  }

  /**
   * Make sure the object contains the mapping of key/value pairs specified.
   *
   * @param containsMap
   *          Key/value pairs the object must contain.
   * @return Current instance.
   */
  public JPJsonObjectCheck contains(HashMap<String, Object> containsMap) {
    this.containsMap = containsMap;
    return this;
  }

  /**
   * Make sure the object does not contain the keys specified.
   *
   * @param doesNotContainKeys
   *          Keys to confirm the object does not contain.
   * @return Current instance.
   */
  public JPJsonObjectCheck keysDoNotContain(List<String> doesNotContainKeys) {
    this.doesNotContainKeys = doesNotContainKeys;
    return this;
  }

  /**
   * Get the number of keys expected.
   *
   * @return Number of keys expected.
   */
  public Integer getExpectedNumberOfKeys() {
    return expectedNumberOfKeys;
  }

  /**
   * Get the list of expected keys.
   *
   * @return List of expected keys.
   */
  public List<String> getExpectedKeys() {
    return expectedKeys;
  }

  /**
   * Get the list of keys that the result must contain.
   *
   * @return List of keys that the result must contain.
   */
  public List<String> getContainsKeys() {
    return containsKeys;
  }

  /**
   * Get the expected map of keys to values.
   *
   * @return Expected map of keys to values.
   */
  public LinkedHashMap<String, Object> getExpectedMap() {
    return expectedMap;
  }

  /**
   * Get the map of keys to values that the result must contain.
   *
   * @return Map of keys to values that the result must contain.
   */
  public HashMap<String, Object> getContainsMap() {
    return containsMap;
  }

  /**
   * Get the list of keys that the result must NOT contain.
   *
   * @return List of keys that the result must NOT contain.
   */
  public List<String> getDoesNotContainKeys() {
    return doesNotContainKeys;
  }

  @Override
  public JPJsonObjectCheck checkIsNull(boolean mustBeNull) {
    isNull = mustBeNull;
    return this;
  }

  @Override
  public boolean isNullExpected() {
    return isNull;
  }

  @SuppressWarnings("unlikely-arg-type")
  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

    LinkedHashMap<String, String> value = null;

    try {
      value = documentContext.read(jsonPath);
    } catch (ClassCastException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("unable to be cast to JSONObject. Original message: %s", e.getMessage())));
      return;
    } catch (PathNotFoundException e) {
      softAssert.fail(AssertMessageBuilder.build(jsonPath, String.format("because the path was not found. Original message: %s", e.getMessage())));
      return;
    }

    if (isNullExpected()) {
      softAssert.assertNull(value, AssertMessageBuilder.build(jsonPath, "because the path does exist"));
    } else {
      softAssert.assertNotNull(value, AssertMessageBuilder.build(jsonPath, "because the path does not exist"));
    }

    if (value == null) {
      return;
    }

    if (expectedNumberOfKeys != null) {
      softAssert.assertEquals(value.keySet().size(), expectedNumberOfKeys.intValue(), AssertMessageBuilder.build(jsonPath, "because path did not return an object with the expected number of keys"));
    }

    if (expectedKeys != null) {
      List<String> actualKeys = Arrays.asList(value.keySet().toArray(new String[0]));
      softAssert.assertEquals(actualKeys, expectedKeys, AssertMessageBuilder.build(jsonPath, "because path did not return expected set of keys"));
    }

    if (containsKeys != null) {
      List<String> actualKeys = Arrays.asList(value.keySet().toArray(new String[0]));
      softAssert
          .assertTrue(
              actualKeys.containsAll(containsKeys),
              AssertMessageBuilder.build(jsonPath, String.format("because path does not contain the keys [%s]. Actual: [%s]", containsKeys, actualKeys)));
    }

    if (expectedMap != null) {
      softAssert.assertEquals(value, expectedMap, AssertMessageBuilder.build(jsonPath, "because path does not contain the specified map of key/values"));
    }

    if (containsMap != null) {
      softAssert
          .assertTrue(
              value.entrySet().containsAll(containsMap.entrySet()),
              AssertMessageBuilder.build(jsonPath, String.format("because path does not contain the key/values [%s]. Actual: [%s]", containsMap.entrySet(), value.entrySet())));
    }

    if (doesNotContainKeys != null) {
      List<String> actualKeys = Arrays.asList(value.keySet().toArray(new String[0]));
      softAssert
          .assertFalse(
              actualKeys.contains(doesNotContainKeys),
              AssertMessageBuilder.build(jsonPath, String.format("because path contains one or all of the unexpected keys [%s]. Actual: [%s]", doesNotContainKeys, actualKeys)));
    }
  }
}
