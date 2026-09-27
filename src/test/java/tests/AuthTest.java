package tests;

import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import config.Config;

public class AuthTest {

    @Test
    public void generateToken() {

        String requestBody = """
                {
                    "username": "admin",
                    "password": "password123"
                }
                """;

        Response response =
                given()
                    .baseUri(Config.BASE_URL)
                    .contentType("application/json")
                    .body(requestBody)

                .when()
                    .post("/auth");

        response.then()
                .statusCode(200)
                .body("token", notNullValue());

        String token = response.jsonPath().getString("token");

        System.out.println("Token: " + token);
    }
}