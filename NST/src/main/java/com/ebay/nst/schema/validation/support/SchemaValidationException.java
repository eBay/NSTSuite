package com.ebay.nst.schema.validation.support;

/**
 * Schema validation exception thrown if there was an issue found when
 * evaluating the service response.
 */
public class SchemaValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String message;

  /**
   * Create an instance with the specified message to show in the test result
   * log.
   *
   * @param message
   *          Message to show in the test result log.
   */
  public SchemaValidationException(String message) {
    this.message = message;
  }

  /**
   * Get the message to show in the test result log.
   */
  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return getMessage();
  }

}
