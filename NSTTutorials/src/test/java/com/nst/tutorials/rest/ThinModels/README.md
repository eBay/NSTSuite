[_Return to Tutorials Overview_](../../../../../../../../README.md)

# Thin Models

### Overview

Now that we’ve successfully sent a request and received a response and passed contract validation, let’s go through how we validate the contents in the response that are specific to our client test automation concerns.

“Thin models” are named as such because they are response data models that are mainly concerned with the specific concerns of the client. The general idea behind thin models is that we should utilize them to ensure the response data would be valid for our UI tests (or some test flow).

Unlike contract validations, thin model checks are performed during the course of test case execution. When the test author instantiates a thin model, that thin model will perform the validation operations against the specified response payload. Thus, these validations performed upon initialization are the general data checks that MUST be valid for all responses supplied to this thin model. Additional validations can be added as separate methods.

Thin models get their name by not being backed by a POJO tree. They are a wrapper around the parsed JSON object and use JSON path calls used to retrieve data from the model. Getters using JSON path may be defined for specific data retrieval needs. This decouples the tests from complex service models and provides a lightweight solution to retrieving response data in tests.

### Topics Covered

1. [Creating a thin model](#Creating a thin model)
2. [Validating JSONPath data](#Validating JSON path data)
3. [Retrieving data](#Retrieving data)
4. [Utilizing thin models](#Utilizing thin models)

### References
- [JSONPath](https://goessner.net/articles/JsonPath/)
- [JPChecks](../../../../../../../../../NST/src/main/java/com/ebay/jsonpath)
- [TMBuilder](../../../../../../../../../TMBuilder)

## Creating a thin model

Let’s look at an example thin model class.

```java
public class ExampleResponseModel extends NSTServiceModelBase {

    public ExampleResponseModel(JSONObject jsonRoot, SoftAssert softAssert) {
        super(jsonRoot, softAssert);
    }

    @Override
    protected void validate(SoftAssert softAssert) {

        generatedValidations(softAssert);
    }

    public Integer getId() {
        return readJsonPath("$.holiday.id");
    }

    // TMB Generated Validation Method
    private void generatedValidations(SoftAssert softAssert) {

        Map<String, JsonPathExecutor> validations = new HashMap<>();
        validations.put("$.holiday.id", new JPIntegerCheck().isEqualTo(15));
        validations.put("$.holiday.nameEn", new JPStringCheck().isEqualTo("Canada Day"));
        evaluateJsonPaths(validations, softAssert);
    }
}
```

- Every thin model must extend the `NSTServiceModelBase` class. As mentioned above, whenever any thin model class is instantiated, the `validate` method will be called. Thus, we would want to ensure that anything inside of the `validate` method applies to all scenarios in which this thin model would need to be utilized - i.e. a “base case” validation.
- The default constructor for `NSTServiceModelBase` requires the `JSONObject` root response object, along with an instance of a TestNG `SoftAssert`.
- The `generatedValidations` have been created using the **TMBuilder**, using the same contract file that is referenced within the service wrapper. For information on the TMBuilder tool, please see [this link](https://github.com/eBay/NSTSuite/tree/main/TMBuilder).

## Validating JSON path data

Next, let’s review the format in which we validate the response data within thin models, using JSON path.

An example can be seen inside the `generatedValidations` method:

```java
Map<String, JsonPathExecutor> validations = new HashMap<>();
validations.put("$.holiday.id", new JPIntegerCheck().isEqualTo(15));
validations.put("$.holiday.nameEn", new JPStringCheck().isEqualTo("Canada Day"));
evaluateJsonPaths(validations, softAssert);
```

`evaluateJsonPaths` and `evaluateJsonPath` are the two methods from `NSTServiceModelBase` that are used to check certain JSON paths in the `JSONObject` response data.

3 things are required for NST thin model validations:

- The JSON path of the data to validate
- The `JPCheck` instance
- The `SoftAssert` instance

There are a variety of JPChecks that are available for various data types. These can be seen [here](https://github.corp.ebay.com/byarger/NSTSuite/tree/main/NST/src/test/java/com/ebay/jsonpath).

For any JPCheck, the default constructor will ensure that the path specified is not null and not empty. The additional methods such as `isEqualTo` are modifiers on top of this “base” JPCheck validation. For example, `isEqualTo` should be utilized when it is critical that some field has a specific value (on top of being not null and not empty). An example of this would be if there is a specific value that the client is explicitly dependent on to trigger some functionality - not just that it has some value to render.

To note, these validations can be generated automatically using the **TMBuilder** and the OpenAPI contract yaml file or JSON schema file.

## Retrieving data

Thin models can also be used to “reach in” and get some data that may be required for other service wrappers or to validate some data.

You can utilize the `readJsonPath` method in order to do this.

**Example**:

```java
public Integer getId() {
    return readJsonPath("$.holiday.id");
}
```

## Utilizing thin models

Now that we’ve created our thin model, we can use it within our tests.

```java
private static final EbaySoftAssert SOFT_ASSERT = new EbaySoftAssert();

@Test
public void exampleRestTest() throws Exception {
    NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

    // Send a GET /api/v1/holidays/{holidayId} request.
    ThinModelsWrapper restServiceWrapper = new ThinModelsWrapper(CanadaHoliday.CANADA_DAY);
    JSONObject response = serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
    ThinModelsThinModel thinModel = new ThinModelsThinModel(response, SOFT_ASSERT);
    Assert.assertNotEquals(thinModel.getId(), 10, "ID must NOT be equal to 10.");
}

@Override
public EbaySoftAssert getSoftAssert() {
    return SOFT_ASSERT;
}
```

The main difference here is that we are now passing the `response` JSONObject data to the newly created instance of the thin model, along with the `SoftAssert`. `SoftAssert` is now being utilized in the thin models, so we are creating an instance of `EbaySoftAssert` and passing it to the thin model to run the validation checks with it. Additionally, we are using the getter method that we created in order to do a hard assertion that some field (in this case, `id`) , is not null.

To reiterate, the SoftAssert instance supplied to the thin model will gather/aggregate failures in the thin model validations and output all of the failures at the end using `assertAll`.

You can see an example of a thin model violation / validation error in the `exampleRestTestWithThinModelFailure` test. The output will show all of the thin model validations that failed, along with an in-lined stacktrace and finally the assertion failure itself.

**Example**:

```
java.lang.AssertionError: The following asserts failed:
	Path $.holiday.nameEn failed with unexpected actual values.. : [com.nst.tutorials.rest.thinmodels.ThinModelsTest.exampleRestTestWithFailure() - ln 33] > [com.nst.tutorials.rest.thinmodels.ThinModelsThinModel.validate() - ln 22] > [com.nst.tutorials.rest.thinmodels.ThinModelsThinModel.generatedValidations() - ln 35] > [com.ebay.nst.NSTServiceModelBase.evaluateJsonPaths() - ln 165] > [com.ebay.nst.NSTServiceModelBase.evaluateJsonPath() - ln 170] > [com.ebay.jsonpath.JPStringCheck.processJsonPath() - ln 207] --> expected [Canada Day] but found [Day of Mourning for Queen Elizabeth II],
	Path $.holiday.id failed with values that do not match.. : [com.nst.tutorials.rest.thinmodels.ThinModelsTest.exampleRestTestWithFailure() - ln 33] > [com.nst.tutorials.rest.thinmodels.ThinModelsThinModel.validate() - ln 22] > [com.nst.tutorials.rest.thinmodels.ThinModelsThinModel.generatedValidations() - ln 35] > [com.ebay.nst.NSTServiceModelBase.evaluateJsonPaths() - ln 165] > [com.ebay.nst.NSTServiceModelBase.evaluateJsonPath() - ln 170] > [com.ebay.jsonpath.JPIntegerCheck.processJsonPath() - ln 69] --> expected [15] but found [27]
```