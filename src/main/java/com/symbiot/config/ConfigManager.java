package com.symbiot.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling application properties.
 * Supports multiple environments (QA, Staging, Production).
 * Implements Singleton pattern for global access.
 */
public class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties;

    private static final String DEFAULT_CONFIG_PATH = "src/test/resources/config/config.properties";

    private ConfigManager() {
        properties = new Properties();
        loadConfiguration();
    }

    /**
     * Get singleton instance of ConfigManager.
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Load configuration from properties files.
     * First loads default config, then environment-specific config.
     */
    private void loadConfiguration() {
        // Load default configuration
        loadPropertiesFile(DEFAULT_CONFIG_PATH);

        // Load environment-specific configuration
        String env = System.getProperty("env", "").toLowerCase();
        if (!env.isEmpty()) {
            String envConfigPath = "src/test/resources/config/" + env + ".properties";
            loadPropertiesFile(envConfigPath);
            logger.info("Loaded environment configuration: {}", env);
        }

        // Override with system properties
        overrideWithSystemProperties();

        logger.info("Configuration loaded successfully");
    }

    /**
     * Load properties from a file.
     * @param filePath Path to the properties file
     */
    private void loadPropertiesFile(String filePath) {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            logger.debug("Loaded properties from: {}", filePath);
        } catch (IOException e) {
            logger.warn("Could not load properties file: {}. Error: {}", filePath, e.getMessage());
        }
    }

    /**
     * Override properties with system properties if provided.
     */
    private void overrideWithSystemProperties() {
        // Override browser if specified via command line
        String browser = System.getProperty("browser");
        if (browser != null && !browser.isEmpty()) {
            properties.setProperty("browser", browser);
        }

        // Override headless mode if specified
        String headless = System.getProperty("headless");
        if (headless != null && !headless.isEmpty()) {
            properties.setProperty("headless", headless);
        }

        // Override base URL if specified
        String baseUrl = System.getProperty("base.url");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            properties.setProperty("base.url", baseUrl);
        }
    }

    /**
     * Get property value as String.
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value with default.
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get property value as integer.
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Property value as integer
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key: {}. Using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Get property value as boolean.
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value as boolean
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get property value as long.
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Property value as long
     */
    public long getLongProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for key: {}. Using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    // Convenience methods for common properties

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getBrowser() {
        return getProperty("browser", "chromium");
    }

    public boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }

    public int getSlowMotion() {
        return getIntProperty("slow.motion", 0);
    }

    public int getViewportWidth() {
        return getIntProperty("viewport.width", 1920);
    }

    public int getViewportHeight() {
        return getIntProperty("viewport.height", 1080);
    }

    public int getDefaultTimeout() {
        return getIntProperty("default.timeout", 30000);
    }

    public int getNavigationTimeout() {
        return getIntProperty("navigation.timeout", 60000);
    }

    public int getActionTimeout() {
        return getIntProperty("action.timeout", 10000);
    }

    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public boolean isVideoOnFailure() {
        return getBooleanProperty("video.on.failure", true);
    }

    public boolean isTraceOnFailure() {
        return getBooleanProperty("trace.on.failure", true);
    }

    public String getDefaultUsername() {
        return getProperty("default.username", "");
    }

    public String getDefaultPassword() {
        return getProperty("default.password", "");
    }

    public int getParallelThreads() {
        return getIntProperty("parallel.threads", 3);
    }

    /**
     * Reload configuration (useful for testing).
     */
    public void reload() {
        properties.clear();
        loadConfiguration();
    }
}