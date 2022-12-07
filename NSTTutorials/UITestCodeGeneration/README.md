[_Return to Tutorials Overview_](../README.md)

# UI Test Code Generation

### Overview

NST can utilize the test methods and request wrappers you have set up in order to export boilerplate code to your UI test files. The assumption behind this functionality is that certain API requests map to specific functionality in a user facing application. For example, you may have a button that when tapped, triggers a list of addresses to load. In this case, we would map the NST service wrapper that returns the response that contains the addresses to tapping the button in the UI.

The benefit provided by this functionality is the ability to run your NST tests and have large portions of your (boilerplate) UI test code generated for you, accelerating the authoring of UI tests.

NST supports **iOS** and **Android** UI test code generation by default, with the understanding (mentioned above) that the platform (iOS / Android) test classes and methods will map 1:1 with the NST test classes and methods. These default output generators ([IosFuiTestLogger](TODO-REPLACE-ME) / [AndroidFuiTestLogger](TODO-REPLACE-ME)) are of course customizable and extendable to suite your specific requirements as well.

### Topics Covered

1. [Runtime arguments](#runtime-arguments)
2. [Output Mapping CSV](#output-mapping-csv)
3. [Utilizing the iOS & Android loggers](#utilizing-the-ios--android-loggers)
4. [Customizing UI test code output](#customizing-ui-test-code-output)

### References
- [nstToFuiMappingsIos.csv](src/test/resources/nstToFuiMappingsIos.csv)
- [IosFuiTestLogger](TODO-REPLACE-ME) / [AndroidFuiTestLogger](TODO-REPLACE-ME)
- [Custom Loggers](../MockGeneration/README.md#adding-a-custom-logger-to-modify-output)

### Prerequisites
You should have familiarity with the "Adding a custom logger" section in the Mock Generation tutorial. If not, please first review that section [here](../MockGeneration/README.md#adding-a-custom-logger-to-modify-output).

## Runtime arguments

There are several required runtime arguments that need to be set in order to trigger UI test code generation when running your NST tests.

- `whatToWrite` - Should be set to include `TESTS` .
- `nstplatform` - Should be set to whatever platform you are trying to generate mocks for (e.g. IOS/ANDROID).
- `androidTestsLocation` / `iosTestsLocation` - Should be set to the directory containing the UI test classes that map to the NST tests.

## Output Mapping CSV

Additionally, we need to create and set up a mapping CSV file with the following columns, with the name of the file prefixed with `nstToFuiMappings`. For example, if creating a mapping file for IOS platform UI test code generation, the file should be called `nstToFuiMappingsIos.csv`. See this module's [resources](src/test/resources) for two example files for iOS and Android.

```csv
SERVICE,API,PLATFORM_REQUEST_TYPE,NAVIGATION,ENTRY,IMPORTS,MEMBER_FIELDS
```

Let’s break down each of these columns and how they can be utilized when providing UI test output.

- `SERVICE` - The `serviceName` defined in the NST service wrapper. In our example, this would be `canadaholidays`.<br><br>
- `API` - The class name of the NST service wrapper. In our example, this would be `UITestCodeGenerationWrapper`.<br><br>
- `PLATFORM_REQUEST_TYPE` - Generally, this should map to the class name of the **client application request class**  that maps to this NST service wrapper. In other words, this would be the client / UI side request class that maps to the NST request that is being made in the test. This is important because it allows one to utilize the mapping in the output or in some client side logic that might map the NST request/response data with the client / UI side request class.<br><br> 
- `NAVIGATION/ENTRY` - `NAVIGATION` and `ENTRY` are linked in how to understand their functionality. These, along with IMPORTS and MEMBER_FIELDS, are what would be added / exported to the UI test file itself.
    
    `NAVIGATION` and `ENTRY` are dependent on the sequence of service wrapper requests that are sent during the course of the NST test. 
    
    ```
    IF I send a service wrapper request
    AND the request is the first request that is made within the test method
    THEN utilize the ENTRY column data as the output to the UI test file
    ELSE utilize the NAVIGATION column data as the output to the UI test file
    ```
    
    The use case here is that when certain APIs are triggered FIRST, they would be associated with some initial action, such as loading a UI view for the first time - whereas any API request AFTER the initial call would be associated with performing some action AFTER the initial view has been loaded - hence the NAVIGATION name of the column.<br><br>
    
- `IMPORTS` - Any imports that may be required by the `NAVIGATION` and `ENTRY` columns for that row (e.g. Java package imports).<br><br>
- `MEMBER_FIELDS` - Any member fields that may be required by the `NAVIGATION` and `ENTRY` columns for that row (e.g. instantiation of shared UI test classes).

## Utilizing the iOS & Android loggers
By default, NST comes with UI test code generation classes specifically built for iOS (`.swift`) & Android (`.java`) output. As we saw above, we will need to ensure that the `nstToFuiMappings` file is set up for either iOS or Android (`nstToFuiMappingsIos`/`nstToFuiMappingsAndroid`) in our resources directory.

As long as we have the appropriate mapping CSV file set up, then the default loggers for either iOS or Android will be utilized. To summarize their implementation, they will look for the classes contained in the `androidTestsLocation` / `iosTestsLocation` (depending on platform) that match both the **class name** and the **method name** of the NST test that is being executed. Once it finds those, it will map the output using the output mapping CSV to the UI test files / classes.

To see an example of this in action, run the test method in the [UITestCodeGenerationTest](src/test/java/com/ebay/nst/tutorials/rest/uitestcodegeneration/UITestCodeGenerationTest.java) class, and observe the UI test code that is generated in the [UITestCodeGenerationTest.swift](src/test/java/com/ebay/nst/tutorials/rest/uitestcodegeneration/UITestCodeGenerationTest.swift) class.
Additionally, you can modify the mapping CSV file to see the changes reflected in the associated Swift class (after re-running the NST test).

## Customizing UI test code output
If the default loggers for iOS or Android do not meet your requirements, or you would like to implement a UI test code generator for another platform, you can customize and extend these supplied loggers, or create one yourself. Review the custom logger tutorial section [here](../MockGeneration/README.md#adding-a-custom-logger-to-modify-output) for guidance, and ensure that the `updateTests` method is implemented to your preference.
