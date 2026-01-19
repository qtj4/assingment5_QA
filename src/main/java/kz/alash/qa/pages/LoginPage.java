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

public class LoginPage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Page URL
    private static final String URL = "https://www.saucedemo.com/";

    // Locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("[data-test='error']");
    private By appLogo = By.className("app_logo");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized");
    }

    public void navigateToLoginPage() {
        logger.info("Navigating to SauceDemo login page: {}", URL);
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(appLogo));
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
            return wait.until(ExpectedConditions.visibilityOfElementLocated(appLogo)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Login page not displayed");
            return false;
        }
    }
}