# Contract Validations

## Overview

One of the core functionalities of NST is contract testing, where a response is compared against the service schema and the data returned in the response is evaluated. This ensures that a service / client contract has been maintained and is intact. This is critical, especially when considering the use case of generating mock response files for use in the client.

The contract can refer to either an OpenAPI / Swagger yaml, JSON schema, or GraphQL schema file that defines the contract between the client and the service. The schema validation happens automatically whenever any service wrapper is sent, provided the `getSchemaValidator` method is implemented and returns a valid *`NSTSchemaValidator`* instance.

## Topics Covered

1. Setting `getSchemaValidator` in the Service Wrapper
2. Schema validation errors
3. Polymorphic schema validation errors

## References
- [JSON Schema Validator](https://github.com/java-json-tools/json-schema-validator)
- [OpenAPI Specification](https://swagger.io/specification/)
- [oneOf / anyOf](https://swagger.io/docs/specification/data-models/oneof-anyof-allof-not/)

### a. Setting `getSchemaValidator` in the Service Wrapper

As we saw above, we need to set up our service wrappers to return a *`NSTSchemaValidator`*. This is the schema validator that will be utilized to compare the service response against the schema definition.

In our example, we are using the `OpenApiSchemaValidator` because the contract of the service we are calling is defined in OpenAPI format. Note that the `OpenApiSchemaValidator` uses the builder pattern to be created, discussed in more detail below.

```java
@Override
public NSTRestSchemaValidator getSchemaValidator() {
    return new OpenApiSchemaValidator.Builder(
            "canada-holidays.yaml",
            "/api/v1/holidays/{holidayId}",
            NstRequestType.GET)
            .allowAdditionalProperties(AllowAdditionalProperties.YES)
            .setStatusCode(StatusCode._200)
            .build();
}
```

The `OpenApiSchemaValidator` builder requires the following:

- Path to the OpenAPI yaml contract schema file
- The endpoint of the request to validate (this would be the line item in the OpenAPI yaml  `paths` definition)
- The request type

Additionally, the builder supports the following:

- `allowAdditionalProperties` - By default, this is set to `YES` . This means that it will allow the response to contain attributes that are not in the contract. When set to `NO`, the contract validation will fail if there are attributes that are not in the contract.
    
    Please note, disallowing additional properties will cause schemas with allOf to be inlined in the generated JSON Schema, thus validation failures for required fields will not describe which part of the composite model carries the required attribute(s).
    
- `setStatusCode` - Setting the status code to map to, again looking through the OpenAPI yaml `paths` definition to find the desired status code for the schema definition you wish to validate against.

By default, the payload used to validate against will be the response payload. However, you can also use `setPayload` if this needs to be modified.

This is just one example, but you are free to use `JsonSchemaValidator` or `OpenApiSchemaValidator`, depending on your use case.

**NOTE**: NST utilizes the [json-schema-validator library](https://github.com/java-json-tools/json-schema-validator) to validate the response data against the supplied schema.

### **b. Schema validation errors**

Let’s walk through an example of what a schema validation error looks like.

```
error: instance type (integer) does not match any allowed primitive type (allowed: ["string"])
    level: "error"
    schema: {"loadingURI":"#","pointer":"/definitions/Holiday/properties/id"}
    instance: {"pointer":"/holiday/id"}
    domain: "validation"
    keyword: "type"
    found: "integer"
    expected: ["string"]
```

In the above error, there are a few things we can use to understand what failed. 

- `schema` - Refers to the schema definition within the OpenAPI yaml contract supplied where the error occurred.
- `instance` - Refers to the path in the JSON response that triggered the validation error / failed.
- `keyword` - The type of contract validation error that occurred.
- `found / expected` - Expected vs. what was found in the response data.

In this case, at (response data) instance {"pointer":"/holiday/id"} , we were expecting the field to be of type `string`, but found an `integer`. 

To see / debug an example of this schema validation error, please see [this](https://github.com/eBay/NSTSuite/blob/main/NSTTutorials/src/test/java/com/nst/tutorials/rest/contractvalidation/ContractValidationTest.java#L22) example test method.

### c. Polymorphic schema validation errors

If the contract definition contains a `oneOf` or an `anyOf` definition, then a polymorphic schema validation error may occur.

Let’s look at an example error log:

```
{
  "level" : "error",
  "schema" : {
    "loadingURI" : "#",
    "pointer" : "/properties/holiday"
  },
  "instance" : {
    "pointer" : "/holiday"
  },
  "domain" : "validation",
  "keyword" : "oneOf",
  "message" : "instance failed to match exactly one schema (matched 0 out of 2)",
  "matched" : 0,
  "nrSchemas" : 2,
  "reports" : {
    "/properties/holiday/oneOf/0" : [ {
      "level" : "error",
      "schema" : {
        "loadingURI" : "#",
        "pointer" : "/definitions/HolidayWithoutId"
      },
      "instance" : {
        "pointer" : "/holiday"
      },
      "domain" : "validation",
      "keyword" : "additionalProperties",
      "message" : "object instance has properties which are not allowed by the schema: [\"id\"]",
      "unwanted" : [ "id" ]
    } ],
    "/properties/holiday/oneOf/1" : [ {
      "level" : "error",
      "schema" : {
        "loadingURI" : "#",
        "pointer" : "/definitions/HolidayExpectingIdAsString/properties/id"
      },
      "instance" : {
        "pointer" : "/holiday/id"
      },
      "domain" : "validation",
      "keyword" : "type",
      "message" : "instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])",
      "found" : "integer",
      "expected" : [ "string" ]
    } ]
  }
}
```

Again, we see `keyword`, which indicates in this case that this is a `oneOf` violation. The `message` tells us that “instance failed to match against exactly one schema (matched 0 out of 2)”. `oneOf` essentially means, it must only match one of the supplied schemas. This is why it failed, since it did not match either of the two definitions.

In our example, the first definition that is allowed for `holiday` is the original `Holiday` definition, but without any `id` field defined. Since the `holiday` returned in the response contains an `id` field, it does  not meet the criteria for this first definition, and moves on to attempt to validate against the next definition for `holiday`, which is the original `Holiday` definition, but expecting a `string` type instead of an `integer` for the `id` field. 

Since the response we are validating against does not match either of these definitions, we get a verbose log showing exactly what failed in each case in the `reports` section. 
Each of the errors in this `reports` object have their own `instance`, `keyword`, `message`, `found`, and `expected` fields, which were described in section b above.

Here is the example definition that would trigger the above error:

```yaml
holiday:
  oneOf:
    - $ref: "#/components/schemas/HolidayWithoutId"
    - $ref: "#/components/schemas/HolidayExpectingIdAsString"
```

Each of the `-` line items in a oneOf or an anyOf definition would be converted to an array, which is what the `0` and `1` indexes in the error messages indicate above.

To summarize this example:

- The contract defined had a `oneOf` (polymorphic) definition for the `holidays` object
- The response data contained an `id` field at the specified `instance` path that was an `integer` type.
- The schema validator ran using this response data, first against the `HolidayWithoutId` definition, at index 0. This failed, since the response data contains an `id` field, and the service wrapper has the `.allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO)` set, disallowing any undefined properties being returned in the response. An appropriate error message was appended to the `reports`.
- The schema validator ran against the next defined type in the `oneOf` block, `HolidayExpectingIdAsString`, at index 1. This failed, since the response data has an `id` field returned as an integer type, and an error message was appended to the `reports`.

To see / debug an example of this polymorphic schema validation error, please see [this](https://github.com/eBay/NSTSuite/blob/main/NSTTutorials/src/test/java/com/nst/tutorials/rest/contractvalidation/ContractValidationTest.java#L29) example test method.