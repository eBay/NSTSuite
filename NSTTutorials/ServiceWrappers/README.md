[_Return to Tutorials Overview_](../README.md)

# Service Wrappers

### Overview

For any contract validation test in NST, we need to make service calls with an associated service wrapper class. Service wrappers are classes that contain all details required for the service to be called using NST, such as the endpoint, the HTTP method, and the like. How the contract and response data is validated and handled per your requirements is shown in further sections (contract validation / thin model validations).

### Topics Covered

1. [Making a basic REST GET request](#making-a-basic-rest-get-request)
2. [Using serviceHosts.csv](#using-serviceHosts.csv)
3. [Other HTTP methods](#other-http-methods)

### References
- [TestNG SoftAssert](https://www.javadoc.io/doc/org.testng/testng/6.8.8/org/testng/asserts/SoftAssert.html)

## Making a basic REST GET request

For this initial example, we will be creating / implementing an NST REST GET request. Any REST request must implement *`NSTRestServiceWrapper`*.

**NOTE**: When you are creating service wrappers, it is important that any specifics that are required such as the query parameters or a request payload object is initialized with the service wrapper class itself. 

The required *`NSTRestServiceWrapper`* methods are:

- `getServiceName` - Must return the name of the service. The service name is used when forming the full endpoint URL in the `prepareRequest` method.
- `getRequestType` - Must return the type of HTTP request, in this example, it would be `NstRequestType.GET`.
- `getEndpointPath` - Must return the **endpoint** of the call.
- `prepareRequest` - Where the request object (`NSTHttpRequest`) is formed. This includes any headers that may need to be sent, setting the actual URL for the request, including setting any required parameters that the service wrapper was initialized with, and finally using the `NSTHttpRequestImpl` builder to form the `NSTHttpRequest`. The host name is retrieved from `getServiceName` and prefixed to `getEndpointPath` to form the full URL. We are using `HostsManager` in order to retrieve the **host** for the provided **service name**, using the `serviceHosts.csv` mapping. This `serviceHosts.csv` mapping allows you to easily switch hosts between environments such as QA, dev, and production. More on this in the following section, `serviceHosts.csv`.
    ```java
    private static final String SERVICE_NAME = "canadaholidays";
    private static final String ENDPOINT = "/api/v1/holidays/{holidayId}";
    
    @Override
    public NSTHttpRequest prepareRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("USER-AGENT", "testHeader");
    
        URL url;
        try {
            String path = HostsManager.getInstance().getHostForService(SERVICE_NAME) + ENDPOINT;
            url = new URL(path.replace("{holidayId}", canadaHoliday.getHolidayId().toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    
        return new NSTHttpRequestImpl.Builder(url, NstRequestType.GET)
                .setHeaders(headers)
                .build();
    }
    ```

- `getSchemaValidator` - Must return some instance of `NSTRestSchemaValidator`. In this first example however, we will be returning null - when set to null, no schema / contract validation will be triggered when this request wrapper is sent.
    
    ```java
    @Override
    public NSTRestSchemaValidator getSchemaValidator() {
        return null
    }
    ```
    

Now, let’s use the successfully created NST service wrapper within a test class to send the request and retrieve the `JSONObject` response.

Create the TestNG test class and have it implement the *`NSTServiceTestRunner`* interface. You **must** supply the test class with an instance of `EbaySoftAssert`, which inherits TestNG’s `SoftAssert` class. This is done in the `getSoftAssert` call. 

SoftAsserts are utilized in order to aggregate failures that occurred into a log that is output at the end of the test instead of failing the test immediately upon the first failure. They are utilized in NST when passing them to the thin models, which are covered in a later tutorial. For now, we will set this to `null`, as we will not be using it yet.

`NSTServiceWrapperProcessor` is the class that we will use to send service wrappers. This processor only takes in 1 parameter for the call to send the request and receive the response (`sendRequestAndGetJSONResponse`), which is the `NSTServiceWrapper` instance.

In the example below, we are using this processor to send our service wrapper we created above in order to retrieve the JSONObject response. Only one instance of the processor is required to send any number of NST service wrappers.

```java
@Test
public void exampleRestTest() throws Exception {
    NSTServiceWrapperProcessor serviceProcessor = new NSTServiceWrapperProcessor();

    // Send a GET /api/v1/holidays/{holidayId} request.
    ExampleRestServiceWrapper restServiceWrapper = new ExampleRestServiceWrapper(CanadaHoliday.CANADA_DAY);
    JSONObject response = serviceProcessor.sendRequestAndGetJSONResponse(restServiceWrapper);
}
```

## Using serviceHosts.csv

As we mentioned above, every service wrapper class has a service name (`getServiceName`), which is used alongside `getEndpointPath` when forming the full URL of the request with the `HostsManager` in the `prepareRequest` method. The `serviceHosts.csv` is used to map the service with the desired host, when the `HostsManager` is utilized. This allows you to easily switch the hosts for service wrappers between environments such as QA and PROD. 

Ensure that this `serviceHosts.csv` resides in your resources directory.
```java
serviceName,environment,host
canadaholidays,qa,https://canada-holidays.ca
```

The columns required in this CSV file are:

- `serviceName` - The name of the service, utilized when `getServiceName` is called in the service wrapper.
- `environment` - The environment of the host, such as `QA` or `PROD`. The environment utilized here is defined by the `testExeEnv` runtime argument. By default, it is set to `QA`.
- `host` - The host of the service to use, for the defined environment.

## Other HTTP methods

When sending requests that require payloads such as PUT or POST methods, we need to pass a serialized request payload in our service wrapper.

The request payload class should be created with any required fields and must implement the *`NSTRestRequest`* interface*.* Once this class is created, ensure that the service wrapper is initialized with it. 

It should then be serialized in your `prepareRequest` service wrapper implementation. You can use any serializer you prefer. In this example we are using `Gson` .

```java
Gson gson = new Gson();
String payload = gson.toJson(request);
return new NSTHttpRequestImpl.Builder(url, NstRequestType.POST)
        .setPayload(payload)
        .setHeaders(headers)
        .build();
```