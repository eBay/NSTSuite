package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.List;

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

public class TMJPListOfIntegerCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failNotNull() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":null},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":3}]}}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo.nonexistent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":3}]}}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo.parent[*].nonexistent", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failLeafNodeInArrayNotFound() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"parent\":[{\"child\":3},{\"sibling\":2},{\"child\":4}]}}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo.parent[*].child", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":2}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListItemClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":\"1\"},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().isEqualTo(Arrays.asList(1,2,3));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failExpectedList() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().isEqualTo(Arrays.asList(1,3,2));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().contains(Arrays.asList(1,2));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failContainsValues() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().contains(Arrays.asList(0,4));
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failListLengthCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasMinLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMinimumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasMinLength(4);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasMaxLength(3);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failMaximumSizeCheck() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":3}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasMaxLength(2);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":1},{\"bar\":1}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasAllValuesEqualTo(1);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failAllValuesEqualToSpecificValue() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"bar\":1},{\"bar\":2},{\"bar\":1}]}");
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck().hasAllValuesEqualTo(1);
    check.processJsonPath("$.foo[*].bar", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPListOfIntegerCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(1,2)).contains(Arrays.asList(1)).hasAllValuesEqualTo(4);
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPListOfIntegerCheck().hasLength(2).hasMinLength(1).hasMaxLength(3).isEqualTo(Arrays.asList(1,2)).contains(Arrays.asList(1)).hasAllValuesEqualTo(4)")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPListOfIntegerCheckToTMJPListOfIntegerCheck() {

    JPListOfIntegerCheck original = new JPListOfIntegerCheck();
    TMJPListOfIntegerCheck clone = new TMJPListOfIntegerCheck(original);

    assertThat("Clone MUST have length set to null.", clone.getHasLength(), is(nullValue()));
    assertThat("Clone MUST have max length set to null.", clone.getMaxLength(), is(nullValue()));
    assertThat("Clone MUST have min length set to null.", clone.getMinLength(), is(nullValue()));
    assertThat("Clone MUST have contains values set to null.", clone.getContainsValues(), is(nullValue()));
    assertThat("Clone MUST have is equal to values set to null.", clone.getIsEqualToValues(), is(nullValue()));
    assertThat("Clone MUST have all expected value set to null.", clone.getAllExpectedValue(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPListOfIntegerCheckToTMJPListOfIntegerCheck() {

    int hasLength = 2;
    int maxLength = 5;
    int minLength = 1;
    List<Integer> containsValues = Arrays.asList(2, 150, 3, 0);
    List<Integer> equalToValues = Arrays.asList(1, 2, 3, 5);
    int allSetTo = 3;

    JPListOfIntegerCheck original = new JPListOfIntegerCheck().hasLength(hasLength).hasMaxLength(maxLength).hasMinLength(minLength).contains(containsValues).isEqualTo(equalToValues).hasAllValuesEqualTo(allSetTo);
    TMJPListOfIntegerCheck clone = new TMJPListOfIntegerCheck(original);

    assertThat("Clone MUST have length set to expected.", clone.getHasLength(), is(equalTo(hasLength)));
    assertThat("Clone MUST have max length set to expected.", clone.getMaxLength(), is(equalTo(maxLength)));
    assertThat("Clone MUST have min length set to expected.", clone.getMinLength(), is(equalTo(minLength)));
    assertThat("Clone MUST have contains values set to expected.", clone.getContainsValues(), is(equalTo(containsValues)));
    assertThat("Clone MUST have is equal to values set to expected.", clone.getIsEqualToValues(), is(equalTo(equalToValues)));
    assertThat("Clone MUST have all expected value set to expected.", clone.getAllExpectedValue(), is(equalTo(allSetTo)));
  }

  @Test(groups = unitTest)
  public void initializeWithTMJPListOfIntegerCheck() {

    List<Integer> expectedValues = Arrays.asList(1, 2, 30);
    TMJPListOfIntegerCheck original = new TMJPListOfIntegerCheck();
    original.setMockValues(expectedValues);

    TMJPListOfIntegerCheck clone = new TMJPListOfIntegerCheck(original);
    assertThat("Clone MUST have mock values set to expected.", clone.getMockValues(), is(equalTo(expectedValues)));
  }

  @Test
  public void getDeveloperMockType() {
    TMJPListOfIntegerCheck check = new TMJPListOfIntegerCheck();
    DeveloperMockType type = check.getMockType();
    assertThat(type, is(equalTo(DeveloperMockType.LIST_OF_INTEGER)));
  }
}
