API Automation
1. Project Overview

This project is an API automation framework developed for testing the Restful Booker API as part of the Leegality QA Engineer API Automation assignment.

The objective of this project is to create a maintainable REST API automation framework that validates the major API operations, authentication, positive scenarios, negative scenarios, response contracts, and known API defects.

The API under test is Restful Booker.

Base URL:

https://restful-booker.herokuapp.com

The API provides booking-related operations such as creating, retrieving, updating, partially updating, and deleting bookings. It also provides an authentication endpoint for secured operations.

2. Technology Stack

The framework is developed using Java and Maven.

The major technologies and libraries used are:

Java 17
Maven
REST Assured
TestNG
Jackson Databind
JSON Schema Validator
Eclipse IDE

REST Assured is used for sending HTTP requests and validating API responses.

TestNG is used as the test execution framework.

Maven is used for dependency management and project execution.

JSON Schema Validator is used for response contract validation.

3. API Under Test

The project tests the following Restful Booker APIs:

Health check using GET /ping
Retrieve all bookings using GET /booking
Retrieve a specific booking using GET /booking/{id}
Create a booking using POST /booking
Update a complete booking using PUT /booking/{id}
Partially update a booking using PATCH /booking/{id}
Delete a booking using DELETE /booking/{id}
Generate an authentication token using POST /auth

The API documentation is available at:

https://restful-booker.herokuapp.com/apidoc/index.html

4. Test Strategy

The automation suite follows a combination of positive testing, negative testing, authentication testing, CRUD validation, and contract validation.

The primary objective is not only to verify that valid API requests work correctly, but also to identify incorrect behavior when invalid data is provided.

The test suite validates HTTP status codes, response bodies, important response fields, authentication behavior, and response schema.

Dynamic booking IDs are used instead of hardcoded IDs because the Restful Booker environment is shared and the data can be reset periodically.

5. Health Check Testing

The /ping endpoint is used to verify that the API is available.

The test sends a GET request to the health-check endpoint and verifies that the API returns HTTP status code 201.

The response body is also validated to ensure that it contains the expected response.

This test provides a basic indication that the API is available before executing other API operations.

6. Booking Creation

The booking creation test sends a POST request to /booking with a valid booking payload.

The request contains:

First name
Last name
Total price
Deposit paid status
Check-in date
Check-out date
Additional needs

The test verifies the successful HTTP response and validates important fields returned by the API.

The booking ID returned by the API is extracted dynamically from the response.

This booking ID is then used by subsequent tests instead of relying on a hardcoded booking ID.

7. CRUD Testing

The framework contains an end-to-end CRUD flow.

First, a new booking is created using POST.

The booking ID returned from the create operation is stored.

The newly created booking is then retrieved using GET and the response is validated.

The framework generates an authentication token before performing secured operations.

The booking is then completely updated using PUT.

After that, the booking is partially updated using PATCH.

Finally, the booking is deleted using DELETE.

After deletion, the framework attempts to retrieve the same booking ID and verifies that the API returns a 404 response.

This approach validates the complete lifecycle of a booking.

8. Authentication

The /auth endpoint is used to generate an authentication token.

The authentication request contains the username and password provided by the Restful Booker API.

The generated token is extracted from the JSON response using JSONPath.

The token is then passed in the request cookie for secured operations such as PUT, PATCH, and DELETE.

Authentication logic has been separated into a reusable AuthUtil class.

This avoids duplicating authentication code in multiple test classes.

The framework also contains a negative authentication scenario where an update request is sent without valid authentication.

The API is expected to reject the unauthorized request.

9. Negative Testing

Negative testing is an important part of this automation framework.

The objective is to verify how the API behaves when invalid or unexpected data is provided.

The following scenarios are covered:

Negative Price

A booking is created with a negative total price.

The expected behavior is that the API should reject the request with a client-side validation error.

The current API accepts the negative value, which has been documented as a defect.

Invalid Booking Dates

A booking is created where the checkout date occurs before the check-in date.

The expected behavior is that the API should reject the request.

The current API accepts the invalid date range, which has been documented as a defect.

Incorrect Data Type

The total price is sent as an invalid data type instead of an integer.

The expected behavior is that the API should reject the request.

The current API accepts the request instead of returning a validation error. This behavior has been documented as a defect.

Missing Required Field

A booking request is sent without a required field such as the first name.

The expected behavior is a client-side validation response such as HTTP 400.

The API currently returns HTTP 500 Internal Server Error.

This has been documented as a defect because invalid client input should not result in an internal server error.

Empty Request Payload

An empty JSON payload is sent while creating a booking.

The expected behavior is a client-side validation response.

The API currently returns HTTP 500 Internal Server Error.

This behavior has been documented as a defect.

Non-existent Booking ID

A request is sent for a booking ID that does not exist.

The test verifies that the API returns HTTP 404.

Unauthorized Update

An update request is sent without valid authentication.

The test verifies that the API rejects the request with the appropriate authorization response.

10. Response Validation

The framework validates both HTTP status codes and response bodies.

For successful operations, the tests verify important fields such as:

Booking ID
First name
Last name
Total price
Deposit status
Booking dates
Additional needs

The tests use REST Assured assertions to validate the API response.

For example, response fields can be validated using JSONPath expressions such as:

booking.firstname

This ensures that the API is returning the expected data and not only the expected HTTP status code.

11. Schema Validation

JSON Schema validation has been implemented to validate the structure of the booking response.

The schema verifies fields such as:

Booking ID
Booking object
First name
Last name
Total price
Deposit paid
Booking dates
Check-in date
Check-out date
Additional needs

The schema is stored under:

src/test/resources/schemas/booking-response-schema.json

REST Assured's JSON Schema Validator is used to compare the API response against this schema.

This provides an additional layer of contract testing and helps detect unexpected changes in the API response structure.

12. Framework Structure

The framework separates configuration, reusable utilities, test classes, and test resources.

The configuration class contains common API configuration such as the base URL and authentication credentials.

The BaseTest class contains the reusable REST Assured request specification.

The request specification centralizes common configuration such as the base URI and content type.

The AuthUtil class contains reusable authentication logic for generating the API token.

Test classes are responsible for executing specific API scenarios.

The schema file is maintained separately under the test resources directory.

This structure makes the framework easier to maintain and extend when additional APIs or test cases are added.

13. Design Decisions
Centralized Configuration

The API base URL and authentication details are maintained in a centralized configuration class.

This prevents the same values from being hardcoded throughout the test classes.

If the environment changes, the configuration can be updated from one location.

Reusable Request Specification

A common REST Assured RequestSpecification is created in the BaseTest class.

This avoids repeating common request configuration in every test.

Reusable Authentication

Authentication has been moved into AuthUtil.

Any test requiring authentication can call the utility method instead of implementing the login request again.

Dynamic Booking IDs

Booking IDs are captured from API responses dynamically.

This is important because the API is a shared sandbox and its data can change or reset.

Using dynamically created data makes the tests more reliable than depending on fixed IDs.

Separation of Tests

Different test classes are used for different testing responsibilities such as health checks, booking creation, CRUD operations, negative testing, authentication, and schema validation.

This makes the framework easier for another QA engineer to understand and maintain.

14. Known Defects

During negative testing, multiple API validation issues were identified.

The current implementation allows negative booking prices.

The current implementation accepts a checkout date that occurs before the check-in date.

The current implementation accepts an invalid data type for total price.

The current implementation returns HTTP 500 when a required field is missing.

The current implementation returns HTTP 500 when an empty booking payload is submitted.

Detailed reproduction steps, expected behavior, actual behavior, severity, and curl commands for these defects are documented separately in BUGS.md.

15. Environment Considerations

The Restful Booker API is a shared sandbox environment.

The assignment specifies that the environment contains preloaded booking records and that the data can be reset periodically.

The environment can also take some time to wake up when it has been idle.

Because of this, the tests avoid relying on permanent booking IDs.

The CRUD flow creates a new booking and captures its ID dynamically.

This approach reduces dependency on pre-existing data and makes the tests more suitable for a shared environment.

16. Handling API Failures

A test failure is treated as useful information rather than something that should simply be hidden.

For example, when a negative-price test expects HTTP 400 but receives HTTP 200, the test fails.

The expected response represents the API contract that the test is validating.

The test should not be modified simply to match the current incorrect behavior of the API.

Instead, the failure is investigated and the corresponding defect is documented in BUGS.md.

This approach helps ensure that the automation suite acts as a safety net for API behavior.

17. How to Run the Project

The project can be imported into Eclipse as an existing Maven project.

After importing the project, Maven automatically downloads the required dependencies from pom.xml.

The tests can be executed directly from Eclipse using TestNG.

Individual test classes can be executed by selecting the class and choosing:

Run As → TestNG Test

The complete test suite can also be executed using Maven.

The Maven command is:

mvn test

The API must be accessible before executing the tests.

Because the API is a shared sandbox, occasional environment-related failures may occur if the service is temporarily unavailable or waking up.

18. Expected Test Behavior

The positive API scenarios are expected to pass when the API behaves according to the documented contract.

Some negative tests are intentionally expected to fail against the current sandbox because they expose the seeded defects in the API.

These failures are important because they demonstrate that the automation framework is capable of identifying incorrect API behavior.

The failing scenarios are documented in BUGS.md.

Therefore, a failed negative test does not automatically mean that the automation code is incorrect.

The failure can represent an actual API defect.

19. Limitations

The current framework is designed specifically for the Restful Booker assignment and the available sandbox environment.

The framework currently does not implement a full CI/CD pipeline.

Advanced performance testing has not been implemented because the primary focus of the assignment is API functional and contract testing.

The environment is shared and can reset its data, so test execution may occasionally be affected by external sandbox behavior.

The current framework can be further extended with additional data-driven testing, reporting, retry handling, and CI/CD integration.

20. Future Improvements

The framework can be extended in several ways.

Allure or another HTML reporting solution can be integrated to provide detailed execution reports.

Data-driven testing can be added for boundary and negative scenarios.

Environment-specific configuration can be introduced so that different environments can be tested without changing the source code.

Retry or controlled wait logic can be added to handle temporary API cold-start behavior.

The project can be integrated with Jenkins or GitHub Actions for continuous execution.

Additional schema validation can be added for other API responses.

More reusable POJO classes can also be introduced for request and response serialization instead of maintaining JSON payloads directly as strings.


21. Test Reporting

This project uses Allure Report for human-readable test execution reporting.

### Generate Test Results

Run the test suite using:

mvn clean test

The test results are generated in the `allure-results` directory.

### Generate Allure Report

Use:

allure serve allure-results

This opens the Allure report in the browser.

The report provides:

- Overall pass/fail summary
- Individual test results
- Test duration
- Test grouping by feature
- Failure details
- Defect categorization
- Contract/schema validation results

### Current Test Execution

The negative test cases intentionally fail where the API does not meet the expected contract. These failures are documented as defects in `BUGS.md`.

The current execution identified defects related to:

- Negative price acceptance
- Invalid booking dates
- Invalid `totalprice` data type
- Missing required fields
- Empty request payload
21. Conclusion

This project provides a maintainable REST API automation framework for the Restful Booker API using Java, REST Assured, TestNG, Maven, and JSON Schema validation.

The framework covers health checks, booking creation, retrieval, complete updates, partial updates, deletion, authentication, negative testing, and response contract validation.

The framework also identifies and documents multiple API validation defects.

The design focuses on reusable components, dynamic test data, centralized configuration, clear test separation, and maintainability.

The automation suite is intended to provide a reliable safety net for detecting regressions and incorrect API behavior.