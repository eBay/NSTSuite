package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.jayway.jsonpath.DocumentContext;

public class IntegerComponentEvaluator implements JsonPathExecutor {

  private Integer value = 3;

  public void setValue(Integer value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public Integer getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    IntegerComponentEvaluator instance = new IntegerComponentEvaluator();
    Method setMethod = IntegerComponentEvaluator.class.getMethod("setValue", Integer.class);
    Method getMethod = IntegerComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new IntegerComponent(new JsonIntegerType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

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
