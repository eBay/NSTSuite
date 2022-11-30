package com.ebay.service.logger.platforms.model;

import java.util.ArrayList;

import com.ebay.nst.coverage.Generated;

@Generated
public class GeneralPlatformImportModel {

  private ArrayList<String> importStatements = new ArrayList<>();

  public void addImportStatement(String importStatement) {
    if (!importStatements.contains(importStatement)) {
      importStatements.add(importStatement);
    }
  }

  public void removeImportStatement(String importStatement) {
    importStatements.remove(importStatement);
  }

  public String[] getImportStatements() {
    return importStatements.toArray(new String[0]);
  }

  public String getImportStatementBlock() {

    StringBuilder importStatementBlock = new StringBuilder();

    for (String statement : importStatements) {
      importStatementBlock.append(statement);
      importStatementBlock.append("\n");
    }

    return importStatementBlock.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((importStatements == null) ? 0 : importStatements.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof GeneralPlatformImportModel)) {
      return false;
    }

    GeneralPlatformImportModel other = (GeneralPlatformImportModel) obj;

    if (importStatements == null) {
      if (other.importStatements != null) {
        return false;
      }
    } else if (!importStatements.equals(other.importStatements)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "GeneralPlatformImportModel [importStatements=" + importStatements + "]";
  }
}
