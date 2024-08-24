/*
 * Author: Devendra Singh
 * Date: 24/08/24
 */

package com.fast.api.core.requestBuilder;

import com.fast.api.core.base.ApiTestBase;
import org.json.JSONObject;

public class ModelRequestBuilder extends ApiTestBase {

    public JSONObject addModelBody(String name, String owner) {
        JSONObject addModel = new JSONObject();
        addModel.put("name", name);
        addModel.put("owner", owner);
        return addModel;
    }

    public JSONObject addModelVersionsBody(String versionName, String huggingFaceModel) {
        JSONObject modelVersion = new JSONObject();
        modelVersion.put("name", versionName);
        modelVersion.put("hugging_face_model", huggingFaceModel);
        return modelVersion;
    }

    public JSONObject performInferenceBody(String text) {
        JSONObject inference = new JSONObject();
        inference.put("text", text);
        return inference;
    }
}