package com.ebay.service.logger.platforms.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

public class PlatformApiCallCounterTest {

  @Test(groups = "unitTest")
  public void checkCountOfNonTrackedApi() {
    int count = PlatformApiCallCounter.getInstance().getApiCountFor("FOO");
    assertThat(count, is(equalTo(0)));
  }

  @Test(groups = "unitTest")
  public void checkCountOfTrackedApi() {
    PlatformApiCallCounter.getInstance().incrementApiCountFor("FOO");
    PlatformApiCallCounter.getInstance().incrementApiCountFor("FOO");
    PlatformApiCallCounter.getInstance().incrementApiCountFor("FOO");
    int count = PlatformApiCallCounter.getInstance().getApiCountFor("FOO");
    assertThat(count, is(equalTo(3)));
  }

  @Test(groups = "unitTest")
  public void incrementApiCounterReturnsCount() {
    int count = PlatformApiCallCounter.getInstance().incrementApiCountFor("FOO");
    assertThat(count, is(equalTo(1)));
  }

  @Test(groups = "unitTest")
  public void clearApiCounter() {
    PlatformApiCallCounter.getInstance().incrementApiCountFor("FOO");
    PlatformApiCallCounter.getInstance().clear();
    int count = PlatformApiCallCounter.getInstance().getApiCountFor("FOO");
    assertThat(count, is(equalTo(0)));
  }
}
