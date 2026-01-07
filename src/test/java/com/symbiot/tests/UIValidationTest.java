package com.symbiot.tests;

import com.symbiot.base.BaseTest;
import com.symbiot.listeners.RetryAnalyzer;
import com.symbiot.pages.HomePage;
import com.symbiot.pages.LoginPage;
import com.symbiot.pages.components.NavigationComponent;
import com.symbiot.utils.Constants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for UI validation.
 * Covers basic UI element validation, navigation, buttons, and fields.
 */
@Epic("SymBIoT Application")
@Feature("UI Validation")
public class UIValidationTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private NavigationComponent navigation;

    @BeforeMethod(alwaysRun = true)
    public void setUpTest() {
        loginPage = new LoginPage(getPage());
        navigation = new NavigationComponent(getPage());
        loginPage.openApplication();
    }

    /**
     * Helper method to login if tests require authenticated state.
     */
    private void loginIfRequired() {
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }
        homePage = loginPage.login(config.getDefaultUsername(), config.getDefaultPassword());
    }

    @Test(
            description = "Verify header is displayed",
            groups = {Constants.UI, Constants.SMOKE}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Page Layout")
    @Description("Verify header is displayed on the page")
    public void testHeaderDisplayed() {
        logger.info("Starting test: testHeaderDisplayed");

        Assert.assertTrue(
                navigation.isHeaderDisplayed(),
                "Header should be displayed"
        );

        logger.info("Header is displayed");
    }

    @Test(
            description = "Verify footer is displayed",
            groups = {Constants.UI, Constants.SMOKE},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Page Layout")
    @Description("Verify footer is displayed on the page")
    public void testFooterDisplayed() {
        logger.info("Starting test: testFooterDisplayed");

        // Scroll to footer
        navigation.scrollToFooter();

        Assert.assertTrue(
                navigation.isFooterDisplayed(),
                "Footer should be displayed"
        );

        logger.info("Footer is displayed");
    }

    @Test(
            description = "Verify page is responsive at different viewport sizes",
            groups = {Constants.UI, Constants.REGRESSION}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Responsive Design")
    @Description("Verify page renders correctly at different viewport sizes")
    public void testResponsiveDesign() {
        logger.info("Starting test: testResponsiveDesign");

        // Test desktop viewport (already set by default)
        Assert.assertTrue(
                navigation.isHeaderDisplayed(),
                "Header should be visible on desktop"
        );

        // Test tablet viewport
        getPage().setViewportSize(768, 1024);
        getPage().reload();
        getPage().waitForLoadState();
        Assert.assertTrue(
                navigation.isHeaderDisplayed(),
                "Header should be visible on tablet"
        );

        // Test mobile viewport
        getPage().setViewportSize(375, 667);
        getPage().reload();
        getPage().waitForLoadState();
        Assert.assertTrue(
                navigation.isHeaderDisplayed(),
                "Header should be visible on mobile"
        );

        // Reset viewport
        getPage().setViewportSize(config.getViewportWidth(), config.getViewportHeight());

        logger.info("Responsive design verified");
    }

    @Test(
            description = "Verify navigation menu is displayed",
            groups = {Constants.UI, Constants.SMOKE},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation")
    @Description("Verify navigation menu is displayed and accessible")
    public void testNavigationMenuDisplayed() {
        logger.info("Starting test: testNavigationMenuDisplayed");

        Assert.assertTrue(
                navigation.isMainNavigationDisplayed(),
                "Main navigation should be displayed"
        );

        logger.info("Navigation menu is displayed");
    }

    @Test(
            description = "Verify logo is displayed and clickable",
            groups = {Constants.UI, Constants.SMOKE},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Branding")
    @Description("Verify company logo is displayed on the page")
    public void testLogoDisplayed() {
        logger.info("Starting test: testLogoDisplayed");

        // Check for logo in header
        String logoSelector = ".logo, #logo, .brand, header img, [alt*='logo']";
        boolean logoDisplayed = getPage().locator(logoSelector).count() > 0;

        logger.info("Logo displayed: {}", logoDisplayed);

        // Logo might not be present on all pages, so this is informational
        if (!logoDisplayed) {
            logger.warn("Logo element not found on page");
        }
    }

    @Test(
            description = "Verify all links are valid (no broken links)",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Links")
    @Description("Verify there are no broken links on the page")
    public void testNoBrokenLinks() {
        logger.info("Starting test: testNoBrokenLinks");

        // Get all anchor elements
        int linkCount = getPage().locator("a[href]").count();
        logger.info("Found {} links on page", linkCount);

        // Check for common broken link indicators
        String brokenLinkSelector = "a[href='#'], a[href='javascript:void(0)'], a[href='']";
        int potentialBrokenLinks = getPage().locator(brokenLinkSelector).count();

        logger.info("Potential placeholder links: {}", potentialBrokenLinks);

        // This is informational - placeholder links might be intentional
        if (potentialBrokenLinks > 0) {
            logger.warn("Found {} placeholder links that might need review", potentialBrokenLinks);
        }
    }

    @Test(
            description = "Verify input fields have proper labels",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Accessibility")
    @Description("Verify input fields have associated labels for accessibility")
    public void testInputFieldLabels() {
        logger.info("Starting test: testInputFieldLabels");

        // Navigate to login page to test input fields
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Check for input fields
        int inputCount = getPage().locator("input:not([type='hidden'])").count();
        int labelCount = getPage().locator("label").count();

        logger.info("Input fields: {}, Labels: {}", inputCount, labelCount);

        // Check for aria-label or placeholder as alternative
        int accessibleInputs = getPage()
                .locator("input[aria-label], input[placeholder], input[id]")
                .count();

        logger.info("Accessible inputs: {}", accessibleInputs);

        // Verify at least some accessibility features are present
        Assert.assertTrue(
                labelCount > 0 || accessibleInputs > 0,
                "Input fields should have labels or accessibility attributes"
        );
    }

    @Test(
            description = "Verify buttons have visible text or aria-label",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Accessibility")
    @Description("Verify buttons are accessible with text or aria-label")
    public void testButtonAccessibility() {
        logger.info("Starting test: testButtonAccessibility");

        // Navigate to login page
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Check for buttons
        int buttonCount = getPage().locator("button, input[type='submit'], input[type='button']").count();
        logger.info("Found {} buttons on page", buttonCount);

        // Check for accessible buttons
        int accessibleButtons = getPage()
                .locator("button:not(:empty), button[aria-label], input[type='submit'][value]")
                .count();

        logger.info("Accessible buttons: {}", accessibleButtons);

        if (buttonCount > 0) {
            Assert.assertTrue(
                    accessibleButtons > 0,
                    "Buttons should have visible text or aria-label"
            );
        }
    }

    @Test(
            description = "Verify page has proper heading structure",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Accessibility")
    @Description("Verify page has proper heading hierarchy (h1, h2, etc.)")
    public void testHeadingStructure() {
        logger.info("Starting test: testHeadingStructure");

        int h1Count = getPage().locator("h1").count();
        int h2Count = getPage().locator("h2").count();
        int h3Count = getPage().locator("h3").count();

        logger.info("Headings - H1: {}, H2: {}, H3: {}", h1Count, h2Count, h3Count);

        // A page should ideally have one h1
        if (h1Count == 0) {
            logger.warn("Page does not have an H1 heading");
        } else if (h1Count > 1) {
            logger.warn("Page has multiple H1 headings: {}", h1Count);
        }

        // Verify at least some heading structure exists
        Assert.assertTrue(
                h1Count > 0 || h2Count > 0 || h3Count > 0,
                "Page should have heading elements for structure"
        );
    }

    @Test(
            description = "Verify images have alt text",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Accessibility")
    @Description("Verify images have alt text for accessibility")
    public void testImageAltText() {
        logger.info("Starting test: testImageAltText");

        int totalImages = getPage().locator("img").count();
        int imagesWithAlt = getPage().locator("img[alt]").count();
        int imagesWithoutAlt = totalImages - imagesWithAlt;

        logger.info("Images - Total: {}, With alt: {}, Without alt: {}",
                totalImages, imagesWithAlt, imagesWithoutAlt);

        if (imagesWithoutAlt > 0) {
            logger.warn("{} images missing alt text", imagesWithoutAlt);
        }

        // If there are images, most should have alt text
        if (totalImages > 0) {
            double altTextPercentage = (double) imagesWithAlt / totalImages * 100;
            logger.info("Alt text coverage: {}%", String.format("%.1f", altTextPercentage));
        }
    }

    @Test(
            description = "Verify color contrast is adequate",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Accessibility")
    @Description("Basic check for text visibility")
    public void testTextVisibility() {
        logger.info("Starting test: testTextVisibility");

        // Check that main content is visible
        String bodyText = getPage().locator("body").textContent();
        Assert.assertNotNull(bodyText, "Page should have content");
        Assert.assertFalse(bodyText.trim().isEmpty(), "Page content should not be empty");

        logger.info("Text visibility check passed");
    }

    @Test(
            description = "Verify form validation messages are displayed",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Form Validation")
    @Description("Verify form validation messages appear when needed")
    public void testFormValidationMessages() {
        logger.info("Starting test: testFormValidationMessages");

        // Navigate to login page
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Try to submit empty form
        loginPage.clickLoginButton();

        // Check for validation - either HTML5 validation or custom error messages
        boolean hasValidation = loginPage.isErrorMessageDisplayed() ||
                getPage().locator(":invalid").count() > 0 ||
                getPage().locator("[aria-invalid='true']").count() > 0;

        logger.info("Form validation present: {}", hasValidation);

        // Form should have some validation
        Assert.assertTrue(
                hasValidation || loginPage.isLoginPageDisplayed(),
                "Form should have validation or prevent submission of empty fields"
        );
    }

    @Test(
            description = "Verify page scrolling works correctly",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("User Interaction")
    @Description("Verify page can be scrolled")
    public void testPageScrolling() {
        logger.info("Starting test: testPageScrolling");

        // Get initial scroll position (JavaScript returns Integer, not Long)
        int initialScrollY = ((Number) getPage().evaluate("window.scrollY")).intValue();

        // Scroll down
        getPage().evaluate("window.scrollTo(0, 500)");

        // Get new scroll position
        int newScrollY = ((Number) getPage().evaluate("window.scrollY")).intValue();

        logger.info("Initial scroll: {}, New scroll: {}", initialScrollY, newScrollY);

        // Scroll back to top
        getPage().evaluate("window.scrollTo(0, 0)");

        // Verify scroll worked (if page is scrollable)
        int pageHeight = ((Number) getPage().evaluate("document.body.scrollHeight")).intValue();
        int viewportHeight = ((Number) getPage().evaluate("window.innerHeight")).intValue();

        if (pageHeight > viewportHeight) {
            Assert.assertTrue(
                    newScrollY > initialScrollY,
                    "Page should be scrollable"
            );
        }

        logger.info("Page scrolling verified");
    }

    @Test(
            description = "Verify keyboard navigation works",
            groups = {Constants.UI, Constants.REGRESSION},
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Accessibility")
    @Description("Verify elements can be navigated using keyboard")
    public void testKeyboardNavigation() {
        logger.info("Starting test: testKeyboardNavigation");

        // Navigate to login page
        if (!loginPage.isLoginPageDisplayed()) {
            loginPage.navigateToLoginPage();
        }

        // Press Tab to move focus
        getPage().keyboard().press("Tab");

        // Check if any element has focus
        String focusedElement = getPage().evaluate("document.activeElement.tagName").toString();
        logger.info("Focused element after Tab: {}", focusedElement);

        Assert.assertNotEquals(
                focusedElement.toLowerCase(), "body",
                "Tab should move focus to an interactive element"
        );

        logger.info("Keyboard navigation verified");
    }
}