package com.ebay.service.logger.platforms.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;

public class PlatformApiToFuiMappingParserTest {

  private static final String nstplatformKey = "nstplatform";

  @BeforeMethod(alwaysRun = true)
  public void beforeEachTest() {
    System.clearProperty(nstplatformKey);
  }

  @AfterClass(alwaysRun = true)
  public void resetAtEnd() {
	System.clearProperty(nstplatformKey);
  }

  @Test(groups = "unitTest")
  public void lookupKnownApiWithPartialColumnData() throws Throwable {

    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForApiName("EditAddress");

    PlatformApiToFuiMapping expectedMappings = new PlatformApiToFuiMapping();
    expectedMappings.setEntryStatements("// Enter: edit a shipping address in xo");
    expectedMappings.setNavigationStatements("// Navigate to: edit a shipping address in xo");
    expectedMappings.setImportStatements("");
    expectedMappings.setMemberFieldStatements("");
    expectedMappings.setPlatformRequestTypeStatements("");

    assertThat(mappings, is(equalTo(expectedMappings)));
  }

  @Test(groups = "unitTest")
  public void lookupKnownApiWithoutService() throws Throwable {

    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForApiName("EnterCheckout");

    PlatformApiToFuiMapping expectedMappings = new PlatformApiToFuiMapping();
    expectedMappings.setEntryStatements("enterCheckoutSession.enterCheckout();");
    expectedMappings.setNavigationStatements("enterCheckoutSession.navigateToCheckout();");
    expectedMappings.setImportStatements("import com.test.EnterCheckoutSession;");
    expectedMappings.setMemberFieldStatements("EnterCheckoutSession enterCheckoutSession = new EnterCheckoutSession();");
    expectedMappings.setPlatformRequestTypeStatements("enter-checkout");

    assertThat(mappings, is(equalTo(expectedMappings)));
  }

  @Test(groups = "unitTest")
  public void lookupKnownApiWithServiceAmdroid() throws Throwable {

    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForServiceAndApiName("checkout", "EnterCheckout");

    PlatformApiToFuiMapping expectedMappings = new PlatformApiToFuiMapping();
    expectedMappings.setEntryStatements("enterCheckoutSession.enterCheckout();");
    expectedMappings.setNavigationStatements("enterCheckoutSession.navigateToCheckout();");
    expectedMappings.setImportStatements("import com.test.EnterCheckoutSession;");
    expectedMappings.setMemberFieldStatements("EnterCheckoutSession enterCheckoutSession = new EnterCheckoutSession();");
    expectedMappings.setPlatformRequestTypeStatements("enter-checkout");

    assertThat(mappings, is(equalTo(expectedMappings)));
  }

  @Test(groups = "unitTest")
  public void lookupKnownApiWithServiceIos() throws Throwable {

    System.setProperty(nstplatformKey, "IOS");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForServiceAndApiName("checkout", "EnterCheckout");

    PlatformApiToFuiMapping expectedMappings = new PlatformApiToFuiMapping();
    expectedMappings.setPlatformRequestTypeStatements("CheckoutModule.EnterCheckoutRequest");
    expectedMappings.setEntryStatements("enterCheckoutSession.enterCheckout()");
    expectedMappings.setNavigationStatements("enterCheckoutSession.navigateToCheckout()");
    expectedMappings.setImportStatements("import EnterCheckoutSession");
    expectedMappings.setMemberFieldStatements("let enterCheckoutSession = EnterCheckoutSession()");

    assertThat(mappings, is(equalTo(expectedMappings)));
  }

  @Test(groups = "unitTest")
  public void lookupUnknownApi() throws Throwable {

    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForApiName("Foo");

    assertThat(mappings, is(nullValue()));
  }

  @Test(groups = "unitTest")
  public void lookupUnknownService() throws Throwable {

    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForServiceAndApiName("foo", "CreateCheckoutSessionV2");

    assertThat(mappings, is(nullValue()));
  }

  @Test(expectedExceptions =  IllegalArgumentException.class, groups = "unitTest")
  public void attemptToAccessNonexistentCsvFile() throws Throwable {

    System.setProperty(nstplatformKey, "MWEB");
    RuntimeConfigManager.getInstance().reinitialize();

    new PlatformApiToFuiMappingParser();
  }

  @Test(groups = "unitTest")
  public void parseCellsWithMultipleLinesOfText() throws Throwable {

    System.setProperty(nstplatformKey, "ANDROID");
    RuntimeConfigManager.getInstance().reinitialize();

    PlatformApiToFuiMappingParser parser = new PlatformApiToFuiMappingParser();
    PlatformApiToFuiMapping mappings = parser.getMappingsForApiName("AddAddress");

    PlatformApiToFuiMapping expectedMappings = new PlatformApiToFuiMapping();
    expectedMappings.setEntryStatements("// Enter: add a shipping address in xo\n// SECOND LINE");
    expectedMappings.setNavigationStatements("// Navigate to: add a shipping address in xo\n// SECOND LINE");
    expectedMappings.setImportStatements("");
    expectedMappings.setMemberFieldStatements("");
    expectedMappings.setPlatformRequestTypeStatements("");

    assertThat(mappings, is(equalTo(expectedMappings)));
  }
}
