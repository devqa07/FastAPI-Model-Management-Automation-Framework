/*
 * Author: Devendra Singh
 * Date: 24/08/24
 */

package com.fast.api.model;

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

public class DeleteModelVersionTest extends ApiTestBase {
    ModelRequestBuilder addModel = new ModelRequestBuilder();
    ModelRequestBuilder addModelVersion = new ModelRequestBuilder();
    ModelResponseParser modelResponse = new ModelResponseParser();
    List<String[]> data = csv.readCsvData(TestConstants.MODEL_CSV);
    String body, owner = csv.getSpecificCSVData(data, 0, 1), huggingFaceModel = csv.getSpecificCSVData(data, 1, 1),
            modelIdInput, parentModelId, versionId, verIdInput, modelName = RestAPIUtils.generateModelName(), versionName = RestAPIUtils.generateVersionName();
    static String modelId;
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
        parentModelId = RestAPIUtils.getSpecificJsonAttribute(response, "parent_model_id");
        versionId = RestAPIUtils.getSpecificJsonAttribute(response, "id");
    }

    @Test(dependsOnMethods = "verifyAddModelVersions_Success", priority = 3)
    public void verifyDeleteModelVersion_Success() {
        extentLog.info(customReport(methodName + ": This test deletes Model version with ModelId and Version Id"));
        response = cm.doDelete(ConfigHelper.returnPropVal("config", "models") + "/" + parentModelId + "/versions" + "/" + versionId);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyStatusCode(response, HttpStatus.SC_OK);

    }

    @Test(priority = 4)
    public void verifyDeleteModelVersion_InvalidHttpMethod() {
        extentLog.info(customReport(methodName + ": This test verifies Delete Model Version error response with Invalid HttpMethod"));
        modelIdInput = csv.getSpecificCSVData(data, 2, 1);
        verIdInput = csv.getSpecificCSVData(data, 3, 1);
        response = cm.doGet(ConfigHelper.returnPropVal("config", "models") + "/" + modelIdInput + "/versions" + "/" + verIdInput);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_METHOD_NOT_ALLOWED,
                ErrorMessages.INVALID_METHOD_TEXT);

    }

    @Test(priority = 5)
    public void verifyDeleteModelVersion_InvalidEndpoint() {
        extentLog.info(customReport(methodName + ": This test verifies Delete Model Version error response with Invalid EndpointUrl"));
        modelIdInput = csv.getSpecificCSVData(data, 2, 1);
        verIdInput = csv.getSpecificCSVData(data, 3, 1);
        response = cm.doDelete(ConfigHelper.returnPropVal("config", "models") + "/" + modelIdInput + "/version" + "/" + verIdInput);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_NOT_FOUND,
                ErrorMessages.NOT_FOUND_TEXT);
    }

    @Test(priority = 6)
    public void verifyDeleteModelVersion_WithInvalidVersionId() {
        extentLog.info(customReport(methodName + ": This test performs Delete Model version with Invalid VersionId"));
        verIdInput = csv.getSpecificCSVData(data, 3, 1);
        response = cm.doDelete(ConfigHelper.returnPropVal("config", "models") + "/" + modelId + "/versions" + "/" + verIdInput);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_NOT_FOUND,
                ErrorMessages.MODEL_VERSION_NOT_FOUND_TEXT);
    }
}