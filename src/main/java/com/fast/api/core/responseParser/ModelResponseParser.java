/*
 * Author: Devendra Singh
 * Date: 24/08/24
 */

package com.fast.api.core.responseParser;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

public class ModelResponseParser {
    JSONObject json;

    // Validate Add Model Response
    public void validateAddModelResponse(Response response, String name, String owner) {
        json = new JSONObject(response.asString());
        Assert.assertNotNull(json.getString("id"));
        Assert.assertEquals(json.getString("name"), name);
        Assert.assertEquals(json.getString("owner"), owner);
    }

    // Validate Add Model versions Response
    public void validateAddModelVersionsResponse(Response response, String name, String parentModelId, String huggingFaceModel) {
        json = new JSONObject(response.asString());
        Assert.assertNotNull(json.getString("id"));
        Assert.assertEquals(json.getString("name"), name);
        Assert.assertEquals(json.getString("parent_model_id"), parentModelId);
        Assert.assertEquals(json.getString("hugging_face_model"), huggingFaceModel);
    }

    // Validate Get Models Response
    public void validateGetModelsResponse(Response response) {
        String jsonString = response.asString();
        JSONArray jsonArray = new JSONArray(jsonString);
        Assert.assertTrue(jsonArray.length() > 0);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Assert.assertNotNull(jsonObject.getString("id"));
            Assert.assertNotNull(jsonObject.getString("name"));
            Assert.assertNotNull(jsonObject.getString("owner"));
        }
    }

    // Validate Get Model versions Response
    public void validateGetModelVersionsResponse(Response response, String parentModelId) {
        String jsonString = response.asString();
        JSONArray jsonArray = new JSONArray(jsonString);
        Assert.assertTrue(jsonArray.length() > 0);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Assert.assertNotNull(jsonObject.getString("id"));
            Assert.assertNotNull(jsonObject.getString("name"));
            Assert.assertEquals(json.getString("parent_model_id"), parentModelId);
            Assert.assertNotNull(jsonObject.getString("hugging_face_model"));
        }
    }

    // Validate Perform Inference Response
    public static void validatePerformInferenceResponse(Response response, String expectedMessage) {
        // Extract response body as a string
        String actualResponse = response.getBody().asString();
        // Validate that the response is not null and contains the expected message
        assertResponseContains(expectedMessage, actualResponse);
    }

    // Helper method to assert that the actual response contains the expected message
    private static void assertResponseContains(String expectedMessage, String actualResponse) {
        Assert.assertNotNull(actualResponse, "Response body is null");
        Assert.assertTrue(actualResponse.contains(expectedMessage),
                "Unexpected response");
    }
}