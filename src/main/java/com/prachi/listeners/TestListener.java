package com.prachi.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.prachi.config.ConfigReader;
import com.prachi.utils.DriverManager;
import com.prachi.utils.ExtentReportManager;
import com.prachi.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener — hooks into TestNG lifecycle for reporting and screenshots.
 * Author: Prachi Dahibhate
 */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    @Override
    public void onStart(ITestContext context) {
        log.info("🚀 Test Suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
        log.info("🏁 Test Suite finished: {} | Passed: {} | Failed: {} | Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String desc     = result.getMethod().getDescription();
        log.info("▶ Test started: {}", testName);
        ExtentReportManager.createTest(testName, desc != null ? desc : "");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ PASS: {}", result.getMethod().getMethodName());
        ExtentReportManager.getTest().log(Status.PASS, "Test Passed ✅");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("❌ FAIL: {} | Reason: {}", testName, result.getThrowable().getMessage());

        ExtentReportManager.getTest().log(Status.FAIL,
                "Test Failed ❌ : " + result.getThrowable().getMessage());

        // Attach screenshot on failure
        if (config.isScreenshotOnFail()) {
            try {
                String base64 = ScreenshotUtils.captureBase64(DriverManager.getDriver());
                ExtentReportManager.getTest().fail(
                        "Screenshot on failure:",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } catch (Exception e) {
                log.warn("⚠️  Could not capture screenshot: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⏭  SKIP: {}", result.getMethod().getMethodName());
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped ⏭");
    }
}
