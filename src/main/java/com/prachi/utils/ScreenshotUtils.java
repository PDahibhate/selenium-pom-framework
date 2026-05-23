package com.prachi.utils;

import com.prachi.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils — captures and saves screenshots.
 * Author: Prachi Dahibhate
 */
public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR =
            ConfigReader.getInstance().getScreenshotPath();

    private ScreenshotUtils() {}

    /**
     * Captures a screenshot and returns the file path.
     * @param testName used as filename prefix
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName   = testName + "_" + timestamp + ".png";
        String filePath   = SCREENSHOT_DIR + fileName;

        try {
            File srcFile  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.forceMkdirParent(destFile);
            FileUtils.copyFile(srcFile, destFile);
            log.info("📸 Screenshot saved: {}", filePath);
        } catch (IOException e) {
            log.error("❌ Screenshot capture failed: {}", e.getMessage());
        }

        return filePath;
    }

    /** Returns screenshot as Base64 string (for embedding in ExtentReport) */
    public static String captureBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
