package com.ebay.jsonpath;

public interface NullCheck<T extends JsonPathExecutor> {

    /**
     * Check if the node selected is returned as null.
     * @param mustBeNull True to check that the value is null, false to make sure it isn't null.
     */
    T checkIsNull(boolean mustBeNull);

    /**
     * Check if null is expected for the node.
     */
    boolean isNullExpected();
}
