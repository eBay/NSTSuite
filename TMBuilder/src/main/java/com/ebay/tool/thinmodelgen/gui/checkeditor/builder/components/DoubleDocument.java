package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class DoubleDocument extends PlainDocument {

  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

    if (str == null) {
      return;
    }

    if (!str.equals(".")) {
      try {
        Double.parseDouble(str);
      } catch (NumberFormatException e) {
        return;
      }
    } else if (super.getText(0, super.getLength()).contains(".")) {
      return;
    }

    String currentText = super.getText(0, super.getLength());
    int dotIndex = currentText.indexOf(".");
    if (currentText.contains(".") && offs > dotIndex) {
      int textLength = currentText.length();
      if ((textLength - dotIndex) > 10) {
        return;
      }
    }

    super.insertString(offs, str, a);
  }
}
