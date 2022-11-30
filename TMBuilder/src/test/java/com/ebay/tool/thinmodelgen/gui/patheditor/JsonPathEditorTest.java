package com.ebay.tool.thinmodelgen.gui.patheditor;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.testng.annotations.Test;

public class JsonPathEditorTest {

  JsonPathEditor editor = new JsonPathEditor();

  @Test(groups = unitTest)
  public void cleanEmptyJsonPath() {
    String result = editor.cleanJsonPath("");
    assertThat("Empty path should yield empty result.", result, is(equalTo("")));
  }

  @Test(groups = unitTest)
  public void cleanNullJsonPath() {
    String result = editor.cleanJsonPath(null);
    assertThat("Null path should yield null result.", result, is(nullValue()));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithoutBrackets() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods")));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithArray() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods.paymentMethods[]");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods.paymentMethods")));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithArrays() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods.paymentMethods[0].entries[*]");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods.paymentMethods.entries")));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithFilter() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods.paymentMethods[?(@.entries.action.name == \"FOO\")]");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods.paymentMethods")));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithFilters() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods.paymentMethods[?(@.entries.action.name == \"FOO\")].entries[?(@.action.name == \"FOO\")]");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods.paymentMethods.entries")));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithFunction() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods.paymentMethods.length()");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods.paymentMethods")));
  }

  @Test(groups = unitTest)
  public void cleanJsonPathWithFiltersAndFunctions() {
    String result = editor.cleanJsonPath("$.modules.paymentMethods.paymentMethods[?(@.entries.action.name == \"FOO\")].entries[?(@.action.name == \"FOO\")].length()");
    assertThat("Path returned does not match expected.", result, is(equalTo("$.modules.paymentMethods.paymentMethods.entries")));
  }
}
