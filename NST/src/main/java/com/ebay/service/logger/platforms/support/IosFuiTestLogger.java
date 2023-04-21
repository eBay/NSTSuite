package com.ebay.service.logger.platforms.support;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.ebay.runtime.event.ObserverPayload;
import org.testng.Reporter;

import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType;
import com.ebay.service.logger.platforms.blocksignatures.PlatformBlockSignatures;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformMethodModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;
import com.ebay.service.logger.platforms.processors.parser.IosSwiftFuiTestParser;
import com.ebay.service.logger.platforms.util.PlatformApiToFuiMapping;
import com.ebay.service.logger.platforms.util.PlatformApiToFuiMappingParser;
import com.ebay.service.logger.platforms.util.PlatformConstants;
import com.ebay.service.logger.platforms.util.PlatformLoggerUtil;
import com.ebay.service.logger.writer.Encode;
import com.ebay.service.logger.writer.FileWriterWithEncoding;

public class IosFuiTestLogger {

  public void updateFuiFile(String className, String methodName, List<ServiceCallCacheData> callLog) {

    File file = PlatformLoggerUtil.getTestFile(className);
    if (file != null) {

      GeneralPlatformFileModel fileModel = getFileModel(file, className, methodName);
      if (fileModel != null) {

        PlatformLoggerUtil.updatedOperations(fileModel, callLog);

        try {
          writeUpdatedFile(file, fileModel, callLog, className, methodName);
          Reporter.log("Added test class statements to iOS test class: " + file, true);
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void writeUpdatedFile(File file, GeneralPlatformFileModel fileModel, List<ServiceCallCacheData> callLog, String className, String methodName) throws IOException, URISyntaxException {

    StringBuilder methodBlockBuilder = new StringBuilder();

    // Iterate over the operations and build up the file contents.
    GeneralPlatformMethodModel method = fileModel.getMethodContents();

    String methodContentsIndent = method.getMethodContentsIndent();
    List<GeneralPlatformOperationModel> operations = method.getMethodOperations();

    List<String> newImportStatements = PlatformLoggerUtil.getImportStatements(operations);
    List<String> newMemberVariables = PlatformLoggerUtil.getMemberVariableStatements(operations);
    String methodOperationStatements = PlatformLoggerUtil.getMethodBlockStatements(operations, methodContentsIndent);
    String injectStatements = getInjectStatement(methodContentsIndent, callLog, className, methodName);

    methodBlockBuilder.append(method.getMethodSignature() + "\n");

    // Remove auto-generated Assert.fail and TODO lines
    String methodLeadInContents = method.getMethodLeadInContents();
    methodLeadInContents = methodLeadInContents.replace("XCTAssert(false, \"Unimplemented test.\")", "");
    if (methodLeadInContents.endsWith("\n\n")) {
      methodLeadInContents = methodLeadInContents.substring(0, methodLeadInContents.length() - 1);
    }

    methodBlockBuilder.append(methodLeadInContents);

    methodBlockBuilder.append(injectStatements);

    methodBlockBuilder.append(methodOperationStatements);

    // Write everything to file
    FileWriterWithEncoding writer = new FileWriterWithEncoding(file, Encode.UTF_8);

    // Write class documentation
    writer.write(fileModel.getClassDocumentation());

    // Possibly need to append imports
    if (fileModel.getImportContents() != null) {

      String[] existingImportStatements = fileModel.getImportContents().getImportStatements();

      for (String importStatement : existingImportStatements) {
        if (!newImportStatements.contains(importStatement)) {
          newImportStatements.add(importStatement);
        }
      }

      for (String statement : newImportStatements) {
        String importLine = statement + "\n";
        writer.write(importLine);
      }
    }

    // Write pre-class signature data
    writer.write(fileModel.getPreClassSignature());

    // Write class signature
    writer.write(fileModel.getClassSignature());
    writer.write("\n");

    // Write member fields
    if (newMemberVariables.size() > 0) {
      String tabCharacter = PlatformConstants.getPlatformSpecificTab();
      writer.write("\n");
      writer.write(tabCharacter);
      writer.write(PlatformBlockSignatures.getBlockEntrySignature(BlockSignatureType.MEMBER_FIELD));
      writer.write("\n");
      for (String memberVariable : newMemberVariables) {
        writer.write(tabCharacter);
        writer.write(memberVariable);
        writer.write("\n");
      }
      writer.write(tabCharacter);
      writer.write(PlatformBlockSignatures.getBlockExitSignature(BlockSignatureType.MEMBER_FIELD));
      writer.write("\n");
    }

    // Write post-class signature data
    writer.write(fileModel.getPostClassSignature());

    // Pre method
    writer.write(fileModel.getPreMethodFileContents());

    if (fileModel.getPostClassSignature().trim().isEmpty() && fileModel.getPreMethodFileContents().trim().isEmpty()) {
      writer.write("\n");
    }

    // Write method contents
    writer.write(methodBlockBuilder.toString());

    // post method
    if (!fileModel.getPostMethodFileContents().trim().isEmpty()) {
      writer.write(fileModel.getPostMethodFileContents());
    }

    writer.close();
  }

  private String getInjectStatement(String indent, List<ServiceCallCacheData> callLog, String className, String methodName) throws URISyntaxException, IOException {

    if (callLog.size() == 0) {
      return null;
    }

    PlatformApiToFuiMappingParser apiToFuiMappingParser = new PlatformApiToFuiMappingParser();

    String extension = ".har";

    StringBuilder injectStatement = new StringBuilder();
    injectStatement.append(indent);
    injectStatement.append(PlatformBlockSignatures.getBlockEntrySignature(BlockSignatureType.INJECTION));
    injectStatement.append("\n");
    injectStatement.append(indent);
    injectStatement.append("inject(responses: [");

    for (int i = 0; i < callLog.size(); i++) {

      ServiceCallCacheData call = callLog.get(i);

      String apiName = call.getServiceCallName();

      PlatformApiToFuiMapping apiToFuiMapping = apiToFuiMappingParser.getMappingsForApiName(apiName);
      String platformRequestTypeStatement = apiToFuiMapping.getPlatformRequestTypeStatement();

      String mockFileName = String.format("%s_%s_%d_%s", className, methodName, i+1, apiName);

      injectStatement.append("\n");
      injectStatement.append(indent);

      // Append the index of the call in the callLog to account for multiple requests of the same type
      String requestTypeKey = platformRequestTypeStatement.trim() + "_" + (i + 1);
      String keyPair = String.format("%s\"%s\": \"%s%s\"", PlatformConstants.getPlatformSpecificTab(), requestTypeKey, mockFileName, extension);
      injectStatement.append(keyPair);

      if (i < (callLog.size() - 1)) {
        injectStatement.append(",");
      }
    }

    injectStatement.append("\n");
    injectStatement.append(indent);
    injectStatement.append(String.format("%s])", PlatformConstants.getPlatformSpecificTab()));
    injectStatement.append("\n");
    injectStatement.append(indent);
    injectStatement.append(PlatformBlockSignatures.getBlockExitSignature(BlockSignatureType.INJECTION));
    injectStatement.append("\n\n");

    return injectStatement.toString();
  }

  private GeneralPlatformFileModel getFileModel(File file, String className, String methodName) {

    try {
      IosSwiftFuiTestParser parser = new IosSwiftFuiTestParser();
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
