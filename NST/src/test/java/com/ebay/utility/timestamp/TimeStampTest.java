package com.ebay.utility.timestamp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TimeStampTest {

  @Test(groups = "unitTest")
  public void getTimestamp() {
    String timestamp = TimeStamp.getCurrentTime();
    assertThat("Timestamp must not be null.", timestamp, is(notNullValue()));
  }

  @Test(groups = "unitTest")
  public void timestampMatchesFormat() {
    String timestamp = TimeStamp.getCurrentTime();
    Boolean patternMatchesExpected = Pattern.matches("^([0-9]{4})/([0-9]{1,2})/([0-9]{1,2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})", timestamp);
    String message = String.format("Timestamp [%s] must match expected format.", timestamp);
    Assert.assertTrue(patternMatchesExpected, message);
  }
}
