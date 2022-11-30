package com.ebay.tool.thinmodelgen.utility;

import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MethodNameCheckerTest {

  @DataProvider(name = "invalidMethods")
  public Object[] getInvalidMethodValues() {
    return new String[] {
        null,
        "",
        " ",
        "01234567890",
        "$&+,:;=?@#|/'<>.^*()%!"
    };
  }

  @DataProvider(name = "validMethods")
  public Object[] getValidMethodValues() {
    return new String[] {
        "validFirst",
        "validsecond",
        "valid_with_underscores"
    };
  }

  @Test(dataProvider = "invalidMethods", groups = "unitTest")
  public void testInvalidMethodName(String value) {
    assertThat(String.format("MethodNameChecker should return false if invalid method name is passed. Value: %s", value),
        !MethodNameChecker.isValueValidMethodName(value));
  }

  @Test(dataProvider = "validMethods", groups = "unitTest")
  public void testValidMethodName(String value) {
    assertThat(String.format("MethodNameChecker should return true if valid method name is passed. Value: %s", value),
        MethodNameChecker.isValueValidMethodName(value));
  }

}
