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

public class LoginPage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page URL
    private static final String URL = "https://www.saucedemo.com/";

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized");
    }

    public void navigateToLoginPage() {
        logger.info("Navigating to SauceDemo login page: {}", URL);
        driver.get(URL);
        try {
            // Wait for page to load by checking title first
            wait.until(ExpectedConditions.titleContains("Swag Labs"));
            // Then wait for username field
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            logger.info("Login page loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load login page, checking page source...");
            logger.debug("Page title: {}", driver.getTitle());
            logger.debug("Current URL: {}", driver.getCurrentUrl());
            // Try to find any input elements as fallback
            try {
                driver.findElement(By.tagName("input"));
                logger.debug("Found input elements on page");
            } catch (Exception e2) {
                logger.debug("No input elements found either");
            }
            throw e;
        }
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        WebElement usernameElement = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password: {}", password);
        WebElement passwordElement = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public ProductsPage clickLoginButton() {
        logger.info("Clicking login button");
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();

        // Wait for products page to load
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        logger.info("Login successful, navigating to products page");
        return new ProductsPage(driver);
    }

    public void clickLoginButtonWithoutWait() {
        logger.info("Clicking login button (without waiting for navigation)");
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
        logger.info("Login button clicked");
    }

    public ProductsPage performLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    public String getErrorMessage() {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        String errorText = errorElement.getText();
        logger.info("Error message displayed: {}", errorText);
        return errorText;
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Error message not displayed");
            return false;
        }
    }

    public boolean isLoginPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Login page not displayed");
            return false;
        }
    }
}