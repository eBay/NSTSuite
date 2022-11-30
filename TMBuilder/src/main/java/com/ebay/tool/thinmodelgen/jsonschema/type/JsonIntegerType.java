package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import com.ebay.jsonpath.TMJPIntegerCheck;
import com.ebay.jsonpath.TMJPListOfIntegerCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;

@SuppressWarnings("serial")
public class JsonIntegerType extends JsonBaseType {

  /**
   * Create a new Integer instance with the node name.
   * @param name Node name.
   */
  public JsonIntegerType(String name) {
    super(name, name, "integer");
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.PRIMITIVE;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    if (willJsonPathReturnListOfResults(jsonPath)) {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPListOfIntegerCheck.class);
    } else {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPIntegerCheck.class);
    }
  }
}
