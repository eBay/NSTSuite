package com.ebay.service.logger.platforms.model;

import java.util.ArrayList;
import java.util.List;

import com.ebay.nst.coverage.Generated;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.platforms.util.PlatformConstants;

@Generated
public class GeneralPlatformMethodModel {

  private String methodSignature;
  private StringBuilder methodLeadInContents = new StringBuilder();
  private List<GeneralPlatformOperationModel> methodOperations = new ArrayList<>();

  public String getMethodSignature() {
    return methodSignature;
  }

  public void setMethodSignature(String methodSignature) {
    this.methodSignature = methodSignature;
  }

  public String getMethodLeadInContents() {
    return methodLeadInContents.toString();
  }

  public void addMethodLeadInContents(String line) {
    this.methodLeadInContents.append(line);
    this.methodLeadInContents.append("\n");
  }

  public List<GeneralPlatformOperationModel> getMethodOperations() {
    return methodOperations;
  }

  public void setMethodOperations(List<GeneralPlatformOperationModel> methodOperations) {
    this.methodOperations = methodOperations;
  }

  public void addMethodOperations(GeneralPlatformOperationModel model) {
    this.methodOperations.add(model);
  }

  /**
   * Get the calculated indent for the contents of this method.
   * @return Whitespace to prepend to any contents written to this method.
   */
  public String getMethodContentsIndent() {

    String indent = "";
    String evalString = methodSignature;

    for (int i = 0; i < methodSignature.length(); i++) {
      String character = evalString.substring(0, 1);

      if (!character.equals(" ")) {
        break;
      }

      indent += " ";
      evalString = evalString.substring(1);
    }

    String tab = PlatformConstants.getPlatformSpecificTab();
    Platform platform = RuntimeConfigManager.getInstance().getPlatform();
    String extraPadding = "";

    switch (platform) {
    case ANDROID:
      extraPadding = "" + tab + tab;
      break;
    case IOS:
      extraPadding = "" + tab + tab;
      break;
    case SITE:
    case MWEB:
      break;
    }

    return indent + extraPadding;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((methodLeadInContents == null) ? 0 : methodLeadInContents.hashCode());
    result = prime * result + ((methodOperations == null) ? 0 : methodOperations.hashCode());
    result = prime * result + ((methodSignature == null) ? 0 : methodSignature.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof GeneralPlatformMethodModel)) {
      return false;
    }

    GeneralPlatformMethodModel other = (GeneralPlatformMethodModel) obj;
    if (methodLeadInContents == null) {
      if (other.methodLeadInContents != null) {
        return false;
      }
    } else if (!methodLeadInContents.toString().equals(other.methodLeadInContents.toString())) {
      return false;
    }

    if (methodOperations == null) {
      if (other.methodOperations != null) {
        return false;
      }
    } else if (!methodOperations.equals(other.methodOperations)) {
      return false;
    }

    if (methodSignature == null) {
      if (other.methodSignature != null) {
        return false;
      }
    } else if (!methodSignature.equals(other.methodSignature)) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "GeneralPlatformMethodModel [methodSignature=" + methodSignature + ", methodLeadInContents=" + methodLeadInContents + ", methodOperations=" + methodOperations + "]";
  }

}
