[_Return to Tutorials Overview_](https://github.com/eBay/NSTSuite/tree/main/NSTTutorials)

# Mock Generation

### Topics Covered

1. [Generating mock files](#Generating mock files)
2. [Adding a custom logger](#Adding a custom logger)

### References
- [NST Runtime Arguments](../../../../../../../../../NST/README.md#Runtime Arguments)

## Generating mock files
Now that we can create and send a request and ensure that the response data is valid for both the contract and any thin model checks we have added, we can generate the mock data for use with our UI tests.

In order to generate the mock files, we need to set the following runtime arguments:

- `whatToWrite` - Should be set to include `MOCKS`.
- `nstplatform` - Should be set to whatever platform you are trying to generate mocks for (e.g. IOS/ANDROID).
- `androidMocksLocation` / `iosMocksLocation` - Should be set to the output directory for the mock response files (HAR files).

By default, NST will generate HAR mock response files (using the `HarLogger`). These HAR files will include both the request and response data for each service call made during the course of the test.

After setting the runtime arguments above, simply running the NST test method will generate the mock response files, provided the test is successful.

To see an example of the mock generation capability of NST, run [this test](MockGenerationTest.java). Running the test will generate the example HAR mock response file in the [current directory](./).

## Adding a custom logger

NST also supports customization of the logging / output that is generated. You may want to add your own custom implementation to modify the output of either the mock response files or the output to the UI test files as well.

**NOTE**: Custom loggers are **required** to be implemented if you want to export boilerplate code to UI test files, which is covered later on in detail in section 7.

To do this, we will need to create our own custom logger file which implements *`FormatWriter`*:

- `getPlatformAssociation` - This should return whichever platform you are creating a custom logger for (`IOS, ANDROID, MWEB, SITE`).
- `writeMocks` - This is where you utilize the supplied list of service calls, along with the `testClassName` and `testMethodName` (if desired) to generate the mocks in the format of your choosing.
- `updateTests` - This is where we can again utilize the list of service calls, along with the `testClassName` and `testMethodName` (if desired) to generate the UI test code output that is customized per your specifications.

You can see an example of a fully formed custom logger [here](CustomFormatWriter.java).

Once the custom logger has been created, we need to point the `customLoggersPackage` runtime argument to the package that contains our custom loggers.

Example: `-DcustomLoggersPackage=com.nst.tutorials.rest.runtimearguments`

Now, when our test is run with the `MOCKS` or `TESTS` argument set for `whatToWrite`, it will first check the `nstplatform` for what platform the test is being run for, then will check the `customLoggersPackage` defined for any classes implementing `FormatWriter` that have the associated platform set in their `getPlatformAssociation` method.

**To summarize:**

- Create a custom logger by creating a class that implements *`FormatWriter`*
- Ensure platform is set in the logger for whichever platform you are trying to customize output for
- Modify custom logger with any desired specifications / customizations
- Set `customLoggersPackage` runtime argument to the directory / package that contains all of your custom loggers