package com.ebay.service.logger.platforms.blocksignatures;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.testng.annotations.Test;

public class PlatformBlockSignaturesTest {

  @Test(groups = "unitTest")
  public void getBlockSignatureEntryWithValidType() {
    String text = PlatformBlockSignatures.getBlockEntrySignature(BlockSignatureType.MEMBER_FIELD);
    assertThat(text, is(equalTo("// AUTO GENERATED MEMBER FIELD CODE BLOCK - DO NOT MODIFY CONTENTS")));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getBlockSignatureEntryWithNullType() {
    PlatformBlockSignatures.getBlockEntrySignature(null);
  }

  @Test(groups = "unitTest")
  public void getBlockSignatureExitWithValidType() {
    String text = PlatformBlockSignatures.getBlockExitSignature(BlockSignatureType.MEMBER_FIELD);
    assertThat(text, is(equalTo("// END OF AUTO GENERATED MEMBER FIELD CODE BLOCK")));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getBlockSignatureExitWithNullType() {
    PlatformBlockSignatures.getBlockExitSignature(null);
  }

  @Test(groups = "unitTest")
  public void getMethodBlockCallSignatureWithValidApiString() {
    String text = PlatformBlockSignatures.getMethodBlockCallSignature("CreateCheckoutSessionV2");
    assertThat(text, is(equalTo("// CORRESPONDING API CALL: CreateCheckoutSessionV2")));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getMethodBlockCallSignatureWithNullApiString() {
    PlatformBlockSignatures.getMethodBlockCallSignature(null);
  }

  @Test(groups = "unitTest")
  public void getMethodBlockOperationIndexSignatureWithValidIndexNumber() {
    String text = PlatformBlockSignatures.getMethodBlockOperationIndexSignature(1);
    assertThat(text, is(equalTo("// OPERATION INDEX: 1")));
  }

  @Test(groups = "unitTest")
  public void getMethodBlockOperationIndexSignatureWithIndexZero() {
    String text = PlatformBlockSignatures.getMethodBlockOperationIndexSignature(0);
    assertThat(text, is(equalTo("// OPERATION INDEX: 0")));
  }

  @Test(groups = "unitTest")
  public void getBlockEntrySignatureTypeFromValidBlockString() {
    BlockSignatureType type = PlatformBlockSignatures.getBlockEntrySignatureType("// AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS");
    assertThat(type, is(equalTo(BlockSignatureType.METHOD)));
  }

  @Test(groups = "unitTest")
  public void getBlockEntrySignatureTypeFromValidBlockStringWithExtraWhitespacePadding() {
    BlockSignatureType type = PlatformBlockSignatures.getBlockEntrySignatureType("   // AUTO GENERATED METHOD CODE BLOCK - DO NOT MODIFY CONTENTS  ");
    assertThat(type, is(equalTo(BlockSignatureType.METHOD)));
  }

  @Test(groups = "unitTest")
  public void getBlockEntrySignatureTypeFromInvalidBlockString() {
    BlockSignatureType type = PlatformBlockSignatures.getBlockEntrySignatureType("// AUTO GENERATED FOO CODE BLOCK - DO NOT MODIFY CONTENTS");
    assertThat(type, is(equalTo(BlockSignatureType.UNKNOWN)));
  }

  @Test(groups = "unitTest")
  public void getBlockExitSignatureTypeFromValidBlockString() {
    BlockSignatureType type = PlatformBlockSignatures.getBlockExitSignatureType("// END OF AUTO GENERATED METHOD CODE BLOCK");
    assertThat(type, is(equalTo(BlockSignatureType.METHOD)));
  }

  @Test(groups = "unitTest")
  public void getBlockExitSignatureTypeFromValidBlockStringWithExtraWhitespacePadding() {
    BlockSignatureType type = PlatformBlockSignatures.getBlockExitSignatureType("   // END OF AUTO GENERATED METHOD CODE BLOCK  ");
    assertThat(type, is(equalTo(BlockSignatureType.METHOD)));
  }

  @Test(groups = "unitTest")
  public void getBlockExitSignatureTypeFromInvalidBlockString() {
    BlockSignatureType type = PlatformBlockSignatures.getBlockExitSignatureType("// AUTO GENERATED FOO CODE BLOCK - DO NOT MODIFY CONTENTS");
    assertThat(type, is(equalTo(BlockSignatureType.UNKNOWN)));
  }

  @Test(groups = "unitTest")
  public void methodBlockApiSignatureMatch() {
    boolean match = PlatformBlockSignatures.isMethodBlockAPICallComment("    // CORRESPONDING API CALL: Session");
    assertThat(match, is(equalTo(true)));
  }

  @Test(groups = "unitTest")
  public void methodBlockApiSignatureMismatch() {
    boolean match = PlatformBlockSignatures.isMethodBlockAPICallComment("    // Some other comment");
    assertThat(match, is(equalTo(false)));
  }

  @Test(groups = "unitTest")
  public void getApiNameFromValidComment() {
    String apiName = PlatformBlockSignatures.getMethodBlockAPICallName("    // CORRESPONDING API CALL: Session call");
    assertThat(apiName, is(equalTo("Session call")));
  }

  @Test(groups = "unitTest")
  public void getNullApiNameFromInvalidComment() {
    String apiName = PlatformBlockSignatures.getMethodBlockAPICallName("    // Some other comment");
    assertThat(apiName, is(nullValue()));
  }

  @Test(groups = "unitTest")
  public void methodBlockOperationIndexMatch() {
    boolean match = PlatformBlockSignatures.isMethodBlockOperationIndexComment("     // OPERATION INDEX: 1");
    assertThat(match, is(equalTo(true)));
  }

  @Test(groups = "unitTest")
  public void methodBlockOperationIndexMismatch() {
    boolean match = PlatformBlockSignatures.isMethodBlockOperationIndexComment("     // Some other comment");
    assertThat(match, is(equalTo(false)));
  }
}
