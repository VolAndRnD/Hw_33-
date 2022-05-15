package com.github.VolAndRnD;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PartialUpdateBookingTest {
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
    void partialUpdateAuthorizationPasswordTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .body("{\n"
                        +   " \"firstname\" : \"Bim\",\n"
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
                .body("{\n"
                        +   " \"firstname\" : \"Jim\",\n"
                        +   " \"lastname\" : \"Brown\",\n"
                        +   " \"totalprice\" : 1111,\n"
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
                .body("{\n"
                        +   " \"firstname\" : \"Jim\",\n"
                        +   " \"lastname\" : \"Brown\",\n"
                        +   " \"totalprice\" : 1111,\n"
                        +   " \"depositpaid\" : true,\n"
                        +   " \"bookingdates\" : {\n"
                        +   "  \"checkin\" :  \"2021-01-01\",\n"
                        +   "  \"checkout\" :  \"2022-01-01\"\n"
                        +   "   },\n"
                        +   " \"additionalneeds\" : \"Breakfast\"\n"
                        +   "}"
                )
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .statusCode(403);

    }
    @Test
    void partialUpdateAuthorizationTokenInvalueBookingdatesTest(){
        given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie","token=" + token )
                .body("{\n"
                        +   " \"firstname\" : \"Jim\",\n"
                        +   " \"lastname\" : \"Brown\",\n"
                        +   " \"totalprice\" : 1111,\n"
                        +   " \"depositpaid\" : true,\n"
                        +   " \"bookingdates\" : {\n"
                        +   "  \"checkin\" :  \"01-01-2021\",\n"
                        +   "  \"checkout\" :  \"01-01-2022\"\n"
                        +   "   },\n"
                        +   " \"additionalneeds\" : \"Breakfast\"\n"
                        +   "}"
                )
                .expect()
                .body("checkin", equalTo("01-01-2021"))
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
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
                .body("{\n"
                        + " \"firstname\" : \"Vladislave\",\n"
                        + " \"lastname\" : \"Brown\",\n"
                        + " \"totalprice\" : 1111,\n"
                        + " \"depositpaid\" : true,\n"
                        + " \"bookingdates\" : {\n"
                        + "  \"checkin\" :  \"2021-01-01\",\n"
                        + "  \"checkout\" :  \"2022-01-01\"\n"
                        + "   },\n"
                        + " \"additionalneeds\" : \"Breakfast\"\n"
                        + "}"
                )
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
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
                .body("{\n"
                        +   " \"firstname\" : \"Ag ent\",\n"
                        +   " \"lastname\" : \"007\",\n"
                        +   " \"totalprice\" : 10000000,\n"
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
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek();
                assertThat(response.statusCode(),equalTo(200));
                assertThat(response.body().jsonPath().get("firstname"), containsStringIgnoringCase("Ag ent"));
                assertThat(response.body().jsonPath().get("lastname"), containsStringIgnoringCase("007"));
                assertThat(response.body().jsonPath().get("totalprice"), equalTo(10000000));
    }
    @Test
    void partialUpdateAuthorizationTokenAndDepositPaidNegativTest() {
        Response response = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n"
                        + " \"firstname\" : \"Jim\",\n"
                        + " \"lastname\" : \"Brown\",\n"
                        + " \"totalprice\" : 1111,\n"
                        + " \"depositpaid\" : 007,\n"
                        + " \"bookingdates\" : {\n"
                        + "  \"checkin\" :  \"2021-01-01\",\n"
                        + "  \"checkout\" :  \"2022-01-01\"\n"
                        + "   },\n"
                        + " \"additionalneeds\" : \"Breakfast\"\n"
                        + "}"
                )
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(400));

    }


}
