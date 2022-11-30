package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

@SuppressWarnings("serial")
public abstract class BaseComponent extends JPanel {

  protected JsonBaseType saveInstance;
  protected String jsonPath;
  protected JsonPathExecutor jsonPathExecutor;
  protected Method setterMethod;
  protected Method getterMethod;
  protected JPanel label;

  public BaseComponent(JsonBaseType saveInstance, String jsonPath, String labelName, String labelDescription, JsonPathExecutor jsonPathExecutor, Method setterMethod, Method getterMethod) {
    this.saveInstance = saveInstance;
    this.jsonPath = jsonPath;
    this.jsonPathExecutor = jsonPathExecutor;
    this.setterMethod = setterMethod;
    this.getterMethod = getterMethod;

    URL questionIcon = this.getClass().getResource("/icons/question.png");
    ImageIcon questionMark = new ImageIcon(questionIcon);

    JLabel text = new JLabel(labelName, JLabel.LEFT);
    JLabel icon = new JLabel(questionMark, JLabel.RIGHT);
    icon.setToolTipText(labelDescription);

    label = new JPanel(new BorderLayout());
    label.add(text, BorderLayout.WEST);
    label.add(icon, BorderLayout.EAST);

    this.setBorder(BorderFactory.createLineBorder(Color.black));
  }
}
