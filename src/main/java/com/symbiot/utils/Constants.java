package com.symbiot.utils;

/**
 * Constants used throughout the test framework.
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    // Time constants (in milliseconds)
    public static final int SHORT_WAIT = 3000;
    public static final int MEDIUM_WAIT = 10000;
    public static final int LONG_WAIT = 30000;
    public static final int EXTRA_LONG_WAIT = 60000;

    // Default values
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final int DEFAULT_NAVIGATION_TIMEOUT = 60000;
    public static final int DEFAULT_ACTION_TIMEOUT = 10000;

    // Paths
    public static final String SCREENSHOTS_DIR = "target/screenshots";
    public static final String VIDEOS_DIR = "target/videos";
    public static final String TRACES_DIR = "target/traces";
    public static final String REPORTS_DIR = "target/reports";
    public static final String ALLURE_RESULTS_DIR = "target/allure-results";

    // Test groups
    public static final String SMOKE = "smoke";
    public static final String REGRESSION = "regression";
    public static final String SANITY = "sanity";
    public static final String INTEGRATION = "integration";
    public static final String E2E = "e2e";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String UI = "ui";
    public static final String NEGATIVE = "negative";
    public static final String POSITIVE = "positive";
    public static final String DEVICE = "device";
    public static final String DEVICE_REGISTRATION = "device_registration";

    // Browser types
    public static final String CHROMIUM = "chromium";
    public static final String FIREFOX = "firefox";
    public static final String WEBKIT = "webkit";

    // Environment names
    public static final String ENV_QA = "qa";
    public static final String ENV_STAGING = "staging";
    public static final String ENV_PROD = "prod";

    // Common error messages
    public static final String ERR_ELEMENT_NOT_FOUND = "Element not found: %s";
    public static final String ERR_ELEMENT_NOT_VISIBLE = "Element not visible: %s";
    public static final String ERR_ELEMENT_NOT_CLICKABLE = "Element not clickable: %s";
    public static final String ERR_PAGE_LOAD_TIMEOUT = "Page load timeout exceeded";
    public static final String ERR_ASSERTION_FAILED = "Assertion failed: %s";

    // Date formats
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_TIMESTAMP = "yyyyMMdd_HHmmss";
    public static final String DATE_FORMAT_DISPLAY = "MM/dd/yyyy";

    // Keyboard keys
    public static final String KEY_ENTER = "Enter";
    public static final String KEY_TAB = "Tab";
    public static final String KEY_ESCAPE = "Escape";
    public static final String KEY_BACKSPACE = "Backspace";
    public static final String KEY_DELETE = "Delete";
    public static final String KEY_ARROW_UP = "ArrowUp";
    public static final String KEY_ARROW_DOWN = "ArrowDown";
    public static final String KEY_ARROW_LEFT = "ArrowLeft";
    public static final String KEY_ARROW_RIGHT = "ArrowRight";

    // HTTP Status codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
}