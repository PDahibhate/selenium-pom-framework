package com.prachi.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CartPage — Page Object for the shopping cart.
 * Author: Prachi Dahibhate
 */
public class CartPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CartPage.class);

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    // ── Actions ────────────────────────────────────────────────────────────────

    public boolean isCartPageDisplayed() {
        return isDisplayed(pageTitle) && getPageTitle().contains("Swag Labs");
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public List<String> getCartItemNames() {
        return cartItemNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isItemInCart(String productName) {
        return getCartItemNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    public CheckoutPage proceedToCheckout() {
        log.info("Proceeding to checkout");
        click(checkoutButton);
        return new CheckoutPage();
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage();
    }
}
