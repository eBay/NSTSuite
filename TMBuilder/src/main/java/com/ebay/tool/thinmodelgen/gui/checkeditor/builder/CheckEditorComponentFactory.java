package com.ebay.tool.thinmodelgen.gui.checkeditor.builder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.checkeditor.annotations.TMCheckData;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.BooleanComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.DoubleComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ErrorComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.IntegerComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ListOfBooleanComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ListOfDoubleComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ListOfIntegerComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ListOfStringComponent;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.StringComponent;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

public class CheckEditorComponentFactory {

  public static synchronized JComponent getComponentsFor(JsonBaseType jsonType, String forJsonPath, Class<?> expectedType) {

    System.out.println("GETTING COMPONENTS FOR: " + forJsonPath);

    JsonPathExecutor storedData = jsonType.getCheckForPath(forJsonPath);
    JsonPathExecutor instance = null;

    // Capture the data object
    if (expectedType.isInstance(storedData)) {
      instance = storedData;
    } else if (storedData != null && storedData.getClass().isAssignableFrom(expectedType)) {
      try {
        instance = (JsonPathExecutor) expectedType.getConstructor(storedData.getClass()).newInstance(storedData);
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
        e.printStackTrace();
        instance = storedData;
      }
    } else {
      try {
        instance = (JsonPathExecutor) expectedType.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
        return new ErrorComponent(e.getMessage());
      }
    }

    jsonType.updateCheckForPath(forJsonPath, instance);

    // Establish the JPanel container for each of the inputs
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    // Parse methods (getters and setters) from class.
    Method[] methods = expectedType.getMethods();
    int elementOrder = -1;

    for (Method setterMethod : methods) {

      System.out.println("-------------------------");

      if (setterMethod.isSynthetic()) {
        System.out.println("Skipping SYNTHETIC METHOD [" + setterMethod.getName() + "]");
        continue;
      }

      // Setters will have the TM annotation.
      TMCheckData checkData = setterMethod.getAnnotation(TMCheckData.class);

      if (checkData != null && checkData.enabled()) {

        String labelName = checkData.inputName();
        String labelDescription = checkData.inputDescription();

        System.out.println("Parsing...");
        System.out.println("Method Name : " + setterMethod.getName());
        System.out.println("TM input name : " + labelName);
        System.out.println("TM input description : " + labelDescription);
        System.out.println("TM getter method name: " + checkData.getterMethodName());

        // Find the matching getter
        String expectedGetterMethodName;

        if (!checkData.getterMethodName().equals("")) {
          expectedGetterMethodName = checkData.getterMethodName();
        } else {
          expectedGetterMethodName = setterMethod.getName().replaceFirst("set", "get");
        }

        System.out.println("Looking for getter method with name : " + expectedGetterMethodName);

        Method getterMethod = null;
        for (Method getter : methods) {
          if (expectedGetterMethodName.equals(getter.getName())) {
            getterMethod = getter;
            break;
          }
        }

        if (getterMethod == null) {
          String message = String.format("Getter method %s, from class %s, was not found.\nPlease specify getterMethodName in the annotation on setter method %s.", expectedGetterMethodName, expectedType.getName(), setterMethod.getName());
          panel.add(new ErrorComponent(message), getGridBagConstraints(elementOrder));
          continue;
        }

        // Take the setter parameters and create the input UI for that type
        Parameter[] parameters = setterMethod.getParameters();

        if (parameters.length != 1) {
          String message = String.format("Method %s, from class %s, does not contain a single parameter.\nMulti parameter setters are not currently supported.", setterMethod.getName(), expectedType.getName());
          panel.add(new ErrorComponent(message), getGridBagConstraints(elementOrder));
          continue;
        }

        elementOrder++;

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = elementOrder;
        constraint.insets = new Insets(2, 2, 2, 2);
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;

        Type type = parameters[0].getParameterizedType();
        String typeName = type.getTypeName();

        System.out.println("Matching component for panel [typeName] : " + typeName);

        if (typeName.equals("java.util.List<java.lang.String>")) {
          panel.add(new ListOfStringComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("java.util.List<java.lang.Boolean>")) {
          panel.add(new ListOfBooleanComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("java.lang.String")) {
          panel.add(new StringComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
          panel.add(new IntegerComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("double") || typeName.equals("java.lang.Double")) {
          panel.add(new DoubleComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("boolean") || typeName.equals("java.lang.Boolean")) {
          panel.add(new BooleanComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("java.util.List<java.lang.Double>")) {
          panel.add(new ListOfDoubleComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else if (typeName.equals("java.util.List<java.lang.Integer>")) {
          panel.add(new ListOfIntegerComponent(jsonType, forJsonPath, labelName, labelDescription, instance, setterMethod, getterMethod), constraint);
        } else {
          System.out.println("Component match unknown. Unidentified type: " + typeName);
        }
      } else {
        System.out.println("No TMCheckData annotation found on method " + setterMethod.getName());
      }
    }

    JLabel label = new JLabel("", JLabel.LEFT);

    if (elementOrder < 0) {
      elementOrder = 0;
    }

    GridBagConstraints paneConstraints = new GridBagConstraints();
    paneConstraints.fill = GridBagConstraints.BOTH;
    paneConstraints.gridx = 0;
    paneConstraints.gridy = elementOrder;
    paneConstraints.weightx = 1.0;
    paneConstraints.weighty = 1.0;
    paneConstraints.insets = new Insets(2, 2, 2, 2);

    JPanel pane = new JPanel();
    pane.add(label);
    panel.add(pane, paneConstraints);

    return panel;
  }

  private static GridBagConstraints getGridBagConstraints(int order) {

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = order;
    constraints.weightx = 1.0;
    constraints.weighty = 0.1;
    constraints.insets = new Insets(2, 2, 2, 2);

    return constraints;
  }
}
