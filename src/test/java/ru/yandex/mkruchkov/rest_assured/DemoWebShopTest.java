package ru.yandex.mkruchkov.rest_assured;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class DemoWebShopTest {

    Faker faker = new Faker();

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
                .body("email=" + faker.internet().emailAddress())
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));
    }

    @DisplayName("Test for adding product to wishlist")
    @Test
    void addProductToWishlistTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH=C36C70AB2001AB8E7287F00E46DEE2C2FB8BCC76A4030C044DF8791B17602AB2E174EA56613DC03037366B2E197312C0A3919987D291BEECCDBFDED44B02F0327DBB5D1803759EDD090CD380990E832730EA5A8A1E9835CC186CFF5CF06983DFCB0DF0E007E449DDAD8695EA76D8F8962E6516CCBF352C89444725A498338B4C;")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/65/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is(notNullValue()));
    }
}
