[_Return to Tutorials Overview_](../README.md)

# Mock Generation

### Topics Covered

1. [Generating mock files](#generating-mock-files)
2. [Adding a custom logger to modify mock output](#adding-a-custom-logger-to-modify-output)

### References
- [NST Runtime Arguments](../../NST/README.md#runtime-arguments)
- [HarLogger](../../NST/src/main/java/com/ebay/service/logger/platforms/HarLogger.java)
- [FormatWriter](../../NST/src/main/java/com/ebay/service/logger/FormatWriter.java)
- [FormatWriterUtil](../../NST/src/main/java/com/ebay/service/logger/FormatWriterUtil.java)

## Generating mock files
Now that we can create and send a request and ensure that the response data is valid for both the contract and any thin model checks we have added, we can generate the mock data for use with our UI tests.

In order to generate the mock files, we need to set the following runtime arguments:

- `whatToWrite` - Should be set to include `MOCKS`.
- `nstplatform` - Should be set to whatever platform you are trying to generate mocks for (e.g. IOS/ANDROID).
- `androidMocksLocation` / `iosMocksLocation` - Should be set to the output directory for the mock response files (HAR files).

By default, NST will generate HAR mock response files (using the `HarLogger`). These HAR files will include both the request and response data for each service call made during the course of the test.

After setting the runtime arguments above, simply running the NST test method will generate the mock response files, provided the test is successful.

To see an example of the mock generation capability of NST, run the test in [this test class](src/test/java/com/ebay/nst/tutorials/rest/mockgeneration). Running the test will generate the example HAR mock response file in that same [directory](src/test/java/com/ebay/nst/tutorials/rest/mockgeneration).

## Adding a custom logger to modify output

NST also supports customization of the output that is generated. You may want to add your own custom implementation of `FormatWriter` to modify the output of the mock response files, if for example the `HarLogger` does not fulfill your requirements.

To create our own custom logger, we will need to create a class which implements *`FormatWriter`*. `FormatWriter` gives us access to the list of service calls made by NST, their payloads, along with the test class and test method name that was triggered:

- `getPlatformAssociation` - This should return whichever platform you are creating a custom logger for (e.g. `IOS, ANDROID, MWEB, SITE`).<br><br>
- `writeMocks` - This would be where you utilize the supplied list of service calls made by NST, along with the `testClassName` and `testMethodName` (if desired) to generate the mocks in the format of your choosing.<br><br>
- `updateTests` - This would be where we can again utilize the list of service calls, along with the `testClassName` and `testMethodName` - however, this method is utilized when generating UI test code output. This is covered in detail section 7, ["UI Test Code Generation"](../UITestCodeGeneration/README.md)). If no modification to UI test files is needed, we do not need to add any meaningful implementation for this `updateTests` method. For our example, we will skip adding any implementation here, since we only care about the `writeMocks` method.<br><br>

You can see an example of our custom logger [here](src/test/java/com/ebay/nst/tutorials/rest/mockgeneration/MockGenerationCustomFormatWriter.java) that would modify the mock generation step of NST to generate JSON files instead of HAR files from the HarLogger. Please also note the usage of [`FormatWriterUtil`](../../NST/src/main/java/com/ebay/service/logger/FormatWriterUtil.java), which has various methods that assist in this step. 

Once the custom logger has been created, we need to point the `customLoggersPackage` runtime argument to the package that contains our custom loggers.

Example: `-DcustomLoggersPackage=com.nst.tutorials.rest.runtimearguments`

Now, when our test is run with the `MOCKS` argument set for `whatToWrite`, it will first check the `nstplatform` for what platform the test is being run for, then will check the `customLoggersPackage` defined for any classes implementing `FormatWriter` that have the associated platform set in their `getPlatformAssociation` method.
NST will then check the current platform runtime argument and match it against any custom loggers in the defined package that may have been set, and utilize those instead of the default `HarLogger`.

**To summarize:**

- Create a custom logger by creating a class that implements *`FormatWriter`*
- Ensure platform is set in the logger for whichever platform you are trying to customize output for
- Modify custom logger with any desired specifications / customizations
- Set `customLoggersPackage` runtime argument to the directory / package that contains all of your custom loggers