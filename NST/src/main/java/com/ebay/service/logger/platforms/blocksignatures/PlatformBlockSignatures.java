package com.ebay.service.logger.platforms.blocksignatures;

public class PlatformBlockSignatures {

  private static final String commonBlockEntrySignature = "// AUTO GENERATED %s CODE BLOCK - DO NOT MODIFY CONTENTS";
  private static final String commonBlockExitSignature = "// END OF AUTO GENERATED %s CODE BLOCK";
  private static final String methodBlockApiCallSignature = "// CORRESPONDING API CALL:";
  private static final String methodBlockOperationIndexSignature = "// OPERATION INDEX:";

  /**
   * Get the block entry signature for the specified block signature type. No whitespace indentation applied.
   * @param type Block signature type to apply.
   * @return Block entry signature comment line.
   */
  public static String getBlockEntrySignature(BlockSignatureType type) {
    if (type == null) {
      throw new IllegalArgumentException("Block entry signature type cannot be null.");
    }
    return String.format(commonBlockEntrySignature, type.getValue());
  }

  /**
   * Get the block exit signature for the specified block signature type. No whitespace indentation applied.
   * @param type Block signature type to apply.
   * @return Block exit signature comment line.
   */
  public static String getBlockExitSignature(BlockSignatureType type) {
    if (type == null) {
      throw new IllegalArgumentException("Block entry signature type cannot be null.");
    }
    return String.format(commonBlockExitSignature, type.getValue());
  }

  /**
   * Get the method block API call signature for the specified API call name. No whitespace indentation applied.
   * @param apiCallName API call name to apply.
   * @return Method block API call signature comment line.
   */
  public static String getMethodBlockCallSignature(String apiCallName) {
    if (apiCallName == null) {
      throw new IllegalArgumentException("API call name cannot be null.");
    }
    return String.format((methodBlockApiCallSignature + " %s"), apiCallName);
  }

  /**
   * Get the method block operation index signature for the specified API call name. No whitespace indentation applied.
   * @param operationIndex Operation index value to apply.
   * @return Method block operation index signature comment line.
   */
  public static String getMethodBlockOperationIndexSignature(int operationIndex) {
    return String.format((methodBlockOperationIndexSignature + " %d"), operationIndex);
  }

  /**
   * Get the block entry signature type from a block entry signature line.
   * @param fromLine Block entry signature line to evaluate.
   * @return BlockSignatureType matched, or UNKNOWN if not matched.
   */
  public static BlockSignatureType getBlockEntrySignatureType(String fromLine) {

    fromLine = fromLine.trim();

    for (BlockSignatureType type : BlockSignatureType.values()) {
      if (String.format(commonBlockEntrySignature, type.getValue()).equals(fromLine)) {
        return type;
      }
    }

    return BlockSignatureType.UNKNOWN;
  }

  /**
   * Get the block exit signature type from a block exit signature line.
   * @param fromLine Block exit signature line to evaluate.
   * @return BlockSignatureType matched, or UNKNOWN if not matched.
   */
  public static BlockSignatureType getBlockExitSignatureType(String fromLine) {

    fromLine = fromLine.trim();

    for (BlockSignatureType type : BlockSignatureType.values()) {
      if (String.format(commonBlockExitSignature, type.getValue()).equals(fromLine)) {
        return type;
      }
    }

    return BlockSignatureType.UNKNOWN;
  }

  /**
   * Determine if the passed in line contains the method block API call signature.
   * @param line Line of text to evaluate.
   * @return True if this is the method block API call comment, false otherwise.
   */
  public static boolean isMethodBlockAPICallComment(String line) {
    return line.trim().startsWith(methodBlockApiCallSignature);
  }

  /**
   * Return the API call name specified in the method block line provided.
   * @param line Method block line to pull API call name from.
   * @return API call name or null if the line provided is not a method API call block.
   */
  public static String getMethodBlockAPICallName(String line) {

    String apiCallName = null;

    if (isMethodBlockAPICallComment(line)) {
      apiCallName = line.replace(methodBlockApiCallSignature, "");
      apiCallName = apiCallName.trim();
    }

    return apiCallName;
  }

  /**
   * Determine if the passed in line contains the method block operation index signature.
   * @param line Line of text to evaluate.
   * @return True if this is the method block operation index comment, false otherwise.
   */
  public static boolean isMethodBlockOperationIndexComment(String line) {
    return line.trim().startsWith(methodBlockOperationIndexSignature);
  }
}
