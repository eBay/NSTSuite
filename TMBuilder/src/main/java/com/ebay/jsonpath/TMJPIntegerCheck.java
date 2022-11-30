package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPIntegerCheck extends JPIntegerCheck implements ThinModelSerializer {

  private static final long serialVersionUID = 13L;

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
  }

  @Override
  @TMCheckData(inputName = "Expected value", inputDescription = "The expected integer value.", getterMethodName = "getExpectedValue")
  public TMJPIntegerCheck isEqualTo(Integer value) {
    super.isEqualTo(value);
    return this;
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
