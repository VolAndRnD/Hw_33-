package com.VolAndRnD.github;

import com.VolAnd.githab.Bookingdates;
import com.VolAnd.githab.CreateIdRequest;
import com.VolAnd.githab.CreateTokenRequest;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
@Severity(SeverityLevel.BLOCKER)
@Story("delete a booking")
@Feature("tests for booking deletion")
public class DeleteTest extends BaseTest{
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
                .post( "/auth")
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
    @Step("deleting previously deleted banking using a password and login for authorization")
    @Test
    void deleteAuthorizationPasswordDropIDTest(){
        given()
                .log()
                .all()
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .when()
                .delete( "/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(405);

    }
    @Step("deleting a booking using a token")
    @Test
    void deleteAuthorizationTokenTest(){
        given()
                .log()
                .all()
                .header("Cookie","token=" + token )
                .when()
                .delete("/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(201);

    }
    @Step("deleting a booking without authorization")
    @Test
    void deleteNonAuthorizationTest(){
        given()
                .log()
                .all()
                .when()
                .delete("/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(403);

    }
}