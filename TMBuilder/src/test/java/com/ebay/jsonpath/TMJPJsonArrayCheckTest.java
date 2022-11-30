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

public class TMJPJsonArrayCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passWithNonNullArray() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithNullArray() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":\"bar\"}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck();
    check.processJsonPath("$.bar.bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithExpectedLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasLength(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithExpectedLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasLength(1);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithMinimumLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasMinLength(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithMinimumLengthCheckEqualToMinimum() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasMinLength(1);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithMinimumLengthCheckGreaterThanMinimum() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"},{\"bar\":\"B\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasMinLength(1);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithMaximumLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"},{\"bar\":\"B\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasMaxLength(1);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithMaximumLengthCheckEqualToMaximum() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"},{\"bar\":\"B\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasMaxLength(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithMaximumLengthCheckLessThanMaximum() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"A\"}]}");
    TMJPJsonArrayCheck check = new TMJPJsonArrayCheck().hasMaxLength(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPJsonArrayCheck().hasLength(3).hasMinLength(1).hasMaxLength(4);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPJsonArrayCheck().hasLength(3).hasMinLength(1).hasMaxLength(4)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPJsonArrayCheckToTMJPJsonArrayCheck() {

    JPJsonArrayCheck original = new JPJsonArrayCheck();
    TMJPJsonArrayCheck clone = new TMJPJsonArrayCheck(original);

    assertThat("Clone MUST have expected length set to null.", clone.getExpectedLength(), is(nullValue()));
    assertThat("Clone MUST have max length set to null.", clone.getMaxLength(), is(nullValue()));
    assertThat("Clone MUST have min length set to null.", clone.getMinLength(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPJsonArrayCheckToTMJPJsonArrayCheck() {

    JPJsonArrayCheck original = new JPJsonArrayCheck().hasLength(3).hasMaxLength(5).hasMinLength(1);
    TMJPJsonArrayCheck clone = new TMJPJsonArrayCheck(original);

    assertThat("Clone MUST have expected length set to expected.", clone.getExpectedLength(), is(equalTo(3)));
    assertThat("Clone MUST have max length set to expected.", clone.getMaxLength(), is(equalTo(5)));
    assertThat("Clone MUST have min length set to expected.", clone.getMinLength(), is(equalTo(1)));
  }
}
