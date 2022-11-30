package com.ebay.jsonpath;

import java.util.Arrays;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JPListOfStringCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = "unitTest")
  public void passNonNullValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":null}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullMissingNode() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"value\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void failNullMissingFilterNode() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"value\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo[?(@.bar == \"nothing\")].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\": \"value\"}]}}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\": \"value\"}]}}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\": \"value\"},{\"child\": \"value\"},{\"sibling\": \"xxx\"}]}}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo.parent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":2}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passExactLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().hasLength(3);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failUnexpectedLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().hasLength(4);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMinLength() {
    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void faiMinLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passMaxLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaxLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passValueMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().isEqualTo(Arrays.asList("A", "B", "C"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failValueMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().isEqualTo(Arrays.asList("A", "C", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().contains(Arrays.asList("A", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().contains(Arrays.asList("A", "D"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = "unitTest")
  public void passIsLimitedToValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"A\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().isLimitedToValues(Arrays.asList("A", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failIsLimitedToValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    JPListOfStringCheck check = new JPListOfStringCheck().isLimitedToValues(Arrays.asList("A", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failFindingTextSpanTextWithActionNameMatch() {

    // This test is designed to confirm that we can get information about
    // missing nested fields. We expect this test to show us that there are two
    // text fields that are null in a set of 6. The case we hope to avoid is
    // that we look at only the four that don't return null and report
    // everything as being fine.
    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath
        .using(config)
        .parse(
            "{\"modules\":[{\"action\":{\"name\":\"TARGET\",\"textSpan\":[{\"text\":\"A\"},{\"text\":\"B\"}]}},{\"action\":{\"name\":\"TARGET\",\"textSpan\":[{\"text\":\"Y\"},{\"text\":\"Z\"}]}},{\"action\":{\"name\":\"TARGET\",\"textSpan\":[{\"style\":\"BOLD\"},{\"style\":\"UNDERSCORE\"}]}}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.modules[?(@.action.name == \"TARGET\")].action.textSpan[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failFindingTextSpanText() {

    // This test is designed to confirm that we can get information about
    // missing nested fields. We expect this test to show us that there are two
    // text fields that are null in a set of 6. The case we hope to avoid is
    // that we look at only the four that don't return null and report
    // everything as being fine.
    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath
        .using(config)
        .parse(
            "{\"modules\":[{\"action\":{\"name\":\"TARGET\",\"textSpan\":[{\"text\":\"A\"},{\"text\":\"B\"}]}},{\"action\":{\"name\":\"TARGET\",\"textSpan\":[{\"text\":\"Y\"},{\"text\":\"Z\"}]}},{\"action\":{\"name\":\"TARGET\",\"textSpan\":[{\"style\":\"BOLD\"},{\"style\":\"UNDERSCORE\"}]}}]}");
    JPListOfStringCheck check = new JPListOfStringCheck();
    check.processJsonPath("$.modules[*].action.textSpan[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }
}
