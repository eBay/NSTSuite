package com.ebay.service.logger.platforms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Reporter;

import com.ebay.service.logger.call.cache.ServiceCallCacheData;
import com.ebay.service.logger.platforms.blocksignatures.BlockSignatureType;
import com.ebay.service.logger.platforms.blocksignatures.PlatformBlockSignatures;
import com.ebay.service.logger.platforms.model.GeneralPlatformFileModel;
import com.ebay.service.logger.platforms.model.GeneralPlatformOperationModel;

public class PlatformLoggerUtil {

	/**
	 * Get the file instance for the specified class name.
	 *
	 * @param className Class name to find corresponding file for.
	 * @return File instance if found, otherwise null.
	 */
	public static File getTestFile(String className) {

		PlatformTestFileManager testFileManager;

		try {
			testFileManager = new PlatformTestFileManager();
		} catch (FileNotFoundException e) {
			Reporter.log("Unable to obtain platform specific test file location.", true);
			e.printStackTrace();
			return null;
		}

		return testFileManager.getTestFile(className);
	}

	/**
	 * Update the method operations in the file model with the operations recorded
	 * from the call log.
	 *
	 * @param fileModel File model to update.
	 * @param callLog   Call log to update file model with.
	 */
	public static void updatedOperations(GeneralPlatformFileModel fileModel, List<ServiceCallCacheData> callLog) {

		if (fileModel.getMethodContents() == null) {
			return;
		}

		List<GeneralPlatformOperationModel> originalOperations = fileModel.getMethodContents().getMethodOperations();
		ArrayList<GeneralPlatformOperationModel> newOperations = new ArrayList<>();

		for (ServiceCallCacheData call : callLog) {

			String apiName = call.getServiceCallName();
			GeneralPlatformOperationModel originalOperation = null;
			int i = 0;

			for (i = 0; i < originalOperations.size(); i++) {
				if (originalOperations.get(i).getServiceWrapperApiName().equals(apiName)) {
					originalOperation = originalOperations.get(i);
					break;
				}
			}

			GeneralPlatformOperationModel newOperation = new GeneralPlatformOperationModel();
			newOperation.setServiceWrapperApiName(apiName);

			// Remove the original instance so we don't trip over it in the future.
			// Copy original instance custom block to the new operation.
			if (originalOperation != null) {
				originalOperations.remove(i);
				String customBlock = originalOperation.getCustomBlock();
				if (!customBlock.isEmpty()) {
					newOperation.addCustomBlockLine(customBlock);
				}
			}

			newOperations.add(newOperation);
		}

		fileModel.getMethodContents().setMethodOperations(newOperations);
	}

	/**
	 * Get the import statements based on the operations provided.
	 * 
	 * @param operations Operations to build import statements from.
	 * @return List of import statements to include in file.
	 * @throws URISyntaxException Thrown exception.
	 * @throws IOException        Thrown exception.
	 */
	public static List<String> getImportStatements(List<GeneralPlatformOperationModel> operations)
			throws URISyntaxException, IOException {

		PlatformApiToFuiMappingParser apiToFuiMappingParser = new PlatformApiToFuiMappingParser();

		ArrayList<String> newImportStatements = new ArrayList<>();

		for (int i = 0; i < operations.size(); i++) {

			GeneralPlatformOperationModel operation = operations.get(i);
			PlatformApiToFuiMapping apiToFuiMapping = apiToFuiMappingParser
					.getMappingsForApiName(operation.getServiceWrapperApiName());

			if (apiToFuiMapping == null) {
				throw new IllegalStateException(String.format("Unable to find FUI mapping lookup for API: %s",
						operation.getServiceWrapperApiName()));
			}

			String[] importStatements = apiToFuiMapping.getImportStatements();
			for (String statement : importStatements) {
				if (newImportStatements.contains(statement)) {
					continue;
				}
				newImportStatements.add(statement);
			}
		}

		return newImportStatements;
	}

	/**
	 * Get the member variable statements based on the operations provided.
	 * 
	 * @param operations Operations to build member variable statements from.
	 * @return List of member variable statements to include in file.
	 * @throws URISyntaxException Thrown exception.
	 * @throws IOException        Thrown exception.
	 */
	public static List<String> getMemberVariableStatements(List<GeneralPlatformOperationModel> operations)
			throws URISyntaxException, IOException {

		PlatformApiToFuiMappingParser apiToFuiMappingParser = new PlatformApiToFuiMappingParser();

		ArrayList<String> newMemberStatements = new ArrayList<>();

		for (int i = 0; i < operations.size(); i++) {

			GeneralPlatformOperationModel operation = operations.get(i);
			PlatformApiToFuiMapping apiToFuiMapping = apiToFuiMappingParser
					.getMappingsForApiName(operation.getServiceWrapperApiName());

			String[] memberVariables = apiToFuiMapping.getMemeberFieldStatements();
			for (String variable : memberVariables) {
				if (variable.isEmpty() || newMemberStatements.contains(variable)) {
					continue;
				}
				newMemberStatements.add(variable);
			}
		}

		return newMemberStatements;
	}

	/**
	 * Get the method block statements based on the operations provided.
	 * 
	 * @param operations           Operations to build member variable statements
	 *                             from.
	 * @param methodContentsIndent Indentation to apply to each statement.
	 * @return Statement block to include in file.
	 * @throws URISyntaxException Thrown exception.
	 * @throws IOException        Thrown exception.
	 */
	public static String getMethodBlockStatements(List<GeneralPlatformOperationModel> operations,
			String methodContentsIndent) throws URISyntaxException, IOException {

		PlatformApiToFuiMappingParser apiToFuiMappingParser = new PlatformApiToFuiMappingParser();
		StringBuilder methodBlockBuilder = new StringBuilder();
		
		if (operations == null) {
			operations = new ArrayList<>();
		}
		
		if (methodContentsIndent == null) {
			methodContentsIndent = "  ";
		}

		for (int i = 0; i < operations.size(); i++) {

			GeneralPlatformOperationModel operation = operations.get(i);
			PlatformApiToFuiMapping apiToFuiMapping = apiToFuiMappingParser
					.getMappingsForApiName(operation.getServiceWrapperApiName());

			methodBlockBuilder.append(methodContentsIndent);
			methodBlockBuilder.append(PlatformBlockSignatures.getBlockEntrySignature(BlockSignatureType.METHOD));
			methodBlockBuilder.append("\n");

			methodBlockBuilder.append(methodContentsIndent);
			methodBlockBuilder
					.append(PlatformBlockSignatures.getMethodBlockCallSignature(operation.getServiceWrapperApiName()));
			methodBlockBuilder.append("\n");

			methodBlockBuilder.append(methodContentsIndent);
			methodBlockBuilder.append(PlatformBlockSignatures.getMethodBlockOperationIndexSignature(i + 1));
			methodBlockBuilder.append("\n");

			String[] statements = null;
			if (apiToFuiMapping != null) {
				if (i == 0) {
					statements = apiToFuiMapping.getEntryStatements();
				} else {
					statements = apiToFuiMapping.getNavigationStatements();
				}
			} else {
				statements = new String[] { "// Undefined mapping for : " + operation.getServiceWrapperApiName() };
			}

			for (String statement : statements) {
				methodBlockBuilder.append(methodContentsIndent);
				methodBlockBuilder.append(statement);
				methodBlockBuilder.append("\n");
			}

			methodBlockBuilder.append(methodContentsIndent);
			methodBlockBuilder.append(PlatformBlockSignatures.getBlockExitSignature(BlockSignatureType.METHOD));
			methodBlockBuilder.append("\n");

			String customBlock = operation.getCustomBlock();
			if (customBlock.trim().isEmpty()) {
				methodBlockBuilder.append("\n");
			} else {
				if (customBlock.endsWith("\n\n")) {
					customBlock = customBlock.substring(0, customBlock.length() - 1);
				}
				methodBlockBuilder.append(customBlock);
			}
		}

		return methodBlockBuilder.toString();
	}

	/**
	 * Check string lead in characters for a new line character among whitespace.
	 * 
	 * @param value String to evaluate.
	 * @return True if there is a newline character before any non-whitespace
	 *         character in the string. False otherwise.
	 */
	public static boolean stringLeadinContainsNewlineCharacterAmongWhitespace(String value) {

		if (value == null) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);

			if (c == '\n' || c == '\r') {
				return true;
			} else if (c == ' ' || c == '\t') {
				continue;
			} else {
				break;
			}
		}

		return false;
	}
}
