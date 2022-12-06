[_Return to Tutorials Overview_](https://github.com/eBay/NSTSuite/tree/main/NSTTutorials)

# Runtime Arguments

### Overview

NST has several runtime arguments that are supported by default. In this section we will review these runtime arguments, how to add your own custom runtime arguments, and how to add your own custom logger / format writer files.

To see a list of the NST runtime arguments supported out of the box, please see [here](../../../../../../../../../NST/README.md#Runtime Arguments).

### Topics Covered

1. [Runtime argument usage](#Runtime argument usage)
2. [Adding custom runtime arguments](#Adding custom runtime arguments)
3. [Overriding runtime arguments](#Overriding runtime arguments)

### References
- [RuntimeConfigManager](../../../../../../../../../NST/src/main/java/com/ebay/runtime/RuntimeConfigManager.java)
- [TestNG SureFire plugin](https://maven.apache.org/surefire/maven-surefire-plugin/examples/testng.html)

## Runtime argument usage

NST TestNG runtime arguments are specified for NST either as command line arguments, specified in the TestNG Surefire plugin configuration, or optionally as parameters in a TestNG test suite file.
Various utilitarian runtime arguments are supported in NST when using the `RuntimeConfigManager`. It's up to you how you want to accomplish providing these NST TestNG runtime argument values. One possible disadvantage of hardcoding runtime arguments in the Surefire TestNG configuration is that the pom.xml file may need to be reloaded / refreshed whenever any change is made (depending on your IDE), as opposed to if the arguments were defined in a TestNG Test Suite file, or in the command line, which would take effect immediately.

There are built in getter methods for the "out of the box" runtime arguments that were [linked](../../../../../../../../../NST/README.md#Runtime Arguments) above.
Additionally, there is an override method for the platform that has been set (`overridePlatform`).

In general, the runtime arguments that are supported (without any customization) are sufficient for creating tests with NST. However, there may be use cases wherein you want to add your own custom runtime argument, which is explained below. 

## Adding custom runtime arguments

To add a custom runtime argument that can be read during a test (for example), you can leverage the `RuntimeConfigManager` . 

To create the runtime argument object, create a class that implements `RuntimeConfigValue`. All the runtime arguments above implement this interface, which you can reference as an example.

[Here](RuntimeArgumentsCustomExample.java) is one such example.
The generic defined with the `RuntimeConfigValue` interface defines what value that runtime argument should return.

Once you have created your runtime argument class, use `RuntimeConfigManager.getInstance().addRuntimeArgument` to add it to the manager. Then, you can reference the value that is set during runtime by referencing the key, as so:

`RuntimeConfigManager.getInstance().getRuntimeArgumentValue(CustomRuntimeArgument.KEY);`

See the [associated test class](RuntimeArgumentsTest.java) for an example of adding a custom runtime argument.

## Overriding runtime arguments

As mentioned above, we have access to directly override the Platform runtime argument using `RuntimeConfigManager`'s `overridePlatform` method. 

However, in order to override any other runtime arguments, we must first retrieve the `RuntimeConfigValue` that has been set.
Because the runtime argument values can be of any data type, we will need to retrieve and cast it using `RuntimeConfigManager.getRuntimeArgument`.

```java
// Override the IosMocksLocation existing runtime argument value
String modifiedValue = "modifiedValue";
RuntimeConfigValue<String> iosMocksLocationConfigValue =
        (RuntimeConfigValue<String>) RuntimeConfigManager.getInstance().getRuntimeArgument(IosMocksLocationArgument.KEY);
String newRuntimeArgumentvalue = iosMocksLocationConfigValue.override(modifiedValue);
```

See the [associated test class](RuntimeArgumentsTest.java) for a runnable example that demonstrates this functionality both for an existing runtime argument and a newly added custom runtime argument.
