package com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog;

import org.testng.annotations.Test;

import io.swagger.v3.oas.models.Paths;

public class SchemaSelectDialogTest {

  // Enable this test method to manually evaluate changes to the schema
  // selection dialog. Otherwise, leave this disabled.
  @Test(enabled = false)
  public void showDialog() {
    Paths paths = new Paths();
    SchemaSelectDialog dialog = new SchemaSelectDialog(paths, null);
    dialog.getPayload();
  }
}
