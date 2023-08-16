package com.ebay.jsonpath;

import java.util.Arrays;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JPStringCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = "unitTest")
  public void passPatternCheck() {
      SoftAssert softAssert = new SoftAssert();
      DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"valueWithPattern12345\"}");
      JPStringCheck check = new JPStringCheck().matchesPattern("(.+)(1)\\d+");
      check.processJsonPath("$.foo", softAssert, jsonPathDocument);

      softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPStringCheck check = new JPStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    JPStringCheck check = new JPStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    JPStringCheck check = new JPStringCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullExpected() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPStringCheck check = new JPStringCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    JPStringCheck check = new JPStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\": {\"bar\":\"VALUE\"}}");
    JPStringCheck check = new JPStringCheck();
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passExpectedOneOfString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPStringCheck check = new JPStringCheck().isEqualToOneOf(Arrays.asList("value", "otherValue"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedOneOfString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPStringCheck check = new JPStringCheck().isEqualToOneOf(Arrays.asList("otherValue", "anotherValue"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passExpectedString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPStringCheck check = new JPStringCheck().isEqualTo("value");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPStringCheck check = new JPStringCheck().isEqualTo("bar");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passContainsString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value in the middle\"}");
    JPStringCheck check = new JPStringCheck().contains("in the");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value in the middle\"}");
    JPStringCheck check = new JPStringCheck().contains("expected string");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passStringLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    JPStringCheck check = new JPStringCheck().hasLength(4);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failStringLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    JPStringCheck check = new JPStringCheck().hasLength(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMinimumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    JPStringCheck check = new JPStringCheck().hasMinimumNumberOfCharacters(4);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    JPStringCheck check = new JPStringCheck().hasMinimumNumberOfCharacters(5);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMaximumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    JPStringCheck check = new JPStringCheck().hasMaximumNumberOfCharacters(4);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    JPStringCheck check = new JPStringCheck().hasMaximumNumberOfCharacters(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPatternCheck() {
      SoftAssert softAssert = new SoftAssert();
      DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"valueWithPattern12345\"}");
      JPStringCheck check = new JPStringCheck().matchesPattern("(.+)(1)");
      check.processJsonPath("$.foo", softAssert, jsonPathDocument);

      softAssert.assertAll();
  }
}
