package com.ebay.builder;

@SuppressWarnings("serial")
public class InvalidSchemaValidationException extends RuntimeException {

    public InvalidSchemaValidationException(String part) {
        super(part + "for Schema Validation");
    }
}
