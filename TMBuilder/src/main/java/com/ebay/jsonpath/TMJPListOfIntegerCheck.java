package com.ebay.jsonpath;

import java.util.List;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPListOfIntegerCheck extends JPListOfIntegerCheck implements ThinModelSerializer {

  private static final long serialVersionUID = 1L;

  /**
   * Run baseline checks for a list of Integers - list is not null and indexes
   * are not null.
   */
  public TMJPListOfIntegerCheck() {
    super();
  }

  /**
   * Clone JPListOfIntegerCheck instance.
   *
   * @param source
   *          JPListOfIntegerCheck to clone.
   */
  public TMJPListOfIntegerCheck(JPListOfIntegerCheck source) {

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
  }

  @Override
  @TMCheckData(inputName = "Expected value for all results", inputDescription = "The expected integer value for all results returned by the JSON path query.", getterMethodName = "getAllExpectedValue")
  public TMJPListOfIntegerCheck hasAllValuesEqualTo(Integer value) {
    super.hasAllValuesEqualTo(value);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected number of results", inputDescription = "The expected number of integer results that should be returned by the specified JSON path.", getterMethodName = "getHasLength")
  public TMJPListOfIntegerCheck hasLength(Integer length) {
    super.hasLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Minimum number of results", inputDescription = "The minimum number of integer results that should be returned by the specified JSON path.", getterMethodName = "getMinLength")
  public TMJPListOfIntegerCheck hasMinLength(Integer length) {
    super.hasMinLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Maximum number of results", inputDescription = "The maximum number of integer results that should be returned by the specified JSON path.", getterMethodName = "getMaxLength")
  public TMJPListOfIntegerCheck hasMaxLength(Integer length) {
    super.hasMaxLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Expected values", inputDescription = "Exact list (size, order and value) of integer values that should be returned by the specified JSON path.", getterMethodName = "getIsEqualToValues")
  public TMJPListOfIntegerCheck isEqualTo(List<Integer> values) {
    super.isEqualTo(values);
    return this;
  }

  /**
   * Make sure the list of Integers contains the specified values.
   *
   * @param values
   *          Values that the list of Integers is expected to contain.
   * @return Current instance.
   */
  @Override
  @TMCheckData(inputName = "Contains values", inputDescription = "List of integer values that list of integer values, returned by the specified JSON path, should contain (values).", getterMethodName = "getContainsValues")
  public TMJPListOfIntegerCheck contains(List<Integer> values) {
    super.contains(values);
    return this;
  }

  // -----------------------------------------------
  // ThinModelSerializer
  // -----------------------------------------------

  @Override
  public String getJavaStatements() {

    StringBuilder builder = new StringBuilder("new JPListOfIntegerCheck()");

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
      for (Integer value : getIsEqualToValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%d", value));
      }
      builder.append(String.format(".isEqualTo(Arrays.asList(%s))", values.toString()));
    }

    if (getContainsValues() != null) {
      StringBuilder values = new StringBuilder();
      for (Integer value : getContainsValues()) {
        if (values.length() > 0) {
          values.append(",");
        }
        values.append(String.format("%d", value));
      }
      builder.append(String.format(".contains(Arrays.asList(%s))", values.toString()));
    }

    if (getAllExpectedValue() != null) {
      builder.append(String.format(".hasAllValuesEqualTo(%d)", getAllExpectedValue()));
    }

    return builder.toString();
  }
}
