package com.symbiot.pages.components;

import com.microsoft.playwright.Page;
import com.symbiot.base.BasePage;
import io.qameta.allure.Step;

/**
 * Page component for navigation elements.
 * Reusable across multiple pages.
 */
public class NavigationComponent extends BasePage {

    // Navigation selectors - Update based on actual application structure
    // Using broader selectors to match various common header/nav patterns
    private static final String HEADER = "header, .header, #header, [role='banner'], .navbar, .top-bar, .app-header";
    private static final String FOOTER = "footer, .footer, #footer, [role='contentinfo'], .app-footer";
    private static final String MAIN_NAV = "nav, .nav, #nav, .navigation, .navbar, [role='navigation'], .menu, .main-menu";
    private static final String SIDEBAR = ".sidebar, #sidebar, aside";
    private static final String BREADCRUMB = ".breadcrumb, nav[aria-label='breadcrumb']";
    private static final String LOGO = ".logo, #logo, .brand";

    // Common navigation links
    private static final String HOME_LINK = "a[href='/'], a[href*='home'], .home-link";
    private static final String ABOUT_LINK = "a[href*='about'], .about-link";
    private static final String CONTACT_LINK = "a[href*='contact'], .contact-link";
    private static final String DASHBOARD_LINK = "a[href*='dashboard'], .dashboard-link";

    public NavigationComponent(Page page) {
        super(page);
    }

    /**
     * Wait for header to be visible and check if it's displayed.
     * Uses multiple fallback selectors for different UI frameworks.
     * @return true if header is visible
     */
    @Step("Check if header is displayed")
    public boolean isHeaderDisplayed() {
        try {
            // Wait for page to be in a stable state first
            waitForPageLoad();

            // Try primary selector first
            try {
                waitForVisible(HEADER, 5000);
                if (isVisible(HEADER)) {
                    return true;
                }
            } catch (Exception ignored) {
                // Try fallback selectors
            }

            // Fallback: Check for any element at the top of the page that could be a header
            // Common patterns in modern SPAs (React, Angular, Vue)
            String fallbackSelectors = "div[class*='header'], div[class*='Header'], " +
                    "div[class*='navbar'], div[class*='Navbar'], div[class*='nav-bar'], " +
                    "div[class*='topbar'], div[class*='TopBar'], div[class*='app-bar'], " +
                    "div[class*='MuiAppBar'], mat-toolbar, .ant-layout-header";

            return page.locator(fallbackSelectors).count() > 0 &&
                   page.locator(fallbackSelectors).first().isVisible();
        } catch (Exception e) {
            logger.debug("Header not visible within timeout: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Wait for footer to be visible and check if it's displayed.
     * @return true if footer is visible
     */
    @Step("Check if footer is displayed")
    public boolean isFooterDisplayed() {
        try {
            waitForVisible(FOOTER, 5000);
            return isVisible(FOOTER);
        } catch (Exception e) {
            logger.debug("Footer not visible within timeout: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Wait for main navigation to be visible and check if it's displayed.
     * @return true if main navigation is visible
     */
    @Step("Check if main navigation is displayed")
    public boolean isMainNavigationDisplayed() {
        try {
            waitForVisible(MAIN_NAV, 5000);
            return isVisible(MAIN_NAV);
        } catch (Exception e) {
            logger.debug("Main navigation not visible within timeout: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if sidebar is displayed.
     * @return true if sidebar is visible
     */
    @Step("Check if sidebar is displayed")
    public boolean isSidebarDisplayed() {
        return isVisible(SIDEBAR);
    }

    /**
     * Click on logo (typically navigates to home).
     * @return NavigationComponent instance for method chaining
     */
    @Step("Click on logo")
    public NavigationComponent clickLogo() {
        logger.info("Clicking on logo");
        if (isVisible(LOGO)) {
            click(LOGO);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Navigate to home page.
     * @return NavigationComponent instance for method chaining
     */
    @Step("Navigate to home")
    public NavigationComponent navigateToHome() {
        logger.info("Navigating to home");
        if (isVisible(HOME_LINK)) {
            click(HOME_LINK);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Navigate to about page.
     * @return NavigationComponent instance for method chaining
     */
    @Step("Navigate to about")
    public NavigationComponent navigateToAbout() {
        logger.info("Navigating to about");
        if (isVisible(ABOUT_LINK)) {
            click(ABOUT_LINK);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Navigate to contact page.
     * @return NavigationComponent instance for method chaining
     */
    @Step("Navigate to contact")
    public NavigationComponent navigateToContact() {
        logger.info("Navigating to contact");
        if (isVisible(CONTACT_LINK)) {
            click(CONTACT_LINK);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Navigate to dashboard.
     * @return NavigationComponent instance for method chaining
     */
    @Step("Navigate to dashboard")
    public NavigationComponent navigateToDashboard() {
        logger.info("Navigating to dashboard");
        if (isVisible(DASHBOARD_LINK)) {
            click(DASHBOARD_LINK);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Get breadcrumb text.
     * @return Breadcrumb text
     */
    @Step("Get breadcrumb text")
    public String getBreadcrumbText() {
        if (isVisible(BREADCRUMB)) {
            return getText(BREADCRUMB);
        }
        return "";
    }

    /**
     * Check if breadcrumb is displayed.
     * @return true if breadcrumb is visible
     */
    @Step("Check if breadcrumb is displayed")
    public boolean isBreadcrumbDisplayed() {
        return isVisible(BREADCRUMB);
    }

    /**
     * Click on a specific breadcrumb item.
     * @param itemText Text of the breadcrumb item
     * @return NavigationComponent instance for method chaining
     */
    @Step("Click breadcrumb item: {itemText}")
    public NavigationComponent clickBreadcrumbItem(String itemText) {
        logger.info("Clicking breadcrumb item: {}", itemText);
        String selector = String.format(".breadcrumb a:has-text('%s')", itemText);
        if (isVisible(selector)) {
            click(selector);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Navigate to a menu item by link text.
     * @param linkText Link text
     * @return NavigationComponent instance for method chaining
     */
    @Step("Click navigation link: {linkText}")
    public NavigationComponent clickNavigationLink(String linkText) {
        logger.info("Clicking navigation link: {}", linkText);
        String selector = String.format("nav a:has-text('%s'), .nav-link:has-text('%s')", linkText, linkText);
        if (isVisible(selector)) {
            click(selector);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Get header text.
     * @return Header text content
     */
    @Step("Get header text")
    public String getHeaderText() {
        if (isVisible(HEADER)) {
            return getText(HEADER);
        }
        return "";
    }

    /**
     * Get footer text.
     * @return Footer text content
     */
    @Step("Get footer text")
    public String getFooterText() {
        if (isVisible(FOOTER)) {
            return getText(FOOTER);
        }
        return "";
    }

    /**
     * Scroll to footer.
     * @return NavigationComponent instance for method chaining
     */
    @Step("Scroll to footer")
    public NavigationComponent scrollToFooter() {
        logger.info("Scrolling to footer");
        if (isVisible(FOOTER)) {
            scrollToElement(FOOTER);
        }
        return this;
    }

    /**
     * Scroll to top (header).
     * @return NavigationComponent instance for method chaining
     */
    @Step("Scroll to top")
    public NavigationComponent scrollToTop() {
        logger.info("Scrolling to top");
        if (isVisible(HEADER)) {
            scrollToElement(HEADER);
        }
        return this;
    }
}