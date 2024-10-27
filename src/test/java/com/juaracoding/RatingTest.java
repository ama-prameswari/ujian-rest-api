package com.juaracoding;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RatingTest {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkM2UzZjc5ZGVmY2FmMDA0ZTE3NjJhYzM3M2NlYjllOCIsIm5iZiI6MTczMDAyNzc3OC4zNjg5NDQsInN1YiI6IjY3MTkwOGJjNDI3YzVjMTlmMDI1YTI0YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.6kNyYqmW1SlaA_wkxC7I1D_w-ftogD0UqE3696EAlac";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
        RestAssured.basePath = "/movie";
    }

    @Test(priority = 1)
    public void testPostMovieRating(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("value", 8.5);

        RequestSpecification request = RestAssured.given();
        request.header("Authorization", token);
        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post("/1189668/rating");

        Assert.assertEquals(response.getStatusCode(), 201); // 201 Created
        Assert.assertTrue(response.getBody().jsonPath().getBoolean("success"));
        Assert.assertTrue(response.getBody().jsonPath()
                .getString("status_message").contains("success"));
    }

    // Negative Scenario 1: Tanpa Content-Type Header
    @Test(priority = 2)
    public void testWithoutContentType(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("value", 8.0);

        RequestSpecification request = RestAssured.given();
        request.header("Authorization", token);
        request.body(requestBody.toJSONString());
        Response response = request.post("/1189668/rating");

        Assert.assertEquals(response.getStatusCode(), 400); // Bad Request
        Assert.assertFalse(response.getBody().jsonPath().getBoolean("success"));
        Assert.assertTrue(response.getBody().jsonPath()
                .getString("status_message").contains("Invalid parameters"));
    }

    // Negative Scenario 2: Request Tanpa Token
    @Test(priority = 3)
    public void testRequestWithoutToken(){
        given()
                .when()
                    .get("/1189668/rating")
                .then()
                    .statusCode(401) // 401 Unauthorized
                    .log().all();
    }

    // Negative Scenario 3: Request dengan URL yang Salah
    @Test(priority = 4)
    public void testWrongUrl(){
        given()
                .header("Authorization", token)
                .when()
                    .get("/1189668/rating")
                .then()
                    .statusCode(404) // 404 Not Found
                    .log().all();
    }

}
