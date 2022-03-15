package ru.yandex.mkruchkov.rest_assured;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoWebShopTest {

    static String cookie;
    static String baseURL = "http://demowebshop.tricentis.com";
    static String email = "qwe@qwe.ru";
    static String password = "111111";
    static boolean rememberMe;
    static String body = String.format("Email=%s&Password=%s&RememberMe=%s", email, password, rememberMe);
    String minimalContent = baseURL + "/Themes/DefaultClean/Content/images/logo.png";
    Faker faker = new Faker();


    @BeforeAll
    public static void getCookie() {

        cookie = given()
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .post(baseURL + "/login")
                .then()
                .statusCode(302)
                .extract().cookie("NOPCOMMERCE.AUTH");
    }


    @AfterEach
    void afterEach() {
        closeWebDriver();
    }


    @Order(0)
    @DisplayName("Login test API + UI")
    @Test
    void loginTest() {
        open(minimalContent);
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookie));
        open(baseURL + "/customer/info");

        $(".account").shouldHave(text(email));
    }


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
                .post(baseURL + "/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));
    }

    @Order(1)
    @DisplayName("Test for adding product to wishlist")
    @Test
    void addProductToWishlistTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", cookie)
                .post(baseURL + "/addproducttocart/details/65/2")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is(notNullValue()));
    }

    // Да, не хорошо делать этот тест зависимым от addProductToWishlistTest(), но хотелось  попробовать @Order.
    @Order(2)
    @DisplayName("Test for deleting product from wishlist API + UI")
    @Test
    void deleteProductFromWishListTest() {

        open(minimalContent);
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookie));
        open(baseURL + "/wishlist");

        $(".cart-item-row").shouldBe(visible);
        $(".cart-item-row").$(By.name("removefromcart")).click();
        $(".update-wishlist-button").click();
        $(".center-1").shouldHave(text("The wishlist is empty!"));
        $(".cart-item-row").shouldNotBe(visible);
    }
}

