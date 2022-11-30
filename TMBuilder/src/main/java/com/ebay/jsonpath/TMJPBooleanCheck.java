package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPBooleanCheck extends JPBooleanCheck implements ThinModelSerializer {

  private static final long serialVersionUID = 1L;

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
  }

  @Override
  @TMCheckData(inputName = "Expected value", inputDescription = "The expected boolean value.", getterMethodName = "getExpectedValue")
  public TMJPBooleanCheck isEqualTo(Boolean value) {
    super.isEqualTo(value);
    return this;
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
