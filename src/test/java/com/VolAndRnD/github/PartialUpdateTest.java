package com.VolAndRnD.github;

import com.VolAnd.githab.Bookingdates;
import com.VolAnd.githab.CreateIdRequest;
import com.VolAnd.githab.CreateTokenRequest;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@Severity(SeverityLevel.BLOCKER)
@Story("partial change of booking")
@Feature("tests for booking сhanges")

public class PartialUpdateTest extends BaseTest {
    private static CreateTokenRequest tokenRequest;
    private static CreateIdRequest idRequest;
    static String token;
    static String id;


    @BeforeAll
    static void beforSuite(){

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
    @Step("making changes using authorization via password and login")
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
                .patch( "/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(200);

    }
    @Step("making changes using token authorization")
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
                .patch("/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(200);

    }
    @Step("making changes without authorization")
    @Test
    void partialUpdateNonAuthorizationTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(idRequest)
                .when()
                .patch("/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(403);}

    @Step("making changes using an incorrect date form")
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
                .patch( "/booking/"+id)
                .prettyPeek();

    }
    @Step("making changes using a negative value in depositpaid")
    @Test
    void partialUpdateAuthorizationTokenDepositPaidyNegativeValueTest() {
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
                .patch("/booking/"+id)
                .prettyPeek();
    }
    @Step("making changes using valid data in the name surname and price")
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
                .patch("/booking/"+id)
                .prettyPeek();
        assertThat(response.statusCode(),equalTo(200));
        assertThat(response.body().jsonPath().get("firstname"), containsStringIgnoringCase("Ag ent"));
        assertThat(response.body().jsonPath().get("lastname"), containsStringIgnoringCase("007"));
        assertThat(response.body().jsonPath().get("totalprice"), equalTo(10000000));
    }
    @Step("making changes to a booking with a non-existent id")
    @Test
    void partialUpdateNonexistingIdTest() {
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
                                .depositpaid(true)
                                .bookingdates(new Bookingdates("2021-01-01", "2022-01-01"))
                                .additionalneeds("1111")
                                .build())
                .when()
                .patch("/booking/"+5555)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(405));

    }


}