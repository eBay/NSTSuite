package com.ebay.jsonpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockListOfValues;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPListOfDoubleCheck extends JPListOfDoubleCheck implements ThinModelSerializer, DeveloperMockListOfValues<Double> {

  private static final long serialVersionUID = 1L;
  private static final List<Double> DEFAULT_DEVELOPER_MOCK_VALUES = Arrays.asList(0.0);
  private List<Double> developerMockValues = DEFAULT_DEVELOPER_MOCK_VALUES;

  /**
   * Run baseline checks for a list of Doubles - list is not null and indexes
   * are not null.
   */
  public TMJPListOfDoubleCheck() {
    super();
  }

  /**
   * Clone JPListOfDoubleCheck instance.
   *
   * @param source
   *          JPListOfDoubleCheck to clone.
   */
  public TMJPListOfDoubleCheck(JPListOfDoubleCheck source) {

    super();

    if (source.getAllExpectedValue() != null) {
      this.hasAllValuesEqualTo(source.getAllExpectedValue());
    }

    if (source.getHasLength() != null) {
      this.hasLength(source.getHasLength());
    }

    if (source.getMinLength() != null) {
      this.hasMinLength(source.getMinLength());
    }

    if (source.getMaxLength() != null) {
      this.hasMaxLength(source.getMaxLength());
    }

    if (source.getIsEqualToValues() != null) {
      this.isEqualTo(source.getIsEqualToValues());
    }

    if (source.getContainsValues() != null) {
      this.contains(source.getContainsValues());
    }

    this.checkIsNull(source.isNullExpected());

    if (source instanceof TMJPListOfDoubleCheck) {
      this.setMockValues(((TMJPListOfDoubleCheck) source).getMockValues());
    }
  }

  @Override
  @TMCheckData(inputName = "Confirm null", inputDescription = "The Double values the result returned by the JSON path query must be null.", getterMethodName = "isNullExpected")
  public TMJPListOfDoubleCheck checkIsNull(boolean mustBeNull) {
    super.checkIsNull(mustBeNull);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected value for all results", inputDescription = "The expected double value for all results returned by the JSON path query.", getterMethodName = "getAllExpectedValue")
  public TMJPListOfDoubleCheck hasAllValuesEqualTo(Double value) {
    super.hasAllValuesEqualTo(value);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected number of results", inputDescription = "The expected number of double results that should be returned by the specified JSON path.", getterMethodName = "getHasLength")
  public TMJPListOfDoubleCheck hasLength(Integer length) {
    super.hasLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Minimum number of results", inputDescription = "The minimum number of double results that should be returned by the specified JSON path.", getterMethodName = "getMinLength")
  public TMJPListOfDoubleCheck hasMinLength(Integer length) {
    super.hasMinLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Maximum number of results", inputDescription = "The maximum number of double results that should be returned by the specified JSON path.", getterMethodName = "getMaxLength")
  public TMJPListOfDoubleCheck hasMaxLength(Integer length) {
    super.hasMaxLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected values", inputDescription = "Exact list (size, order and value) of double values that should be returned by the specified JSON path.", getterMethodName = "getIsEqualToValues")
  public TMJPListOfDoubleCheck isEqualTo(List<Double> values) {
    super.isEqualTo(values);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Contains values", inputDescription = "List of double values that list of double values, returned by the specified JSON path, should contain (values).", getterMethodName = "getContainsValues")
  public TMJPListOfDoubleCheck contains(List<Double> values) {
    super.contains(values);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockListOfValues<Double> getter and setter
  // ----------------------------------------------

  @Override
  public DeveloperMockType getMockType() {
    return DeveloperMockType.LIST_OF_DOUBLE;
  }

  @Override
  public List<Double> getMockValues() {
    return developerMockValues;
  }

  @Override
  @TMCheckData(inputName = "Mock values", inputDescription = "The mock double values to use when producing developer mocks. Array indexes with a wildcard [*] default to 1 (index 0).", getterMethodName = "getMockValues")
  public void setMockValues(List<Double> values) {
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

    StringBuilder builder = new StringBuilder("new JPListOfDoubleCheck()");

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
      for (Double value : getIsEqualToValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%s", DoubleUtility.removeTrailingZeros(value)));
      }
      builder.append(String.format(".isEqualTo(Arrays.asList(%s))", values.toString()));
    }

    if (getContainsValues() != null) {
      StringBuilder values = new StringBuilder();
      for (Double value : getContainsValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%s", DoubleUtility.removeTrailingZeros(value)));
      }
      builder.append(String.format(".contains(Arrays.asList(%s))", values.toString()));
    }

    if (getAllExpectedValue() != null) {
      builder.append(String.format(".hasAllValuesEqualTo(%s)", DoubleUtility.removeTrailingZeros(getAllExpectedValue())));
    }

    if (isNullExpected() == true) {
      builder.append(String.format(".checkIsNull(true)"));
    }

    return builder.toString();
  }

  @Override
  public String getKotlinStatements() {
    StringBuilder builder = new StringBuilder("JPListOfDoubleCheck()");

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
      for (Double value : getIsEqualToValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%s", DoubleUtility.removeTrailingZeros(value)));
      }
      builder.append(String.format(".isEqualTo(listOf(%s))", values.toString()));
    }

    if (getContainsValues() != null) {
      StringBuilder values = new StringBuilder();
      for (Double value : getContainsValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%s", DoubleUtility.removeTrailingZeros(value)));
      }
      builder.append(String.format(".contains(listOf(%s))", values.toString()));
    }

    if (getAllExpectedValue() != null) {
      builder.append(String.format(".hasAllValuesEqualTo(%s)", DoubleUtility.removeTrailingZeros(getAllExpectedValue())));
    }

    if (isNullExpected() == true) {
      builder.append(String.format(".checkIsNull(true)"));
    }

    return builder.toString();
  }
}
