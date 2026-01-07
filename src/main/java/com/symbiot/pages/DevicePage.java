package com.symbiot.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.symbiot.base.BasePage;
import com.symbiot.utils.TestDataUtils;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for Device Management.
 * Handles device registration, viewing, and management operations.
 */
public class DevicePage extends BasePage {

    // Navigation selectors
    private static final String DEVICE_TAB = "a[href*='device'], button:has-text('Device'), button:has-text('Devices'), .nav-link:has-text('Device'), .nav-link:has-text('Devices'), [data-testid='device-tab'], a:has-text('Device'), a:has-text('Devices'), .menu-item:has-text('Device'), .sidebar-link:has-text('Device'), li:has-text('Device') a, .nav-item:has-text('Device')";
    private static final String DEVICES_MENU = ".devices-menu, #devices-menu, a:has-text('Devices'), button:has-text('Devices')";

    // Skip tour modal
    private static final String SKIP_TOUR_BUTTON = "button:has-text('Skip'), button:has-text('Skip tour'), button:has-text('Skip Tour'), button:has-text('SKIP'), a:has-text('Skip'), .skip-tour, .skip-btn, [data-testid='skip-tour'], .tour-skip, .onboarding-skip, button.skip, .modal button:has-text('Skip'), .dialog button:has-text('Skip'), button:has-text('Later'), button:has-text('Not now'), button:has-text('Close'), .introjs-skipbutton, .shepherd-cancel-icon, .tour-close";
    private static final String TOUR_MODAL = ".tour-modal, .onboarding-modal, [data-testid='tour-modal'], .intro-modal, .welcome-modal, .shepherd-modal, .introjs-overlay";

    // Device registration selectors
    private static final String REGISTER_NEW_DEVICE_BUTTON = "button:has-text('Register New Device'), button:has-text('Add Device'), .register-device-btn, [data-testid='register-device']";
    private static final String DEVICE_REGISTRATION_FORM = "form.device-form, .device-registration-form, [data-testid='device-form']";
    private static final String DEVICE_REGISTRATION_MODAL = ".device-registration-modal, .modal:has-text('Register'), [data-testid='device-registration-modal']";

    // Device form fields
    private static final String DEVICE_TYPE_DROPDOWN = "select[name='deviceType'], #deviceType, [data-testid='device-type'], .device-type-select";
    private static final String DEVICE_TYPE_OPTIONS = "select[name='deviceType'] option, #deviceType option, .device-type-option";
    private static final String DEVICE_NAME_INPUT = "input[name='deviceName'], #deviceName, [data-testid='device-name']";
    private static final String DEVICE_ID_INPUT = "input[name='deviceId'], #deviceId, [data-testid='device-id']";
    private static final String DEVICE_SERIAL_INPUT = "input[name='serialNumber'], #serialNumber, [data-testid='serial-number']";
    private static final String DEVICE_MODEL_INPUT = "input[name='model'], #model, [data-testid='device-model']";
    private static final String DEVICE_DESCRIPTION_INPUT = "textarea[name='description'], #description, [data-testid='device-description']";
    private static final String MANUFACTURER_DROPDOWN = "select[name='manufacturer'], select[name='company'], #manufacturer, #company, [data-testid='manufacturer']";
    private static final String MANUFACTURER_OPTIONS = "select[name='manufacturer'] option, select[name='company'] option, #manufacturer option, #company option";
    private static final String LOCATION_INPUT = "input[name='location'], #location, [data-testid='device-location']";
    private static final String MAC_ADDRESS_INPUT = "input[name='macAddress'], #macAddress, [data-testid='mac-address']";
    private static final String IP_ADDRESS_INPUT = "input[name='ipAddress'], #ipAddress, [data-testid='ip-address']";
    private static final String FIRMWARE_VERSION_INPUT = "input[name='firmwareVersion'], #firmwareVersion, [data-testid='firmware-version']";

    // Generate sample data checkbox
    private static final String GENERATE_SAMPLE_DATA_CHECKBOX = "input[name='generateSampleData'], #generateSampleData, input:near(:text('Generate sample data')), [data-testid='generate-sample-data']";
    private static final String GENERATE_SAMPLE_DATA_LABEL = "label:has-text('Generate sample data'), .sample-data-label";

    // Form buttons
    private static final String REGISTER_DEVICE_BUTTON = "button[type='submit']:has-text('Register'), button:has-text('Register Device'), .register-btn, [data-testid='submit-device']";
    private static final String CANCEL_BUTTON = "button:has-text('Cancel'), .cancel-btn, [data-testid='cancel-registration']";
    private static final String SAVE_BUTTON = "button:has-text('Save'), .save-btn";

    // Success/Error messages
    private static final String SUCCESS_MESSAGE = ".success-message, .toast-success, .alert-success, [data-testid='success-message']";
    private static final String ERROR_MESSAGE = ".error-message, .toast-error, .alert-danger, [data-testid='error-message']";

    // Device data tabs
    private static final String HEALTH_TAB = "a:has-text('Health'), button:has-text('Health'), .health-tab, [data-testid='health-tab']";
    private static final String ACTIVITY_TAB = "a:has-text('Activity'), button:has-text('Activity'), .activity-tab, [data-testid='activity-tab']";
    private static final String OVERVIEW_TAB = "a:has-text('Overview'), button:has-text('Overview'), .overview-tab, [data-testid='overview-tab']";
    private static final String ANALYTICS_TAB = "a:has-text('Analytics'), button:has-text('Analytics'), .analytics-tab, [data-testid='analytics-tab']";

    // Widgets and data display
    private static final String HEALTH_WIDGET = ".health-widget, [data-testid='health-widget'], .widget:has-text('Health')";
    private static final String ACTIVITY_WIDGET = ".activity-widget, [data-testid='activity-widget'], .widget:has-text('Activity')";
    private static final String DATA_CHART = ".chart, .data-chart, canvas, [data-testid='data-chart']";
    private static final String DATA_TABLE = "table.data-table, .device-data-table, [data-testid='data-table']";
    private static final String DATA_ROWS = "table tbody tr, .data-row";

    // Device list
    private static final String DEVICE_LIST = ".device-list, .devices-table, [data-testid='device-list']";
    private static final String DEVICE_CARD = ".device-card, .device-item, [data-testid='device-card']";

    // Loading indicators
    private static final String LOADING_SPINNER = ".loading, .spinner, [role='progressbar'], .loading-indicator";

    public DevicePage(Page page) {
        super(page);
    }

    /**
     * Skip the application tour if displayed.
     * @return DevicePage instance for method chaining
     */
    @Step("Skip tour if displayed")
    public DevicePage skipTourIfDisplayed() {
        logger.info("Checking for tour modal");
        try {
            // Wait briefly for tour modal
            page.waitForSelector(SKIP_TOUR_BUTTON, new Page.WaitForSelectorOptions().setTimeout(5000));
            if (isVisible(SKIP_TOUR_BUTTON)) {
                logger.info("Tour modal found, clicking Skip");
                click(SKIP_TOUR_BUTTON);
                waitForPageLoad();
            }
        } catch (Exception e) {
            logger.debug("No tour modal found or already dismissed");
        }
        return this;
    }

    /**
     * Navigate to Device tab.
     * @return DevicePage instance for method chaining
     */
    @Step("Navigate to Device tab")
    public DevicePage navigateToDeviceTab() {
        logger.info("Navigating to Device tab");
        waitForVisible(DEVICE_TAB);
        click(DEVICE_TAB);
        waitForPageLoad();
        waitForLoadingToComplete();
        return this;
    }

    /**
     * Wait for loading spinner to disappear.
     */
    private void waitForLoadingToComplete() {
        try {
            waitForHidden(LOADING_SPINNER);
        } catch (Exception e) {
            logger.debug("No loading spinner found or already hidden");
        }
    }

    /**
     * Click Register New Device button.
     * @return DevicePage instance for method chaining
     */
    @Step("Click Register New Device button")
    public DevicePage clickRegisterNewDevice() {
        logger.info("Clicking Register New Device button");
        waitForVisible(REGISTER_NEW_DEVICE_BUTTON);
        click(REGISTER_NEW_DEVICE_BUTTON);
        // Wait for registration form/modal to appear
        try {
            page.waitForSelector(DEVICE_TYPE_DROPDOWN, new Page.WaitForSelectorOptions().setTimeout(10000));
        } catch (Exception e) {
            logger.debug("Waiting for device registration form");
        }
        return this;
    }

    /**
     * Get all available device types from dropdown.
     * @return List of device type names
     */
    @Step("Get available device types")
    public List<String> getAvailableDeviceTypes() {
        logger.info("Getting available device types");
        List<String> deviceTypes = new ArrayList<>();
        waitForVisible(DEVICE_TYPE_DROPDOWN);

        Locator options = page.locator(DEVICE_TYPE_OPTIONS);
        int count = options.count();

        for (int i = 0; i < count; i++) {
            String optionText = options.nth(i).textContent();
            if (optionText != null && !optionText.trim().isEmpty() &&
                !optionText.toLowerCase().contains("select")) {
                deviceTypes.add(optionText.trim());
            }
        }

        logger.info("Found {} device types: {}", deviceTypes.size(), deviceTypes);
        return deviceTypes;
    }

    /**
     * Select device type from dropdown.
     * @param deviceType Device type to select
     * @return DevicePage instance for method chaining
     */
    @Step("Select device type: {deviceType}")
    public DevicePage selectDeviceType(String deviceType) {
        logger.info("Selecting device type: {}", deviceType);
        waitForVisible(DEVICE_TYPE_DROPDOWN);
        selectByLabel(DEVICE_TYPE_DROPDOWN, deviceType);
        return this;
    }

    /**
     * Select device type by index.
     * @param index Index of device type (0-based)
     * @return DevicePage instance for method chaining
     */
    @Step("Select device type by index: {index}")
    public DevicePage selectDeviceTypeByIndex(int index) {
        logger.info("Selecting device type by index: {}", index);
        waitForVisible(DEVICE_TYPE_DROPDOWN);
        // Add 1 to skip the placeholder option
        selectByIndex(DEVICE_TYPE_DROPDOWN, index + 1);
        return this;
    }

    /**
     * Get the currently selected device type.
     * @return Selected device type name
     */
    @Step("Get selected device type")
    public String getSelectedDeviceType() {
        waitForVisible(DEVICE_TYPE_DROPDOWN);
        return page.locator(DEVICE_TYPE_DROPDOWN).inputValue();
    }

    /**
     * Enter device name.
     * @param deviceName Device name to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter device name: {deviceName}")
    public DevicePage enterDeviceName(String deviceName) {
        logger.info("Entering device name: {}", deviceName);
        if (isVisible(DEVICE_NAME_INPUT)) {
            clearAndType(DEVICE_NAME_INPUT, deviceName);
        }
        return this;
    }

    /**
     * Enter device ID.
     * @param deviceId Device ID to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter device ID: {deviceId}")
    public DevicePage enterDeviceId(String deviceId) {
        logger.info("Entering device ID: {}", deviceId);
        if (isVisible(DEVICE_ID_INPUT)) {
            clearAndType(DEVICE_ID_INPUT, deviceId);
        }
        return this;
    }

    /**
     * Enter serial number.
     * @param serialNumber Serial number to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter serial number: {serialNumber}")
    public DevicePage enterSerialNumber(String serialNumber) {
        logger.info("Entering serial number: {}", serialNumber);
        if (isVisible(DEVICE_SERIAL_INPUT)) {
            clearAndType(DEVICE_SERIAL_INPUT, serialNumber);
        }
        return this;
    }

    /**
     * Enter device model.
     * @param model Device model to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter device model: {model}")
    public DevicePage enterDeviceModel(String model) {
        logger.info("Entering device model: {}", model);
        if (isVisible(DEVICE_MODEL_INPUT)) {
            clearAndType(DEVICE_MODEL_INPUT, model);
        }
        return this;
    }

    /**
     * Enter device description.
     * @param description Description to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter device description")
    public DevicePage enterDeviceDescription(String description) {
        logger.info("Entering device description");
        if (isVisible(DEVICE_DESCRIPTION_INPUT)) {
            clearAndType(DEVICE_DESCRIPTION_INPUT, description);
        }
        return this;
    }

    /**
     * Select manufacturer/company from dropdown.
     * @param manufacturer Manufacturer name to select
     * @return DevicePage instance for method chaining
     */
    @Step("Select manufacturer: {manufacturer}")
    public DevicePage selectManufacturer(String manufacturer) {
        logger.info("Selecting manufacturer: {}", manufacturer);
        if (isVisible(MANUFACTURER_DROPDOWN)) {
            selectByLabel(MANUFACTURER_DROPDOWN, manufacturer);
        }
        return this;
    }

    /**
     * Get available manufacturers from dropdown.
     * @return List of manufacturer names
     */
    @Step("Get available manufacturers")
    public List<String> getAvailableManufacturers() {
        logger.info("Getting available manufacturers");
        List<String> manufacturers = new ArrayList<>();

        if (isVisible(MANUFACTURER_DROPDOWN)) {
            Locator options = page.locator(MANUFACTURER_OPTIONS);
            int count = options.count();

            for (int i = 0; i < count; i++) {
                String optionText = options.nth(i).textContent();
                if (optionText != null && !optionText.trim().isEmpty() &&
                    !optionText.toLowerCase().contains("select")) {
                    manufacturers.add(optionText.trim());
                }
            }
        }

        logger.info("Found {} manufacturers", manufacturers.size());
        return manufacturers;
    }

    /**
     * Select first available manufacturer.
     * @return DevicePage instance for method chaining
     */
    @Step("Select first available manufacturer")
    public DevicePage selectFirstAvailableManufacturer() {
        logger.info("Selecting first available manufacturer");
        if (isVisible(MANUFACTURER_DROPDOWN)) {
            List<String> manufacturers = getAvailableManufacturers();
            if (!manufacturers.isEmpty()) {
                selectManufacturer(manufacturers.get(0));
            }
        }
        return this;
    }

    /**
     * Enter location.
     * @param location Location to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter location: {location}")
    public DevicePage enterLocation(String location) {
        logger.info("Entering location: {}", location);
        if (isVisible(LOCATION_INPUT)) {
            clearAndType(LOCATION_INPUT, location);
        }
        return this;
    }

    /**
     * Enter MAC address.
     * @param macAddress MAC address to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter MAC address: {macAddress}")
    public DevicePage enterMacAddress(String macAddress) {
        logger.info("Entering MAC address: {}", macAddress);
        if (isVisible(MAC_ADDRESS_INPUT)) {
            clearAndType(MAC_ADDRESS_INPUT, macAddress);
        }
        return this;
    }

    /**
     * Enter IP address.
     * @param ipAddress IP address to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter IP address: {ipAddress}")
    public DevicePage enterIpAddress(String ipAddress) {
        logger.info("Entering IP address: {}", ipAddress);
        if (isVisible(IP_ADDRESS_INPUT)) {
            clearAndType(IP_ADDRESS_INPUT, ipAddress);
        }
        return this;
    }

    /**
     * Enter firmware version.
     * @param version Firmware version to enter
     * @return DevicePage instance for method chaining
     */
    @Step("Enter firmware version: {version}")
    public DevicePage enterFirmwareVersion(String version) {
        logger.info("Entering firmware version: {}", version);
        if (isVisible(FIRMWARE_VERSION_INPUT)) {
            clearAndType(FIRMWARE_VERSION_INPUT, version);
        }
        return this;
    }

    /**
     * Check the Generate Sample Data checkbox.
     * @return DevicePage instance for method chaining
     */
    @Step("Select Generate Sample Data for testing")
    public DevicePage selectGenerateSampleData() {
        logger.info("Selecting Generate Sample Data checkbox");
        try {
            // Try clicking the checkbox directly
            if (isVisible(GENERATE_SAMPLE_DATA_CHECKBOX)) {
                if (!isChecked(GENERATE_SAMPLE_DATA_CHECKBOX)) {
                    click(GENERATE_SAMPLE_DATA_CHECKBOX);
                }
            } else if (isVisible(GENERATE_SAMPLE_DATA_LABEL)) {
                // Try clicking the label
                click(GENERATE_SAMPLE_DATA_LABEL);
            }
        } catch (Exception e) {
            logger.warn("Could not select generate sample data checkbox: {}", e.getMessage());
        }
        return this;
    }

    /**
     * Check if Generate Sample Data is selected.
     * @return true if checkbox is checked
     */
    @Step("Check if Generate Sample Data is selected")
    public boolean isGenerateSampleDataSelected() {
        try {
            return isChecked(GENERATE_SAMPLE_DATA_CHECKBOX);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fill device form with dummy/test data.
     * @param deviceType Device type being registered
     * @return DevicePage instance for method chaining
     */
    @Step("Fill device form with test data for: {deviceType}")
    public DevicePage fillFormWithDummyData(String deviceType) {
        logger.info("Filling device form with dummy data for: {}", deviceType);

        String uniqueId = TestDataUtils.shortUniqueId();
        String deviceName = "Test_" + deviceType.replace(" ", "_") + "_" + uniqueId;
        String deviceId = "DEV-" + TestDataUtils.randomNumeric(8);
        String serialNumber = "SN-" + TestDataUtils.randomAlphanumeric(10).toUpperCase();
        String model = deviceType + " Model " + TestDataUtils.randomNumeric(3);
        String description = "Test device registered for " + deviceType + " testing. ID: " + uniqueId;
        String location = "Test Lab " + TestDataUtils.randomNumeric(2);
        String macAddress = generateRandomMacAddress();
        String ipAddress = generateRandomIpAddress();
        String firmwareVersion = "v" + TestDataUtils.randomInt(1, 9) + "." + TestDataUtils.randomInt(0, 9) + "." + TestDataUtils.randomInt(0, 99);

        enterDeviceName(deviceName);
        enterDeviceId(deviceId);
        enterSerialNumber(serialNumber);
        enterDeviceModel(model);
        enterDeviceDescription(description);
        selectFirstAvailableManufacturer();
        enterLocation(location);
        enterMacAddress(macAddress);
        enterIpAddress(ipAddress);
        enterFirmwareVersion(firmwareVersion);

        return this;
    }

    /**
     * Generate a random MAC address.
     * @return Random MAC address string
     */
    private String generateRandomMacAddress() {
        StringBuilder mac = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (i > 0) mac.append(":");
            mac.append(String.format("%02X", TestDataUtils.randomInt(0, 255)));
        }
        return mac.toString();
    }

    /**
     * Generate a random IP address.
     * @return Random IP address string
     */
    private String generateRandomIpAddress() {
        return "192.168." + TestDataUtils.randomInt(1, 255) + "." + TestDataUtils.randomInt(1, 254);
    }

    /**
     * Click Register Device button to submit the form.
     * @return DevicePage instance for method chaining
     */
    @Step("Click Register Device button")
    public DevicePage clickRegisterDevice() {
        logger.info("Clicking Register Device button");
        waitForVisible(REGISTER_DEVICE_BUTTON);
        click(REGISTER_DEVICE_BUTTON);
        waitForPageLoad();
        waitForLoadingToComplete();
        return this;
    }

    /**
     * Click Cancel button.
     * @return DevicePage instance for method chaining
     */
    @Step("Click Cancel button")
    public DevicePage clickCancel() {
        logger.info("Clicking Cancel button");
        if (isVisible(CANCEL_BUTTON)) {
            click(CANCEL_BUTTON);
        }
        return this;
    }

    /**
     * Check if success message is displayed.
     * @return true if success message is visible
     */
    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        try {
            waitForVisible(SUCCESS_MESSAGE, config.getActionTimeout());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get success message text.
     * @return Success message text
     */
    @Step("Get success message")
    public String getSuccessMessage() {
        if (isSuccessMessageDisplayed()) {
            return getText(SUCCESS_MESSAGE);
        }
        return "";
    }

    /**
     * Check if error message is displayed.
     * @return true if error message is visible
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
    @Step("Get error message")
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getText(ERROR_MESSAGE);
        }
        return "";
    }

    /**
     * Navigate to Health tab.
     * @return DevicePage instance for method chaining
     */
    @Step("Navigate to Health tab")
    public DevicePage navigateToHealthTab() {
        logger.info("Navigating to Health tab");
        if (isVisible(HEALTH_TAB)) {
            click(HEALTH_TAB);
            waitForPageLoad();
            waitForLoadingToComplete();
        }
        return this;
    }

    /**
     * Navigate to Activity tab.
     * @return DevicePage instance for method chaining
     */
    @Step("Navigate to Activity tab")
    public DevicePage navigateToActivityTab() {
        logger.info("Navigating to Activity tab");
        if (isVisible(ACTIVITY_TAB)) {
            click(ACTIVITY_TAB);
            waitForPageLoad();
            waitForLoadingToComplete();
        }
        return this;
    }

    /**
     * Navigate to Overview tab.
     * @return DevicePage instance for method chaining
     */
    @Step("Navigate to Overview tab")
    public DevicePage navigateToOverviewTab() {
        logger.info("Navigating to Overview tab");
        if (isVisible(OVERVIEW_TAB)) {
            click(OVERVIEW_TAB);
            waitForPageLoad();
            waitForLoadingToComplete();
        }
        return this;
    }

    /**
     * Check if Health widget is displayed with data.
     * @return true if Health widget is visible
     */
    @Step("Check if Health widget is displayed")
    public boolean isHealthWidgetDisplayed() {
        return isVisible(HEALTH_WIDGET) || isVisible(DATA_CHART);
    }

    /**
     * Check if Activity widget is displayed with data.
     * @return true if Activity widget is visible
     */
    @Step("Check if Activity widget is displayed")
    public boolean isActivityWidgetDisplayed() {
        return isVisible(ACTIVITY_WIDGET) || isVisible(DATA_TABLE);
    }

    /**
     * Check if data is present in the current tab.
     * @return true if data rows are found
     */
    @Step("Check if data is present")
    public boolean isDataPresent() {
        try {
            return page.locator(DATA_ROWS).count() > 0 ||
                   isVisible(DATA_CHART) ||
                   isVisible(HEALTH_WIDGET) ||
                   isVisible(ACTIVITY_WIDGET);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count of data rows.
     * @return Number of data rows
     */
    @Step("Get data row count")
    public int getDataRowCount() {
        try {
            return page.locator(DATA_ROWS).count();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Verify generated sample data in tabs and widgets.
     * @return true if data is found in Health and Activity sections
     */
    @Step("Verify generated sample data")
    public boolean verifyGeneratedSampleData() {
        logger.info("Verifying generated sample data");

        boolean healthDataFound = false;
        boolean activityDataFound = false;

        // Check Health tab
        navigateToHealthTab();
        healthDataFound = isHealthWidgetDisplayed() || isDataPresent();
        logger.info("Health data found: {}", healthDataFound);

        // Check Activity tab
        navigateToActivityTab();
        activityDataFound = isActivityWidgetDisplayed() || isDataPresent();
        logger.info("Activity data found: {}", activityDataFound);

        return healthDataFound || activityDataFound;
    }

    /**
     * Check if device list is displayed.
     * @return true if device list is visible
     */
    @Step("Check if device list is displayed")
    public boolean isDeviceListDisplayed() {
        return isVisible(DEVICE_LIST) || isVisible(DEVICE_CARD);
    }

    /**
     * Get registered device count.
     * @return Number of registered devices
     */
    @Step("Get registered device count")
    public int getRegisteredDeviceCount() {
        try {
            return page.locator(DEVICE_CARD).count();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Check if Register New Device button is displayed.
     * @return true if button is visible
     */
    @Step("Check if Register New Device button is displayed")
    public boolean isRegisterNewDeviceButtonDisplayed() {
        return isVisible(REGISTER_NEW_DEVICE_BUTTON);
    }

    /**
     * Check if Device tab is displayed.
     * @return true if Device tab is visible
     */
    @Step("Check if Device tab is displayed")
    public boolean isDeviceTabDisplayed() {
        return isVisible(DEVICE_TAB);
    }

    /**
     * Register a device with specified type and dummy data.
     * Complete registration workflow.
     * @param deviceType Device type to register
     * @return DevicePage instance for method chaining
     */
    @Step("Register device of type: {deviceType}")
    public DevicePage registerDevice(String deviceType) {
        logger.info("Registering device of type: {}", deviceType);

        clickRegisterNewDevice();
        selectDeviceType(deviceType);
        fillFormWithDummyData(deviceType);
        selectGenerateSampleData();
        clickRegisterDevice();

        return this;
    }

    /**
     * Register all available device types.
     * @return List of registered device types
     */
    @Step("Register all available device types")
    public List<String> registerAllDeviceTypes() {
        logger.info("Registering all available device types");

        List<String> registeredTypes = new ArrayList<>();

        // Get available device types first
        clickRegisterNewDevice();
        List<String> deviceTypes = getAvailableDeviceTypes();
        clickCancel();

        for (String deviceType : deviceTypes) {
            logger.info("Registering device type: {}", deviceType);

            try {
                registerDevice(deviceType);

                // Verify registration success
                if (isSuccessMessageDisplayed() || !isErrorMessageDisplayed()) {
                    registeredTypes.add(deviceType);
                    logger.info("Successfully registered: {}", deviceType);
                } else {
                    logger.warn("Failed to register: {}", deviceType);
                }

                // Small wait between registrations
                page.waitForTimeout(1000);

            } catch (Exception e) {
                logger.error("Error registering device type {}: {}", deviceType, e.getMessage());
            }
        }

        logger.info("Registered {} out of {} device types", registeredTypes.size(), deviceTypes.size());
        return registeredTypes;
    }
}