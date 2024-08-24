package com.fast.api.core.utils;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class CommonMethods {
    String endPoint;
    Response response;

    public Response doPost(String endpoint, String body) {
        // Combine the base URL with the endpoint
        String fullEndpoint = RestAPIUtils.getEnvToRun() + endpoint;
        // Build and execute the POST request
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter(TestConstants.REQUEST_CAPTURE))
                .log().all()
                .body(body)
                .post(fullEndpoint);

        // Prints the response details
        System.out.println("================================== RESPONSE ====================================");
        response.prettyPrint();
        System.out.println("=================================================================================");

        // Returns the response
        return response;
    }

    public Response doPut(String endpoint, String body) {
        String fullEndpoint = RestAPIUtils.getEnvToRun() + endpoint;
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter(TestConstants.REQUEST_CAPTURE))
                .log().all()
                .body(body)
                .put(fullEndpoint);

        // Prints the response details
        System.out.println("================================== RESPONSE ====================================");
        response.prettyPrint();
        System.out.println("=================================================================================");

        // Returns the response
        return response;
    }

    public Response doGet(String endpoint) {
        String fullEndpoint = RestAPIUtils.getEnvToRun() + endpoint;
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter(TestConstants.REQUEST_CAPTURE))
                .log().all()
                .get(fullEndpoint);

        // Prints the response details
        System.out.println("================================== RESPONSE ====================================");
        response.prettyPrint();
        System.out.println("=================================================================================");
        // Returns the response
        return response;
    }


    public Response doDelete(String endpoint) {
        String fullEndpoint = RestAPIUtils.getEnvToRun() + endpoint;
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter(TestConstants.REQUEST_CAPTURE))
                .log().all()
                .delete(fullEndpoint);

        // Prints the response details
        System.out.println("================================== RESPONSE ====================================");
        response.prettyPrint();
        System.out.println("=================================================================================");

        // Returns the response
        return response;
    }

    public Response doPOST(String endpoint) {
        endPoint = RestAPIUtils.getEnvToRun() + endpoint;
        response = RestAssured.given().with().contentType("application/json")
                .filter(new RequestLoggingFilter(TestConstants.REQUEST_CAPTURE)).when().log().all().post(endPoint);
        System.out.println(
                "=================================================RESPONSE=======================================================================");
        response.prettyPrint();

        System.out.println(
                "================================================================================================================================");
        return response;
    }

}