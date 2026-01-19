package kz.alash.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class CartPage {
    private static final Logger logger = LogManager.getLogger(CartPage.class);
    public static final String INVENTORY_ITEM_NAME = "inventory_item_name";
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By cartTitle = By.className("title");
    private final By cartItems = By.className("cart_item");

    static {
        By.className("inventory_item_price");
    }

    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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

    public void removeItemFromCart(String productName) {
        logger.info("Removing item from cart: {}", productName);

        List<WebElement> cartItemElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));

        for (WebElement item : cartItemElements) {
            WebElement nameElement = item.findElement(By.className(INVENTORY_ITEM_NAME));
            if (nameElement.getText().equals(productName)) {
                WebElement removeButton = item.findElement(By.cssSelector("button[data-test*='remove']"));
                removeButton.click();
                logger.info("Successfully removed {} from cart", productName);
                return;
            }
        }

        throw new NoSuchElementException("Product not found in cart: " + productName);
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
}