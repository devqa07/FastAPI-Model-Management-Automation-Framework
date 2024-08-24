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

public class AddModelTest extends ApiTestBase {
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
        name = RestAPIUtils.getSpecificJsonAttribute(response, "name");
    }

    @Test(dependsOnMethods = "verifyAddModel_Success", priority = 2)
    public void verifyAddModel_DuplicateModel() {
        extentLog.info(customReport(methodName + ": This test performs Add model with duplicate Model Name"));
        body = addModel.addModelBody(name, owner).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_BAD_REQUEST,
                ErrorMessages.DUPLICATE_MODEL_TEXT + name);

    }

    @Test(priority = 3)
    public void verifyAddModel_InvalidHttpMethod() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model error response with Invalid HttpMethod"));
        body = addModel.addModelBody(modelName, owner).toString();
        response = cm.doPut(ConfigHelper.returnPropVal("config", "models"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_METHOD_NOT_ALLOWED,
                ErrorMessages.INVALID_METHOD_TEXT);

    }

    @Test(priority = 4)
    public void verifyAddModel_InvalidEndpoint() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model error response with Invalid EndpointUrl"));
        body = addModel.addModelBody(modelName, owner).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "model"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_NOT_FOUND,
                ErrorMessages.NOT_FOUND_TEXT);
    }

    @Test(priority = 5)
    public void verifyAddModel_WithoutModelNameAttribute() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model error response Without ModelName Attribute"));
        body = addModel.addModelBody(null, owner).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test(priority = 6)
    public void verifyAddModel_WithoutOwnerAttribute() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model error response Without Owner Attribute"));
        body = addModel.addModelBody(modelName, null).toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyErrorResponse(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test(priority = 7)
    public void verifyAddModel_Intentional_Fail_With_EmptyOwner() {
        extentLog.info(customReport(methodName + ": This test verifies Add Model with Empty Owner"));
        String name = RestAPIUtils.generateModelName();
        body = addModel.addModelBody(name, "").toString();
        response = cm.doPost(ConfigHelper.returnPropVal("config", "models"), body);
        extentLog.log(Status.INFO, customReport("Click here to the view the Request", requestWriter.toString()));
        extentLog.log(Status.INFO, customReport("Click here to the view the Response",
                response.asString()));
        verifyStatusCode(response, HttpStatus.SC_OK);
        modelResponse.validateAddModelResponse(response, name, owner);
    }

}


