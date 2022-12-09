import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.Assert;
import org.testng.annotations.Test;


public class APITests {

    @Test
    void AddBookTest() {
        // Adding the book which was added already - Here I put expected code 400,
        // because otherwise If test launches again it will fail,
        // so I think is better to check that same book cannot be added twice.

        RestAssured.baseURI = "https://demoqa.com";
        String bookData = """
                {\r
                  "userId": "c450205e-4e83-4927-b7b6-fc1f12c4315d",\r
                 "collectionOfIsbns": [{"isbn": "9781449325862"}]\r
                }""";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.auth().preemptive().basic("Matt", "Password123!");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("accept", "application/json");
        httpRequest.when();
        Response response = httpRequest.body(bookData).post("/BookStore/v1/Books");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
//        System.out.println(response.statusCode());
//        System.out.println(response.asString());
    }

    @Test
    void AddDeleteBookTest() {
        // STEP 1 Adding new book that was not added before
        RestAssured.baseURI = "https://demoqa.com";
        String bookData = """
                {\r
                  "userId": "c450205e-4e83-4927-b7b6-fc1f12c4315d",\r
                 "collectionOfIsbns": [{"isbn": "9781449331818"}]\r
                }""";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.auth().preemptive().basic("Matt", "Password123!");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("accept", "application/json");
        httpRequest.when();
        Response response = httpRequest.body(bookData).post("/BookStore/v1/Books");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
//        System.out.println(response.statusCode());
//        System.out.println(response.asString());

        // STEP 2 Deleting the book that was just added
        String deleteBook = """
                {
                  "isbn": "9781449331818",
                  "userId": "c450205e-4e83-4927-b7b6-fc1f12c4315d"
                }""";
        Response response2 = httpRequest.body(deleteBook).delete("/BookStore/v1/Book");
        int statusCode2 = response2.getStatusCode();
        Assert.assertEquals(statusCode2, 204);
//        System.out.println(response2.statusCode());
//        System.out.println(response2.asString());
    }
}
