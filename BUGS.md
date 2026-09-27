API Defects – Restful Booker
BUG-001: Negative booking price is accepted
Severity: High
Endpoint: POST /booking
Description
The API accepts a negative value for totalprice and creates the booking successfully.
Reproduction
curl -X POST "https://restful-booker.herokuapp.com/booking" \
-H "Content-Type: application/json" \
-d '{
  "firstname": "Test",
  "lastname": "User",
  "totalprice": -500,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-01-01",
    "checkout": "2026-01-05"
  },
  "additionalneeds": "Breakfast"
}'
Expected Result
The API should reject a negative booking price with a 4xx validation response, ideally:
400 Bad Request
Actual Result
The API returns:
200 OK
and creates the booking with:
"totalprice": -500
Impact
Invalid financial data can be stored in the booking system.
________________________________________
BUG-002: Checkout date before check-in date is accepted
Severity: High
Endpoint: POST /booking
Description
The API accepts a booking where the checkout date occurs before the check-in date.
Reproduction
curl -X POST "https://restful-booker.herokuapp.com/booking" \
-H "Content-Type: application/json" \
-d '{
  "firstname": "Date",
  "lastname": "Test",
  "totalprice": 500,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-01-10",
    "checkout": "2026-01-05"
  },
  "additionalneeds": "Breakfast"
}'
Expected Result
The API should reject the request because:
checkout < checkin
Expected response:
400 Bad Request
Actual Result
The API returns:
200 OK
and creates the booking with the invalid date range.
Impact
The system can store logically invalid booking periods.
________________________________________
BUG-003: Invalid data type for totalprice is accepted
Severity: High
Endpoint: POST /booking
Description
The API accepts a string value for totalprice, even though the field is expected to contain a numeric value.
Reproduction
curl -X POST "https://restful-booker.herokuapp.com/booking" \
-H "Content-Type: application/json" \
-d '{
  "firstname": "Type",
  "lastname": "Test",
  "totalprice": "five hundred",
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-01-01",
    "checkout": "2026-01-05"
  },
  "additionalneeds": "Breakfast"
}'
Expected Result
The API should reject the invalid data type with a 4xx validation response, ideally:
400 Bad Request
Actual Result
The API returns:
200 OK
and accepts the invalid value, with the response representing totalprice as null.
Impact
Invalid data can enter the system and may lead to incorrect downstream processing.
________________________________________
BUG-004: Missing required field causes HTTP 500
Severity: High
Endpoint: POST /booking
Description
When a required field such as firstname is omitted, the API returns an internal server error instead of handling the invalid request through validation.
Reproduction
curl -X POST "https://restful-booker.herokuapp.com/booking" \
-H "Content-Type: application/json" \
-d '{
  "lastname": "User",
  "totalprice": 500,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-04-01",
    "checkout": "2026-04-05"
  }
}'
Expected Result
The API should identify the missing required field and return a 4xx validation response, ideally:
400 Bad Request
Actual Result
The API returns:
500 Internal Server Error
with response body:
Internal Server Error
The observed response was HTTP 500 with Content-Type: text/plain.
Impact
Invalid client input causes a server-side error instead of being handled gracefully by request validation.
________________________________________
BUG-005: Empty booking payload causes HTTP 500
Severity: High
Endpoint: POST /booking
Description
The API returns an internal server error when an empty JSON object is submitted.
Reproduction
curl -X POST "https://restful-booker.herokuapp.com/booking" \
-H "Content-Type: application/json" \
-d '{}'
Expected Result
The API should reject the empty payload with a 4xx validation response, ideally:
400 Bad Request
Actual Result
The API returns:
500 Internal Server Error
with response body:
Internal Server Error
The observed response was HTTP 500.
Impact
The API does not gracefully handle an invalid empty request and exposes an internal server failure instead.
________________________________________
Notes
These defects were identified through automated negative and boundary tests using REST Assured.
The following negative scenarios were also tested and behaved as expected:
•	Updating a booking without authentication → 403 Forbidden
•	Requesting a non-existent booking ID → 404 Not Found
The automated test run confirmed these two scenarios passed.

