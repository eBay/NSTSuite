package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.lang.reflect.Method;
import java.util.List;

import javax.swing.SwingUtilities;

import org.testng.asserts.SoftAssert;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.jayway.jsonpath.DocumentContext;

public class ListOfIntegerComponentEvaluator implements JsonPathExecutor {

  private List<Integer> value = null;

  public void setValue(List<Integer> value) {
    this.value = value;
    System.out.println("Value set to: " + value);
  }

  public List<Integer> getValue() {
    System.out.println("Get value " + value);
    return value;
  }

  public static void main(String[] args) throws Throwable {

    ListOfIntegerComponentEvaluator instance = new ListOfIntegerComponentEvaluator();
    Method setMethod = ListOfIntegerComponentEvaluator.class.getMethod("setValue", List.class);
    Method getMethod = ListOfIntegerComponentEvaluator.class.getMethod("getValue");

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new ListOfIntegerComponent(new JsonBooleanType("foo"), "", "input value", "Input a value to save it", instance, setMethod, getMethod));

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
