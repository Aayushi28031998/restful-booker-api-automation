package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;



@Epic("Restful Booker API")
@Feature("Contract Testing")
public class SchemaValidationTest extends BaseTest {
	@Severity(SeverityLevel.CRITICAL)
	@Description("Validates the booking API response against the expected JSON schema.")
    @Test
    public void validateBookingResponseSchema() {

        String requestBody = """
                {
                    "firstname": "Schema",
                    "lastname": "Test",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-04-01",
                        "checkout": "2026-04-05"
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
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath(
                    "schemas/booking-response-schema.json"
            ));
    }
}
