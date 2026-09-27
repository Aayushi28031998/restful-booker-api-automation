package tests;

import base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Restful Booker API")
@Feature("Negative Testing")
public class NegativeBookingTest extends BaseTest {
	
	@Severity(SeverityLevel.CRITICAL)
	@Description("Verifies that negative total price is rejected by the booking API.")

    @Test
    public void verifyNegativePrice() {

        String requestBody = """
                {
                    "firstname": "Test",
                    "lastname": "User",
                    "totalprice": -500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-01-01",
                        "checkout": "2026-01-05"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        given()
            .spec(requestSpec)
            .body(requestBody)

        .when()
            .post("/booking")

            .then()
            .statusCode(400);
    }
	@Severity(SeverityLevel.CRITICAL)
	@Description("Verifies that checkout date earlier than checkin date is rejected.")
    @Test
    public void verifyInvalidBookingDates() {
    	

        String requestBody = """
                {
                    "firstname": "Test",
                    "lastname": "User",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-01-10",
                        "checkout": "2026-01-05"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        given()
            .spec(requestSpec)
            .body(requestBody)

        .when()
            .post("/booking")

            .then()
            .statusCode(400);
    }
    
	@Severity(SeverityLevel.CRITICAL)
	@Description("Verifies that an update request without valid authentication is rejected.")
    
    @Test
    public void updateBookingWithoutAuthentication() {

        // Create a booking first
        String requestBody = """
                {
                    "firstname": "Auth",
                    "lastname": "Test",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-03-01",
                        "checkout": "2026-03-05"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        int bookingId =
                given()
                    .spec(requestSpec)
                    .body(requestBody)

                .when()
                    .post("/booking")

                .then()
                    .statusCode(200)
                    .extract()
                    .path("bookingid");

        System.out.println("Booking created for auth test: " + bookingId);


       
        String updateBody = """
                {
                    "firstname": "Unauthorized",
                    "lastname": "Update",
                    "totalprice": 999,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-03-01",
                        "checkout": "2026-03-05"
                    },
                    "additionalneeds": "Lunch"
                }
                """;

        given()
            .spec(requestSpec)
            .body(updateBody)

        .when()
            .put("/booking/" + bookingId)

        .then()
            .statusCode(403);
    }
    
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that an invalid data type for totalprice is rejected.")
    @Test
    public void verifyWrongPriceDataType() {

        String requestBody = """
                {
                    "firstname": "Test",
                    "lastname": "User",
                    "totalprice": "five hundred",
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-01-01",
                        "checkout": "2026-01-05"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        given()
            .spec(requestSpec)
            .body(requestBody)

        .when()
            .post("/booking")

            .then()
            .statusCode(400);
    }
    
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that a booking request without the required firstname field is rejected.")
    @Test
    public void verifyMissingRequiredField() {

        String requestBody = """
                {
                    "lastname": "User",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-04-01",
                        "checkout": "2026-04-05"
                    }
                }
                """;

        given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/booking")
            .then()
            .log().all()
            .statusCode(400);
    }
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that requesting a booking ID that does not exist returns the appropriate response.")
    @Test
    public void verifyNonExistentBookingId() {

        given()
            .spec(requestSpec)
        .when()
            .get("/booking/999999")
        .then()
            .statusCode(404);
    }
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that an empty booking payload is rejected.")
    @Test
    public void verifyEmptyPayload() {

        given()
            .spec(requestSpec)
            .body("{}")
        .when()
            .post("/booking")
            .then()
            .log().all()
            .statusCode(400);
    }
}