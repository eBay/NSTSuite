package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;

import javax.swing.JComponent;

import com.ebay.jsonpath.TMJPListOfStringCheck;
import com.ebay.jsonpath.TMJPStringCheck;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.CheckEditorComponentFactory;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ListOfStringComponent;

@SuppressWarnings("serial")
public class JsonStringType extends JsonBaseType {

  private String[] enumSet = new String[0];

  /**
   * Create a new String instance with the node name.
   * @param name Node name.
   */
  public JsonStringType(String name) {
    this(name, null);
  }

  /**
   * Create a new String instance with the node name and enum set specified.
   * @param name Node name.
   * @param enumSet Enum set.
   */
  public JsonStringType(String name, String[] enumSet) {
    super(name, name, "String");

    if (enumSet != null) {
      this.enumSet = new String[enumSet.length];
      for (int i = 0; i < enumSet.length; i++) {
        this.enumSet[i] = enumSet[i];
      }
    }
  }

  @Override
  public JsonType getJsonType() {
    return JsonType.PRIMITIVE;
  }

  /**
   * Get the enum set.
   * @return Enum set. If no enums defined, the array will be empty.
   */
  public String[] getEnumSet() {
    return enumSet;
  }

  @Override
  public Component getCheckEditorComponent(String jsonPath) {

    JComponent component = null;

    if (willJsonPathReturnListOfResults(jsonPath)) {
      component = CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPListOfStringCheck.class);
    } else {
      component = CheckEditorComponentFactory.getComponentsFor(this, jsonPath, TMJPStringCheck.class);
    }

    if (component instanceof ListOfStringComponent) {

      if (enumSet != null && enumSet.length > 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Defined enum values:\n");

        for (String value : enumSet) {
          stringBuilder.append(String.format("- %s\n", value));
        }

        ((ListOfStringComponent)component).addEnumInformation(stringBuilder.toString());
      }
    }

    return component;
  }
}
