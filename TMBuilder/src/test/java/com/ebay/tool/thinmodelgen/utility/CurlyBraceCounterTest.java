package com.ebay.tool.thinmodelgen.utility;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;

public class CurlyBraceCounterTest {

  @DataProvider(name = "counterNoBraceStrings")
  public Object[] getCounterNoBraceStrings() {
    return new String[] {
        "[$&+,:;=?@#|/'<>.^*()%!-01234567890]",
        " ",
        "\n\t"
    };
  }

  @Test(dataProvider = "counterNoBraceStrings", groups = "unitTest")
  public void testReadLineCountNoBraces(String value) {
    CurlyBraceCounter testCounter = new CurlyBraceCounter();
    testCounter.readLine(value);
    assertThat(String.format("CurlyBraceCounter should be empty when no braces have been passed. Value: %s", value), testCounter.isBraceCountEmpty());
  }

  @Test(groups = unitTest)
  public void testReadLineCountBraceOpen() {
    CurlyBraceCounter testCounter = new CurlyBraceCounter();
    testCounter.readLine("{");
    assertThat("CurlyBraceCounter should not be empty when open brace is passed.", testCounter.isBraceCountNotEmpty());
  }

  @Test(expectedExceptions = IllegalStateException.class, groups = "unitTest")
  public void testReadLineCountBraceClosed() {
    CurlyBraceCounter testCounter = new CurlyBraceCounter();
    testCounter.readLine("}");
  }

  @Test(expectedExceptions = IllegalStateException.class, groups = "unitTest")
  public void testReadLineCountBraceClosedAfterCompletedBrace() {
    CurlyBraceCounter testCounter = new CurlyBraceCounter();
    testCounter.readLine("{");
    testCounter.readLine("}");
    testCounter.readLine("}");
  }

  @Test(groups = unitTest)
  public void testReadMultipleLinesWithBraces() {
    CurlyBraceCounter testCounter = new CurlyBraceCounter();
    testCounter.readLine("{");
    testCounter.readLine("test");
    testCounter.readLine("}");
    assertThat("CurlyBraceCounter should be empty when open and closed braces are passed.", testCounter.isBraceCountEmpty());
  }

  @Test(groups = unitTest)
  public void testReadBothBracketsSameLine() {
    CurlyBraceCounter testCounter = new CurlyBraceCounter();
    testCounter.readLine("{}");
    assertThat("CurlyBraceCounter should be empty when open and closed braces are passed on same line.", testCounter.isBraceCountEmpty());
  }

}
