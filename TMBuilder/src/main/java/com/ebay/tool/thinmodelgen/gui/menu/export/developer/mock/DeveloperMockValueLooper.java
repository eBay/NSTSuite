package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeveloperMockValueLooper {

    private List<Object> mockValues;
    private int mockValueIndex = 0;

    public DeveloperMockValueLooper(DeveloperMockTypeDecoder mockValues) {
        Objects.requireNonNull(mockValues, "MockValues cannot be null.");
        init(mockValues);
    }

    public Object getNextMockValue() {
        if (mockValueIndex >= mockValues.size()) {
            mockValueIndex = 0;
        }
        Object value = mockValues.get(mockValueIndex);
        mockValueIndex++;
        return value;
    }

    public int getNumberOfMockValues() {
        return mockValues.size();
    }

    private void init(DeveloperMockTypeDecoder mockValues) {

        if (mockValues instanceof DeveloperMockValue) {
            Object value = ((DeveloperMockValue) mockValues).getMockValue();
            Objects.requireNonNull(value, "Mock value MUST never be null.");
            this.mockValues = new ArrayList<>();
            this.mockValues.add(value);
        } else if (mockValues instanceof  DeveloperMockListOfValues) {
            List<Object> values = ((DeveloperMockListOfValues) mockValues).getMockValues();
            Objects.requireNonNull(values, "Mock values MUST never be null.");
            this.mockValues = new ArrayList<>(values);
        }
    }
}
