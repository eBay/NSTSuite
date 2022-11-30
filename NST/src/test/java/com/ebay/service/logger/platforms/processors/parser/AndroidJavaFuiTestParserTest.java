package com.ebay.service.logger.platforms.processors.parser;

import static com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType.MEMBER_FIELD;
import static com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType.METHOD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.service.logger.platforms.blocksignatures.PlatformBlockSignatures;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformMethodModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;
import com.ebay.service.logger.platforms.processors.parser.AndroidJavaFuiTestParser.STATE;

public class AndroidJavaFuiTestParserTest {

  private static final String cwd = System.getProperty("user.dir");

  AndroidJavaFuiTestParser testParser = new AndroidJavaFuiTestParser();

  @BeforeMethod
  public void beforeEachTest() {
    testParser.resetCurlyBraceCounter();
  }

  //
  // Package to Import Transition Checks
  //

  @DataProvider(name = "PackageToImport")
  public Object[][] getPackageToImportTestValues() {
    return new Object[][] {
        { "package com.ebay.foo;", STATE.PACKAGE, STATE.PACKAGE },
        { "import com.ebay.foo;", STATE.PACKAGE, STATE.IMPORTS },
        { "", STATE.PACKAGE, STATE.PACKAGE },
        { " ", STATE.PACKAGE, STATE.PACKAGE } };
  }

  @Test(dataProvider = "PackageToImport", groups = "unitTest")
  public void transitionFromPackageToImports(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from package to import did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Package to Class Signature Transition Checks
  //

  @DataProvider(name = "PackageToClassSignature")
  public Object[][] getPackageToClassSignatureTestValues() {
    return new Object[][] {
        { "package com.ebay.foo;", STATE.PACKAGE, STATE.PACKAGE },
        { "public class Foo {", STATE.PACKAGE, STATE.CLASS_SIGNATURE },
        { "public class Foo", STATE.PACKAGE, STATE.CLASS_SIGNATURE },
        { "", STATE.PACKAGE, STATE.PACKAGE },
        { " ", STATE.PACKAGE, STATE.PACKAGE } };
  }

  @Test(dataProvider = "PackageToClassSignature", groups = "unitTest")
  public void transitionFromPackageToClassSignature(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from package to class signature did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Package to Pre Class Signature Transition Checks
  //

  @DataProvider(name = "PackageToPreClassSignature")
  public Object[][] getPackageToPreClassSignatureTestValues() {
    return new Object[][] {
        { "package com.ebay.foo;", STATE.PACKAGE, STATE.PACKAGE },
        { "/**", STATE.PACKAGE, STATE.PRE_CLASS_SIGNATURE },
        { "", STATE.PACKAGE, STATE.PACKAGE },
        { " ", STATE.PACKAGE, STATE.PACKAGE },
        { "// Comment line", STATE.PACKAGE, STATE.PRE_CLASS_SIGNATURE } };
  }

  @Test(dataProvider = "PackageToPreClassSignature", groups = "unitTest")
  public void transitionFromPackageToPreClassSignature(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from package to pre class signature did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Imports to Class Signature Transition Checks
  //

  @DataProvider(name = "ImportToClassSignature")
  public Object[][] getImportsToClassSignatureTestValues() {
    return new Object[][] {
        { "public class Foo {", STATE.IMPORTS, STATE.CLASS_SIGNATURE },
        { "public class Foo", STATE.IMPORTS, STATE.CLASS_SIGNATURE },
        { "", STATE.IMPORTS, STATE.IMPORTS },
        { " ", STATE.IMPORTS, STATE.IMPORTS },
        { "import com.ebay.foo", STATE.IMPORTS, STATE.IMPORTS } };
  }

  @Test(dataProvider = "ImportToClassSignature", groups = "unitTest")
  public void transitionFromImportsToClassSignature(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from imports to class signature did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Imports to Pre Class Signature Transition Checks
  //

  @DataProvider(name = "ImportToPreClassSignature")
  public Object[][] getImportsToPreClassSignatureTestValues() {
    return new Object[][] {
        { "/**", STATE.IMPORTS, STATE.PRE_CLASS_SIGNATURE },
        { "// Comment line", STATE.IMPORTS, STATE.PRE_CLASS_SIGNATURE },
        { "", STATE.IMPORTS, STATE.IMPORTS },
        { " ", STATE.IMPORTS, STATE.IMPORTS },
        { "import com.ebay.foo", STATE.IMPORTS, STATE.IMPORTS } };
  }

  @Test(dataProvider = "ImportToPreClassSignature", groups = "unitTest")
  public void transitionFromImportsToPreClassSignature(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from imports to pre class signature did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Pre Class Signature to Class Signature Transition Checks
  //

  @DataProvider(name = "PreClassSignatureToClassSignature")
  public Object[][] getPreClassSignatureToClassSignatureTestValues() {
    return new Object[][] {
        { "public class Foo {", STATE.IMPORTS, STATE.CLASS_SIGNATURE },
        { "public class Foo", STATE.IMPORTS, STATE.CLASS_SIGNATURE },
        { " */", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE },
        { "*/", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE },
        { " // Comment line", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE },
        { "// Comment line", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE },
        { "", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE },
        { " ", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE } };
  }

  @Test(dataProvider = "PreClassSignatureToClassSignature", groups = "unitTest")
  public void transitionFromPreClassSignatureToClassSignature(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from pre class signature to class signature did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Class Signature to Post Class Header Transition Checks
  //

  @DataProvider(name = "ClassSignatureToPostClassSignature")
  public Object[][] getClassSignatureToPostClassSignature() {
    return new Object[][] {
        { "@Rule", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "{", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE },
        { "", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "  ", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " // comment line", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "   //", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " public String variable;", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE } };
  }

  @Test(dataProvider = "ClassSignatureToPostClassSignature", groups = "unitTest")
  public void transitionFromClassSignatureToPostClassSignature(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from class signature to post class signature did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Class Signature to Member Field(s) Transition Checks
  //

  @DataProvider(name = "ClassSignatureToMemberFields")
  public Object[][] getClassSignatureToMemberFields() {
    return new Object[][] {
        { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + "  ", STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS } };
  }

  @Test(dataProvider = "ClassSignatureToMemberFields", groups = "unitTest")
  public void transitionFromClassSignatureToMemberFields(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from class signature to member fields did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Post Class Signature to Member Field(s) Transition Checks
  //

  @DataProvider(name = "PostClassSignatureToMemberFields")
  public Object[][] getPostClassSignatureToMemberFields() {
    return new Object[][] {
        { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + "  ", STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
        { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS } };
  }

  @Test(dataProvider = "PostClassSignatureToMemberFields", groups = "unitTest")
  public void transitionFromPostClassSignatureToMemberFields(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from class signature to member fields did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Post Class Signature to Method Transition Checks
  //

  @DataProvider(name = "PostClassSignatureToMethodFields")
  public Object[][] getPostClassSignatureToMethodFields() {
    return new Object[][] {
        { "public void foo() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "public void foo() throws Throwable {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "public void foo()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { " public void foo() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { " public void foo()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "   public void foo() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "   public void foo()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "public  void foo()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "public void  foo()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
        { "public void oof() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "public void Foo() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "public void fooo() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "public void fOo() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "public void bar() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " public void bar() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "   public void bar() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "public void bar()", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " public void bar()", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " ", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "   ", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " // comment line", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { "   //", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " public String variable;", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE },
        { " // comment line", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE } };
  }

  @Test(dataProvider = "PostClassSignatureToMethodFields", groups = "unitTest")
  public void transitionFromPostClassSignatureToMethodFields(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from post class signature to method did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Member Fields to Pre Method Transition Checks
  //

  @DataProvider(name = "MemberFieldsToPreMethod")
  public Object[][] getMemberFieldsToPreMethodValues() {
    return new Object[][] {
        { PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD), STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
        { "  " + PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD), STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
        { PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD) + "  ", STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
        { "  " + PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD) + "  ", STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
        { " " + PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD) + " ", STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
        { "@Rule", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { "   if (foo != true) {", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { "", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { "  ", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { " // comment line", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { "   //", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { " String variable;", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS },
        { " // comment line", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS } };
  }

  @Test(dataProvider = "MemberFieldsToPreMethod", groups = "unitTest")
  public void transitionFromMemberFieldsToPreMethod(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from member field to pre method did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Pre Method to Method Transition Checks
  //

  @DataProvider(name = "PreMethodToMethod")
  public Object[][] getPreMethodToMethodValues() {
    return new Object[][] {
        { "   public void foo() throws Throwable {", STATE.PRE_METHOD, STATE.METHOD },
        { "public void foo() throws Throwable {", STATE.PRE_METHOD, STATE.METHOD },
        { "   public void foo() {" + " ", STATE.PRE_METHOD, STATE.METHOD },
        { "   public void foo()", STATE.PRE_METHOD, STATE.METHOD },
        { "public void foo()", STATE.PRE_METHOD, STATE.METHOD },
        { "   @TestRail(testCaseId = \"C2121462\")", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   @Test(enabled = true, groups = \"AndroidEmptyTestClass\")", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   public void firstTestMethod() throws Throwable {", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   // CORRESPONDING API CALL: CreateCheckoutSessionV2", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   // OPERATION INDEX: 1", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   Checkout.launchActivity(getStubCheckoutIntent(getEbayContext()))", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   // END OF AUTO GENERATED METHOD CODE BLOCK", STATE.PRE_METHOD, STATE.PRE_METHOD },
        { "   Assert.assertTrue(checkWeAreOnCheckoutHub(), \"We are not where we think we are.\");", STATE.PRE_METHOD, STATE.PRE_METHOD } };
  }

  @Test(dataProvider = "PreMethodToMethod", groups = "unitTest")
  public void transitionFromPreMethodToMethod(String line, STATE startingState, STATE expectedState) {
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from pre method to method did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  //
  // Method to Post Method Transition Checks
  //

  @DataProvider(name = "MethodToPostMethod")
  public Object[][] getMethodToPostMethodValues() {
    return new Object[][] {
        { PlatformBlockSignatures.getBlockExitSignature(METHOD) + "}", STATE.METHOD, STATE.POST_METHOD },
        { "{\n" + PlatformBlockSignatures.getBlockExitSignature(METHOD) + " }} ", STATE.METHOD, STATE.POST_METHOD },
        { "\n}", STATE.METHOD, STATE.POST_METHOD },
        { "{{}}}", STATE.METHOD, STATE.POST_METHOD },
        { "}", STATE.METHOD, STATE.POST_METHOD },
        { PlatformBlockSignatures.getBlockExitSignature(METHOD), STATE.METHOD, STATE.METHOD },
        { PlatformBlockSignatures.getBlockExitSignature(METHOD), STATE.METHOD, STATE.METHOD },
        { PlatformBlockSignatures.getBlockExitSignature(METHOD) + "  ", STATE.METHOD, STATE.METHOD },
        { PlatformBlockSignatures.getBlockExitSignature(METHOD) + " ", STATE.METHOD, STATE.METHOD },
        { PlatformBlockSignatures.getMethodBlockCallSignature("service"), STATE.METHOD, STATE.METHOD },
        { PlatformBlockSignatures.getMethodBlockOperationIndexSignature(1), STATE.METHOD, STATE.METHOD },
        { "   String value = 0;", STATE.METHOD, STATE.METHOD },
        { " for (int i = 0; i < 10; i++) {", STATE.METHOD, STATE.METHOD },
        { "{\n", STATE.METHOD, STATE.METHOD },
        { "//", STATE.METHOD, STATE.METHOD },
        { "/*", STATE.METHOD, STATE.METHOD },
        { "   //", STATE.METHOD, STATE.METHOD },
        { "//METHOD", STATE.METHOD, STATE.METHOD },
        { "{{{{{}}}}", STATE.METHOD, STATE.METHOD },
        { "{{{{{}", STATE.METHOD, STATE.METHOD } };
  }

  @Test(dataProvider = "MethodToPostMethod", groups = "unitTest")
  public void transitionFromMethodToPostMethod(String line, STATE startingState, STATE expectedState) {
    testParser.getParsingState("   public void foo() throws Throwable {", STATE.PRE_METHOD, "foo");
    STATE state = testParser.getParsingState(line, startingState, "foo");
    assertThat("State transition from method to post method did not resolve correctly.", state, is(equalTo(expectedState)));
  }

  @Test(groups = "unitTest")
  public void parseTestFileTargetingFirstMethod() throws IOException {
    File file = new File(String.format("%s/./target/test-classes/com/ebay/service/logger/platform/android/SOURCE_AndroidPopulatedTestClass.java", cwd));

    AndroidJavaFuiTestParser parser = new AndroidJavaFuiTestParser();
    GeneralPlatformFileModel fileModel = parser.parse(file, "firstTestMethod");
    GeneralPlatformFileModel expectedModel = getExpectedModel();

    assertThat(fileModel.getPackageName(), is(equalTo(expectedModel.getPackageName())));
    assertThat(fileModel.getImportContents(), is(equalTo(expectedModel.getImportContents())));
    assertThat(fileModel.getPreClassSignature(), is(equalTo(expectedModel.getPreClassSignature())));
    assertThat(fileModel.getClassSignature(), is(equalTo(expectedModel.getClassSignature())));
    assertThat(fileModel.getPostClassSignature(), is(equalTo(expectedModel.getPostClassSignature())));
    assertThat(fileModel.getMemberFieldContents(), is(equalTo(expectedModel.getMemberFieldContents())));
    assertThat(fileModel.getPreMethodFileContents(), is(equalTo(expectedModel.getPreMethodFileContents())));
    assertThat(fileModel.getMethodContents(), is(equalTo(expectedModel.getMethodContents())));
    assertThat(fileModel.getPostMethodFileContents(), is(equalTo(expectedModel.getPostMethodFileContents())));

    assertThat(fileModel, is(equalTo(expectedModel)));
  }

  private GeneralPlatformFileModel getExpectedModel() {

    GeneralPlatformFileModel fileModel = new GeneralPlatformFileModel();

    fileModel.setPackageName("package com.ebay.service.logger.platform.android;");

    fileModel.addImportStatement("import androidx.test.filters.LargeTest;");
    fileModel.addImportStatement("");
    fileModel.addImportStatement("import org.json.JSONObject;");
    fileModel.addImportStatement("import org.junit.Assert;");
    fileModel.addImportStatement("import org.junit.Rule;");
    fileModel.addImportStatement("import org.junit.Test;");
    fileModel.addImportStatement("import org.junit.rules.RuleChain;");
    fileModel.addImportStatement("import com.ebay.nst.NSTServiceTestBase;");
    fileModel.addImportStatement("import com.ebay.test.utils.testrail.TestCase;");
    fileModel.addImportStatement("");

    fileModel.addPreClassSignatureData("/**");
    fileModel.addPreClassSignatureData(" * AndroidEmptyTestClass.java");
    fileModel.addPreClassSignatureData(" * <p>");
    fileModel.addPreClassSignatureData(" * eBay Created by <author> on 07/16/19");
    fileModel.addPreClassSignatureData(" * <p>");
    fileModel.addPreClassSignatureData(" * Copyright 2019 eBay. All rights reserved.");
    fileModel.addPreClassSignatureData(" */");
    fileModel.addPreClassSignatureData("@LargeTest");
    fileModel.addPreClassSignatureData("@PreconditionUser");
    fileModel.addPreClassSignatureData("@PreconditionCountry(siteId = EbaySite.SITE_ID.US)");

    fileModel.addClassSignatureData("public class AndroidEmptyTestClass extends AndroidBaseClass\n{");

    fileModel.addPostClassSignatureData("\tprivate final String directDebitAssertionMessage\n\t\t= \"Direct Debit was not successfully selected as the payment method.\";");

    fileModel.addMemberFieldStatement("\t@Rule");
    fileModel.addMemberFieldStatement("\tpublic final CheckoutMixedActivityTestRule checkout = new CheckoutMixedActivityTestRule(");
    fileModel.addMemberFieldStatement("\t\tnew CheckoutMixedActivityFactory(), true, false, false);");
    fileModel.addMemberFieldStatement("\t@Rule");
    fileModel.addMemberFieldStatement("\tpublic final InterceptorFlowRule interceptorFlowRule = new InterceptorFlowRule(requestInterceptors);");
    fileModel.addMemberFieldStatement("\tprivate final XoHubPageModel xoHubPageModel = new XoHubPageModel();");
    fileModel.addMemberFieldStatement("\tprivate final PaymentSelection psp = new PaymentSelection();");

    fileModel.addPreMethodData("\n\t@Test\n\t@TestCase(id = \"C2121462\")");

    GeneralPlatformMethodModel methodModel = new GeneralPlatformMethodModel();
    methodModel.setMethodSignature("\tpublic void firstTestMethod() throws Throwable");
    methodModel.addMethodLeadInContents("\t{");
    methodModel.addMethodLeadInContents("");
    methodModel.addMethodLeadInContents("\t\tint i = 0;");
    methodModel.addMethodLeadInContents("");

    GeneralPlatformOperationModel operation1 = new GeneralPlatformOperationModel();
    operation1.setServiceWrapperApiName("CreateCheckoutSessionV2");
    operation1.addCustomBlockLine("\n\t\tAssert.assertTrue(checkWeAreOnCheckoutHub(), \"We are not where we think we are.\");");
    operation1.addCustomBlockLine("\t\tAssert.assertTrue(checkoutHubHasOneItemInCheckout(), \"We do not have a single item in checkout.\");");
    operation1.addCustomBlockLine("");
    methodModel.addMethodOperations(operation1);

    GeneralPlatformOperationModel operation2 = new GeneralPlatformOperationModel();
    operation2.setServiceWrapperApiName("SetPaymentInstrument");
    operation2.addCustomBlockLine("\n\t\tpsp.paymentMethodPrimaryText(PaymentMethodType.DIRECT_DEBIT.name()).perform(click());");
    operation2.addCustomBlockLine("");
    methodModel.addMethodOperations(operation2);

    GeneralPlatformOperationModel operation3 = new GeneralPlatformOperationModel();
    operation3.setServiceWrapperApiName("PurchaseCheckoutSession");
    operation3.addCustomBlockLine("\n\t\tAssert.assertTrue(checkThatWeCompletedCheckout(), \"Checkout was not completed successfully.\");");
    methodModel.addMethodOperations(operation3);

    fileModel.setMethodContents(methodModel);

    fileModel.addPostMethodData("\t}");
    fileModel.addPostMethodData("");
    fileModel.addPostMethodData("\t@Test");
    fileModel.addPostMethodData("\t@TestCase(id = \"C2152776\")");
    fileModel.addPostMethodData("\tpublic void secondTestMethod() throws Throwable");
    fileModel.addPostMethodData("\t{");
    fileModel.addPostMethodData("\t\t// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS");
    fileModel.addPostMethodData("\t\t// CORRESPONDING API CALL: CreateCheckoutSessionV2");
    fileModel.addPostMethodData("\t\t// OPERATION INDEX: 1");
    fileModel.addPostMethodData("\t\tenterCheckoutWithItem();");
    fileModel.addPostMethodData("\t\t// END OF AUTO GENERATED METHOD CODE BLOCK");
    fileModel.addPostMethodData("");
    fileModel.addPostMethodData("\t\tAssert.assertTrue(checkWeAreOnCheckoutHub(), \"We are not where we think we are.\");");
    fileModel.addPostMethodData("\t\tAssert.assertTrue(checkoutHubHasOneItemInCheckout(), \"We do not have a single item in checkout.\");");
    fileModel.addPostMethodData("\t}");
    fileModel.addPostMethodData("");
    fileModel.addPostMethodData("\t/**");
    fileModel.addPostMethodData("\t * Simple private method for evaluation purposes.,");
    fileModel.addPostMethodData("\t */");
    fileModel.addPostMethodData("\tprivate void doSomething()");
    fileModel.addPostMethodData("\t{");
    fileModel.addPostMethodData("\t\tint b = 0;");
    fileModel.addPostMethodData("\t\tb++;");
    fileModel.addPostMethodData("\t}");
    fileModel.addPostMethodData("}");

    return fileModel;
  }
}
