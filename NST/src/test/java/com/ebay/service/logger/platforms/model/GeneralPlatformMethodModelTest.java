package com.ebay.service.logger.platforms.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;

public class GeneralPlatformMethodModelTest {

  private static final String NST_PLATFORM = "nstplatform";

  @BeforeMethod(alwaysRun = true)
  @AfterMethod(alwaysRun = true)
  public void resetBeforeAndAfterEachTest() {
    resetRuntimeOverrides();
  }

  // Reset the overrides.
  private void resetRuntimeOverrides() {
    System.clearProperty(NST_PLATFORM);
  }

  GeneralPlatformMethodModel model = new GeneralPlatformMethodModel();

  @Test(groups = "unitTest")
  public void calculateIndentFromMethodSignatureWithNoIndentIos() {

    System.setProperty(NST_PLATFORM, Platform.IOS.name());
    RuntimeConfigManager.getInstance().reinitialize();

    model.setMethodSignature("public void calculateIndentFromMethodSignatureWithNoIndent() {");
    String indent = model.getMethodContentsIndent();
    assertThat(indent, is(equalTo("" + '\u0009' + '\u0009')));
  }

  @Test(groups = "unitTest")
  public void calculateIndentFromMethodSignatureWithNoIndent() {

    System.setProperty(NST_PLATFORM, Platform.ANDROID.name());
    RuntimeConfigManager.getInstance().reinitialize();

    model.setMethodSignature("public void calculateIndentFromMethodSignatureWithNoIndent() {");
    String indent = model.getMethodContentsIndent();
    assertThat(indent, is(equalTo("\t\t")));
  }

  @Test(groups = "unitTest")
  public void calculateIndentFromMethodSignatureWithOneTabIndent() {

    System.setProperty(NST_PLATFORM, Platform.ANDROID.name());
    RuntimeConfigManager.getInstance().reinitialize();

    model.setMethodSignature("  public void calculateIndentFromMethodSignatureWithNoIndent() {");
    String indent = model.getMethodContentsIndent();
    assertThat(indent, is(equalTo("  \t\t")));
  }

  @Test(groups = "unitTest")
  public void calculateIndentFromMethodSignatureWithTwoTabIndent() {

    System.setProperty(NST_PLATFORM, Platform.ANDROID.name());
    RuntimeConfigManager.getInstance().reinitialize();

    model.setMethodSignature("    public void calculateIndentFromMethodSignatureWithNoIndent() {");
    String indent = model.getMethodContentsIndent();
    assertThat(indent, is(equalTo("    \t\t")));
  }

  @Test(groups = "unitTest")
  public void calculateIndentFromMethodSignatureWithOddNumberOfSpacesIndent() {

    System.setProperty(NST_PLATFORM, Platform.ANDROID.name());
    RuntimeConfigManager.getInstance().reinitialize();

    model.setMethodSignature("   public void calculateIndentFromMethodSignatureWithNoIndent() {");
    String indent = model.getMethodContentsIndent();
    assertThat(indent, is(equalTo("   \t\t")));
  }
}
