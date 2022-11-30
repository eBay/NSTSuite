package com.ebay.service.logger.platforms.processors.parser;

import static com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType.INJECTION;
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
import com.ebay.service.logger.platforms.processors.parser.IosSwiftFuiTestParser.METHOD_STATE;
import com.ebay.service.logger.platforms.processors.parser.IosSwiftFuiTestParser.STATE;

public class IosSwiftFuiTestParserTest {

  private static final String cwd = System.getProperty("user.dir");

  IosSwiftFuiTestParser testParser = new IosSwiftFuiTestParser();

  @BeforeMethod
  public void beforeEachTest() {
    testParser.resetCurlyBraceCounter();
  }

  // =======================================
  // General parsing state transfer tests
  // =======================================

  //
  // Test class documentation to imports
  //
  @DataProvider(name = "classDocumentationToImportsTestData")
  public Object[][] classDocumentationToImportsTestData() {
    return new Object[][] {
      {"//", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"  //", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {" //", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"  ", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"var foo = Foo()", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"import XCTestCase", STATE.CLASS_DOCUMENTATION, STATE.IMPORTS},
      {" import XCTestCase", STATE.CLASS_DOCUMENTATION, STATE.IMPORTS},
      {"  import XCTestCase", STATE.CLASS_DOCUMENTATION, STATE.IMPORTS},
      };
  }

  @Test(dataProvider = "classDocumentationToImportsTestData", groups = "unitTest")
  public void classDocumentationToImportsTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test imports to pre class signature
  //

  @DataProvider(name = "importsToPreClassSignatureTestData")
  public Object[][] importsToPreClassSignatureTestData() {
    return new Object[][] {
      {"import XCTestCase", STATE.IMPORTS, STATE.IMPORTS},
      {" import XCTestCase", STATE.IMPORTS, STATE.IMPORTS},
      {"  import XCTestCase", STATE.IMPORTS, STATE.IMPORTS},
      {"", STATE.IMPORTS, STATE.IMPORTS},
      {" ", STATE.IMPORTS, STATE.IMPORTS},
      {"  ", STATE.IMPORTS, STATE.IMPORTS},
      {"var foo = Foo()", STATE.IMPORTS, STATE.PRE_CLASS_SIGNATURE},
      {"//", STATE.IMPORTS, STATE.PRE_CLASS_SIGNATURE},
      {" //", STATE.IMPORTS, STATE.PRE_CLASS_SIGNATURE},
      {"  //", STATE.IMPORTS, STATE.PRE_CLASS_SIGNATURE}
    };
  }

  @Test(dataProvider = "importsToPreClassSignatureTestData", groups = "unitTest")
  public void importsToPreClassSignatureTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test imports to class signature
  //

  @DataProvider(name = "importsToClassSignatureTestData")
  public Object[][] importsToClassSignatureTestData() {
    return new Object[][] {
      {"import XCTestCase", STATE.IMPORTS, STATE.IMPORTS},
      {" import XCTestCase", STATE.IMPORTS, STATE.IMPORTS},
      {"  import XCTestCase", STATE.IMPORTS, STATE.IMPORTS},
      {"", STATE.IMPORTS, STATE.IMPORTS},
      {" ", STATE.IMPORTS, STATE.IMPORTS},
      {"  ", STATE.IMPORTS, STATE.IMPORTS},
      {"class TestClass {", STATE.IMPORTS, STATE.CLASS_SIGNATURE},
      {" class TestClass {", STATE.IMPORTS, STATE.CLASS_SIGNATURE},
      {"  class TestClass {", STATE.IMPORTS, STATE.CLASS_SIGNATURE},
      {"class TestClass", STATE.IMPORTS, STATE.CLASS_SIGNATURE},
      {" class TestClass", STATE.IMPORTS, STATE.CLASS_SIGNATURE},
      {"  class TestClass", STATE.IMPORTS, STATE.CLASS_SIGNATURE}
    };
  }

  @Test(dataProvider = "importsToClassSignatureTestData", groups = "unitTest")
  public void importsToClassSignatureTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test pre class signature to class signature
  //

  @DataProvider(name = "preClassSignatureToClassSignatureTestData")
  public Object[][] preClassSignatureToClassSignatureTestData() {
    return new Object[][] {
      {"var foo = Foo()", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {"//", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {" //", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {"  //", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {"", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {" ", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {"  ", STATE.PRE_CLASS_SIGNATURE, STATE.PRE_CLASS_SIGNATURE},
      {"class TestClass {", STATE.PRE_CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {" class TestClass {", STATE.PRE_CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"  class TestClass {", STATE.PRE_CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"class TestClass", STATE.PRE_CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {" class TestClass", STATE.PRE_CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"  class TestClass", STATE.PRE_CLASS_SIGNATURE, STATE.CLASS_SIGNATURE}

    };
  }

  @Test(dataProvider = "preClassSignatureToClassSignatureTestData", groups = "unitTest")
  public void preClassSignatureToClassSignatureTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test class documentation to class signature
  //

  @DataProvider(name = "classDocumentationToClassSignatureTestData")
  public Object[][] classDocumentationToClassSignatureTestData() {
    return new Object[][] {
      {"//", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"  //", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {" //", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"  ", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"var foo = Foo()", STATE.CLASS_DOCUMENTATION, STATE.CLASS_DOCUMENTATION},
      {"class TestClass {", STATE.CLASS_DOCUMENTATION, STATE.CLASS_SIGNATURE},
      {" class TestClass {", STATE.CLASS_DOCUMENTATION, STATE.CLASS_SIGNATURE},
      {"  class TestClass {", STATE.CLASS_DOCUMENTATION, STATE.CLASS_SIGNATURE},
      {"class TestClass", STATE.CLASS_DOCUMENTATION, STATE.CLASS_SIGNATURE},
      {" class TestClass", STATE.CLASS_DOCUMENTATION, STATE.CLASS_SIGNATURE},
      {"  class TestClass", STATE.CLASS_DOCUMENTATION, STATE.CLASS_SIGNATURE}
    };
  }

  @Test(dataProvider = "classDocumentationToClassSignatureTestData", groups = "unitTest")
  public void classDocumentationToClassSignatureTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test class signature to post class signature
  //

  @DataProvider(name = "classSignatureToPostClassSignatureTestData")
  public Object[][] classSignatureToPostClassSignatureTestData() {
    return new Object[][] {
      {"class TestClass {", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {" class TestClass {", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"  class TestClass {", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"class TestClass", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {" class TestClass", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"  class TestClass", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"//", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"  //", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {" //", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"  ", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"var foo = Foo()", STATE.CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE}
    };
  }

  @Test(dataProvider = "classSignatureToPostClassSignatureTestData", groups = "unitTest")
  public void classSignatureToPostClassSignatureTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test class signature to member fields
  //

  @DataProvider(name = "classSignatureToMemeberFieldsTestData")
  public Object[][] classSignatureToMemeberFieldsTestData() {
    return new Object[][] {
      {"class TestClass {", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {" class TestClass {", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"  class TestClass {", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"class TestClass", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {" class TestClass", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      {"  class TestClass", STATE.CLASS_SIGNATURE, STATE.CLASS_SIGNATURE},
      { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + "  ", STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.CLASS_SIGNATURE, STATE.MEMBER_FIELDS }
    };
  }

  @Test(dataProvider = "classSignatureToMemeberFieldsTestData", groups = "unitTest")
  public void classSignatureToMemeberFieldsTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test post class signature to member fields
  //

  @DataProvider(name = "postClassSignatureToMemberFieldsTestData")
  public Object[][] postClassSignatureToMemeberFieldsTestData() {
    return new Object[][] {
      {"//", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"  //", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {" //", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"  ", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"var foo = Foo()", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD), STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + "  ", STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(MEMBER_FIELD) + " ", STATE.POST_CLASS_SIGNATURE, STATE.MEMBER_FIELDS }
    };
  }

  @Test(dataProvider = "postClassSignatureToMemberFieldsTestData", groups = "unitTest")
  public void postClassSignatureToMemberFieldsTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test post class signature to method
  //

  @DataProvider(name = "postClassSignatureToMethodTestData")
  public Object[][] postClassSignatureToMethodTestData() {
    return new Object[][] {
      {"//", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"  //", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {" //", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"  ", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"var foo = Foo()", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"func testbar() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"func testfoo() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"func test_foo() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      {"func test_other() {", STATE.POST_CLASS_SIGNATURE, STATE.POST_CLASS_SIGNATURE},
      { "func testfooBar() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { " func testfooBar()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { " func testfooBar() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { "   func testfooBar()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { "   func testfooBar() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { "func test_fooBar() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { " func test_fooBar() {", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { "func test_fooBar()", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
      { "func test_fooBar() {}", STATE.POST_CLASS_SIGNATURE, STATE.METHOD },
    };
  }

  @Test(dataProvider = "postClassSignatureToMethodTestData", groups = "unitTest")
  public void postClassSignatureToMethodTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test member fields to pre method
  //

  @DataProvider(name = "memberFieldsToPreMethodTestData")
  public Object[][] memberFieldsToPreMethodTestData() {
    return new Object[][] {
      {"var foo = Foo()", STATE.MEMBER_FIELDS, STATE.MEMBER_FIELDS},
      { PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD), STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
      { " " + PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD), STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
      { PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD) + " ", STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
      { " " + PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD) + "  ", STATE.MEMBER_FIELDS, STATE.PRE_METHOD },
      { " " + PlatformBlockSignatures.getBlockExitSignature(MEMBER_FIELD) + " ", STATE.MEMBER_FIELDS, STATE.PRE_METHOD }
    };
  }

  @Test(dataProvider = "memberFieldsToPreMethodTestData", groups = "unitTest")
  public void memberFieldsToPreMethodTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "foo");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test pre method to method
  //

  @DataProvider(name = "preMethodToMethodTestData")
  public Object[][] preMethodToMethodTestData() {
    return new Object[][] {
      {"", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"  ", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"//", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"  //", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"var foo = Foo()", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"func testbar() {", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"func testfoo() {", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"func test_foo() {", STATE.PRE_METHOD, STATE.PRE_METHOD},
      {"func test_other() {", STATE.PRE_METHOD, STATE.PRE_METHOD},
      { "func testfooBar() {", STATE.PRE_METHOD, STATE.METHOD },
      { " func testfooBar()", STATE.PRE_METHOD, STATE.METHOD },
      { " func testfooBar() {", STATE.PRE_METHOD, STATE.METHOD },
      { "   func testfooBar()", STATE.PRE_METHOD, STATE.METHOD },
      { "   func testfooBar() {", STATE.PRE_METHOD, STATE.METHOD },
      { "func test_fooBar() {", STATE.PRE_METHOD, STATE.METHOD },
      { " func test_fooBar() {", STATE.PRE_METHOD, STATE.METHOD },
      { "func test_fooBar()", STATE.PRE_METHOD, STATE.METHOD },
      { "func test_fooBar() {}", STATE.PRE_METHOD, STATE.METHOD },
    };
  }

  @Test(dataProvider = "preMethodToMethodTestData", groups = "unitTest")
  public void preMethodToMethodTest(String line, STATE startingState, STATE expectedState) {
    STATE actualState = testParser.getParsingState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test method to post method
  //

  @DataProvider(name = "testMethodToPostMethodTestData")
  public Object[][] testMethodToPostMethodTestData() {
    return new Object[][] {
      {"{", STATE.METHOD, STATE.METHOD},
      {"{}", STATE.METHOD, STATE.METHOD},
      {"{{{{}}}", STATE.METHOD, STATE.METHOD},
      {"var foo = Foo()", STATE.METHOD, STATE.METHOD},
      {"let i = 1", STATE.METHOD, STATE.METHOD},
      {"guard", STATE.METHOD, STATE.METHOD},
      {"//", STATE.METHOD, STATE.METHOD},
      {"", STATE.METHOD, STATE.METHOD},
      {"  ", STATE.METHOD, STATE.METHOD},
      {"}", STATE.METHOD, STATE.POST_METHOD},
      {"{}}", STATE.METHOD, STATE.POST_METHOD},
      {"{{{}}}}", STATE.METHOD, STATE.POST_METHOD},
    };
  }

  @Test(dataProvider = "testMethodToPostMethodTestData", groups = "unitTest")
  public void testMethodToPostMethodTest(String line, STATE startingState, STATE expectedState) {
    testParser.resetCurlyBraceCounter();
    testParser.getParsingState("func test_fooBar() {", STATE.PRE_METHOD, "fooBar");
    STATE actualState = testParser.getParsingState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  // ========================================
  // Method operation state transfer tests
  // ========================================

  //
  // Test method signature match
  //

  @DataProvider(name = "methodSignatureMatchTestData")
  public Object[][] methodSignatureMatchTestData() {
    return new Object[][] {
      { "func testfoo() {", null, null },
      { "func testfooBar() {", null, METHOD_STATE.SIGNATURE },
      { "func testfooBar() {", METHOD_STATE.OPERATION_CALL_NAME, METHOD_STATE.SIGNATURE },
      { "func testfooBar() {", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.SIGNATURE },
      { "func testFooBar() {", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.SIGNATURE },
      { "func testfooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { " func testfooBar()", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { " func testfooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { "   func testfooBar()", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { "   func testfooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { "func test_fooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { " func test_fooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { "func test_fooBar()", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { "func test_fooBar() {}", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE }
    };
  }

  @Test(dataProvider = "methodSignatureMatchTestData", groups = "unitTest")
  public void methodSignatureMatchTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test signature to lead in
  //

  @DataProvider(name = "signatureToLeadInTestData")
  public Object[][] signatureToLeadInTestData() {
    return new Object[][] {
      { "func testfooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      {"", METHOD_STATE.SIGNATURE, METHOD_STATE.LEAD_IN},
      {"  ", METHOD_STATE.SIGNATURE, METHOD_STATE.LEAD_IN},
      {"//", METHOD_STATE.SIGNATURE, METHOD_STATE.LEAD_IN},
      {"  //", METHOD_STATE.SIGNATURE, METHOD_STATE.LEAD_IN},
      {"var foo = Foo()", METHOD_STATE.SIGNATURE, METHOD_STATE.LEAD_IN},
      {"for i in group {", METHOD_STATE.SIGNATURE, METHOD_STATE.LEAD_IN}
    };
  }

  @Test(dataProvider = "signatureToLeadInTestData", groups = "unitTest")
  public void signatureToLeadInTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test signature to begin injection block
  //

  @DataProvider(name = "signatureToBeginInjectionBlockTestData")
  public Object[][] signatureToBeginInjectionBlockTestData() {
    return new Object[][] {
      { "func testfooBar() {", METHOD_STATE.SIGNATURE, METHOD_STATE.SIGNATURE },
      { PlatformBlockSignatures.getBlockEntrySignature(INJECTION), METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(INJECTION), METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { PlatformBlockSignatures.getBlockEntrySignature(INJECTION) + " ", METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(INJECTION) + "  ", METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(INJECTION) + " ", METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_INJECTION_BLOCK }
    };
  }

  @Test(dataProvider = "signatureToBeginInjectionBlockTestData", groups = "unitTest")
  public void signatureToBeginInjectionBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test lead in to begin injection block
  //

  @DataProvider(name = "leadInToBeginInjectionBlockTestData")
  public Object[][] leadInToBeginInjectionBlockTestData() {
    return new Object[][] {
      { "", METHOD_STATE.LEAD_IN, METHOD_STATE.LEAD_IN },
      { PlatformBlockSignatures.getBlockEntrySignature(INJECTION), METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(INJECTION), METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { PlatformBlockSignatures.getBlockEntrySignature(INJECTION) + " ", METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(INJECTION) + "  ", METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_INJECTION_BLOCK },
      { " " + PlatformBlockSignatures.getBlockEntrySignature(INJECTION) + " ", METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_INJECTION_BLOCK }
    };
  }

  @Test(dataProvider = "leadInToBeginInjectionBlockTestData", groups = "unitTest")
  public void leadInToBeginInjectionBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test begin injection block to injection block
  //

  @DataProvider(name = "beginInjectionBlockToInjectionBlockTestData")
  public Object[][] beginInjectionBlockToInjectionBlockTestData() {
    return new Object[][] {
      {"inject(responses: [", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"  inject(responses: [", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"  ", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"\"PaymentsModule.CreateCheckoutSessionRequest\": \"SimpleCheckoutExampleTest_firstTest_1_CreateCheckoutSessionV2Response.json\",", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"             ])", METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
    };
  }

  @Test(dataProvider = "beginInjectionBlockToInjectionBlockTestData", groups = "unitTest")
  public void beginInjectionBlockToInjectionBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test injection block to end injection block
  //

  @DataProvider(name = "injectionBlockToEndInjectionBlockTestData")
  public Object[][] injectionBlockToEndInjectionBlockTestData() {
    return new Object[][] {
      {"inject(responses: [", METHOD_STATE.INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"", METHOD_STATE.INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"  ", METHOD_STATE.INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"\"PaymentsModule.CreateCheckoutSessionRequest\": \"SimpleCheckoutExampleTest_firstTest_1_CreateCheckoutSessionV2Response.json\",", METHOD_STATE.INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {"             ])", METHOD_STATE.INJECTION_BLOCK, METHOD_STATE.INJECTION_BLOCK},
      {PlatformBlockSignatures.getBlockExitSignature(INJECTION), METHOD_STATE.INJECTION_BLOCK, METHOD_STATE.END_INJECTION_BLOCK}
    };
  }

  @Test(dataProvider = "injectionBlockToEndInjectionBlockTestData", groups = "unitTest")
  public void injectionBlockToEndInjectionBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test begin injection block to end injection block
  //

  @DataProvider(name = "beginInjectionBlockToEndInjectionBlockTestData")
  public Object[][] beginInjectionBlockToEndInjectionBlockTestData() {
    return new Object[][] {
      {PlatformBlockSignatures.getBlockExitSignature(INJECTION), METHOD_STATE.BEGIN_INJECTION_BLOCK, METHOD_STATE.END_INJECTION_BLOCK}
    };
  }

  @Test(dataProvider = "beginInjectionBlockToEndInjectionBlockTestData", groups = "unitTest")
  public void beginInjectionBlockToEndInjectionBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test end injection block to post injection block custom code
  //

  @DataProvider(name = "endInjectionBlockToPostInjectionBlockCustomCodeTestData")
  public Object[][] endInjectionBlockToPostInjectionBlockCustomCodeTestData() {
    return new Object[][] {
      {"", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
      {" ", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
      {"  ", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
      {"//", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
      {"var foo = Foo()", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
      {"guard let i = variable.value else {", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
      {"", METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE},
    };
  }

  @Test(dataProvider = "endInjectionBlockToPostInjectionBlockCustomCodeTestData", groups = "unitTest")
  public void endInjectionBlockToPostInjectionBlockCustomCodeTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test signature to begin operation block
  // Test lead in to begin operation block
  // Test end injection block to begin operation block
  // Test post injection block custom code to begin operation block
  // Test end operation block to begin operation block
  // Test post operation custom code to begin operation block
  //

  @DataProvider(name = "switchToBeginOperationBlockTestData")
  public Object[][] switchToBeginOperationBlockTestData() {
    return new Object[][] {
      {PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {" " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {"  " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.SIGNATURE, METHOD_STATE.BEGIN_OPERATION_BLOCK},

      {PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {" " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {"  " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.LEAD_IN, METHOD_STATE.BEGIN_OPERATION_BLOCK},

      {PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {" " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {"  " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.END_INJECTION_BLOCK, METHOD_STATE.BEGIN_OPERATION_BLOCK},

      {PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {" " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {"  " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE, METHOD_STATE.BEGIN_OPERATION_BLOCK},

      {PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {" " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {"  " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.BEGIN_OPERATION_BLOCK},

      {PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.POST_OPERATION_CUSTOM_CODE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {" " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.POST_OPERATION_CUSTOM_CODE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
      {"  " + PlatformBlockSignatures.getBlockEntrySignature(METHOD), METHOD_STATE.POST_OPERATION_CUSTOM_CODE, METHOD_STATE.BEGIN_OPERATION_BLOCK},
    };
  }

  @Test(dataProvider = "switchToBeginOperationBlockTestData", groups = "unitTest")
  public void switchToBeginOperationBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test begin operation block to operation call name
  //

  @DataProvider(name = "beginOperationToOperationCallNameTestData")
  public Object[][] beginOperationToOperationCallNameTestData() {
    return new Object[][] {
      {PlatformBlockSignatures.getMethodBlockCallSignature("CreateCheckoutSessionV2"), METHOD_STATE.BEGIN_OPERATION_BLOCK, METHOD_STATE.OPERATION_CALL_NAME}
    };
  }

  @Test(dataProvider = "beginOperationToOperationCallNameTestData", groups = "unitTest")
  public void beginOperationToOperationCallNameTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test operation call name to operation index
  //

  @DataProvider(name = "operationCallNameToOperationIndexTestData")
  public Object[][] operationCallNameToOperationIndexTestData() {
    return new Object[][] {
      {PlatformBlockSignatures.getMethodBlockOperationIndexSignature(1), METHOD_STATE.OPERATION_CALL_NAME, METHOD_STATE.OPERATION_INDEX}
    };
  }

  @Test(dataProvider = "operationCallNameToOperationIndexTestData", groups = "unitTest")
  public void operationCallNameToOperationIndexTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test operation index to operation block
  //

  @DataProvider(name = "operationIndexToOperationBlockTestData")
  public Object[][] operationIndexToOperationBlockTestData() {
    return new Object[][] {
      {"", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {" ", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {"  ", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {"//", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {" //", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {"  //", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {"let foo = Foo()", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
      {"  guard let foo = foo.value else {", METHOD_STATE.OPERATION_INDEX, METHOD_STATE.OPERATION_BLOCK},
    };
  }

  @Test(dataProvider = "operationIndexToOperationBlockTestData", groups = "unitTest")
  public void operationIndexToOperationBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test operation block to end operation block
  //

  @DataProvider(name = "operationBlockToEndOperationBlockTestData")
  public Object[][] operationBlockToEndOperationBlockTestData() {
    return new Object[][] {
      {PlatformBlockSignatures.getBlockExitSignature(METHOD), METHOD_STATE.OPERATION_BLOCK, METHOD_STATE.END_OPERATION_BLOCK}
    };
  }

  @Test(dataProvider = "operationBlockToEndOperationBlockTestData", groups = "unitTest")
  public void operationBlockToEndOperationBlockTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  //
  // Test end operation block to post operation custom code
  //

  @DataProvider(name = "endOperationBlockToPostOperationCustomCodeTestData")
  public Object[][] endOperationBlockToPostOperationCustomCodeTestData() {
    return new Object[][] {
      {"", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {" ", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {"  ", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {"//", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {" //", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {"  //", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {"var foo = Foo()", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
      {"  guard let i = foo.value else {", METHOD_STATE.END_OPERATION_BLOCK, METHOD_STATE.POST_OPERATION_CUSTOM_CODE},
    };
  }

  @Test(dataProvider = "endOperationBlockToPostOperationCustomCodeTestData", groups = "unitTest")
  public void endOperationBlockToPostOperationCustomCodeTest(String line, METHOD_STATE startingState, METHOD_STATE expectedState) {
    METHOD_STATE actualState = testParser.getMethodState(line, startingState, "fooBar");
    assertThat(String.format("Line: [%s], did not transfer from %s to %s correctly.", line, startingState, expectedState),  actualState, is(equalTo(expectedState)));
  }

  // ========================================
  // Test parsing
  // ========================================

  @Test(groups = "unitTest")
  public void parseTestFile() throws IOException {
    File file = new File(String.format("%s/./target/test-classes/com/ebay/service/logger/platform/ios/SOURCE_IosPopulatedTestClass.swift", cwd));

    IosSwiftFuiTestParser parser = new IosSwiftFuiTestParser();
    GeneralPlatformFileModel fileModel = parser.parse(file, "addAnotherBankAccountDeletesExisting");
    GeneralPlatformFileModel expectedModel = getExpectedModel();

    assertThat(fileModel.getClassDocumentation(), is(equalTo(expectedModel.getClassDocumentation())));
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

    fileModel.addClassDocumentationData("//");
    fileModel.addClassDocumentationData("//  SOURCE_IosPopulatedTestClass.swift");
    fileModel.addClassDocumentationData("//  UITests");
    fileModel.addClassDocumentationData("//");
    fileModel.addClassDocumentationData("//  Author:");
    fileModel.addClassDocumentationData("//  Copyright © 2019 eBay. All rights reserved.");
    fileModel.addClassDocumentationData("//");

    fileModel.addImportStatement("import XCTest");
    fileModel.addImportStatement("import ModularUITestSupport");
    fileModel.addImportStatement("");

    fileModel.addClassSignatureData("class AddAccount: UITestCase {");

    fileModel.addPostClassSignatureData("    ");
    fileModel.addPostClassSignatureData("    var form = FormPageModel()");
    fileModel.addPostClassSignatureData("    ");
    fileModel.addPostClassSignatureData("    var page = PageModel()");
    fileModel.addPostClassSignatureData("    ");

    fileModel.addMemberFieldStatement("    let xoHubPageModel = XoHubPageModel()");
    fileModel.addMemberFieldStatement("    let pspPageModel = PspPageModel()");
    fileModel.addMemberFieldStatement("    let xoSuccessPageModel = XoSuccessPageModel()");

    fileModel.addPreMethodData("    ");
    fileModel.addPreMethodData("    ");
    fileModel.addPreMethodData("    func testExample() throws {");
    fileModel.addPreMethodData("        ");
    fileModel.addPreMethodData("        // AUTO GENERATED MOCK INJECTION CODE BLOCK - DO NOT MODIFY CONTENTS");
    fileModel.addPreMethodData("        inject(responses: [");
    fileModel.addPreMethodData("            \"PaymentsModule.PPGetWalletListRequest_1\": \"AddABankAccount_addFirstBankAccount_1_WalletListV2.har\",");
    fileModel.addPreMethodData("            \"PaymentsModule.WalletAddPaymentDetailsRequest_2\": \"AddABankAccount_addFirstBankAccount_2_GetBlankPaymentOptionV2.har\",");
    fileModel.addPreMethodData("            \"PaymentsModule.WalletAddPaymentInstrumentRequest_3\": \"AddABankAccount_addFirstBankAccount_3_SavePaymentOptionV2.har\"");
    fileModel.addPreMethodData("            ])");
    fileModel.addPreMethodData("        // END OF AUTO GENERATED MOCK INJECTION CODE BLOCK");
    fileModel.addPreMethodData("        ");
    fileModel.addPreMethodData("        // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS");
    fileModel.addPreMethodData("        // CORRESPONDING API CALL: CreateCheckoutSessionV2");
    fileModel.addPreMethodData("        // OPERATION INDEX: 1");
    fileModel.addPreMethodData("        launchWalletEntry()");
    fileModel.addPreMethodData("        // END OF AUTO GENERATED METHOD CODE BLOCK");
    fileModel.addPreMethodData("        ");
    fileModel.addPreMethodData("        // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS");
    fileModel.addPreMethodData("        // CORRESPONDING API CALL: SetPayment");
    fileModel.addPreMethodData("        // OPERATION INDEX: 2");
    fileModel.addPreMethodData("        page.addPayoutOption.tap()");
    fileModel.addPreMethodData("        // END OF AUTO GENERATED METHOD CODE BLOCK");
    fileModel.addPreMethodData("        ");
    fileModel.addPreMethodData("        addBankAccount(withAcountNumber: \"123456123456\", withRoutingNumber: \"021000021\")");
    fileModel.addPreMethodData("        XCTAssertEqual(page.notificationText, \"You've added a checking account.\", \"Notification does not indicate a successful addition of user's bank account.\")");
    fileModel.addPreMethodData("    }");
    fileModel.addPreMethodData("    ");

    GeneralPlatformMethodModel methodModel = new GeneralPlatformMethodModel();
    methodModel.setMethodSignature("    func testAddAnotherBankAccountDeletesExisting() throws {");
    methodModel.addMethodLeadInContents("        testRailCaseId = \"8675309\"");
    methodModel.addMethodLeadInContents("        ");
    methodModel.addMethodLeadInContents("        ");

    GeneralPlatformOperationModel operation1 = new GeneralPlatformOperationModel();
    operation1.setServiceWrapperApiName("CreateCheckoutSessionV2");
    operation1.addCustomBlockLine("");
    operation1.addCustomBlockLine("        validateWalletEntry()");
    operation1.addCustomBlockLine("");
    methodModel.addMethodOperations(operation1);

    GeneralPlatformOperationModel operation2 = new GeneralPlatformOperationModel();
    operation2.setServiceWrapperApiName("SetPayment");
    operation2.addCustomBlockLine("");
    operation2.addCustomBlockLine("        let changePayoutMethodAlert = app.alerts[\"Change payout method\"].waitToExist()");
    operation2.addCustomBlockLine("        changePayoutMethodAlert.buttons[\"Continue\"].waitToExist().tap()");
    operation2.addCustomBlockLine("        addBankAccount(withAcountNumber: \"123456123456\", withRoutingNumber: \"021000021\")");
    operation2.addCustomBlockLine("        XCTAssertEqual(page.notificationText, \"You've added a checking account.\", \"Notification does not indicate a successful addition of user's bank account.\")");
    methodModel.addMethodOperations(operation2);

    fileModel.setMethodContents(methodModel);

    fileModel.addPostMethodData("    }");
    fileModel.addPostMethodData("    ");
    fileModel.addPostMethodData("    func tapAddPayoutAccount() {");
    fileModel.addPostMethodData("        app.element(containing: \"_BankAccount\").waitToExist().tap()");
    fileModel.addPostMethodData("    }");
    fileModel.addPostMethodData("    ");
    fileModel.addPostMethodData("    func addBankAccount(withAcountNumber: String, withRoutingNumber: String) {");
    fileModel.addPostMethodData("        ");
    fileModel.addPostMethodData("        form.bankNickName.paste(text: \"EvilBank\")");
    fileModel.addPostMethodData("        form.accountUserName.paste(text: \"BankVictim\")");
    fileModel.addPostMethodData("        form.routingNumber.paste(text: withRoutingNumber)");
    fileModel.addPostMethodData("        form.accountNumber.paste(text: withAcountNumber)");
    fileModel.addPostMethodData("        try! form.tapAddButton()");
    fileModel.addPostMethodData("    }");
    fileModel.addPostMethodData("}");

    return fileModel;
  }
}
