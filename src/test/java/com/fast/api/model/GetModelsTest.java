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

public class GetModelsTest extends ApiTestBase {
    ModelRequestBuilder addModel = new ModelRequestBuilder();
    ModelResponseParser modelResponse = new ModelResponseParser();
    List<String[]> data = csv.readCsvData(TestConstants.MODEL_CSV);
    String body, owner = csv.getSpecificCSVData(data, 0, 1), name,
            modelName = RestAPIUtils.generateModelName();
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

    }

    @Test(dependsOnMethods = "verifyAddModel_Success", priority = 2)
    public void verifyGetModels_Success() {
        extentLog.info(customReport(methodName + ": This test Gets the list models"));
        response = cm.doGet(ConfigHelper.returnPropVal("config", "models"));
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyStatusCode(response, HttpStatus.SC_OK);
        modelResponse.validateGetModelsResponse(response);
    }

    @Test(priority = 3)
    public void verifyGetModels_InvalidHttpMethod() {
        extentLog.info(customReport(methodName + ": This test verifies Get Models error response with Invalid HttpMethod"));
        response = cm.doDelete(ConfigHelper.returnPropVal("config", "models"));
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_METHOD_NOT_ALLOWED,
                ErrorMessages.INVALID_METHOD_TEXT);

    }

    @Test(priority = 4)
    public void verifyGetModel_InvalidEndpoint() {
        extentLog.info(customReport(methodName + ": This test verifies Get Models error response with Invalid EndpointUrl"));
        response = cm.doGet(ConfigHelper.returnPropVal("config", "model"));
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_NOT_FOUND,
                ErrorMessages.NOT_FOUND_TEXT);
    }
}