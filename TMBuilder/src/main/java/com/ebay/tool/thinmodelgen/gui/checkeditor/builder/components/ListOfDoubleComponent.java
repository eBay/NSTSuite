package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.Document;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

@SuppressWarnings("serial")
public class ListOfDoubleComponent extends BaseComponent {

  private DefaultListModel<String> listModel = new DefaultListModel<>();

  public ListOfDoubleComponent(JsonBaseType saveInstance, String jsonPath, String labelName, String labelDescription, JsonPathExecutor jsonPathExecutor, Method setterMethod, Method getterMethod) {
    super(saveInstance, jsonPath, labelName, labelDescription, jsonPathExecutor, setterMethod, getterMethod);

    this.setLayout(new GridBagLayout());

    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.fill = GridBagConstraints.HORIZONTAL;
    labelConstraints.gridx = 0;
    labelConstraints.gridy = 0;
    labelConstraints.weightx = 1.0;
    labelConstraints.weighty = 0.5;
    labelConstraints.insets = new Insets(2, 2, 2, 2);
    labelConstraints.gridwidth = GridBagConstraints.REMAINDER;

    this.add(label, labelConstraints);

    // Add the list
    GridBagConstraints listConstraints = new GridBagConstraints();
    listConstraints.fill = GridBagConstraints.BOTH;
    listConstraints.gridx = 0;
    listConstraints.gridy = 1;
    listConstraints.weightx = 1.0;
    listConstraints.weighty = 1.0;
    listConstraints.insets = new Insets(2, 2, 2, 2);
    listConstraints.gridwidth = GridBagConstraints.REMAINDER;

    Object getValue = null;

    try {
      getValue = getterMethod.invoke(jsonPathExecutor);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
      e1.printStackTrace();
    }

    if (getValue != null && getValue instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> stringValues = (List<String>) getValue;
      for (String val : stringValues) {
        listModel.addElement(val);
      }
    }

    JList<String> list = new JList<>(listModel);
    list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.add(new JScrollPane(list), listConstraints);

    // Setup the move up/down panel
    JPanel moveUpDownPanel = new JPanel(new GridBagLayout());

    // Setup the move down button
    GridBagConstraints moveDownButtonConstraints = new GridBagConstraints();
    moveDownButtonConstraints.gridx = 0;
    moveDownButtonConstraints.gridy = 0;
    moveDownButtonConstraints.weightx = 0.1;
    moveDownButtonConstraints.weighty = 1.0;
    moveDownButtonConstraints.insets = new Insets(2, 2, 2, 2);

    JButton downButton = new JButton("down");
    downButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();

        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex > model.size() - 2) {
          return;
        }

        String element = model.get(selectedIndex);
        model.remove(selectedIndex);
        model.add(selectedIndex + 1, element);
        list.setSelectedIndex(selectedIndex + 1);
        updateModelInstance();
      }
    });

    moveUpDownPanel.add(downButton, moveDownButtonConstraints);

    // Setup the move up button
    GridBagConstraints moveUpButtonConstraints = new GridBagConstraints();
    moveUpButtonConstraints.gridx = 1;
    moveUpButtonConstraints.gridy = 0;
    moveUpButtonConstraints.weightx = 0.1;
    moveUpButtonConstraints.weighty = 1.0;
    moveUpButtonConstraints.insets = new Insets(2, 2, 2, 2);

    JButton upButton = new JButton("up");
    upButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex < 1) {
          return;
        }
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        String element = model.get(selectedIndex);
        model.remove(selectedIndex);
        model.add(selectedIndex - 1, element);
        list.setSelectedIndex(selectedIndex - 1);
        updateModelInstance();
      }
    });

    moveUpDownPanel.add(upButton, moveUpButtonConstraints);

    GridBagConstraints moveUpDownConstraints = new GridBagConstraints();
    moveUpDownConstraints.fill = GridBagConstraints.HORIZONTAL;
    moveUpDownConstraints.gridx = 0;
    moveUpDownConstraints.gridy = 2;
    moveUpDownConstraints.weightx = 1.0;
    moveUpDownConstraints.weighty = 0.1;
    moveUpDownConstraints.insets = new Insets(2, 2, 2, 2);
    moveUpDownConstraints.gridwidth = GridBagConstraints.REMAINDER;

    this.add(moveUpDownPanel, moveUpDownConstraints);

    // Setup the control panel.
    JPanel controls = new JPanel(new GridBagLayout());

    // Add the remove button
    GridBagConstraints removeButtonConstraints = new GridBagConstraints();
    removeButtonConstraints.gridx = 0;
    removeButtonConstraints.gridy = 0;
    removeButtonConstraints.weightx = 0.1;
    removeButtonConstraints.weighty = 1.0;
    removeButtonConstraints.insets = new Insets(2, 2, 2, 2);

    JButton removeButton = new JButton("-");
    removeButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex < 0) {
          return;
        }
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        model.remove(selectedIndex);
        updateModelInstance();
      }
    });

    controls.add(removeButton, removeButtonConstraints);

    // Add text field

    GridBagConstraints integerTextFieldConstraints = new GridBagConstraints();
    integerTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    integerTextFieldConstraints.gridx = 1;
    integerTextFieldConstraints.gridy = 0;
    integerTextFieldConstraints.weightx = 5.0;
    integerTextFieldConstraints.weighty = 1.0;
    integerTextFieldConstraints.insets = new Insets(2, 2, 2, 2);

    DoubleTextField doubleTextField = new DoubleTextField(5);

    controls.add(doubleTextField, integerTextFieldConstraints);

    // Add the add button
    GridBagConstraints addButtonConstraints = new GridBagConstraints();
    addButtonConstraints.gridx = 2;
    addButtonConstraints.gridy = 0;
    addButtonConstraints.weightx = 0.1;
    addButtonConstraints.weighty = 1.0;
    addButtonConstraints.insets = new Insets(2, 2, 2, 2);

    JButton addButton = new JButton("+");
    addButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String text = doubleTextField.getText();
        doubleTextField.setText("");
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        model.addElement(text);
        updateModelInstance();
      }
    });

    controls.add(addButton, addButtonConstraints);

    // Add the controls.
    GridBagConstraints controlConstraints = new GridBagConstraints();
    controlConstraints.fill = GridBagConstraints.HORIZONTAL;
    controlConstraints.gridx = 0;
    controlConstraints.gridy = 3;
    controlConstraints.weightx = 1.0;
    controlConstraints.weighty = 0.5;
    controlConstraints.insets = new Insets(2, 2, 2, 2);
    controlConstraints.gridwidth = GridBagConstraints.REMAINDER;

    this.add(controls, controlConstraints);
  }

  private void updateModelInstance() {

    System.out.println("--------------------------");
    System.out.println("Saving data...");

    ArrayList<Double> saveData = new ArrayList<>();

    for (int i = 0; i < listModel.size(); i++) {
      String stringData = listModel.getElementAt(i);
      Double data = Double.parseDouble(stringData);
      System.out.println(i + " " + stringData + " parsed as [" + data + "]");
      saveData.add(data);
    }

    if (saveData.isEmpty()) {
      saveData = null;
    }

    try {
      setterMethod.invoke(jsonPathExecutor, saveData);
      saveInstance.updateCheckForPath(jsonPath, jsonPathExecutor);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
      e1.printStackTrace();
    }
  }

  class DoubleTextField extends JTextField implements ActionListener {

    public DoubleTextField(int cols) {
      super(cols);
      this.addActionListener(this);
    }

    @Override
    protected Document createDefaultModel() {
      return new DoubleDocument();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      String text = this.getText();
      Double value = null;

      if (!text.isEmpty()) {
        value = Double.parseDouble(text);
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
