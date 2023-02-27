package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.jsonpath.*;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBooleanType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonFloatType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonIntegerType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;

import java.io.IOException;
import java.util.List;

public class TestUtility {

    public static NodeModel getStringNodeModel(String nodeName, String jsonPath, String mockValue) throws IOException {
        JsonStringType type = new JsonStringType(nodeName);
        type.updatePath("", jsonPath);
        TMJPStringCheck typeCheck = new TMJPStringCheck();
        typeCheck.setMockValue(mockValue);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getListOfStringNodeModel(String nodeName, String jsonPath, List<String> mockValues) throws IOException {
        JsonStringType type = new JsonStringType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfStringCheck typeCheck = new TMJPListOfStringCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getIntegerNodeModel(String nodeName, String jsonPath, int mockValues) throws IOException {
        JsonIntegerType type = new JsonIntegerType(nodeName);
        type.updatePath("", jsonPath);
        TMJPIntegerCheck typeCheck = new TMJPIntegerCheck();
        typeCheck.setMockValue(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getListOfIntegerNodeModel(String nodeName, String jsonPath, List<Integer> mockValues) throws IOException {
        JsonIntegerType type = new JsonIntegerType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfIntegerCheck typeCheck = new TMJPListOfIntegerCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getListOfBooleanNodeModel(String nodeName, String jsonPath, List<Boolean> mockValues) throws IOException {
        JsonBooleanType type = new JsonBooleanType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfBooleanCheck typeCheck = new TMJPListOfBooleanCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getBooleanNodeModel(String nodeName, String jsonPath, boolean mockValues) throws IOException {
        JsonBooleanType type = new JsonBooleanType(nodeName);
        type.updatePath("", jsonPath);
        TMJPBooleanCheck typeCheck = new TMJPBooleanCheck();
        typeCheck.setMockValue(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getListOfDoubleNodeModel(String nodeName, String jsonPath, List<Double> mockValues) throws IOException {
        JsonFloatType type = new JsonFloatType(nodeName);
        type.updatePath("", jsonPath);
        TMJPListOfDoubleCheck typeCheck = new TMJPListOfDoubleCheck();
        typeCheck.setMockValues(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }

    public static NodeModel getDoubleNodeModel(String nodeName, String jsonPath, double mockValues) throws IOException {
        JsonFloatType type = new JsonFloatType(nodeName);
        type.updatePath("", jsonPath);
        TMJPDoubleCheck typeCheck = new TMJPDoubleCheck();
        typeCheck.setMockValue(mockValues);
        type.updateCheckForPath(jsonPath, typeCheck);
        String serializedType = JsonBaseTypePersistence.serialize(type);
        NodeModel model = new NodeModel(null, serializedType);
        return model;
    }
}
