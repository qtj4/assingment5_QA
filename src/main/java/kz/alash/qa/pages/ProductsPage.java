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

public class ProductsPage {
    private static final Logger logger = LogManager.getLogger(ProductsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By productsTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By addToCartButtons = By.cssSelector("button[data-test*='add-to-cart']");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By inventoryItemNames = By.className("inventory_item_name");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("ProductsPage initialized");
    }

    public boolean isProductsPageDisplayed() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitle));
            return title.getText().equals("Products");
        } catch (Exception e) {
            logger.debug("Products page not displayed");
            return false;
        }
    }

    public int getProductCount() {
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        logger.info("Found {} products on the page", products.size());
        return products.size();
    }

    public void addFirstProductToCart() {
        logger.info("Adding first product to cart");
        WebElement firstAddButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButtons));
        firstAddButton.click();
        logger.info("First product added to cart successfully");
    }


    public String getCartBadgeCount() {
        try {
            WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(shoppingCartBadge));
            String count = badge.getText();
            logger.info("Cart badge shows count: {}", count);
            return count;
        } catch (Exception e) {
            logger.debug("Cart badge not visible (cart empty)");
            return "0";
        }
    }

    public CartPage clickShoppingCart() {
        logger.info("Clicking shopping cart link");
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(shoppingCartLink));
        cartLink.click();

        logger.info("Navigating to cart page");
        return new CartPage(driver);
    }

    public String getFirstProductName() {
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryItemNames));
        return firstProduct.getText();
    }
}