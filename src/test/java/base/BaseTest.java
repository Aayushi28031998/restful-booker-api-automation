package base;

import config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

    protected RequestSpecification requestSpec;

    public BaseTest() {

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(Config.BASE_URL)
                .setContentType("application/json")
                .build();
    }
}