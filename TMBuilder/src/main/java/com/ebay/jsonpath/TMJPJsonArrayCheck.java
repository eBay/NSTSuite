package com.ebay.jsonpath;

import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;

public class TMJPJsonArrayCheck extends JPJsonArrayCheck implements ThinModelSerializer {

  private static final long serialVersionUID = -37782752660517274L;

  public TMJPJsonArrayCheck() {
    super();
  }

  /**
   * Clone JPJsonArrayCheck instance.
   *
   * @param source
   *          JPJsonArrayCheck to clone.
   */
  public TMJPJsonArrayCheck(JPJsonArrayCheck source) {

    super();

    if (source.getExpectedLength() != null) {
      this.hasLength(source.getExpectedLength());
    }

    if (source.getMaxLength() != null) {
      this.hasMaxLength(source.getMaxLength());
    }

    if (source.getMinLength() != null) {
      this.hasMinLength(source.getMinLength());
    }
  }

  @Override
  @TMCheckData(inputName = "Expected length", inputDescription = "The expected number of elements in the JSON array.", getterMethodName = "getExpectedLength")
  public TMJPJsonArrayCheck hasLength(int length) {
    super.hasLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Minimum length", inputDescription = "The minimum number of elements expected in the JSON array.", getterMethodName = "getMinLength")
  public TMJPJsonArrayCheck hasMinLength(int length) {
    super.hasMinLength(length);
    return this;
  }

  @Override
  @TMCheckData(inputName = "Maximum length", inputDescription = "The maximum number of elements expected in the JSON array.", getterMethodName = "getMaxLength")
  public TMJPJsonArrayCheck hasMaxLength(int length) {
    super.hasMaxLength(length);
    return this;
  }

  // -----------------------------------------------
  // ThinModelSerializer
  // -----------------------------------------------

  @Override
  public String getJavaStatements() {

    StringBuilder builder = new StringBuilder("new JPJsonArrayCheck()");

    if (getExpectedLength() != null) {
      builder.append(String.format(".hasLength(%d)", getExpectedLength().intValue()));
    }

    if (getMinLength() != null) {
      builder.append(String.format(".hasMinLength(%d)", getMinLength().intValue()));
    }

    if (getMaxLength() != null) {
      builder.append(String.format(".hasMaxLength(%d)", getMaxLength().intValue()));
    }

    return builder.toString();
  }
}
