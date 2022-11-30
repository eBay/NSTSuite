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

public class AndroidJavaFuiTestParser {

  private static final String methodNameMatcher = ".*?(public).*?(void).*?(%s)(\\()";
  private long methodCurlyBraceCounter = 0;
  private static final boolean enableLogging = false;

  public enum STATE {
    PACKAGE,
    IMPORTS,
    PRE_CLASS_SIGNATURE,
    CLASS_SIGNATURE,
    POST_CLASS_SIGNATURE,
    MEMBER_FIELDS,
    PRE_METHOD,
    METHOD,
    POST_METHOD
  }

  public GeneralPlatformFileModel parse(File file, String methodName) throws IOException {

    if (file == null) {
      throw new IllegalArgumentException("Cannot parse a null file reference.");
    }

    FileReaderWithEncoding reader = new FileReaderWithEncoding(file, Encode.UTF_8);
    Reporter.log(String.format("Parsing platform file: %s", file.getAbsoluteFile()), true);

    STATE parseState = STATE.PACKAGE;
    GeneralPlatformFileModel fileModel = new GeneralPlatformFileModel();
    resetCurlyBraceCounter();

    // These fields are specifically used to parse auto generated method blocks.
    boolean beforeGeneratedMethodBlocks = true;
    boolean insideGeneratedMethodBlock = false;
    GeneralPlatformOperationModel platformOperationModel = null;

    String line = reader.readLine();
    while (line != null) {

      parseState = getParsingState(line, parseState, methodName);

      switch (parseState) {
      case PACKAGE:
        if (!line.trim().isEmpty()) {
          fileModel.setPackageName(line);
        }
        break;
      case IMPORTS:
        fileModel.addImportStatement(line);
        break;
      case PRE_CLASS_SIGNATURE:
        fileModel.addPreClassSignatureData(line);
        break;
      case CLASS_SIGNATURE:
        if (fileModel.getClassSignature() != null) {
          line = fileModel.getClassSignature() + "\n" + line;
        }
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

        if (methodSignatureMatched(line, methodName)) {

          fileModel.setMethodContentsSignature(line);

        } else if (PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.METHOD) {

          beforeGeneratedMethodBlocks = false;
          insideGeneratedMethodBlock = true;
          if (platformOperationModel != null) {
            fileModel.addMethodContentsOperation(platformOperationModel);
          }
          platformOperationModel = new GeneralPlatformOperationModel();

        } else if (PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.METHOD) {

          insideGeneratedMethodBlock = false;

        } else if (insideGeneratedMethodBlock) {

          if (PlatformBlockSignatures.isMethodBlockAPICallComment(line)) {
            String apiName = PlatformBlockSignatures.getMethodBlockAPICallName(line);
            platformOperationModel.setServiceWrapperApiName(apiName);
          }

        } else if (beforeGeneratedMethodBlocks) {

          fileModel.addMethodContentsLeadIn(line);

        } else if (!insideGeneratedMethodBlock) {

          platformOperationModel.addCustomBlockLine(line);

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
      throw new IllegalStateException(String.format("Android platform FUI class %s was not fully parsed.", file.getAbsolutePath()));
    }

    return fileModel;
  }

  protected void resetCurlyBraceCounter() {
    methodCurlyBraceCounter = 0;
  }

  protected STATE getParsingState(String line, STATE currentState, String methodName) {

    if (enableLogging) {
      Reporter.log(String.format("PARSER STATE EVAL - current state: %s | line: %s", currentState.name(), line), true);
    }

    boolean methodSignatureMatched = methodSignatureMatched(line, methodName);
    STATE newState = currentState;

    if (newState == STATE.PACKAGE && line.startsWith("import ")) {
      newState = STATE.IMPORTS;
    } else if ((newState == STATE.PACKAGE || newState == STATE.PRE_CLASS_SIGNATURE || newState == STATE.IMPORTS) && line.startsWith("public class")) {
      newState = STATE.CLASS_SIGNATURE;
    } else if ((newState == STATE.PACKAGE || newState == STATE.IMPORTS) && line.startsWith("/")) {
      newState = STATE.PRE_CLASS_SIGNATURE;
    } else if ((newState == STATE.POST_CLASS_SIGNATURE || newState == STATE.CLASS_SIGNATURE) && (PlatformBlockSignatures.getBlockEntrySignatureType(line) == BlockSignatureType.MEMBER_FIELD)) {
      newState = STATE.MEMBER_FIELDS;
    } else if (newState == STATE.CLASS_SIGNATURE && !line.matches("^[\\s]*\\{$")) {
      newState = STATE.POST_CLASS_SIGNATURE;
    }  else if (newState == STATE.MEMBER_FIELDS && (PlatformBlockSignatures.getBlockExitSignatureType(line) == BlockSignatureType.MEMBER_FIELD)) {
      newState = STATE.PRE_METHOD;
    } else if ((newState == STATE.POST_CLASS_SIGNATURE || newState == STATE.PRE_METHOD) && methodSignatureMatched) {
      methodCurlyBraceCounter = line.chars().filter(ch -> ch == '{').count();
      methodCurlyBraceCounter -= line.chars().filter(ch -> ch == '}').count();
      newState = STATE.METHOD;
    } else if (newState == STATE.METHOD && methodCurlyBraceCounter >= 0) {
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
    String methodNameMatch = String.format(methodNameMatcher, methodName);
    Pattern pattern = Pattern.compile(methodNameMatch, Pattern.DOTALL);
    Matcher matcher = pattern.matcher(line);
    return matcher.find();
  }
}
