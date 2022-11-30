package com.ebay.service.logger.platforms.model;

import com.ebay.nst.coverage.Generated;

@Generated
public class GeneralPlatformFileModel {

  // In order of existence in the file.
  public StringBuilder classDocumentation = new StringBuilder(); // iOS/Swift
  public String packageName; // Android/Java
  public GeneralPlatformImportModel importContents = new GeneralPlatformImportModel();
  public StringBuilder preClassSignature = new StringBuilder();
  public String classSignature;
  public StringBuilder postClassSignature = new StringBuilder();
  public GeneralPlatformMemberFieldModel memberFieldContents = new GeneralPlatformMemberFieldModel();
  public StringBuilder preMethodFileContents = new StringBuilder();
  public GeneralPlatformMethodModel methodContents = new GeneralPlatformMethodModel();
  public StringBuilder postMethodFileContents = new StringBuilder();

  public String getClassDocumentation() {
    return this.classDocumentation.toString();
  }

  public void addClassDocumentationData(String line) {
    this.classDocumentation.append(line);
    this.classDocumentation.append("\n");
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String line) {
    this.packageName = line;
  }

  public GeneralPlatformImportModel getImportContents() {
    return importContents;
  }

  public void setImportContents(GeneralPlatformImportModel importContents) {
    this.importContents = importContents;
  }

  public void addImportStatement(String line) {
    this.importContents.addImportStatement(line);
  }

  public String getPreClassSignature() {
    return preClassSignature.toString();
  }

  public void addPreClassSignatureData(String line) {
    this.preClassSignature.append(line);
    this.preClassSignature.append("\n");
  }

  public String getClassSignature() {
    return classSignature;
  }

  public void addClassSignatureData(String line) {
    this.classSignature = line;
  }

  public String getPostClassSignature() {
    String postClassSignatureValue = postClassSignature.toString();

    if (postClassSignatureValue.trim().isEmpty()) {
      return "";
    }

    return postClassSignatureValue;
  }

  public void addPostClassSignatureData(String line) {
    this.postClassSignature.append(line);
    this.postClassSignature.append("\n");
  }

  public GeneralPlatformMemberFieldModel getMemberFieldContents() {
    return memberFieldContents;
  }

  public void setMemberFieldContents(GeneralPlatformMemberFieldModel memberFieldContents) {
    this.memberFieldContents = memberFieldContents;
  }

  public void addMemberFieldStatement(String line) {
    this.memberFieldContents.addMemberFieldStatement(line);
  }

  public String getPreMethodFileContents() {

    String preMethodFileContentsValue = preMethodFileContents.toString();

    if (preMethodFileContentsValue.trim().isEmpty()) {
      return "";
    }

    return preMethodFileContents.toString();
  }

  public void addPreMethodData(String line) {
    this.preMethodFileContents.append(line);
    this.preMethodFileContents.append("\n");
  }

  public GeneralPlatformMethodModel getMethodContents() {
    return methodContents;
  }

  public void setMethodContents(GeneralPlatformMethodModel methodContents) {
    this.methodContents = methodContents;
  }

  public void setMethodContentsSignature(String line) {
    this.methodContents.setMethodSignature(line);
  }

  public void addMethodContentsLeadIn(String line) {
    this.methodContents.addMethodLeadInContents(line);
  }

  public void addMethodContentsOperation(GeneralPlatformOperationModel model) {
    this.methodContents.addMethodOperations(model);
  }

  public String getPostMethodFileContents() {

    String postMethodFileContentsValue = postMethodFileContents.toString();

    if (postMethodFileContentsValue.trim().isEmpty()) {
      return "";
    }

    return postMethodFileContents.toString();
  }

  public void addPostMethodData(String line) {
    this.postMethodFileContents.append(line);
    this.postMethodFileContents.append("\n");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((preClassSignature == null) ? 0 : preClassSignature.hashCode());
    result = prime * result + ((classSignature == null) ? 0 : classSignature.hashCode());
    result = prime * result + ((postClassSignature == null) ? 0 : postClassSignature.hashCode());
    result = prime * result + ((importContents == null) ? 0 : importContents.hashCode());
    result = prime * result + ((memberFieldContents == null) ? 0 : memberFieldContents.hashCode());
    result = prime * result + ((methodContents == null) ? 0 : methodContents.hashCode());
    result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
    result = prime * result + ((postMethodFileContents == null) ? 0 : postMethodFileContents.hashCode());
    result = prime * result + ((preMethodFileContents == null) ? 0 : preMethodFileContents.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof GeneralPlatformFileModel)) {
      return false;
    }
    GeneralPlatformFileModel other = (GeneralPlatformFileModel) obj;

    if (preClassSignature == null) {
      if (other.preClassSignature != null) {
        return false;
      }
    } else if (!preClassSignature.toString().equals(other.preClassSignature.toString())) {
      return false;
    }

    if (classSignature == null) {
      if (other.classSignature != null) {
        return false;
      }
    } else if (!classSignature.equals(other.classSignature)) {
      return false;
    }

    if (postClassSignature == null) {
      if (other.postClassSignature != null) {
        return false;
      }
    } else if (!postClassSignature.toString().equals(other.postClassSignature.toString())) {
      return false;
    }

    if (importContents == null) {
      if (other.importContents != null) {
        return false;
      }
    } else if (!importContents.equals(other.importContents)) {
      return false;
    }
    if (memberFieldContents == null) {
      if (other.memberFieldContents != null) {
        return false;
      }
    } else if (!memberFieldContents.equals(other.memberFieldContents)) {
      return false;
    }
    if (methodContents == null) {
      if (other.methodContents != null) {
        return false;
      }
    } else if (!methodContents.equals(other.methodContents)) {
      return false;
    }
    if (packageName == null) {
      if (other.packageName != null) {
        return false;
      }
    } else if (!packageName.equals(other.packageName)) {
      return false;
    }
    if (postMethodFileContents == null) {
      if (other.postMethodFileContents != null) {
        return false;
      }
    } else if (!postMethodFileContents.toString().equals(other.postMethodFileContents.toString())) {
      return false;
    }
    if (preMethodFileContents == null) {
      if (other.preMethodFileContents != null) {
        return false;
      }
    } else if (!preMethodFileContents.toString().equals(other.preMethodFileContents.toString())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "GeneralPlatformFileModel [packageName="
        + packageName
        + ", importContents="
        + importContents
        + ", preClassSignature="
        + preClassSignature
        + ", classSignature="
        + classSignature
        + ", postClassSignature="
        + postClassSignature
        + ", memberFieldContents="
        + memberFieldContents
        + ", preMethodFileContents="
        + preMethodFileContents
        + ", methodContents="
        + methodContents
        + ", postMethodFileContents="
        + postMethodFileContents
        + "]";
  }

}
