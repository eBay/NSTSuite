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

    protected void populateJsonMapWithMockValues(ValidationSetModel validationSetModel, Map<String, Object> rootNode) throws IOException, ClassNotFoundException {

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

    protected void populateJsonModel(DeveloperMockValueLooper mockValueLooper, String[] jsonPathSteps, Object node) {

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
                map.put(step, mockValueLooper.getNextMockValue());
            }
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



//        String[] jsonPathSteps = jsonPath.split("\\.");
//        Map<String, Object> mapNode = jsonMap;
//        for (int i = 1; i <= jsonPathSteps.length - 2; i++) {
//
//            String jsonPathStep = jsonPathSteps[i];
//            Integer index = null;
//
//            if (jsonPathStep.endsWith("]")) {
//                String jsonArrayIndex = jsonPathStep.substring(jsonPathStep.indexOf("["));
//                jsonArrayIndex.replace("[", "");
//                jsonArrayIndex.replace("]", "");
//
//                index = Integer.parseInt(jsonArrayIndex);
//                jsonPathStep = jsonPathStep.substring(0, jsonPathStep.indexOf("]"));
//            }
//
//            if (mapNode.containsKey(jsonPathStep)) {
//
//                // If index == null we are dealing with an object.
//                // Else we are dealing with an existing array.
//                if (index == null) {
//                    mapNode = (Map<String, Object>) mapNode.get(jsonPathStep);
//                } else {
//                    List<Map<String, Object>> listNode = (List<Map<String, Object>>) mapNode.get(jsonPathStep);
//
//                    // If the index exists, continue down that path. Otherwise, add it.
//                    Map<String, Object> mapElement = listNode.get(index);
//
//                    if (mapElement == null) {
//                        mapElement = new HashMap<>();
//                        listNode.add(index, mapElement);
//                    }
//
//                    mapNode = mapElement;
//                }
//            } else if (index == null) {
//                Map<String, Object> mapElement = new HashMap<>();
//                mapNode.put(jsonPathStep, mapElement);
//                mapNode = mapElement;
//            } else {
//                Map<String, Object> mapElement = new HashMap<>();
//                List<Map<String, Object>> listNode = new ArrayList<>();
//                listNode.add(index, mapElement);
//                mapNode.put(jsonPathStep, listNode);
//                mapNode = mapElement;
//            }
//        }
//
//        String jsonPathStep = jsonPathSteps[jsonPathSteps.length - 1];
//
//        if (jsonPathStep.endsWith("]")) {
//            String jsonArrayIndex = jsonPathStep.substring(jsonPathStep.indexOf("["));
//            jsonArrayIndex.replace("[", "");
//            jsonArrayIndex.replace("]", "");
//
//            int index = Integer.parseInt(jsonArrayIndex);
//            if (mapNode.containsKey(jsonPathStep)) {
//                List<Object> leafList = (List<Object>) mapNode.get(jsonPathStep);
//                leafList.add(index, mockValue.getMockValue());
//            } else {
//                List<Object> leafList = new ArrayList<>();
//                leafList.add(index, mockValue.getMockValue());
//                mapNode.put(jsonPathStep, leafList);
//            }
//        } else {
//            mapNode.put(jsonPathStep, mockValue.getMockValue());
//        }
    }
}
