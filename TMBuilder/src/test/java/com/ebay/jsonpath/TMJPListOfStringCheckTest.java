package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.List;

import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class TMJPListOfStringCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passNonNullValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":null}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNullMissingNode() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"value\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void failNullMissingFilterNode() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"value\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo[?(@.bar == \"nothing\")].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\": \"value\"}]}}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\": \"value\"}]}}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\": \"value\"},{\"child\": \"value\"},{\"sibling\": \"xxx\"}]}}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo.parent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":2}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passExactLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().hasLength(3);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failUnexpectedLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().hasLength(4);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMinLength() {
    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void faiMinLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMaxLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaxLength() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passValueMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().isEqualTo(Arrays.asList("A", "B", "C"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failValueMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().isEqualTo(Arrays.asList("A", "C", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().contains(Arrays.asList("A", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().contains(Arrays.asList("A", "D"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passIsLimitedToValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"A\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().isLimitedToValues(Arrays.asList("A", "B"));
    check.processJsonPath("$.foo[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failIsLimitedToValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"text\":\"A\"},{\"text\":\"B\"},{\"text\":\"C\"}]}");
    TMJPListOfStringCheck check = new TMJPListOfStringCheck().isLimitedToValues(Arrays.asList("A", "B"));
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
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
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
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    check.processJsonPath("$.modules[*].action.textSpan[*].text", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPListOfStringCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList("one","two")).contains(Arrays.asList("one")).hasAllValuesEqualTo("three").isLimitedToValues(Arrays.asList("one","two"));
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPListOfStringCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(\"one\",\"two\")).contains(Arrays.asList(\"one\")).hasAllValuesEqualTo(\"three\").isLimitedToValues(Arrays.asList(\"one\",\"two\"))")));
  }

  @Test(groups = unitTest)
  public void kotlinThinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPListOfStringCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList("one","two")).contains(Arrays.asList("one")).hasAllValuesEqualTo("three").isLimitedToValues(Arrays.asList("one","two"));
    String serialized = serializer.getKotlinStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("JPListOfStringCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(listOf(\"one\",\"two\")).contains(listOf(\"one\")).hasAllValuesEqualTo(\"three\").isLimitedToValues(listOf(\"one\",\"two\"))")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPListOfStringCheckToTMJPListOfStringCheck() {

    JPListOfStringCheck original = new JPListOfStringCheck();
    TMJPListOfStringCheck clone = new TMJPListOfStringCheck(original);

    assertThat("Clone MUST have length set to null.", clone.getHasLengthValue(), is(nullValue()));
    assertThat("Clone MUST have max length set to null.", clone.getMaxLength(), is(nullValue()));
    assertThat("Clone MUST have min length set to null.", clone.getMinLength(), is(nullValue()));
    assertThat("Clone MUST have contains values set to null.", clone.getContainsValues(), is(nullValue()));
    assertThat("Clone MUST have is equal to values set to null.", clone.getIsEqualToValues(), is(nullValue()));
    assertThat("Clone MUST have all expected value set to null.", clone.getAllExpectedValue(), is(nullValue()));
    assertThat("Clone MUST have all limited to values set to null.", clone.getLimitedToValues(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPListOfStringCheckToTMJPListOfStringCheck() {

    int hasLength = 2;
    int maxLength = 5;
    int minLength = 1;
    List<String> containsValues = Arrays.asList("apple", "banana", "pear");
    List<String> equalToValues = Arrays.asList("one", "two", "three", "four");
    String allSetTo = "MONSTOR";
    List<String> limitedToValues = Arrays.asList("green", "red");

    JPListOfStringCheck original = new JPListOfStringCheck().hasLength(hasLength).hasMaxLength(maxLength).hasMinLength(minLength).contains(containsValues).isEqualTo(equalToValues).hasAllValuesEqualTo(allSetTo).isLimitedToValues(limitedToValues);
    TMJPListOfStringCheck clone = new TMJPListOfStringCheck(original);

    assertThat("Clone MUST have length set to expected.", clone.getHasLengthValue(), is(equalTo(hasLength)));
    assertThat("Clone MUST have max length set to expected.", clone.getMaxLength(), is(equalTo(maxLength)));
    assertThat("Clone MUST have min length set to expected.", clone.getMinLength(), is(equalTo(minLength)));
    assertThat("Clone MUST have contains values set to expected.", clone.getContainsValues(), is(equalTo(containsValues)));
    assertThat("Clone MUST have is equal to values set to expected.", clone.getIsEqualToValues(), is(equalTo(equalToValues)));
    assertThat("Clone MUST have all expected value set to expected.", clone.getAllExpectedValue(), is(equalTo(allSetTo)));
    assertThat("Clone MUST have all limited to values set to null.", clone.getLimitedToValues(), is(equalTo(limitedToValues)));
  }

  @Test(groups = unitTest)
  public void initializeWithTMJPListOfStringCheck() {

    List<String> expectedValues = Arrays.asList("Bob", "Joe", "Nancy");
    TMJPListOfStringCheck original = new TMJPListOfStringCheck();
    original.setMockValues(expectedValues);

    TMJPListOfStringCheck clone = new TMJPListOfStringCheck(original);
    assertThat("Clone MUST have mock values set to expected.", clone.getMockValues(), is(equalTo(expectedValues)));
  }

  @Test
  public void getDeveloperMockType() {
    TMJPListOfStringCheck check = new TMJPListOfStringCheck();
    DeveloperMockType type = check.getMockType();
    assertThat(type, is(equalTo(DeveloperMockType.LIST_OF_STRING)));
  }
}
