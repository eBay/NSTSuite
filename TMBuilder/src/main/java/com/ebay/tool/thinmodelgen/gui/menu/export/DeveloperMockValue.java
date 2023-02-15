package com.ebay.tool.thinmodelgen.gui.menu.export;

public interface DeveloperMockValue<T> extends DeveloperMockTypeDecoder<T> {

    public T getMockValue();

    public void setMockValue(T value);
}
