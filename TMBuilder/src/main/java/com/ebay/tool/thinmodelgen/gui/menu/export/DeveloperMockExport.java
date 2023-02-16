package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeveloperMockExport {

    private static final String DEVELOPER_MOCK_FILE_NAME_FORMAT = "TMMock_%s.json";

    // Initialize for testing purposes.
    private Map<String, Object> jsonMap = new HashMap<>();

    public void export(File exportPath, List<ValidationSetModel> validationSetModels) throws IOException, ClassNotFoundException {

        ValidationSetModel coreValidaitonSet = getCoreValidationSet(validationSetModels);

        // Build up the jsonSchemaRoot instance, resetting the jsonSchemaRoot instance every time.
        // Start with the core validation set. This set's nodes be overridden by a value on the custom set.
        // Apply the custom set values.
        // Write the json to file, rinse and repeat.
        for (ValidationSetModel setModel : validationSetModels) {
            jsonMap = new HashMap<>();
            processValidationSetModel(coreValidaitonSet);
            processValidationSetModel(setModel);

            String fileName = String.format(DEVELOPER_MOCK_FILE_NAME_FORMAT, setModel.getValidationSetName());
        }
    }

    private void processValidationSetModel(ValidationSetModel setModel) throws IOException, ClassNotFoundException {
        String validationSetName = setModel.getValidationSetName();
        NodeModel[] nodeModels = setModel.getData();

        for (NodeModel nodeModel : nodeModels) {

            String serializedData = nodeModel.getSerializedUserObject();
            JsonBaseType jsonBaseType = JsonBaseTypePersistence.deserialize(serializedData);
            String[] savedJsonPaths = jsonBaseType.getSavedPathsForNode();

            for (String savedJsonPath : savedJsonPaths) {

                JsonPathExecutor jsonPathExecutor = jsonBaseType.getCheckForPath(savedJsonPath);
                if (jsonPathExecutor instanceof  DeveloperMockListOfValues) {
                    DeveloperMockListOfValues<?> mockListOfValues = (DeveloperMockListOfValues<?>) jsonPathExecutor;
                    DeveloperMockType mockType = mockListOfValues.getMockType();
                    mockListOfValues.getMockValues();
                } else if (jsonPathExecutor instanceof DeveloperMockValue) {
                    DeveloperMockValue<?> mockValue = (DeveloperMockValue<?>) jsonPathExecutor;
                    DeveloperMockType mockType = mockValue.getMockType();
                    mockValue.getMockValue();
                } else {
                    continue;
                }
            }
        }
    }

    protected Map<String, Object> getJson() {
        return jsonMap;
    }

    protected void populateJsonModel(DeveloperMockListOfValues<?> mockValues, String jsonPath) {

    }

    protected void populateJsonModel(DeveloperMockValue<?> mockValue, String jsonPath) {

        String[] jsonPathSteps = jsonPath.split("\\.");
        Map<String, Object> mapNode = jsonMap;
        for (int i = 1; i <= jsonPathSteps.length - 2; i++) {

            String jsonPathStep = jsonPathSteps[i];
            Integer index = null;

            if (jsonPathStep.endsWith("]")) {
                String jsonArrayIndex = jsonPathStep.substring(jsonPathStep.indexOf("["));
                jsonArrayIndex.replace("[", "");
                jsonArrayIndex.replace("]", "");

                index = Integer.parseInt(jsonArrayIndex);
                jsonPathStep = jsonPathStep.substring(0, jsonPathStep.indexOf("]"));
            }

            if (mapNode.containsKey(jsonPathStep)) {

                // If index == null we are dealing with an object.
                // Else we are dealing with an existing array.
                if (index == null) {
                    mapNode = (Map<String, Object>) mapNode.get(jsonPathStep);
                } else {
                    List<Map<String, Object>> listNode = (List<Map<String, Object>>) mapNode.get(jsonPathStep);

                    // If the index exists, continue down that path. Otherwise, add it.
                    Map<String, Object> mapElement = listNode.get(index);

                    if (mapElement == null) {
                        mapElement = new HashMap<>();
                        listNode.add(index, mapElement);
                    }

                    mapNode = mapElement;
                }
            } else if (index == null) {
                Map<String, Object> mapElement = new HashMap<>();
                mapNode.put(jsonPathStep, mapElement);
                mapNode = mapElement;
            } else {
                Map<String, Object> mapElement = new HashMap<>();
                List<Map<String, Object>> listNode = new ArrayList<>();
                listNode.add(index, mapElement);
                mapNode.put(jsonPathStep, listNode);
                mapNode = mapElement;
            }
        }

        String jsonPathStep = jsonPathSteps[jsonPathSteps.length - 1];

        if (jsonPathStep.endsWith("]")) {
            String jsonArrayIndex = jsonPathStep.substring(jsonPathStep.indexOf("["));
            jsonArrayIndex.replace("[", "");
            jsonArrayIndex.replace("]", "");

            int index = Integer.parseInt(jsonArrayIndex);
            if (mapNode.containsKey(jsonPathStep)) {
                List<Object> leafList = (List<Object>) mapNode.get(jsonPathStep);
                leafList.add(index, mockValue.getMockValue());
            } else {
                List<Object> leafList = new ArrayList<>();
                leafList.add(index, mockValue.getMockValue());
                mapNode.put(jsonPathStep, leafList);
            }
        } else {
            mapNode.put(jsonPathStep, mockValue.getMockValue());
        }
    }

    /**
     * Get the core validation set, from the list of validation sets, and remove it from the list passed in.
     *
     * @param validationSetModels List of validation sets.
     * @return Core validation if found, otherwise null.
     */
    private ValidationSetModel getCoreValidationSet(List<ValidationSetModel> validationSetModels) {

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
}
