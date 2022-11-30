package com.ebay.service.logger.platforms.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.FileNotFoundException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;

public class PlatformTestFileManagerTest {

  private static final String cwd = System.getProperty("user.dir");
  private static final String nstplatformKey = "nstplatform";
  private static final String androidPlatformTestLocationKey = "androidTestsLocation";
  private static final String iosPlatformTestLocationKey = "iosTestsLocation";

  @BeforeMethod(alwaysRun = true)
  public void beforeEachTest() {
    System.clearProperty(nstplatformKey);
    System.clearProperty(androidPlatformTestLocationKey);
    System.clearProperty(iosPlatformTestLocationKey);
  }

  @Test(groups = "unitTest")
  public void findAndroidTestClassWithExtension() throws Throwable {
    System.setProperty(nstplatformKey, "ANDROID");
    System.setProperty(androidPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.hasTestFile("SOURCE_AndroidEmptyTestClass.java"), is(equalTo(true)));
  }

  @Test(groups = "unitTest")
  public void findAndroidTestClassWithoutExtension() throws Throwable {
    System.setProperty(nstplatformKey, "ANDROID");
    System.setProperty(androidPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.hasTestFile("SOURCE_AndroidEmptyTestClass"), is(equalTo(true)));
  }

  @Test(groups = "unitTest")
  public void findIosTestClassWithExtension() throws Throwable {
    System.setProperty(nstplatformKey, "IOS");
    System.setProperty(iosPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.hasTestFile("SOURCE_IosEmptyTestClass.swift"), is(equalTo(true)));
  }

  @Test(groups = "unitTest")
  public void findIosTestClassWithoutExtension() throws Throwable {
    System.setProperty(nstplatformKey, "IOS");
    System.setProperty(iosPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.hasTestFile("SOURCE_IosEmptyTestClass"), is(equalTo(true)));
  }

  @Test(expectedExceptions = FileNotFoundException.class)
  public void specifyInvalidDirectory() throws Throwable {
    System.setProperty(nstplatformKey, "ANDROID");
    System.setProperty(androidPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android/dne", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    new PlatformTestFileManager();
  }

  @Test(groups = "unitTest")
  public void getAndroidTestFileWithExtension() throws Throwable {
    System.setProperty(nstplatformKey, "ANDROID");
    System.setProperty(androidPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.getTestFile("SOURCE_AndroidEmptyTestClass.java"), is(notNullValue()));
  }

  @Test(groups = "unitTest")
  public void getAndroidTestFileWithoutExtension() throws Throwable {
    System.setProperty(nstplatformKey, "ANDROID");
    System.setProperty(androidPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.getTestFile("SOURCE_AndroidEmptyTestClass"), is(notNullValue()));
  }

  @Test(groups = "unitTest")
  public void getIosTestFileWithExtension() throws Throwable {
    System.setProperty(nstplatformKey, "IOS");
    System.setProperty(iosPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.getTestFile("SOURCE_IosEmptyTestClass.swift"), is(notNullValue()));
  }

  @Test(groups = "unitTest")
  public void getIosTestFileWithoutExtension() throws Throwable {
    System.setProperty(nstplatformKey, "IOS");
    System.setProperty(iosPlatformTestLocationKey, String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios", cwd));
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformTestFileManager fileManager = new PlatformTestFileManager();
    assertThat(fileManager.getTestFile("SOURCE_IosEmptyTestClass"), is(notNullValue()));
  }
}
