package com.ebay.jsonpath;

import java.util.Arrays;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JPListOfDoubleCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = "unitTest")
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":null},{\"bar\":null}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.checkIsNull(true);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":2.5}]}}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":2.5}]}}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":2.5},{\"sibling\":1.5},{\"child\":3.5}]}}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2.0}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"1.0\"},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0,2.5,3.14));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0,3.14,2.5));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().contains(Arrays.asList(1.0,2.5));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().contains(Arrays.asList(0.0,4.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":1.0},{\"bar\":1.0}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasAllValuesEqualTo(1.0);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.0},{\"bar\":1.0}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().hasAllValuesEqualTo(1.0);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passConvertingListOfIntegerToListOfDouble() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0, 2.0, 3.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passConvertingListOfIntegerToListOfDoubleWithListContainingADouble() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3.0}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0, 2.0, 3.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passConvertingListOfIntegerToListOfDoubleWithListContainingADoubleAsFirstValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2},{\"bar\":3}]}");
    JPListOfDoubleCheck check = new JPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0, 2.0, 3.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }
}
