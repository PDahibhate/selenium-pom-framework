package com.prachi.tests;

import com.prachi.pages.CartPage;
import com.prachi.pages.CheckoutPage;
import com.prachi.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CheckoutTest — end-to-end checkout journey.
 * Author: Prachi Dahibhate
 *
 * Test Coverage:
 *   ✅ Add product → Cart → Checkout → Order confirmation
 *   ✅ Cart item count validation
 *   ✅ Missing checkout info validation
 */
public class CheckoutTest extends BaseTest {

    @Test(description = "E2E: Add product, checkout, confirm order",
          priority = 1)
    public void testCompleteCheckoutFlow() {
        // Login
        InventoryPage inventory = getLoginPage()
                .loginAs("standard_user", "secret_sauce");

        Assert.assertTrue(inventory.isInventoryPageDisplayed(), "Should land on inventory");

        // Add product to cart
        inventory.addProductToCartByIndex(0);
        Assert.assertEquals(inventory.getCartBadgeCount(), 1, "Cart badge should show 1");

        // Go to cart
        CartPage cart = inventory.goToCart();
        Assert.assertTrue(cart.isCartPageDisplayed(), "Should be on cart page");
        Assert.assertEquals(cart.getCartItemCount(), 1, "Cart should have 1 item");

        // Checkout
        CheckoutPage checkout = cart.proceedToCheckout();
        checkout.enterShippingInfo("Prachi", "Dahibhate", "411001")
                .clickContinue();

        // Verify total and finish
        String total = checkout.getTotalPrice();
        Assert.assertNotNull(total, "Total price should be displayed");
        log.info("🛒 Order total: {}", total);

        checkout.clickFinish();
        Assert.assertTrue(checkout.isOrderConfirmed(),
                "Confirmation page should be shown after order");

        log.info("✅ E2E checkout test passed");
    }

    @Test(description = "Verify checkout fails without shipping info",
          priority = 2)
    public void testCheckoutWithoutShippingInfo() {
        CheckoutPage checkout = getLoginPage()
                .loginAs("standard_user", "secret_sauce")
                .addProductToCartByIndex(0)
                .goToCart()
                .proceedToCheckout();

        // Click continue without filling info
        checkout.clickContinue();

        Assert.assertFalse(checkout.getErrorMessage().isEmpty(),
                "Error should be shown when shipping info is missing");
    }

    @Test(description = "Verify multiple products added to cart",
          priority = 3)
    public void testAddMultipleProductsToCart() {
        InventoryPage inventory = getLoginPage()
                .loginAs("standard_user", "secret_sauce");

        inventory.addProductToCartByIndex(0);
        inventory.addProductToCartByIndex(1);
        inventory.addProductToCartByIndex(2);

        Assert.assertEquals(inventory.getCartBadgeCount(), 3,
                "Cart badge should show 3 items");

        CartPage cart = inventory.goToCart();
        Assert.assertEquals(cart.getCartItemCount(), 3,
                "Cart should contain 3 items");
    }
}
