package com.ebay.tool.thinmodelgen.gui.checkeditor;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ebay.tool.thinmodelgen.gui.patheditor.events.JsonPathEditorSelectionChangeListener;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

public class JsonPathCheckEditor implements JsonPathEditorSelectionChangeListener {

  private JScrollPane jsonPathCheckEditorPane;
  private JPanel defaultPanel;

  public JsonPathCheckEditor() {

    defaultPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(defaultPanel, BoxLayout.Y_AXIS);
    defaultPanel.setLayout(boxLayout);
    defaultPanel.add(new JLabel("Add a JSON path to show check options."));

    jsonPathCheckEditorPane = new JScrollPane(defaultPanel);
  }

  public JScrollPane getJsonPathCheckEditorScrollPane() {
    return jsonPathCheckEditorPane;
  }

  // ---------------------------------------------------
  // JsonPathEditorSelectionChangeListener
  // ---------------------------------------------------

  @Override
  public void jsonPathEditorSelectionChanged(String jsonPath, JsonBaseType node) {

    jsonPathCheckEditorPane.getViewport().removeAll();
    jsonPathCheckEditorPane.getViewport().validate();

    if (node == null) {
      jsonPathCheckEditorPane.getViewport().add(defaultPanel);
    } else {
      jsonPathCheckEditorPane.getViewport().add(node.getCheckEditorComponent(jsonPath));
    }

    jsonPathCheckEditorPane.getViewport().validate();
  }
}
