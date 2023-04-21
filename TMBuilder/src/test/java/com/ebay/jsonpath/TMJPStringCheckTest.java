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

public class TMJPStringCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    TMJPStringCheck check = new TMJPStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    TMJPStringCheck check = new TMJPStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    TMJPStringCheck check = new TMJPStringCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\": {\"bar\":\"VALUE\"}}");
    TMJPStringCheck check = new TMJPStringCheck();
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passExpectedOneOfString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    TMJPStringCheck check = new TMJPStringCheck().isEqualToOneOf(Arrays.asList("value", "otherValue"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedOneOfString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    TMJPStringCheck check = new TMJPStringCheck().isEqualToOneOf(Arrays.asList("otherValue", "anotherValue"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passExpectedString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    TMJPStringCheck check = new TMJPStringCheck().isEqualTo("value");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value\"}");
    TMJPStringCheck check = new TMJPStringCheck().isEqualTo("bar");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passContainsString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value in the middle\"}");
    TMJPStringCheck check = new TMJPStringCheck().contains("in the");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsString() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"value in the middle\"}");
    TMJPStringCheck check = new TMJPStringCheck().contains("expected string");
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passStringLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    TMJPStringCheck check = new TMJPStringCheck().hasLength(4);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failStringLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    TMJPStringCheck check = new TMJPStringCheck().hasLength(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMinimumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    TMJPStringCheck check = new TMJPStringCheck().hasMinimumNumberOfCharacters(4);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    TMJPStringCheck check = new TMJPStringCheck().hasMinimumNumberOfCharacters(5);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMaximumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    TMJPStringCheck check = new TMJPStringCheck().hasMaximumNumberOfCharacters(4);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumCharacterCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"four\"}");
    TMJPStringCheck check = new TMJPStringCheck().hasMaximumNumberOfCharacters(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPStringCheck().hasLength(3).isEqualTo("one").contains("on").hasMinimumNumberOfCharacters(1).hasMaximumNumberOfCharacters(3);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPStringCheck().hasLength(3).isEqualTo(\"one\").contains(\"on\").hasMinimumNumberOfCharacters(1).hasMaximumNumberOfCharacters(3)")));
  }

  @Test(groups = unitTest)
  public void kotlinThinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPStringCheck().hasLength(3).isEqualTo("one").contains("on").hasMinimumNumberOfCharacters(1).hasMaximumNumberOfCharacters(3);
    String serialized = serializer.getKotlinStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("JPStringCheck().hasLength(3).isEqualTo(\"one\").contains(\"on\").hasMinimumNumberOfCharacters(1).hasMaximumNumberOfCharacters(3)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPStringCheckToTMJPStringCheck() {

    JPStringCheck original = new JPStringCheck();
    TMJPStringCheck clone = new TMJPStringCheck(original);

    assertThat("Clone MUST have equal to expected value set to null.", clone.getIsEqualToExpectedValue(), is(nullValue()));
    assertThat("Clone MUST have equal to one of set to null.", clone.getIsEqualToOneOfExpectedValue(), is(nullValue()));
    assertThat("Clone MUST have contains value set to null.", clone.getContainsValue(), is(nullValue()));
    assertThat("Clone MUST have length set to null.", clone.getHasLengthExpectedValue(), is(nullValue()));
    assertThat("Clone MUST have maximum number of characters set to null.", clone.getMaximumNumberOfCharacters(), is(nullValue()));
    assertThat("Clone MUST have minimum number of characters set to null.", clone.getMinimumNumberOfCharacters(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPStringCheckToTMJPStringCheck() {

    String isEqualToExpected = "foo";
    List<String> isEqualToOneOf = Arrays.asList("one", "two", "three");
    String containsValue = "partial";
    int hasLength = 5;
    int maximumNumberOfCharacters = 10;
    int minimumNumberOfCharacters = 1;

    JPStringCheck original = new JPStringCheck().isEqualTo(isEqualToExpected).isEqualToOneOf(isEqualToOneOf).contains(containsValue).hasLength(hasLength).hasMaximumNumberOfCharacters(maximumNumberOfCharacters).hasMinimumNumberOfCharacters(minimumNumberOfCharacters);
    TMJPStringCheck clone = new TMJPStringCheck(original);

    assertThat("Clone MUST have equal to expected value set to null.", clone.getIsEqualToExpectedValue(), is(equalTo(isEqualToExpected)));
    assertThat("Clone MUST have equal to one of set to null.", clone.getIsEqualToOneOfExpectedValue(), is(equalTo(isEqualToOneOf)));
    assertThat("Clone MUST have contains value set to null.", clone.getContainsValue(), is(equalTo(containsValue)));
    assertThat("Clone MUST have length set to null.", clone.getHasLengthExpectedValue(), is(equalTo(hasLength)));
    assertThat("Clone MUST have maximum number of characters set to null.", clone.getMaximumNumberOfCharacters(), is(equalTo(maximumNumberOfCharacters)));
    assertThat("Clone MUST have minimum number of characters set to null.", clone.getMinimumNumberOfCharacters(), is(equalTo(minimumNumberOfCharacters)));
  }

  @Test(groups = unitTest)
  public void initializeWithTMJPStringCheck() {

    TMJPStringCheck original = new TMJPStringCheck();
    original.setMockValue("Bar");

    TMJPStringCheck clone = new TMJPStringCheck(original);
    assertThat("Clone MUST have mock value set to expected.", clone.getMockValue(), is(equalTo("Bar")));
  }

  @Test
  public void getDeveloperMockType() {
    TMJPStringCheck check = new TMJPStringCheck();
    DeveloperMockType type = check.getMockType();
    assertThat(type, is(equalTo(DeveloperMockType.STRING)));
  }
}
