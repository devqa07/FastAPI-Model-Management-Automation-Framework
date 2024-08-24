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

    public JSONObject performInferenceBody(String text, boolean applyTemplate,int maxNewTokens,boolean doSample,float temperature,int topK,float topP) {
        JSONObject inference = new JSONObject();
        inference.put("text", text);
        inference.put("apply_template", applyTemplate);
        inference.put("max_new_tokens", maxNewTokens);
        inference.put("do_sample", doSample);
        inference.put("temperature", temperature);
        inference.put("top_k", topK);
        inference.put("top_p", topP);
        return inference;
    }
}