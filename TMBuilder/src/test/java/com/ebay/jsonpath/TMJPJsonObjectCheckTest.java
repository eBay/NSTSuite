package com.ebay.jsonpath;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

public class TMJPJsonObjectCheckTest {

  private static final Configuration config = Configuration.defaultConfiguration().setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

  @Test(groups = unitTest)
  public void passWithNonNullObject() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithNullObject() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":null}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithClassCastException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":[{\"first\":\"A\"},{\"second\":2},{\"third\":true}]}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck();
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failPathNotFoundException() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck();
    check.processJsonPath("$.bar.first", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithExpectedNumberOfKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().hasNumberOfKeys(3);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithUnexpectedNumberOfKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().hasNumberOfKeys(2);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithExpectedKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().keysAreEqualTo(Arrays.asList("first","second","third"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithUnexpectedKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().keysAreEqualTo(Arrays.asList("first","third","fourth"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithContainsKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().keysContain(Arrays.asList("first","second"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithDoesNotContainKeys() {

    SoftAssert softAssert = new SoftAssert();

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().keysContain(Arrays.asList("fourth","second"));
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithExpectedMap() {

    SoftAssert softAssert = new SoftAssert();

    LinkedHashMap<String, Object> expectedMap = new LinkedHashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("second", 2);
    expectedMap.put("third", true);

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().isEqualTo(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithUnexpectedMap() {

    SoftAssert softAssert = new SoftAssert();

    LinkedHashMap<String, Object> expectedMap = new LinkedHashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("second", 2);
    expectedMap.put("fourth", "B");

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().isEqualTo(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void passWithContainsMap() {

    SoftAssert softAssert = new SoftAssert();

    HashMap<String, Object> expectedMap = new HashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("second", 2);

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().contains(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(expectedExceptions = AssertionError.class, groups = "unitTest")
  public void failWithDoesNotContainMap() {

    SoftAssert softAssert = new SoftAssert();

    HashMap<String, Object> expectedMap = new HashMap<>();
    expectedMap.put("first", "A");
    expectedMap.put("fail", 2);

    DocumentContext jsonPathDocument = JsonPath.using(config).parse("{\"foo\":{\"first\":\"A\",\"second\":2,\"third\":true}}");
    TMJPJsonObjectCheck check = new TMJPJsonObjectCheck().contains(expectedMap);
    check.processJsonPath("$.foo", softAssert, jsonPathDocument);

    softAssert.assertAll();
  }

  @Test(groups = unitTest)
  public void thinModelExportCheck() {

    ThinModelSerializer serializer = new TMJPJsonObjectCheck();
    String serialized = serializer.getJavaStatements();
    MatcherAssert.assertThat("Serialized variant must match expected.", serialized, Matchers.is(Matchers.equalTo("new JPJsonObjectCheck()")));
  }

  @Test(groups = unitTest)
  public void convertDefaultJPJsonObjectCheckToTMJPJsonObjectCheck() {

    JPJsonObjectCheck original = new JPJsonObjectCheck();
    TMJPJsonObjectCheck clone = new TMJPJsonObjectCheck(original);

    assertThat("Clone MUST have expected number of keys set to null.", clone.getExpectedNumberOfKeys(), is(nullValue()));
    assertThat("Clone MUST have expected keys set to null.", clone.getExpectedKeys(), is(nullValue()));
    assertThat("Clone MUST have contains keys set to null.", clone.getContainsKeys(), is(nullValue()));
    assertThat("Clone MUST have expected map set to null.", clone.getExpectedMap(), is(nullValue()));
    assertThat("Clone MUST have contains map set to null.", clone.getContainsMap(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void convertJPJsonObjectCheckToTMJPJsonObjectCheck() {

    List<String> expectedKeys = Arrays.asList("one", "two", "three");
    List<String> containsKeys = Arrays.asList("apple", "banana");
    LinkedHashMap<String, Object> expectedMap = new LinkedHashMap<>();
    expectedMap.put("test", "FOO");
    expectedMap.put("TEST2", 35);
    HashMap<String, Object> containsMap = new HashMap<>();
    containsMap.put("CONTAINS", "VALUE");

    JPJsonObjectCheck original = new JPJsonObjectCheck().hasNumberOfKeys(3).keysAreEqualTo(expectedKeys).keysContain(containsKeys).isEqualTo(expectedMap).contains(containsMap);
    TMJPJsonObjectCheck clone = new TMJPJsonObjectCheck(original);

    assertThat("Clone MUST have expected number of keys set to expected.", clone.getExpectedNumberOfKeys(), is(equalTo(3)));
    assertThat("Clone MUST have expected keys set to expected.", clone.getExpectedKeys(), is(equalTo(expectedKeys)));
    assertThat("Clone MUST have contains keys set to expected.", clone.getContainsKeys(), is(equalTo(containsKeys)));
    assertThat("Clone MUST have expected map set to expected.", clone.getExpectedMap(), is(equalTo(expectedMap)));
    assertThat("Clone MUST have contains map set to expected.", clone.getContainsMap(), is(equalTo(containsMap)));
  }
}
