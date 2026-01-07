package com.symbiot.base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.symbiot.config.ConfigManager;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base page class for all Page Object classes.
 * Provides common methods for page interactions.
 */
public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final Page page;
    protected final ConfigManager config;

    /**
     * Constructor for BasePage.
     * @param page Playwright Page instance
     */
    public BasePage(Page page) {
        this.page = page;
        this.config = ConfigManager.getInstance();
    }

    /**
     * Navigate to a URL.
     * @param url URL to navigate to
     */
    @Step("Navigate to URL: {url}")
    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        page.navigate(url);
        waitForPageLoad();
    }

    /**
     * Wait for page to fully load.
     */
    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logger.debug("Page load complete");
    }

    /**
     * Click on an element.
     * @param selector Element selector
     */
    @Step("Click on element: {selector}")
    protected void click(String selector) {
        logger.debug("Clicking on element: {}", selector);
        page.locator(selector).click();
    }

    /**
     * Click on a locator.
     * @param locator Playwright Locator
     */
    @Step("Click on element")
    protected void click(Locator locator) {
        logger.debug("Clicking on locator");
        locator.click();
    }

    /**
     * Double-click on an element.
     * @param selector Element selector
     */
    @Step("Double-click on element: {selector}")
    protected void doubleClick(String selector) {
        logger.debug("Double-clicking on element: {}", selector);
        page.locator(selector).dblclick();
    }

    /**
     * Type text into an element.
     * @param selector Element selector
     * @param text Text to type
     */
    @Step("Type '{text}' into element: {selector}")
    protected void type(String selector, String text) {
        logger.debug("Typing '{}' into element: {}", text, selector);
        page.locator(selector).fill(text);
    }

    /**
     * Type text into a locator.
     * @param locator Playwright Locator
     * @param text Text to type
     */
    @Step("Type '{text}' into element")
    protected void type(Locator locator, String text) {
        logger.debug("Typing '{}' into locator", text);
        locator.fill(text);
    }

    /**
     * Clear and type text into an element.
     * @param selector Element selector
     * @param text Text to type
     */
    @Step("Clear and type '{text}' into element: {selector}")
    protected void clearAndType(String selector, String text) {
        logger.debug("Clearing and typing '{}' into element: {}", text, selector);
        Locator locator = page.locator(selector);
        locator.clear();
        locator.fill(text);
    }

    /**
     * Get text from an element.
     * @param selector Element selector
     * @return Element text content
     */
    @Step("Get text from element: {selector}")
    protected String getText(String selector) {
        String text = page.locator(selector).textContent();
        logger.debug("Got text from element {}: {}", selector, text);
        return text;
    }

    /**
     * Get text from a locator.
     * @param locator Playwright Locator
     * @return Element text content
     */
    @Step("Get text from element")
    protected String getText(Locator locator) {
        String text = locator.textContent();
        logger.debug("Got text from locator: {}", text);
        return text;
    }

    /**
     * Get attribute value from an element.
     * @param selector Element selector
     * @param attribute Attribute name
     * @return Attribute value
     */
    @Step("Get attribute '{attribute}' from element: {selector}")
    protected String getAttribute(String selector, String attribute) {
        String value = page.locator(selector).getAttribute(attribute);
        logger.debug("Got attribute '{}' from element {}: {}", attribute, selector, value);
        return value;
    }

    /**
     * Get input value from an element.
     * @param selector Element selector
     * @return Input value
     */
    @Step("Get input value from element: {selector}")
    protected String getInputValue(String selector) {
        String value = page.locator(selector).inputValue();
        logger.debug("Got input value from element {}: {}", selector, value);
        return value;
    }

    /**
     * Check if an element is visible.
     * @param selector Element selector
     * @return true if element is visible
     */
    @Step("Check if element is visible: {selector}")
    protected boolean isVisible(String selector) {
        boolean visible = page.locator(selector).isVisible();
        logger.debug("Element {} is visible: {}", selector, visible);
        return visible;
    }

    /**
     * Check if an element is enabled.
     * @param selector Element selector
     * @return true if element is enabled
     */
    @Step("Check if element is enabled: {selector}")
    protected boolean isEnabled(String selector) {
        boolean enabled = page.locator(selector).isEnabled();
        logger.debug("Element {} is enabled: {}", selector, enabled);
        return enabled;
    }

    /**
     * Check if an element is checked (for checkboxes/radio buttons).
     * @param selector Element selector
     * @return true if element is checked
     */
    @Step("Check if element is checked: {selector}")
    protected boolean isChecked(String selector) {
        boolean checked = page.locator(selector).isChecked();
        logger.debug("Element {} is checked: {}", selector, checked);
        return checked;
    }

    /**
     * Wait for element to be visible.
     * @param selector Element selector
     */
    @Step("Wait for element to be visible: {selector}")
    protected void waitForVisible(String selector) {
        logger.debug("Waiting for element to be visible: {}", selector);
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
    }

    /**
     * Wait for element to be visible with custom timeout.
     * @param selector Element selector
     * @param timeout Timeout in milliseconds
     */
    @Step("Wait for element to be visible: {selector}")
    protected void waitForVisible(String selector, int timeout) {
        logger.debug("Waiting for element to be visible: {} (timeout: {}ms)", selector, timeout);
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }

    /**
     * Wait for element to be hidden.
     * @param selector Element selector
     */
    @Step("Wait for element to be hidden: {selector}")
    protected void waitForHidden(String selector) {
        logger.debug("Waiting for element to be hidden: {}", selector);
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }

    /**
     * Wait for element to be attached to DOM.
     * @param selector Element selector
     */
    @Step("Wait for element to be attached: {selector}")
    protected void waitForAttached(String selector) {
        logger.debug("Waiting for element to be attached: {}", selector);
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
    }

    /**
     * Hover over an element.
     * @param selector Element selector
     */
    @Step("Hover over element: {selector}")
    protected void hover(String selector) {
        logger.debug("Hovering over element: {}", selector);
        page.locator(selector).hover();
    }

    /**
     * Select option from dropdown by value.
     * @param selector Element selector
     * @param value Option value
     */
    @Step("Select option '{value}' from dropdown: {selector}")
    protected void selectByValue(String selector, String value) {
        logger.debug("Selecting value '{}' from dropdown: {}", value, selector);
        page.locator(selector).selectOption(value);
    }

    /**
     * Select option from dropdown by label.
     * @param selector Element selector
     * @param label Option label
     */
    @Step("Select option by label '{label}' from dropdown: {selector}")
    protected void selectByLabel(String selector, String label) {
        logger.debug("Selecting label '{}' from dropdown: {}", label, selector);
        page.locator(selector).selectOption(new SelectOption().setLabel(label));
    }

    /**
     * Select option from dropdown by index.
     * @param selector Element selector
     * @param index Option index
     */
    @Step("Select option by index {index} from dropdown: {selector}")
    protected void selectByIndex(String selector, int index) {
        logger.debug("Selecting index {} from dropdown: {}", index, selector);
        page.locator(selector).selectOption(new SelectOption().setIndex(index));
    }

    /**
     * Check a checkbox.
     * @param selector Element selector
     */
    @Step("Check checkbox: {selector}")
    protected void check(String selector) {
        logger.debug("Checking checkbox: {}", selector);
        page.locator(selector).check();
    }

    /**
     * Uncheck a checkbox.
     * @param selector Element selector
     */
    @Step("Uncheck checkbox: {selector}")
    protected void uncheck(String selector) {
        logger.debug("Unchecking checkbox: {}", selector);
        page.locator(selector).uncheck();
    }

    /**
     * Press a keyboard key.
     * @param key Key to press (e.g., "Enter", "Tab", "Escape")
     */
    @Step("Press key: {key}")
    protected void pressKey(String key) {
        logger.debug("Pressing key: {}", key);
        page.keyboard().press(key);
    }

    /**
     * Get page title.
     * @return Page title
     */
    @Step("Get page title")
    public String getPageTitle() {
        String title = page.title();
        logger.debug("Page title: {}", title);
        return title;
    }

    /**
     * Get current URL.
     * @return Current URL
     */
    @Step("Get current URL")
    public String getCurrentUrl() {
        String url = page.url();
        logger.debug("Current URL: {}", url);
        return url;
    }

    /**
     * Refresh the page.
     */
    @Step("Refresh page")
    protected void refresh() {
        logger.debug("Refreshing page");
        page.reload();
        waitForPageLoad();
    }

    /**
     * Go back in browser history.
     */
    @Step("Navigate back")
    protected void goBack() {
        logger.debug("Navigating back");
        page.goBack();
    }

    /**
     * Go forward in browser history.
     */
    @Step("Navigate forward")
    protected void goForward() {
        logger.debug("Navigating forward");
        page.goForward();
    }

    /**
     * Accept alert/dialog.
     */
    @Step("Accept dialog")
    protected void acceptDialog() {
        logger.debug("Accepting dialog");
        page.onDialog(dialog -> dialog.accept());
    }

    /**
     * Dismiss alert/dialog.
     */
    @Step("Dismiss dialog")
    protected void dismissDialog() {
        logger.debug("Dismissing dialog");
        page.onDialog(dialog -> dialog.dismiss());
    }

    /**
     * Get element count.
     * @param selector Element selector
     * @return Number of matching elements
     */
    @Step("Get element count: {selector}")
    protected int getElementCount(String selector) {
        int count = page.locator(selector).count();
        logger.debug("Element count for {}: {}", selector, count);
        return count;
    }

    /**
     * Scroll to element.
     * @param selector Element selector
     */
    @Step("Scroll to element: {selector}")
    protected void scrollToElement(String selector) {
        logger.debug("Scrolling to element: {}", selector);
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    /**
     * Take screenshot.
     * @return Screenshot as byte array
     */
    @Step("Take screenshot")
    protected byte[] takeScreenshot() {
        logger.debug("Taking screenshot");
        return page.screenshot();
    }

    /**
     * Wait for URL to contain a specific string.
     * @param urlPart String to check in URL
     */
    @Step("Wait for URL to contain: {urlPart}")
    protected void waitForUrlContains(String urlPart) {
        logger.debug("Waiting for URL to contain: {}", urlPart);
        page.waitForURL("**" + urlPart + "**");
    }

    /**
     * Wait for a specific condition.
     * @param condition Condition to wait for (should return true when condition is met)
     */
    protected void waitFor(java.util.function.BooleanSupplier condition) {
        page.waitForCondition(condition);
    }
}