package com.prachi.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton ConfigReader — loads config.properties once.
 * Author: Prachi Dahibhate
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties = new Properties();
    private static final String CONFIG_PATH = "src/main/resources/config.properties";

    private ConfigReader() {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
            log.info("✅ config.properties loaded successfully.");
        } catch (IOException e) {
            log.error("❌ Failed to load config.properties: {}", e.getMessage());
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) instance = new ConfigReader();
            }
        }
        return instance;
    }

    public String get(String key) {
        String value = System.getProperty(key);  // CLI overrides config file
        if (value == null) value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("❌ Config key not found: " + key);
        return value.trim();
    }

    public String getBaseUrl()          { return get("base.url"); }
    public String getBrowser()          { return get("browser"); }
    public String getRunMode()          { return get("run.mode"); }
    public String getGridUrl()          { return get("grid.url"); }
    public int    getImplicitWait()     { return Integer.parseInt(get("implicit.wait")); }
    public int    getExplicitWait()     { return Integer.parseInt(get("explicit.wait")); }
    public int    getPageLoadTimeout()  { return Integer.parseInt(get("page.load.timeout")); }
    public String getTestDataPath()     { return get("test.data.path"); }
    public boolean isScreenshotOnFail() { return Boolean.parseBoolean(get("screenshot.on.failure")); }
    public String getScreenshotPath()   { return get("screenshot.path"); }
    public String getReportPath()       { return get("report.path"); }
    public String getReportName()       { return get("report.name"); }
    public boolean isHeadless()         { return Boolean.parseBoolean(get("headless")); }
    public int    getRetryCount()       { return Integer.parseInt(get("retry.count")); }
}
