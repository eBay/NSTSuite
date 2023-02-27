package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;

public class FileModel {

  private String fileFormatVersion;
  private String sourceSchemaFilePath;
  private NodeModel[] data;
  private String exportFilePath;
  private String mockFilePath;
  private ValidationSetModel[] validationSets;
  private SchemaParserPayload payload;

  /*
   * For each DefaultMutableTreeNode - get the userObject for the node and use
   * the jsonPathNodeName to identify the node. Process each child node by index
   * and jsonPathNodeName (from the JsonBaseType)
   */

  public FileModel(String fileFormatVersion, String sourceSchemaFilePath, NodeModel[] data, String exportFilePath, String mockFilePath, SchemaParserPayload payload) {
    super();
    this.fileFormatVersion = fileFormatVersion;
    this.sourceSchemaFilePath = sourceSchemaFilePath;
    this.data = data;
    this.exportFilePath = exportFilePath;
    this.mockFilePath = mockFilePath;
    this.payload = payload;
  }

  public FileModel(String fileFormatVersion, String sourceSchemaFilePath, ValidationSetModel[] validationSets, String exportFilePath, String mockFilePath, SchemaParserPayload payload) {
    super();
    this.fileFormatVersion = fileFormatVersion;
    this.sourceSchemaFilePath = sourceSchemaFilePath;
    this.validationSets = validationSets;
    this.exportFilePath = exportFilePath;
    this.mockFilePath = mockFilePath;
    this.payload = payload;
  }

  public String getFileFormatVersion() {
    return fileFormatVersion;
  }

  public void setFileFormatVersion(String fileFormatVersion) {
    this.fileFormatVersion = fileFormatVersion;
  }

  public String getSourceSchemaFilePath() {
    return sourceSchemaFilePath;
  }

  public void setSourceSchemaFilePath(String sourceSchemaFilePath) {
    this.sourceSchemaFilePath = sourceSchemaFilePath;
  }

  public NodeModel[] getData() {
    return data;
  }

  public void setData(NodeModel[] data) {
    this.data = data;
  }

  public String getExportFilePath() {
    return exportFilePath;
  }

  public void setExportFilePath(String exportFilePath) {
    this.exportFilePath = exportFilePath;
  }

  public String getMockFilePath() {
    return mockFilePath;
  }

  public void setMockFilePath(String mockFilePath) {
    this.mockFilePath = mockFilePath;
  }

  public ValidationSetModel[] getValidationSets() {
    return validationSets;
  }

  public void setValidationSets(ValidationSetModel[] validationSets) {
    this.validationSets = validationSets;
  }

  public SchemaParserPayload getPayload() {
    return payload;
  }

  public void setPayload(SchemaParserPayload payload) {
    this.payload = payload;
  }

}
