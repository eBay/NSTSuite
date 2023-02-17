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

public class TMJPListOfBooleanCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":true}]}}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":true}]}}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"sibling\":true},{\"child\":false},{\"child\":true}]}}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":true}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"true\"},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().isEqualTo(Arrays.asList(true,false,true));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().isEqualTo(Arrays.asList(true,true,false));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().contains(Arrays.asList(false,true));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":true},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().contains(Arrays.asList(false,false));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":true},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasAllValuesEqualTo(true);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":true},{\"bar\":false},{\"bar\":true}]}");
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck().hasAllValuesEqualTo(false);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPListOfBooleanCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(true,false)).contains(Arrays.asList(true)).hasAllValuesEqualTo(false);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPListOfBooleanCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(true,false)).contains(Arrays.asList(true)).hasAllValuesEqualTo(false)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPListOfBooleanCheckToTMJPListOfBooleanCheck() {

    JPListOfBooleanCheck original = new JPListOfBooleanCheck();
    TMJPListOfBooleanCheck clone = new TMJPListOfBooleanCheck(original);

    assertThat("Clone MUST have length set to null.", clone.getHasLength(), is(nullValue()));
    assertThat("Clone MUST have max length set to null.", clone.getMaxLength(), is(nullValue()));
    assertThat("Clone MUST have min length set to null.", clone.getMinLength(), is(nullValue()));
    assertThat("Clone MUST have contains values set to null.", clone.getContainsValues(), is(nullValue()));
    assertThat("Clone MUST have is equal to values set to null.", clone.getIsEqualToValues(), is(nullValue()));
    assertThat("Clone MUST have all expected value set to null.", clone.getAllExpectedValue(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPListOfBooleanCheckToTMJPListOfBooleanCheck() {

    int hasLength = 2;
    int maxLength = 5;
    int minLength = 1;
    List<Boolean> containsValues = Arrays.asList(true, false, true, true);
    List<Boolean> equalToValues = Arrays.asList(true, true, true, false, false);
    boolean allSetTo = false;

    JPListOfBooleanCheck original = new JPListOfBooleanCheck().hasLength(hasLength).hasMaxLength(maxLength).hasMinLength(minLength).contains(containsValues).isEqualTo(equalToValues).hasAllValuesEqualTo(allSetTo);
    TMJPListOfBooleanCheck clone = new TMJPListOfBooleanCheck(original);

    assertThat("Clone MUST have length set to expected.", clone.getHasLength(), is(equalTo(hasLength)));
    assertThat("Clone MUST have max length set to expected.", clone.getMaxLength(), is(equalTo(maxLength)));
    assertThat("Clone MUST have min length set to expected.", clone.getMinLength(), is(equalTo(minLength)));
    assertThat("Clone MUST have contains values set to expected.", clone.getContainsValues(), is(equalTo(containsValues)));
    assertThat("Clone MUST have is equal to values set to expected.", clone.getIsEqualToValues(), is(equalTo(equalToValues)));
    assertThat("Clone MUST have all expected value set to expected.", clone.getAllExpectedValue(), is(equalTo(allSetTo)));
  }

  @Test(groups = unitTest)
  public void initializeWithTMJPListOfBooleanCheck() {

    List<Boolean> expectedValues = Arrays.asList(false, true, false);
    TMJPListOfBooleanCheck original = new TMJPListOfBooleanCheck();
    original.setMockValues(expectedValues);

    TMJPListOfBooleanCheck clone = new TMJPListOfBooleanCheck(original);
    assertThat("Clone MUST have mock values set to expected.", clone.getMockValues(), is(equalTo(expectedValues)));
  }

  @Test
  public void getDeveloperMockType() {
    TMJPListOfBooleanCheck check = new TMJPListOfBooleanCheck();
    DeveloperMockType type = check.getMockType();
    assertThat(type, is(equalTo(DeveloperMockType.LIST_OF_BOOLEAN)));
  }
}
