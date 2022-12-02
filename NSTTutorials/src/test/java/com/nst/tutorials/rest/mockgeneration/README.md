[_Return to Tutorials Overview_](https://github.com/eBay/NSTSuite/tree/main/NSTTutorials)

# Mock Generation

Now that we can create and send a request and ensure that the response data is valid for both the contract and any thin model checks we have added, we can generate the mock data for use with our UI tests.

In order to generate the mock response data, we need to set the following runtime arguments:

- `whatToWrite` - Should be set to include `MOCKS`.
- `nstplatform` - Should be set to whatever platform you are trying to generate mocks for (e.g. IOS/ANDROID).
- `androidMocksLocation` / `iosMocksLocation` - Should be set to the output directory for the mock response files (HAR / JSON files).

By default, NST will generate HAR mock response files (using the `HarLogger`). These HAR files will include both the request and response data for each service call made during the course of the test.

After setting the runtime arguments above, simply running the NST test method will generate the mock response files, provided the test is successful.

To see an example of the mock generation capability of NST, run [this test](MockGenerationTest.java).