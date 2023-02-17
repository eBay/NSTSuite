package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPBooleanCheck extends JPBooleanCheck implements ThinModelSerializer, DeveloperMockValue<Boolean> {

  private static final long serialVersionUID = 1L;
  private static final boolean DEFAULT_DEVELOPER_MOCK_VALUE = true;
  private Boolean developerMockValue = DEFAULT_DEVELOPER_MOCK_VALUE;

  /**
   * Run the baseline checks for a boolean - not null.
   */
  public TMJPBooleanCheck() {
    super();
  }

  /**
   * Clone JPBooleanCheck instance.
   * @param source JPBooleanCheck instance to clone.
   */
  public TMJPBooleanCheck(JPBooleanCheck source) {
    super();

    if (source.getExpectedValue() != null) {
      this.isEqualTo(source.getExpectedValue());
    }

    if (source instanceof TMJPBooleanCheck) {
      this.setMockValue(((TMJPBooleanCheck) source).getMockValue());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected value", inputDescription = "The expected boolean value.", getterMethodName = "getExpectedValue")
  public TMJPBooleanCheck isEqualTo(Boolean value) {
    super.isEqualTo(value);
    return this;
  }

  // ----------------------------------------------
  // DeveloperMockValue<Boolean> getter and setter
  // ----------------------------------------------

  @Override
  public DeveloperMockType getMockType() {
    return DeveloperMockType.BOOLEAN;
  }

  @Override
  public Boolean getMockValue() {
    return developerMockValue;
  }

  @Override
  @TMCheckData(inputName = "Mock value", inputDescription = "The mock boolean value to use when producing developer mocks.", getterMethodName = "getMockValue")
  public void setMockValue(Boolean value) {

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

    StringBuilder builder = new StringBuilder("new JPBooleanCheck()");

    if (getExpectedValue() != null) {
      builder.append(String.format(".isEqualTo(%b)", getExpectedValue().booleanValue()));
    }

    return builder.toString();
  }
}
