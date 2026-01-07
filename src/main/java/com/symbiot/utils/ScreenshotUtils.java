package com.symbiot.utils;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for screenshot operations.
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Capture screenshot of the current page.
     * @param page Playwright Page instance
     * @return Screenshot as byte array
     */
    public static byte[] captureScreenshot(Page page) {
        logger.debug("Capturing screenshot");
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    /**
     * Capture screenshot of a specific element.
     * @param page Playwright Page instance
     * @param selector Element selector
     * @return Screenshot as byte array
     */
    public static byte[] captureElementScreenshot(Page page, String selector) {
        logger.debug("Capturing element screenshot: {}", selector);
        return page.locator(selector).screenshot();
    }

    /**
     * Save screenshot to file system.
     * @param screenshotBytes Screenshot byte array
     * @param testName Name of the test
     * @param directory Directory to save screenshot
     * @return Path to saved screenshot
     */
    public static String saveScreenshot(byte[] screenshotBytes, String testName, String directory) {
        try {
            // Create directory if it doesn't exist
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String fileName = sanitizeFileName(testName) + "_" + timestamp + ".png";
            Path filePath = dirPath.resolve(fileName);

            // Save screenshot
            Files.write(filePath, screenshotBytes);
            logger.info("Screenshot saved: {}", filePath);

            return filePath.toString();

        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Capture and save screenshot in one operation.
     * @param page Playwright Page instance
     * @param testName Name of the test
     * @param directory Directory to save screenshot
     * @return Path to saved screenshot
     */
    public static String captureAndSave(Page page, String testName, String directory) {
        byte[] screenshot = captureScreenshot(page);
        return saveScreenshot(screenshot, testName, directory);
    }

    /**
     * Sanitize file name by removing invalid characters.
     * @param fileName Original file name
     * @return Sanitized file name
     */
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}