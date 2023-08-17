package com.ebay.jsonpath;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JPJsonObjectCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = "unitTest")
  public void passWithNonNullObject() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithNullObject() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    JPJsonObjectCheck check = new JPJsonObjectCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    JPJsonObjectCheck check = new JPJsonObjectCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullExpected() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"first\":\"A\"},{\"second\":2},{\"third\":true}]}");
    JPJsonObjectCheck check = new JPJsonObjectCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck();
    check.processJsonPath("$.bar.first", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithExpectedNumberOfKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().hasNumberOfKeys(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithUnexpectedNumberOfKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().hasNumberOfKeys(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithExpectedKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().keysAreEqualTo(Arrays.asList("first","second","third"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithUnexpectedKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().keysAreEqualTo(Arrays.asList("first","third","fourth"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithContainsKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().keysContain(Arrays.asList("first","second"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithDoesNotContainKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().keysContain(Arrays.asList("fourth","second"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithExpectedMap() {

    SoftAssert softAssert = new SoftAssert();

    LinkedHashMap<String, Object> expectedMap = new LinkedHashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("second", 2);
    expectedMap.put("third", true);

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().isEqualTo(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithUnexpectedMap() {

    SoftAssert softAssert = new SoftAssert();

    LinkedHashMap<String, Object> expectedMap = new LinkedHashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("second", 2);
    expectedMap.put("fourth", "B");

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().isEqualTo(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithContainsMap() {

    SoftAssert softAssert = new SoftAssert();

    HashMap<String, Object> expectedMap = new HashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("second", 2);

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().contains(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithDoesNotContainMap() {

    SoftAssert softAssert = new SoftAssert();

    HashMap<String, Object> expectedMap = new HashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("fail", 2);

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    JPJsonObjectCheck check = new JPJsonObjectCheck().contains(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }
}
