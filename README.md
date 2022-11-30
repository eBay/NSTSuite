# NSTSuite
NSTSuite is a suite of tools for contract testing (REST and GraphQL), generating mocks and UI test automation and verifying existing mocks. The suite of tools consists of:

## NST
NST (pronounced \nest\) is a contract testing framework that provides all of the above stated capabilities. It is extendable but will work right out of the box.

With NST you define service wrappers and request models. Those are used with the service processor to send requests and evaluate the response. You will get the most out of NST if you have service schemas (OpenAPI for REST services and GraphQL schemas for GraphQL). NST will first compare the service response against the provided schema for violations. The second order check is to validate the data in the response. For that we recommend using the TMBuilder (described below). Paired together we get a complete picture of the correctness of the service response relative to the service schema and use case requirements of your test.

## TMBuilder
TMBuilder is a GUI tool for visually specifying and generating data checks against a response. What does the `TM` in TMBuilder stand for? It stands for `Thin Model`. Instead of having POJOs for each of the models in a schema we use JSON path and specific checks based on the type of each node in the schema to retieve data and inspect it. Because we are not parsing the response into a concrete model we came up with the name `Thin Model` to refer to the classes used to validate data in the service response.

TMBuilder consumes your service schema and displays a tree view of your schema for selecting nodes defining the checks to perform. All checks can be exported to your thin model class saving you time both in writing the checks by hand and in reviewing with team members the checks being performed. Humans are much better at reviewing the nodes in the schema tree than they are at reading JSON path definitions and check notation.

## More!
Check out the respective project READMEs for more information.

## License
NSTSuite is licensed under the Apache License 2.0. Please see license.txt for more information.
