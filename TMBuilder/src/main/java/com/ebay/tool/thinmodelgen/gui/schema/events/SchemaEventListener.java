package com.ebay.tool.thinmodelgen.gui.schema.events;

import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

public interface SchemaEventListener {

  /**
   * Called when a tree node is selected.
   * @param path Path from root (index 0) to the selected node, or null if node was unselected or no node selected.
   */
  public void treeNodeSelected(JsonBaseType[] path);
}
