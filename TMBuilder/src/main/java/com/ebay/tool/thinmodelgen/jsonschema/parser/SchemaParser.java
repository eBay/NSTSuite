package com.ebay.tool.thinmodelgen.jsonschema.parser;

import javax.swing.tree.DefaultMutableTreeNode;

public interface SchemaParser {

  /**
   * Parse the given schema provided by the schema path.
   *
   * @param schemaPath
   *          Path to schema to parse.
   * @return Parsed tree model.
   */
  public DefaultMutableTreeNode parseSchema(String schemaPath);
}
