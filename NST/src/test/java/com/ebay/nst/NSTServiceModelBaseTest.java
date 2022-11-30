package com.ebay.nst;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JPIntegerCheck;
import com.ebay.jsonpath.JPStringCheck;
import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.softassert.EbaySoftAssert;
import com.jayway.jsonpath.PathNotFoundException;

public class NSTServiceModelBaseTest {
	
	private JSONObject jsonObject;
	private EbaySoftAssert softAssert;
	
	@BeforeMethod
	public void beforeEachMethod() {
		
		jsonObject = new JSONObject("{ \"root\": { \"name\": \"Bob\", \"age\": 23 } }");
		
		softAssert = new EbaySoftAssert();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void initializeFailsValidate() {
		new ServiceModelTestFailureDouble(jsonObject, softAssert);
	}
	
	@Test
	public void readJsonPathAndGetLeafNode() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		String name = testDouble.readJsonPath("$.root.name");
		assertThat(name, is(equalTo("Bob")));
	}
	
	@Test
	public void readJsonPathForUnknownLeafNode() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		String name = testDouble.readJsonPath("$.root.height");
		assertThat(name, is(nullValue()));
	}
	
	@Test
	public void readRequiredJsonPathAndGetLeafNode() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		String name = testDouble.readRequiredJsonPath("$.root.name");
		assertThat(name, is(equalTo("Bob")));
	}

	@Test
	public void readRequiredJsonPathForUnknownLeafNode() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		String name = testDouble.readRequiredJsonPath("$.root.height");
		assertThat(name, is(nullValue()));
	}
	
	@Test(expectedExceptions = PathNotFoundException.class)
	public void readRequiredJsonPathForUnknownPath() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.readRequiredJsonPath("$.roots.name");
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void assertStringIsNotNullOrEmptyWithNullString() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertStringIsNotNullOrEmpty(softAssert, null, "error message.");
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void assertStringIsNotNullOrEmptyWithEmptyString() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertStringIsNotNullOrEmpty(softAssert, "", "error message.");
		softAssert.assertAll();
	}
	
	@Test
	public void assertStringIsNotNullOrEmptyWithValidString() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertStringIsNotNullOrEmpty(softAssert, "pass", "error message.");
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void assertListOfStringIsNotNullOrEmptyWithNullList() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertListOfStringsIsNotNullOrEmptyAndEachStringIsNotNullOrEmpty(softAssert, null, "error message");
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void assertListOfStringIsNotNullOrEmptyWithEmptyList() {
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertListOfStringsIsNotNullOrEmptyAndEachStringIsNotNullOrEmpty(softAssert, new ArrayList<String>(), "error message");
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void assertListOfStringIsNotNullOrEmptyWithNullStringElement() {
		List<String> inputs = new ArrayList<>();
		inputs.add(null);
		
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertListOfStringsIsNotNullOrEmptyAndEachStringIsNotNullOrEmpty(softAssert, inputs, "error message");
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void assertListOfStringIsNotNullOrEmptyWithEmptyStringElement() {
		List<String> inputs = new ArrayList<>();
		inputs.add("");
		
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertListOfStringsIsNotNullOrEmptyAndEachStringIsNotNullOrEmpty(softAssert, inputs, "error message");
		softAssert.assertAll();
	}
	
	@Test
	public void assertListOfStringIsNotNullOrEmptyWithValidListOfString() {
		
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.assertListOfStringsIsNotNullOrEmptyAndEachStringIsNotNullOrEmpty(softAssert, Arrays.asList("pass", "good"), "error message");
		softAssert.assertAll();
	}
	
	@Test
	public void evaluateJsonPathPass() {
		
		JsonPathExecutor executor = new JPStringCheck().isEqualTo("Bob");
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.evaluateJsonPath("$.root.name", softAssert, executor);
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void evaluateJsonPathFail() {
		
		JsonPathExecutor executor = new JPStringCheck().isEqualTo("fail");
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.evaluateJsonPath("$.root.name", softAssert, executor);
		softAssert.assertAll();
	}
	
	@Test
	public void evaluateJsonPathsPass() {
		
		Map<String, JsonPathExecutor> executors = new HashMap<>();
		executors.put("$.root.name", new JPStringCheck().isEqualTo("Bob"));
		executors.put("$.root.age", new JPIntegerCheck().isEqualTo(23));
		
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.evaluateJsonPaths(executors, softAssert);
		softAssert.assertAll();
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void evaluateJsonPathsFail() {
		
		Map<String, JsonPathExecutor> executors = new HashMap<>();
		executors.put("$.root.name", new JPStringCheck().isEqualTo("Bob"));
		executors.put("$.root.age", new JPIntegerCheck().isEqualTo(45));
		
		ServiceTestDouble testDouble = new ServiceTestDouble(jsonObject, softAssert);
		testDouble.evaluateJsonPaths(executors, softAssert);
		softAssert.assertAll();
	}
	
	class ServiceModelTestFailureDouble extends NSTServiceModelBase {

		public ServiceModelTestFailureDouble(JSONObject jsonRoot, SoftAssert softAssert) {
			super(jsonRoot, softAssert);
		}

		@Override
		protected void validate(SoftAssert softAssert) {
			softAssert.assertTrue(false);
		}
	}
	
	class ServiceTestDouble extends NSTServiceModelBase {

		public ServiceTestDouble(JSONObject jsonRoot, SoftAssert softAssert) {
			super(jsonRoot, softAssert);
		}

		@Override
		protected void validate(SoftAssert softAssert) {

		}
		
	}
}
