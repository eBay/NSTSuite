package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockTypeDecoder;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValueLooper;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

import java.io.IOException;
import java.util.*;

public class JsonMapPopulateProcessor {

    /**
     * Add mock values from the validation set model to the JSON map.
     * @param validationSetModel Validation set model with mock values to add to JSON.
     * @param rootNode Root of the JSON map tree.
     * @throws IOException Pass through.
     * @throws ClassNotFoundException Pass through.
     */
    public static void populateJsonMapWithMockValues(ValidationSetModel validationSetModel, Map<String, Object> rootNode) throws IOException, ClassNotFoundException {

        NodeModel[] nodes = validationSetModel.getData();

        for (NodeModel node : nodes) {

            JsonBaseType jsonBaseType = DeveloperMockProcessorUtility.convertNodeModelToJsonBaseType(node);
            String[] savedPaths = jsonBaseType.getSavedPathsForNode();

            for (String savedPath : savedPaths) {

                JsonPathExecutor pathCheck = jsonBaseType.getCheckForPath(savedPath);
                DeveloperMockTypeDecoder mockValueDecoder = null;
                if (pathCheck instanceof DeveloperMockTypeDecoder) {
                    mockValueDecoder = (DeveloperMockTypeDecoder) pathCheck;
                } else {
                    continue;
                }

                DeveloperMockValueLooper mockValueLooper = new DeveloperMockValueLooper(mockValueDecoder);

                populateJsonModel(mockValueLooper, savedPath.split("\\."), rootNode);
            }
        }
    }

    /**
     * Recursively traverse the json tree and add the mock value to the leaf node.
     * @param mockValueLooper Mock value looper instance.
     * @param jsonPathSteps JSON path steps to traverse.
     * @param node Node to look for next JSON path step on.
     */
    protected static void populateJsonModel(DeveloperMockValueLooper mockValueLooper, String[] jsonPathSteps, Object node) {

        String step = jsonPathSteps[0];

        if (jsonPathSteps.length == 1) {
            // We are at the end of the road. Populate the value and turn back.
            if (DeveloperMockProcessorUtility.isJsonPathStepAnArray(step)) {
                int index = DeveloperMockProcessorUtility.getArrayIndexFromStep(step);
                Map map = (Map) node;
                step = step.substring(0, step.indexOf("["));
                List<Object> array = (List<Object>) map.get(step);
                if (index == -1) {
                    for (int i = 0; i < array.size(); i++) {
                        Object element = array.get(i);
                        // When looping over values, we will ONLY set them when the existing index HAS NOT been set.
                        if (element == null) {
                            array.set(i, mockValueLooper.getNextMockValue());
                        }
                    }
                } else {
                    array.set(index, mockValueLooper.getNextMockValue());
                }
            } else {
                Map map = (Map) node;
                if (mockValueLooper.containsArrayValues()) {
                    if (map.get(step) == null) {
                        map.put(step, mockValueLooper.getNextMockValue());
                    }
                } else {
                    map.put(step, mockValueLooper.getNextMockValue());
                }
            }
            return;
        }

        jsonPathSteps = Arrays.copyOfRange(jsonPathSteps, 1, jsonPathSteps.length);

        if (step.equals("$")) {
            populateJsonModel(mockValueLooper, jsonPathSteps, node);
            return;
        }

        // Expect every node instance to be a map.
        if (DeveloperMockProcessorUtility.isJsonPathStepAnArray(step)) {
            int index = DeveloperMockProcessorUtility.getArrayIndexFromStep(step);
            Map map = (Map) node;
            step = step.substring(0, step.indexOf("["));
            List<Map<String, Object>> array = (List<Map<String, Object>>) map.get(step);
            if (index == -1) {
                for (Map<String, Object> element : array) {
                    populateJsonModel(mockValueLooper, jsonPathSteps, element);
                }
            } else {
                populateJsonModel(mockValueLooper, jsonPathSteps, array.get(index));
            }
        } else {
            Map map = (Map) node;
            Object nextNode = map.get(step);
            populateJsonModel(mockValueLooper, jsonPathSteps, nextNode);
        }
    }
}
