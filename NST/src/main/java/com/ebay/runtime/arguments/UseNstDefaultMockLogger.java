package com.ebay.runtime.arguments;

import com.ebay.runtime.RuntimeConfigValue;

public class UseNstDefaultMockLogger implements RuntimeConfigValue<Boolean> {

    public static final String KEY = "useNstDefaultMockLogger";
    private Boolean useNstDefaultMockLogger;

    @Override
    public String getRuntimeArgumentKey() {
        return KEY;
    }

    @Override
    public Boolean getRuntimeArgumentValue() {
        return useNstDefaultMockLogger;
    }

    @Override
    public Boolean override(Boolean value) {
        throw new IllegalArgumentException(String.format("The %s runtime argument MAY NOT be overridden.", KEY));
    }

    @Override
    public void parseRuntimeArgument(String argumentValue) {
        useNstDefaultMockLogger = Boolean.parseBoolean(argumentValue);
    }
}
