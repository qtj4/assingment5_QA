# SauceDemo Test Automation Project

This project implements automated test cases for the SauceDemo e-commerce website using Java, TestNG, Selenium WebDriver, and ExtentReports.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── kz/alash/qa/pages/          # Page Object Model classes
│   │       ├── LoginPage.java
│   │       ├── ProductsPage.java
│   │       ├── CartPage.java
│   │       ├── CheckoutPage.java
│   │       ├── CheckoutOverviewPage.java
│   │       └── CheckoutCompletePage.java
│   └── resources/
│       └── log4j2.xml                   # Logging configuration
└── test/
    └── java/
        └── kz/alash/qa/
            ├── base/
            │   └── BaseTest.java       # Base test class with setup/teardown
            └── tests/
                └── SauceDemoTests.java # Test cases
```

## Technology Stack

- **Java 21**: Programming language
- **TestNG**: Test framework for lifecycle management
- **Selenium WebDriver**: Browser automation
- **Log4j**: Logging framework
- **ExtentReports**: HTML test reporting
- **WebDriverManager**: Automatic driver management
- **Maven**: Build and dependency management

## Prerequisites

1. **Java 21** installed and configured
2. **Maven 3.6+** installed
3. **Chrome browser** installed (tests run in headless mode by default)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd assignment5_QA
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Setup
```bash
mvn test-compile
```

## Test Execution

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=SauceDemoTests
```

### Run with Different Browser
```bash
# Chrome (default)
mvn clean test

# Firefox
mvn clean test -Dbrowser=firefox
```

### Run Single Test Method
```bash
mvn clean test -Dtest=SauceDemoTests#testValidLogin
```

## Test Cases Implemented

### TC-LOGIN-001: Valid Login Test
- **Description**: Verify user can login with valid credentials
- **Preconditions**: User is on login page
- **Test Data**: username=standard_user, password=secret_sauce
- **Expected Result**: User successfully logged in and redirected to products page

### TC-LOGIN-002: Invalid Login Test
- **Description**: Verify error message for invalid credentials
- **Preconditions**: User is on login page
- **Test Data**: username=invalid_user, password=invalid_password
- **Expected Result**: Error message displayed and user remains on login page

### TC-CART-001: Add and Remove Product from Cart
- **Description**: Verify user can add and remove products from cart
- **Preconditions**: User is logged in and on products page
- **Test Data**: username=standard_user, password=secret_sauce
- **Expected Result**: Product added and removed from cart successfully

### TC-CHECKOUT-001: Complete Purchase Flow
- **Description**: Verify complete purchase flow from login to order completion
- **Preconditions**: User is on login page
- **Test Data**: username=standard_user, password=secret_sauce, checkout_info={John, Doe, 12345}
- **Expected Result**: Order completed successfully

## Reporting and Logs

### ExtentReports
- **Location**: `test-output/ExtentReports/`
- **Format**: HTML with detailed test execution summary
- **Features**:
  - Test execution summary (pass/fail/skip)
  - Individual test case results
  - Expected vs actual results
  - Screenshots on test failure
  - Clear failure descriptions
  - Logs included in report

### Logging
- **Framework**: Log4j2
- **Locations**:
  - Console output
  - File: `logs/test-execution.log`
- **Log Levels**: DEBUG, INFO, WARN, ERROR

### Screenshots
- **Location**: `test-output/Screenshots/`
- **Captured**: Automatically on test failure
- **Naming**: `{testMethodName}_{timestamp}.png`

## Configuration

### Browser Configuration
Tests run in headless mode by default. To run with visible browser:
- Remove `--headless` arguments from `BaseTest.java`
- Comment out `--window-size=1920,1080` if needed

### Logging Configuration
Modify `src/main/resources/log4j2.xml` to adjust log levels and appenders.

### TestNG Configuration
Modify `testng.xml` to:
- Change browser parameter
- Add/remove test methods
- Configure parallel execution
- Add custom listeners

## Project Deliverables

1. **Source Code**: Complete Maven project with all test automation code
2. **Test Reports**: HTML ExtentReports generated after test execution
3. **Documentation**: This README file with setup and execution instructions
4. **Logs**: Detailed execution logs for debugging and analysis
5. **Screenshots**: Failure screenshots for bug analysis

## Bug Reports

If bugs are discovered during test execution, they should be reported using the following template:

### Bug Report Template
1. **ID**: BUG-XXX
2. **Summary/Title**: Short description of the issue
3. **Priority**: High / Medium / Low
4. **Environment**: OS, Browser, App Version, Device
5. **Steps to Reproduce**: Numbered steps to trigger the bug
6. **Actual Result**: What actually happens
7. **Expected Result**: What should happen
8. **Attachment**: Screenshot or video evidence

## Troubleshooting

### Common Issues

1. **WebDriver not found**
   - Ensure Chrome/Firefox is installed
   - WebDriverManager handles driver downloads automatically

2. **Tests fail due to timing**
   - Increase wait timeouts in page objects if needed
   - Check network connectivity

3. **Maven build fails**
   - Ensure Java 21 is properly installed
   - Run `mvn clean install` to resolve dependencies

4. **Reports not generated**
   - Check write permissions in project directory
   - Ensure test execution completes properly

### Debug Mode
Enable debug logging by modifying `log4j2.xml`:
```xml
<Logger name="kz.alash" level="DEBUG" additivity="false">
```

## Contact Information

For questions or issues related to this test automation project, please refer to the project documentation or contact the development team.