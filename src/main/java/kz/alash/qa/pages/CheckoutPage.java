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

public class CheckoutPage {
    private static final Logger logger = LogManager.getLogger(CheckoutPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By checkoutTitle = By.className("title");
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By postalCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By cancelButton = By.id("cancel");
    private By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("CheckoutPage initialized");
    }

    public boolean isCheckoutPageDisplayed() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutTitle));
            return title.getText().equals("Checkout: Your Information");
        } catch (Exception e) {
            logger.debug("Checkout page not displayed");
            return false;
        }
    }

    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        WebElement firstNameElement = wait.until(ExpectedConditions.elementToBeClickable(firstNameField));
        firstNameElement.clear();
        firstNameElement.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        WebElement lastNameElement = wait.until(ExpectedConditions.elementToBeClickable(lastNameField));
        lastNameElement.clear();
        lastNameElement.sendKeys(lastName);
    }

    public void enterPostalCode(String postalCode) {
        logger.info("Entering postal code: {}", postalCode);
        WebElement postalCodeElement = wait.until(ExpectedConditions.elementToBeClickable(postalCodeField));
        postalCodeElement.clear();
        postalCodeElement.sendKeys(postalCode);
    }

    public CheckoutOverviewPage clickContinue() {
        logger.info("Clicking continue button on checkout page");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();

        logger.info("Navigating to checkout overview page");
        return new CheckoutOverviewPage(driver);
    }

    public CheckoutOverviewPage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return clickContinue();
    }

    public CartPage clickCancel() {
        logger.info("Clicking cancel button on checkout page");
        WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancelBtn.click();

        logger.info("Navigating back to cart page");
        return new CartPage(driver);
    }

    public String getErrorMessage() {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        String errorText = errorElement.getText();
        logger.info("Checkout error message: {}", errorText);
        return errorText;
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Checkout error message not displayed");
            return false;
        }
    }
}