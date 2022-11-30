package com.ebay.nst.schema.validation;

import com.ebay.nst.schema.validation.support.SchemaValidationException;

public interface NSTSchemaValidator {

  /**
   * Perform the validate operation against the defined service schema.
   *
   * @param responseBody
   *          Response payload to verify against the schema.
   * @throws SchemaValidationException
   *           Throw an exception if the validation check fails. Exception must
   *           contain all of the messaging needed to present to the tester
   *           regarding the validation failures.
   */
  public void validate(String responseBody) throws SchemaValidationException;
}
