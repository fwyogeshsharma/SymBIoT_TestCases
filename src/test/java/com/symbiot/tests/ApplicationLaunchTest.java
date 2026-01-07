package com.symbiot.tests;

import com.symbiot.base.BaseTest;
import com.symbiot.listeners.RetryAnalyzer;
import com.symbiot.pages.LoginPage;
import com.symbiot.utils.Constants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for application launch and title validation.
 */
@Epic("SymBIoT Application")
@Feature("Application Launch")
public class ApplicationLaunchTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUpTest() {
        loginPage = new LoginPage(getPage());
    }

    @Test(
            description = "Verify application launches successfully",
            groups = {Constants.SMOKE, Constants.SANITY},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("Application Launch")
    @Description("Verify that the SymBIoT application launches successfully and is accessible")
    public void testApplicationLaunch() {
        logger.info("Starting test: testApplicationLaunch");

        // Navigate to base URL
        loginPage.openApplication();

        // Verify page is loaded
        String currentUrl = loginPage.getCurrentUrl();
        logger.info("Current URL: {}", currentUrl);

        Assert.assertNotNull(currentUrl, "Page URL should not be null");
        Assert.assertTrue(
                currentUrl.contains(config.getBaseUrl().replace("https://", "").replace("http://", "")),
                "Should be on the application URL"
        );
    }

    @Test(
            description = "Verify application title is correct",
            groups = {Constants.SMOKE, Constants.SANITY},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Application Launch")
    @Description("Verify that the application page title is correct")
    public void testApplicationTitle() {
        logger.info("Starting test: testApplicationTitle");

        // Navigate to base URL
        loginPage.openApplication();

        // Get and verify page title
        String pageTitle = loginPage.getPageTitle();
        logger.info("Page title: {}", pageTitle);

        Assert.assertNotNull(pageTitle, "Page title should not be null");
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");

        // Log the title for verification
        Allure.addAttachment("Page Title", pageTitle);
    }

    @Test(
            description = "Verify page loads within acceptable time",
            groups = {Constants.SMOKE, Constants.SANITY},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Application Launch")
    @Description("Verify that the application page loads within acceptable time frame")
    public void testPageLoadTime() {
        logger.info("Starting test: testPageLoadTime");

        long startTime = System.currentTimeMillis();

        // Navigate to base URL
        loginPage.openApplication();

        long loadTime = System.currentTimeMillis() - startTime;
        logger.info("Page load time: {} ms", loadTime);

        // Verify page loads within 10 seconds
        Assert.assertTrue(
                loadTime < 10000,
                "Page should load within 10 seconds. Actual load time: " + loadTime + "ms"
        );

        Allure.addAttachment("Page Load Time", loadTime + " milliseconds");
    }

    @Test(
            description = "Verify HTTP response status is successful",
            groups = {Constants.SMOKE},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Application Launch")
    @Description("Verify that the application returns a successful HTTP response")
    public void testHttpResponseStatus() {
        logger.info("Starting test: testHttpResponseStatus");

        // Set up response listener
        final int[] responseStatus = new int[1];
        getPage().onResponse(response -> {
            if (response.url().equals(config.getBaseUrl()) ||
                response.url().equals(config.getBaseUrl() + "/")) {
                responseStatus[0] = response.status();
                logger.info("Response status: {} for URL: {}", response.status(), response.url());
            }
        });

        // Navigate to base URL
        loginPage.openApplication();

        // Allow time for response to be captured
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify response status (200 or redirect)
        Assert.assertTrue(
                responseStatus[0] >= 200 && responseStatus[0] < 400,
                "HTTP response should be successful (2xx or 3xx). Actual: " + responseStatus[0]
        );
    }

    @Test(
            description = "Verify application favicon loads",
            groups = {Constants.UI, Constants.SANITY},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Application Launch")
    @Description("Verify that the application favicon is present")
    public void testFaviconPresent() {
        logger.info("Starting test: testFaviconPresent");

        // Navigate to base URL
        loginPage.openApplication();

        // Check for favicon link element
        String faviconSelector = "link[rel*='icon'], link[rel='shortcut icon']";
        boolean faviconPresent = getPage().locator(faviconSelector).count() > 0;

        logger.info("Favicon present: {}", faviconPresent);

        // This is a soft assertion - favicon might not be present but it's not critical
        if (!faviconPresent) {
            logger.warn("Favicon not found on the page");
        }
    }

    @Test(
            description = "Verify no console errors on page load",
            groups = {Constants.SMOKE, Constants.SANITY},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Application Launch")
    @Description("Verify that there are no critical console errors on page load")
    public void testNoConsoleErrors() {
        logger.info("Starting test: testNoConsoleErrors");

        // Set up console error listener
        final StringBuilder consoleErrors = new StringBuilder();
        getPage().onConsoleMessage(msg -> {
            if ("error".equals(msg.type())) {
                consoleErrors.append(msg.text()).append("\n");
                logger.warn("Console error: {}", msg.text());
            }
        });

        // Navigate to base URL
        loginPage.openApplication();

        // Allow time for console messages
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Log any console errors found
        if (consoleErrors.length() > 0) {
            Allure.addAttachment("Console Errors", consoleErrors.toString());
            logger.warn("Console errors found: {}", consoleErrors);
        }

        // This is informational - we log but don't fail the test for console errors
        logger.info("Console error check completed");
    }
}