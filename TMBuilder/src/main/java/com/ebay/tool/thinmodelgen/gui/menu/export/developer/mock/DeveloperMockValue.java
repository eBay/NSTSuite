package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock;

import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.DeveloperMockTypeDecoder;

public interface DeveloperMockValue<T> extends DeveloperMockTypeDecoder<T> {

    public T getMockValue();

    public void setMockValue(T value);
}
