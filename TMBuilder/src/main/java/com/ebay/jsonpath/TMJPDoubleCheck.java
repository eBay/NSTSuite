package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPDoubleCheck extends JPDoubleCheck implements ThinModelSerializer, DeveloperMockValue<Double> {

  private static final long serialVersionUID = 1L;
  private static final Double DEFAULT_DEVELOPER_MOCK_VALUE = 0.0;
  private Double developerMockValue = DEFAULT_DEVELOPER_MOCK_VALUE;

  /**
   * Run the baseline checks for a double - not null.
   */
  public TMJPDoubleCheck() {
    super();
  }

  /**
   * Clone JPDoubleCheck instance.
   *
   * @param source
   *          JPDoubleCheck instance to clone.
   */
  public TMJPDoubleCheck(JPDoubleCheck source) {

    super();

    if (source.getExpectedValue() != null) {
      this.isEqualTo(source.getExpectedValue());
    }

    if (source instanceof TMJPDoubleCheck) {
      this.setMockValue(((TMJPDoubleCheck) source).getMockValue());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected value", inputDescription = "The expected double value.", getterMethodName = "getExpectedValue")
  public TMJPDoubleCheck isEqualTo(Double value) {
    super.isEqualTo(value);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockValue<Double> getter and setter
  // ----------------------------------------------

  @Override
  public DeveloperMockType getMockType() {
    return DeveloperMockType.DOUBLE;
  }

  @Override
  public Double getMockValue() {
    return developerMockValue;
  }

  @Override
  @TMCheckData(inputName = "Mock value", inputDescription = "The mock double value to use when producing developer mocks. Array indexes with a wildcard [*] default to 1 (index 0).", getterMethodName = "getMockValue")
  public void setMockValue(Double value) {

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

    StringBuilder builder = new StringBuilder("new JPDoubleCheck()");

    if (getExpectedValue() != null) {
      builder.append(String.format(".isEqualTo(%s)", DoubleUtility.removeTrailingZeros(getExpectedValue())));
    }

    return builder.toString();
  }
}
