package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.jayway.jsonpath.DocumentContext;

public class DoubleComponentEvaluator implements JsonPathExecutor {

  private Double value = 3.0;

  public void setValue(Double value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public Double getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    DoubleComponentEvaluator instance = new DoubleComponentEvaluator();
    Method setMethod = DoubleComponentEvaluator.class.getMethod("setValue", Double.class);
    Method getMethod = DoubleComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new DoubleComponent(new JsonFloatType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

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
