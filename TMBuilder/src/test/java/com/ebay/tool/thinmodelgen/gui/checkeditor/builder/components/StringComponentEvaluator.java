package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import com.jayway.jsonpath.DocumentContext;

public class StringComponentEvaluator implements JsonPathExecutor {

  private String value = "the quick brown fox";

  public void setValue(String value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public String getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    StringComponentEvaluator instance = new StringComponentEvaluator();
    Method setMethod = StringComponentEvaluator.class.getMethod("setValue", String.class);
    Method getMethod = StringComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new StringComponent(new JsonStringType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mainFrame.setVisible(true);
      }
    });
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {
    // TODO Auto-generated method stub

  }
}
