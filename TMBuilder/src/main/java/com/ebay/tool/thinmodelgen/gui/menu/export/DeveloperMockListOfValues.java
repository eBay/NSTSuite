package com.ebay.tool.thinmodelgen.gui.menu.export;

import java.util.List;

public interface DeveloperMockListOfValues<T> extends DeveloperMockTypeDecoder<T> {

    public List<T> getMockValues();

    public void setMockValues(List<T> values);

    @Override
    default DeveloperMockType getMockType() {

        DeveloperMockType mockType = DeveloperMockTypeDecoder.super.getMockType();

        switch (mockType) {
            case BOOLEAN:
                return DeveloperMockType.LIST_OF_BOOLEAN;
            case DOUBLE:
                return DeveloperMockType.LIST_OF_DOUBLE;
            case INTEGER:
                return DeveloperMockType.LIST_OF_INTEGER;
            case STRING:
                return DeveloperMockType.LIST_OF_STRING;
            default:
                return DeveloperMockType.UNKNOWN;
        }
    }
}
