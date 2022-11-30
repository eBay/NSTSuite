package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.jayway.jsonpath.DocumentContext;

public class BooleanComponentEvaluator implements JsonPathExecutor {

  private Boolean value = true;

  public void setValue(Boolean value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public Boolean getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    BooleanComponentEvaluator instance = new BooleanComponentEvaluator();
    Method setMethod = BooleanComponentEvaluator.class.getMethod("setValue", Boolean.class);
    Method getMethod = BooleanComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new BooleanComponent(new JsonBooleanType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mainFrame.setVisible(true);
      }
    });
  }

  @Override
  public void processJsonPath(String jsonPath, SoftAssert softAssert, DocumentContext documentContext) {

  }
}
