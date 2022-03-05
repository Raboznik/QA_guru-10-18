package ru.yandex.mkruchkov.rest_assured;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class DemoWebShopTest {

    @DisplayName("Negative test for newsletter")
    @Test
    void newsletterNegativeTest() {
        given()
                .contentType("text/html; charset=utf-8")
                .body("email=test")
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Success", is(false))
                .body("Result", is("Enter valid email"));
    }

    @DisplayName("Test for newsletter")
    @Test
    void newsletterTest() {
        given().
                contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("email=test@test.ru")
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));

    }
}
