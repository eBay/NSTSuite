package com.ebay.service.logger.platforms.util;

import java.util.Arrays;

public class PlatformApiToFuiMapping {

  private String platformRequestTypeStatement;
  private String[] navigationStatements;
  private String[] entryStatements;
  private String[] importStatements;
  private String[] memberFieldStatements;

  /**
   * Set the platform request type (platform specific API wrapper identifier) statement as read directly from the source file.
   * @param statement Statement to set.
   */
  public void setPlatformRequestTypeStatements(String statement) {
    if (statement != null) {

      if (statement.split("\n").length != 1) {
        throw new IllegalArgumentException(String.format("Platform request type values cannot be multi-line statements. Found %s.", statement));
      }

      platformRequestTypeStatement = statement;
    }
  }

  /**
   * Set the navigation (post initial operation) statements as read directly from the source file. Splits statements by new line character.
   * @param statements Statement(s) to set.
   */
  public void setNavigationStatements(String statements) {
    if (statements != null) {
      navigationStatements = statements.split("\n");
    }
  }

  /**
   * Set the entry (first operation) statements as read directly from the source file. Splits statements by new line character.
   * @param statements Statement(s) to set.
   */
  public void setEntryStatements(String statements) {
    if (statements != null) {
      entryStatements = statements.split("\n");
    }
  }

  /**
   * Set the import statements as read directly from the source file. Splits statements by new line character.
   * @param statements Statement(s) to set.
   */
  public void setImportStatements(String statements) {
    if (statements != null) {
      importStatements = statements.split("\n");
    }
  }

  /**
   * Set the member field statements as read directly from the source file. Splits statements by new line character.
   * @param statements Statement(s) to set.
   */
  public void setMemberFieldStatements(String statements) {
    if (statements != null) {
      memberFieldStatements = statements.split("\n");
    }
  }

  /**
   * Get the platform request type statement.
   * @return Platform request type statement.
   */
  public String getPlatformRequestTypeStatement() {
    return platformRequestTypeStatement;
  }

  /**
   * Get the navigation (second or later operation) statements.
   * @return One statement line per array index.
   */
  public String[] getNavigationStatements() {
    return navigationStatements;
  }

  /**
   * Get the entry (first operation) statements.
   * @return One statement line per array index.
   */
  public String[] getEntryStatements() {
    return entryStatements;
  }

  /**
   * Get the import statements.
   * @return One statement line per array index.
   */
  public String[] getImportStatements() {
    return importStatements;
  }

  /**
   * Get the member field statements.
   * @return One statement line per array index.
   */
  public String[] getMemeberFieldStatements() {
    return memberFieldStatements;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + platformRequestTypeStatement.hashCode();
    result = prime * result + Arrays.hashCode(entryStatements);
    result = prime * result + Arrays.hashCode(importStatements);
    result = prime * result + Arrays.hashCode(memberFieldStatements);
    result = prime * result + Arrays.hashCode(navigationStatements);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PlatformApiToFuiMapping)) {
      return false;
    }
    PlatformApiToFuiMapping other = (PlatformApiToFuiMapping) obj;
    if (platformRequestTypeStatement == null) {
      if (other.platformRequestTypeStatement == null) {
        return true;
      } else {
        return false;
      }
    } else if (!platformRequestTypeStatement.equals(other.platformRequestTypeStatement)) {
      return false;
    }
    if (!Arrays.equals(entryStatements, other.entryStatements)) {
      return false;
    }
    if (!Arrays.equals(importStatements, other.importStatements)) {
      return false;
    }
    if (!Arrays.equals(memberFieldStatements, other.memberFieldStatements)) {
      return false;
    }
    if (!Arrays.equals(navigationStatements, other.navigationStatements)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "PlatformApiToFuiMapping [navigationStatements="
        + Arrays.toString(navigationStatements)
        + ", entryStatements="
        + Arrays.toString(entryStatements)
        + ", importStatements="
        + Arrays.toString(importStatements)
        + ", memberFieldStatements="
        + Arrays.toString(memberFieldStatements)
        + ", platformRequestTypeStatement="
        + platformRequestTypeStatement
        + "]";
  }

}
