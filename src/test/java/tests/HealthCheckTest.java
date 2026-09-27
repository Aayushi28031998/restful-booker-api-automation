package tests;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;


import config.Config;
@Epic("Restful Booker API")
@Feature("Health Check")
public class HealthCheckTest {
	@Severity(SeverityLevel.CRITICAL)
	@Description("Verifies that the Restful Booker API health endpoint is available.")
	

    @Test
    public void verifyHealthCheck() {

        given()

        .when()
            .get(Config.BASE_URL + "/ping")

        .then()
            .statusCode(201)
            .body(equalTo("Created"));
    }
}