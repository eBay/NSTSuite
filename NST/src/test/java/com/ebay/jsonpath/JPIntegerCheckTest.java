package com.ebay.jsonpath;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JPIntegerCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithIntegerNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    JPIntegerCheck check = new JPIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithIntegerNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    JPIntegerCheck check = new JPIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    JPIntegerCheck check = new JPIntegerCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullExpected() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    JPIntegerCheck check = new JPIntegerCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithIntegerMismatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    JPIntegerCheck check = new JPIntegerCheck().isEqualTo(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passWithIntegerMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    JPIntegerCheck check = new JPIntegerCheck().isEqualTo(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"2\"}");
    JPIntegerCheck check = new JPIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\": {\"bar\":1}}");
    JPIntegerCheck check = new JPIntegerCheck();
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }
}
