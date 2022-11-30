package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import com.ebay.jsonpath.TMJPDoubleCheck;
import com.ebay.jsonpath.TMJPListOfDoubleCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;

@SuppressWarnings("serial")
public class JsonFloatType extends JsonBaseType {

  /**
   * Create a new Float (number) instance with the node name.
   * @param name Node name.
   */
  public JsonFloatType(String name) {
    super(name, name, "number(float)");
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.PRIMITIVE;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    if (willJsonPathReturnListOfResults(jsonPath)) {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPListOfDoubleCheck.class);
    } else {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPDoubleCheck.class);
    }
  }
}
