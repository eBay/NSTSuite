package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;

public class TMFileSingleton {

  private static TMFileSingleton instance;

  private FileModel sessionFileModel;

  private TMFileSingleton() {
    sessionFileModel = new FileModel(null, null, new NodeModel[] {}, null, null, null);
  }

  public static TMFileSingleton getInstance() {

    if (instance == null) {
      synchronized (TMFileSingleton.class) {
        if (instance == null) {
          instance = new TMFileSingleton();
        }
      }
    }

    return instance;
  }

  public void setFileModel(FileModel fileModel) {
    sessionFileModel = fileModel;
  }

  public FileModel getFileModel() {
    return sessionFileModel;
  }

  public void setPayload(SchemaParserPayload payload) {
    sessionFileModel.setPayload(payload);
  }

  public SchemaParserPayload getPayload() {
    return sessionFileModel.getPayload();
  }
}