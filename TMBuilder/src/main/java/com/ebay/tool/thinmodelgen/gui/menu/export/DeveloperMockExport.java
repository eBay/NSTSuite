package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.service.logger.har.Param;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.*;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import org.json.JSONObject;

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

    /**
     * Export the validation mock values to JSON for each validation defined. Each custom validation will also include
     * the core validation set, because, those are true in every response and therefore MUST be part of every mock.
     * @param exportPath Folder to export mocks to.
     * @param validationSetModels Validation set models containing mock values to write to JSON.
     * @throws IOException Pass through.
     * @throws ClassNotFoundException Pass through.
     */
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
            populateJsonMapWithMockValues(validationSetModel);

            // 4) Write the JSON to file.
            String fileName = String.format(DEVELOPER_MOCK_FILE_NAME_FORMAT, validationSetModel.getValidationSetName());
            JSONObject jsonObject = new JSONObject(jsonMap);
            String json = jsonObject.toString();
            // TODO: write jsonMap, as JSON, to file.
        }
    }

    /**
     * For testing.
     * @return Unmodifiable map.
     */
    protected Map<String, Object> getJsonMap() {
        return Collections.unmodifiableMap(jsonMap);
    }

    /**
     * For testing - access to map. Cannot modify source map with copy returned.
     * @return Copy of map.
     */
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

    protected void populateJsonMapWithMockValues(ValidationSetModel validationSetModel) throws IOException, ClassNotFoundException {

        NodeModel[] nodes = validationSetModel.getData();
        for (NodeModel node : nodes) {

            JsonBaseType jsonBaseType = convertNodeModelToJsonBaseType(node);
            String[] savedPaths = jsonBaseType.getSavedPathsForNode();

            for (String savedPath : savedPaths) {

                JsonPathExecutor pathCheck = jsonBaseType.getCheckForPath(savedPath);
                DeveloperMockTypeDecoder mockValueDecoder = null;
                if (pathCheck instanceof DeveloperMockTypeDecoder) {
                    mockValueDecoder = (DeveloperMockTypeDecoder) pathCheck;
                }
                String[] splitPath = savedPath.split("\\.");
                Map rootNode = jsonMap;
                walkAndPopulateJsonMap(rootNode, splitPath, new DeveloperMockValueLooper(mockValueDecoder));
            }
        }
    }

    protected void walkAndPopulateJsonMap(Map currentNode, String[] pathSteps, DeveloperMockValueLooper mockValueLooper) {

        if (currentNode == null || pathSteps == null || mockValueLooper == null) {
            return;
        } else if (pathSteps.length == 1) {
            currentNode.put(pathSteps[0], mockValueLooper.getNextMockValue());
            return;
        }

        if (pathSteps[0].equals("$")) {
            if (pathSteps.length > 1) {
                pathSteps = Arrays.copyOfRange(pathSteps, 1, pathSteps.length);
            } else {
                return;
            }
        }

        String pathStep = pathSteps[0];
        // pathStepArrayIndex will be null (not an array), -1 for *, or a specific value for a specific array element.
        Integer pathStepArrayIndex = null;
        if (pathStep.contains("[")) {
            String indexVal = pathStep.substring(pathStep.indexOf("[")+1, pathStep.indexOf("]"));
            if (indexVal.equals("*")) {
                pathStepArrayIndex = -1;
            } else {
                pathStepArrayIndex = Integer.valueOf(indexVal);
            }
        }
        pathSteps = Arrays.copyOfRange(pathSteps, 1, pathSteps.length);

        // Check if the pathStep is an array or map.
        // If it is a map, check for the child, add it if missing and continue.
        // If it is an array, do effectively the same operation as the map, but for each index of the array.

        // The trick with the array is matching the mocks to indexes. If we have an explicit path, arrays without
        // * for indexes, we don't have to worry. We will be dealing with a single mock value.
        // For paths with * and a single mock value we need still have a relatively simple effort, because, each
        // node will get the same value.
        // For a wildcard (*) path with a list of mock values we need to get the mock values added to the correct
        // indexes

        // For example, if we have the path $.first[*].number with mock values for number [1, 2, 3] we expect to
        // generate the JSON as:
        /*
        {
            "first": [
                {
                    "number": 1
                },
                {
                    "number": 2
                },
                {
                    "number": 3
                }
            ]
        }
         */


        // The more complicated case is: $.first[*].second[*].number with mock values for number [1, 2, 3] we expect
        // to generate the JSON as:
        /*
        {
            "first": [
                {
                    "second": [
                        {
                            "number": 1
                        },
                        {
                            "number": 2
                        },
                        {
                            "number": 3
                        }
                    ]
                }
            ]
        }
        */

        // Another case is where the sub array nodes have more than one element. There was another JSON path defined
        // as $.first[3].second[2].number with mock value [9] and then we have our case:
        // $.first[*].second[*].number with mock values [1, 2, 3], then first[] will have 3 elements
        // (from $.first[3]) and second[] will also have 3 elements, because, the wild card mock values for number
        // has 3 mock values.
        // we expect the JSON to look like:
        /*
        {
            "first": [
                {
                    "second": [
                        {
                            "number": 1
                        },
                        {
                            "number": 2
                        },
                        {
                            "number": 3
                        }
                    ]
                },
                {
                    "second": [
                        {
                            "number": 1
                        },
                        {
                            "number": 2
                        },
                        {
                            "number": 3
                        }
                    ]
                },
                {
                    "second": [
                        {
                            "number": 1
                        },
                        {
                            "number": 9 // --------------- This is where 9 is set
                        },
                        {
                            "number": 3
                        }
                    ]
                }
            ]
        }
         */

        Object childNode = currentNode.get(pathStep);
        if (childNode == null) {
            childNode = new HashMap<String, Object>();
            currentNode.put(pathStep, childNode);
            walkAndPopulateJsonMap((Map) childNode, pathSteps, mockValueLooper);
        } else if (childNode instanceof Map) {
            walkAndPopulateJsonMap((Map) childNode, pathSteps, mockValueLooper);
        } else if (childNode instanceof List) {
            List<Map<String, Object>> childList = (List) childNode;

            if (pathStepArrayIndex == null) {
                throw new IllegalStateException("PathStepArrayIndex is not set for an array element.");
            }

            // Check if the remaining path steps contain an array.
            // If there are other array elements, simply iterate over each element and continue recursion with each
            // element.
            if (doPathStepsContainArray(pathSteps)) {

                if (pathStepArrayIndex == -1) {
                    for (Map child : childList) {
                        walkAndPopulateJsonMap(child, pathSteps, mockValueLooper);
                    }
                } else {
                    childNode = childList.get(pathStepArrayIndex);
                    walkAndPopulateJsonMap((Map) childNode, pathSteps, mockValueLooper);
                }

            } else {
                // This is the last step with an array. We need to pass the array indexed mock value to populate mocks
                // in the way expected by the mock author.
                if (pathStepArrayIndex >= 0) {
                    childNode = childList.get(pathStepArrayIndex);
                    walkAndPopulateJsonMap((Map) childNode, pathSteps, mockValueLooper);
                }

                // If we get a case where the number of elements in the array is > the number of elements in the
                // mock set, restart with the first element in the mock set and keep looping until all array
                // elements have been set.
                for (Map<String, Object> child : childList) {
                    // TODO
                }
            }
        } else {
            throw new IllegalStateException("Unexpected childNode type encountered.");
        }
    }

    /**
     * Check if the path steps provided contain a step that is an array element. EG: foo[*] or foo[2].
     * @param pathSteps Steps to check.
     * @return True if path steps contain an array element, false otherwise.
     */
    protected boolean doPathStepsContainArray(String[] pathSteps) {
        if (pathSteps == null) {
            return false;
        }

        for (String step : pathSteps) {
            if (step.contains("[")) {
                return true;
            }
        }
        return false;
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
