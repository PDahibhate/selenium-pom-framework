package com.prachi.utils;

import com.prachi.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

/**
 * ThreadLocal DriverManager — supports parallel test execution safely.
 * Author: Prachi Dahibhate
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            initDriver();
        }
        return driverThread.get();
    }

    public static void initDriver() {
        String browser   = config.getBrowser().toLowerCase();
        String runMode   = config.getRunMode().toLowerCase();
        boolean headless = config.isHeadless();
        WebDriver driver;

        log.info("🚀 Initialising driver: browser={} | mode={} | headless={}", browser, runMode, headless);

        try {
            if ("remote".equals(runMode)) {
                driver = createRemoteDriver(browser, headless);
            } else {
                driver = createLocalDriver(browser, headless);
            }
        } catch (Exception e) {
            log.error("❌ Driver initialisation failed: {}", e.getMessage());
            throw new RuntimeException("Failed to initialise WebDriver", e);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();

        driverThread.set(driver);
        log.info("✅ Driver initialised successfully.");
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                yield new FirefoxDriver(opts);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                yield new EdgeDriver(opts);
            }
            default -> {  // chrome
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new");
                opts.addArguments("--no-sandbox", "--disable-dev-shm-usage",
                        "--disable-gpu", "--window-size=1920,1080");
                yield new ChromeDriver(opts);
            }
        };
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless) throws Exception {
        String gridUrl = config.getGridUrl();
        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                yield new RemoteWebDriver(new URL(gridUrl), opts);
            }
            case "edge" -> {
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                yield new RemoteWebDriver(new URL(gridUrl), opts);
            }
            default -> {
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new");
                yield new RemoteWebDriver(new URL(gridUrl), opts);
            }
        };
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
            log.info("🔴 Driver quit and removed from ThreadLocal.");
        }
    }
}
