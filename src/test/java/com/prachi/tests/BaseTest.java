package com.prachi.tests;

import com.prachi.config.ConfigReader;
import com.prachi.pages.LoginPage;
import com.prachi.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

/**
 * BaseTest — all Test classes extend this.
 * Handles driver init/teardown and base URL navigation.
 * Author: Prachi Dahibhate
 */
@Listeners({
    com.prachi.listeners.TestListener.class
})
public abstract class BaseTest {

    protected static final Logger log    = LogManager.getLogger(BaseTest.class);
    protected static final ConfigReader config = ConfigReader.getInstance();

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        log.info("═══ Starting: {} ═══", method.getName());
        DriverManager.initDriver();
        DriverManager.getDriver().get(config.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method) {
        log.info("═══ Finishing: {} ═══", method.getName());
        DriverManager.quitDriver();
    }

    /** Returns a fresh LoginPage (driver is already on base URL) */
    protected LoginPage getLoginPage() {
        return new LoginPage();
    }
}
