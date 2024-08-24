package com.fast.api.test;

import com.aventstack.extentreports.Status;
import com.fast.api.core.base.ApiTestBase;
import com.fast.api.core.requestBuilder.ModelRequestBuilder;
import com.fast.api.core.responseParser.ModelResponseParser;
import com.fast.api.core.utils.ConfigHelper;
import com.fast.api.core.utils.ErrorMessages;
import com.fast.api.core.utils.RestAPIUtils;
import com.fast.api.core.utils.TestConstants;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.List;

public class AddModelVersionsTest extends ApiTestBase {
    ModelRequestBuilder addModel = new ModelRequestBuilder();
    ModelRequestBuilder addModelVersion = new ModelRequestBuilder();
    ModelResponseParser modelResponse = new ModelResponseParser();
    List<String[]> data = csv.readCsvData(TestConstants.MODEL_CSV);
    String body, owner = csv.getSpecificCSVData(data, 0, 1), huggingFaceModel = csv.getSpecificCSVData(data, 1, 1),
            name, modelId, modelIdInput, modelName = RestAPIUtils.generateModelName(),versionName=RestAPIUtils.generateVersionName();
    Response response;


    @Test(priority = 1)
    public void verifyAddModel_Success() {
        extentLog.info(customReport(methodName + ": This test performs Add Model"));
        body = addModel.addModelBody(modelName, owner).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyStatusCode(response, HttpStatus.SC_OK);
        modelResponse.validateAddModelResponse(response, modelName, owner);
        modelId = RestAPIUtils.getSpecificJsonAttribute(response, "id");
    }


    @Test(dependsOnMethods = "verifyAddModel_Success", priority = 2)
    public void verifyAddModelVersions_Success() {
        extentLog.info(customReport(methodName + ": This test performs Add Model versions with ModelId"));
        body = addModelVersion.addModelVersionsBody(versionName, huggingFaceModel).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models") + "/" + modelId + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyStatusCode(response, HttpStatus.SC_OK);
        modelResponse.validateAddModelVersionsResponse(response, versionName, modelId, huggingFaceModel);
        name = RestAPIUtils.getSpecificJsonAttribute(response, "name");
    }

    @Test(dependsOnMethods = "verifyAddModelVersions_Success", priority = 3)
    public void verifyAddModelVersions_DuplicateModel() {
        extentLog.info(customReport(methodName + ": This test performs Add model Versions with duplicate Model Name"));
        body = addModelVersion.addModelVersionsBody(name, huggingFaceModel).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models") + "/" + modelId + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_BAD_REQUEST,
                ErrorMessages.DUPLICATE_MODEL_TEXT + name);

    }

    @Test(priority = 4)
    public void verifyAddModelVersions_InvalidHttpMethod() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model Versions error response with Invalid HttpMethod"));
        modelIdInput = csv.getSpecificCSVData(data, 2, 1);
        body = addModelVersion.addModelVersionsBody(name, huggingFaceModel).toString();
        response = cm.doPut(ConfigHelper.returnPropVal("config", "models") + "/" + modelIdInput + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_METHOD_NOT_ALLOWED,
                ErrorMessages.INVALID_METHOD_TEXT);

    }

    @Test(priority = 5)
    public void verifyAddModelVersion_InvalidEndpoint() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model Versions error response with Invalid EndpointUrl"));
        modelIdInput = csv.getSpecificCSVData(data, 2, 1);
        body = addModelVersion.addModelVersionsBody(name, huggingFaceModel).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "model") + "/" + modelIdInput + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_NOT_FOUND,
                ErrorMessages.NOT_FOUND_TEXT);
    }

    @Test(priority = 6)
    public void verifyAddModelVersions_WithInvalidModelId() {
        extentLog.info(customReport(methodName + ": This test performs Add Model versions with ModelId"));
        modelIdInput = csv.getSpecificCSVData(data, 2, 1);
        body = addModelVersion.addModelVersionsBody(versionName, huggingFaceModel).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models") + "/" + modelIdInput + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_NOT_FOUND,
                ErrorMessages.MODEL_NOT_FOUND_TEXT);
    }

    @Test(priority = 7)
    public void verifyAddModelVersions_WithoutVersionNameAttribute() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model Versions error response Without Version Name Attribute"));
        modelIdInput = csv.getSpecificCSVData(data, 2, 1);
        body = addModelVersion.addModelVersionsBody(null, huggingFaceModel).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models") + "/" + modelIdInput + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test(priority = 8)
    public void verifyAddModelVersions_WithoutHuggingFaceModelAttribute() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model Versions error response Without HuggingFaceMode Attribute"));
        body = addModelVersion.addModelVersionsBody(versionName, null).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models") + "/" + modelId + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test(priority = 9, dependsOnMethods = "verifyAddModel_Success")
    public void verifyAddModelVersions_Intentional_Fail_With_EmptyHuggingFaceMode() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model with Empty Owner"));
        String name = RestAPIUtils.generateVersionName();
        body = addModelVersion.addModelVersionsBody(name, "").toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models") + "/" + modelId + "/versions", body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyStatusCode(response, HttpStatus.SC_OK);
        modelResponse.validateAddModelVersionsResponse(response, name, modelId, huggingFaceModel);
    }

}


