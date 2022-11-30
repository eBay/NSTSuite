package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import java.util.Arrays;
import java.util.Objects;

public class NodeModel {

  private PathNode[] treePathToNode;
  private String serializedUserObject;

  public NodeModel(PathNode[] treePathToNode, String serializedUserObject) {
    super();
    this.treePathToNode = treePathToNode;
    this.serializedUserObject = serializedUserObject;
  }

  public PathNode[] getTreePathToNode() {
    return treePathToNode;
  }

  public void setTreePathToNode(PathNode[] treePathToNode) {
    this.treePathToNode = treePathToNode;
  }

  public String getSerializedUserObject() {
    return serializedUserObject;
  }

  public void setSerializedUserObject(String serializedUserObject) {
    this.serializedUserObject = serializedUserObject;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NodeModel nodeModel = (NodeModel) o;
    return Arrays.equals(treePathToNode, nodeModel.treePathToNode) && Objects.equals(serializedUserObject, nodeModel.serializedUserObject);
  }

  @Override public int hashCode() {
    int result = Objects.hash(serializedUserObject);
    result = 31 * result + Arrays.hashCode(treePathToNode);
    return result;
  }
}
