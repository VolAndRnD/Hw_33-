package com.VolAndRnD.github;

import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class Delete {
    static String token;
    static String id;

    @BeforeAll
    static void beforAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeAll
     static void setToken(){

       token =  given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body("{\n"
                +   " \"username\" : \"admin\",\n"
                +   " \"password\" : \"password123\"\n"
                +   "}")
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
    }

    @BeforeAll
    static void setUp(){
         id = given()
                 .log()
                 .all()
                 .header("Content-Type", "application/json")
                .body("{\n"
                  +   " \"firstname\" : \"Jim\",\n"
                  +   " \"lastname\" : \"Brown\",\n"
                  +   " \"totalprice\" : 111,\n"
                  +   " \"depositpaid\" : true,\n"
                  +   " \"bookingdates\" : {\n"
                  +   "  \"checkin\" :  \"2021-01-01\",\n"
                  +   "  \"checkout\" :  \"2022-01-01\"\n"
                  +   "   },\n"
                  +   " \"additionalneeds\" : \"Breakfast\"\n"
                  +   "}"
                )
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
    void deleteAuthorizationPasswordTest(){
        given()
                .log()
                .all()
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(201);

    }
    @Test
    void deleteAuthorizationTokenTest(){
        given()
                .log()
                .all()
                .header("Cookie","token=" + token )
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/"+id)
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
                .delete("https://restful-booker.herokuapp.com/booking/"+id)
                .prettyPeek()
                .then()
                .statusCode(403);

    }



}
