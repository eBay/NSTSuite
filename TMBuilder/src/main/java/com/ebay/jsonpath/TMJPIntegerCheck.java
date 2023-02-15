package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPIntegerCheck extends JPIntegerCheck implements ThinModelSerializer, DeveloperMockValue<Integer> {

  private static final long serialVersionUID = 13L;
  private static final Integer DEFAULT_DEVELOPER_MOCK_VALUE = 0;
  private Integer developerMockValue = DEFAULT_DEVELOPER_MOCK_VALUE;

  /**
   * Run the baseline checks for an integer - not null.
   */
  public TMJPIntegerCheck() {
    super();
  }

  /**
   * Clone JPIntegerCheck instance.
   *
   * @param source
   *          JPIntegerCheck to clone.
   */
  public TMJPIntegerCheck(JPIntegerCheck source) {

    super();

    if (source.getExpectedValue() != null) {
      this.isEqualTo(source.getExpectedValue());
    }

    if (source instanceof TMJPIntegerCheck) {
      this.setMockValue(((TMJPIntegerCheck) source).getMockValue());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected value", inputDescription = "The expected integer value.", getterMethodName = "getExpectedValue")
  public TMJPIntegerCheck isEqualTo(Integer value) {
    super.isEqualTo(value);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockValue<Integer> getter and setter
  // ----------------------------------------------

  @Override
  public Integer getMockValue() {
    return developerMockValue;
  }

  @Override
  @TMCheckData(inputName = "Mock value", inputDescription = "The mock integer value to use when producing developer mocks.", getterMethodName = "getMockValue")
  public void setMockValue(Integer value) {

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

    StringBuilder builder = new StringBuilder("new JPIntegerCheck()");

    if (getExpectedValue() != null) {
      builder.append(String.format(".isEqualTo(%d)", getExpectedValue().intValue()));
    }

    return builder.toString();
  }
}
