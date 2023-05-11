package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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

public class TMJPIntegerCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithIntegerNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    TMJPIntegerCheck check = new TMJPIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithIntegerNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    TMJPIntegerCheck check = new TMJPIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithIntegerMismatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    TMJPIntegerCheck check = new TMJPIntegerCheck().isEqualTo(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithIntegerMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    TMJPIntegerCheck check = new TMJPIntegerCheck().isEqualTo(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"2\"}");
    TMJPIntegerCheck check = new TMJPIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\": {\"bar\":1}}");
    TMJPIntegerCheck check = new TMJPIntegerCheck();
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPIntegerCheck().isEqualTo(42);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPIntegerCheck().isEqualTo(42)")));
  }

  @Test(groups = unitTest)
  public void kotlinThinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPIntegerCheck().isEqualTo(42);
    String serialized = serializer.getKotlinStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("JPIntegerCheck().isEqualTo(42)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPIntegerCheckToTMJPIntegerCheck() {

    JPIntegerCheck original = new JPIntegerCheck();
    TMJPIntegerCheck clone = new TMJPIntegerCheck(original);

    assertThat("Clone MUST have equal check field set to null.", clone.getExpectedValue(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPIntegerCheckToTMJPIntegerCheck() {

    JPIntegerCheck original = new JPIntegerCheck().isEqualTo(5);
    TMJPIntegerCheck clone = new TMJPIntegerCheck(original);

    assertThat("Clone MUST have equal check field set to expected.", clone.getExpectedValue(), is(equalTo(5)));
  }

  @Test(groups = unitTest)
  public void initializeWithTMJPIntegerCheck() {

    TMJPIntegerCheck original = new TMJPIntegerCheck();
    original.setMockValue(11);
    original.isEqualTo(5);

    TMJPIntegerCheck clone = new TMJPIntegerCheck(original);
    assertThat("Clone MUST have equal check field set to expected.", clone.getExpectedValue(), is(equalTo(5)));
    assertThat("Clone MUST have mock value set to expected.", clone.getMockValue(), is(equalTo(11)));
  }

  @Test
  public void getDeveloperMockType() {
    TMJPIntegerCheck check = new TMJPIntegerCheck();
    DeveloperMockType type = check.getMockType();
    assertThat(type, is(equalTo(DeveloperMockType.INTEGER)));
  }
}
