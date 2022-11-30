package com.ebay.tool.thinmodelgen.gui.menu.filemodel;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import org.testng.annotations.Test;

import com.ebay.jsonpath.TMJPStringCheck;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

public class JsonStringTypeSerializeDeserializeTest {

  @Test(groups = unitTest)
  public void serializeDeserializeTest() throws IOException, ClassNotFoundException {

    JsonBaseType stringType = new JsonStringType("FOO");
    stringType.updateCheckForPath("$.modules.paymentMethods.paymentMethods[0].entries[0].action.name", new TMJPStringCheck().hasLength(4).isEqualTo("test"));

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(stringType);
    objectOutputStream.close();
    String stringTypeSerialized = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

    byte[] stringTypeByteArray = Base64.getDecoder().decode(stringTypeSerialized);
    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(stringTypeByteArray));
    Object obj = objectInputStream.readObject();
    objectInputStream.close();

    assertThat("Deserialized object is not of expected type.", obj, is(instanceOf(JsonStringType.class)));
  }
}
