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

public class ProductsPage {
    private static final Logger logger = LogManager.getLogger(ProductsPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By productsTitle = By.className("title");
    private By inventoryItems = By.className("inventory_item");
    private By addToCartButtons = By.cssSelector("button[data-test*='add-to-cart']");
    private By removeFromCartButtons = By.cssSelector("button[data-test*='remove']");
    private By shoppingCartBadge = By.className("shopping_cart_badge");
    private By shoppingCartLink = By.className("shopping_cart_link");
    private By sortDropdown = By.className("product_sort_container");
    private By inventoryItemNames = By.className("inventory_item_name");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    public void addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);

        // Find the product by name and click add to cart button
        List<WebElement> productElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        for (WebElement product : productElements) {
            WebElement nameElement = product.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement addButton = product.findElement(By.cssSelector("button[data-test*='add-to-cart']"));
                addButton.click();
                logger.info("Successfully added {} to cart", productName);
                return;
            }
        }

        throw new RuntimeException("Product not found: " + productName);
    }

    public void addFirstProductToCart() {
        logger.info("Adding first product to cart");
        WebElement firstAddButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButtons));
        firstAddButton.click();
        logger.info("First product added to cart successfully");
    }

    public void removeProductFromCart(String productName) {
        logger.info("Removing product from cart: {}", productName);

        List<WebElement> productElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        for (WebElement product : productElements) {
            WebElement nameElement = product.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement removeButton = product.findElement(By.cssSelector("button[data-test*='remove']"));
                removeButton.click();
                logger.info("Successfully removed {} from cart", productName);
                return;
            }
        }

        throw new RuntimeException("Product not found: " + productName);
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

    public void sortProductsBy(String sortOption) {
        logger.info("Sorting products by: {}", sortOption);
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        dropdown.click();

        // Select sort option by value
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("option[value='" + sortOption + "']")));
        option.click();

        logger.info("Products sorted successfully");
    }

    public List<String> getProductNames() {
        List<WebElement> nameElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItemNames));
        return nameElements.stream().map(WebElement::getText).toList();
    }

    public String getFirstProductName() {
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryItemNames));
        return firstProduct.getText();
    }
}