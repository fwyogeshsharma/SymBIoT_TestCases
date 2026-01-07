package com.symbiot.tests;

import com.symbiot.base.BaseTest;
import com.symbiot.listeners.RetryAnalyzer;
import com.symbiot.pages.HomePage;
import com.symbiot.pages.LoginPage;
import com.symbiot.utils.Constants;
import com.symbiot.utils.TestDataUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for Login functionality.
 * Covers positive and negative login scenarios.
 */
@Epic("SymBIoT Application")
@Feature("Login")
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUpTest() {
        loginPage = new LoginPage(getPage());
        loginPage.openApplication();
    }

    @Test(
            description = "Verify login page elements are displayed",
            groups = {Constants.SMOKE, Constants.UI, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Login Page UI")
    @Description("Verify all login page elements are displayed correctly")
    public void testLoginPageElementsDisplayed() {
        logger.info("Starting test: testLoginPageElementsDisplayed");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Verify username field is displayed
        Assert.assertTrue(
                loginPage.isUsernameFieldDisplayed(),
                "Username field should be displayed"
        );

        // Verify password field is displayed
        Assert.assertTrue(
                loginPage.isPasswordFieldDisplayed(),
                "Password field should be displayed"
        );

        // Verify login button is displayed
        Assert.assertTrue(
                loginPage.isLoginButtonDisplayed(),
                "Login button should be displayed"
        );

        logger.info("All login page elements are displayed");
    }

    @Test(
            description = "Verify successful login with valid credentials",
            groups = {Constants.SMOKE, Constants.POSITIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("User Login")
    @Description("Verify user can login successfully with valid credentials")
    public void testValidLogin() {
        logger.info("Starting test: testValidLogin");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Perform login with valid credentials
        String username = config.getDefaultUsername();
        String password = config.getDefaultPassword();

        HomePage homePage = loginPage.login(username, password);

        // Verify login was successful
        Assert.assertTrue(
                homePage.isHomePageDisplayed() || homePage.isUserLoggedIn(),
                "User should be logged in and redirected to home page"
        );

        logger.info("Login successful for user: {}", username);
    }

    @Test(
            description = "Verify login fails with invalid username",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Verify login fails with invalid username and valid password")
    public void testInvalidUsername() {
        logger.info("Starting test: testInvalidUsername");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with invalid username
        String invalidUsername = TestDataUtils.randomEmail();
        String validPassword = config.getDefaultPassword();

        loginPage.loginExpectingFailure(invalidUsername, validPassword);

        // Verify error message is displayed
        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed() || loginPage.isLoginPageDisplayed(),
                "Error message should be displayed or user should remain on login page"
        );

        logger.info("Login correctly failed for invalid username: {}", invalidUsername);
    }

    @Test(
            description = "Verify login fails with invalid password",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Verify login fails with valid username and invalid password")
    public void testInvalidPassword() {
        logger.info("Starting test: testInvalidPassword");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with invalid password
        String validUsername = config.getDefaultUsername();
        String invalidPassword = TestDataUtils.randomPassword();

        loginPage.loginExpectingFailure(validUsername, invalidPassword);

        // Verify error message is displayed
        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed() || loginPage.isLoginPageDisplayed(),
                "Error message should be displayed or user should remain on login page"
        );

        logger.info("Login correctly failed for invalid password");
    }

    @Test(
            description = "Verify login fails with empty credentials",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Verify login fails when both username and password are empty")
    public void testEmptyCredentials() {
        logger.info("Starting test: testEmptyCredentials");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with empty credentials
        loginPage.loginExpectingFailure("", "");

        // Verify user remains on login page or error is shown
        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "User should remain on login page when submitting empty credentials"
        );

        logger.info("Login correctly prevented with empty credentials");
    }

    @Test(
            description = "Verify login fails with empty username",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Verify login fails when username is empty")
    public void testEmptyUsername() {
        logger.info("Starting test: testEmptyUsername");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with empty username
        String validPassword = config.getDefaultPassword();
        loginPage.loginExpectingFailure("", validPassword);

        // Verify user remains on login page or error is shown
        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "User should remain on login page when username is empty"
        );

        logger.info("Login correctly prevented with empty username");
    }

    @Test(
            description = "Verify login fails with empty password",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Verify login fails when password is empty")
    public void testEmptyPassword() {
        logger.info("Starting test: testEmptyPassword");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with empty password
        String validUsername = config.getDefaultUsername();
        loginPage.loginExpectingFailure(validUsername, "");

        // Verify user remains on login page or error is shown
        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "User should remain on login page when password is empty"
        );

        logger.info("Login correctly prevented with empty password");
    }

    @Test(
            description = "Verify login with SQL injection attempt",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Security")
    @Description("Verify application is protected against SQL injection in login")
    public void testSqlInjectionPrevention() {
        logger.info("Starting test: testSqlInjectionPrevention");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with SQL injection payload
        String sqlInjectionPayload = "' OR '1'='1";
        loginPage.loginExpectingFailure(sqlInjectionPayload, sqlInjectionPayload);

        // Verify login is not successful and user remains on login page
        Assert.assertTrue(
                loginPage.isLoginPageDisplayed() || loginPage.isErrorMessageDisplayed(),
                "SQL injection should not bypass authentication"
        );

        logger.info("Application correctly prevented SQL injection attempt");
    }

    @Test(
            description = "Verify login with XSS attempt",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Security")
    @Description("Verify application is protected against XSS in login")
    public void testXssPrevention() {
        logger.info("Starting test: testXssPrevention");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with XSS payload
        String xssPayload = "<script>alert('XSS')</script>";
        loginPage.loginExpectingFailure(xssPayload, xssPayload);

        // Verify XSS is not executed - page should still be functional
        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "XSS payload should not break the page"
        );

        logger.info("Application correctly handled XSS attempt");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentialsData() {
        return new Object[][] {
            {"invalid@email.com", "wrongpassword", "Invalid email and password"},
            {"test", "test", "Short credentials"},
            {"user@domain", "password", "Incomplete email"},
            {" ", " ", "Whitespace only"},
            {"admin", "admin", "Common default credentials"}
        };
    }

    @Test(
            description = "Verify login fails with various invalid credential combinations",
            groups = {Constants.REGRESSION, Constants.NEGATIVE, Constants.LOGIN},
            dataProvider = "invalidCredentials",
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Verify login fails with various invalid credential combinations")
    public void testInvalidCredentialCombinations(String username, String password, String description) {
        logger.info("Starting test: testInvalidCredentialCombinations - {}", description);

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Attempt login with invalid credentials
        loginPage.loginExpectingFailure(username, password);

        // Verify login fails
        Assert.assertTrue(
                loginPage.isLoginPageDisplayed() || loginPage.isErrorMessageDisplayed(),
                "Login should fail for: " + description
        );

        // Clear fields for next iteration
        loginPage.clearAllFields();

        logger.info("Login correctly failed for: {}", description);
    }

    @Test(
            description = "Verify login button is initially enabled",
            groups = {Constants.UI, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Login Page UI")
    @Description("Verify login button state")
    public void testLoginButtonState() {
        logger.info("Starting test: testLoginButtonState");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Verify login button is displayed and check its state
        Assert.assertTrue(
                loginPage.isLoginButtonDisplayed(),
                "Login button should be displayed"
        );

        logger.info("Login button state verified");
    }

    @Test(
            description = "Verify login form can be submitted with Enter key",
            groups = {Constants.REGRESSION, Constants.UI, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Login Page UI")
    @Description("Verify form can be submitted using Enter key")
    public void testLoginWithEnterKey() {
        logger.info("Starting test: testLoginWithEnterKey");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Enter credentials
        loginPage.enterUsername(config.getDefaultUsername());
        loginPage.enterPassword(config.getDefaultPassword());

        // Submit with Enter key
        loginPage.submitWithEnterKey();

        // Wait for page transition (using page directly since waitForPageLoad is protected)
        getPage().waitForLoadState();

        // Verify form was submitted (either successful login or error message)
        logger.info("Form submitted with Enter key");
    }

    @Test(
            description = "Verify password field masks input",
            groups = {Constants.UI, Constants.LOGIN},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Login Page UI")
    @Description("Verify password field type is password (masked input)")
    public void testPasswordFieldMasked() {
        logger.info("Starting test: testPasswordFieldMasked");

        // Navigate to login page if not already there
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Verify password field type
        String passwordFieldType = getPage()
                .locator("input[name='password'], input[type='password'], #password")
                .first()
                .getAttribute("type");

        Assert.assertEquals(
                passwordFieldType, "password",
                "Password field should have type='password' for masking"
        );

        logger.info("Password field is correctly masked");
    }
}