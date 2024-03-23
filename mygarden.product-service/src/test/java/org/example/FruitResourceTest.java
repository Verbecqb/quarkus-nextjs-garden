package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class FruitResourceTest {

    @Test
     void testGetMethod() {

         given()
            .when().get("/fruits")
                 .then()
                 .statusCode(200);

    }
}
