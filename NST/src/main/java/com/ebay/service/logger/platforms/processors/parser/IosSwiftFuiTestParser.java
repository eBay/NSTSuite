package com.ebay.service.logger.platforms.processors.parser;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Reporter;

import com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType;
import com.ebay.service.logger.platforms.blocksignatures.PlatformBlockSignatures;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;
import com.ebay.service.logger.reader.FileReaderWithEncoding;
import com.ebay.service.logger.writer.Encode;

public class IosSwiftFuiTestParser {

  private static final String methodNameMatcher = ".*?(func).*?(test).*?(%s\\().*?";
  private long methodCurlyBraceCounter = 0;
  private static final boolean enableLogging = false;

  public enum STATE {
    CLASS_DOCUMENTATION,
    IMPORTS,
    PRE_CLASS_SIGNATURE,
    CLASS_SIGNATURE,
    POST_CLASS_SIGNATURE,
    MEMBER_FIELDS,
    PRE_METHOD,
    METHOD,
    POST_METHOD
  }

  public enum METHOD_STATE {
    SIGNATURE,
    LEAD_IN,
    BEGIN_INJECTION_BLOCK,
    INJECTION_BLOCK,
    END_INJECTION_BLOCK,
    POST_INJECTION_BLOCK_CUSTOM_CODE,
    BEGIN_OPERATION_BLOCK,
    OPERATION_CALL_NAME,
    OPERATION_INDEX,
    OPERATION_BLOCK,
    END_OPERATION_BLOCK,
    POST_OPERATION_CUSTOM_CODE,
  }

  /**
   * Parse the specified file and search for the specified method name while
   * parsing. If the method exists, the contents of the method will be extracted
   * into the data model returned.
   *
   * @param file
   *          Swift file to parse.
   * @param methodName
   *          Method name (minus the 'test' prefix).
   * @return Data model for the parsed file.
   * @throws IOException
   *           For file access issues. Will also throw an error if file is not
   *           parsed correctly.
   */
  public GeneralPlatformFileModel parse(File file, String methodName) throws IOException {

    if (file == null) {
      throw new IllegalArgumentException("Cannot parse a null file reference.");
    }

    FileReaderWithEncoding reader = new FileReaderWithEncoding(file, Encode.UTF_8);
    Reporter.log(String.format("Parsing platform file: %s", file.getAbsoluteFile()), true);

    STATE parseState = STATE.CLASS_DOCUMENTATION;
    METHOD_STATE methodParseState = METHOD_STATE.SIGNATURE;

    GeneralPlatformFileModel fileModel = new GeneralPlatformFileModel();
    resetCurlyBraceCounter();

    GeneralPlatformOperationModel platformOperationModel = null;

    String line = reader.readLine();
    while (line != null) {

      parseState = getParsingState(line, parseState, methodName);

      switch (parseState) {
      case CLASS_DOCUMENTATION:
        fileModel.addClassDocumentationData(line);
        break;
      case IMPORTS:
        fileModel.addImportStatement(line);
        break;
      case PRE_CLASS_SIGNATURE:
        fileModel.addPreClassSignatureData(line);
        break;
      case CLASS_SIGNATURE:
        fileModel.addClassSignatureData(line);
        break;
      case POST_CLASS_SIGNATURE:
        fileModel.addPostClassSignatureData(line);
        break;
      case MEMBER_FIELDS:
        if (PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.MEMBER_FIELD) {
          break;
        } else if (PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.MEMBER_FIELD) {
          break;
        }
        fileModel.addMemberFieldStatement(line);
        break;
      case PRE_METHOD:

        if (PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.MEMBER_FIELD) {
          break;
        }

        fileModel.addPreMethodData(line);
        break;
      case METHOD:

        methodParseState = getMethodState(line, methodParseState, methodName);

        switch (methodParseState) {
        case SIGNATURE:
          fileModel.setMethodContentsSignature(line);
          break;
        case LEAD_IN:
          fileModel.addMethodContentsLeadIn(line);
          break;
        case BEGIN_INJECTION_BLOCK:
          // Nothing to do - ignore, we will re-populate
          break;
        case INJECTION_BLOCK:
          // Ignore, we will re-populate
          break;
        case END_INJECTION_BLOCK:
          // Nothing to do, ignore, we will re-populate
          break;
        case POST_INJECTION_BLOCK_CUSTOM_CODE:
          fileModel.addMethodContentsLeadIn(line);
          break;
        case BEGIN_OPERATION_BLOCK:
          if (platformOperationModel != null) {
            fileModel.addMethodContentsOperation(platformOperationModel);
          }
          platformOperationModel = new GeneralPlatformOperationModel();
          break;
        case OPERATION_CALL_NAME:
          if (PlatformBlockSignatures.isMethodBlockAPICallComment(line)) {
            String apiName = PlatformBlockSignatures.getMethodBlockAPICallName(line);
            platformOperationModel.setServiceWrapperApiName(apiName);
          }
          break;
        case OPERATION_INDEX:
          // Ignore, we will re-populate
          break;
        case OPERATION_BLOCK:
          // Ignore, we will re-populate
          break;
        case END_OPERATION_BLOCK:
          // Ignore, we will re-populate
          break;
        case POST_OPERATION_CUSTOM_CODE:
          if (line.trim().isEmpty()) {
            line = "";
          }
          platformOperationModel.addCustomBlockLine(line);
          break;
        }

        break;
      case POST_METHOD:
        fileModel.addPostMethodData(line);
        break;
      }

      line = reader.readLine();
    }

    // Add the last operation model constructed.
    if (platformOperationModel != null) {
      fileModel.addMethodContentsOperation(platformOperationModel);
    }

    if (parseState != STATE.POST_METHOD) {
      throw new IllegalStateException(String.format("iOS platform FUI class %s was not fully parsed.", file.getAbsolutePath()));
    }

    return fileModel;
  }

  protected void resetCurlyBraceCounter() {
    methodCurlyBraceCounter = 0;
  }

  /**
   * State transition logic. Takes the current state and the line from the file
   * to determine which state to transition to. This is secondary to the
   * getParsingState() method which has overall precedence in the parsing of the
   * file.
   *
   * @param line
   *          Line read from file.
   * @param currentState
   *          Current state for method parsing.
   * @param methodName
   *          Name of the method we want to locate.
   * @return State to transition to based upon the line in the file we are
   *         parsing.
   */
  protected METHOD_STATE getMethodState(String line, METHOD_STATE currentState, String methodName) {

    if (enableLogging) {
      Reporter.log(String.format("PARSER METHOD_STATE EVAL - current state: %s | line: %s", currentState.name(), line), true);
    }

    boolean methodSignatureMatched = methodSignatureMatched(line, methodName);
    METHOD_STATE newState = currentState;

    if (methodSignatureMatched) {
      newState = METHOD_STATE.SIGNATURE;
    } else if (currentState == METHOD_STATE.SIGNATURE && PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.UNKNOWN) {
      newState = METHOD_STATE.LEAD_IN;
    } else if ((currentState == METHOD_STATE.SIGNATURE || currentState == METHOD_STATE.LEAD_IN) && PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.INJECTION) {
      newState = METHOD_STATE.BEGIN_INJECTION_BLOCK;
    } else if (currentState == METHOD_STATE.BEGIN_INJECTION_BLOCK && PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.UNKNOWN) {
      newState = METHOD_STATE.INJECTION_BLOCK;
    } else if ((currentState == METHOD_STATE.BEGIN_INJECTION_BLOCK || currentState == METHOD_STATE.INJECTION_BLOCK)
        && PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.INJECTION) {
      newState = METHOD_STATE.END_INJECTION_BLOCK;
    } else if (currentState == METHOD_STATE.END_INJECTION_BLOCK && PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.UNKNOWN) {
      newState = METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE;
    } else if ((currentState == METHOD_STATE.END_OPERATION_BLOCK
        || currentState == METHOD_STATE.END_INJECTION_BLOCK
        || currentState == METHOD_STATE.POST_INJECTION_BLOCK_CUSTOM_CODE
        || currentState == METHOD_STATE.LEAD_IN
        || currentState == METHOD_STATE.SIGNATURE
        || currentState == METHOD_STATE.POST_OPERATION_CUSTOM_CODE) && PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.METHOD) {
      newState = METHOD_STATE.BEGIN_OPERATION_BLOCK;
    } else if (currentState == METHOD_STATE.BEGIN_OPERATION_BLOCK && PlatformBlockSignatures.isMethodBlockAPICallComment(line)) {
      newState = METHOD_STATE.OPERATION_CALL_NAME;
    } else if (currentState == METHOD_STATE.OPERATION_CALL_NAME && PlatformBlockSignatures.isMethodBlockOperationIndexComment(line)) {
      newState = METHOD_STATE.OPERATION_INDEX;
    } else if (currentState == METHOD_STATE.OPERATION_INDEX && PlatformBlockSignatures.getBlockExitSignatureType(line) != BlockSignatureType.METHOD) {
      newState = METHOD_STATE.OPERATION_BLOCK;
    } else if (currentState == METHOD_STATE.OPERATION_BLOCK && PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.METHOD) {
      newState = METHOD_STATE.END_OPERATION_BLOCK;
    } else if (currentState == METHOD_STATE.END_OPERATION_BLOCK && PlatformBlockSignatures.getBlockEntrySignatureType(line) != BlockSignatureType.METHOD) {
      newState = METHOD_STATE.POST_OPERATION_CUSTOM_CODE;
    }

    if (enableLogging) {
      Reporter.log(String.format("PARSER METHOD_STATE EVAL RESULT - state: %s", newState.name()), true);
    }

    return newState;
  }

  /**
   * State transition logic. Takes the current state and the line from the file
   * to determine which state to transition to. Main state consideration for
   * parsing files.
   *
   * @param line
   *          Line read from file.
   * @param currentState
   *          Current state for file parsing.
   * @param methodName
   *          Name of the method we want to locate.
   * @return State to transition to based upon the line in the file we are
   *         parsing.
   */
  protected STATE getParsingState(String line, STATE currentState, String methodName) {

    if (enableLogging) {
      Reporter.log(String.format("PARSER STATE EVAL - current state: %s | line: %s", currentState.name(), line), true);
    }

    boolean methodSignatureMatched = methodSignatureMatched(line, methodName);
    STATE newState = currentState;

    if (currentState == STATE.CLASS_DOCUMENTATION && line.trim().startsWith("import ")) {
      newState = STATE.IMPORTS;
    } else if ((currentState == STATE.IMPORTS || currentState == STATE.CLASS_DOCUMENTATION || currentState == STATE.PRE_CLASS_SIGNATURE) && line.trim().startsWith("class ")) {
      newState = STATE.CLASS_SIGNATURE;
    } else if (currentState == STATE.IMPORTS && !line.trim().startsWith("import ") && !line.trim().isEmpty()) {
      newState = STATE.PRE_CLASS_SIGNATURE;
    } else if (currentState == STATE.CLASS_SIGNATURE && !line.trim().startsWith("class ") && PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.UNKNOWN) {
      newState = STATE.POST_CLASS_SIGNATURE;
    } else if ((currentState == STATE.CLASS_SIGNATURE || currentState == STATE.POST_CLASS_SIGNATURE) && PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.MEMBER_FIELD) {
      newState = STATE.MEMBER_FIELDS;
    } else if (currentState == STATE.MEMBER_FIELDS && (PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.MEMBER_FIELD) && !methodSignatureMatched) {
      newState = STATE.PRE_METHOD;
    } else if ((currentState == STATE.MEMBER_FIELDS || currentState == STATE.PRE_METHOD || currentState == STATE.POST_CLASS_SIGNATURE) && methodSignatureMatched) {
      methodCurlyBraceCounter = line.chars().filter(ch -> ch == '{').count();
      methodCurlyBraceCounter -= line.chars().filter(ch -> ch == '}').count();
      newState = STATE.METHOD;
    } else if (currentState == STATE.METHOD && methodCurlyBraceCounter > 0) {
      methodCurlyBraceCounter += line.chars().filter(ch -> ch == '{').count();
      methodCurlyBraceCounter -= line.chars().filter(ch -> ch == '}').count();
    }

    if (currentState == STATE.METHOD && newState == STATE.METHOD && methodCurlyBraceCounter <= 0) {
      newState = STATE.POST_METHOD;
    }

    if (enableLogging) {
      Reporter.log(String.format("PARSER STATE EVAL RESULT - state: %s", newState.name()), true);
    }

    return newState;
  }

  private boolean methodSignatureMatched(String line, String methodName) {
    String methodNameMatch = String.format(methodNameMatcher, methodName.toLowerCase());
    Pattern pattern = Pattern.compile(methodNameMatch, Pattern.DOTALL);
    Matcher matcher = pattern.matcher(line.toLowerCase());
    return matcher.find();
  }
}
