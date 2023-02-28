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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

@SuppressWarnings("serial")
public class ListOfStringComponent extends BaseComponent {

  private DefaultListModel<String> listModel = new DefaultListModel<>();

  public ListOfStringComponent(JsonBaseType saveInstance, String jsonPath, String labelName, String labelDescription, JsonPathExecutor jsonPathExecutor, Method setterMethod, Method getterMethod) {
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
        if (selectedIndex > model.size()-2) {
          return;
        }

        String element = model.get(selectedIndex);
        model.remove(selectedIndex);
        model.add(selectedIndex+1, element);
        list.setSelectedIndex(selectedIndex+1);
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
        model.add(selectedIndex-1, element);
        list.setSelectedIndex(selectedIndex-1);
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

    // Add the text field
    GridBagConstraints inputFieldConstraints = new GridBagConstraints();
    inputFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    inputFieldConstraints.gridx = 1;
    inputFieldConstraints.gridy = 0;
    inputFieldConstraints.weightx = 0.8;
    inputFieldConstraints.weighty = 1.0;
    inputFieldConstraints.insets = new Insets(2, 2, 2, 2);

    JTextField inputField = new JTextField();

    controls.add(inputField, inputFieldConstraints);

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
        String text = inputField.getText();
        if (text.isEmpty()) {
          return;
        }
        inputField.setText("");
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

  public void addEnumInformation(String enumMessage) {

    int numberOFComponents = this.getComponentCount();

    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.fill = GridBagConstraints.HORIZONTAL;
    labelConstraints.gridx = 0;
    labelConstraints.gridy = numberOFComponents+1;
    labelConstraints.weightx = 1.0;
    labelConstraints.weighty = 0.5;
    labelConstraints.insets = new Insets(2, 2, 2, 2);
    labelConstraints.gridwidth = GridBagConstraints.REMAINDER;

    JLabel text = new JLabel(enumMessage, JLabel.LEFT);

    this.add(text, labelConstraints);
  }

  private void updateModelInstance() {

    System.out.println("--------------------------");
    System.out.println("Saving data...");

    ArrayList<String> saveData = new ArrayList<>();

    for (int i = 0; i < listModel.size(); i++) {
      String data = listModel.getElementAt(i);
      System.out.println(i + " " + data);
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
}
