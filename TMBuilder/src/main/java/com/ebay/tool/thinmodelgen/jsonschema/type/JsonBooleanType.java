package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import com.ebay.jsonpath.TMJPBooleanCheck;
import com.ebay.jsonpath.TMJPListOfBooleanCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;

@SuppressWarnings("serial")
public class JsonBooleanType extends JsonBaseType {

  /**
   * Create a new Boolean instance with the node name.
   * @param name Node name.
   */
  public JsonBooleanType(String name) {
    super(name, name, "boolean");
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.PRIMITIVE;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    if (willJsonPathReturnListOfResults(jsonPath)) {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPListOfBooleanCheck.class);
    } else {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPBooleanCheck.class);
    }
  }
}
