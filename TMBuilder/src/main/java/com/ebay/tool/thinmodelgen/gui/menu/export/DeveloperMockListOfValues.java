package com.ebay.tool.thinmodelgen.gui.menu.export;

import java.util.List;

public interface DeveloperMockListOfValues<T> extends DeveloperMockTypeDecoder<T> {

    public List<T> getMockValues();

    public void setMockValues(List<T> values);

}
