package kz.alash.qa.tests;

import kz.alash.qa.base.BaseTest;
import kz.alash.qa.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoTests extends BaseTest {

    // Test Data
    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String INVALID_USERNAME = "invalid_user";
    private static final String INVALID_PASSWORD = "invalid_password";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String POSTAL_CODE = "12345";

    /**
     * TC-LOGIN-001: Valid Login Test
     * Test Case ID: TC-LOGIN-001
     * Requirement ID: REQ-AUTH-001
     * Test Case Title: Verify user can login with valid credentials
     * Preconditions: User is on login page
     * Test Steps:
     * 1. Navigate to SauceDemo login page
     * 2. Enter valid username
     * 3. Enter valid password
     * 4. Click login button
     * Test Data: username=standard_user, password=secret_sauce
     * Expected Result: User should be logged in and redirected to products page
     * Status: Pass/Fail/Blocked
     */
    @Test(priority = 1, description = "TC-LOGIN-001: Verify user can login with valid credentials")
    public void testValidLogin() {
        logStep("Starting TC-LOGIN-001: Valid Login Test");

        // Navigate to login page
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        logStep("Navigated to SauceDemo login page");

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Login page should be displayed");
        logStep("Login page displayed successfully");

        // Perform login
        ProductsPage productsPage = loginPage.performLogin(VALID_USERNAME, VALID_PASSWORD);
        logStep("Entered valid credentials and clicked login");

        // Verify successful login
        Assert.assertTrue(productsPage.isProductsPageDisplayed(),
            "Products page should be displayed after successful login");
        logStep("User successfully logged in and redirected to products page");

        // Verify products are loaded
        int productCount = productsPage.getProductCount();
        Assert.assertTrue(productCount > 0,
            "Products should be displayed on the page");
        logStep("Verified " + productCount + " products are displayed");

        logStep("TC-LOGIN-001 completed successfully");
    }

    /**
     * TC-LOGIN-002: Invalid Login Test
     * Test Case ID: TC-LOGIN-002
     * Requirement ID: REQ-AUTH-002
     * Test Case Title: Verify error message is displayed for invalid credentials
     * Preconditions: User is on login page
     * Test Steps:
     * 1. Navigate to SauceDemo login page
     * 2. Enter invalid username
     * 3. Enter invalid password
     * 4. Click login button
     * Test Data: username=invalid_user, password=invalid_password
     * Expected Result: Error message should be displayed and user should remain on login page
     * Status: Pass/Fail/Blocked
     */
    @Test(priority = 2, description = "TC-LOGIN-002: Verify error message for invalid credentials")
    public void testInvalidLogin() {
        logStep("Starting TC-LOGIN-002: Invalid Login Test");

        // Navigate to login page
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        logStep("Navigated to SauceDemo login page");

        // Attempt login with invalid credentials
        loginPage.enterUsername(INVALID_USERNAME);
        loginPage.enterPassword(INVALID_PASSWORD);
        loginPage.clickLoginButton();
        logStep("Entered invalid credentials and clicked login");

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid credentials");
        logStep("Error message displayed as expected");

        // Verify error message content
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Username and password do not match"),
            "Error message should indicate invalid credentials");
        logStep("Error message content verified: " + errorMessage);

        // Verify user remains on login page
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "User should remain on login page after failed login");
        logStep("User correctly remains on login page");

        logStep("TC-LOGIN-002 completed successfully");
    }

    /**
     * TC-CART-001: Add and Remove Product from Cart
     * Test Case ID: TC-CART-001
     * Requirement ID: REQ-CART-001
     * Test Case Title: Verify user can add and remove products from cart
     * Preconditions: User is logged in and on products page
     * Test Steps:
     * 1. Login with valid credentials
     * 2. Add first product to cart
     * 3. Verify cart badge shows 1 item
     * 4. Go to cart page
     * 5. Verify product is in cart
     * 6. Remove product from cart
     * 7. Verify cart is empty
     * Test Data: username=standard_user, password=secret_sauce
     * Expected Result: Product should be added and removed from cart successfully
     * Status: Pass/Fail/Blocked
     */
    @Test(priority = 3, description = "TC-CART-001: Verify add and remove product from cart")
    public void testAddAndRemoveFromCart() {
        logStep("Starting TC-CART-001: Add and Remove Product from Cart");

        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        ProductsPage productsPage = loginPage.performLogin(VALID_USERNAME, VALID_PASSWORD);
        logStep("User logged in successfully");

        // Add first product to cart
        String firstProductName = productsPage.getFirstProductName();
        productsPage.addFirstProductToCart();
        logStep("Added first product to cart: " + firstProductName);

        // Verify cart badge
        String badgeCount = productsPage.getCartBadgeCount();
        Assert.assertEquals(badgeCount, "1",
            "Cart badge should show 1 item");
        logStep("Cart badge shows correct count: " + badgeCount);

        // Go to cart page
        CartPage cartPage = productsPage.clickShoppingCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
            "Cart page should be displayed");
        logStep("Navigated to cart page");

        // Verify product is in cart
        int cartItemCount = cartPage.getCartItemCount();
        Assert.assertEquals(cartItemCount, 1,
            "Cart should contain 1 item");
        logStep("Cart contains " + cartItemCount + " item(s)");

        // Remove product from cart
        cartPage.removeItemFromCart(firstProductName);
        logStep("Removed product from cart: " + firstProductName);

        // Verify cart is empty
        Assert.assertTrue(cartPage.isCartEmpty(),
            "Cart should be empty after removing the product");
        logStep("Cart is now empty");

        logStep("TC-CART-001 completed successfully");
    }

    /**
     * TC-CHECKOUT-001: Complete Purchase Flow
     * Test Case ID: TC-CHECKOUT-001
     * Requirement ID: REQ-CHECKOUT-001
     * Test Case Title: Verify complete purchase flow from login to order completion
     * Preconditions: User is on login page
     * Test Steps:
     * 1. Login with valid credentials
     * 2. Add a product to cart
     * 3. Go to cart and proceed to checkout
     * 4. Fill checkout information
     * 5. Review order on overview page
     * 6. Complete the order
     * 7. Verify order completion
     * Test Data: username=standard_user, password=secret_sauce, checkout_info={John, Doe, 12345}
     * Expected Result: Order should be completed successfully
     * Status: Pass/Fail/Blocked
     */
    @Test(priority = 4, description = "TC-CHECKOUT-001: Verify complete purchase flow")
    public void testCompletePurchaseFlow() {
        logStep("Starting TC-CHECKOUT-001: Complete Purchase Flow");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        ProductsPage productsPage = loginPage.performLogin(VALID_USERNAME, VALID_PASSWORD);
        logStep("User logged in successfully");

        // Add product to cart
        String firstProductName = productsPage.getFirstProductName();
        productsPage.addFirstProductToCart();
        logStep("Added product to cart: " + firstProductName);

        // Go to cart and checkout
        CartPage cartPage = productsPage.clickShoppingCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        logStep("Navigated to checkout page");

        // Fill checkout information
        CheckoutOverviewPage overviewPage = checkoutPage.fillCheckoutInformation(
            FIRST_NAME, LAST_NAME, POSTAL_CODE);
        logStep("Filled checkout information and proceeded to overview");

        // Verify overview page
        Assert.assertTrue(overviewPage.isCheckoutOverviewPageDisplayed(),
            "Checkout overview page should be displayed");
        logStep("Checkout overview page displayed");

        // Verify order details
        int itemCount = overviewPage.getItemCount();
        Assert.assertEquals(itemCount, 1,
            "Overview should show 1 item");
        logStep("Overview shows correct item count: " + itemCount);

        // Complete the order
        CheckoutCompletePage completePage = overviewPage.clickFinish();
        logStep("Order completed");

        // Verify order completion
        Assert.assertTrue(completePage.isCheckoutCompletePageDisplayed(),
            "Order completion page should be displayed");
        Assert.assertTrue(completePage.isOrderComplete(),
            "Order should be marked as complete");
        logStep("Order completion verified successfully");

        // Return to products page
        completePage.clickBackHome();
        Assert.assertTrue(productsPage.isProductsPageDisplayed(),
            "Should return to products page after order completion");
        logStep("Returned to products page");

        logStep("TC-CHECKOUT-001 completed successfully");
    }
}