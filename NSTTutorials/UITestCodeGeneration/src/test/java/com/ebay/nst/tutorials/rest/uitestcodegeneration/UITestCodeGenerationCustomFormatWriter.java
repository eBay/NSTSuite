package com.ebay.nst.tutorials.rest.uitestcodegeneration;

import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.FormatWriterUtil;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.har.Har;
import com.ebay.service.logger.har.builder.HarLogBuilder;
import com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType;
import com.ebay.service.logger.platforms.blocksignatures.PlatformBlockSignatures;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformMethodModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;
import com.ebay.service.logger.platforms.processors.parser.IosSwiftFuiTestParser;
import com.ebay.service.logger.platforms.util.PlatformConstants;
import com.ebay.service.logger.platforms.util.PlatformLoggerUtil;
import com.ebay.service.logger.writer.Encode;
import com.ebay.service.logger.writer.FileWriterWithEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

// TODO: REMOVE THIS CLASS AND UTILIZE THE DEFAULT LOGGER
public class UITestCodeGenerationCustomFormatWriter implements FormatWriter {

    @Override
    public Platform getPlatformAssociation() {
        return Platform.IOS;
    }

    @Override
    // Identical to HarLogger functionality
    public void writeMocks(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
        FormatWriterUtil.removeMockFilesMatchingClassAndMethodName(testClassName, testMethodName);
        int sequenceCounter = 0;

        for (ServiceCallCacheData call : calls) {

            String fullFilePath = FormatWriterUtil.getMockFolderAndFileName(testClassName, testMethodName, sequenceCounter++, call.getServiceCallName()) + ".har";

            HarLogBuilder harLogBuilder = new HarLogBuilder();
            Har har = harLogBuilder.buildHarFromRequestResponse(call.getRequest(), call.getResponse());

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String harJson = gson.toJson(har);

            FileWriterWithEncoding writer;
            try {
                writer = new FileWriterWithEncoding(fullFilePath, Encode.UTF_8);
                writer.write(harJson);
                writer.close();
            } catch (IOException e) {
                throw new IllegalStateException("Error writing mocks to file: " + fullFilePath, e);
            }

            Reporter.log(String.format("Wrote har: %s", fullFilePath), true);
        }
    }

    @Override
    public void updateTests(List<ServiceCallCacheData> calls, String testClassName, String testMethodName) {
        File file = PlatformLoggerUtil.getTestFile(testClassName);
        if (file != null) {

            GeneralPlatformFileModel fileModel = getFileModel(file, testClassName, testMethodName);
            if (fileModel != null) {

                PlatformLoggerUtil.updatedOperations(fileModel, calls);

                try {
                    writeUpdatedFile(file, fileModel);
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
        methodLeadInContents = methodLeadInContents.replace("XCTAssert(false, \"Unimplemented test.\")", "");
        if (methodLeadInContents.endsWith("\n\n")) {
            methodLeadInContents = methodLeadInContents.substring(0, methodLeadInContents.length() - 1);
        }

        methodBlockBuilder.append(methodLeadInContents);

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
