package tests;

import base.BaseTest;
import utils.AuthUtil;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Restful Booker API")
@Feature("Booking CRUD")

public class CrudBookingTest extends BaseTest {
	@Severity(SeverityLevel.CRITICAL)
	@Description("Validates complete booking lifecycle: Create, Read, Update, Partial Update and Delete.")
	

    @Test
    public void verifyBookingCRUD() {

        // -------------------------------
        // 1. CREATE BOOKING - POST
        // -------------------------------

        String requestBody = """
                {
                    "firstname": "Aayushi",
                    "lastname": "Test",
                    "totalprice": 500,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-01-01",
                        "checkout": "2026-01-05"
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
                    .body("booking.firstname", equalTo("Aayushi"))
                    .body("booking.lastname", equalTo("Test"))
                    .body("booking.totalprice", equalTo(500))
                    .body("booking.depositpaid", equalTo(true))
                    .extract()
                    .path("bookingid");


        System.out.println("Created Booking ID: " + bookingId);


        // -------------------------------
        // 2. READ BOOKING - GET
        // -------------------------------

        given()
            .spec(requestSpec)

        .when()
            .get("/booking/" + bookingId)

        .then()
            .statusCode(200)
            .body("firstname", equalTo("Aayushi"))
            .body("lastname", equalTo("Test"))
            .body("totalprice", equalTo(500));


        // -------------------------------
        // 3. AUTHENTICATION
        // -------------------------------

        String token = AuthUtil.getToken();

        System.out.println("Token generated successfully");


        // -------------------------------
        // 4. UPDATE BOOKING - PUT
        // -------------------------------

        String putBody = """
                {
                    "firstname": "Aayushi Updated",
                    "lastname": "Test Updated",
                    "totalprice": 700,
                    "depositpaid": false,
                    "bookingdates": {
                        "checkin": "2026-02-01",
                        "checkout": "2026-02-05"
                    },
                    "additionalneeds": "Lunch"
                }
                """;

        given()
            .spec(requestSpec)
            .cookie("token", token)
            .body(putBody)

        .when()
            .put("/booking/" + bookingId)

        .then()
            .statusCode(200)
            .body("firstname", equalTo("Aayushi Updated"))
            .body("lastname", equalTo("Test Updated"))
            .body("totalprice", equalTo(700))
            .body("depositpaid", equalTo(false));


        // -------------------------------
        // 5. PARTIAL UPDATE - PATCH
        // -------------------------------

        String patchBody = """
                {
                    "firstname": "Aayushi Patched"
                }
                """;

        given()
            .spec(requestSpec)
            .cookie("token", token)
            .body(patchBody)

        .when()
            .patch("/booking/" + bookingId)

        .then()
            .statusCode(200)
            .body("firstname", equalTo("Aayushi Patched"));


        // -------------------------------
        // 6. DELETE BOOKING
        // -------------------------------

        given()
            .spec(requestSpec)
            .cookie("token", token)

        .when()
            .delete("/booking/" + bookingId)

        .then()
            .statusCode(201);


        // -------------------------------
        // 7. VERIFY DELETION
        // -------------------------------

        given()
            .spec(requestSpec)

        .when()
            .get("/booking/" + bookingId)

        .then()
            .statusCode(404);

        System.out.println("CRUD test completed successfully.");
    }
}