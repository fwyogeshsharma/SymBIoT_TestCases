package com.symbiot.pages;

import com.microsoft.playwright.Page;
import com.symbiot.base.BasePage;
import io.qameta.allure.Step;

/**
 * Page Object for the Login Page.
 * Contains all login-related elements and actions.
 */
public class LoginPage extends BasePage {

    // Selectors - Using data-testid, id, or CSS selectors for stability
    // Note: Update these selectors based on actual application structure

    // Dashboard login button (on the main dashboard page before login form appears)
    private static final String DASHBOARD_LOGIN_BUTTON = "button:has-text('Login'), button:has-text('Sign In'), a:has-text('Login'), a:has-text('Sign In'), [data-testid='login-button'], .login-btn, #loginBtn";

    // Login form elements
    private static final String USERNAME_INPUT = "input[name='username'], input[type='email'], input[name='email'], #username, #email, input[placeholder*='email' i], input[placeholder*='username' i]";
    private static final String PASSWORD_INPUT = "input[name='password'], input[type='password'], #password, input[placeholder*='password' i]";
    private static final String LOGIN_BUTTON = "button[type='submit'], input[type='submit'], button:has-text('Sign In'), button:has-text('Login'), #login-btn, .login-button, .signin-button";
    private static final String REMEMBER_ME_CHECKBOX = "input[name='remember'], #remember-me, .remember-checkbox";
    private static final String FORGOT_PASSWORD_LINK = "a[href*='forgot'], .forgot-password, #forgot-password";
    private static final String REGISTER_LINK = "a[href*='register'], a[href*='signup'], .register-link";
    private static final String ERROR_MESSAGE = ".error-message, .alert-danger, .error, #error-msg, [role='alert'], .toast-error, .notification-error";
    private static final String LOGIN_FORM = "form, .login-form, #login-form, .auth-form, .signin-form";
    private static final String PAGE_TITLE = "h1, .page-title, .login-title";

    public LoginPage(Page page) {
        super(page);
    }

    /**
     * Navigate to the login page by clicking Login button on dashboard.
     * @return LoginPage instance for method chaining
     */
    @Step("Navigate to Login Page")
    public LoginPage navigateToLoginPage() {
        logger.info("Clicking Login button on dashboard to access login form");
        // First check if we're already on the login form
        if (isLoginFormVisible()) {
            logger.info("Login form already visible");
            return this;
        }
        // Click the Login button on the dashboard
        clickDashboardLoginButton();
        waitForLoginFormToAppear();
        return this;
    }

    /**
     * Click the Login button on the dashboard page.
     * @return LoginPage instance for method chaining
     */
    @Step("Click Login button on Dashboard")
    public LoginPage clickDashboardLoginButton() {
        logger.info("Clicking Login button on dashboard");
        try {
            waitForVisible(DASHBOARD_LOGIN_BUTTON, config.getNavigationTimeout());
            click(DASHBOARD_LOGIN_BUTTON);
            waitForPageLoad();
        } catch (Exception e) {
            logger.warn("Dashboard login button not found with primary selector, trying alternatives");
            // Try clicking any visible login/sign-in text
            page.locator("text=Login, text=Sign In, text=Log In").first().click();
        }
        return this;
    }

    /**
     * Wait for the login form to appear.
     */
    private void waitForLoginFormToAppear() {
        logger.info("Waiting for login form to appear");
        try {
            waitForVisible(USERNAME_INPUT, config.getNavigationTimeout());
        } catch (Exception e) {
            logger.warn("Login form did not appear within timeout");
        }
    }

    /**
     * Check if login form is visible (username/password fields).
     * @return true if login form is visible
     */
    private boolean isLoginFormVisible() {
        try {
            return page.locator(USERNAME_INPUT).first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Open the application base URL.
     * @return LoginPage instance for method chaining
     */
    @Step("Open application")
    public LoginPage openApplication() {
        String baseUrl = config.getBaseUrl();
        logger.info("Opening application: {}", baseUrl);
        navigateTo(baseUrl);
        waitForPageLoad();
        return this;
    }

    /**
     * Check if login page is displayed (login form visible with username/password fields).
     * @return true if login page is displayed
     */
    @Step("Verify login page is displayed")
    public boolean isLoginPageDisplayed() {
        try {
            // Check if username and password fields are visible
            boolean usernameVisible = page.locator(USERNAME_INPUT).first().isVisible();
            boolean passwordVisible = page.locator(PASSWORD_INPUT).first().isVisible();
            return usernameVisible && passwordVisible;
        } catch (Exception e) {
            logger.debug("Login form not found: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Enter username.
     * @param username Username to enter
     * @return LoginPage instance for method chaining
     */
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        waitForVisible(USERNAME_INPUT);
        clearAndType(USERNAME_INPUT, username);
        return this;
    }

    /**
     * Enter password.
     * @param password Password to enter
     * @return LoginPage instance for method chaining
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        logger.info("Entering password: ****");
        waitForVisible(PASSWORD_INPUT);
        clearAndType(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Click login button.
     * @return LoginPage instance for method chaining
     */
    @Step("Click Login button")
    public LoginPage clickLoginButton() {
        logger.info("Clicking login button");
        waitForVisible(LOGIN_BUTTON);
        click(LOGIN_BUTTON);
        return this;
    }

    /**
     * Check remember me checkbox.
     * @return LoginPage instance for method chaining
     */
    @Step("Check Remember Me checkbox")
    public LoginPage checkRememberMe() {
        logger.info("Checking remember me checkbox");
        if (!isChecked(REMEMBER_ME_CHECKBOX)) {
            check(REMEMBER_ME_CHECKBOX);
        }
        return this;
    }

    /**
     * Uncheck remember me checkbox.
     * @return LoginPage instance for method chaining
     */
    @Step("Uncheck Remember Me checkbox")
    public LoginPage uncheckRememberMe() {
        logger.info("Unchecking remember me checkbox");
        if (isChecked(REMEMBER_ME_CHECKBOX)) {
            uncheck(REMEMBER_ME_CHECKBOX);
        }
        return this;
    }

    /**
     * Click forgot password link.
     * @return LoginPage instance for method chaining
     */
    @Step("Click Forgot Password link")
    public LoginPage clickForgotPassword() {
        logger.info("Clicking forgot password link");
        click(FORGOT_PASSWORD_LINK);
        return this;
    }

    /**
     * Click register link.
     * @return LoginPage instance for method chaining
     */
    @Step("Click Register link")
    public LoginPage clickRegisterLink() {
        logger.info("Clicking register link");
        click(REGISTER_LINK);
        return this;
    }

    /**
     * Perform complete login action.
     * @param username Username
     * @param password Password
     * @return HomePage instance after successful login
     */
    @Step("Login with username: {username}")
    public HomePage login(String username, String password) {
        logger.info("Performing login with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        waitForPageLoad();
        return new HomePage(page);
    }

    /**
     * Perform login with remember me option.
     * @param username Username
     * @param password Password
     * @return HomePage instance after successful login
     */
    @Step("Login with Remember Me - username: {username}")
    public HomePage loginWithRememberMe(String username, String password) {
        logger.info("Performing login with remember me for username: {}", username);
        enterUsername(username);
        enterPassword(password);
        checkRememberMe();
        clickLoginButton();
        waitForPageLoad();
        return new HomePage(page);
    }

    /**
     * Perform login expecting failure (for negative test cases).
     * @param username Username
     * @param password Password
     * @return LoginPage instance (stays on login page after failure)
     */
    @Step("Attempt login with invalid credentials - username: {username}")
    public LoginPage loginExpectingFailure(String username, String password) {
        logger.info("Attempting login with invalid credentials: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    /**
     * Check if error message is displayed.
     * @return true if error message is displayed
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        try {
            waitForVisible(ERROR_MESSAGE, config.getActionTimeout());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message text.
     * @return Error message text
     */
    @Step("Get error message text")
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            String message = getText(ERROR_MESSAGE);
            logger.info("Error message: {}", message);
            return message;
        }
        return "";
    }

    /**
     * Check if username field is displayed.
     * @return true if username field is displayed
     */
    @Step("Check if username field is displayed")
    public boolean isUsernameFieldDisplayed() {
        return isVisible(USERNAME_INPUT);
    }

    /**
     * Check if password field is displayed.
     * @return true if password field is displayed
     */
    @Step("Check if password field is displayed")
    public boolean isPasswordFieldDisplayed() {
        return isVisible(PASSWORD_INPUT);
    }

    /**
     * Check if login button is displayed.
     * @return true if login button is displayed
     */
    @Step("Check if login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isVisible(LOGIN_BUTTON);
    }

    /**
     * Check if login button is enabled.
     * @return true if login button is enabled
     */
    @Step("Check if login button is enabled")
    public boolean isLoginButtonEnabled() {
        return isEnabled(LOGIN_BUTTON);
    }

    /**
     * Get the login page title.
     * @return Page title text
     */
    @Step("Get login page title")
    public String getLoginPageTitle() {
        if (isVisible(PAGE_TITLE)) {
            return getText(PAGE_TITLE);
        }
        return getPageTitle();
    }

    /**
     * Clear all input fields.
     * @return LoginPage instance for method chaining
     */
    @Step("Clear all fields")
    public LoginPage clearAllFields() {
        logger.info("Clearing all input fields");
        clearAndType(USERNAME_INPUT, "");
        clearAndType(PASSWORD_INPUT, "");
        return this;
    }

    /**
     * Submit form using Enter key.
     * @return LoginPage instance for method chaining
     */
    @Step("Submit form with Enter key")
    public LoginPage submitWithEnterKey() {
        logger.info("Submitting form with Enter key");
        pressKey("Enter");
        return this;
    }
}