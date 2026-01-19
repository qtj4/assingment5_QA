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

public class CheckoutOverviewPage {
    private static final Logger logger = LogManager.getLogger(CheckoutOverviewPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By overviewTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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

    public CheckoutCompletePage clickFinish() {
        logger.info("Clicking finish button");
        WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(finishButton));
        finishBtn.click();

        logger.info("Completing checkout process");
        return new CheckoutCompletePage(driver);
    }
}