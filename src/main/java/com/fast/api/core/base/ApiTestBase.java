/*
 * Author: Devendra Singh
 * Date: 24/08/24
 */

package com.fast.api.core.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fast.api.core.utils.TestConstants;
import com.fast.api.core.utils.CommonMethods;
import com.fast.api.core.utils.CsvUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.output.WriterOutputStream;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class ApiTestBase {
    protected static ExtentReports extent;
    protected static ExtentTest extentLog;
    protected static String testName = "";
    protected static StringWriter requestWriter;
    protected static PrintStream requestCapture;
    protected String methodName;
    protected CsvUtils csv = new CsvUtils();
    protected CommonMethods cm = new CommonMethods();

    @BeforeSuite
    public void setUp() {
        extent = ExtentManager.getExtentReport();
    }

    public String customReport(String message) {
        String format = "<b class='exception' style='display:block; cursor:pointer; user-select:none' onclick='($(\".exception\").click(function(){ $(this).next().toggle()}))'>"
                + message + "</b>" + "<pre style='display:none'></pre>";
        return format;
    }

    public String customReport(String message, String response) {
        String format = "<b class='exception' style='display:block; cursor:pointer; user-select:none' onclick='($(\".exception\").click(function(){ $(this).next().toggle()}))'>"
                + message + "</b>" + "<pre style='display:none'>" + response + "</pre>";
        return format;
    }

    public void verifyStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected status code");
    }

    public void verifyErrorResponse(Response response, int expectedStatusCode) {
        // Assert the status code
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected status code");
    }

    public void verifyErrorResponse(Response response, int expectedStatusCode, String expectedErrorMessage) {
        JsonPath jsonResponse = response.jsonPath();
        // Assert the status code
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected status code");
        // Assert the error message
        Assert.assertEquals(jsonResponse.getString("detail"), expectedErrorMessage, "Unexpected error message");
    }

    @BeforeMethod
    public void getMethodName(Method m) {
        methodName = m.getName();
        requestWriter = new StringWriter();
        requestCapture = new PrintStream(new WriterOutputStream(requestWriter), true);
        TestConstants.REQUEST_CAPTURE = requestCapture;
        String packageName = this.getClass().getPackage().getName();
        testName = this.getClass().getSimpleName() + " : " + m.getName();
        extentLog = extent.createTest(testName, m.getAnnotation(Test.class).description())
                .assignCategory(packageName.substring(packageName.lastIndexOf(".") + 1));
    }

    @AfterMethod
    public void reportsUpdate(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {
            extentLog.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
            extentLog.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentLog.log(Status.SKIP,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
            extentLog.log(Status.SKIP,
                    MarkupHelper.createLabel(result.getThrowable() + " - Test Case Skipped", ExtentColor.ORANGE));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentLog.log(Status.PASS,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Passed", ExtentColor.GREEN));
        }
    }

    @AfterSuite
    public void afterSuite(ITestContext context) {
        System.out.println("In after suite************************************************");
        extent.flush();
    }
}