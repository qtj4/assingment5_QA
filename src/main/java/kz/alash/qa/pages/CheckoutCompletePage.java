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

public class CheckoutCompletePage {
    private static final Logger logger = LogManager.getLogger(CheckoutCompletePage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By completeTitle = By.className("title");
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("CheckoutCompletePage initialized");
    }

    public boolean isCheckoutCompletePageDisplayed() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(completeTitle));
            return title.getText().equals("Checkout: Complete!");
        } catch (Exception e) {
            logger.debug("Checkout complete page not displayed");
            return false;
        }
    }

    public String getCompleteHeader() {
        WebElement headerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader));
        String header = headerElement.getText();
        logger.info("Complete header: {}", header);
        return header;
    }

    public String getCompleteText() {
        WebElement textElement = wait.until(ExpectedConditions.visibilityOfElementLocated(completeText));
        String text = textElement.getText();
        logger.info("Complete text: {}", text);
        return text;
    }

    public void clickBackHome() {
        logger.info("Clicking back home button");
        WebElement backHomeBtn = wait.until(ExpectedConditions.elementToBeClickable(backHomeButton));
        backHomeBtn.click();

        logger.info("Navigating back to products page");
        new ProductsPage(driver);
    }

    public boolean isOrderComplete() {
        return getCompleteHeader().equals("Thank you for your order!")
            && getCompleteText().contains("Your order has been dispatched");
    }
}