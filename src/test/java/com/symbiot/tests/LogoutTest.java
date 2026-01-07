package com.symbiot.tests;

import com.symbiot.base.BaseTest;
import com.symbiot.listeners.RetryAnalyzer;
import com.symbiot.pages.HomePage;
import com.symbiot.pages.LoginPage;
import com.symbiot.utils.Constants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for Logout functionality.
 */
@Epic("SymBIoT Application")
@Feature("Logout")
public class LogoutTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setUpTest() {
        loginPage = new LoginPage(getPage());
        loginPage.openApplication();

        // Login before each logout test
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }
        homePage = loginPage.login(config.getDefaultUsername(), config.getDefaultPassword());
    }

    @Test(
            description = "Verify successful logout",
            groups = {Constants.SMOKE, Constants.LOGOUT, Constants.POSITIVE},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("User Logout")
    @Description("Verify user can logout successfully")
    public void testSuccessfulLogout() {
        logger.info("Starting test: testSuccessfulLogout");

        // Verify user is logged in
        Assert.assertTrue(
                homePage.isHomePageDisplayed() || homePage.isUserLoggedIn(),
                "User should be logged in before logout test"
        );

        // Perform logout
        LoginPage logoutPage = homePage.logout();

        // Verify user is logged out and redirected to login page
        Assert.assertTrue(
                logoutPage.isLoginPageDisplayed() || !logoutPage.getCurrentUrl().contains("dashboard"),
                "User should be redirected to login page after logout"
        );

        logger.info("Logout successful");
    }

    @Test(
            description = "Verify session is cleared after logout",
            groups = {Constants.REGRESSION, Constants.LOGOUT},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Logout")
    @Description("Verify user session is properly cleared after logout")
    public void testSessionClearedAfterLogout() {
        logger.info("Starting test: testSessionClearedAfterLogout");

        // Verify user is logged in
        Assert.assertTrue(
                homePage.isHomePageDisplayed() || homePage.isUserLoggedIn(),
                "User should be logged in before logout test"
        );

        // Perform logout
        LoginPage logoutPage = homePage.logout();

        // Try to navigate to a protected page
        navigateTo("/dashboard");

        // Verify user cannot access protected page and is redirected to login
        String currentUrl = getPage().url();
        Assert.assertTrue(
                currentUrl.contains("login") || logoutPage.isLoginPageDisplayed(),
                "User should be redirected to login when accessing protected page after logout"
        );

        logger.info("Session correctly cleared after logout");
    }

    @Test(
            description = "Verify browser back button after logout does not restore session",
            groups = {Constants.REGRESSION, Constants.LOGOUT},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Session Security")
    @Description("Verify using browser back button after logout does not restore user session")
    public void testBackButtonAfterLogout() {
        logger.info("Starting test: testBackButtonAfterLogout");

        // Get URL before logout
        String dashboardUrl = homePage.getCurrentUrl();

        // Perform logout
        LoginPage logoutPage = homePage.logout();

        // Use browser back button
        getPage().goBack();

        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify user is not logged in (either redirected to login or page shows logged out state)
        String currentUrl = getPage().url();
        boolean isStillOnLoginPage = logoutPage.isLoginPageDisplayed();
        boolean isOnDashboard = currentUrl.equals(dashboardUrl);

        // If user is back on dashboard URL, verify they're not actually logged in
        if (isOnDashboard) {
            // Refresh to ensure no cached content
            getPage().reload();
            // Should be redirected to login or see login page
            Assert.assertTrue(
                    loginPage.isLoginPageDisplayed() || getPage().url().contains("login"),
                    "User should not have access to protected content after logout"
            );
        }

        logger.info("Browser back button security verified");
    }

    @Test(
            description = "Verify logout button is accessible from home page",
            groups = {Constants.UI, Constants.LOGOUT},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Logout UI")
    @Description("Verify logout button/link is visible and accessible")
    public void testLogoutButtonAccessibility() {
        logger.info("Starting test: testLogoutButtonAccessibility");

        // Verify user is logged in
        Assert.assertTrue(
                homePage.isHomePageDisplayed() || homePage.isUserLoggedIn(),
                "User should be logged in"
        );

        // Verify user profile menu is displayed
        Assert.assertTrue(
                homePage.isUserProfileMenuDisplayed(),
                "User profile menu should be displayed"
        );

        // Verify logout button is accessible
        Assert.assertTrue(
                homePage.isLogoutButtonDisplayed(),
                "Logout button should be accessible from user menu"
        );

        logger.info("Logout button is accessible");
    }

    @Test(
            description = "Verify user can login again after logout",
            groups = {Constants.REGRESSION, Constants.LOGOUT},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User Logout")
    @Description("Verify user can successfully login again after logging out")
    public void testLoginAfterLogout() {
        logger.info("Starting test: testLoginAfterLogout");

        // Perform logout
        LoginPage logoutPage = homePage.logout();

        // Verify user is on login page
        Assert.assertTrue(
                logoutPage.isLoginPageDisplayed(),
                "User should be on login page after logout"
        );

        // Login again
        HomePage newHomePage = logoutPage.login(
                config.getDefaultUsername(),
                config.getDefaultPassword()
        );

        // Verify user is logged in again
        Assert.assertTrue(
                newHomePage.isHomePageDisplayed() || newHomePage.isUserLoggedIn(),
                "User should be able to login again after logout"
        );

        logger.info("User successfully logged in again after logout");
    }

    @Test(
            description = "Verify multiple logout attempts are handled gracefully",
            groups = {Constants.REGRESSION, Constants.LOGOUT},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("User Logout")
    @Description("Verify application handles multiple logout attempts gracefully")
    public void testMultipleLogoutAttempts() {
        logger.info("Starting test: testMultipleLogoutAttempts");

        // Perform first logout
        LoginPage logoutPage = homePage.logout();

        // Try to navigate to logout URL directly again
        String logoutUrl = config.getBaseUrl() + "/logout";
        navigateTo("/logout");

        // Verify no errors - should either stay on login or redirect
        String currentUrl = getPage().url();
        Assert.assertFalse(
                currentUrl.contains("error") || currentUrl.contains("500"),
                "Multiple logout attempts should be handled gracefully"
        );

        logger.info("Multiple logout attempts handled gracefully");
    }
}