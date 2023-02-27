package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.jsonpath.*;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;

import java.io.IOException;
import java.util.Objects;

public class DeveloperMockProcessorUtility {

    public enum PrimitiveType {
        STRING,
        BOOLEAN,
        INTEGER,
        DOUBLE,
        UNKNOWN
    }

    /**
     * Convert a NodeModel to a JsonBaseType.
     * @param nodeModel Model to convert.
     * @return JsonBaseType instance of NodeModel.
     * @throws IOException Pass through.
     * @throws ClassNotFoundException Pass through.
     */
    public static JsonBaseType convertNodeModelToJsonBaseType(NodeModel nodeModel) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(nodeModel, "NodeModel MUST NOT be null.");
        String serializedData = nodeModel.getSerializedUserObject();
        return JsonBaseTypePersistence.deserialize(serializedData);
    }

    /**
     * Check if the JSON path step provided is an array or not. An array step is defined as a step containing array brackets [].
     * @param jsonPathStep JSON path step to evaluate.
     * @return True if JSON path step is an array, false otherwise.
     */
    public static boolean isJsonPathStepAnArray(String jsonPathStep) {
        if (jsonPathStep != null && jsonPathStep.contains("[")) {
            return true;
        }
        return false;
    }

    /**
     * Get the array index from the step. EG: 'foo[1]' would return index 1.
     * For an array with a wildcard index '*' -1 will be returned.
     * @param step Step to evaluate.
     * @return Array index or -1 if the array index is wildcard '*'.
     * @throws IllegalArgumentException If the step does not contain an array index.
     * @throws NumberFormatException If the array index value is unable to be parsed as an int.
     */
    public static int getArrayIndexFromStep(String step) throws IllegalArgumentException, NumberFormatException {

        if (!isJsonPathStepAnArray(step)) {
            throw new IllegalArgumentException(String.format("Path step %s does not contain an array index.", step));
        }

        step = step.substring(step.indexOf("[") + 1);
        step = step.substring(0, step.indexOf("]"));

        if (step.equals("*")) {
            return -1;
        } else {
            return Integer.parseInt(step);
        }
    }

    /**
     * Get the primitive type of the check associated with the specified path.
     * @param jsonBaseType JsonBaseType instance to get check for based on the path.
     * @param path Path to use with the JsonBaseType to extract the primitive type of the check.
     * @return Primitive type value. Will return UNKNOWN if the path does not resolve correctly or the check type is unknown.
     */
    public static PrimitiveType getPrimitiveTypeForCheck(JsonBaseType jsonBaseType, String path) {

        Objects.requireNonNull(jsonBaseType, "JsonBaseType MUST NOT be null.");
        Objects.requireNonNull(path, "Path MUST NOT be null.");
        JsonPathExecutor pathCheck = jsonBaseType.getCheckForPath(path);

        if (pathCheck == null) {
            return PrimitiveType.UNKNOWN;
        }

        if (pathCheck instanceof TMJPBooleanCheck || pathCheck instanceof TMJPListOfBooleanCheck) {
            return PrimitiveType.BOOLEAN;
        } else if (pathCheck instanceof TMJPDoubleCheck || pathCheck instanceof TMJPListOfDoubleCheck) {
            return PrimitiveType.DOUBLE;
        } else if (pathCheck instanceof TMJPIntegerCheck || pathCheck instanceof TMJPListOfIntegerCheck) {
            return PrimitiveType.INTEGER;
        } else if (pathCheck instanceof TMJPStringCheck || pathCheck instanceof TMJPListOfStringCheck) {
            return PrimitiveType.STRING;
        }

        return PrimitiveType.UNKNOWN;
    }
}
