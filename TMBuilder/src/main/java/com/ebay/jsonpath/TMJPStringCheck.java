package com.ebay.jsonpath;

import java.util.List;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPStringCheck extends JPStringCheck implements ThinModelSerializer, DeveloperMockValue<String> {

  private static final long serialVersionUID = 1L;
  private static final String DEFAULT_DEVELOPER_MOCK_VALUE = "lorem ipsum";
  private String developerMockValue = DEFAULT_DEVELOPER_MOCK_VALUE;

  /**
   * Run the baseline checks for a String - not null and not empty.
   */
  public TMJPStringCheck() {
    super();
  }

  /**
   * Clone a JPStringCheck instance.
   *
   * @param source
   *          JPStringCheck instance to clone.
   */
  public TMJPStringCheck(JPStringCheck source) {

    super();

    if (source.getIsEqualToExpectedValue() != null) {
      this.isEqualTo(source.getIsEqualToExpectedValue());
    }

    if (source.getIsEqualToOneOfExpectedValue() != null) {
      this.isEqualToOneOf(source.getIsEqualToOneOfExpectedValue());
    }

    if (source.getContainsValue() != null) {
      this.contains(source.getContainsValue());
    }

    if (source.getHasLengthExpectedValue() != null) {
      this.hasLength(source.getHasLengthExpectedValue());
    }

    if (source.getMaximumNumberOfCharacters() != null) {
      this.hasMaximumNumberOfCharacters(source.getMaximumNumberOfCharacters());
    }

    if (source.getMinimumNumberOfCharacters() != null) {
      this.hasMinimumNumberOfCharacters(source.getMinimumNumberOfCharacters());
    }

    if (source instanceof TMJPStringCheck) {
      this.setMockValue(((TMJPStringCheck) source).getMockValue());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected one of string", inputDescription = "The String values the result returned by the JSON path query must equal one of.", getterMethodName = "getIsEqualToOneOfExpectedValue")
  public TMJPStringCheck isEqualToOneOf(List<String> values) {
    super.isEqualToOneOf(values);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected string", inputDescription = "The expected String value for the result returned by the JSON path query.", getterMethodName = "getIsEqualToExpectedValue")
  public TMJPStringCheck isEqualTo(String value) {
    super.isEqualTo(value);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Contains string", inputDescription = "The String value the result returned by the JSON path query must contain.", getterMethodName = "getContainsValue")
  public TMJPStringCheck contains(String value) {
    super.contains(value);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected string length", inputDescription = "The length of the String the result returned by the JSON path query must have.", getterMethodName = "getHasLengthExpectedValue")
  public TMJPStringCheck hasLength(int length) {
    super.hasLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Minimum string length", inputDescription = "The minimum length of the String the result returned by the JSON path query must have.", getterMethodName = "getMinimumNumberOfCharacters")
  public TMJPStringCheck hasMinimumNumberOfCharacters(int length) {
    super.hasMinimumNumberOfCharacters(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Maximum string length", inputDescription = "The maximum length of the String the result returned by the JSON path query must have.", getterMethodName = "getMaximumNumberOfCharacters")
  public TMJPStringCheck hasMaximumNumberOfCharacters(int length) {
    super.hasMaximumNumberOfCharacters(length);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockValue<Integer> getter and setter
  // ----------------------------------------------

  @Override
  public DeveloperMockType getMockType() {
    return DeveloperMockType.STRING;
  }

  @Override
  public String getMockValue() {
    return developerMockValue;
  }

  @Override
  @TMCheckData(inputName = "Mock value", inputDescription = "The mock String value to use when producing developer mocks.", getterMethodName = "getMockValue")
  public void setMockValue(String value) {

    if (value == null) {
      developerMockValue = DEFAULT_DEVELOPER_MOCK_VALUE;
    } else {
      developerMockValue = value;
    }
  }

  // -----------------------------------------------
  // ThinModelSerializer
  // -----------------------------------------------

  @Override
  public String getJavaStatements() {

    StringBuilder builder = new StringBuilder("new JPStringCheck()");

    if (getHasLengthExpectedValue() != null) {
      builder.append(String.format(".hasLength(%d)", getHasLengthExpectedValue().intValue()));
    }

    if (getIsEqualToExpectedValue() != null) {
      builder.append(String.format(".isEqualTo(\"%s\")", getIsEqualToExpectedValue()));
    }

    if (getIsEqualToOneOfExpectedValue() != null) {
      StringBuilder values = new StringBuilder();
      for (String value : getIsEqualToOneOfExpectedValue()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("\"%s\"", value));
      }
      builder.append(String.format(".isEqualToOneOf(Arrays.asList(%s))", values.toString()));
    }

    if (getContainsValue() != null) {
      builder.append(String.format(".contains(\"%s\")", getContainsValue()));
    }

    if (getMinimumNumberOfCharacters() != null) {
      builder.append(String.format(".hasMinimumNumberOfCharacters(%d)", getMinimumNumberOfCharacters().intValue()));
    }

    if (getMaximumNumberOfCharacters() != null) {
      builder.append(String.format(".hasMaximumNumberOfCharacters(%d)", getMaximumNumberOfCharacters().intValue()));
    }

    return builder.toString();
  }
}
