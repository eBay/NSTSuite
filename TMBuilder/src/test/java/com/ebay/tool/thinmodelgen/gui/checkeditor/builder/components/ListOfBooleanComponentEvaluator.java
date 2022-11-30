package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;
import java.util.List;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.jayway.jsonpath.DocumentContext;

public class ListOfBooleanComponentEvaluator implements JsonPathExecutor {

  private List<Boolean> value = null;

  public void setValue(List<Boolean> value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public List<Boolean> getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    ListOfBooleanComponentEvaluator instance = new ListOfBooleanComponentEvaluator();
    Method setMethod = ListOfBooleanComponentEvaluator.class.getMethod("setValue", List.class);
    Method getMethod = ListOfBooleanComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new ListOfBooleanComponent(new JsonBooleanType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

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
