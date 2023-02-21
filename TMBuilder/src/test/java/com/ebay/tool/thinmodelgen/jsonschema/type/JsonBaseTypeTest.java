package com.ebay.tool.thinmodelgen.jsonschema.type;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.awt.Component;

import org.testng.annotations.Test;

public class JsonBaseTypeTest {

  @SuppressWarnings("serial")
  JsonBaseType jsonBaseType = new JsonBaseType("", "","") {

    @Override
    public JsonType getJsonType() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Component getCheckEditorComponent(String jsonPath) {
      // TODO Auto-generated method stub
      return null;
    }
  };

  @Test(groups = unitTest)
  public void jsonPathWithoutArrayOrWildcardWillNotReturnList() {

    String path = "$.modules.paymentMethods";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must not report as returning a list.",  path), result, is(equalTo(false)));
  }

  @Test(groups = unitTest)
  public void jsonPathWithArrayWithWildCardsWillReturnList() {

    String path = "$.modules.paymentMethods.paymentMethods[*].action";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must report as returning a list.",  path), result, is(equalTo(true)));
  }

  @Test(groups = unitTest)
  public void jsonPathWithArrayWithIndexesWillNotReturnList() {

    String path = "$.modules.paymentMethods.paymentMethods[10].entries[0].action";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must not report as returning a list.",  path), result, is(equalTo(false)));
  }

  @Test(groups = unitTest)
  public void jsonPathWithArrayFilterWillReturnList() {

    String path = "$.modules.paymentMethods.paymentMethods[?(@.entries.action.name == \"TEST\")].action";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must report as returning a list.",  path), result, is(equalTo(true)));
  }

  @Test(groups = unitTest)
  public void jsonPathWithWildcardWillReturnList() {

    String path = "$.modules.[*].paymentMethods.entries.action";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must report as returning a list.",  path), result, is(equalTo(true)));
  }

  @Test(groups = unitTest)
  public void jsonPathWithMultipleWildcardsWillReturnList() {

    String path = "$.modules.[*].paymentMethods.[*].action";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must report as returning a list.",  path), result, is(equalTo(true)));
  }

  @Test(groups = unitTest)
  public void jsonPathWithWildcardAndArrayWillReturnList() {

    String path = "$.modules.*.paymentMethods.entries[*].action[0]";
    boolean result = jsonBaseType.willJsonPathReturnListOfResults(path);
    assertThat(String.format("Path %s must report as returning a list.",  path), result, is(equalTo(true)));
  }
}
