package com.symbiot.base;

import com.microsoft.playwright.*;
import com.symbiot.config.BrowserConfig;
import com.symbiot.config.ConfigManager;
import com.symbiot.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;

/**
 * Base test class for all test classes.
 * Handles Playwright browser lifecycle and provides common test setup/teardown.
 */
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Thread-local storage for parallel execution support
    protected static ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    protected ConfigManager config;
    private boolean recordVideo;

    /**
     * Suite-level setup - runs once before all tests.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("========================================");
        logger.info("Starting Test Suite Execution");
        logger.info("========================================");
        config = ConfigManager.getInstance();
        logger.info("Base URL: {}", config.getBaseUrl());
        logger.info("Browser: {}", config.getBrowser());
        logger.info("Headless: {}", config.isHeadless());
    }

    /**
     * Test-level setup - runs before each test method.
     * Initializes Playwright, Browser, Context, and Page.
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String browser) {
        logger.info("----------------------------------------");
        logger.info("Setting up test environment");

        config = ConfigManager.getInstance();
        recordVideo = config.isVideoOnFailure();

        // Initialize Playwright
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);
        logger.debug("Playwright instance created");

        // Determine browser type
        BrowserConfig.BrowserType browserType;
        if (browser != null && !browser.isEmpty()) {
            browserType = BrowserConfig.BrowserType.valueOf(browser.toUpperCase());
        } else {
            browserType = BrowserConfig.BrowserType.valueOf(config.getBrowser().toUpperCase());
        }

        // Initialize Browser
        Browser browserInstance = BrowserConfig.createBrowser(playwright, browserType);
        browserThreadLocal.set(browserInstance);

        // Initialize Context with tracing
        BrowserContext context = BrowserConfig.createContext(browserInstance, recordVideo);
        contextThreadLocal.set(context);

        // Start tracing for debugging
        if (config.isTraceOnFailure()) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
            logger.debug("Tracing started");
        }

        // Initialize Page
        Page page = BrowserConfig.createPage(context);
        pageThreadLocal.set(page);

        logger.info("Test environment setup complete - Browser: {}", browserType);
    }

    /**
     * Test-level teardown - runs after each test method.
     * Captures screenshots/videos on failure and closes browser.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        logger.info("Tearing down test environment");

        try {
            // Handle test failure
            if (result.getStatus() == ITestResult.FAILURE) {
                handleTestFailure(result);
            }

            // Close Page
            Page page = pageThreadLocal.get();
            if (page != null) {
                page.close();
                logger.debug("Page closed");
            }

            // Stop tracing and save if test failed
            BrowserContext context = contextThreadLocal.get();
            if (context != null) {
                if (config.isTraceOnFailure() && result.getStatus() == ITestResult.FAILURE) {
                    String tracePath = "target/traces/" + result.getName() + "-trace.zip";
                    context.tracing().stop(new Tracing.StopOptions()
                            .setPath(Paths.get(tracePath)));
                    logger.info("Trace saved to: {}", tracePath);
                } else {
                    context.tracing().stop();
                }
                context.close();
                logger.debug("Browser context closed");
            }

            // Close Browser
            Browser browser = browserThreadLocal.get();
            if (browser != null) {
                browser.close();
                logger.debug("Browser closed");
            }

            // Close Playwright
            Playwright playwright = playwrightThreadLocal.get();
            if (playwright != null) {
                playwright.close();
                logger.debug("Playwright closed");
            }

        } catch (Exception e) {
            logger.error("Error during teardown: {}", e.getMessage(), e);
        } finally {
            // Clear thread locals
            pageThreadLocal.remove();
            contextThreadLocal.remove();
            browserThreadLocal.remove();
            playwrightThreadLocal.remove();
        }

        logger.info("Test environment teardown complete");
        logger.info("----------------------------------------");
    }

    /**
     * Suite-level teardown - runs once after all tests.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("========================================");
        logger.info("Test Suite Execution Completed");
        logger.info("========================================");
    }

    /**
     * Handle test failure - capture screenshots, videos, and attach to report.
     * @param result Test result
     */
    private void handleTestFailure(ITestResult result) {
        logger.error("Test FAILED: {}", result.getName());
        logger.error("Failure reason: {}", result.getThrowable() != null ?
                result.getThrowable().getMessage() : "Unknown");

        Page page = getPage();
        if (page != null && config.isScreenshotOnFailure()) {
            try {
                // Capture screenshot
                byte[] screenshot = ScreenshotUtils.captureScreenshot(page);
                String screenshotPath = ScreenshotUtils.saveScreenshot(
                        screenshot,
                        result.getName(),
                        "target/screenshots"
                );
                logger.info("Screenshot saved: {}", screenshotPath);

                // Attach screenshot to Allure report
                Allure.addAttachment(
                        "Screenshot - " + result.getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png"
                );

            } catch (Exception e) {
                logger.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Get current Page instance for this thread.
     * @return Page instance
     */
    protected Page getPage() {
        return pageThreadLocal.get();
    }

    /**
     * Get current BrowserContext instance for this thread.
     * @return BrowserContext instance
     */
    protected BrowserContext getContext() {
        return contextThreadLocal.get();
    }

    /**
     * Get current Browser instance for this thread.
     * @return Browser instance
     */
    protected Browser getBrowser() {
        return browserThreadLocal.get();
    }

    /**
     * Get current Playwright instance for this thread.
     * @return Playwright instance
     */
    protected Playwright getPlaywright() {
        return playwrightThreadLocal.get();
    }

    /**
     * Navigate to the base URL.
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        logger.info("Navigating to: {}", baseUrl);
        getPage().navigate(baseUrl);
    }

    /**
     * Navigate to a specific path relative to base URL.
     * @param path Relative path
     */
    protected void navigateTo(String path) {
        String fullUrl = config.getBaseUrl() + path;
        logger.info("Navigating to: {}", fullUrl);
        getPage().navigate(fullUrl);
    }

    /**
     * Get page title.
     * @return Current page title
     */
    protected String getPageTitle() {
        return getPage().title();
    }

    /**
     * Get current URL.
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return getPage().url();
    }
}