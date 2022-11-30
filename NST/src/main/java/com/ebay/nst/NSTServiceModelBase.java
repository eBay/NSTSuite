package com.ebay.nst;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

/**
 * ServiceModelBase is intended to force the adoption of a thin model object
 * structure that does not depend on full POJOs. Instead accessors are
 * implemented using JSON Path to target the values desired.
 *
 * For usage, see:
 *
 * JSON Lint (use to validate JSON samples used in JSON path online evaluator):
 * https://jsonlint.com/ JSON Path evaluator:
 * http://jsonpath.herokuapp.com/?path=$.store.book[*].author JSON Path
 * documentation: https://github.com/json-path/JsonPath
 *
 * @author byarger
 */
public abstract class NSTServiceModelBase {

	protected JSONObject jsonRoot;
	private DocumentContext jsonPathDocument;
	private DocumentContext requiredJsonPathDocument;

	/**
	 * Initialize instance and validate the response.
	 * 
	 * @param jsonRoot   JSONObject root data to parse. Cannot be null.
	 * @param softAssert SoftAssert instance if passed in will validate the
	 *                   response. Validate will be ignored if softAssert is null.
	 *                   If validation is performed the SoftAssert checks will be
	 *                   executed and any failures will throw an AssertException.
	 */
	public NSTServiceModelBase(JSONObject jsonRoot, SoftAssert softAssert) {

		if (jsonRoot == null) {
			throw new IllegalArgumentException("JSON Root cannot be null.");
		}

		this.jsonRoot = jsonRoot;

		Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS,
				Option.DEFAULT_PATH_LEAF_TO_NULL);
		jsonPathDocument = JsonPath.using(config).parse(jsonRoot.toString());

		Configuration configRequiredPath = Configuration.defaultConfiguration()
				.setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
		requiredJsonPathDocument = JsonPath.using(configRequiredPath).parse(jsonRoot.toString());

		if (softAssert != null) {
			validate(softAssert);
			softAssert.assertAll();
		}
	}

	/**
	 * Validate the model according to baseline criteria (expected population of
	 * certain data fields) of the response.
	 *
	 * Does not perform specific value tests for specific scenarios. Test author
	 * should evaluate those conditions directly in their test.
	 *
	 * Validate encapsulates the validation functionality performed on every single
	 * response regardless of circumstance.
	 * 
	 * @param softAssert SoftAssert to perform asserts against.
	 */
	protected abstract void validate(SoftAssert softAssert);

	/**
	 * Read the JSON path specified.
	 * 
	 * @param <T>  Expected return type.
	 * @param path Path to return data for.
	 * @return Data found or null if the path doesn't exist or the leaf doesn't
	 *         exist.
	 */
	protected <T> T readJsonPath(String path) {
		return jsonPathDocument.read(path);
	}

	/**
	 * Read the JSON path specified as a required path (exception will be thrown if
	 * path doesn't exist).
	 * 
	 * @param <T>  Expected return type.
	 * @param path Path to return data for, or throw exception if it does not exist.
	 * @return Data found, or null if the leaf does not exist. Exception thrown if
	 *         the path does not exist.
	 */
	protected <T> T readRequiredJsonPath(String path) {
		return requiredJsonPathDocument.read(path);
	}

	/**
	 * Validate that the given string is not null or empty.
	 * 
	 * @param softAssert  SoftAssert instance to record results against.
	 * @param stringValue String to evaluate.
	 * @param message     Custom message to apply to the assert. Can be null to not
	 *                    apply a custom message to the report.
	 */
	protected void assertStringIsNotNullOrEmpty(SoftAssert softAssert, String stringValue, String message) {

		if (message == null) {
			message = "";
		}

		softAssert.assertNotNull(stringValue, String.format("%s - Found null string.", message));
		if (stringValue != null) {
			softAssert.assertTrue(stringValue.length() > 0, String.format("%s - Found empty string.", message));
		}
	}

	/**
	 * Validate that the given list of strings is itself not null or empty and that
	 * each of the string values are not null or empty.
	 * 
	 * @param softAssert   SoftAssert instance to record results against.
	 * @param stringValues List of strings to evaluate.
	 * @param message      Custom message to apply to the assert. Can be null to not
	 *                     apply a custom message to the report.
	 */
	protected void assertListOfStringsIsNotNullOrEmptyAndEachStringIsNotNullOrEmpty(SoftAssert softAssert,
			List<String> stringValues, String message) {

		if (message == null) {
			message = "";
		}

		softAssert.assertNotNull(stringValues, String.format("%s - List of string values was null.", message));

		if (stringValues == null) {
			return;
		}

		softAssert.assertTrue(stringValues.size() > 0, String.format("%s - List of string values was empty.", message));

		for (int i = 0; i < stringValues.size(); i++) {
			String value = stringValues.get(i);
			softAssert.assertNotNull(value, String.format("%s - Found null value on index: %d.", message, i));
			if (value != null) {
				softAssert.assertTrue(value.length() > 0,
						String.format("%s - Found empty value on index: %d.", message, i));
			}
		}
	}

	protected void evaluateJsonPaths(Map<String, JsonPathExecutor> jsonPathToJsonPathExecutorMap,
			SoftAssert softAssert) {

		Set<String> keys = jsonPathToJsonPathExecutorMap.keySet();
		for (String key : keys) {
			evaluateJsonPath(key, softAssert, jsonPathToJsonPathExecutorMap.get(key));
		}
	}

	protected void evaluateJsonPath(String jsonPath, SoftAssert softAssert, JsonPathExecutor executor) {
		executor.processJsonPath(jsonPath, softAssert, requiredJsonPathDocument);
	}
}
