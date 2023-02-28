package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.*;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor.ArrayPathToArraySizeMapProcessor;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor.JsonMapPopulateProcessor;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor.JsonMapStructureProcessor;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor.SmallestToLargestArrayPathComparator;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DeveloperMockExport {

    private static final String DEVELOPER_MOCK_FILE_NAME_FORMAT = "TMMock_%s.json";

    /**
     * Export the validation mock values to JSON for each validation defined. Each custom validation will also include
     * the core validation set, because, those are true in every response and therefore MUST be part of every mock.
     * @param exportPath Folder to export mocks to.
     * @param validationSetModels Validation set models containing mock values to write to JSON.
     * @throws IOException Pass through.
     * @throws ClassNotFoundException Pass through.
     */
    public void export(File exportPath, List<ValidationSetModel> validationSetModels) throws IOException, ClassNotFoundException {

        ValidationSetModel coreValidationSet = getCoreValidationSet(validationSetModels);


        for (ValidationSetModel validationSetModel : validationSetModels) {

            String json = getJsonFromValidationSet(coreValidationSet, validationSetModel);

            String validationSetName = validationSetModel.getValidationSetName();
            validationSetName = camelCaseValidationSetName(validationSetName);

            String fileName = String.format(DEVELOPER_MOCK_FILE_NAME_FORMAT, validationSetName);
            File exportFile = new File(exportPath, fileName);
            FileWriter fileWriter = new FileWriter(exportFile);
            fileWriter.write(json);
            fileWriter.close();
        }
    }

    public String getJsonFromValidationSet(ValidationSetModel coreValidationSet, ValidationSetModel customValidationSet) throws IOException, ClassNotFoundException {

        TreeMap<String, Integer> coreValidationArrayPathToArraySizeMap = ArrayPathToArraySizeMapProcessor.getArrayPathToArraySizeMap(coreValidationSet);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Build up the array size map.
        TreeMap<String, Integer> arrayPathToArraySizeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        arrayPathToArraySizeMap.putAll(coreValidationArrayPathToArraySizeMap);

        TreeMap<String, Integer> customValidationArrayPathToArraySizeMap = ArrayPathToArraySizeMapProcessor.getArrayPathToArraySizeMap(customValidationSet);
        arrayPathToArraySizeMap.putAll(customValidationArrayPathToArraySizeMap);

        // Build up the jsonMap structure.
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap = JsonMapStructureProcessor.getJsonMapForValidations(arrayPathToArraySizeMap, coreValidationSet, customValidationSet);

        // Populate the jsonMap with mock values.
        JsonMapPopulateProcessor.populateJsonMapWithMockValues(coreValidationSet, jsonMap);
        JsonMapPopulateProcessor.populateJsonMapWithMockValues(customValidationSet, jsonMap);

        // Write the JSON to file.
        String json = gson.toJson(jsonMap);
        return json;
    }

    /**
     * Get the core validation set, from the list of validation sets, and remove it from the list passed in.
     *
     * @param validationSetModels List of validation sets.
     * @return Core validation if found, otherwise null.
     */
    protected ValidationSetModel getCoreValidationSet(List<ValidationSetModel> validationSetModels) {

        ValidationSetModel validationSet;
        for (int i = validationSetModels.size() - 1; i >= 0; i--) {
            validationSet = validationSetModels.get(i);
            if (validationSet.getValidationSetName().equals(ExportConstants.CORE_VALIDATION_SET)) {
                validationSetModels.remove(i);
                return validationSet;
            }
        }

        return null;
    }

    /**
     * Apply camel casing to the first letter of the validation set name.
     * @param validationSetName Validation set name to process.
     * @return Validation set name with the first letter converted to upper case.
     */
    protected String camelCaseValidationSetName(String validationSetName) {
        if (validationSetName.length() > 0) {
            String firstCharacter = String.valueOf(validationSetName.charAt(0));
            firstCharacter = firstCharacter.toUpperCase();
            validationSetName = firstCharacter + validationSetName.substring(1);
        }
        return validationSetName;
    }
}
