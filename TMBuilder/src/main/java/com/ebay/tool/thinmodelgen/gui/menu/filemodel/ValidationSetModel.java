package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import java.util.Arrays;
import java.util.Objects;

public class ValidationSetModel {

  private String validationSetName;
  private NodeModel[] data;

  public ValidationSetModel(String validationSetName, NodeModel[] data) {
    this.validationSetName = validationSetName;
    this.data = data;
  }

  public NodeModel[] getData() {
    return data;
  }

  public String getValidationSetName() {
    return validationSetName;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ValidationSetModel that = (ValidationSetModel) o;
    return Objects.equals(validationSetName, that.validationSetName) && Arrays.deepEquals(data, that.data);
  }

  @Override public int hashCode() {
    int result = Objects.hash(validationSetName);
    result = 31 * result + Arrays.hashCode(data);
    return result;
  }
}
