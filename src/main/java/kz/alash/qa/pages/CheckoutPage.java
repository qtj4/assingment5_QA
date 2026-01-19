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

public class CheckoutPage {
    private static final Logger logger = LogManager.getLogger(CheckoutPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("CheckoutPage initialized");
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
}