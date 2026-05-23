package com.prachi.tests;

import com.prachi.listeners.RetryAnalyzer;
import com.prachi.pages.InventoryPage;
import com.prachi.pages.LoginPage;
import com.prachi.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * LoginTest — covers all login scenarios.
 * Author: Prachi Dahibhate
 *
 * Test Coverage:
 *   ✅ Valid login
 *   ✅ Invalid credentials
 *   ✅ Empty username / password
 *   ✅ Locked-out user
 *   ✅ Data-driven login (Excel)
 */
public class LoginTest extends BaseTest {

    // ── Valid Login ────────────────────────────────────────────────────────────

    @Test(description = "Verify successful login with valid credentials",
          priority = 1, retryAnalyzer = RetryAnalyzer.class)
    public void testValidLogin() {
        InventoryPage inventory = getLoginPage()
                .loginAs("standard_user", "secret_sauce");

        Assert.assertTrue(inventory.isInventoryPageDisplayed(),
                "Inventory page should be displayed after valid login");
        Assert.assertEquals(inventory.getPageHeading(), "Products",
                "Page heading should be 'Products'");

        log.info("✅ Valid login test passed");
    }

    // ── Invalid Credentials ────────────────────────────────────────────────────

    @Test(description = "Verify error shown for invalid credentials",
          priority = 2)
    public void testInvalidCredentials() {
        LoginPage loginPage = getLoginPage()
                .loginWithInvalidCredentials("wrong_user", "wrong_pass");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be visible");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error message text mismatch");

        log.info("✅ Invalid credentials test passed");
    }

    // ── Empty Username ─────────────────────────────────────────────────────────

    @Test(description = "Verify error shown when username is empty",
          priority = 3)
    public void testEmptyUsername() {
        LoginPage loginPage = getLoginPage()
                .loginWithInvalidCredentials("", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
    }

    // ── Empty Password ─────────────────────────────────────────────────────────

    @Test(description = "Verify error shown when password is empty",
          priority = 4)
    public void testEmptyPassword() {
        LoginPage loginPage = getLoginPage()
                .loginWithInvalidCredentials("standard_user", "");

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
    }

    // ── Locked Out User ────────────────────────────────────────────────────────

    @Test(description = "Verify locked-out user cannot login",
          priority = 5)
    public void testLockedOutUser() {
        LoginPage loginPage = getLoginPage()
                .loginWithInvalidCredentials("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"));
    }

    // ── Data-Driven Login ──────────────────────────────────────────────────────

    @Test(description = "Data-driven login test from Excel",
          dataProvider = "loginData", priority = 6)
    @SuppressWarnings("unchecked")
    public void testLoginDataDriven(Map<String, String> data) {
        String username      = data.get("username");
        String password      = data.get("password");
        String expectedResult = data.get("expected");

        log.info("🧪 Testing login — user: {} | expected: {}", username, expectedResult);

        if ("pass".equalsIgnoreCase(expectedResult)) {
            InventoryPage inventory = getLoginPage().loginAs(username, password);
            Assert.assertTrue(inventory.isInventoryPageDisplayed(),
                    "Login should succeed for: " + username);
        } else {
            LoginPage loginPage = getLoginPage()
                    .loginWithInvalidCredentials(username, password);
            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Error should be shown for: " + username);
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getDataProviderData(config.getTestDataPath(), "LoginData");
    }
}
