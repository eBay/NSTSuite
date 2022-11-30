package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import com.ebay.jsonpath.TMJPJsonArrayCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ErrorComponent;

@SuppressWarnings("serial")
public class JsonArrayType extends JsonBaseType {

  /**
   * Create a new JsonArray instance with the node name.
   * @param name Node name.
   */
  public JsonArrayType(String name) {
    super(name, name + "[]", "array");
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.ARRAY;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    if (willJsonPathReturnListOfResults(jsonPath)) {
      return new ErrorComponent("List of arrays is not currently supported.");
    } else {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPJsonArrayCheck.class);
    }
  }
}
