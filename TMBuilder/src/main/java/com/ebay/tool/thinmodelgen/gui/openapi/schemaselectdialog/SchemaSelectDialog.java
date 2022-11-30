package com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserRequestMethod;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

public class SchemaSelectDialog extends JDialog implements ListSelectionListener {

  private static final long serialVersionUID = 1L;

  private Paths paths;

  private JList<String> pathList;
  private JList<String> methodList;
  private JList<String> responseCodeList;
  private JList<String> mediaTypeList;
  private JButton okButton;

  private SchemaParserPayload payload;

  public SchemaSelectDialog(Paths paths, Frame parent) {
    super(parent, "OpenAPI Schema Selection", ModalityType.APPLICATION_MODAL);

    this.paths = paths;
    init();
  }

  /**
   * Show the dialog and get the payload values selected.
   *
   * @return Payload of selected values.
   */
  public SchemaParserPayload getPayload() {
    this.setVisible(true);
    return payload;
  }

  /**
   * Initialize the dialog.
   */
  private void init() {

    Dimension dialogDimension = new Dimension(800, 300);
    setSize(dialogDimension);
    setPreferredSize(dialogDimension);
    setLocationRelativeTo(MainWindow.getInstance());

    JPanel dialogPanel = new JPanel(new GridBagLayout());

    // ------------------------
    // PATH
    // ------------------------

    // Label
    JLabel pathLabel = new JLabel("Path");

    GridBagConstraints pathLabelConstraints = new GridBagConstraints();
    pathLabelConstraints.insets = new Insets(2, 2, 0, 2);
    pathLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
    pathLabelConstraints.gridx = 0;
    pathLabelConstraints.gridy = 0;
    pathLabelConstraints.weightx = 1.0;
    pathLabelConstraints.weighty = 0.1;

    dialogPanel.add(pathLabel, pathLabelConstraints);

    // selection
    GridBagConstraints pathSelectionConstraints = new GridBagConstraints();
    pathSelectionConstraints.insets = new Insets(0, 2, 2, 2);
    pathSelectionConstraints.fill = GridBagConstraints.BOTH;
    pathSelectionConstraints.gridx = 0;
    pathSelectionConstraints.gridy = 1;
    pathSelectionConstraints.weightx = 1.0;
    pathSelectionConstraints.weighty = 1.0;

    DefaultListModel<String> pathOptions = new DefaultListModel<>();
    Set<String> pathValues = paths.keySet();
    for (String pathValue : pathValues) {
      pathOptions.addElement(pathValue);
    }

    pathList = new JList<>();
    pathList.setModel(pathOptions);
    pathList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    pathList.addListSelectionListener(this);

    JScrollPane pathsScrollPane = new JScrollPane(pathList);

    dialogPanel.add(pathsScrollPane, pathSelectionConstraints);

    // ------------------------
    // METHOD
    // ------------------------

    // Label
    JLabel methodLabel = new JLabel("Request Method");

    GridBagConstraints methodLabelConstraints = new GridBagConstraints();
    methodLabelConstraints.insets = new Insets(2, 2, 0, 2);
    methodLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
    methodLabelConstraints.gridx = 1;
    methodLabelConstraints.gridy = 0;
    methodLabelConstraints.weightx = 0.25;
    methodLabelConstraints.weighty = 0.1;

    dialogPanel.add(methodLabel, methodLabelConstraints);

    // selection
    GridBagConstraints methodSelectionConstraints = new GridBagConstraints();
    methodSelectionConstraints.insets = new Insets(0, 2, 2, 2);
    methodSelectionConstraints.fill = GridBagConstraints.BOTH;
    methodSelectionConstraints.gridx = 1;
    methodSelectionConstraints.gridy = 1;
    methodSelectionConstraints.weightx = 0.25;
    methodSelectionConstraints.weighty = 1.0;

    methodList = new JList<>();
    methodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    methodList.addListSelectionListener(this);

    JScrollPane methodScrollPane = new JScrollPane(methodList);

    dialogPanel.add(methodScrollPane, methodSelectionConstraints);

    // ------------------------
    // Response Code
    // ------------------------

    // Label
    JLabel responseCodeLabel = new JLabel("Response Code");

    GridBagConstraints responseCodeLabelConstraints = new GridBagConstraints();
    responseCodeLabelConstraints.insets = new Insets(2, 0, 0, 2);
    responseCodeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
    responseCodeLabelConstraints.gridx = 2;
    responseCodeLabelConstraints.gridy = 0;
    responseCodeLabelConstraints.weightx = 0.25;
    responseCodeLabelConstraints.weighty = 0.1;

    dialogPanel.add(responseCodeLabel, responseCodeLabelConstraints);

    // selection
    GridBagConstraints responseCodeSelectionConstraints = new GridBagConstraints();
    responseCodeSelectionConstraints.insets = new Insets(0, 0, 2, 2);
    responseCodeSelectionConstraints.fill = GridBagConstraints.BOTH;
    responseCodeSelectionConstraints.gridx = 2;
    responseCodeSelectionConstraints.gridy = 1;
    responseCodeSelectionConstraints.weightx = 0.25;
    responseCodeSelectionConstraints.weighty = 1.0;

    responseCodeList = new JList<>();
    responseCodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    responseCodeList.addListSelectionListener(this);

    JScrollPane responseCodeScrollPane = new JScrollPane(responseCodeList);

    dialogPanel.add(responseCodeScrollPane, responseCodeSelectionConstraints);

    // ------------------------
    // Media Type
    // ------------------------

    // Label
    JLabel mediaTypeLabel = new JLabel("Media/Content Type");

    GridBagConstraints mediaTypeLabelConstraints = new GridBagConstraints();
    mediaTypeLabelConstraints.insets = new Insets(2, 0, 0, 2);
    mediaTypeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
    mediaTypeLabelConstraints.gridx = 3;
    mediaTypeLabelConstraints.gridy = 0;
    mediaTypeLabelConstraints.weightx = 0.25;
    mediaTypeLabelConstraints.weighty = 0.1;

    dialogPanel.add(mediaTypeLabel, mediaTypeLabelConstraints);

    // selection
    GridBagConstraints mediaTypeSelectionConstraints = new GridBagConstraints();
    mediaTypeSelectionConstraints.insets = new Insets(0, 0, 2, 2);
    mediaTypeSelectionConstraints.fill = GridBagConstraints.BOTH;
    mediaTypeSelectionConstraints.gridx = 3;
    mediaTypeSelectionConstraints.gridy = 1;
    mediaTypeSelectionConstraints.weightx = 0.25;
    mediaTypeSelectionConstraints.weighty = 1.0;

    mediaTypeList = new JList<>();
    mediaTypeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    mediaTypeList.addListSelectionListener(this);

    JScrollPane mediaTypeScrollPane = new JScrollPane(mediaTypeList);

    dialogPanel.add(mediaTypeScrollPane, mediaTypeSelectionConstraints);

    // Save Button
    GridBagConstraints okButtonPanelConstraints = new GridBagConstraints();
    okButtonPanelConstraints.insets = new Insets(0, 0, 2, 2);
    okButtonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
    okButtonPanelConstraints.anchor = GridBagConstraints.EAST;
    okButtonPanelConstraints.gridx = 3;
    okButtonPanelConstraints.gridy = 2;
//    okButtonPanelConstraints.gridwidth = 1;
//    okButtonPanelConstraints.weightx = 1.0;
//    okButtonPanelConstraints.weighty = 1.0;

    okButton = new JButton("Open");
    okButton.setEnabled(false);

    okButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        String path = pathList.getSelectedValue();
        String methodValue = methodList.getSelectedValue();
        SchemaParserRequestMethod method = SchemaParserRequestMethod.getEnumForValue(methodValue);
        String responseCode = responseCodeList.getSelectedValue();
        String contentType = mediaTypeList.getSelectedValue();

        payload = new SchemaParserPayload().setPath(path).setMethod(method).setResponseCode(responseCode).setContentType(contentType);

        setVisible(false);
      }
    });

    dialogPanel.add(okButton, okButtonPanelConstraints);

    // Wrap it all up
    getContentPane().add(dialogPanel);
    pack();
  }

  // ------------------------------------------------
  // ListSelectionListener
  // ------------------------------------------------

  @Override
  public void valueChanged(ListSelectionEvent e) {

    Object source = e.getSource();
    if (source == pathList) {

      String selectedPath = pathList.getSelectedValue();
      pathList.setSelectedValue(selectedPath, true);
      PathItem pathItem = paths.get(selectedPath);

      DefaultListModel<String> methodModel = new DefaultListModel<>();
      if (pathItem.getGet() != null) {
        methodModel.addElement("GET");
      }

      if (pathItem.getPut() != null) {
        methodModel.addElement("PUT");
      }

      if (pathItem.getPost() != null) {
        methodModel.addElement("POST");
      }

      if (pathItem.getDelete() != null) {
        methodModel.addElement("DELETE");
      }

      setMethodList(methodModel);
      setResponseCodeList(new DefaultListModel<String>());
      setMediaTypeList(new DefaultListModel<String>());

    } else if (source == methodList) {

      String selectedPath = pathList.getSelectedValue();
      String selectedMethod = methodList.getSelectedValue();
      methodList.setSelectedValue(selectedMethod, true);

      PathItem pathItem = paths.get(selectedPath);
      Operation operation = null;

      if ("GET".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getGet();
      } else if ("PUT".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getPut();
      } else if ("POST".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getPost();
      } else if ("DELETE".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getDelete();
      }

      if (operation == null) {
        return;
      }

      ApiResponses apiResponses = operation.getResponses();
      Set<String> keySet = apiResponses.keySet();
      DefaultListModel<String> responseCodeModel = new DefaultListModel<>();
      for (String key : keySet) {
        responseCodeModel.addElement(key);
      }

      setResponseCodeList(responseCodeModel);
      setMediaTypeList(new DefaultListModel<String>());

    } else if (source == responseCodeList) {

      String selectedPath = pathList.getSelectedValue();
      String selectedMethod = methodList.getSelectedValue();
      String selectedResponseCode = responseCodeList.getSelectedValue();
      responseCodeList.setSelectedValue(selectedResponseCode, true);

      PathItem pathItem = paths.get(selectedPath);
      Operation operation = null;

      if ("GET".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getGet();
      } else if ("PUT".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getPut();
      } else if ("POST".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getPost();
      } else if ("DELETE".equalsIgnoreCase(selectedMethod)) {
        operation = pathItem.getDelete();
      }

      if (operation == null) {
        return;
      }

      ApiResponses apiResponses = operation.getResponses();
      ApiResponse apiResponse = apiResponses.get(selectedResponseCode);

      if (apiResponse == null) {
        return;
      }

      Content content = apiResponse.getContent();
      Set<String> contentKeys = content.keySet();
      DefaultListModel<String> mediaTypeModel = new DefaultListModel<>();

      for (String key : contentKeys) {
        mediaTypeModel.addElement(key);
      }

      setMediaTypeList(mediaTypeModel);

    } else if (source == mediaTypeList) {

      String mediaType = mediaTypeList.getSelectedValue();
      mediaTypeList.setSelectedValue(mediaType, true);
      okButton.setEnabled(true);
    }
  }

  private void setMethodList(DefaultListModel<String> model) {
    methodList.clearSelection();
    methodList.setModel(model);
  }

  private void setResponseCodeList(DefaultListModel<String> model) {
    responseCodeList.clearSelection();
    responseCodeList.setModel(model);
  }

  private void setMediaTypeList(DefaultListModel<String> model) {
    okButton.setEnabled(false);
    mediaTypeList.clearSelection();
    mediaTypeList.setModel(model);
  }
}
