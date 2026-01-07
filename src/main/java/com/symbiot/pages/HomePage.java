package com.symbiot.pages;

import com.microsoft.playwright.Page;
import com.symbiot.base.BasePage;
import io.qameta.allure.Step;

/**
 * Page Object for the Home Page / Dashboard.
 * Contains all home page related elements and actions.
 */
public class HomePage extends BasePage {

    // Selectors - Update based on actual application structure
    private static final String USER_PROFILE_MENU = ".user-menu, .profile-menu, #user-profile, .avatar, .user-avatar, .profile-dropdown, [data-testid='user-menu'], .MuiAvatar-root, .account-menu, button[aria-label*='account'], button[aria-label*='profile'], .header-user, .user-icon, img[alt*='avatar'], img[alt*='profile'], .dropdown-toggle.user, .nav-user, .user-dropdown";
    private static final String LOGOUT_BUTTON = "a[href*='logout'], button[onclick*='logout'], #logout-btn, .logout, button:has-text('Logout'), button:has-text('Log out'), button:has-text('Sign out'), a:has-text('Logout'), a:has-text('Log out'), a:has-text('Sign out'), [data-testid='logout'], .logout-btn, .signout-btn";
    private static final String LOGOUT_CONFIRM_BUTTON = ".confirm-logout, #confirm-logout, button:has-text('Confirm'), button:has-text('Yes'), button:has-text('OK')";
    private static final String DASHBOARD_CONTAINER = ".dashboard, #dashboard, .home-container, main, .main-content, .app-content, [role='main'], .container, .content-wrapper, .page-content";
    private static final String WELCOME_MESSAGE = ".welcome-message, .greeting, h1, .page-title, .dashboard-title, .welcome-text";
    private static final String NAVIGATION_MENU = "nav, .navigation, #nav-menu, .sidebar";
    private static final String SEARCH_INPUT = "input[type='search'], .search-input, #search";
    private static final String NOTIFICATIONS_ICON = ".notifications, #notifications, .bell-icon";
    private static final String SETTINGS_LINK = "a[href*='settings'], .settings-link, #settings";
    private static final String HELP_LINK = "a[href*='help'], .help-link, #help";

    // Common UI elements
    private static final String LOADING_SPINNER = ".loading, .spinner, [role='progressbar']";
    private static final String TOAST_MESSAGE = ".toast, .notification, .alert";

    public HomePage(Page page) {
        super(page);
    }

    /**
     * Wait for home page to load completely.
     * @return HomePage instance for method chaining
     */
    @Step("Wait for home page to load")
    public HomePage waitForPageToLoad() {
        logger.info("Waiting for home page to load");
        waitForPageLoad();
        try {
            waitForHidden(LOADING_SPINNER);
        } catch (Exception e) {
            logger.debug("No loading spinner found or already hidden");
        }
        return this;
    }

    /**
     * Check if home page is displayed.
     * @return true if home page is displayed
     */
    @Step("Verify home page is displayed")
    public boolean isHomePageDisplayed() {
        try {
            waitForVisible(DASHBOARD_CONTAINER, config.getActionTimeout());
            return true;
        } catch (Exception e) {
            logger.debug("Dashboard container not found");
            return false;
        }
    }

    /**
     * Check if user is logged in by verifying user profile menu.
     * @return true if user profile menu is visible
     */
    @Step("Verify user is logged in")
    public boolean isUserLoggedIn() {
        try {
            waitForVisible(USER_PROFILE_MENU, config.getActionTimeout());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get welcome message text.
     * @return Welcome message text
     */
    @Step("Get welcome message")
    public String getWelcomeMessage() {
        if (isVisible(WELCOME_MESSAGE)) {
            String message = getText(WELCOME_MESSAGE);
            logger.info("Welcome message: {}", message);
            return message;
        }
        return "";
    }

    /**
     * Click on user profile menu.
     * @return HomePage instance for method chaining
     */
    @Step("Click user profile menu")
    public HomePage clickUserProfileMenu() {
        logger.info("Clicking user profile menu");
        waitForVisible(USER_PROFILE_MENU);
        click(USER_PROFILE_MENU);
        return this;
    }

    /**
     * Perform logout action.
     * @return LoginPage instance after logout
     */
    @Step("Logout from application")
    public LoginPage logout() {
        logger.info("Performing logout");
        clickUserProfileMenu();
        waitForVisible(LOGOUT_BUTTON);
        click(LOGOUT_BUTTON);

        // Handle logout confirmation if present
        try {
            if (isVisible(LOGOUT_CONFIRM_BUTTON)) {
                click(LOGOUT_CONFIRM_BUTTON);
            }
        } catch (Exception e) {
            logger.debug("No logout confirmation dialog found");
        }

        waitForPageLoad();
        return new LoginPage(page);
    }

    /**
     * Click logout button directly (if visible without menu).
     * @return LoginPage instance after logout
     */
    @Step("Click logout button")
    public LoginPage clickLogoutButton() {
        logger.info("Clicking logout button");
        waitForVisible(LOGOUT_BUTTON);
        click(LOGOUT_BUTTON);
        waitForPageLoad();
        return new LoginPage(page);
    }

    /**
     * Check if navigation menu is displayed.
     * @return true if navigation is visible
     */
    @Step("Check if navigation menu is displayed")
    public boolean isNavigationMenuDisplayed() {
        return isVisible(NAVIGATION_MENU);
    }

    /**
     * Enter search text.
     * @param searchText Text to search
     * @return HomePage instance for method chaining
     */
    @Step("Search for: {searchText}")
    public HomePage search(String searchText) {
        logger.info("Searching for: {}", searchText);
        waitForVisible(SEARCH_INPUT);
        clearAndType(SEARCH_INPUT, searchText);
        pressKey("Enter");
        return this;
    }

    /**
     * Check if search input is displayed.
     * @return true if search input is visible
     */
    @Step("Check if search input is displayed")
    public boolean isSearchInputDisplayed() {
        return isVisible(SEARCH_INPUT);
    }

    /**
     * Click notifications icon.
     * @return HomePage instance for method chaining
     */
    @Step("Click notifications icon")
    public HomePage clickNotifications() {
        logger.info("Clicking notifications icon");
        if (isVisible(NOTIFICATIONS_ICON)) {
            click(NOTIFICATIONS_ICON);
        }
        return this;
    }

    /**
     * Check if notifications icon is displayed.
     * @return true if notifications icon is visible
     */
    @Step("Check if notifications icon is displayed")
    public boolean isNotificationsIconDisplayed() {
        return isVisible(NOTIFICATIONS_ICON);
    }

    /**
     * Navigate to settings page.
     * @return HomePage instance for method chaining
     */
    @Step("Navigate to settings")
    public HomePage navigateToSettings() {
        logger.info("Navigating to settings");
        if (isVisible(SETTINGS_LINK)) {
            click(SETTINGS_LINK);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Navigate to help page.
     * @return HomePage instance for method chaining
     */
    @Step("Navigate to help")
    public HomePage navigateToHelp() {
        logger.info("Navigating to help");
        if (isVisible(HELP_LINK)) {
            click(HELP_LINK);
            waitForPageLoad();
        }
        return this;
    }

    /**
     * Check if a toast/notification message is displayed.
     * @return true if toast message is visible
     */
    @Step("Check if toast message is displayed")
    public boolean isToastMessageDisplayed() {
        try {
            waitForVisible(TOAST_MESSAGE, 5000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get toast message text.
     * @return Toast message text
     */
    @Step("Get toast message")
    public String getToastMessage() {
        if (isToastMessageDisplayed()) {
            return getText(TOAST_MESSAGE);
        }
        return "";
    }

    /**
     * Check if user profile menu is displayed.
     * @return true if user profile menu is visible
     */
    @Step("Check if user profile menu is displayed")
    public boolean isUserProfileMenuDisplayed() {
        return isVisible(USER_PROFILE_MENU);
    }

    /**
     * Check if logout button is displayed.
     * @return true if logout button is visible
     */
    @Step("Check if logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        clickUserProfileMenu();
        return isVisible(LOGOUT_BUTTON);
    }

    /**
     * Navigate to a menu item by text.
     * @param menuText Menu item text
     * @return HomePage instance for method chaining
     */
    @Step("Navigate to menu: {menuText}")
    public HomePage navigateToMenu(String menuText) {
        logger.info("Navigating to menu: {}", menuText);
        String menuSelector = String.format("nav a:has-text('%s'), .nav-link:has-text('%s')", menuText, menuText);
        if (isVisible(menuSelector)) {
            click(menuSelector);
            waitForPageLoad();
        }
        return this;
    }
}