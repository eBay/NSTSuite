package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.ebay.tool.thinmodelgen.gui.menu.export.DeveloperMockType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class TMJPDoubleCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passWithDoubleNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2.0}");
    TMJPDoubleCheck check = new TMJPDoubleCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithDoubleNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    TMJPDoubleCheck check = new TMJPDoubleCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithValueMatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2.0}");
    TMJPDoubleCheck check = new TMJPDoubleCheck().isEqualTo(2.0);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithValueMismatch() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2.0}");
    TMJPDoubleCheck check = new TMJPDoubleCheck().isEqualTo(12.1);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passConvertingIntegerToDouble() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":1}");
    TMJPDoubleCheck check = new TMJPDoubleCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"2.0\"}");
    TMJPDoubleCheck check = new TMJPDoubleCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"bar\":2.0}}");
    TMJPDoubleCheck check = new TMJPDoubleCheck();
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPDoubleCheck().isEqualTo(3.014159000);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPDoubleCheck().isEqualTo(3.014159)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPDoubleCheckToTMJPDoubleCheck() {

    JPDoubleCheck original = new JPDoubleCheck();
    TMJPDoubleCheck clone = new TMJPDoubleCheck(original);

    assertThat("Clone MUST have equal check field set to null.", clone.getExpectedValue(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPDoubleCheckToTMJPDoubleCheck() {

    JPDoubleCheck original = new JPDoubleCheck().isEqualTo(5.5);
    TMJPDoubleCheck clone = new TMJPDoubleCheck(original);

    assertThat("Clone MUST have equal check field set to expected.", clone.getExpectedValue(), is(equalTo(5.5)));
  }

  @Test(groups = unitTest)
  public void initializeWithTMJPDoubleCheck() {

    TMJPDoubleCheck original = new TMJPDoubleCheck();
    original.setMockValue(11.0);
    original.isEqualTo(5.5);

    TMJPDoubleCheck clone = new TMJPDoubleCheck(original);
    assertThat("Clone MUST have equal check field set to expected.", clone.getExpectedValue(), is(equalTo(5.5)));
    assertThat("Clone MUST have mock value set to expected.", clone.getMockValue(), is(equalTo(11.0)));
  }

  @Test
  public void getDeveloperMockType() {
    TMJPDoubleCheck check = new TMJPDoubleCheck();
    DeveloperMockType type = check.getMockType();
    assertThat(type, is(equalTo(DeveloperMockType.DOUBLE)));
  }
}
