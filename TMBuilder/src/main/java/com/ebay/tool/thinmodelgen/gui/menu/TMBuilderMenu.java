package com.ebay.tool.thinmodelgen.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.gui.TMGuiConstants;
import com.ebay.tool.thinmodelgen.gui.file.recents.RecentFileManager;
import com.ebay.tool.thinmodelgen.gui.file.recents.RecentFileManagerObserver;
import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockExport;
import com.ebay.tool.thinmodelgen.gui.menu.export.ExportConstants;
import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelExport;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.FileModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.FileOperationHandler;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.TMFileSingleton;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;
import com.ebay.tool.thinmodelgen.utility.MethodNameChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class TMBuilderMenu extends JMenuBar implements ActionListener, RecentFileManagerObserver {

  private static final String NEW = "New";
  private static final String OPEN = "Open";
  private static final String SAVE_AS = "Save As";
  private static final String SAVE = "Save";
  private static final String EXPORT_TM_CHECKS_TO = "Export TM Checks to...";
  private static final String EXPORT_DEVELOPER_MOCKS_TO = "Export Developer Mocks to...";
  private static String EXPORT_FILE = "No export path specified...";

  private static final String DEFAULT_VALIDATION_SET = "Core Validation";
  private static final String DEFAULT_VALIDATION_KEY = ExportConstants.CORE_VALIDATION_SET;
  private static final String NEW_VALIDATION_SET = "New...";

  private static final String SELECT_VALIDATION_SET = "Select";
  private static final String REVIEW_VALIDATION_SET_CHECK_BUTTON = "Review Check";
  private static final String REVIEW_VALIDATION_SET_MOCK_BUTTON = "Review Mock";
  private static final String DISCARD_VALIDATION_CHANGES = "Discard Changes";
  private static final String REVIEW_VALIDATION_SET = "Review Set";
  private static final String EDIT_VALIDATION_SET = "Edit Name";
  private static final String DELETE_VALIDATION_SET = "Delete Set";
  private static final String VALIDATION_MENU_TITLE = "Validation Sets";

  private static final String INVALID_SET_NAME_MESSAGE = "Validation set name must be unique and must be a valid method name (no spaces / numbers / special characters).";

  private File currentTmbFile = null;
  private File currentSchemaFile = null;
  private File currentExportFile = null;

  private File currentDeveloperMockExportFile = null;

  private JMenuItem tmCheckExportFilePath = null;

  private JMenuItem developerMockExportFilePath = null;

  private JMenu validationMenu;
  private LinkedHashMap<String, NodeModel[]> validationSetCache = new LinkedHashMap<>();
  private String currentValidationSet;

  private FileOperationHandler fileOperationHandler;

  public TMBuilderMenu(FileOperationHandler fileOperationHandler) {
    super();

    this.fileOperationHandler = fileOperationHandler;

    JMenu fileMenu = new JMenu("File");
    this.add(fileMenu);

    JMenuItem newFile = new JMenuItem(NEW);
    newFile.addActionListener(this);
    fileMenu.add(newFile);

    JMenuItem openFile = new JMenuItem(OPEN);
    openFile.addActionListener(this);
    fileMenu.add(openFile);

    JMenuItem saveFileAs = new JMenuItem(SAVE_AS);
    saveFileAs.addActionListener(this);
    fileMenu.add(saveFileAs);

    JMenuItem saveFile = new JMenuItem(SAVE);
    saveFile.addActionListener(this);
    fileMenu.add(saveFile);

    // Recent files

    fileMenu.add(RecentFileManager.getInstance().getRecentFilesMenu());
    RecentFileManager.getInstance().addObserver(this);

    // Export TM Check Menu

    JMenu exportTmChecksMenu = new JMenu("Export TM Checks");
    fileMenu.add(exportTmChecksMenu);

    JMenuItem exportTo = new JMenuItem(EXPORT_TM_CHECKS_TO);
    exportTo.addActionListener(this);
    exportTmChecksMenu.add(exportTo);

    tmCheckExportFilePath = new JMenuItem(EXPORT_FILE);
    tmCheckExportFilePath.addActionListener(this);
    exportTmChecksMenu.add(tmCheckExportFilePath);

    // Export Developer Mocks Menu
    JMenu exportDeveloperMocksMenu = new JMenu("Export Developer Mocks");
    fileMenu.add(exportDeveloperMocksMenu);

    JMenuItem exportDeveloperMocksTo = new JMenuItem(EXPORT_DEVELOPER_MOCKS_TO);
    exportDeveloperMocksTo.addActionListener(this);
    exportDeveloperMocksMenu.add(exportDeveloperMocksTo);

    developerMockExportFilePath = new JMenuItem(EXPORT_FILE);
    developerMockExportFilePath.addActionListener(this);
    exportDeveloperMocksMenu.add(developerMockExportFilePath);

    // Validation Set Menu
    validationMenu = new JMenu(VALIDATION_MENU_TITLE);
    this.add(validationMenu);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    String actionCommand = e.getActionCommand();

    switch (actionCommand) {
      case NEW:
        if (confirmCreateNew() && doLoadSchemaFile()) {
          currentTmbFile = null;
          currentExportFile = null;
          currentDeveloperMockExportFile = null;
          resetValidationSetCache();
          currentValidationSet = DEFAULT_VALIDATION_SET;
          setValidationSetNameAsMenuTitle(DEFAULT_VALIDATION_SET);

          try {
            addCurrentValidationSetToCache();
          } catch (Exception i) {
            i.printStackTrace();
          }

          updateExportFilePath(null);
          setDefaultValidationSetMenuItems();
        }
        break;
      case OPEN:
        doOpenFile();
        break;
      case SAVE_AS:
        doSaveAs();
        break;
      case SAVE:
        doSave();
        break;
      case EXPORT_TM_CHECKS_TO:
        doExportTo();
        break;
      case EXPORT_DEVELOPER_MOCKS_TO:
        doExportDeveloperMocksTo();
        break;
      case NEW_VALIDATION_SET:
        doNewValidationSet();
        break;
      case SELECT_VALIDATION_SET:
        try {
          addCurrentValidationSetToCache();
        } catch (Exception j) {
          j.printStackTrace();
        }

        doLoadValidationSetFromCache(getValidationMenuInvokerText(e));
        break;
      case REVIEW_VALIDATION_SET:
        doReviewValidationSet(getValidationMenuInvokerText(e));
        break;
      case REVIEW_VALIDATION_SET_CHECK_BUTTON:
        doReviewValidationSet(currentValidationSet);
        break;
      case REVIEW_VALIDATION_SET_MOCK_BUTTON:
        doReviewMock(currentValidationSet);
        break;
      case DISCARD_VALIDATION_CHANGES:
        doDiscardChanges();
        break;
      case EDIT_VALIDATION_SET:
        doEditValidationSet(getValidationMenuInvokerText(e));
        break;
      case DELETE_VALIDATION_SET:
        doDeleteValidationSet(getValidationMenuInvokerText(e));
        break;
      default:
        // Default is to handle the export to TM checks or developer mocks.
        // Inspect the source JMenuItem to determine which menu item was selected.
        if (e.getSource() == tmCheckExportFilePath) {
          doExportToStoredFile();
        } else if (e.getSource() == developerMockExportFilePath) {
          doDeveloperMockExportToStoredFile();
        }
        break;
    }

    setFileNameInAppWindow();
  }

  // ================================================================================
  // RecentFileManagerObserver - handle messages
  // ================================================================================

  @Override
  public void openRecentFile(String filePath) {
    currentTmbFile = new File(filePath);
    loadCurrentTmbFile();
    setFileNameInAppWindow();
  }

  // ================================================================================
  // region Validation Sets
  // ================================================================================

  private String getValidationMenuInvokerText(ActionEvent e) {
    JMenuItem menuItem = (JMenuItem) e.getSource();
    JPopupMenu parentPopupMenu = (JPopupMenu) menuItem.getParent();
    JMenu parentMenu = (JMenu) parentPopupMenu.getInvoker();
    return parentMenu.getText();
  }

  private void setDefaultValidationSetMenuItems() {
    validationMenu.removeAll();

    // Add new button
    JMenuItem newItemMenuItem = new JMenuItem(NEW_VALIDATION_SET);
    newItemMenuItem.addActionListener(this);
    validationMenu.add(newItemMenuItem);

    // Discard Changes
    JMenuItem discardMenuItem = new JMenuItem(DISCARD_VALIDATION_CHANGES);
    discardMenuItem.addActionListener(this);
    validationMenu.add(discardMenuItem);

    // Review checks
    JMenuItem reviewChecksItem = new JMenuItem(REVIEW_VALIDATION_SET_CHECK_BUTTON);
    reviewChecksItem.addActionListener(this);
    validationMenu.add(reviewChecksItem);

    // Review mocks
    JMenuItem reviewMocksButton = new JMenuItem(REVIEW_VALIDATION_SET_MOCK_BUTTON);
    reviewMocksButton.addActionListener(this);
    validationMenu.add(reviewMocksButton);

    addValidationSetMenuItem(DEFAULT_VALIDATION_SET);
  }

  private void addValidationSetMenuItem(String validationSetName) {
    JMenu validationMenuItem = new JMenu(validationSetName);
    validationMenuItem.addActionListener(this);
    validationMenu.add(validationMenuItem);

    // Select Button
    JMenuItem selectMenuItem = new JMenuItem(SELECT_VALIDATION_SET);
    selectMenuItem.addActionListener(this);
    validationMenuItem.add(selectMenuItem);

    // Review Button
    JMenuItem reviewMenuItem = new JMenuItem(REVIEW_VALIDATION_SET);
    reviewMenuItem.addActionListener(this);
    validationMenuItem.add(reviewMenuItem);

    if (!validationSetName.equals(DEFAULT_VALIDATION_SET)) {
      // Edit Button
      JMenuItem editMenuItem = new JMenuItem(EDIT_VALIDATION_SET);
      editMenuItem.addActionListener(this);
      validationMenuItem.add(editMenuItem);

      // Delete Button
      JMenuItem deleteMenuItem = new JMenuItem(DELETE_VALIDATION_SET);
      deleteMenuItem.addActionListener(this);
      validationMenuItem.add(deleteMenuItem);
    }
  }

  private void resetValidationSetCache() {
    validationSetCache = new LinkedHashMap<>();
  }

  private List<ValidationSetModel> getValidationSetCacheAsModel() {

    List<ValidationSetModel> validationSetModels = new ArrayList<>();
    for (Entry<String, NodeModel[]> entry : validationSetCache.entrySet()) {
      validationSetModels.add(new ValidationSetModel(entry.getKey(), entry.getValue()));
    }

    return validationSetModels;
  }

  private boolean validationSetCacheContainsSetWithName(String validationSetName) {
    String converted = getConvertedValidationSetName(validationSetName);
    return new ArrayList<>(validationSetCache.keySet()).contains(converted);
  }

  private void doNewValidationSet() {
    String name = JOptionPane.showInputDialog(MainWindow.getInstance(), "Enter new validation set name:");

    if (name != null) {
      if (MethodNameChecker.isValueValidMethodName(name) && !validationSetCacheContainsSetWithName(name)) {
        try {
          addCurrentValidationSetToCache();
          addValidationSetMenuItem(name);

          // Load new tree
          doReloadSchemaTree();

          setValidationSetNameAsMenuTitle(name);
          currentValidationSet = name;

          // Add the newly created set to cache
          addCurrentValidationSetToCache();
        } catch (Exception e) {
          e.printStackTrace();
          displayErrorDialog(String.format("An error occurred when creating a new validation set. \nERROR: %s", e.getMessage()), "Create Validation Set Error");
        }
      } else {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), INVALID_SET_NAME_MESSAGE, "Error", JOptionPane.ERROR_MESSAGE);
        doNewValidationSet();
      }
    }

  }

  private void doEditValidationSet(String validationSetName) {
    String editedSetName = JOptionPane.showInputDialog(MainWindow.getInstance(), "Edit validation set name:", validationSetName);

    if (editedSetName != null) {
      if (MethodNameChecker.isValueValidMethodName(editedSetName) && !validationSetCacheContainsSetWithName(editedSetName)) {
        replaceValidationSet(validationSetName, editedSetName);
      } else {
        displayErrorDialog(INVALID_SET_NAME_MESSAGE, "Error");
        doEditValidationSet(validationSetName);
      }
    }
  }

  private void doReviewValidationSet(String validationSetName) {
    try {
      LinkedHashMap<String, NodeModel[]> tempCache = getTemporaryValidationSetCache();

      if (tempCache == null) {
        return;
      }

      ValidationSetModel setModel = null;
      for (Entry<String, NodeModel[]> entry : tempCache.entrySet()) {
        if (entry.getKey().equals(getConvertedValidationSetName(validationSetName))) {
          setModel = new ValidationSetModel(entry.getKey(), entry.getValue());
          break;
        }
      }

      String message = new ThinModelExport().getValidationStatementsForValidationSet(setModel);
      message = message.replaceAll("\t", "").trim();

      JTextArea textArea = new JTextArea(25, 125);
      textArea.setText(message);
      textArea.setEditable(false);

      JScrollPane scrollPane = new JScrollPane(textArea);
      JOptionPane.showMessageDialog(MainWindow.getInstance(), scrollPane, String.format("Review Validation Set Output - %s", validationSetName), JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
      e.printStackTrace();
      displayErrorDialog(String.format("An error occurred when reviewing validation set: %s \nERROR: %s", validationSetName, e.getMessage()), "Review Validation Set Error");
    }
  }

  private void doReviewMock(String validationSetName) {
    try {
      LinkedHashMap<String, NodeModel[]> tempCache = getTemporaryValidationSetCache();

      if (tempCache == null) {
        return;
      }

      ValidationSetModel coreValidationSet = null;
      for (Entry<String, NodeModel[]> entry : tempCache.entrySet()) {
        if (entry.getKey().equals(ExportConstants.CORE_VALIDATION_SET)) {
          coreValidationSet = new ValidationSetModel(entry.getKey(), entry.getValue());
          break;
        }
      }

      // Find the validation set to show
      ValidationSetModel setModel = null;
      for (Entry<String, NodeModel[]> entry : tempCache.entrySet()) {
        if (entry.getKey().equals(getConvertedValidationSetName(validationSetName))) {
          setModel = new ValidationSetModel(entry.getKey(), entry.getValue());
          break;
        }
      }

      String json = new DeveloperMockExport().getMockForValidationSet(coreValidationSet, setModel);

      JTextArea textArea = new JTextArea(25, 125);
      textArea.setText(json);
      textArea.setEditable(false);

      JScrollPane scrollPane = new JScrollPane(textArea);
      JOptionPane.showMessageDialog(MainWindow.getInstance(), scrollPane, String.format("Review Validation Set Output - %s", validationSetName), JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
      e.printStackTrace();
      displayErrorDialog(String.format("An error occurred when reviewing validation set: %s \nERROR: %s", validationSetName, e.getMessage()), "Review Validation Set Error");
    }
  }

  private void doDiscardChanges() {
    try {
      if (currentValidationSetHasChanges() && confirmDiscardChanges()) {
        if (validationSetCacheContainsSetWithName(currentValidationSet)) {
          doLoadValidationSetFromCache(currentValidationSet);
        } else {
          doReloadSchemaTree();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      displayErrorDialog(String.format("An error occurred when checking for changes. \nERROR: %s", e.getMessage()), "Error");
    }
  }

  private void doDeleteValidationSet(String validationSetName) {
    try {
      if (confirmDeleteValidationSet(validationSetName)) {

        if (currentValidationSet.equals(validationSetName)) {
          doLoadValidationSetFromCache(DEFAULT_VALIDATION_SET);
        }

        // Add one to ignore 'New...' menu item
        validationMenu.getPopupMenu().remove(findIndexOfValidationSet(validationSetName) + 1);
        validationSetCache.remove(validationSetName);
      }

    } catch (Exception e) {
      e.printStackTrace();
      displayErrorDialog(String.format("An error occurred when deleting validation set with name: %s. \nERROR: %s", validationSetName, e.getMessage()), "Error");
    }
  }

  private void doLoadValidationSetFromCache(String validationSetName) {
    try {
      String converted = getConvertedValidationSetName(validationSetName);

      for (ValidationSetModel model : getValidationSetCacheAsModel()) {
        if (model.getValidationSetName().equals(getConvertedValidationSetName(converted))) {
          try {
        	  fileOperationHandler.loadSchema(currentSchemaFile.getCanonicalPath(), model.getData());
          } catch (IllegalStateException e) {
        	  e.printStackTrace();
        	  displayErrorDialog(String.format("Unable to load schema from path:\n%s.\n\nEdit your TMB file's schema path to point to the correct schema.", currentSchemaFile.getCanonicalPath()), "Load Validation Set");
        	  return;
          }
          setValidationSetNameAsMenuTitle(validationSetName);
          currentValidationSet = validationSetName;
          break;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      displayErrorDialog(String.format("An error occurred when loading validation set: %s \nERROR: %s", validationSetName, e.getMessage()), "Load Validation Set Error");
    }
  }

  private String getConvertedCurrentValidationSetName() {
    return getConvertedValidationSetName(currentValidationSet);
  }

  private String getConvertedValidationSetName(String validationSetName) {
    if (validationSetName == null) {
      return null;
    }
    return validationSetName.equals(DEFAULT_VALIDATION_SET) ? DEFAULT_VALIDATION_KEY : validationSetName;
  }

  private boolean currentValidationSetHasChanges() throws IOException {
    String currentValidationSet = getConvertedCurrentValidationSetName();

    if (currentValidationSet == null) {
      return false;
    }

    // Create temporary model with current changes
    NodeModel[] currentTempData = fileOperationHandler.getJsonPathCheckData().toArray(new NodeModel[0]);
    ValidationSetModel currentTempModel = new ValidationSetModel(currentValidationSet, currentTempData);

    // Set default data to a new node model in case the current set is not yet
    // saved to file
    ValidationSetModel cachedValidationSetModel = new ValidationSetModel(currentValidationSet, new NodeModel[0]);

    // Loop through cache to find existing set
    for (ValidationSetModel model : getValidationSetCacheAsModel()) {
      if (model.getValidationSetName().equals(currentValidationSet)) {
        cachedValidationSetModel = model;
        break;
      }
    }

    return !cachedValidationSetModel.equals(currentTempModel);
  }

  private FileModel getCurrentTmbFileModel() throws FileNotFoundException {
    FileReader fileReader = new FileReader(currentTmbFile);
    Gson gson = new Gson();
    FileModel model = gson.fromJson(fileReader, FileModel.class);
    TMFileSingleton.getInstance().setFileModel(model);
    return model;
  }

  private void doReloadSchemaTree() throws IOException, ClassNotFoundException {
    fileOperationHandler.loadSchema(currentSchemaFile.getCanonicalPath(), null);
  }

  private int findIndexOfValidationSet(String validationSetName) {
    Iterator<Entry<String, NodeModel[]>> itr = validationSetCache.entrySet().iterator();

    int index = 0;
    while (itr.hasNext()) {
      if (itr.next().getKey().equals(validationSetName)) {
        break;
      }
      index++;
    }

    return index;
  }

  private void replaceValidationSet(String validationSetName, String replacementName) {
    List<String> keySet = new ArrayList<>(validationSetCache.keySet());
    List<NodeModel[]> valueSet = new ArrayList<>(validationSetCache.values());
    int replaceIndex = findIndexOfValidationSet(validationSetName);
    resetValidationSetCache();

    // Add one to ignore 'New...' menu item
    JMenuItem validationMenuItemToBeReplaced = validationMenu.getItem(replaceIndex + 1);
    String previousName = validationMenuItemToBeReplaced.getText();
    validationMenuItemToBeReplaced.setText(replacementName);

    if (currentValidationSet.equals(previousName)) {
      setValidationSetNameAsMenuTitle(replacementName);
      currentValidationSet = replacementName;
    }

    for (int i = 0; i < keySet.size(); i++) {
      if (i == replaceIndex) {
        validationSetCache.put(replacementName, valueSet.get(i));
      } else {
        validationSetCache.put(keySet.get(i), valueSet.get(i));
      }
    }
  }

  private void setValidationSetNameAsMenuTitle(String validationSetName) {
    validationMenu.setText(String.format("%s (%s)", VALIDATION_MENU_TITLE, validationSetName));
  }

  private void addCurrentValidationSetToCache() throws IOException {
    if (currentValidationSet != null) {
      List<NodeModel> checkData = fileOperationHandler.getJsonPathCheckData();
      NodeModel[] nodeData = checkData.toArray(new NodeModel[0]);
      addNodeDataToCacheWithSetName(getConvertedCurrentValidationSetName(), nodeData);
    }
  }

  private void addNodeDataToCacheWithSetName(String validationSetName, NodeModel[] data) {
    validationSetCache.put(validationSetName, data);
  }

  private LinkedHashMap<String, NodeModel[]> getTemporaryValidationSetCache() throws IOException {
    if (currentValidationSet != null) {
      LinkedHashMap<String, NodeModel[]> tempCache = validationSetCache;

      // Add current set
      List<NodeModel> checkData = fileOperationHandler.getJsonPathCheckData();
      NodeModel[] value = checkData.toArray(new NodeModel[0]);

      tempCache.put(getConvertedCurrentValidationSetName(), value);
      return tempCache;
    }

    return null;
  }

  private boolean confirmCreateNew() {
    if (currentSchemaFile == null) {
      return true;
    }

    return displayConfirmationDialog("Are you sure you want to start a new file?\nAll unsaved work will be lost.");
  }

  private boolean confirmDeleteValidationSet(String validationSetName) {
    return displayConfirmationDialog(String.format("Are you sure you want to delete the %s validation set?", validationSetName));
  }

  private boolean confirmDiscardChanges() {
    return displayConfirmationDialog("Are you sure you want to discard your current validation set changes?");
  }

  // endregion

  private boolean displayConfirmationDialog(String message) {
    String[] options = new String[] { "Yes", "No" };
    int response = JOptionPane.showOptionDialog(MainWindow.getInstance(), message, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

    return response == JOptionPane.YES_OPTION;
  }

  private void displayErrorDialog(String message, String title) {
    JOptionPane.showMessageDialog(MainWindow.getInstance(), message, title, JOptionPane.ERROR_MESSAGE);
  }

  private boolean doLoadSchemaFile() {

    JFileChooser fc = new JFileChooser();
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(new SchemaFileFilter());

    int returnVal = fc.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      currentSchemaFile = fc.getSelectedFile();
    } else {
      return false;
    }

    String schemaFileName = "[undefined]";

    boolean status = true;
    try {
      TMFileSingleton.getInstance().setPayload(null);
      schemaFileName = currentSchemaFile.getCanonicalPath();
      fileOperationHandler.loadSchema(schemaFileName, null);
      RecentFileManager.getInstance().addRecentFile(schemaFileName);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
      displayErrorDialog(String.format("Error parsing file:\n%s\n\n%s", schemaFileName, e.getMessage()), "Load JSON Schema Error");
      status = false;
    }

    return status;
  }

  private void doSaveAs() {

    JFileChooser fc = new JFileChooser();
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(new TmbFileFilter());

    int returnVal = fc.showSaveDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      currentTmbFile = fc.getSelectedFile();

      String fileName = currentTmbFile.getName();
      if (!fileName.endsWith(".tmb")) {

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
          fileName = fileName.substring(0, dotIndex);
        }

        String filePath = currentTmbFile.getParent();
        filePath = filePath + File.separator + fileName + ".tmb";
        currentTmbFile = new File(filePath);
      }
    } else {
      return;
    }

    String tmbFilePath = "";

    try {
      tmbFilePath = currentTmbFile.getCanonicalPath();
      writeCurrentTmbFile();
    } catch (IOException e) {
      e.printStackTrace();

      JOptionPane.showMessageDialog(MainWindow.getInstance(), String.format("Error writing file:\n%s\n\n%s", tmbFilePath, e.getMessage()), "SaveAs TMB File Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doSave() {

    if (currentTmbFile == null) {
      doSaveAs();
      return;
    }

    String tmbFilePath = "";

    try {
      tmbFilePath = currentTmbFile.getCanonicalPath();
      writeCurrentTmbFile();
    } catch (IOException e) {
      e.printStackTrace();

      JOptionPane.showMessageDialog(MainWindow.getInstance(), String.format("Error writing file:\n%s\n\n%s", tmbFilePath, e.getMessage()), "Save TMB File Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doOpenFile() {

    JFileChooser fc = new JFileChooser();
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(new TmbFileFilter());

    int returnVal = fc.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      currentTmbFile = fc.getSelectedFile();
    }

    loadCurrentTmbFile();
  }

  private void loadCurrentTmbFile() {

    String tmbFilePath = "";

    if (currentTmbFile == null) {
      return;
    }

    try {
      tmbFilePath = currentTmbFile.getCanonicalPath();
      readCurrentTmbFile();
      RecentFileManager.getInstance().addRecentFile(tmbFilePath);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();

      JOptionPane.showMessageDialog(MainWindow.getInstance(), String.format("Error opening file:\n%s\n\n%s", tmbFilePath, e.getMessage()), "Open TMB File Error", JOptionPane.ERROR_MESSAGE);
    } catch (UnsupportedOperationException e) {
      // This is thrown when the user decides to halt loading due to a reported
      // issue.
      // No need to throw another prompt.
    }
  }

  private void doExportTo() {

    doSave();

    JFileChooser fc = new JFileChooser();
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(new JavaFileFilter());

    int returnVal = fc.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      updateExportFilePath(fc.getSelectedFile().getPath());
    } else {
      return;
    }

    writeExportToFile();
  }

  private void doExportDeveloperMocksTo() {

    doSave();

    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(new JavaFileFilter());

    int returnVal = fc.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      updateExportDeveloperMocksFilePath(fc.getSelectedFile().getPath());
    } else {
      return;
    }

    writeDeveloperMocksToFile();
  }

  private void doExportToStoredFile() {

    if (currentExportFile == null) {
      doExportTo();
    } else {
      doSave();
      writeExportToFile();
    }
  }

  private void doDeveloperMockExportToStoredFile() {

    if (currentDeveloperMockExportFile == null) {
      doExportDeveloperMocksTo();
    } else {
      doSave();
      writeDeveloperMocksToFile();
    }
  }

  private void writeExportToFile() {

    ThinModelExport thinModelExport = new ThinModelExport();
    String tmbFilePath = "";

    try {
      tmbFilePath = currentExportFile.getCanonicalPath();
      thinModelExport.export(currentExportFile, getValidationSetCacheAsModel());
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();

      JOptionPane
          .showMessageDialog(
              MainWindow.getInstance(),
              String.format("Error exporting thin model validations:\n%s\n\n%s", tmbFilePath, e.getMessage()),
              "Export Thin Model Validation File Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void writeDeveloperMocksToFile() {
    DeveloperMockExport developerMockExport = new DeveloperMockExport();

    String developerMockOutputFolder = "";

    try {
      developerMockOutputFolder = currentDeveloperMockExportFile.getCanonicalPath();
      developerMockExport.export(currentDeveloperMockExportFile, getValidationSetCacheAsModel());
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();

      JOptionPane
              .showMessageDialog(
                      MainWindow.getInstance(),
                      String.format("Error exporting developer mocks:\n%s\n\n%s", developerMockOutputFolder, e.getMessage()),
                      "Export Developer Mocks File Error",
                      JOptionPane.ERROR_MESSAGE);
    }
  }

  private void writeCurrentTmbFile() throws IOException {
    addCurrentValidationSetToCache();

    String currentSchemaPath = null;
    if (currentSchemaFile != null) {
      String relativeSchemaPath = convertPathToRelativePath(currentTmbFile.getCanonicalPath(), currentSchemaFile.getCanonicalPath());
      currentSchemaPath = relativeSchemaPath;
    }

    String currentExportPath = null;
    if (currentExportFile != null) {
      String relativeExportPath = convertPathToRelativePath(currentTmbFile.getCanonicalPath(), currentExportFile.getCanonicalPath());
      currentExportPath = relativeExportPath;
    }

    String currentMockPath = null;
    if (currentDeveloperMockExportFile != null) {
      String relativeMockPath = convertPathToRelativePath(currentTmbFile.getCanonicalPath(), currentDeveloperMockExportFile.getCanonicalPath());
      currentMockPath = relativeMockPath;
    }

    int setModelLength = validationSetCache == null ? 0 : validationSetCache.size();
    ValidationSetModel[] validationSets = new ValidationSetModel[setModelLength];

    int i = 0;
    if (validationSetCache != null && validationSetCache.entrySet().size() > 0) {
      for (Entry<String, NodeModel[]> entry : validationSetCache.entrySet()) {
        String key = entry.getKey();
        NodeModel[] data = entry.getValue();

        validationSets[i] = new ValidationSetModel(key, data);
        i++;
      }
    }

    // TODO: refactor this class - reduce number of methods (better encapsulate
    // functionality) and standardize model loading, usage and writing.
    SchemaParserPayload payload = TMFileSingleton.getInstance().getPayload();
    FileModel fileModel = new FileModel(TMGuiConstants.APP_NAME, currentSchemaPath, validationSets, currentExportPath, currentMockPath, payload);
    TMFileSingleton.getInstance().setFileModel(fileModel);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String serializedModel = gson.toJson(fileModel);

    FileWriter fileWriter = new FileWriter(currentTmbFile);
    fileWriter.write(serializedModel);
    fileWriter.close();

    RecentFileManager.getInstance().addRecentFile(currentTmbFile.getCanonicalPath());
  }

  private void readCurrentTmbFile() throws IOException, ClassNotFoundException {

    FileModel fileModel = getCurrentTmbFileModel();

    if (fileModel.getSourceSchemaFilePath() != null) {
      String schemaFilePath = resolveRelativePath(currentTmbFile.getCanonicalPath(), fileModel.getSourceSchemaFilePath());
      currentSchemaFile = new File(schemaFilePath);
    } else {
      currentSchemaFile = null;
    }

    if (fileModel.getExportFilePath() != null) {
      String exportFilePath = resolveRelativePath(currentTmbFile.getCanonicalPath(), fileModel.getExportFilePath());
      updateExportFilePath(exportFilePath);
    } else {
      currentExportFile = null;
      updateExportFilePath(null);
    }

    resetValidationSetCache();
    setDefaultValidationSetMenuItems();

    if (fileModel.getValidationSets() != null && fileModel.getValidationSets().length > 0) {
      for (ValidationSetModel model : fileModel.getValidationSets()) {
        if (!model.getValidationSetName().equals(DEFAULT_VALIDATION_KEY)) {
          addValidationSetMenuItem(model.getValidationSetName());
        }

        validationSetCache.put(model.getValidationSetName(), model.getData());
      }
      doLoadValidationSetFromCache(DEFAULT_VALIDATION_SET);
    } else {
      // Load legacy files
      currentValidationSet = DEFAULT_VALIDATION_SET;
      addNodeDataToCacheWithSetName(getConvertedCurrentValidationSetName(), fileModel.getData());
      doLoadValidationSetFromCache(DEFAULT_VALIDATION_SET);
    }
  }

  protected String convertPathToRelativePath(String tmbFilePath, String otherFilePath) {

    Path filePath = Paths.get(otherFilePath);
    Path tmbPath = Paths.get(tmbFilePath);
    
    if (new File(tmbFilePath).isFile()) {
    	tmbPath = tmbPath.getParent();
    }
    
    
    Path relativePath = tmbPath.relativize(filePath);
    String relative = relativePath.toString();
    System.out.printf("Convert path [%s] to relative path [%s].%n", otherFilePath, relative);
    return relative;
  }

  protected String resolveRelativePath(String tmbFilePath, String path) {

    File tmbFile = new File(tmbFilePath);
    if (tmbFile.isFile()) {
    	tmbFile = tmbFile.getParentFile();
    }
    
    File combinedPath = new File(tmbFile, path);
    
    if (!combinedPath.exists()) {
    	// TODO: This block is being kept to support existing tmb files.
    	// Plan to remove this in a future release when we expect all
    	// users to have up-to-date tmb files that use paths relative
    	// to the tmb file itself instead of the TMBuilder.
	    Path filePath = Paths.get(path);
	    combinedPath = filePath.toFile();
	    System.out.printf("Resolve path [%s] as [%s].%n", path, combinedPath);
    }
    
    System.out.printf("Resolve path [%s] as [%s].%n", path, combinedPath);
    return combinedPath.toString();
  }

  private void updateExportFilePath(String filePath) {

    if (filePath == null) {
      tmCheckExportFilePath.setText(EXPORT_FILE);
    } else {
      currentExportFile = new File(filePath);
      tmCheckExportFilePath.setText(currentExportFile.getAbsolutePath());
    }
  }

  private void updateExportDeveloperMocksFilePath(String filePath) {

    if (filePath == null) {
      developerMockExportFilePath.setText(EXPORT_FILE);
    } else {
      currentDeveloperMockExportFile = new File(filePath);
      developerMockExportFilePath.setText(currentDeveloperMockExportFile.getAbsolutePath());
    }
  }

  private void setFileNameInAppWindow() {

    if (currentTmbFile == null) {
      MainWindow.getInstance().setOpenFilePath(null);
    } else {
      try {
        MainWindow.getInstance().setOpenFilePath(currentTmbFile.getCanonicalPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  class TmbFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

      String fileName = f.getName();
      int dotIndex = fileName.lastIndexOf(".");

      if (dotIndex < 0) {
        return false;
      }

      String extension = fileName.substring(dotIndex);

      return extension.equals(".tmb");
    }

    @Override
    public String getDescription() {
      return "TMBuilder File Filter (.tmb)";
    }

  }

  class SchemaFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

      String fileName = f.getName();
      int dotIndex = fileName.lastIndexOf(".");

      if (dotIndex < 0) {
        return false;
      }

      String extension = fileName.substring(dotIndex);
      return extension.equals(".json") || extension.equals(".zip") || extension.equals(".yaml") || extension.equals(".graphqls") || extension.equals(".graphql");
    }

    @Override
    public String getDescription() {
      return ".json / .zip / .yaml / .graphql(s)";
    }

  }

  class JavaFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

      String fileName = f.getName();
      int dotIndex = fileName.lastIndexOf(".");

      if (dotIndex < 0) {
        return false;
      }

      String extension = fileName.substring(dotIndex);

      return extension.equals(".java");
    }

    @Override
    public String getDescription() {
      return "Java (.java)";
    }

  }
}
