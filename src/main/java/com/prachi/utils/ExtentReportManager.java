package com.prachi.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.prachi.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ExtentReportManager — Thread-safe ExtentReports singleton.
 * Author: Prachi Dahibhate
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(config.getReportPath());
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle(config.getReportName());
            spark.config().setReportName(config.getReportName());
            spark.config().setEncoding("utf-8");
            spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
            spark.config().setCss(".badge-primary { background: #2E4057 !important; }");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "Prachi Dahibhate");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", config.getBrowser());
            extent.setSystemInfo("Environment", config.getBaseUrl());

            log.info("📊 ExtentReport initialised at: {}", config.getReportPath());
        }
        return extent;
    }

    public static ExtentTest getTest()                     { return testThread.get(); }
    public static void       setTest(ExtentTest test)      { testThread.set(test); }
    public static void       removeTest()                  { testThread.remove(); }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        setTest(test);
        return test;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            log.info("📊 ExtentReport flushed.");
        }
    }
}
