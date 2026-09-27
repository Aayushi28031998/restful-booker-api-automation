package utils;

import config.Config;

import static io.restassured.RestAssured.*;

public class AuthUtil {

    public static String getToken() {

        String requestBody = """
                {
                    "username": "admin",
                    "password": "password123"
                }
                """;

        return given()
                .baseUri(Config.BASE_URL)
                .contentType("application/json")
                .body(requestBody)

            .when()
                .post("/auth")

            .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
