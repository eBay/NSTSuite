package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;
import java.util.List;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import com.jayway.jsonpath.DocumentContext;

public class ListOfStringComponentEvaluator implements JsonPathExecutor {

  private List<String> value = null;

  public void setValue(List<String> value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public List<String> getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    ListOfStringComponentEvaluator instance = new ListOfStringComponentEvaluator();
    Method setMethod = ListOfStringComponentEvaluator.class.getMethod("setValue", List.class);
    Method getMethod = ListOfStringComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new ListOfStringComponent(new JsonStringType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

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
