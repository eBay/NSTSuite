package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import java.io.IOException;
import java.util.List;

public interface FileOperationHandler {

  /**
   * Load the specified schema and apply saved JSON path check data to the model
   * if saved data is provided.
   *
   * @param schemaPath
   *          Schema file to load.
   * @param savedData
   *          Optional saved data. May be null.
   * @throws IOException Pass through exception.
   * @throws ClassNotFoundException Pass through exception.
   */
  public void loadSchema(String schemaPath, NodeModel[] savedData) throws IOException, ClassNotFoundException;

  /**
   * Get the JSON path check data for the current schema tree.
   *
   * @return JSON path check data list.
   * @throws IOException Pass through exception.
   */
  public List<NodeModel> getJsonPathCheckData() throws IOException;
}
