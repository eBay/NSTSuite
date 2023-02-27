package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockListOfValues;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class ArrayPathToArraySizeMapProcessor {

    /**
     * Populate the arrayPathToArraySize map with the validationSetModel provided. Each time this method is called the
     * arrayPathToArraySize map is added to with paths that contain arrays. The last node in each path added to the
     * arrayPathToArraySize map will end with the array element and the key is the maximum size of that array. The size
     * is parsed from the path (EG: value[3] would be an array node 'value' with size 3. [*] defaults to size 1).
     *
     * @param validationSetModel Model to parse.
     * @return TreeMap of String JSON paths to Integer size of JSON path array. Last array node in each key is what the size value corresponds to. Tree map is ordered according to the SmallestToLargestArrayPathComparator.
     * @throws IOException            Pass through.
     * @throws ClassNotFoundException Pass through.
     */
    public static TreeMap<String, Integer> getArrayPathToArraySizeMap(ValidationSetModel validationSetModel) {

        TreeMap<String, Integer> arrayPathToArraySizeMap = new TreeMap<>(new SmallestToLargestArrayPathComparator());

        NodeModel[] nodes = validationSetModel.getData();
        for (NodeModel node : nodes) {

            JsonBaseType jsonBaseType = null;
            try {
                jsonBaseType = DeveloperMockProcessorUtility.convertNodeModelToJsonBaseType(node);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }

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

                        String indexString = value.substring(arrayOpeningBracketPosition + 1, value.indexOf("]"));
                        int arraySize = 1;
                        if (!indexString.equals("*")) {
                            arraySize = Integer.parseInt(indexString) + 1;
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

        return arrayPathToArraySizeMap;
    }
}
