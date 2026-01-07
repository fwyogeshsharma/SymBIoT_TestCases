package com.symbiot.config;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Browser configuration and factory class.
 * Handles creation of Playwright browser instances with proper configuration.
 */
public class BrowserConfig {

    private static final Logger logger = LogManager.getLogger(BrowserConfig.class);

    /**
     * Supported browser types.
     */
    public enum BrowserType {
        CHROMIUM,
        FIREFOX,
        WEBKIT
    }

    /**
     * Create a new browser instance based on configuration.
     * @param playwright Playwright instance
     * @return Configured Browser instance
     */
    public static Browser createBrowser(Playwright playwright) {
        ConfigManager config = ConfigManager.getInstance();
        String browserName = config.getBrowser().toUpperCase();
        BrowserType browserType;

        try {
            browserType = BrowserType.valueOf(browserName);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid browser type: {}. Defaulting to CHROMIUM", browserName);
            browserType = BrowserType.CHROMIUM;
        }

        return createBrowser(playwright, browserType);
    }

    /**
     * Create a new browser instance of specified type.
     * @param playwright Playwright instance
     * @param browserType Type of browser to create
     * @return Configured Browser instance
     */
    public static Browser createBrowser(Playwright playwright, BrowserType browserType) {
        ConfigManager config = ConfigManager.getInstance();
        com.microsoft.playwright.BrowserType.LaunchOptions options = buildLaunchOptions(config);

        Browser browser;
        switch (browserType) {
            case FIREFOX:
                browser = playwright.firefox().launch(options);
                logger.info("Launched Firefox browser");
                break;
            case WEBKIT:
                browser = playwright.webkit().launch(options);
                logger.info("Launched WebKit browser");
                break;
            case CHROMIUM:
            default:
                browser = playwright.chromium().launch(options);
                logger.info("Launched Chromium browser");
                break;
        }

        return browser;
    }

    /**
     * Build launch options from configuration.
     * @param config ConfigManager instance
     * @return Configured LaunchOptions
     */
    private static com.microsoft.playwright.BrowserType.LaunchOptions buildLaunchOptions(ConfigManager config) {
        com.microsoft.playwright.BrowserType.LaunchOptions options = new com.microsoft.playwright.BrowserType.LaunchOptions();

        options.setHeadless(config.isHeadless());
        options.setSlowMo(config.getSlowMotion());

        // Add common Chrome args for better stability
        if (config.getBrowser().equalsIgnoreCase("chromium")) {
            options.setArgs(Arrays.asList(
                    "--disable-dev-shm-usage",
                    "--no-sandbox",
                    "--disable-gpu",
                    "--disable-extensions"
            ));
        }

        logger.debug("Browser launch options configured - Headless: {}, SlowMo: {}",
                config.isHeadless(), config.getSlowMotion());

        return options;
    }

    /**
     * Create a new browser context with specified configuration.
     * @param browser Browser instance
     * @param recordVideo Whether to record video
     * @return Configured BrowserContext
     */
    public static BrowserContext createContext(Browser browser, boolean recordVideo) {
        ConfigManager config = ConfigManager.getInstance();
        Browser.NewContextOptions options = buildContextOptions(config, recordVideo);
        return browser.newContext(options);
    }

    /**
     * Build context options from configuration.
     * @param config ConfigManager instance
     * @param recordVideo Whether to record video
     * @return Configured NewContextOptions
     */
    private static Browser.NewContextOptions buildContextOptions(ConfigManager config, boolean recordVideo) {
        Browser.NewContextOptions options = new Browser.NewContextOptions();

        // Set viewport size
        options.setViewportSize(config.getViewportWidth(), config.getViewportHeight());

        // Set locale and timezone
        options.setLocale("en-US");
        options.setTimezoneId("America/New_York");

        // Enable video recording if requested
        if (recordVideo) {
            options.setRecordVideoDir(Paths.get("target/videos/"));
            options.setRecordVideoSize(config.getViewportWidth(), config.getViewportHeight());
            logger.debug("Video recording enabled");
        }

        // Ignore HTTPS errors (useful for test environments)
        options.setIgnoreHTTPSErrors(true);

        logger.debug("Browser context options configured - Viewport: {}x{}",
                config.getViewportWidth(), config.getViewportHeight());

        return options;
    }

    /**
     * Create a new page with default timeout configurations.
     * @param context Browser context
     * @return Configured Page instance
     */
    public static Page createPage(BrowserContext context) {
        ConfigManager config = ConfigManager.getInstance();
        Page page = context.newPage();

        // Set default timeout
        page.setDefaultTimeout(config.getDefaultTimeout());
        page.setDefaultNavigationTimeout(config.getNavigationTimeout());

        logger.debug("Page created with default timeout: {}ms, navigation timeout: {}ms",
                config.getDefaultTimeout(), config.getNavigationTimeout());

        return page;
    }
}