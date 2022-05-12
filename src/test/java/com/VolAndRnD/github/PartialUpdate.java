package com.VolAndRnD.github;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PartialUpdate {
    private static CreateTokenRequest tokenRequest;
    private static CreateIdRequest idRequest;
    static String token;
    static String id;


    @BeforeAll
    static void beforAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        tokenRequest = CreateTokenRequest.builder()
                .username("admin")
                .password("password123")
                .build();
        idRequest = CreateIdRequest.builder()
                .firstname("Jim")
                .lastname("Brown")
                .totalprice(150)
                .depositpaid(true)
                .bookingdates(new Bookingdates("2021-01-01", "2022-01-01"))
                .additionalneeds("ice-cream")
                .build();

        token =  given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(tokenRequest)
                .expect()
                .statusCode(200)
                .body("token",is(CoreMatchers.not(nullValue())))
                .when()
                .post("https://restful-booker.herokuapp.com/auth")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("token")
                .toString();

        id = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(idRequest)
                .expect()
                .statusCode(200)
                .when()
                .post("https://restful-booker.herokuapp.com/booking")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("bookingid")
                .toString();

    }
    @Test
    void partialUpdateAuthorizationPasswordTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .body(idRequest)
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .statusCode(200);

    }
    @Test
    void partialUpdateAuthorizationTokenTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie","token=" + token )
                .body(idRequest)
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @Test
    void partialUpdateNonAuthorizationTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(idRequest)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .statusCode(403);}


        @Test
    void partialUpdateAuthorizationTokenInvalueBookingdatesTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie","token=" + token )
                .body(CreateIdRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(1111)
                        .depositpaid(true)
                        .bookingdates(new Bookingdates("01-01-2021", "01-01-2022"))
                        .additionalneeds("Breakfast")
                        .build())
                .expect()
                .body("checkin", equalTo("01-01-2021"))
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek();

                }
    @Test
    void partialUpdateAuthorizationTokenInvalueDepositPaidTest() {
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(
                        CreateIdRequest.builder()
                                .firstname("Jim")
                                .lastname("Brown")
                                .totalprice(1111)
                                .depositpaid(false)
                                .bookingdates(new Bookingdates("2021-01-01", "2022-01-01"))
                                .additionalneeds("Breakfast")
                                .build())
                .expect()
                .body("depositpaid", equalTo(false))
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek();
    }
    @Test
    void partialUpdateAuthorizationPasswordAndFirstNameAndLastNameAndTotalPrisePozitivTest(){
        Response response = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .body(
                        CreateIdRequest.builder()
                                .firstname("Ag ent")
                                .lastname("007")
                                .totalprice(10000000)
                                .depositpaid(true)
                                .bookingdates(new Bookingdates("2021-01-01", "2022-01-01"))
                                .additionalneeds("Breakfast")
                                .build())
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek();
                assertThat(response.statusCode(),equalTo(200));
                assertThat(response.body().jsonPath().get("firstname"), containsStringIgnoringCase("Ag ent"));
                assertThat(response.body().jsonPath().get("lastname"), containsStringIgnoringCase("007"));
                assertThat(response.body().jsonPath().get("totalprice"), equalTo(10000000));
    }
    @Test
    void partialUpdateAuthorizationTokenAndDepositPaidTest() {
        Response response = given()
                .log()
                .all()
                .header(
                        "Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(
                        CreateIdRequest.builder()
                                .firstname("Jim")
                                .lastname("Brown")
                                .totalprice(1111)
                                .depositpaid(false)
                                .bookingdates(new Bookingdates("2021-01-01", "2022-01-01"))
                                .additionalneeds("Breakfast")
                                .build())
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));

    }


}
