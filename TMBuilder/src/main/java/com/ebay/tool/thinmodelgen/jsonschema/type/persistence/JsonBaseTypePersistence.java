package com.ebay.tool.thinmodelgen.jsonschema.type.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;

public class JsonBaseTypePersistence {

  public static String serialize(JsonBaseType jsonBaseType) throws IOException {

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(jsonBaseType);
    objectOutputStream.close();
    String serializedJsonPathExecutorInstanceData = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

    return serializedJsonPathExecutorInstanceData;
  }

  public static JsonBaseType deserialize(String serializedData) throws IOException, ClassNotFoundException {

    byte[] serializedDataByteArray = Base64.getDecoder().decode(serializedData);
    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(serializedDataByteArray));
    JsonBaseType jsonBaseType = (JsonBaseType) objectInputStream.readObject();
    objectInputStream.close();

    return jsonBaseType;
  }
}
