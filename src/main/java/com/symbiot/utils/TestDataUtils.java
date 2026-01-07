package com.symbiot.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * Utility class for generating test data.
 */
public class TestDataUtils {

    private static final Logger logger = LogManager.getLogger(TestDataUtils.class);
    private static final Random random = new Random();

    private static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC_CHARS = "0123456789";
    private static final String ALPHANUMERIC_CHARS = ALPHA_CHARS + NUMERIC_CHARS;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:',.<>?";

    /**
     * Generate a random string of specified length.
     * @param length Desired string length
     * @return Random string
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_CHARS.charAt(random.nextInt(ALPHA_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a random alphanumeric string of specified length.
     * @param length Desired string length
     * @return Random alphanumeric string
     */
    public static String randomAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC_CHARS.charAt(random.nextInt(ALPHANUMERIC_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a random numeric string of specified length.
     * @param length Desired string length
     * @return Random numeric string
     */
    public static String randomNumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(NUMERIC_CHARS.charAt(random.nextInt(NUMERIC_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a random integer within a range.
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Random integer
     */
    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Generate a random email address.
     * @return Random email address
     */
    public static String randomEmail() {
        return "test_" + randomAlphanumeric(8).toLowerCase() + "@example.com";
    }

    /**
     * Generate a random email address with specified domain.
     * @param domain Email domain
     * @return Random email address
     */
    public static String randomEmail(String domain) {
        return "test_" + randomAlphanumeric(8).toLowerCase() + "@" + domain;
    }

    /**
     * Generate a unique identifier.
     * @return UUID string
     */
    public static String uniqueId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a short unique identifier (8 characters).
     * @return Short unique ID
     */
    public static String shortUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generate a timestamp string.
     * @return Timestamp in format yyyyMMdd_HHmmss
     */
    public static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    /**
     * Generate a random username.
     * @return Random username
     */
    public static String randomUsername() {
        return "user_" + randomAlphanumeric(6).toLowerCase();
    }

    /**
     * Generate a random password that meets common requirements.
     * @return Random password (uppercase, lowercase, number, special char, 12 chars)
     */
    public static String randomPassword() {
        return randomString(4) +
               randomString(4).toUpperCase() +
               randomNumeric(2) +
               SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())) +
               SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
    }

    /**
     * Generate a random phone number (US format).
     * @return Random phone number
     */
    public static String randomPhoneNumber() {
        return String.format("(%s) %s-%s",
                randomNumeric(3),
                randomNumeric(3),
                randomNumeric(4));
    }

    /**
     * Generate a random date in the past.
     * @param maxDaysAgo Maximum number of days in the past
     * @return Random past date
     */
    public static LocalDate randomPastDate(int maxDaysAgo) {
        return LocalDate.now().minusDays(randomInt(1, maxDaysAgo));
    }

    /**
     * Generate a random date in the future.
     * @param maxDaysAhead Maximum number of days in the future
     * @return Random future date
     */
    public static LocalDate randomFutureDate(int maxDaysAhead) {
        return LocalDate.now().plusDays(randomInt(1, maxDaysAhead));
    }

    /**
     * Generate a random first name.
     * @return Random first name
     */
    public static String randomFirstName() {
        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Emma",
                               "James", "Emily", "Robert", "Olivia", "William", "Sophia"};
        return firstNames[random.nextInt(firstNames.length)];
    }

    /**
     * Generate a random last name.
     * @return Random last name
     */
    public static String randomLastName() {
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia",
                              "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez"};
        return lastNames[random.nextInt(lastNames.length)];
    }

    /**
     * Generate a random full name.
     * @return Random full name
     */
    public static String randomFullName() {
        return randomFirstName() + " " + randomLastName();
    }

    /**
     * Generate a random company name.
     * @return Random company name
     */
    public static String randomCompanyName() {
        String[] prefixes = {"Acme", "Global", "Premier", "Advanced", "Digital", "Tech"};
        String[] suffixes = {"Solutions", "Systems", "Industries", "Corp", "Inc", "LLC"};
        return prefixes[random.nextInt(prefixes.length)] + " " +
               suffixes[random.nextInt(suffixes.length)];
    }

    /**
     * Generate a test name with timestamp for uniqueness.
     * @param baseName Base name for the test
     * @return Unique test name
     */
    public static String uniqueTestName(String baseName) {
        return baseName + "_" + timestamp();
    }

    /**
     * Pick a random item from an array.
     * @param items Array of items
     * @param <T> Type of items
     * @return Random item from array
     */
    public static <T> T randomFrom(T[] items) {
        return items[random.nextInt(items.length)];
    }

    /**
     * Generate a random boolean.
     * @return Random boolean
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }
}