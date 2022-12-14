package com.ebay.nst.tutorials.rest.runtimearguments;

import com.ebay.runtime.RuntimeConfigValue;

public class RuntimeArgumentsCustomExample implements RuntimeConfigValue<String> {

    public static final String KEY = "runtimeArgumentsCustomExample";
    private String runtimeArgumentsCustomExample = "initialValue";

    @Override
    public String getRuntimeArgumentKey() {
        return KEY;
    }

    @Override
    public String getRuntimeArgumentValue() {
        return runtimeArgumentsCustomExample;
    }

    @Override
    public void parseRuntimeArgument(String argumentValue) {
        if (argumentValue != null) {
            runtimeArgumentsCustomExample = argumentValue;
        }
    }

    @Override
    public String override(String value) {
        runtimeArgumentsCustomExample = value;
        return runtimeArgumentsCustomExample;
    }

}
