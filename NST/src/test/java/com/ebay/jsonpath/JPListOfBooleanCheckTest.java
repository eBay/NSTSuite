package com.ebay.jsonpath;

import java.util.Arrays;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JPListOfBooleanCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = "unitTest")
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":true}]}}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":true}]}}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"sibling\":true},{\"child\":false},{\"child\":true}]}}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":true}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"true\"},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().isEqualTo(Arrays.asList(true,false,true));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().isEqualTo(Arrays.asList(true,true,false));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().contains(Arrays.asList(false,true));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":true},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().contains(Arrays.asList(false,false));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":true},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasAllValuesEqualTo(true);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    JPListOfBooleanCheck check = new JPListOfBooleanCheck().hasAllValuesEqualTo(false);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }
}
