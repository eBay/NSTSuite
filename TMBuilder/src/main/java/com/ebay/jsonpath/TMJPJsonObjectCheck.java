package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TMJPJsonObjectCheck extends JPJsonObjectCheck implements ThinModelSerializer {

  private static final long serialVersionUID = 7567402389400120218L;

  public TMJPJsonObjectCheck() {
    super();
  }

  /**
   * Clone JPJsonObjectCheck instance.
   *
   * @param source
   *          JPJsonObjectCheck instance.
   */
  public TMJPJsonObjectCheck(JPJsonObjectCheck source) {

    super();

    if (source.getExpectedNumberOfKeys() != null) {
      this.hasNumberOfKeys(source.getExpectedNumberOfKeys());
    }

    if (source.getExpectedKeys() != null) {
      this.keysAreEqualTo(source.getExpectedKeys());
    }

    if (source.getContainsKeys() != null) {
      this.keysContain(source.getContainsKeys());
    }

    if (source.getExpectedMap() != null) {
      this.isEqualTo(source.getExpectedMap());
    }

    if (source.getContainsMap() != null) {
      this.contains(source.getContainsMap());
    }

    this.checkIsNull(source.isNullExpected());

    if (source.getDoesNotContainKeys() != null) {
      this.keysDoNotContain(source.getDoesNotContainKeys());
    }
  }

  @Override
  @TMCheckData(inputName = "Confirm null", inputDescription = "The Object result returned by the JSON path query must be null.", getterMethodName = "isNullExpected")
  public TMJPJsonObjectCheck checkIsNull(boolean mustBeNull) {
    super.checkIsNull(mustBeNull);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected number of keys", inputDescription = "The number of keys that the object is expected to have.", getterMethodName = "getExpectedNumberOfKeys")
  public TMJPJsonObjectCheck hasNumberOfKeys(Integer numberOfKeys) {
    super.hasNumberOfKeys(numberOfKeys);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected keys", inputDescription = "The explicit list of keys that must be matched (value, size and order).", getterMethodName = "getExpectedKeys")
  public TMJPJsonObjectCheck keysAreEqualTo(List<String> expectedKeys) {
    super.keysAreEqualTo(expectedKeys);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Contains keys", inputDescription = "List of keys that must be matched, regardless of order and size.", getterMethodName = "getContainsKeys")
  public TMJPJsonObjectCheck keysContain(List<String> containsKeys) {
    super.keysContain(containsKeys);
    return this;
  }

  @Override
  @TMCheckData(inputName = "", inputDescription = "", enabled = false)
  public TMJPJsonObjectCheck isEqualTo(LinkedHashMap<String, Object> expectedMap) {
    super.isEqualTo(expectedMap);
    return this;
  }

  @Override
  @TMCheckData(inputName = "", inputDescription = "", enabled = false)
  public TMJPJsonObjectCheck contains(HashMap<String, Object> containsMap) {
    super.contains(containsMap);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Does not contain keys", inputDescription = "List of keys that must not be matched.", getterMethodName = "getDoesNotContainKeys")
  public JPJsonObjectCheck keysDoNotContain(List<String> doesNotContainKeys) {
    super.keysDoNotContain(doesNotContainKeys);
    return this;
  }

  // -----------------------------------------------
  // ThinModelSerializer
  // -----------------------------------------------

  @Override
  public String getJavaStatements() {

    StringBuilder builder = new StringBuilder("new JPJsonObjectCheck()");

    if (getExpectedNumberOfKeys() != null) {
      builder.append(String.format(".hasNumberOfKeys(%d)", getExpectedNumberOfKeys().intValue()));
    }

    if (getExpectedKeys() != null) {
        builder.append(getAppendedValues("keysAreEqualTo", getExpectedKeys()));
    }

    if (getContainsKeys() != null) {
        builder.append(getAppendedValues("keysContain", getContainsKeys()));
    }

    if (getDoesNotContainKeys() != null) {
        builder.append(getAppendedValues("keysDoNotContain", getDoesNotContainKeys()));
    }

    if (isNullExpected() == true) {
      builder.append(String.format(".checkIsNull(true)"));
    }

    return builder.toString();
  }

  @Override
  public String getKotlinStatements() {
    StringBuilder builder = new StringBuilder("JPJsonObjectCheck()");

    if (getExpectedNumberOfKeys() != null) {
      builder.append(String.format(".hasNumberOfKeys(%d)", getExpectedNumberOfKeys().intValue()));
    }

    if (getExpectedKeys() != null) {
      builder.append(getAppendedValues("keysAreEqualTo", getExpectedKeys()));
    }

    if (getContainsKeys() != null) {
      builder.append(getAppendedValues("keysContain", getContainsKeys()));
    }

    if (getDoesNotContainKeys() != null) {
      builder.append(getAppendedValues("keysDoNotContain", getDoesNotContainKeys()));
    }

    if (isNullExpected() == true) {
      builder.append(String.format(".checkIsNull(true)"));
    }

    return builder.toString();
  }

  private String getAppendedValues(String key, List<String> actualValues) {
      StringBuilder valuesToAppend = new StringBuilder();
      for (String value : actualValues) {
          if (valuesToAppend.length() > 0) {
              valuesToAppend.append(", ");
          }

          valuesToAppend.append("\"").append(value).append("\"");
      }

      return String.format(".%s(Arrays.asList(%s))", key, valuesToAppend);
  }
}
