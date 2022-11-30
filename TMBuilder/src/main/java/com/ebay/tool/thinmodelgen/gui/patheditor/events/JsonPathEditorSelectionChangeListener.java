package com.ebay.tool.thinmodelgen.gui.patheditor.events;

import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

public interface JsonPathEditorSelectionChangeListener {

  /**
   * Receive notification of JSON path editor selection changes.
   * @param jsonPath JSON path selected, or null if none selected.
   * @param node JSON Base Type.
   */
  public void jsonPathEditorSelectionChanged(String jsonPath, JsonBaseType node);
}
