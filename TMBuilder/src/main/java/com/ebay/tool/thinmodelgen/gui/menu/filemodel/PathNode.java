package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import java.util.Objects;

public class PathNode {

  private String treeUserObjectIdentifier;
  private String treeUserObjectClassName;
  private int treeNodeIndex;

  public PathNode(String treeUserObjectIdentifier, String treeUserObjectClassName, int treeNodeIndexUnderParent) {
    this.treeUserObjectIdentifier = treeUserObjectIdentifier;
    this.treeUserObjectClassName = treeUserObjectClassName;
    this.treeNodeIndex = treeNodeIndexUnderParent;
  }

  public String getTreeUserObjectIdentifier() {
    return treeUserObjectIdentifier;
  }

  public void setTreeUserObjectIdentifier(String treeUserObjectIdentifier) {
    this.treeUserObjectIdentifier = treeUserObjectIdentifier;
  }

  public String getTreeUserObjectClassName() {
    return treeUserObjectClassName;
  }

  public void setTreeUserObjectClassName(String treeUserObjectClassName) {
    this.treeUserObjectClassName = treeUserObjectClassName;
  }

  public int getTreeNodeIndex() {
    return treeNodeIndex;
  }

  public void setTreeNodeIndex(int treeNodeIndexUnderParent) {
    this.treeNodeIndex = treeNodeIndexUnderParent;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PathNode pathNode = (PathNode) o;
    return treeNodeIndex == pathNode.treeNodeIndex && Objects.equals(treeUserObjectIdentifier, pathNode.treeUserObjectIdentifier) && Objects.equals(treeUserObjectClassName, pathNode.treeUserObjectClassName);
  }

  @Override public int hashCode() {
    return Objects.hash(treeUserObjectIdentifier, treeUserObjectClassName, treeNodeIndex);
  }
}
