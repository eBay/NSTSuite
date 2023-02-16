package com.ebay.jsonpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockListOfValues;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPListOfStringCheck extends JPListOfStringCheck implements ThinModelSerializer, DeveloperMockListOfValues<String> {

  private static final long serialVersionUID = 1L;
  private static final List<String> DEFAULT_DEVELOPER_MOCK_VALUES = Arrays.asList("lorem ipsum");
  private List<String> developerMockValues = DEFAULT_DEVELOPER_MOCK_VALUES;

  /**
   * Run baseline checks for a list of strings - list is not null and indexes
   * are not null or empty strings.
   */
  public TMJPListOfStringCheck() {
    super();
  }

  /**
   * Clone a JPListOfStringCheck instance.
   *
   * @param source
   *          JPListOfStringCheck to clone.
   */
  public TMJPListOfStringCheck(JPListOfStringCheck source) {

    super();

    if (source.getAllExpectedValue() != null) {
      this.hasAllValuesEqualTo(source.getAllExpectedValue());
    }

    if (source.getHasLengthValue() != null) {
      this.hasLength(source.getHasLengthValue());
    }

    if (source.getMaxLength() != null) {
      this.hasMaxLength(source.getMaxLength());
    }

    if (source.getMinLength() != null) {
      this.hasMinLength(source.getMinLength());
    }

    if (source.getIsEqualToValues() != null) {
      this.isEqualTo(source.getIsEqualToValues());
    }

    if (source.getContainsValues() != null) {
      this.contains(source.getContainsValues());
    }

    if (source.getLimitedToValues() != null) {
      this.isLimitedToValues(source.getLimitedToValues());
    }

    if (source instanceof TMJPListOfStringCheck) {
      this.setMockValues(((TMJPListOfStringCheck) source).getMockValues());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected value for all results", inputDescription = "The expected String value for all results returned by the JSON path query.", getterMethodName = "getAllExpectedValue")
  public TMJPListOfStringCheck hasAllValuesEqualTo(String value) {
    super.hasAllValuesEqualTo(value);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected length", inputDescription = "The expected number of elements in the list of Strings returned by the JSON path query.", getterMethodName = "getHasLengthValue")
  public TMJPListOfStringCheck hasLength(int length) {
    super.hasLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Minimum length", inputDescription = "The minimum number of elements in the list of Strings returned by the JSON path query.", getterMethodName = "getMinLength")
  public TMJPListOfStringCheck hasMinLength(int length) {
    super.hasMinLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Maximum length", inputDescription = "The maximum number of elements in the list of Strings returned by the JSON path query.", getterMethodName = "getMaxLength")
  public TMJPListOfStringCheck hasMaxLength(int length) {
    super.hasMaxLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected values", inputDescription = "The expected list of Strings (size, order and value) returned by the JSON path query.", getterMethodName = "getIsEqualToValues")
  public TMJPListOfStringCheck isEqualTo(List<String> values) {
    super.isEqualTo(values);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Contains values", inputDescription = "The list of Strings that results returned by the JSON path query should contain.", getterMethodName = "getContainsValues")
  public TMJPListOfStringCheck contains(List<String> values) {
    super.contains(values);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Limited to values", inputDescription = "The list of Strings that results returned by the JSON path query is limited to (must match only values in this list).", getterMethodName = "getLimitedToValues")
  public TMJPListOfStringCheck isLimitedToValues(List<String> values) {
    super.isLimitedToValues(values);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockListOfValues<Integer> getter and setter
  // ----------------------------------------------

  @Override
  public DeveloperMockType getMockType() {
    return DeveloperMockType.LIST_OF_STRING;
  }

  @Override
  public List<String> getMockValues() {
    return developerMockValues;
  }

  @Override
  @TMCheckData(inputName = "Mock values", inputDescription = "The mock integer values to use when producing developer mocks.", getterMethodName = "getMockValues")
  public void setMockValues(List<String> values) {
    if (values == null || values.isEmpty()) {
      developerMockValues = DEFAULT_DEVELOPER_MOCK_VALUES;
    } else {
      developerMockValues = new ArrayList<>(values);
    }
  }

  // -----------------------------------------------
  // ThinModelSerializer
  // -----------------------------------------------

  @Override
  public String getJavaStatements() {

    StringBuilder builder = new StringBuilder("new JPListOfStringCheck()");

    if (getHasLengthValue() != null) {
      builder.append(String.format(".hasLength(%d)", getHasLengthValue().intValue()));
    }

    if (getMinLength() != null) {
      builder.append(String.format(".hasMinLength(%d)", getMinLength().intValue()));
    }

    if (getMaxLength() != null) {
      builder.append(String.format(".hasMaxLength(%d)", getMaxLength().intValue()));
    }

    if (getIsEqualToValues() != null) {
      StringBuilder values = new StringBuilder();
      for (String value : getIsEqualToValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("\"%s\"", value));
      }
      builder.append(String.format(".isEqualTo(Arrays.asList(%s))", values.toString()));
    }

    if (getContainsValues() != null) {
      StringBuilder values = new StringBuilder();
      for (String value : getContainsValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("\"%s\"", value));
      }
      builder.append(String.format(".contains(Arrays.asList(%s))", values.toString()));
    }

    if (getAllExpectedValue() != null) {
      builder.append(String.format(".hasAllValuesEqualTo(\"%s\")", getAllExpectedValue()));
    }

    if (getLimitedToValues() != null) {
      StringBuilder values = new StringBuilder();
      for (String value : getLimitedToValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("\"%s\"", value));
      }
      builder.append(String.format(".isLimitedToValues(Arrays.asList(%s))", values.toString()));
    }

    return builder.toString();
  }
}
