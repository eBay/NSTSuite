package com.ebay.builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.testng.Reporter;

import com.ebay.nst.schema.validation.NSTSchemaValidator;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

/**
 * Class to help Validate Open API Schema with service response
 *
 * <p>
 * <b>Sample Usage:</b> <code>
 *  new SchemaValidation.Construct()
 * .response(response)
 * .schemaValidator(new OpenApiSchemaValidator())
 * .finishConstruct()
 * .validate();
 *
 * The validate operation throws ProcessingException if validation fails.
 *
 * </code>
 * </p>
 */
public class SchemaValidation {

  private final Construct construct;

  private SchemaValidation(Construct construct) {
    construct = Objects.requireNonNull(construct, "Construct MUST NOT be null.");
    this.construct = construct;
  }

  public static class Construct {

    // required parameter
    private String response;

    // Any one parameter is required
    private NSTSchemaValidator nstSchemaValidator;

    /**
     * Set the response to evaluate.
     *
     * @param response
     *          Response to evaluate.
     * @return Builder instance.
     */
    public Construct setResponse(String response) {
      response = Objects.requireNonNull(response, "Response MUST NOT be null when building Construct.");
      this.response = response;
      return this;
    }

    /**
     * Set the schema validator to evaluate the response with.
     *
     * @param nstSchemaValidator
     *          Schema validator instance to evaluate response with.
     * @return Builder instance.
     */
    public Construct setSchemaValidator(NSTSchemaValidator nstSchemaValidator) {
      nstSchemaValidator = Objects.requireNonNull(nstSchemaValidator, "Validator MUST NOT be null when building Construct.");
      this.nstSchemaValidator = nstSchemaValidator;
      return this;
    }

    /**
     * Finish building and return the instance.
     *
     * @return Instance, if there weren't any errors.
     */
    public SchemaValidation finishConstruct() {

      if (((null == response) || (response.length() == 0))) {
        throw new InvalidSchemaValidationException("response is empty/null");
      }

      if ((null == nstSchemaValidator)) {
        throw new InvalidSchemaValidationException("schemaNode is null");
      }

      return new SchemaValidation(this);
    }
  }

  /**
   * Execute the validation that compares the service response against the
   * contract schema.
   *
   * @throws ProcessingException
   *           Pass through of ProcessingException, in the event there is a
   *           validation error.
   */
  public void validate() throws ProcessingException {

    Reporter.log(String.format("Start json validation at %s", getCurrentTime()), true);
    SchemaValidationException schemaValidaitonException = null;

    try {
      construct.nstSchemaValidator.validate(construct.response);
    } catch (SchemaValidationException sve) {
      schemaValidaitonException = sve;
    }

    Reporter.log(String.format("Finish json validation at %s", getCurrentTime()), true);

    if (schemaValidaitonException == null) {
      Reporter.log("Validate Schema with Response is Success", true);
    } else {
      Reporter.log("Validate Schema with Response is Failed", true);
      throw new ProcessingException(schemaValidaitonException.getMessage());
    }
  }

  private String getCurrentTime() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

}
