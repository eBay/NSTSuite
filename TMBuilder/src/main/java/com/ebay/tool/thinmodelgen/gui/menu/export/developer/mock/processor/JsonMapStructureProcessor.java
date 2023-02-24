package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

import java.io.IOException;
import java.util.*;

public class JsonMapStructureProcessor {

    /**
     * Populate a JSON map, that will be serialized as the JSON mock, with all of the JSON paths from the check definitions.
     * This operation will initialize each of the array nodes to their expected sizes.
     * Expected that ArrayPathToArraySizeMapProcessor.getArrayPathToArraySizeMap() was called first to setup the arrayPathToSizeMap.
     * @param arrayPathToSizeMap Array path to array size lookup map.
     * @param coreValidationSetModel Core validation set model.
     * @param customValidationSetModel Custom validation set model.
     */
    public static Map<String, Object> getJsonMapForValidations(TreeMap<String, Integer> arrayPathToSizeMap, ValidationSetModel coreValidationSetModel, ValidationSetModel customValidationSetModel) {

        Map<String, Object> jsonMap = new HashMap<>();

        // Iterate over each path from both the coreValidationSetModel and the customValidationSetModel, using the
        // arrayPathToSizeMap to initialize array nodes to the right size.
        NodeModel[] coreNodes = coreValidationSetModel.getData();
        NodeModel[] customNodes = customValidationSetModel.getData();

        processNodeModels(coreNodes, arrayPathToSizeMap, jsonMap);
        processNodeModels(customNodes, arrayPathToSizeMap, jsonMap);

        return jsonMap;
    }

    /**
     * Process the node models and add all of the JSON paths found (associated with node checks) to our jsonMap. This
     * creates the full JSON structure we can populate with check values.
     * @param nodeModels Node models to extract JSON path data from.
     * @param arrayPathToSizeMap Array to size lookup map.
     * @param jsonMap JSON map we are building up.
     */
    protected static void processNodeModels(NodeModel[] nodeModels, TreeMap<String, Integer> arrayPathToSizeMap, Map<String, Object> jsonMap) {

        for (NodeModel nodeModel : nodeModels) {
            JsonBaseType jsonBaseType;
            try {
                jsonBaseType = DeveloperMockProcessorUtility.convertNodeModelToJsonBaseType(nodeModel);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            processJsonBaseTypeCheckPaths(jsonBaseType, arrayPathToSizeMap, jsonMap);
        }
    }

    /**
     * For each path check defined on a JsonBaseType instance, add the path structure (without check value) to the jsonMap.
     * @param jsonBaseType JsonBaseType instance to extract paths from.
     * @param arrayPathToSizeMap Array path to size map for checking array sizes as we build up the jsonMap.
     * @param jsonMap Map to build up of our JSON.
     */
    protected static void processJsonBaseTypeCheckPaths(JsonBaseType jsonBaseType, TreeMap<String, Integer> arrayPathToSizeMap, Map<String, Object> jsonMap) {

        String[] savedPaths = jsonBaseType.getSavedPathsForNode();

        for (String savedPath : savedPaths) {
            String[] splitPath = savedPath.split("\\.");
            List<String> traversedSteps = new ArrayList<>();
            setupJsonMap(splitPath, traversedSteps, arrayPathToSizeMap, jsonMap);
        }
    }

    /**
     * Setup the JSON map so the structure exists to support the specified splitJsonPath. This is a recursive operation. Please be sure initial inputs align with parameter descriptions.
     * @param splitJsonPath The JSON path, split into an array of step values.
     * @param traversedSteps Send in as an empty ArrayList.
     * @param arrayPathToSizeMap Existing arrayPathToSizeMap - used to check array sizes.
     * @param node Start with the root node of the String/Object tree Map.
     */
    protected static void setupJsonMap(String[] splitJsonPath, List<String> traversedSteps, TreeMap<String, Integer> arrayPathToSizeMap, Object node) {

        String step = splitJsonPath[0];
        splitJsonPath = Arrays.copyOfRange(splitJsonPath, 1, splitJsonPath.length);
        traversedSteps.add(step);
        String currentArrayPath = String.join(".", traversedSteps);

        if (step.equals("$")) {
            setupJsonMap(splitJsonPath, traversedSteps, arrayPathToSizeMap, node);
            return;
        }

        boolean isArrayStep = DeveloperMockProcessorUtility.isJsonPathStepAnArray(step);
        if (step.contains("[")) {
            step = step.substring(0, step.indexOf("["));
        }

        // Once we get to the last step in our path, splitJsonPath will have the last step removed, we are done.
        // Add the correct leaf node and then head back up the recursive call chain.
        if (splitJsonPath.length == 0) {

            Map map = (Map) node;
            if (map.containsKey(step)) {
                return;
            } else if (isArrayStep) {
                int arraySize = arrayPathToSizeMap.get(currentArrayPath);
                List<Object> newArray = new ArrayList<Object>();
                for (int i = 0; i < arraySize; i++) {
                    newArray.add(null);
                }
                map.put(step, newArray);
            } else {
                map.put(step, null);
            }

            return;
        }

        if (node instanceof Map) {

            Map map = (Map) node;
            if (map.containsKey(step)) {
                node = map.get(step);
                if (node instanceof List) {
                    // We have to do something tricky here. Because we always pluck off the next step from the
                    // splitJsonPath array when we enter this method, and we are going to rely on the array iteration
                    // portion of this method we need to append the step back onto the splitJsonPath array.
                    List<String> tempSplitJsonPath = new ArrayList<>();
                    tempSplitJsonPath.add(step);
                    tempSplitJsonPath.addAll(Arrays.asList(splitJsonPath));
                    splitJsonPath = tempSplitJsonPath.toArray(new String[tempSplitJsonPath.size()]);
                    traversedSteps.remove(traversedSteps.size()-1);
                }
                setupJsonMap(splitJsonPath, traversedSteps, arrayPathToSizeMap, node);
            } else {
                if (isArrayStep) {
                    // We don't need to worry about leaf nodes.
                    // For that reason, every array will contain a map.
                    // Initialize the array with n number of maps based on the array size.
                    Integer arraySize = arrayPathToSizeMap.get(currentArrayPath);
                    List<Map<String, Object>> newArray = new ArrayList<>();
                    if (arraySize != null) {
                        for (int j = 0; j < arraySize; j++) {
                            newArray.add(new HashMap<>());
                        }
                    } else {
                        throw new IllegalStateException("Array size MUST be known to proceed.");
                    }

                    map.put(step, newArray);
                    // We have to do something tricky here. Because we always pluck off the next step from the
                    // splitJsonPath array when we enter this method, and we are going to rely on the array iteration
                    // portion of this method we need to append the step back onto the splitJsonPath array.
                    List<String> tempSplitJsonPath = new ArrayList<>();
                    tempSplitJsonPath.add(step);
                    tempSplitJsonPath.addAll(Arrays.asList(splitJsonPath));
                    splitJsonPath = tempSplitJsonPath.toArray(new String[tempSplitJsonPath.size()]);
                    traversedSteps.remove(traversedSteps.size()-1);
                    setupJsonMap(splitJsonPath, traversedSteps, arrayPathToSizeMap, newArray);
                } else {
                    Map<String, Object> newNode = new HashMap<>();
                    map.put(step, newNode);
                    setupJsonMap(splitJsonPath, traversedSteps, arrayPathToSizeMap, newNode);
                }
            }

        } else if (node instanceof List) {

            // If we have a list it will have already been initialized with each element being a map.
            // Because we will never be here with a leaf node we will iterate over the map elements and
            // send each through the recursive processing.
            List listNode = (List) node;
            for (Object obj : listNode) {
                setupJsonMap(splitJsonPath, traversedSteps, arrayPathToSizeMap, obj);
            }
        }
    }
}
