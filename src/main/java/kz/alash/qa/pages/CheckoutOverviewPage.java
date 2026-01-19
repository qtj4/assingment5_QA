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

public class CheckoutOverviewPage {
    private static final Logger logger = LogManager.getLogger(CheckoutOverviewPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By overviewTitle = By.className("title");
    private By cartItems = By.className("cart_item");
    private By itemNames = By.className("inventory_item_name");
    private By itemPrices = By.className("inventory_item_price");
    private By itemQuantities = By.className("cart_quantity");
    private By subtotalLabel = By.className("summary_subtotal_label");
    private By taxLabel = By.className("summary_tax_label");
    private By totalLabel = By.className("summary_total_label");
    private By finishButton = By.id("finish");
    private By cancelButton = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("CheckoutOverviewPage initialized");
    }

    public boolean isCheckoutOverviewPageDisplayed() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(overviewTitle));
            return title.getText().equals("Checkout: Overview");
        } catch (Exception e) {
            logger.debug("Checkout overview page not displayed");
            return false;
        }
    }

    public int getItemCount() {
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));
        logger.info("Found {} items in checkout overview", items.size());
        return items.size();
    }

    public List<String> getItemNames() {
        List<WebElement> nameElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(itemNames));
        List<String> names = nameElements.stream().map(WebElement::getText).toList();
        logger.info("Items in checkout overview: {}", names);
        return names;
    }

    public String getSubtotal() {
        WebElement subtotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel));
        String subtotal = subtotalElement.getText();
        logger.info("Subtotal: {}", subtotal);
        return subtotal;
    }

    public String getTax() {
        WebElement taxElement = wait.until(ExpectedConditions.visibilityOfElementLocated(taxLabel));
        String tax = taxElement.getText();
        logger.info("Tax: {}", tax);
        return tax;
    }

    public String getTotal() {
        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLabel));
        String total = totalElement.getText();
        logger.info("Total: {}", total);
        return total;
    }

    public CheckoutCompletePage clickFinish() {
        logger.info("Clicking finish button");
        WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(finishButton));
        finishBtn.click();

        logger.info("Completing checkout process");
        return new CheckoutCompletePage(driver);
    }

    public CartPage clickCancel() {
        logger.info("Clicking cancel button on checkout overview");
        WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancelBtn.click();

        logger.info("Cancelling checkout, navigating back to cart");
        return new CartPage(driver);
    }

    public double getSubtotalAmount() {
        String subtotalText = getSubtotal();
        // Extract numeric value from "Item total: $XX.XX"
        String amount = subtotalText.replace("Item total: $", "");
        return Double.parseDouble(amount);
    }

    public double getTaxAmount() {
        String taxText = getTax();
        // Extract numeric value from "Tax: $XX.XX"
        String amount = taxText.replace("Tax: $", "");
        return Double.parseDouble(amount);
    }

    public double getTotalAmount() {
        String totalText = getTotal();
        // Extract numeric value from "Total: $XX.XX"
        String amount = totalText.replace("Total: $", "");
        return Double.parseDouble(amount);
    }
}