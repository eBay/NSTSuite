[_Return to Tutorials Overview_](https://github.com/eBay/NSTSuite/tree/main/NSTTutorials)

# Response Logger Injections

### Overview

For use cases where the mock response files / data needs to be modified in some way before the file is generated, we can leverage response loggers.

### Topics Covered

1. Test

### References
- [ResponseLoggerInjector](../../../../../../../../../NST/src/main/java/com/ebay/service/logger/injection/ResponseLoggerInjector.java)

An example use case would be some scenario in which you would want the fully validated mock response, but then wanted to modify some specific field’s VALUE to something else, for testing purposes.

This is accomplished at the service wrapper level. Service wrappers have a method `getResponseLoggerInjector` that requires an implementation of *`ResponseLoggerInjector`.* 

*`ResponseLoggerInjector`* only has 1 method, `processServiceResponse` , which is where we will be making our modifications to the response data. 

Here is an example of how you may wish to do that:

```java
@Override
public String processServiceResponse(String rawServiceResponsePayload) {
    // Change the childKey value to "modified".
    JSONObject jsonObject = null;
    try {
        jsonObject = new JSONObject(rawServiceResponsePayload);
        jsonObject.getJSONObject("field").put("childKey", "modified");
    } catch (JSONException e) {
        e.printStackTrace();
    }

    return jsonObject.toString();
}
```

After we’ve created our *`ResponseLoggerInjector` ,* we just need to set it in the service wrapper class in `getResponseLoggerInjector`. Now, whenever this service wrapper is sent, the generated mock response file will have the specified fields “injected” into it.