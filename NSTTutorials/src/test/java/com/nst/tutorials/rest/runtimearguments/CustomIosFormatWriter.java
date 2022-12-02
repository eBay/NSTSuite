package com.nst.tutorials.rest.runtimearguments;

import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.HarLogger;
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
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class CustomIosFormatWriter extends HarLogger {

    @Override
    public Platform getPlatformAssociation() {
        return Platform.IOS;
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
