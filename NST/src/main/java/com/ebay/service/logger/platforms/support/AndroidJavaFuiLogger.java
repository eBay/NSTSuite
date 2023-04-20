package com.ebay.service.logger.platforms.support;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.testng.Reporter;

import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType;
import com.ebay.service.logger.platforms.blocksignatures.PlatformBlockSignatures;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformMethodModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;
import com.ebay.service.logger.platforms.processors.parser.AndroidJavaFuiTestParser;
import com.ebay.service.logger.platforms.util.PlatformConstants;
import com.ebay.service.logger.platforms.util.PlatformLoggerUtil;
import com.ebay.service.logger.writer.Encode;
import com.ebay.service.logger.writer.FileWriterWithEncoding;

public class AndroidJavaFuiLogger {

  public void updateFuiFile(String className, String methodName, List<ServiceCallCacheData> calls) {

    File file = PlatformLoggerUtil.getTestFile(className);
    if (file != null) {

      GeneralPlatformFileModel fileModel = getFileModel(file, className, methodName);
      if (fileModel != null) {

        PlatformLoggerUtil.updatedOperations(fileModel, calls);

        try {
          writeUpdatedFile(file, fileModel);
          Reporter.log("Added test class statements to Android test class: " + file, true);
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void writeUpdatedFile(File file, GeneralPlatformFileModel fileModel) throws IOException, URISyntaxException {

    StringBuilder methodBlockBuilder = new StringBuilder();

    // Iterate over the operations and build up the file contents.
    GeneralPlatformMethodModel method = fileModel.getMethodContents();

    String methodContentsIndent = method.getMethodContentsIndent();
    List<GeneralPlatformOperationModel> operations = method.getMethodOperations();

    List<String> newImportStatements = PlatformLoggerUtil.getImportStatements(operations);
    List<String> newMemberVariables = PlatformLoggerUtil.getMemberVariableStatements(operations);
    String methodOperationStatements = PlatformLoggerUtil.getMethodBlockStatements(operations, methodContentsIndent);

    methodBlockBuilder.append(method.getMethodSignature() + "\n");

    // Remove auto-generated Assert.fail and TODO lines
    String methodLeadInContents = method.getMethodLeadInContents();
    String contentsTrimmed = methodLeadInContents.trim();

    if (contentsTrimmed.matches("^\\{[\\s]*//[\\s]*TODO:[\\s]*implement test")) {
      methodBlockBuilder.append("\t{\n\n");
    } else if (contentsTrimmed.matches("^[\\s]*//[\\s]*TODO:[\\s]*implement test")) {
      methodBlockBuilder.append("\n");
    } else {
      methodBlockBuilder.append(methodLeadInContents);
    }

    methodBlockBuilder.append(methodOperationStatements);

    // Write everything to file
    FileWriterWithEncoding writer = new FileWriterWithEncoding(file, Encode.UTF_8);

    // Write package
    writer.write(fileModel.getPackageName());
    writer.write("\n\n");

    // Possibly need to append imports
    if (fileModel.getImportContents() != null) {

      String[] existingImportStatements = fileModel.getImportContents().getImportStatements();

      for (String importStatement : existingImportStatements) {
        if (!newImportStatements.contains(importStatement) && !importStatement.trim().isEmpty()) {
          newImportStatements.add(importStatement);
        }
      }

      for (String statement : newImportStatements) {
        String importLine = statement + "\n";
        writer.write(importLine);
      }

      writer.write("\n");
    }

    // Write pre-class data
    writer.write(fileModel.getPreClassSignature());

    // Write class header
    writer.write(fileModel.getClassSignature());

    // Write member fields
    if (newMemberVariables.size() > 0) {

      String tab = PlatformConstants.getPlatformSpecificTab();

      writer.write("\n\n");
      writer.write(tab);
      writer.write(PlatformBlockSignatures.getBlockEntrySignature(BlockSignatureType.MEMBER_FIELD));
      writer.write("\n");
      for (String memberVariable : newMemberVariables) {
        writer.write(tab);
        writer.write(memberVariable);
        writer.write("\n");
      }
      writer.write(tab);
      writer.write(PlatformBlockSignatures.getBlockExitSignature(BlockSignatureType.MEMBER_FIELD));
      writer.write("\n");
    }

    // Write post-class data
    String getPostClassSignature = fileModel.getPostClassSignature();
    if (!getPostClassSignature.isEmpty() && !PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace(getPostClassSignature)) {
      writer.write("\n");
    }
    writer.write(getPostClassSignature);

    // Pre method
    String preMethodFileContents = fileModel.getPreMethodFileContents();
    if (preMethodFileContents != null) {

      boolean hasnewLineAtBeginning = PlatformLoggerUtil.stringLeadinContainsNewlineCharacterAmongWhitespace(preMethodFileContents);

      if (!preMethodFileContents.isEmpty() && !hasnewLineAtBeginning) {
        writer.write("\n");
      }

      writer.write(preMethodFileContents);
    }

    // Write method contents
    writer.write(methodBlockBuilder.toString());

    // post method
    if (fileModel.getPostMethodFileContents() != null) {
      writer.write(fileModel.getPostMethodFileContents());
    }

    writer.close();
  }

  private GeneralPlatformFileModel getFileModel(File file, String className, String methodName) {

    try {
      AndroidJavaFuiTestParser parser = new AndroidJavaFuiTestParser();
      return parser.parse(file, methodName);
    } catch (IOException e) {
      Reporter.log(String.format("Unable to parse file: %s, for method: %s.", className, methodName), true);
      e.printStackTrace();
      return null;
    } catch (IllegalStateException e) {
      Reporter.log(e.getMessage(), true);
      e.printStackTrace();
      return null;
    }
  }
}
