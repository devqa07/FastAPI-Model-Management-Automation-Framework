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
}