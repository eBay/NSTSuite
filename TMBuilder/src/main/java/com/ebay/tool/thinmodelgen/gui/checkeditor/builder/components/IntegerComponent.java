package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

@SuppressWarnings("serial")
public class IntegerComponent extends BaseComponent {

  public IntegerComponent(JsonBaseType saveInstance, String jsonPath, String labelName, String labelDescription, JsonPathExecutor jsonPathExecutor, Method setterMethod, Method getterMethod) {
    super(saveInstance, jsonPath, labelName, labelDescription, jsonPathExecutor, setterMethod, getterMethod);

    this.setLayout(new GridBagLayout());

    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.fill = GridBagConstraints.HORIZONTAL;
    labelConstraints.gridx = 0;
    labelConstraints.gridy = 0;
    labelConstraints.weightx = 5.0;
    labelConstraints.weighty = 1.0;
    labelConstraints.insets = new Insets(2, 2, 2, 2);

    this.add(label, labelConstraints);

    GridBagConstraints integerTextFieldConstraints = new GridBagConstraints();
    integerTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    integerTextFieldConstraints.gridx = 1;
    integerTextFieldConstraints.gridy = 0;
    integerTextFieldConstraints.weightx = 5.0;
    integerTextFieldConstraints.weighty = 1.0;
    integerTextFieldConstraints.insets = new Insets(2, 2, 2, 2);

    IntegerTextField integerTextField = new IntegerTextField(5);

    Object getValue = null;

    try {
      getValue = getterMethod.invoke(jsonPathExecutor);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }

    if (getValue != null && getValue instanceof Integer) {
      Integer value = (Integer) getValue;
      integerTextField.setText(String.valueOf(value));
    }

    this.add(integerTextField, integerTextFieldConstraints);
  }

  class IntegerTextField extends JTextField implements ActionListener {

    public IntegerTextField(int cols) {
      super(cols);
      this.addActionListener(this);
    }

    @Override
    protected Document createDefaultModel() {
      return new IntegerDocument();
    }

    class IntegerDocument extends PlainDocument {

      @Override
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        if (str == null) {
          return;
        }

        try {
          Integer.parseInt(str);
        } catch (NumberFormatException e) {
          return;
        }

        super.insertString(offs, str, a);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      String text = this.getText();
      Integer value = null;

      if (!text.isEmpty()) {
        value = Integer.parseInt(text);
      }

      try {
        setterMethod.invoke(jsonPathExecutor, value);
        saveInstance.updateCheckForPath(jsonPath, jsonPathExecutor);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        e1.printStackTrace();
      }
    }
  }
}
