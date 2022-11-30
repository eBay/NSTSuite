package com.ebay.tool.thinmodelgen.gui.patheditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.gui.TMGuiConstants;
import com.ebay.tool.thinmodelgen.gui.patheditor.events.JsonPathEditorSelectionChangeListener;
import com.ebay.tool.thinmodelgen.gui.schema.JTreeRefresh;
import com.ebay.tool.thinmodelgen.gui.schema.events.SchemaEventListener;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType.JsonType;

public class JsonPathEditor implements SchemaEventListener, ActionListener, FocusListener {

  // This is returned to main UI.
  private JPanel editorControlPanel;

  private JComboBox<String> jsonPathComboBox;
  private JTextComponent comboBoxEditor;

  private String editorJsonPath = null;
  private int selectedComboBoxIndex = -1;
  private JsonBaseType[] selectedNodePath;

  private JTreeRefresh jTreeRefresh = null;

  private ArrayList<JsonPathEditorSelectionChangeListener> editorSelectionChangeListeners = new ArrayList<>();
  private List<String> unsupportedNodeTypes = Arrays.asList("anyOf", "oneOf", "array");
  
  public JsonPathEditor() {

    editorControlPanel = new JPanel(new GridBagLayout());

    // -----------------
    // Add button
    // -----------------

    JButton addButton = new JButton("+");
    addButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        if (selectedNodePath == null) {
          return;
        } else if (editorJsonPath == null) {
        	JOptionPane.showMessageDialog(MainWindow.getInstance(),
        		    "The selected node type does not support checks. Please select a different node.",
        		    "Warning",
        		    JOptionPane.WARNING_MESSAGE);
        	return;
        }

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jsonPathComboBox.getModel();
        if (model.getIndexOf(editorJsonPath) >= 0) {
          jsonPathComboBox.setSelectedItem(editorJsonPath);
        } else {
          model.addElement(editorJsonPath);
          jsonPathComboBox.setSelectedItem(editorJsonPath);
          saveJsonPathChanges();
        }
      }
    });

    GridBagConstraints addButtonConstraints = new GridBagConstraints();
    addButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
    addButtonConstraints.gridx = 0;
    addButtonConstraints.gridy = 0;
    editorControlPanel.add(addButton, addButtonConstraints);

    // ------------------
    // Remove button
    // ------------------

    JButton removeButton = new JButton("-");
    removeButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        int selectedIndex = jsonPathComboBox.getSelectedIndex();
        String selectedPath = (String) jsonPathComboBox.getSelectedItem();

        if (selectedPath.isEmpty()) {
          return;
        }

        if (!removeCurrentPathPrompt()) {
          return;
        }

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jsonPathComboBox.getModel();
        model.removeElement(selectedPath);
        selectedNodePath[selectedNodePath.length - 1].removePath(selectedPath);

        jsonPathComboBox.validate();
        jsonPathComboBox.repaint();

        if (selectedIndex > 0) {
          jsonPathComboBox.setSelectedIndex(selectedIndex - 1);
        } else if (model.getSize() == 0) {
          return;
        } else {
          jsonPathComboBox.setSelectedIndex(0);
        }
      }
    });

    GridBagConstraints removeButtonConstraints = new GridBagConstraints();
    removeButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
    removeButtonConstraints.gridx = 1;
    removeButtonConstraints.gridy = 0;
    editorControlPanel.add(removeButton, removeButtonConstraints);

    // ----------------------------
    // Add editor combo box
    // ----------------------------

    jsonPathComboBox = new JComboBox<>(new String[] { "" });
    jsonPathComboBox.setEditable(true);
    jsonPathComboBox.addActionListener(this);

    comboBoxEditor = (JTextComponent) jsonPathComboBox.getEditor().getEditorComponent();
    comboBoxEditor.addFocusListener(this);

    GridBagConstraints jsonPathEditFieldConstraints = new GridBagConstraints();
    jsonPathEditFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    jsonPathEditFieldConstraints.gridx = 2;
    jsonPathEditFieldConstraints.gridy = 0;
    jsonPathEditFieldConstraints.weightx = 0.5;
    jsonPathEditFieldConstraints.insets = new Insets(0, 5, 5, 5);
    editorControlPanel.add(jsonPathComboBox, jsonPathEditFieldConstraints);
  }

  public void setJTreeRefresh(JTreeRefresh jTreeRefresh) {
    this.jTreeRefresh = jTreeRefresh;
  }

  /**
   * Get the JSON path editor panel. This JPanel is setup in full-width
   * horizontal layout.
   *
   * @return JPanel to add to UI.
   */
  public JPanel getJsonPathEditorPanel() {
    return editorControlPanel;
  }

  /**
   * Add a selection change listener.
   *
   * @param listener
   *          Listener that would like to receive notifications.
   */
  public void addJsonPathEditorSelectionChangeListener(JsonPathEditorSelectionChangeListener listener) {
    this.editorSelectionChangeListeners.add(listener);
  }

  /**
   * Remove a selection change listener.
   *
   * @param listener
   *          Listener that would like to stop receiving notifications.
   */
  public void removeJsonPathEditorSelectionChangeListener(JsonPathEditorSelectionChangeListener listener) {
    this.editorSelectionChangeListeners.remove(listener);
  }

  // ---------------------------------------
  // SchemaEventListener
  // ---------------------------------------

  @Override
  public void treeNodeSelected(JsonBaseType[] path) {

    selectedNodePath = path;
    jsonPathComboBox.removeAllItems();

    if (path == null) {
      return;
	} else if (unsupportedNodeTypes.contains(path[path.length - 1].getNodeType())) {
		editorJsonPath = null;
		return;
	}

    editorJsonPath = "";
    for (int i = 0; i < path.length; i++) {

      JsonBaseType node = path[i];

      if (i > 1 && isNodePolymorphicAndShouldBeSkipped(node, selectedNodePath[i-1])) {
        continue;
      } else if (node == null || node.getSkipInJsonPath()) {
        continue;
      }

      if (!editorJsonPath.isEmpty()) {
        editorJsonPath += ".";
      }

      editorJsonPath += node.getJsonPathNodeName();
      if (node.getJsonType() == JsonType.ARRAY) {
        editorJsonPath += "[*]";
      }
    }

    String[] savedPaths = path[path.length - 1].getSavedPathsForNode();

    if (savedPaths != null && savedPaths.length > 0) {
      for (String savedPath : savedPaths) {
        jsonPathComboBox.addItem(savedPath);
      }
    }
  }

  // -----------------------------------
  // Focus Listener - Combo Box Text Editor
  // -----------------------------------

  @Override
  public void focusGained(FocusEvent e) {
    System.out.println("focus gained");
  }

  @Override
  public void focusLost(FocusEvent e) {
    System.out.println("focus lost");
  }

  // --------------------------------------
  // Action Listener - Combo Box
  // --------------------------------------

  @Override
  public void actionPerformed(ActionEvent e) {

    if ("comboBoxChanged".equals(e.getActionCommand())) {

      // If the selected index is >= 0 then there was a shift in the selection.
      // Otherwise, the selection hasn't changed from before.
      if (jsonPathComboBox.getSelectedIndex() >= 0) {
        selectedComboBoxIndex = jsonPathComboBox.getSelectedIndex();
        JsonBaseType jsonBaseType = null;
        if (selectedNodePath != null) {
          jsonBaseType = selectedNodePath[selectedNodePath.length - 1];
        }
        notifiyListenersOfEditorSelectionChange(jsonPathComboBox.getItemAt(selectedComboBoxIndex), jsonBaseType);
      } else {
        notifiyListenersOfEditorSelectionChange(null, null);
      }

    } else if ("comboBoxEdited".equals(e.getActionCommand())) {

      if (selectedNodePath == null) {
        System.out.println("ATTEMPTING TO EDIT COMBO BOX WITHOUT A SELECTED NODE!");
        return;
      }

      saveJsonPathChanges();
    }
  }

  // ------------------------------------
  // Protected methods
  // ------------------------------------

  /**
   * Clean the JSON path provided.
   *
   * 1) Contents between [] is to be ignored. 2) Functions: [min(), max(),
   * avg(), stddev(), length(), sum()] are to be ignored at the end of the path.
   *
   * @param jsonPath JSON path to clean.
   * @return Cleaned JSON path, or null if jsonPath is null.
   */
  protected String cleanJsonPath(String jsonPath) {

    if (jsonPath == null) {
      return null;
    }

    String[] functions = { ".min\\(\\)", ".max\\(\\)", ".avg\\(\\)", ".stddev\\(\\)", ".length\\(\\)", ".sum\\(\\)" };

    String cleanedPath = "";
    int squareBracketCounter = 0;

    for (int i = 0; i < jsonPath.length(); i++) {
      char letter = jsonPath.charAt(i);

      if (letter == '[') {
        squareBracketCounter++;
        continue;
      } else if (letter == ']') {
        squareBracketCounter--;
        continue;
      }

      if (squareBracketCounter < 0) {
        throw new IllegalStateException("More closing brackets were counted than opening ones. Indeterminate json path: " + jsonPath);
      }

      if (squareBracketCounter == 0) {
        cleanedPath += letter;
      }
    }

    if (squareBracketCounter > 0) {
      throw new IllegalStateException("More opening brackets were counted than closing ones. Indeterminate json path: " + jsonPath);
    }

    for (String function : functions) {
      cleanedPath = cleanedPath.replaceAll(function, "");
    }

    return cleanedPath;
  }

  // ---------------------------------------
  // Private methods
  // ---------------------------------------

  private boolean removeCurrentPathPrompt() {

    String[] options = new String[] { "Yes", "No" };
    int response = JOptionPane
        .showOptionDialog(MainWindow.getInstance(), "Are you sure you want to remove the current path?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

    if (response != JOptionPane.YES_OPTION) {
      return false;
    }

    return true;
  }

  private void showInvalidJsonPathPromptAndPutFocusBackOnEditPathField() {

    comboBoxEditor.requestFocusInWindow();

    JOptionPane
        .showMessageDialog(
            MainWindow.getInstance(),
            "Your JSON path is not valid for the currently selected node.\nThe path specified must lead to the currently selected node.\nPlease correct your JSON path.",
            "JSON path error",
            JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Save the JSON path changes made, if the path is valid. Otherwise, show a
   * message and keep focus on the edit field.
   */
  private void saveJsonPathChanges() {

    String updatedValue = (String) jsonPathComboBox.getSelectedItem();

    if (updatedValue == null) {
      return;
    }

    if (!validPath(updatedValue)) {
      showInvalidJsonPathPromptAndPutFocusBackOnEditPathField();
      return;
    }

    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jsonPathComboBox.getModel();

    String originalValue = model.getElementAt(selectedComboBoxIndex);

    model.removeElementAt(selectedComboBoxIndex);
    model.addElement(updatedValue);

    selectedComboBoxIndex = model.getIndexOf(updatedValue);
    jsonPathComboBox.setSelectedIndex(selectedComboBoxIndex);
    jsonPathComboBox.validate();
    jsonPathComboBox.repaint();

    selectedNodePath[selectedNodePath.length - 1].updatePath(originalValue, updatedValue);

    refreshTree();
    notifiyListenersOfEditorSelectionChange(updatedValue, selectedNodePath[selectedNodePath.length - 1]);
  }

  /**
   * Refresh the tree if we have a tree instance to refresh.
   */
  private void refreshTree() {
    if (jTreeRefresh != null) {
      jTreeRefresh.refreshTree();
    }
  }

  /**
   * Check if the provided JSON path is a valid path or not for the currently
   * selected node.
   *
   * @param jsonPath
   *          JSON path to evaluate.
   * @return True if path is valid. False otherwise.
   */
  private boolean validPath(String jsonPath) {

    try {
      jsonPath = cleanJsonPath(jsonPath);
    } catch (IllegalStateException e) {
      e.printStackTrace();
      return false;
    }

    String[] pathsteps = jsonPath.split("\\.");
    String[] selectedNodeSteps = getNodesInSelectedNodePath();

    if (pathsteps.length != selectedNodeSteps.length) {
      return false;
    }

    for (int i = 0; i < pathsteps.length; i++) {
      String pathStepValue = pathsteps[i];
      String selectedNodePathValue = selectedNodeSteps[i];

      if (selectedNodePathValue.equals(TMGuiConstants.DICTIONARY_KEY)) {
        continue;
      }

      if (!pathStepValue.equals(selectedNodePathValue)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Get the non polymorphic nodes in the currently selected node path.
   *
   * @return String name list of nodes.
   */
  private String[] getNodesInSelectedNodePath() {

    ArrayList<String> nodes = new ArrayList<>();
    boolean keyIsDictionaryKey = false;

    for (int i = 0; i < selectedNodePath.length; i++) {

      JsonBaseType node = selectedNodePath[i];

      if (i > 1 && isNodePolymorphicAndShouldBeSkipped(node, selectedNodePath[i-1])) {
        continue;
      } else if (node == null) {
        continue;
      } else if (node.getSkipInJsonPath()) {
        continue;
      } else if (node.getJsonType() == JsonType.DICTIONARY) {
        nodes.add(node.getJsonPathNodeName());
        keyIsDictionaryKey = true;
      } else if (keyIsDictionaryKey) {
        nodes.add(TMGuiConstants.DICTIONARY_KEY);
        keyIsDictionaryKey = false;
      } else {
        nodes.add(node.getJsonPathNodeName());
      }
    }

    return nodes.toArray(new String[0]);
  }

  /**
   * Check if the specified node is polymorphic and should be skipped. Considers
   * parent to the node in this evaluation.
   *
   * @param node
   *          Node to consier.
   * @param parent
   *          Node's parent.
   * @return True if it should be skipped, false otherwise.
   */
  private boolean isNodePolymorphicAndShouldBeSkipped(JsonBaseType node, JsonBaseType parent) {

    if (node.getJsonType() != JsonType.POLYMORPHIC) {
      return false;
    } else if (parent != null && parent.getJsonType() != JsonType.ARRAY) {
      return false;
    } else if (parent != null && !parent.getJsonPathNodeName().equals(node.getJsonPathNodeName())) {
      return false;
    }

    return true;
  }

  /**
   * Notify each listener of the selected JSON path.
   *
   * @param selectedJsonPath
   *          Selected JSON path, or null if none selected.
   * @param node
   *          Node that is selected in the tree.
   */
  private void notifiyListenersOfEditorSelectionChange(String selectedJsonPath, JsonBaseType node) {
    for (JsonPathEditorSelectionChangeListener listener : editorSelectionChangeListeners) {
      listener.jsonPathEditorSelectionChanged(selectedJsonPath, node);
    }
  }
}
