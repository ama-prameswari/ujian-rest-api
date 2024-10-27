package com.juaracoding;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieNowPlayingTest {

    String API_KEY = "d3e3f79defcaf004e1762ac373ceb9e8";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
        RestAssured.basePath = "/movie";
    }

    @Test(priority = 1)
    public void testMovieNowPlaying01(){
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("/now_playing")
                .then()
                    .statusCode(200) // 200 OK
                    .log().all();
    }

    @Test(priority = 2)
    public void testMovieNowPlaying02(){
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("/now_playing")
                .then()
                    .statusCode(200) // 200 OK
                    .body("results.size()", greaterThan(0)) // Memastikan setidaknya lebih dari 1 film dalam movie lists
                    .body("results.title", everyItem(notNullValue())) // Memastikan setiap film memiliki "title"
                    .log().all();
    }

    @Test(priority = 3)
    public void testMovieNowPlaying03(){
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("/now_playing")
                .then()
                    .statusCode(200) // 200 OK
                    .body("results.title", everyItem(notNullValue())) // Memastikan setiap film dalam movie lists memiliki properti seperti "title"
                    .log().all();
    }

    // Negative Scenario 1: Invalid API key
    @Test(priority = 4)
    public void testInvalidApiKey() {
        given()
                .queryParam("api_key", "INVALID_API_KEY")
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("/now_playing")
                .then()
                    .statusCode(401) // 401 Unauthorized
                    .body("status_message", containsString("Invalid API key"))
                    .log().all();
    }

    // Negative Scenario 2: Incorrect endpoint (404 Not Found)
    @Test(priority = 5)
    public void testInvalidEndpoint() {
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("/now_showing") // Incorrect endpoint
                .then()
                    .statusCode(404) // 404 Not Found
                    .body("status_message", containsString("Invalid id"))
                    .log().all();
    }

    // Negative Scenario 3: Missing required API key
    @Test(priority = 6)
    public void testMissingApiKey() {
        given()
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("/now_playing")
                .then()
                    .statusCode(401) // 401 Unauthorized
                    .body("status_message", containsString("Invalid API key"))
                    .log().all();
    }

}
