package com.ebay.nst;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.ebay.runtime.arguments.DisableConsoleLog;
import org.json.JSONObject;
import org.testng.Reporter;

import com.ebay.nst.schema.validation.NSTSchemaValidator;
import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.FormatWriterUtil;
import com.ebay.service.logger.WhatToWrite;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.call.cache.ServiceCallCacheManager;
import com.ebay.service.logger.formats.ErrorLogger;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpClient;
import com.ebay.service.protocol.http.NSTHttpClientImpl;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpResponse;
import com.ebay.utility.service.ServiceUtil;

public final class NSTServiceWrapperProcessor {

	private boolean disableSchemaValidation = false;
	private boolean disableRequestResponseLogging = false;
	private boolean confirmSuccess = true;
	private NSTHttpClient<NSTHttpRequest, NSTHttpResponse> client;

	/**
	 * Creates a new service process with the default HttpUrlConnection used for
	 * making client calls.
	 */
	public NSTServiceWrapperProcessor() {
		client = new NSTHttpClientImpl();
	}

	/**
	 * Provide a custom client implementation to use instead of the default
	 * HttpUrlConnection provided by NST.
	 * 
	 * @param client Client implementation to use.
	 */
	public NSTServiceWrapperProcessor(NSTHttpClient<NSTHttpRequest, NSTHttpResponse> client) {
		this.client = client;
	}

	/**
	 * Allow for schema validation to be turned off at the service instance. This
	 * will override all runtime settings and prevent contract validation from
	 * running.
	 * 
	 * @return Current instance.
	 */
	public NSTServiceWrapperProcessor disableSchemaValidation() {
		disableSchemaValidation = true;
		return this;
	}

	/**
	 * Check if schema validation is disabled.
	 * 
	 * @return True if disabled, false otherwise.
	 */
	public boolean isSchemaValidationDisabled() {
		return disableSchemaValidation;
	}

	/**
	 * Disable request and response logging to file. This will override all runtime
	 * settings and prevent mocks and client test code from being written.
	 * 
	 * @return Current instance.
	 */
	public NSTServiceWrapperProcessor disableRequestResponseLogging() {
		disableRequestResponseLogging = true;
		return this;
	}

	/**
	 * Check if all request response logging is disabled.
	 * 
	 * @return True if disabled, false otherwise.
	 */
	public boolean isRequestResponseLoggingDisabled() {
		return disableRequestResponseLogging;
	}

	/**
	 * Disable confirm success of response check.
	 * 
	 * @return Current instance.
	 */
	public NSTServiceWrapperProcessor disableConfirmSuccess() {
		confirmSuccess = false;
		return this;
	}

	/**
	 * Check if confirm success is disabled.
	 * 
	 * @return True if disabled, false otherwise.
	 */
	public boolean isConfirmSuccessDisabled() {
		return !confirmSuccess;
	}

	/**
	 * Reset the disableSchemaValidation flag to false.
	 * 
	 * @return Current instance.
	 */
	public NSTServiceWrapperProcessor resetDisableSchemaValidation() {
		disableSchemaValidation = false;
		return this;
	}

	/**
	 * Reset the disableRequestResponseLogging flag to false
	 * 
	 * @return Current instance.
	 */
	public NSTServiceWrapperProcessor resetDisableRequestResponseLogging() {
		disableRequestResponseLogging = false;
		return this;
	}

	/**
	 * Reset the confirm success flag to true.
	 * 
	 * @return Current instance.
	 */
	public NSTServiceWrapperProcessor resetConfirmSuccess() {
		confirmSuccess = true;
		return this;
	}

	/**
	 * Send the request and get back the response JSON object.
	 *
	 * @param serviceWrapper Service wrapper that will send the request.
	 *
	 * @return JSON object.
	 * @throws IOException           Pass through.
	 * @throws URISyntaxException    Pass through.
	 * @throws IllegalStateException Pass through.
	 */
	public JSONObject sendRequestAndGetJSONResponse(
			NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper)
			throws IllegalStateException, URISyntaxException, IOException {

		NSTHttpResponse response = sendRequestWrapper(serviceWrapper);
		JSONObject root = new JSONObject(response.getPayload());
		return root;
	}

	protected void logCallDetails(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper,
			NSTHttpRequest requestInstance, NSTHttpResponse response)
			throws IllegalStateException, URISyntaxException, IOException {

		String serviceWrapperName = getServiceWrapperName(serviceWrapper);
		boolean isLoggingDisabled = isRequestResponseLoggingDisabled();
		List<WhatToWrite> whatToWrite = RuntimeConfigManager.getInstance().getWhatToWrite();
		boolean whatToWriteContainsNone = whatToWrite.contains(WhatToWrite.NONE);
		boolean logRequestResponse = !isLoggingDisabled && !whatToWriteContainsNone
				&& !serviceWrapper.alwaysDisableRequestResponseLogging();

		// Log the request and response.
		if (logRequestResponse) {

			ResponseLoggerInjector injector = serviceWrapper.getResponseLoggerInjector();
			NSTHttpResponse mockResponse = response;
			if (injector != null) {
				mockResponse = FormatWriterUtil.getModifiedResponsePayload(response, injector);
			}
			
			ServiceCallCacheData callData = new ServiceCallCacheData(requestInstance, mockResponse, serviceWrapperName);
			ServiceCallCacheManager.getInstance().addCallToCache(callData);
		}
	}

	protected void confirmSuccess(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper,
			NSTHttpRequest requestInstance, NSTHttpResponse response) throws IOException {
		String serviceWrapperName = getServiceWrapperName(serviceWrapper);

		// If we have an error and were asked to confirm success then we will
		// write the error file, regardless of the log settings.
		if (confirmSuccess) {

			// If we have a 2XX value then consider the response a success and return.
			int responseCodeValue = response.getResponseCode();
			if (responseCodeValue >= 200 && responseCodeValue < 300) {
				return;
			}
			
			ErrorLogger errorLogger = new ErrorLogger();
			String errorFile = errorLogger.writeFile(requestInstance, response, serviceWrapperName);

			throw new IllegalStateException(
					String.format("Unable to complete call. [status=%d]. Error log written to: %s",
							response.getResponseCode(), errorFile));
		}
	}

	protected void schemaValidation(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper,
			NSTHttpResponse response) {
		// validate the schema if schema node is not null and flag is not set to
		// false
		boolean isSchemaValidate = RuntimeConfigManager.getInstance().validateSchema();
		NSTSchemaValidator schemaValidator = serviceWrapper.getSchemaValidator();

		if ((!isSchemaValidationDisabled()) && (isSchemaValidate) && (schemaValidator != null)
				&& !serviceWrapper.alwaysDisableSchemaValidation()) {
			Reporter.log("Schema Validation Turned On.", true);
			Reporter.log(schemaValidator.toString(), true); // Print out the validator details including schema path and API choice.
			schemaValidator.validate(response.getPayload());
		} else {
			Reporter.log("Schema Validation Turned Off");
		}
	}

	protected String getServiceWrapperName(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper) {
		if (serviceWrapper.getUniqueServiceWrapperName() != null) {
			return serviceWrapper.getUniqueServiceWrapperName();
		} else {
			return serviceWrapper.getClass().getSimpleName();
		}
	}

	protected NSTHttpResponse sendRequestWrapper(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper)
			throws URISyntaxException, IllegalStateException, IOException {

		NSTHttpRequest requestInstance = serviceWrapper.prepareRequest();
		logRequestDetailsToConsole(serviceWrapper, requestInstance);

		logServiceDetails(serviceWrapper);

		NSTHttpResponse response = sendRequest(requestInstance);
		logResponseDetailsToConsole(serviceWrapper, response);

		confirmSuccess(serviceWrapper, requestInstance, response);

		schemaValidation(serviceWrapper, response);

		logCallDetails(serviceWrapper, requestInstance, response);

		return response;
	}

	protected void logServiceDetails(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper) {

		NSTServiceDetails details = serviceWrapper.getServiceDetails();
		if (details != null) {
			String detailsOutput = details.toString();
			if (!detailsOutput.isEmpty()) {
				Reporter.log(detailsOutput, true);
			}
		}
	}

	protected void logRequestDetailsToConsole(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper,
			NSTHttpRequest request) throws URISyntaxException, IOException, IllegalStateException {

		if (request == null) {
			return;
		}

		Reporter.log(String.format("Request: %s", request.getClass().getSimpleName()), true);
		Reporter.log(String.format("Request URL: %s", ServiceUtil.getUrl(serviceWrapper)), true);

		Object payload = request.getPayload();
		Map<String, String> headers = request.getHeaders();

		String payloadValue = "No payload sent";
		String headerValue = "No headers sent";

		if (payload != null) {
			payloadValue = payload.toString();
		}

		if (headers != null) {
			headerValue = headers.toString();
		}

		Reporter.log(String.format("Request payload: %s", payloadValue), true);
		Reporter.log(String.format("Request headers: %s", headerValue), true);
	}

	protected NSTHttpResponse sendRequest(NSTHttpRequest request)
			throws URISyntaxException, IOException, IllegalStateException {

		return client.sendRequest(request);
	}

	protected void logResponseDetailsToConsole(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper,
			NSTHttpResponse response) {

		if (RuntimeConfigManager.getInstance().getDisableConsoleLogValues().contains(DisableConsoleLog.RESPONSE_PAYLOAD)) {
			return;
		}

		if (response == null) {
			Reporter.log("Response was null - skipping logging response details.", true);
			return;
		}

		Map<String, String> responseHeaders = response.getHeaders();
		if (responseHeaders != null && !responseHeaders.isEmpty()) {
			Reporter.log("Response Headers:", true);
			for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
				Reporter.log(String.format("%s : %s", entry.getKey(), entry.getValue()), true);
			}
		} else {
			Reporter.log("Response does not contain headers.", true);
		}

		if (response.getPayload() != null) {
			Reporter.log("Response Payload:", true);
			Reporter.log(response.getPayload(), true);
		} else {
			Reporter.log("Response does not contain a payload.", true);
		}

		// Log custom info from wrapper
		String customLog = serviceWrapper.getServiceWrapperConsoleOutput(response);
		if (customLog != null) {
			Reporter.log(String.format("Response : %s", customLog), true);
		}
	}
}
