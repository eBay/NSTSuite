package com.ebay.nst.schema.validation;

import java.io.IOException;
import java.util.Objects;

import org.testng.Reporter;

import com.ebay.builder.InvalidSchemaValidationException;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.nst.schema.validation.support.SchemaValidatorUtil;
import com.ebay.utility.schema.SchemaLoader;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonSchemaValidator implements NSTRestSchemaValidator {

  private final JsonNode schemaNode;

  /**
   * Initialize validator with the response and root node of the JSON schema
   * tree to validate against.
   *
   * @param schemaNode
   *          Root of the JSON schema tree to validate against.
   */
  public JsonSchemaValidator(JsonNode schemaNode) {
    this.schemaNode = Objects.requireNonNull(schemaNode, "JSON Schema Validator: schema node must not be null.");
  }

  /**
   * Initialize validator with the response and the path to the JSON schema
   * resource to validate against.
   *
   * @param schemaResourcePath
   *          Path to the JSON schema resource to validate against.
   */
  public JsonSchemaValidator(String schemaResourcePath) {

    schemaResourcePath = Objects.requireNonNull(schemaResourcePath, "JSON Schema Validator: schema resource path must not be null.");

    try {
      this.schemaNode = SchemaLoader.loadSchema(schemaResourcePath);
    } catch (IOException e) {
      Reporter.log(String.format("IOException from schemaResourcePath. %s", e.getMessage()), true);
      throw new InvalidSchemaValidationException(e.getMessage());
    }
  }

  @Override
  public void validate(String responseBody) throws SchemaValidationException {

    responseBody = Objects.requireNonNull(responseBody, "Response body must not be null.");
    SchemaValidatorUtil schemaValidator = new SchemaValidatorUtil();
    schemaValidator.validate(schemaNode, responseBody);
  }
}
