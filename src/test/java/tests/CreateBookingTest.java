package tests;

import base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Restful Booker API")
@Feature("Booking Creation")

public class CreateBookingTest extends BaseTest {
	
	@Severity(SeverityLevel.CRITICAL)
	@Description("Creates a new booking and validates the booking response.")
	

    @Test
    public void createBooking() {

        String requestBody = """
                {
                    "firstname": "Jim",
                    "lastname": "Brown",
                    "totalprice": 111,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2024-01-01",
                        "checkout": "2024-01-05"
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
            .body("booking.firstname", equalTo("Jim"))
            .body("booking.lastname", equalTo("Brown"))
            .body("booking.totalprice", equalTo(111))
            .body("booking.depositpaid", equalTo(true));
    }
}