package com.prachi.pages;

import com.prachi.config.ConfigReader;
import com.prachi.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage — all Page Objects extend this.
 * Provides reusable, wait-wrapped actions.
 * Author: Prachi Dahibhate
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Actions actions;
    private static final Logger log = LogManager.getLogger(BasePage.class);
    private static final int TIMEOUT = ConfigReader.getInstance().getExplicitWait();

    protected BasePage() {
        this.driver  = DriverManager.getDriver();
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    protected void navigateTo(String url) {
        log.info("🌐 Navigating to: {}", url);
        driver.get(url);
    }

    protected String getCurrentUrl() { return driver.getCurrentUrl(); }
    protected String getPageTitle()  { return driver.getTitle(); }

    // ── Click Actions ──────────────────────────────────────────────────────────

    protected void click(WebElement element) {
        waitForClickable(element);
        log.debug("🖱️  Clicking element: {}", element);
        element.click();
    }

    protected void clickWithJS(WebElement element) {
        log.debug("⚡ JS click on: {}", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // ── Input Actions ──────────────────────────────────────────────────────────

    protected void type(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        log.debug("⌨️  Typing '{}' into element", text);
        element.sendKeys(text);
    }

    protected void clearAndType(WebElement element, String text) {
        waitForVisible(element);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    // ── Dropdown ───────────────────────────────────────────────────────────────

    protected void selectByVisibleText(WebElement element, String text) {
        waitForVisible(element);
        new Select(element).selectByVisibleText(text);
        log.debug("📋 Selected '{}' from dropdown", text);
    }

    protected void selectByValue(WebElement element, String value) {
        waitForVisible(element);
        new Select(element).selectByValue(value);
    }

    // ── Wait Helpers ───────────────────────────────────────────────────────────

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ── Assertions Helpers ─────────────────────────────────────────────────────

    protected boolean isDisplayed(WebElement element) {
        try { return element.isDisplayed(); }
        catch (NoSuchElementException | StaleElementReferenceException e) { return false; }
    }

    protected boolean isEnabled(WebElement element) {
        try { return element.isEnabled(); }
        catch (NoSuchElementException e) { return false; }
    }

    protected String getText(WebElement element) {
        waitForVisible(element);
        return element.getText().trim();
    }

    protected String getAttribute(WebElement element, String attr) {
        waitForVisible(element);
        return element.getAttribute(attr);
    }

    // ── Scroll ──────────────────────────────────────────────────────────────────

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    // ── Hover ───────────────────────────────────────────────────────────────────

    protected void hoverOver(WebElement element) {
        actions.moveToElement(element).perform();
    }

    // ── Alert ───────────────────────────────────────────────────────────────────

    protected String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    // ── Frame ───────────────────────────────────────────────────────────────────

    protected void switchToFrame(WebElement frameElement) {
        driver.switchTo().frame(frameElement);
    }

    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }
}
