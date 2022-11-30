package com.ebay.nst;

import com.ebay.nst.schema.validation.NSTSchemaValidator;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;

public interface NSTServiceWrapper<SchemaValidator extends NSTSchemaValidator> {

	/**
	 * Get the service name to make the call.
	 *
	 * @return Service Name in String
	 */
	String getServiceName();

	/**
	 * Get the request type of the call.
	 *
	 * @return NST Request Type.
	 */
	NstRequestType getRequestType();

	/**
	 * Get the endpoint of the host to be called.
	 *
	 * @return Endpoint of the host to be called.
	 */
	String getEndpointPath();

	/**
	 * Prepare the request to be sent (headers, payload, etc) and return the Breeze
	 * Request. This will be handed to the sendRequest() method to be sent.
	 *
	 * @return Breeze Request instance.
	 */
	NSTHttpRequest prepareRequest();

	/**
	 * Get the schema validator to use when evaluating service responses. Set to
	 * null to remove the current schema validator. To temporarily disable use the
	 * runtime argument to disable schema validation. Set "-Dschemavalidation=false"
	 * in pom.xml
	 *
	 * @return Schema validator to apply, or null if no validator is to be applied.
	 */
	SchemaValidator getSchemaValidator();

	/**
	 * Optional method to override that allows for logging information about the
	 * response to the console. Usefull for logging response information, like
	 * session ID/details, to console.
	 *
	 * @param response Response instance from the service call.
	 * @return Custom message to print to console, otherwise null.
	 */
	default String getServiceWrapperConsoleOutput(NSTHttpResponse response) {
		return null;
	}

	/**
	 * Optional method to log additional information about the service call made to
	 * console. Nothing will be logged if null is returned.
	 * 
	 * @return Additional service details or null to not log anything.
	 */
	default NSTServiceDetails getServiceDetails() {
		return null;
	}

	/**
	 * Optional method to return a ResponseLoggerInjector that will be used to
	 * modify the response payload before it is written to the mock file. Useful for
	 * injecting values like web view URLs to force the return of control to the
	 * native client when the web view opens.
	 * 
	 * @return ResponseLoggerInjector instance to have modify the response payload.
	 */
	default ResponseLoggerInjector getResponseLoggerInjector() {
		return null;
	}

	/**
	 * Check if schema validation should always be disabled for every use of the
	 * service wrapper. If this method returns true, schema validaiton WILL NEVER
	 * run, even if the service processor is set to run schema validation. If this
	 * is set to false the processor settings will take over.
	 * 
	 * @return True to ALWAYS disable schema validation. Default is false.
	 */
	default boolean alwaysDisableSchemaValidation() {
		return false;
	}

	/**
	 * Check if writing of request and response data (mocks and client tests) should
	 * always be disabled for every use of the service wrapper. If this method
	 * returns true, requests and responses WILL NEVER be written, even if the
	 * service processor is set to write. If this is set to false the processor
	 * settings will take over.
	 * 
	 * @return True to ALWAYS disable request and response logging. Default is
	 *         false.
	 */
	default boolean alwaysDisableRequestResponseLogging() {
		return false;
	}
	
	/**
	 * Optional method to return the unique service wrapper name, to be utilized when generating mocks.
	 * If not set, then the class name of the service wrapper will be utilized.
	 *
	 * @return The unique service wrapper name to be used with mock generation.
	 */
	default String getUniqueServiceWrapperName() {
		return null;
	}
}
