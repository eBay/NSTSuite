package com.ebay.softassert;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.testng.asserts.SoftAssert;

public class EbaySoftAssert extends SoftAssert {

  private SoftAssertMessageBuilder softAssertMessageBuilder;

  /**
   * Create a default instance.
   */
  public EbaySoftAssert() {
    this("");
  }

  /**
   * Specify the class name to ignore. EG: com.ebay.softassert.EbaySoftAssert.
   * @param ignoreClass Class to ignore. EG: com.ebay.softassert.EbaySoftAssert.
   */
  public EbaySoftAssert(String ignoreClass) {
    this(Arrays.asList(ignoreClass));
  }

  /**
   * Specify the list of class names to ignore. EG: com.ebay.softassert.EbaySoftAssert.
   * @param ignoreClasses List of classes to ignore. EG: com.ebay.softassert.EbaySoftAssert.
   */
  public EbaySoftAssert(List<String> ignoreClasses) {
    softAssertMessageBuilder = new SoftAssertMessageBuilder();

    for (String ignoreClass : ignoreClasses) {
      if (ignoreClass != null && ignoreClass != "") {
        softAssertMessageBuilder.ignoreClass(ignoreClass);
      }
    }

    softAssertMessageBuilder.ignoreClass(EbaySoftAssert.class.getName());
  }

  @Override
  public void assertTrue(boolean condition, String message) {
    super.assertTrue(condition, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertFalse(boolean condition, String message) {
    super.assertFalse(condition, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void fail(String message, Throwable realCause) {
    super.fail(softAssertMessageBuilder.buildMessage(message), realCause);
  }

  @Override
  public void fail(String message) {
    super.fail(softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public <T> void assertEquals(T actual, T expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(String actual, String expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(double actual, double expected, double delta, String message) {
    super.assertEquals(actual, expected, delta, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(float actual, float expected, float delta, String message) {
    super.assertEquals(actual, expected, delta, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(long actual, long expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(boolean actual, boolean expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(byte actual, byte expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(char actual, char expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(short actual, short expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(int actual, int expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotNull(Object object, String message) {
    super.assertNotNull(object, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNull(Object object, String message) {
    super.assertNull(object, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertSame(Object actual, Object expected, String message) {
    super.assertSame(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotSame(Object actual, Object expected, String message) {
    super.assertNotSame(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(Collection<?> actual, Collection<?> expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(Object[] actual, Object[] expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEqualsNoOrder(Object[] actual, Object[] expected, String message) {
    super.assertEqualsNoOrder(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(byte[] actual, byte[] expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertEquals(Set<?> actual, Set<?> expected, String message) {
    super.assertEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(Object actual, Object expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(String actual, String expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(long actual, long expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(boolean actual, boolean expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(byte actual, byte expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(char actual, char expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(short actual, short expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(int actual, int expected, String message) {
    super.assertNotEquals(actual, expected, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(float actual, float expected, float delta, String message) {
    super.assertNotEquals(actual, expected, delta, softAssertMessageBuilder.buildMessage(message));
  }

  @Override
  public void assertNotEquals(double actual, double expected, double delta, String message) {
    super.assertNotEquals(actual, expected, delta, softAssertMessageBuilder.buildMessage(message));
  }
}
