package com.ebay.jsonpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockListOfValues;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPListOfBooleanCheck extends JPListOfBooleanCheck implements ThinModelSerializer, DeveloperMockListOfValues<Boolean> {

  private static final long serialVersionUID = 7056471666195229575L;
  private static final List<Boolean> DEFAULT_DEVELOPER_MOCK_VALUES = Arrays.asList(true);
  private List<Boolean> developerMockValues = DEFAULT_DEVELOPER_MOCK_VALUES;

  /**
   * Run baseline checks for a list of Booleans - list is not null and indexes
   * are not null.
   */
  public TMJPListOfBooleanCheck() {
    super();
  }

  /**
   * Clone JPListOfBooleanCheck instance.
   * @param source JPListOfBooleanCheck instance.
   */
  public TMJPListOfBooleanCheck(JPListOfBooleanCheck source) {

    super();

    if (source.getAllExpectedValue() != null) {
      this.hasAllValuesEqualTo(source.getAllExpectedValue());
    }

    if (source.getHasLength() != null) {
      this.hasLength(source.getHasLength());
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

    if (source instanceof TMJPListOfBooleanCheck) {
      this.setMockValues(((TMJPListOfBooleanCheck) source).getMockValues());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected value for all results", inputDescription = "The expected boolean value for all results returned by the JSON path query.", getterMethodName = "getAllExpectedValue")
  public TMJPListOfBooleanCheck hasAllValuesEqualTo(Boolean value) {
    super.hasAllValuesEqualTo(value);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected number of results", inputDescription = "The expected number of boolean results that should be returned by the specified JSON path.", getterMethodName = "getHasLength")
  public TMJPListOfBooleanCheck hasLength(Integer length) {
    super.hasLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Minimum number of results", inputDescription = "The minimum number of boolean results that should be returned by the specified JSON path.", getterMethodName = "getMinLength")
  public TMJPListOfBooleanCheck hasMinLength(Integer length) {
    super.hasMinLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Maximum number of results", inputDescription = "The maximum number of boolean results that should be returned by the specified JSON path.", getterMethodName = "getMaxLength")
  public TMJPListOfBooleanCheck hasMaxLength(Integer length) {
    super.hasMaxLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected values", inputDescription = "Exact list (size, order and value) of boolean values that should be returned by the specified JSON path.", getterMethodName = "getIsEqualToValues")
  public TMJPListOfBooleanCheck isEqualTo(List<Boolean> values) {
    super.isEqualTo(values);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Contains values", inputDescription = "List of boolean values that list of boolean values, returned by the specified JSON path, should contain (values).", getterMethodName = "getContainsValues")
  public TMJPListOfBooleanCheck contains(List<Boolean> values) {
    super.contains(values);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockListOfValues<Boolean> getter and setter
  // ----------------------------------------------

  @Override
  public DeveloperMockType getMockType() {
    return DeveloperMockType.LIST_OF_BOOLEAN;
  }

  @Override
  public List<Boolean> getMockValues() {
    return developerMockValues;
  }

  @Override
  @TMCheckData(inputName = "Mock values", inputDescription = "The mock boolean values to use when producing developer mocks.", getterMethodName = "getMockValues")
  public void setMockValues(List<Boolean> values) {
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

    StringBuilder builder = new StringBuilder("new JPListOfBooleanCheck()");

    if (getHasLength() != null) {
      builder.append(String.format(".hasLength(%d)", getHasLength().intValue()));
    }

    if (getMinLength() != null) {
      builder.append(String.format(".hasMinLength(%d)", getMinLength().intValue()));
    }

    if (getMaxLength() != null) {
      builder.append(String.format(".hasMaxLength(%d)", getMaxLength().intValue()));
    }

    if (getIsEqualToValues() != null) {
      StringBuilder values = new StringBuilder();
      for (Boolean value : getIsEqualToValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%b", value));
      }
      builder.append(String.format(".isEqualTo(Arrays.asList(%s))", values.toString()));
    }

    if (getContainsValues() != null) {
      StringBuilder values = new StringBuilder();
      for (Boolean value : getContainsValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%b", value));
      }
      builder.append(String.format(".contains(Arrays.asList(%s))", values.toString()));
    }

    if (getAllExpectedValue() != null) {
      builder.append(String.format(".hasAllValuesEqualTo(%s)", getAllExpectedValue()));
    }

    return builder.toString();
  }
}
