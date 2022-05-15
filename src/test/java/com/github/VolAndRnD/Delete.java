package com.github.VolAndRnD;

import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class Delete {
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
    void deleteAuthorizationPasswordDropIDTest(){
        given()
                .log()
                .all()
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .when()
                .delete("/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(405);

    }
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
