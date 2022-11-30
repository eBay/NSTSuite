package com.ebay.tool.thinmodelgen.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import com.ebay.tool.thinmodelgen.gui.checkeditor.JsonPathCheckEditor;
import com.ebay.tool.thinmodelgen.gui.menu.TMBuilderMenu;
import com.ebay.tool.thinmodelgen.gui.patheditor.JsonPathEditor;
import com.ebay.tool.thinmodelgen.gui.schema.SchemaTree;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

  private static MainWindow instance;

  private MainWindow() {
    initialize();
  }

  private void setupExitConfirmation() {
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent arg0) {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        int result = JOptionPane.showConfirmDialog(instance,
            "Are you sure you want to exit? All unsaved progress will be lost.", "Exit", JOptionPane.YES_NO_OPTION);

        if (result == 0) {
          System.exit(0);
        }
      }
    });
  }

  public static MainWindow getInstance() {

    if (instance == null) {
      synchronized (MainWindow.class) {
        if (instance == null) {
          instance = new MainWindow();
        }
      }
    }

    return instance;
  }

  public void setOpenFilePath(String filePath) {
    if (filePath != null) {
      this.setTitle(String.format("%s - %s", TMGuiConstants.APP_NAME, filePath));
    } else {
      this.setTitle(TMGuiConstants.APP_NAME);
    }
  }

  private void initialize() {

    setupExitConfirmation();

    Dimension windowDimension = new Dimension(TMGuiConstants.DEFAULT_WINDOW_WIDTH, TMGuiConstants.DEFAULT_WINDOW_HEIGHT);

    this.setTitle(TMGuiConstants.APP_NAME);
    this.setPreferredSize(windowDimension);
    this.setSize(windowDimension);
    this.setBackground(Color.BLACK);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new GridBagLayout());

    SchemaTree schemaTree;
    try {
      schemaTree = new SchemaTree();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    this.setJMenuBar(new TMBuilderMenu(schemaTree));

    JScrollPane treeScrollPane = schemaTree.getTreeScrollPane();

    JsonPathCheckEditor jsonPathCheckEditor = new JsonPathCheckEditor();
    JScrollPane jsonPathCheckEditorPane = new JScrollPane(jsonPathCheckEditor.getJsonPathCheckEditorScrollPane());

    GridBagConstraints splitPaneConstraints = new GridBagConstraints();
    splitPaneConstraints.fill = GridBagConstraints.BOTH;
    splitPaneConstraints.gridx = 0;
    splitPaneConstraints.gridy = 0;
    splitPaneConstraints.weightx = 1.0;
    splitPaneConstraints.weighty = 0.5;
    splitPaneConstraints.insets = new Insets(5, 5, 5, 5);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, jsonPathCheckEditorPane);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(windowDimension.width/2);
    splitPane.setResizeWeight(1);
    this.add(splitPane, splitPaneConstraints);

    JsonPathEditor jsonPathEditor = new JsonPathEditor();
    schemaTree.addSchemaEventListener(jsonPathEditor);
    jsonPathEditor.setJTreeRefresh(schemaTree);
    jsonPathEditor.addJsonPathEditorSelectionChangeListener(jsonPathCheckEditor);

    GridBagConstraints jsonPathEditFieldConstraints = new GridBagConstraints();
    jsonPathEditFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    jsonPathEditFieldConstraints.gridx = 0;
    jsonPathEditFieldConstraints.gridy = 1;
    jsonPathEditFieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
    jsonPathEditFieldConstraints.weightx = 1.0;
    jsonPathEditFieldConstraints.insets = new Insets(0, 5, 5, 5);
    this.add(jsonPathEditor.getJsonPathEditorPanel(), jsonPathEditFieldConstraints);

    GridBagConstraints helpMessageConstraints = new GridBagConstraints();
    helpMessageConstraints.fill = GridBagConstraints.HORIZONTAL;
    helpMessageConstraints.gridx = 0;
    helpMessageConstraints.gridy = 2;
    helpMessageConstraints.gridwidth = GridBagConstraints.REMAINDER;
    helpMessageConstraints.weightx = 1.0;
    helpMessageConstraints.insets = new Insets(5, 5, 5, 5);
    this.add(new JLabel("Please press enter to save edited data in input fields."), helpMessageConstraints);
  }
}
