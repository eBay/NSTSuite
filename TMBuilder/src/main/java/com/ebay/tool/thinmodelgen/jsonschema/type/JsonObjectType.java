package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import com.ebay.jsonpath.TMJPJsonObjectCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ErrorComponent;

@SuppressWarnings("serial")
public class JsonObjectType extends JsonBaseType {

  /**
   * Create a new String instance with the node name.
   * @param name Node name.
   */
  public JsonObjectType(String name) {
    super(name, name, "object");
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.OBJECT;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    if (willJsonPathReturnListOfResults(jsonPath)) {
      return new ErrorComponent("List of object is not currently supported.");
    } else {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPJsonObjectCheck.class);
    }
  }
}
