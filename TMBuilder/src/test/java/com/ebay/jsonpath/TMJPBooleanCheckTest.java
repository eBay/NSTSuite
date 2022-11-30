package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class TMJPBooleanCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passWithBoolNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":true}");
    TMJPBooleanCheck check = new TMJPBooleanCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithBoolNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    TMJPBooleanCheck check = new TMJPBooleanCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithCorrectValueCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":true}");
    TMJPBooleanCheck check = new TMJPBooleanCheck().isEqualTo(true);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithWrongValueCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":true}");
    TMJPBooleanCheck check = new TMJPBooleanCheck().isEqualTo(false);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"true\"}");
    TMJPBooleanCheck check = new TMJPBooleanCheck().isEqualTo(false);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"bar\":true}}");
    TMJPBooleanCheck check = new TMJPBooleanCheck().isEqualTo(false);
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPBooleanCheck().isEqualTo(true);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPBooleanCheck().isEqualTo(true)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPBooleanCheckToTMJPBooleanCheck() {

    JPBooleanCheck original = new JPBooleanCheck();
    TMJPBooleanCheck clone = new TMJPBooleanCheck(original);

    assertThat("Clone MUST have equal check field set to null.", clone.getExpectedValue(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPBooleanCheckToTMJPBooleanCheck() {

    JPBooleanCheck original = new JPBooleanCheck().isEqualTo(true);
    TMJPBooleanCheck clone = new TMJPBooleanCheck(original);

    assertThat("Clone MUST have equal check field set to expected.", clone.getExpectedValue(), is(equalTo(true)));
  }
}
