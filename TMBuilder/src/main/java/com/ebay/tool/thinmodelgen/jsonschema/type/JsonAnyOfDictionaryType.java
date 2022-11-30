package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import com.ebay.jsonpath.TMJPJsonObjectCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ErrorComponent;

@SuppressWarnings("serial")
public class JsonAnyOfDictionaryType extends JsonBaseType {

  /**
   * Create a new Dictionary (key/value) instance with the node name.
   * @param name Node name.
   */
  public JsonAnyOfDictionaryType(String name) {
    super(name, name, "anyOf dictionary");
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.POLYMORPHIC_DICTIONARY;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    if (willJsonPathReturnListOfResults(jsonPath)) {
      return new ErrorComponent("List of anyOf dictionary is not currently supported.");
    } else {
      return CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPJsonObjectCheck.class);
    }
  }
}
