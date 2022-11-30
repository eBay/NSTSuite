package com.ebay.service.logger.platforms.model;

import java.util.ArrayList;

import com.ebay.nst.coverage.Generated;

@Generated
public class GeneralPlatformMemberFieldModel {

  private ArrayList<String> memberFieldStatements = new ArrayList<>();

  public void addMemberFieldStatement(String memberFieldStatement) {
    memberFieldStatements.add(memberFieldStatement);
  }

  public void removeMemberFieldStatement(String memberFieldStatement) {
    memberFieldStatements.remove(memberFieldStatement);
  }

  public String getMemberFieldStatementBlock() {

    StringBuilder memberFieldStatementBlock = new StringBuilder();

    for (String statement : memberFieldStatements) {
      memberFieldStatementBlock.append(statement);
      memberFieldStatementBlock.append("\n");
    }

    return memberFieldStatementBlock.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((memberFieldStatements == null) ? 0 : memberFieldStatements.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof GeneralPlatformMemberFieldModel)) {
      return false;
    }

    GeneralPlatformMemberFieldModel other = (GeneralPlatformMemberFieldModel) obj;

    if (memberFieldStatements == null) {
      if (other.memberFieldStatements != null) {
        return false;
      }
    } else if (!memberFieldStatements.equals(other.memberFieldStatements)) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "GeneralPlatformMemberFieldModel [memberFieldStatements=" + memberFieldStatements + "]";
  }

}
