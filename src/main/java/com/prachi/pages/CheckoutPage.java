package com.prachi.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CheckoutPage — Page Object for the checkout flow (Step 1 & 2).
 * Author: Prachi Dahibhate
 */
public class CheckoutPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CheckoutPage.class);

    // Step 1
    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // Step 2
    @FindBy(css = ".summary_total_label")
    private WebElement totalPrice;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // Confirmation
    @FindBy(css = ".complete-header")
    private WebElement confirmationHeader;

    // ── Actions ────────────────────────────────────────────────────────────────

    public CheckoutPage enterShippingInfo(String firstName, String lastName, String postalCode) {
        log.info("Entering shipping info");
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        click(continueButton);
        return this;
    }

    public String getTotalPrice() {
        return getText(totalPrice);
    }

    public CheckoutPage clickFinish() {
        log.info("Clicking Finish — completing order");
        click(finishButton);
        return this;
    }

    public boolean isOrderConfirmed() {
        return isDisplayed(confirmationHeader) &&
               getText(confirmationHeader).contains("Thank you");
    }

    public String getErrorMessage() {
        return isDisplayed(errorMessage) ? getText(errorMessage) : "";
    }
}
