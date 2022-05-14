package com.VolAndRnD.github;

import com.VolAnd.githab.Bookingdates;
import com.VolAnd.githab.CreateIdRequest;
import com.VolAnd.githab.CreateTokenRequest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public abstract class BaseTest {
   private static final String PROPERTIES_FILE_PATH = "src/test/resources/application.properties";

   static Properties properties =new Properties();
   static String baseUrl;





    @BeforeAll
    static void beforAll() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured.filters(new AllureRestAssured());

        properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        RestAssured.baseURI = properties.getProperty("base.url");
    }
}