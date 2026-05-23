package com.prachi.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InventoryPage — Page Object for the products/inventory screen.
 * Author: Prachi Dahibhate
 */
public class InventoryPage extends BasePage {

    private static final Logger log = LogManager.getLogger(InventoryPage.class);

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = "button[id^='add-to-cart']")
    private List<WebElement> addToCartButtons;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = "[data-test='product_sort_container']")
    private WebElement sortDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    // ── Actions ────────────────────────────────────────────────────────────────

    public boolean isInventoryPageDisplayed() {
        return isDisplayed(pageTitle) && getPageTitle().contains("Swag Labs");
    }

    public String getPageHeading() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        return inventoryItems.size();
    }

    public List<String> getAllProductNames() {
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public InventoryPage addProductToCartByIndex(int index) {
        log.info("Adding product {} to cart", index);
        click(addToCartButtons.get(index));
        return this;
    }

    public InventoryPage addProductToCartByName(String productName) {
        log.info("Adding '{}' to cart", productName);
        productNames.stream()
                .filter(e -> e.getText().equalsIgnoreCase(productName))
                .findFirst()
                .ifPresentOrElse(
                        e -> {
                            WebElement parent = e.findElement(
                                    org.openqa.selenium.By.xpath("./ancestor::div[@class='inventory_item']"));
                            parent.findElement(
                                    org.openqa.selenium.By.cssSelector("button[id^='add-to-cart']")).click();
                        },
                        () -> { throw new RuntimeException("Product not found: " + productName); }
                );
        return this;
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) return 0;
        return Integer.parseInt(getText(cartBadge));
    }

    public CartPage goToCart() {
        log.info("Navigating to cart");
        click(cartIcon);
        return new CartPage();
    }

    public InventoryPage sortProductsBy(String sortOption) {
        log.info("Sorting by: {}", sortOption);
        selectByVisibleText(sortDropdown, sortOption);
        return this;
    }

    public LoginPage logout() {
        click(hamburgerMenu);
        waitForClickable(logoutLink);
        click(logoutLink);
        return new LoginPage();
    }
}
