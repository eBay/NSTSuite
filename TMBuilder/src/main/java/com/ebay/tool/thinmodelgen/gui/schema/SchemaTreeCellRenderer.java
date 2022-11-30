package com.ebay.tool.thinmodelgen.gui.schema;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

public class SchemaTreeCellRenderer implements TreeCellRenderer {

  private static final String defaultNode = "/icons/white12x12.png";
  private static final String defaultFolder = "/icons/gray12x12.png";
  private static final String selectedNode = "/icons/yellow12x12.png";
  private static final String nodeWithChecks = "/icons/green12x12.png";
  private static final String parentOfNodeWithChecks = "/icons/blue12x12.png";

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
    JsonBaseType jsonBaseType = (JsonBaseType) treeNode.getUserObject();

    JLabel label = new JLabel();

    if (jsonBaseType != null) {
      label.setText(jsonBaseType.toString());
    }

    URL icon = null;

    if (leaf) {
      icon = getClass().getResource(defaultNode);
    } else {
      icon = getClass().getResource(defaultFolder);
    }

    if (selected) {
      icon = getClass().getResource(selectedNode);
    } else if (jsonBaseType != null && jsonBaseType.hasAssignedValidations()) {
      icon = getClass().getResource(nodeWithChecks);
    } else if (hasValidationNodeInPath(treeNode)) {
      icon = getClass().getResource(parentOfNodeWithChecks);
    }

    // Add the icon to the label
    if (icon != null) {
      label.setIcon(new ImageIcon(icon));
    }

    return label;
  }


  private boolean hasValidationNodeInPath(DefaultMutableTreeNode treeNode) {

    JsonBaseType jsonBaseType = (JsonBaseType) treeNode.getUserObject();
    if (jsonBaseType != null && jsonBaseType.hasAssignedValidations()) {
      return true;
    }

    for (int i = 0; i < treeNode.getChildCount(); i++) {
      if (hasValidationNodeInPath((DefaultMutableTreeNode) treeNode.getChildAt(i))) {
        return true;
      }
    }

    return false;
  }
}
