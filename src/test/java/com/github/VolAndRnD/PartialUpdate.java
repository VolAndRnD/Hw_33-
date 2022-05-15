package com.github.VolAndRnD;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PartialUpdate {
    private static CreateTokenRequest tokenRequest;
    private static CreateIdRequest idRequest;
    static String token;
    static String id;

    private static final String PROPERTIES_FILE_PATH = "src/test/resources/application.properties";
    static Properties properties =new Properties();



    @BeforeAll
    static void beforAll() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        RestAssured.baseURI = properties.getProperty("base.url");

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
                .post("/auth")
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
                .post("/booking")
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
                .patch("/booking/"+ id)
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
                .patch("/booking/"+ id)
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
                .patch("/booking/"+ id)
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
                .patch("/booking/"+ id)
                .prettyPeek();

                }
    @Test
    void partialUpdateAuthorizationTokenAndChangeOneValueTest() {
        Response response = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(
                        CreateIdRequest.builder()
                                .firstname("Vladislave")
                                .lastname("Brown")
                                .totalprice(1111)
                                .depositpaid(true)
                                .bookingdates(new Bookingdates("2021-01-01", "2022-01-01"))
                                .additionalneeds("Breakfast")
                                .build())
                .when()
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(),equalTo(200));
        assertThat(response.body().jsonPath().get("firstname"), containsStringIgnoringCase("Vladislave"));
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
                .patch("/booking/"+ id)
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
                .patch("/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));

    }


}
