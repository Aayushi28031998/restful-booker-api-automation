package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import config.Config;

public class GetBookingNegativeTest {

    @Test
    public void getNonExistingBooking() {

        int bookingId = 99999999;

        given()
            .baseUri(Config.BASE_URL)

        .when()
            .get("/booking/" + bookingId)

        .then()
            .statusCode(404);
    }
    @Test
    public void updateBookingWithoutAuthentication() {

        String createBody = """
                {
                    "firstname": "Auth",
                    "lastname": "Test",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-10-01",
                        "checkout": "2026-10-05"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        int bookingId =
            given()
                .baseUri(Config.BASE_URL)
                .contentType("application/json")
                .body(createBody)

            .when()
                .post("/booking")

            .then()
                .statusCode(200)
                .extract()
                .path("bookingid");


        String updateBody = """
                {
                    "firstname": "ShouldNotUpdate",
                    "lastname": "Test",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-10-01",
                        "checkout": "2026-10-05"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        given()
            .baseUri(Config.BASE_URL)
            .contentType("application/json")
            .body(updateBody)

        .when()
            .put("/booking/" + bookingId)

        .then()
            .log().all();
    }
}