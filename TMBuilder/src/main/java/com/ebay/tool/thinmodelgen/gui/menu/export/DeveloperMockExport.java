package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.service.logger.har.Param;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.SmallestToLargestArrayPathComparator;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockListOfValues;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockType;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockValue;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DeveloperMockExport {

    private static final String DEVELOPER_MOCK_FILE_NAME_FORMAT = "TMMock_%s.json";

    // Initialize for testing purposes.
    private Map<String, Object> jsonMap = new HashMap<>();
    private TreeMap<String,Integer> arrayPathToArraySizeMap;

    public DeveloperMockExport() {
        // Order the map from smallest to largest array node path.
        arrayPathToArraySizeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());
    }

    public void export(File exportPath, List<ValidationSetModel> validationSetModels) throws IOException, ClassNotFoundException {

        ValidationSetModel coreValidaitonSet = getCoreValidationSet(validationSetModels);
        arrayPathToArraySizeMap.clear();
        jsonMap.clear();

        for (ValidationSetModel validationSetModel : validationSetModels) {

            // Steps:

            // 1) Take each validation set JSON path and for each array in the path add the array, and index size,
            // to the arrayPathToArraySizeMap (only applying maximum index size as determined by the largest
            // explicit array index found on the JSON path). Wildcard array indexes default to length 1.
            populateArrayPathToArraySizeMap(coreValidaitonSet);
            populateArrayPathToArraySizeMap(validationSetModel);

            // 2) Populate the jsonMap with the array elements from the arrayPathToArraySizeMap, using ensureCapacity()
            // for those list nodes.
            populateJsonMapArrays();

            // 3) Use recursive loop to apply values to each node, including array parents, that match the path.
            // If path includes wildcard matchers for array indexes then the value can only be set if no other value is
            // already in place. Explicit indexes will cause an overwrite of existing values.
            //
            // EG:
            // $.root.next[*].step[*].key - will not overwrite existing values assigned to key nodes
            // $.root.next[1].step[2].key - will set/overwrite only one key node at the specified array indexes noted
            // $.root.next[*].step[2].key - will set/overwrite every 2nd step.key
            // $.root.next[1].step[*].key - will set/overwrite every step.key on the first next index.


        }







        // Build up the jsonSchemaRoot instance, resetting the jsonSchemaRoot instance every time.
        // Start with the core validation set. This set's nodes be overridden by a value on the custom set.
        // Apply the custom set values.
        // Write the json to file, rinse and repeat.
//        for (ValidationSetModel setModel : validationSetModels) {
//            jsonMap = new HashMap<>();
//            processValidationSetModel(coreValidaitonSet);
//            processValidationSetModel(setModel);
//
//            String fileName = String.format(DEVELOPER_MOCK_FILE_NAME_FORMAT, setModel.getValidationSetName());
//        }
    }

    protected Map<String, Object> getJsonMap() {
        return Collections.unmodifiableMap(jsonMap);
    }

    protected TreeMap<String,Integer> getArrayPathToArraySizeMap() {
        return new TreeMap<>(arrayPathToArraySizeMap);
    }

    /**
     * Populate the JSON map, that will be serialized as the JSON mock, with the parsed array data. This operation will
     * initialize each of the array nodes to their expected sizes. Expected that populateArrayPathToArraySizeMap() was
     * called first to setup the source data.
     */
    protected void populateJsonMapArrays() {

        Set<String> orderedKeys = arrayPathToArraySizeMap.keySet();
        for (String key : orderedKeys) {
            String[] steps = key.split("\\.");
            if (steps.length > 0 && steps[0].equals("$")) {
                steps = Arrays.copyOfRange(steps, 1, steps.length);
            }
            populateJsonMapArray(jsonMap, steps, new ArrayList<>(Arrays.asList("$")));
        }
    }

    /**
     * Populate the arrayPathToArraySize map with the validationSetModel provided. Each time this method is called the
     * arrayPathToArraySize map is added to with paths that contain arrays. The last node in each path added to the
     * arrayPathToArraySize map will end with the array element and the key is the maximum size of that array. The size
     * is parsed from the path (EG: value[3] would be an array node 'value' with size 3. [*] defaults to size 1).
     *
     * @param validationSetModel Model to parse.
     * @throws IOException Pass through.
     * @throws ClassNotFoundException Pass through.
     */
    protected void populateArrayPathToArraySizeMap(ValidationSetModel validationSetModel) throws IOException, ClassNotFoundException {

        NodeModel[] nodes = validationSetModel.getData();
        for (NodeModel node : nodes) {
            JsonBaseType jsonBaseType = convertNodeModelToJsonBaseType(node);
            String[] savedPaths = jsonBaseType.getSavedPathsForNode();

            for (String savedPath : savedPaths) {
                String[] splitSavedPath = savedPath.split("\\.");
                StringBuilder pathBuilder = new StringBuilder();
                String lastArrayPath = "";
                for (String value : splitSavedPath) {
                    if (pathBuilder.length() > 0) {
                        pathBuilder.append(".");
                    }

                    // Check for step with array signature.
                    // If found, add to arrayPathToArraySizeMap if it doesn't exist
                    // or is a greater array size than the existing value.
                    int arrayOpeningBracketPosition = value.indexOf("[");
                    if (arrayOpeningBracketPosition != -1) {
                        String indexString = value.substring(arrayOpeningBracketPosition+1, value.indexOf("]"));
                        int arraySize = 1;
                        if (!indexString.equals("*")) {
                            arraySize = Integer.parseInt(indexString);
                        }

                        value = value.substring(0, arrayOpeningBracketPosition);
                        pathBuilder.append(value);

                        Integer currentArraySize = arrayPathToArraySizeMap.get(pathBuilder.toString());
                        if (currentArraySize == null) {
                            arrayPathToArraySizeMap.put(pathBuilder.toString(), arraySize);
                        } else if (currentArraySize < arraySize) {
                            arrayPathToArraySizeMap.put(pathBuilder.toString(), arraySize);
                        }

                        lastArrayPath = pathBuilder.toString();

                    } else {
                        pathBuilder.append(value);
                    }
                }

                // For the very last array path, we may need to increase the list size if the check is a listOf* check.
                // If we have an array path, check the type for instanceOf DeveloperMockListOfValues.
                // If it is a match, check if the list of mock values is greater than the current array size.
                // If it is, us the larger size.
                if (!lastArrayPath.isEmpty()) {
                    JsonPathExecutor pathExecutor = jsonBaseType.getCheckForPath(savedPath);
                    if (pathExecutor instanceof DeveloperMockListOfValues) {
                        DeveloperMockListOfValues listOfValues = (DeveloperMockListOfValues) pathExecutor;
                        List mockValues = listOfValues.getMockValues();
                        Integer currentArraySize = arrayPathToArraySizeMap.get(lastArrayPath);
                        if (currentArraySize != null && currentArraySize < mockValues.size()) {
                            arrayPathToArraySizeMap.put(lastArrayPath, mockValues.size());
                        }
                    }
                }
             }
        }
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
     * Recursive operation to populate the JSON map with array details parsed from the paths and stored on the
     * arrayPathToArraySizeMap.
     * @param mapNode Initial call needs to pass in Map<String, Object>.
     * @param remainingSteps Initial call needs to pass in the complete JSON path steps.
     * @param pathHistory The path history traversed to this point.
     */
    private void populateJsonMapArray(Object mapNode, String[] remainingSteps, List<String> pathHistory) {

        if (remainingSteps.length == 0) {
            return;
        }

        String step = remainingSteps[0];
        pathHistory.add(step);
        if (remainingSteps.length == 1) {

            String path = String.join(".", pathHistory);
            Integer initializeArraySize = arrayPathToArraySizeMap.get(path);
            if (initializeArraySize == null) {
                throw new IllegalStateException("Path should always be known.");
            }

            if (mapNode instanceof Map) {
                ((Map) mapNode).put(step, new ArrayList<>(initializeArraySize));
            } else if (mapNode instanceof List) {
                HashMap<String, Object> listObject = new HashMap<>();
                listObject.put(step, new ArrayList<>(initializeArraySize));
                ((List) mapNode).add(listObject);
            } else {
                throw new IllegalStateException("Encountered node type that does not match expectations." + mapNode);
            }

        } else if (mapNode instanceof Map && ((Map)mapNode).containsKey(step)) {

            Object node = ((Map) mapNode).get(step);
            remainingSteps = Arrays.copyOfRange(remainingSteps, 1, remainingSteps.length);
            populateJsonMapArray(node, remainingSteps, pathHistory);

        } else if (mapNode instanceof Map) {

            // If the map doesn't contain the key we need to add it.
            remainingSteps = Arrays.copyOfRange(remainingSteps, 1, remainingSteps.length);
            ((Map) mapNode).put(step, new HashMap<String, Object>());
            mapNode = ((Map) mapNode).get(step);
            populateJsonMapArray(mapNode, remainingSteps, pathHistory);

        } else if (mapNode instanceof List) {

            List<Object> mapNodeArray = (List) mapNode;
            for (Object obj : mapNodeArray) {
                populateJsonMapArray(obj, remainingSteps, pathHistory);
            }

        } else {
            throw new IllegalStateException("Attempting to process step of unknown data type.");
        }

        pathHistory.remove(pathHistory.size()-1);
    }

    /**
     * Convert a NodeModel to a JsonBaseType.
     * @param nodeModel Model to convert.
     * @return JsonBaseType instance of NodeModel.
     * @throws IOException Pass through.
     * @throws ClassNotFoundException Pass through.
     */
    private JsonBaseType convertNodeModelToJsonBaseType(NodeModel nodeModel) throws IOException, ClassNotFoundException {
        String serializedData = nodeModel.getSerializedUserObject();
        return JsonBaseTypePersistence.deserialize(serializedData);
    }





    // =========================================================================







//    private void processValidationSetModel(ValidationSetModel setModel) throws IOException, ClassNotFoundException {
//        String validationSetName = setModel.getValidationSetName();
//        NodeModel[] nodeModels = setModel.getData();
//
//        for (NodeModel nodeModel : nodeModels) {
//
//            String serializedData = nodeModel.getSerializedUserObject();
//            JsonBaseType jsonBaseType = JsonBaseTypePersistence.deserialize(serializedData);
//            String[] savedJsonPaths = jsonBaseType.getSavedPathsForNode();
//
//            for (String savedJsonPath : savedJsonPaths) {
//
//                JsonPathExecutor jsonPathExecutor = jsonBaseType.getCheckForPath(savedJsonPath);
//                if (jsonPathExecutor instanceof DeveloperMockListOfValues) {
//                    DeveloperMockListOfValues<?> mockListOfValues = (DeveloperMockListOfValues<?>) jsonPathExecutor;
//                    DeveloperMockType mockType = mockListOfValues.getMockType();
//                    mockListOfValues.getMockValues();
//                } else if (jsonPathExecutor instanceof DeveloperMockValue) {
//                    DeveloperMockValue<?> mockValue = (DeveloperMockValue<?>) jsonPathExecutor;
//                    DeveloperMockType mockType = mockValue.getMockType();
//                    mockValue.getMockValue();
//                } else {
//                    continue;
//                }
//            }
//        }
//    }


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


}
