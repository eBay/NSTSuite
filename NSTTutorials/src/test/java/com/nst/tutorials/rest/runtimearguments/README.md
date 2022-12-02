[_Return to Tutorials Overview_](https://github.com/eBay/NSTSuite/tree/main/NSTTutorials)

# Runtime Arguments

### Overview

NST has several runtime arguments that are supported by default. In this section we will review these runtime arguments, how to add your own custom runtime arguments, and how to add your own custom logger / format writer files.

To see a list of the NST runtime arguments supported out of the box, please see [here](../../../../../../../../../NST/README.md#Runtime Arguments).

### Topics Covered

1. [Adding custom runtime arguments](#Adding custom runtime arguments)
2. [Adding a custom logger](#Adding a custom logger)
3. [Overriding an existing runtime argument](#Overriding an existing runtime argument)

### References
- [RuntimeConfigManager](../../../../../../../../../NST/src/main/java/com/ebay/runtime/RuntimeConfigManager.java)

## Adding custom runtime arguments

To add a custom runtime argument that can be read during a test (for example), you can leverage the `RuntimeConfigManager` . 

To create the runtime argument object, create a class that implements `RuntimeConfigValue`. All the runtime arguments above implement this interface, which you can reference as an example.

[Here](RuntimeArgumentsCustomExample.java) is one such example.
The generic defined with the `RuntimeConfigValue` interface defines what value that runtime argument should return.

Once you have created your runtime argument class, use `RuntimeConfigManager.*getInstance*().addRuntimeArgument` to add it to the manager. Then, you can reference the value that is set during runtime by referencing the key, as so:

`RuntimeConfigManager.*getInstance*().getRuntimeArgumentValue(CustomRuntimeArgument.***KEY***);`

See the [associated test class](RuntimeArgumentsTest.java) for an example of adding a custom runtime argument.

## Overriding an existing runtime argument

We can override custom runtime arguments that have been set by retrieving the `RuntimeConfigValue` that has been set.
Because the runtime argument values can be of any data type, we will need to retrieve and cast it using `RuntimeConfigManager.getRuntimeArgument`.

```java
RuntimeConfigValue<String> customRuntimeArgumentExampleValue =
        (RuntimeConfigValue<String>) RuntimeConfigManager.getInstance().getRuntimeArgument(customRuntimeArgumentExample.getRuntimeArgumentKey());
customRuntimeArgumentExampleValue.override("modifiedValue");
```

See the [associated test class](RuntimeArgumentsTest.java) for a runnable example that demonstrates this functionality.

## Adding a custom logger

NST supports customization of the logging / output that is generated. You may want to add your own custom implementation to modify the output of either the mock response files or the output to the UI test files as well.

Custom loggers are **required** to be implemented if you want to export boilerplate code to UI test files, which is covered later on in detail in section 7.

To do this, we will need to create our own custom logger file which implements *`FormatWriter`*:

- `getPlatformAssociation` - This should return whichever platform you are creating a custom logger for (`IOS, ANDROID, MWEB, SITE`).
- `writeMocks` - This is where you utilize the supplied list of service calls, along with the `testClassName` and `testMethodName` (if desired) to generate the mocks in the format of your choosing.
- `updateTests` - This is where we can again utilize the list of service calls, along with the `testClassName` and `testMethodName` (if desired) to generate the UI test code output that is customized per your specifications.

You can see an example of a fully formed custom logger [here](IosFormatWriter.java).

Once the custom logger has been created, we need to point the `customLoggersPackage` runtime argument to the package that contains our custom loggers.

Example: `-DcustomLoggersPackage=com.nst.tutorials.rest.runtimearguments`

Now, when our test is run with the `MOCKS` or `TESTS` argument set for `whatToWrite`, it will first check the `nstplatform` for what platform the test is being run for, then will check the `customLoggersPackage` defined for any classes implementing `FormatWriter` that have the associated platform set in their `getPlatformAssociation` method.

**To summarize:**

- Create a custom logger by creating a class that implements *`FormatWriter`*
- Ensure platform is set in the logger for whichever platform you are trying to customize output for
- Modify custom logger with any desired specifications / customizations
- Set `customLoggersPackage` to the directory / package that contains your custom loggers
