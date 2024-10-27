package com.juaracoding;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class MovieDetailsTest {

    String API_KEY = "d3e3f79defcaf004e1762ac373ceb9e8";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
        RestAssured.basePath = "/movie";
    }

    @Test(priority = 1)
    public void testMovieDetails01(){
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when()
                    .get("912649") // Endpoint untuk mendapatkan movie details (movie_Id)
                .then()
                .statusCode(200) // 200 OK
                .log().all();
    }

    @Test(priority = 2)
    public void testMovieDetails02(){
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when() // Send request
                    .get("1184918") // Endpoint untuk mendapatkan movie details (movie_Id)
                .then()
                    .statusCode(200) // 200 OK
                    .body("size()", greaterThan(0)) // Memastikan terdapat setidaknya 1 film
                    .body("id", notNullValue()) // Memastikan properti 'overview' ada dan tidak null
                    .body("id", equalTo(1184918)) // Memastikan properti 'id' ada dan tidak null
                    .body("overview", notNullValue()) // Memastikan properti 'overview' ada dan tidak null
                    .body("overview", equalTo("After a shipwreck, an intelligent robot called Roz is stranded " +
                            "on an uninhabited island. To survive the harsh environment, Roz bonds with the island's animals " +
                            "and cares for an orphaned baby goose.")) // Memastikan properti 'overview' ada dan tidak null
                .log().all();
    }

    @Test(priority = 3)
    public void testMovieDetails03(){
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .when() // Send request
                    .get("1184918") // Endpoint untuk mendapatkan movie details (movie_Id)
                .then()
                    .statusCode(200) // 200 OK
                    .body("title", notNullValue()) // Memastikan properti 'title' tidak null
                    .body("title", equalTo("The Wild Robot")) // Memastikan properti 'title' tidak null
                .log().all();
    }

    // Negative Scenario 1: ID film yang tidak valid
    @Test(priority = 4)
    public void testInvalidMovieId() {
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .when()
                    .get("9999999") // movie_id yang tidak valid
                .then()
                    .statusCode(404) // 404 Not Found
                    .body("status_message", containsString("The resource you requested could not be found."))
                    .log().all();
    }

    // Negative Scenario 2: Menggunakan API key yang tidak valid
    @Test(priority = 5)
    public void testInvalidApiKey() {
        given()
                .queryParam("api_key", "INVALID_API_KEY") // API key yang tidak valid
                .queryParam("language", "en-US")
                .when()
                    .get("912649") // Endpoint untuk mendapatkan movie details (movie_Id)
                .then()
                    .statusCode(401) // 401 Unauthorized
                    .body("status_message", containsString("Invalid API key"))
                    .log().all();
    }

    // Negative Scenario 3: Parameter page tidak valid (misal, page=-1)
    @Test(priority = 6)
    public void testInvalidPageParameter() {
        given()
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .queryParam("page", -1) // Parameter page tidak valid
                .when()
                    .get("1184918") // Endpoint untuk mendapatkan movie details (movie_Id)
                .then()
                    .statusCode(400) // 400 Bad Request
                    .body("status_message", containsString("Invalid page"))
                    .log().all();
    }

}
