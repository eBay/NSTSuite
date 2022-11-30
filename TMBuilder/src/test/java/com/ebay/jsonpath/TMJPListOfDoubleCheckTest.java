package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ebay.tool.thinmodelgen.gui.menu.export.ThinModelSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class TMJPListOfDoubleCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":2.5}]}}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":2.5}]}}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":2.5},{\"sibling\":1.5},{\"child\":3.5}]}}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2.0}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"1.0\"},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0,2.5,3.14));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0,3.14,2.5));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().contains(Arrays.asList(1.0,2.5));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().contains(Arrays.asList(0.0,4.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.5},{\"bar\":3.14}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":1.0},{\"bar\":1.0}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasAllValuesEqualTo(1.0);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2.0},{\"bar\":1.0}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().hasAllValuesEqualTo(1.0);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passConvertingListOfIntegerToListOfDouble() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0, 2.0, 3.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passConvertingListOfIntegerToListOfDoubleWithListContainingADouble() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3.0}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0, 2.0, 3.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passConvertingListOfIntegerToListOfDoubleWithListContainingADoubleAsFirstValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1.0},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfDoubleCheck check = new TMJPListOfDoubleCheck().isEqualTo(Arrays.asList(1.0, 2.0, 3.0));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPListOfDoubleCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(3.1415926535,2.00000)).contains(Arrays.asList(1.0000)).hasAllValuesEqualTo(4.0001);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPListOfDoubleCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(3.1415926535,2.0)).contains(Arrays.asList(1.0)).hasAllValuesEqualTo(4.0001)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPListOfDoubleCheckToTMJPListOfDoubleCheck() {

    JPListOfDoubleCheck original = new JPListOfDoubleCheck();
    TMJPListOfDoubleCheck clone = new TMJPListOfDoubleCheck(original);

    assertThat("Clone MUST have length set to null.", clone.getHasLength(), is(nullValue()));
    assertThat("Clone MUST have max length set to null.", clone.getMaxLength(), is(nullValue()));
    assertThat("Clone MUST have min length set to null.", clone.getMinLength(), is(nullValue()));
    assertThat("Clone MUST have contains values set to null.", clone.getContainsValues(), is(nullValue()));
    assertThat("Clone MUST have is equal to values set to null.", clone.getIsEqualToValues(), is(nullValue()));
    assertThat("Clone MUST have all expected value set to null.", clone.getAllExpectedValue(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPListOfDoubleCheckToTMJPListOfDoubleCheck() {

    int hasLength = 2;
    int maxLength = 5;
    int minLength = 1;
    List<Double> containsValues = Arrays.asList(2.0, 150.5, 3.125, 0.1);
    List<Double> equalToValues = Arrays.asList(1.0, 2.0, 3.0, 5.9);
    double allSetTo = 3.5;

    JPListOfDoubleCheck original = new JPListOfDoubleCheck().hasLength(hasLength).hasMaxLength(maxLength).hasMinLength(minLength).contains(containsValues).isEqualTo(equalToValues).hasAllValuesEqualTo(allSetTo);
    TMJPListOfDoubleCheck clone = new TMJPListOfDoubleCheck(original);

    assertThat("Clone MUST have length set to expected.", clone.getHasLength(), is(equalTo(hasLength)));
    assertThat("Clone MUST have max length set to expected.", clone.getMaxLength(), is(equalTo(maxLength)));
    assertThat("Clone MUST have min length set to expected.", clone.getMinLength(), is(equalTo(minLength)));
    assertThat("Clone MUST have contains values set to expected.", clone.getContainsValues(), is(equalTo(containsValues)));
    assertThat("Clone MUST have is equal to values set to expected.", clone.getIsEqualToValues(), is(equalTo(equalToValues)));
    assertThat("Clone MUST have all expected value set to expected.", clone.getAllExpectedValue(), is(equalTo(allSetTo)));
  }
}
