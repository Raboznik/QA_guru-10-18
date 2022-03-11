package ru.yandex.mkruchkov.rest_assured;

import com.codeborne.selenide.Browser;
import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class DemoWebShopTest {



    @AfterEach
    void afterEach() {
        closeWebDriver();
    }

    Faker faker = new Faker();
    String cookie;

    public  String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @DisplayName("Login test")
    @Test
    void loginTest() {
      setCookie(
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .body("Email=qwe@qwe.ru&Password=111111&RememberMe=false")
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .statusCode(302)
                        .extract().cookie("NOPCOMMERCE.AUTH"));

        System.out.println(getCookie());


        Selenide.open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", getCookie()));
        Selenide.open("http://demowebshop.tricentis.com/customer/info");

        $(".account").shouldHave(text("qwe@qwe.ru"));
        sleep(2000);
        System.out.println(getCookie());

    }
    //693046733DECFEA94E9B765126F6A9972DC54E
    //D576ACBA1A
    //262BD8007DB52

//    @DisplayName("Negative test for newsletter")
//    @Test
//    void newsletterNegativeTest() {
//
//        given()
//                .contentType("text/html; charset=utf-8")
//                .body("email=test")
//                .when()
//                .post("http://demowebshop.tricentis.com/subscribenewsletter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("Success", is(false))
//                .body("Result", is("Enter valid email"));
//    }
//
//    @DisplayName("Test for newsletter")
//    @Test
//    void newsletterTest() {
//
//        given().
//                contentType("application/x-www-form-urlencoded; charset=UTF-8")
//                .body("email=" + faker.internet().emailAddress())
//                .when()
//                .post("http://demowebshop.tricentis.com/subscribenewsletter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("Success", is(true))
//                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));
//    }
    //7C18F48E0B7792A8EB370EE90961025D4CBD1B661AA4DEEEC0105F7EE9E16371A46B1E55CB35FF44056616588AF21185FB71E6B3A00E2DEBD03814191EEDDBEEC226973A39E0C59AFEE5D2667175E1728F0530BA3C5DBD13DCB165D5304C9B897D815A4F6CCB28F5FA2E87E2736D3BE3BC91D4212FC83C067E16E51C6A892DFA
    @DisplayName("Test for adding product to wishlist")
    @Test
    void addProductToWishlistTest() {


             setCookie(   given().contentType("application/x-www-form-urlencoded")
                        .body("Email=qwe@qwe.ru&Password=111111&RememberMe=false")
                        .post("http://demowebshop.tricentis.com/login")
                        .then().extract().cookie("NOPCOMMERCE.AUTH"));



        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(getCookie())
//             .log().cookies()
//                .cookie("NOPCOMMERCE.AUTH=C36C70AB2001AB8E7287F00E46DEE2C2FB8BCC76A4030C044DF8791B17602AB2E174EA56613DC03037366B2E197312C0A3919987D291BEECCDBFDED44B02F0327DBB5D1803759EDD090CD380990E832730EA5A8A1E9835CC186CFF5CF06983DFCB0DF0E007E449DDAD8695EA76D8F8962E6516CCBF352C89444725A498338B4C;")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/65/2")


                .then()
//                .log().body()
//             .log().cookies()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is(notNullValue()));
//                .extract().cookie("NOPCOMMERCE.AUTH");

//        System.out.println(c2 + " 2");
    }



    @DisplayName("Test for deleting product from wishlist")
    @Test
    void deleteProductFromWishListTest() {

//        addProductToWishlistTest();
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println("before delete : \n" + cookie);

           setCookie(     given()
                      .contentType("application/x-www-form-urlencoded")
                        .body("Email=qwe@qwe.ru&Password=111111&RememberMe=false")
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .statusCode(302)
                        .extract().cookie("NOPCOMMERCE.AUTH"));



        Selenide.open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", getCookie()));
        Selenide.open("http://demowebshop.tricentis.com/wishlist");

        $(".cart-item-row").shouldBe(visible);
        $(".cart-item-row").$(By.name("removefromcart")).click();
        $(".update-wishlist-button").click();
        $(".center-1").shouldHave(text("The wishlist is empty!"));
        $(".cart-item-row").shouldNotBe(visible);
        sleep(2000);
    }
}

