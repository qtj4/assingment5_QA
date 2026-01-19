package kz.alash.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {
    private static final Logger logger = LogManager.getLogger(CartPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By cartTitle = By.className("title");
    private By cartItems = By.className("cart_item");
    private By cartItemNames = By.className("inventory_item_name");
    private By cartItemPrices = By.className("inventory_item_price");
    private By removeButtons = By.cssSelector("button[data-test*='remove']");
    private By continueShoppingButton = By.id("continue-shopping");
    private By checkoutButton = By.id("checkout");
    private By cartQuantity = By.className("cart_quantity");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("CartPage initialized");
    }

    public boolean isCartPageDisplayed() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
            return title.getText().equals("Your Cart");
        } catch (Exception e) {
            logger.debug("Cart page not displayed");
            return false;
        }
    }

    public int getCartItemCount() {
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));
        logger.info("Found {} items in cart", items.size());
        return items.size();
    }

    public List<String> getCartItemNames() {
        List<WebElement> nameElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItemNames));
        List<String> names = nameElements.stream().map(WebElement::getText).toList();
        logger.info("Cart contains items: {}", names);
        return names;
    }

    public void removeItemFromCart(String productName) {
        logger.info("Removing item from cart: {}", productName);

        List<WebElement> cartItemElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));

        for (WebElement item : cartItemElements) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement removeButton = item.findElement(By.cssSelector("button[data-test*='remove']"));
                removeButton.click();
                logger.info("Successfully removed {} from cart", productName);
                return;
            }
        }

        throw new RuntimeException("Product not found in cart: " + productName);
    }

    public ProductsPage clickContinueShopping() {
        logger.info("Clicking continue shopping button");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
        continueBtn.click();

        logger.info("Navigating back to products page");
        return new ProductsPage(driver);
    }

    public CheckoutPage clickCheckout() {
        logger.info("Clicking checkout button");
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkoutBtn.click();

        logger.info("Navigating to checkout page");
        return new CheckoutPage(driver);
    }

    public boolean isCartEmpty() {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            return items.isEmpty();
        } catch (Exception e) {
            logger.debug("Cart appears to be empty");
            return true;
        }
    }

    public String getCartQuantity(String productName) {
        List<WebElement> cartItemElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));

        for (WebElement item : cartItemElements) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement quantityElement = item.findElement(By.className("cart_quantity"));
                return quantityElement.getText();
            }
        }

        return "0";
    }
}