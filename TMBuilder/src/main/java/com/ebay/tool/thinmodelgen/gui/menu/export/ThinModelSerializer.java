package com.ebay.tool.thinmodelgen.gui.menu.export;

public interface ThinModelSerializer {

  /**
   * Get the Java statements to be used in the thin model serializer. This should be a single line of builder statements.
   * @return Statements to use in thin model implementation.
   */
  public String getJavaStatements();

  String getKotlinStatements();
}
