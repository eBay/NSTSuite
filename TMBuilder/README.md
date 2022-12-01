# TMBuilder
Thin Model Builder (TMBuilder) is a UI tool for designing and generating NST thin models. It consumes OpenAPI yaml, JSON schema or GraphQL schema and allows you to visually specify the thin model validations you would like to perform. These validations can then be exported to your java classes for use in your test automation.  

## Executable

Executable requires Java 8 or later. Download is available under [releases](https://github.com/eBay/NSTSuite/releases).

## Build & Run Locally

**Maven Build Command**:
`mvn clean install exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=com.ebay.tool.thinmodelgen.TMBuilder`  

**Launch Command - If Already Built**:
`mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=com.ebay.tool.thinmodelgen.TMBuilder`  

**Launch Command - with verbose output flag set**:  
`mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=com.ebay.tool.thinmodelgen.TMBuilder -Dexec.args="-v"`

## Release Notes

**Release version**  
<details>
  <summary>Click to expand!</summary>
  
| Version  | Notes  |
|---|---|
| 2.0.2 | GraphQL schema processing bug fixes - multiple schema file support and reference type name presentation. If a type definition is missing from the schemas a pop-up message is displayed to the user. |
| 2.0.1 | Correct GraphQL parsing bug that dropped the operation name from the schema tree. |
| 2.0.0 | Adding GraphQL Support. TMB internal file pathing is no longer relative to the TMBuilder executable. Pathing to the schema and export file location is now relative to the TMB file. To update you existing files, place the TMBuilder executable in the same location as your existing TMBuilder executable, open each of your TMB files and then save them. After saving each of your TMB files with the 2.0.0 TMBuilder you are free to move your TMBuilder executable anywhere you like on your file system. |
| 1.0.8 | Fix for array parsing of primitive items. |
| 1.0.7 | Fix parsing error related to JSON schema with arrays without defined items. |
| 1.0.6 | Patch for JPJsonObjectCheck output not wrapping output in quotes. |
| 1.0.5 | Support for oneOf/anyOf response model schemas. |
| 1.0.4 | JPObjectCheck support code generation for defined checks and added negative contains check for object fields. |
| 1.0.3 | Fix import statement generation to choose the wrapped JP*Check. |
| 1.0.2 | Fix duplicate field entry issue. |
| 1.0.1 | Fix for issue reported when parsing strings that are DateTime instances. Significant improvement to OpenAPI parsing time. Recent files will only track .tmb files. |
| 1.0.0 | Support for OpenAPI schema |
</details>

## Running on Mac OS  
If you are running Mac OS, file access permission will be an issue. Executable jars (jars that can be run by double clicking on the jar) are launched by JavaLauncher. It is the JavaLauncher that you need to give file access permissions to.

Open your System Properties and then open Security & Privacy. Select the Privacy tab. Unlock to make changes, then select Full Disk Access. Press the + button to add an application and navigate to `/System/Library/CoreServices` and select JavaLauncher and press open to close the file chooser dialog. Make sure JavaLauncher is shown in the list of approved applications and it has a check by it. Lock the settings and double click on the executable jar. You now have access to all of your files and folders using the file chooser in the app!

If you are running the jar from the command line using 'java -jar' then you would do the same series of steps but add the java executable for each of your installed jdk/jre versions. The path would be `/Library/Java/JavaVirtualMachines` then the java version and then navigate to the bin folder and select java to add it to the list.
