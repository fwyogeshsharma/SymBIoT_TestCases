package com.symbiot.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.symbiot.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BooleanSupplier;

/**
 * Utility class for wait operations.
 * Uses Playwright's built-in wait mechanisms - avoids hard-coded waits.
 */
public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Wait for element to be visible.
     * @param page Playwright Page instance
     * @param selector Element selector
     */
    public static void waitForVisible(Page page, String selector) {
        waitForVisible(page, selector, config.getDefaultTimeout());
    }

    /**
     * Wait for element to be visible with custom timeout.
     * @param page Playwright Page instance
     * @param selector Element selector
     * @param timeout Timeout in milliseconds
     */
    public static void waitForVisible(Page page, String selector, int timeout) {
        logger.debug("Waiting for element to be visible: {} (timeout: {}ms)", selector, timeout);
        page.locator(selector).waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }

    /**
     * Wait for element to be hidden.
     * @param page Playwright Page instance
     * @param selector Element selector
     */
    public static void waitForHidden(Page page, String selector) {
        waitForHidden(page, selector, config.getDefaultTimeout());
    }

    /**
     * Wait for element to be hidden with custom timeout.
     * @param page Playwright Page instance
     * @param selector Element selector
     * @param timeout Timeout in milliseconds
     */
    public static void waitForHidden(Page page, String selector, int timeout) {
        logger.debug("Waiting for element to be hidden: {} (timeout: {}ms)", selector, timeout);
        page.locator(selector).waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeout));
    }

    /**
     * Wait for element to be attached to DOM.
     * @param page Playwright Page instance
     * @param selector Element selector
     */
    public static void waitForAttached(Page page, String selector) {
        logger.debug("Waiting for element to be attached: {}", selector);
        page.locator(selector).waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
    }

    /**
     * Wait for element to be detached from DOM.
     * @param page Playwright Page instance
     * @param selector Element selector
     */
    public static void waitForDetached(Page page, String selector) {
        logger.debug("Waiting for element to be detached: {}", selector);
        page.locator(selector).waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                .setState(WaitForSelectorState.DETACHED));
    }

    /**
     * Wait for page to fully load.
     * @param page Playwright Page instance
     */
    public static void waitForPageLoad(Page page) {
        logger.debug("Waiting for page to load");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.LOAD);
    }

    /**
     * Wait for network to be idle.
     * @param page Playwright Page instance
     */
    public static void waitForNetworkIdle(Page page) {
        logger.debug("Waiting for network idle");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Wait for URL to contain a specific string.
     * @param page Playwright Page instance
     * @param urlPart String that URL should contain
     */
    public static void waitForUrlContains(Page page, String urlPart) {
        logger.debug("Waiting for URL to contain: {}", urlPart);
        page.waitForURL("**" + urlPart + "**");
    }

    /**
     * Wait for URL to match exactly.
     * @param page Playwright Page instance
     * @param url Expected URL
     */
    public static void waitForUrl(Page page, String url) {
        logger.debug("Waiting for URL: {}", url);
        page.waitForURL(url);
    }

    /**
     * Wait for a custom condition to be true.
     * @param page Playwright Page instance
     * @param condition BooleanSupplier that returns true when condition is met
     * @param timeout Timeout in milliseconds
     * @param pollingInterval Polling interval in milliseconds
     */
    public static void waitForCondition(Page page, BooleanSupplier condition, int timeout, int pollingInterval) {
        logger.debug("Waiting for custom condition (timeout: {}ms)", timeout);
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout) {
            if (condition.getAsBoolean()) {
                logger.debug("Condition met after {}ms", System.currentTimeMillis() - startTime);
                return;
            }
            try {
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
        throw new RuntimeException("Timeout waiting for condition after " + timeout + "ms");
    }

    /**
     * Wait for element text to contain a specific string.
     * @param page Playwright Page instance
     * @param selector Element selector
     * @param expectedText Expected text
     */
    public static void waitForTextContains(Page page, String selector, String expectedText) {
        logger.debug("Waiting for element {} to contain text: {}", selector, expectedText);
        page.locator(selector).filter(new com.microsoft.playwright.Locator.FilterOptions()
                .setHasText(expectedText)).waitFor();
    }

    /**
     * Wait for element to be enabled.
     * @param page Playwright Page instance
     * @param selector Element selector
     */
    public static void waitForEnabled(Page page, String selector) {
        logger.debug("Waiting for element to be enabled: {}", selector);
        waitForCondition(page,
                () -> page.locator(selector).isEnabled(),
                config.getDefaultTimeout(),
                100);
    }

    /**
     * Wait for element count to match expected value.
     * @param page Playwright Page instance
     * @param selector Element selector
     * @param expectedCount Expected number of elements
     */
    public static void waitForElementCount(Page page, String selector, int expectedCount) {
        logger.debug("Waiting for {} elements matching: {}", expectedCount, selector);
        waitForCondition(page,
                () -> page.locator(selector).count() == expectedCount,
                config.getDefaultTimeout(),
                100);
    }

    /**
     * Wait for API response matching the pattern during an action.
     * @param page Playwright Page instance
     * @param urlPattern URL pattern to match
     * @param action Action that triggers the response
     */
    public static void waitForResponse(Page page, String urlPattern, Runnable action) {
        logger.debug("Waiting for response matching: {}", urlPattern);
        page.waitForResponse(urlPattern, action);
    }

    /**
     * Wait for API response matching the pattern (waits for next matching response).
     * @param page Playwright Page instance
     * @param urlPattern URL pattern to match
     */
    public static void waitForResponse(Page page, String urlPattern) {
        logger.debug("Waiting for response matching: {}", urlPattern);
        page.waitForResponse(urlPattern, () -> {});
    }

    /**
     * Wait for navigation to complete.
     * @param page Playwright Page instance
     * @param action Action that triggers navigation
     */
    public static void waitForNavigation(Page page, Runnable action) {
        logger.debug("Waiting for navigation");
        page.waitForNavigation(() -> action.run());
    }
}