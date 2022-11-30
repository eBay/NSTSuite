package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPDoubleCheck extends JPDoubleCheck implements ThinModelSerializer {

  private static final long serialVersionUID = 1L;

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
  }

  @Override
  @TMCheckData(inputName = "Expected value", inputDescription = "The expected double value.", getterMethodName = "getExpectedValue")
  public TMJPDoubleCheck isEqualTo(Double value) {
    super.isEqualTo(value);
    return this;
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
