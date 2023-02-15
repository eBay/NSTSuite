package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface DeveloperMockTypeDecoder<T> {

    default DeveloperMockType getMockType() {

        Type[] genericInterfaces = getClass().getGenericInterfaces();
        if (genericInterfaces == null || genericInterfaces.length != 1) {
            return DeveloperMockType.UNKNOWN;
        }
        ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) genericInterfaces[0];
        Type[] actualTypeArguments = typeImpl.getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length != 1) {
            return DeveloperMockType.UNKNOWN;
        }
        String actualGenericClassType = actualTypeArguments[0].getTypeName();

        if (actualGenericClassType.equals("java.lang.Boolean")) {
            return DeveloperMockType.BOOLEAN;
        } else if (actualGenericClassType.equals("java.lang.Double")) {
            return DeveloperMockType.DOUBLE;
        } else if (actualGenericClassType.equals("java.lang.Integer")) {
            return DeveloperMockType.INTEGER;
        } else if (actualGenericClassType.equals("java.lang.String")) {
            return DeveloperMockType.STRING;
        } else {
            return DeveloperMockType.UNKNOWN;
        }
    }
}
