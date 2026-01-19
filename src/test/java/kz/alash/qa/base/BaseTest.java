package kz.alash.qa.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    private static final String REPORT_PATH = "test-output/ExtentReports/";
    private static final String SCREENSHOT_PATH = "test-output/Screenshots/";

    @BeforeSuite
    public void setupSuite() {
        logger.info("=== Starting Test Suite Execution ===");

        // Setup WebDriver
        WebDriverManager.chromedriver().setup();

        // Setup ExtentReports
        setupExtentReports();

        // Create directories for screenshots
        createDirectories();
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setupTest(@Optional("chrome") String browser, ITestResult result) {
        logger.info("=== Setting up test: {} ===", result.getMethod().getMethodName());

        // Initialize WebDriver based on browser parameter
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(options);
        }

        // Create ExtentTest instance
        test = extent.createTest(result.getMethod().getMethodName());

        logger.info("WebDriver initialized successfully for browser: {}", browser);
    }

    @AfterMethod
    public void tearDownTest(ITestResult result) {
        logger.info("=== Tearing down test: {} ===", result.getMethod().getMethodName());

        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.error("Test failed: {}", result.getThrowable().getMessage());
                test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());

                // Capture screenshot on failure
                String screenshotPath = captureScreenshot(result.getMethod().getMethodName());
                if (screenshotPath != null) {
                    test.fail("Screenshot captured", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                logger.info("Test passed successfully");
                test.log(Status.PASS, "Test passed successfully");
            } else if (result.getStatus() == ITestResult.SKIP) {
                logger.warn("Test was skipped");
                test.log(Status.SKIP, "Test was skipped");
            }
        } catch (Exception e) {
            logger.error("Error during test teardown: {}", e.getMessage());
        } finally {
            // Close browser
            if (driver != null) {
                driver.quit();
                logger.info("WebDriver closed successfully");
            }
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        logger.info("=== Test Suite Execution Completed ===");

        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed successfully");
        }
    }

    private void setupExtentReports() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = REPORT_PATH + "TestReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("SauceDemo Test Automation Report");
        spark.config().setReportName("Test Execution Report");
        spark.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        logger.info("ExtentReports setup completed at: {}", reportPath);
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(REPORT_PATH));
            Files.createDirectories(Paths.get(SCREENSHOT_PATH));
            logger.info("Directories created successfully");
        } catch (IOException e) {
            logger.error("Failed to create directories: {}", e.getMessage());
        }
    }

    private String captureScreenshot(String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String screenshotName = testName + "_" + timestamp + ".png";
                String screenshotPath = SCREENSHOT_PATH + screenshotName;

                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(screenshot.toPath(), Paths.get(screenshotPath));

                logger.info("Screenshot captured: {}", screenshotPath);
                return screenshotPath;
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }
        return null;
    }

    protected void logStep(String message) {
        logger.info("STEP: {}", message);
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    protected void logError(String message) {
        logger.error("ERROR: {}", message);
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }
}