package com.ebay.jsonpath;

public interface NullCheck {

    /**
     * Check if the node selected is returned as null.
     * @param mustBeNull True to check that the value is null, false to make sure it isn't null.
     */
    void checkIsNull(boolean mustBeNull);
}
