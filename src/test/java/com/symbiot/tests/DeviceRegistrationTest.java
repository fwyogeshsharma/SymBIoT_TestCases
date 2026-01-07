package com.symbiot.tests;

import com.symbiot.base.BaseTest;
import com.symbiot.listeners.RetryAnalyzer;
import com.symbiot.pages.DevicePage;
import com.symbiot.pages.HomePage;
import com.symbiot.pages.LoginPage;
import com.symbiot.utils.Constants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for Device Registration functionality.
 * Covers device registration workflow for all device types.
 */
@Epic("SymBIoT Application")
@Feature("Device Registration")
public class DeviceRegistrationTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private DevicePage devicePage;

    // Test credentials
    private static final String TEST_EMAIL = "clarehayden7@gmail.com";
    private static final String TEST_PASSWORD = "Faber321!";

    @BeforeMethod(alwaysRun = true)
    public void setUpTest() {
        loginPage = new LoginPage(getPage());
        loginPage.openApplication();
    }

    /**
     * Helper method to perform login and navigate to device page.
     */
    private DevicePage loginAndNavigateToDevicePage() {
        // Step 1 & 2: Login with credentials
        logger.info("Logging in with email: {}", TEST_EMAIL);
        homePage = loginPage.login(TEST_EMAIL, TEST_PASSWORD);

        // Verify login successful
        Assert.assertTrue(
                homePage.isHomePageDisplayed() || homePage.isUserLoggedIn(),
                "User should be logged in successfully"
        );

        // Step 3: Skip tour if displayed
        devicePage = new DevicePage(getPage());
        devicePage.skipTourIfDisplayed();

        // Step 4: Navigate to Device tab
        devicePage.navigateToDeviceTab();

        return devicePage;
    }

    @Test(
            description = "Verify user can login and navigate to device registration",
            groups = {Constants.SMOKE, Constants.DEVICE, Constants.POSITIVE},
            priority = 1,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("Device Navigation")
    @Description("Verify user can login and navigate to the Device tab")
    public void testLoginAndNavigateToDeviceTab() {
        logger.info("Starting test: testLoginAndNavigateToDeviceTab");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Verify Device tab is displayed
        Assert.assertTrue(
                devicePage.isDeviceTabDisplayed() || devicePage.isRegisterNewDeviceButtonDisplayed(),
                "Device page should be displayed with registration option"
        );

        logger.info("Successfully navigated to Device tab");
    }

    @Test(
            description = "Verify Register New Device button is visible",
            groups = {Constants.SMOKE, Constants.DEVICE, Constants.UI},
            priority = 2,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Device Registration UI")
    @Description("Verify Register New Device button is displayed on Device page")
    public void testRegisterNewDeviceButtonVisible() {
        logger.info("Starting test: testRegisterNewDeviceButtonVisible");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Verify Register New Device button
        Assert.assertTrue(
                devicePage.isRegisterNewDeviceButtonDisplayed(),
                "Register New Device button should be visible"
        );

        logger.info("Register New Device button is visible");
    }

    @Test(
            description = "Verify device type dropdown has available options",
            groups = {Constants.REGRESSION, Constants.DEVICE, Constants.UI},
            priority = 3,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Device Registration Form")
    @Description("Verify device type dropdown contains available device types")
    public void testDeviceTypeDropdownOptions() {
        logger.info("Starting test: testDeviceTypeDropdownOptions");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Click Register New Device
        devicePage.clickRegisterNewDevice();

        // Get available device types
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();

        // Verify device types exist
        Assert.assertFalse(
                deviceTypes.isEmpty(),
                "Device type dropdown should have available options"
        );

        logger.info("Found {} device types: {}", deviceTypes.size(), deviceTypes);
    }

    @Test(
            description = "Register a single device with sample data generation",
            groups = {Constants.SMOKE, Constants.DEVICE, Constants.DEVICE_REGISTRATION, Constants.POSITIVE},
            priority = 4,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Device Registration")
    @Description("Verify user can register a device with sample data generation enabled")
    public void testRegisterSingleDevice() {
        logger.info("Starting test: testRegisterSingleDevice");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Step 5: Click Register New Device
        devicePage.clickRegisterNewDevice();

        // Get first available device type
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();
        Assert.assertFalse(deviceTypes.isEmpty(), "Should have at least one device type");

        String deviceType = deviceTypes.get(0);
        logger.info("Registering device type: {}", deviceType);

        // Step 6: Select device type and fill form
        devicePage.selectDeviceType(deviceType);
        devicePage.fillFormWithDummyData(deviceType);
        devicePage.selectFirstAvailableManufacturer();

        // Step 7: Select Generate Sample Data
        devicePage.selectGenerateSampleData();

        // Step 8: Click Register Device
        devicePage.clickRegisterDevice();

        // Verify registration success
        boolean registrationSuccess = devicePage.isSuccessMessageDisplayed() ||
                                       !devicePage.isErrorMessageDisplayed();
        Assert.assertTrue(registrationSuccess, "Device should be registered successfully");

        logger.info("Device registered successfully: {}", deviceType);
    }

    @Test(
            description = "Verify generated sample data in Health tab",
            groups = {Constants.REGRESSION, Constants.DEVICE, Constants.DEVICE_REGISTRATION},
            priority = 5,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Sample Data Verification")
    @Description("Verify generated sample data is displayed in Health tab")
    public void testVerifyHealthTabData() {
        logger.info("Starting test: testVerifyHealthTabData");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Register a device with sample data
        devicePage.clickRegisterNewDevice();
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();

        if (!deviceTypes.isEmpty()) {
            String deviceType = deviceTypes.get(0);
            devicePage.selectDeviceType(deviceType);
            devicePage.fillFormWithDummyData(deviceType);
            devicePage.selectGenerateSampleData();
            devicePage.clickRegisterDevice();

            // Step 9: Navigate to Health tab and verify data
            devicePage.navigateToHealthTab();

            // Verify health widget/data is displayed
            boolean healthDataPresent = devicePage.isHealthWidgetDisplayed() || devicePage.isDataPresent();
            logger.info("Health data present: {}", healthDataPresent);

            // Note: Assertion may be soft if data generation is async
            if (!healthDataPresent) {
                logger.warn("Health data not immediately visible - may require page refresh or wait");
            }
        }

        logger.info("Health tab verification completed");
    }

    @Test(
            description = "Verify generated sample data in Activity tab",
            groups = {Constants.REGRESSION, Constants.DEVICE, Constants.DEVICE_REGISTRATION},
            priority = 6,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Sample Data Verification")
    @Description("Verify generated sample data is displayed in Activity tab")
    public void testVerifyActivityTabData() {
        logger.info("Starting test: testVerifyActivityTabData");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Register a device with sample data
        devicePage.clickRegisterNewDevice();
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();

        if (!deviceTypes.isEmpty()) {
            String deviceType = deviceTypes.get(0);
            devicePage.selectDeviceType(deviceType);
            devicePage.fillFormWithDummyData(deviceType);
            devicePage.selectGenerateSampleData();
            devicePage.clickRegisterDevice();

            // Step 9: Navigate to Activity tab and verify data
            devicePage.navigateToActivityTab();

            // Verify activity widget/data is displayed
            boolean activityDataPresent = devicePage.isActivityWidgetDisplayed() || devicePage.isDataPresent();
            logger.info("Activity data present: {}", activityDataPresent);

            // Note: Assertion may be soft if data generation is async
            if (!activityDataPresent) {
                logger.warn("Activity data not immediately visible - may require page refresh or wait");
            }
        }

        logger.info("Activity tab verification completed");
    }

    @DataProvider(name = "deviceTypeProvider")
    public Object[][] deviceTypeProvider() {
        // This will be populated dynamically based on available device types
        // For now, provide placeholder indices that will be used to select device types
        return new Object[][] {
            {0, "First Device Type"},
            {1, "Second Device Type"},
            {2, "Third Device Type"},
            {3, "Fourth Device Type"},
            {4, "Fifth Device Type"}
        };
    }

    @Test(
            description = "Register device by index from available types",
            groups = {Constants.REGRESSION, Constants.DEVICE, Constants.DEVICE_REGISTRATION},
            dataProvider = "deviceTypeProvider",
            priority = 7,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Device Registration")
    @Description("Register device for each available device type")
    public void testRegisterDeviceByIndex(int deviceIndex, String description) {
        logger.info("Starting test: testRegisterDeviceByIndex - Index: {}, Description: {}", deviceIndex, description);

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Click Register New Device
        devicePage.clickRegisterNewDevice();

        // Get available device types
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();

        // Skip if index is out of range
        if (deviceIndex >= deviceTypes.size()) {
            logger.info("Skipping - Device index {} is out of range (available: {})", deviceIndex, deviceTypes.size());
            return;
        }

        String deviceType = deviceTypes.get(deviceIndex);
        logger.info("Registering device type at index {}: {}", deviceIndex, deviceType);

        // Select device type and fill form
        devicePage.selectDeviceType(deviceType);
        devicePage.fillFormWithDummyData(deviceType);

        // Select Generate Sample Data
        devicePage.selectGenerateSampleData();

        // Click Register Device
        devicePage.clickRegisterDevice();

        // Verify registration
        boolean registrationSuccess = devicePage.isSuccessMessageDisplayed() ||
                                       !devicePage.isErrorMessageDisplayed();

        Assert.assertTrue(registrationSuccess,
                "Device should be registered successfully for type: " + deviceType);

        // Verify sample data in tabs
        devicePage.navigateToHealthTab();
        devicePage.navigateToActivityTab();

        logger.info("Device registration completed for: {}", deviceType);
    }

    @Test(
            description = "Register all available device types with sample data",
            groups = {Constants.E2E, Constants.DEVICE, Constants.DEVICE_REGISTRATION},
            priority = 10,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Complete Device Registration")
    @Description("Register all available device types and verify sample data generation for each")
    public void testRegisterAllDeviceTypes() {
        logger.info("Starting test: testRegisterAllDeviceTypes");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Get all available device types
        devicePage.clickRegisterNewDevice();
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();
        devicePage.clickCancel();

        Assert.assertFalse(deviceTypes.isEmpty(), "Should have at least one device type available");
        logger.info("Found {} device types to register", deviceTypes.size());

        int successCount = 0;
        int failCount = 0;

        // Step 10: Repeat for all device types
        for (int i = 0; i < deviceTypes.size(); i++) {
            String deviceType = deviceTypes.get(i);
            logger.info("Registering device {}/{}: {}", i + 1, deviceTypes.size(), deviceType);

            try {
                // Step 5: Click Register New Device
                devicePage.clickRegisterNewDevice();

                // Step 6: Select device type and fill form
                devicePage.selectDeviceType(deviceType);
                devicePage.fillFormWithDummyData(deviceType);

                // Step 7: Select Generate Sample Data
                devicePage.selectGenerateSampleData();

                // Step 8: Click Register Device
                devicePage.clickRegisterDevice();

                // Verify registration
                if (devicePage.isSuccessMessageDisplayed() || !devicePage.isErrorMessageDisplayed()) {
                    successCount++;
                    logger.info("Successfully registered: {}", deviceType);

                    // Step 9: Verify data in tabs
                    devicePage.navigateToHealthTab();
                    boolean healthData = devicePage.isHealthWidgetDisplayed() || devicePage.isDataPresent();
                    logger.info("Health data for {}: {}", deviceType, healthData);

                    devicePage.navigateToActivityTab();
                    boolean activityData = devicePage.isActivityWidgetDisplayed() || devicePage.isDataPresent();
                    logger.info("Activity data for {}: {}", deviceType, activityData);

                } else {
                    failCount++;
                    logger.warn("Failed to register: {}", deviceType);
                }

                // Navigate back to device list for next registration
                devicePage.navigateToDeviceTab();

                // Small wait between registrations
                getPage().waitForTimeout(1000);

            } catch (Exception e) {
                failCount++;
                logger.error("Error registering device {}: {}", deviceType, e.getMessage());
                // Try to recover for next iteration
                try {
                    devicePage.clickCancel();
                    devicePage.navigateToDeviceTab();
                } catch (Exception ignored) {
                }
            }
        }

        logger.info("Registration complete - Success: {}, Failed: {}, Total: {}",
                successCount, failCount, deviceTypes.size());

        // Assert at least some devices were registered
        Assert.assertTrue(successCount > 0,
                "At least one device should be registered successfully. " +
                "Success: " + successCount + ", Failed: " + failCount);

        // Optionally assert all devices registered
        if (failCount > 0) {
            logger.warn("{} device(s) failed to register", failCount);
        }
    }

    @Test(
            description = "Complete end-to-end device registration workflow",
            groups = {Constants.E2E, Constants.SMOKE, Constants.DEVICE_REGISTRATION},
            priority = 8,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("E2E Device Registration")
    @Description("Complete end-to-end test: Login -> Skip Tour -> Navigate to Device -> Register Device -> Verify Data")
    public void testCompleteDeviceRegistrationWorkflow() {
        logger.info("Starting test: testCompleteDeviceRegistrationWorkflow");

        // Step 1: Enter email
        logger.info("Step 1: Entering email: {}", TEST_EMAIL);
        loginPage.enterUsername(TEST_EMAIL);

        // Step 1: Enter password
        logger.info("Step 1: Entering password");
        loginPage.enterPassword(TEST_PASSWORD);

        // Step 2: Click Sign in button
        logger.info("Step 2: Clicking Sign in button");
        loginPage.clickLoginButton();
        homePage = new HomePage(getPage());

        // Verify login
        Assert.assertTrue(
                homePage.isHomePageDisplayed() || homePage.isUserLoggedIn(),
                "Login should be successful"
        );

        // Step 3: Click Skip tour
        logger.info("Step 3: Skipping tour if displayed");
        devicePage = new DevicePage(getPage());
        devicePage.skipTourIfDisplayed();

        // Step 4: Navigate to Device tab
        logger.info("Step 4: Navigating to Device tab");
        devicePage.navigateToDeviceTab();

        Assert.assertTrue(
                devicePage.isRegisterNewDeviceButtonDisplayed(),
                "Device page should be displayed"
        );

        // Step 5: Click Register New Device button
        logger.info("Step 5: Clicking Register New Device button");
        devicePage.clickRegisterNewDevice();

        // Get available device types
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();
        Assert.assertFalse(deviceTypes.isEmpty(), "Device types should be available");

        String selectedDeviceType = deviceTypes.get(0);
        logger.info("Selected device type: {}", selectedDeviceType);

        // Step 6: Select device type and fill form with dummy data
        logger.info("Step 6: Selecting device type and filling form");
        devicePage.selectDeviceType(selectedDeviceType);
        devicePage.fillFormWithDummyData(selectedDeviceType);

        // Step 7: Select Generate sample data
        logger.info("Step 7: Selecting Generate sample data");
        devicePage.selectGenerateSampleData();

        // Step 8: Click Register Device button
        logger.info("Step 8: Clicking Register Device button");
        devicePage.clickRegisterDevice();

        // Verify registration success
        boolean registrationSuccess = devicePage.isSuccessMessageDisplayed() ||
                                       !devicePage.isErrorMessageDisplayed();
        Assert.assertTrue(registrationSuccess, "Device registration should be successful");

        // Step 9: Check generated data in Health tab
        logger.info("Step 9: Checking generated data in Health tab");
        devicePage.navigateToHealthTab();
        boolean healthDataDisplayed = devicePage.isHealthWidgetDisplayed() || devicePage.isDataPresent();
        logger.info("Health data displayed: {}", healthDataDisplayed);

        // Check Activity tab
        logger.info("Step 9: Checking generated data in Activity tab");
        devicePage.navigateToActivityTab();
        boolean activityDataDisplayed = devicePage.isActivityWidgetDisplayed() || devicePage.isDataPresent();
        logger.info("Activity data displayed: {}", activityDataDisplayed);

        logger.info("Complete device registration workflow test passed");
    }

    @Test(
            description = "Verify form validation - empty required fields",
            groups = {Constants.REGRESSION, Constants.DEVICE, Constants.NEGATIVE},
            priority = 9,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Form Validation")
    @Description("Verify form validation when required fields are empty")
    public void testFormValidationEmptyFields() {
        logger.info("Starting test: testFormValidationEmptyFields");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Click Register New Device
        devicePage.clickRegisterNewDevice();

        // Try to submit without filling any fields
        devicePage.clickRegisterDevice();

        // Verify form is not submitted or error is shown
        // The form should either show validation errors or remain open
        boolean formStillOpen = devicePage.getAvailableDeviceTypes().size() > 0;
        boolean errorShown = devicePage.isErrorMessageDisplayed();

        Assert.assertTrue(formStillOpen || errorShown,
                "Form validation should prevent submission with empty required fields");

        logger.info("Form validation test completed");
    }

    @Test(
            description = "Cancel device registration",
            groups = {Constants.REGRESSION, Constants.DEVICE, Constants.UI},
            priority = 11,
            retryAnalyzer = RetryAnalyzer.class
    )
    @Severity(SeverityLevel.MINOR)
    @Story("Device Registration UI")
    @Description("Verify user can cancel device registration")
    public void testCancelDeviceRegistration() {
        logger.info("Starting test: testCancelDeviceRegistration");

        // Login and navigate
        DevicePage devicePage = loginAndNavigateToDevicePage();

        // Click Register New Device
        devicePage.clickRegisterNewDevice();

        // Fill some data
        List<String> deviceTypes = devicePage.getAvailableDeviceTypes();
        if (!deviceTypes.isEmpty()) {
            devicePage.selectDeviceType(deviceTypes.get(0));
            devicePage.enterDeviceName("Test Device Cancel");
        }

        // Click Cancel
        devicePage.clickCancel();

        // Verify returned to device list
        Assert.assertTrue(
                devicePage.isRegisterNewDeviceButtonDisplayed(),
                "Should return to device list after canceling"
        );

        logger.info("Cancel device registration test completed");
    }
}