package com.fast.api.core.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    static ExtentHtmlReporter htmlReporter;
    static ExtentReports extent;
    final static String current_Platform = System.getProperty("os.name");

    public static ExtentReports getExtentReport() {
        return setExtentReport();
    }

    private static ExtentReports setExtentReport() {
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/" + "Fast-API-AutomationReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", current_Platform);
        htmlReporter.config().setDocumentTitle("Fast-API Automation-Report");
        htmlReporter.config().setReportName("Fast-API Automation Test Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setTheme(Theme.DARK);
        return extent;
    }
}