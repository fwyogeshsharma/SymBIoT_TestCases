package com.symbiot.listeners;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for handling test events and logging.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Started: {}", context.getName());
        logger.info("========================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Finished: {}", context.getName());
        logger.info("Total Tests: {}", context.getAllTestMethods().length);
        logger.info("Passed: {}", context.getPassedTests().size());
        logger.info("Failed: {}", context.getFailedTests().size());
        logger.info("Skipped: {}", context.getSkippedTests().size());
        logger.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("----------------------------------------");
        logger.info("Test Started: {}", getTestMethodName(result));
        logger.info("Description: {}", result.getMethod().getDescription());

        // Add test info to Allure report
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: {}", getTestMethodName(result));
        logger.info("Duration: {} ms", result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test FAILED: {}", getTestMethodName(result));
        logger.error("Duration: {} ms", result.getEndMillis() - result.getStartMillis());

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            logger.error("Failure Reason: {}", throwable.getMessage());
            logger.error("Stack Trace:", throwable);

            // Attach exception to Allure report
            Allure.addAttachment("Exception", throwable.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test SKIPPED: {}", getTestMethodName(result));

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            logger.warn("Skip Reason: {}", throwable.getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test FAILED but within success percentage: {}", getTestMethodName(result));
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.error("Test FAILED with TIMEOUT: {}", getTestMethodName(result));
    }

    /**
     * Get fully qualified test method name.
     * @param result Test result
     * @return Fully qualified method name
     */
    private String getTestMethodName(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName();
    }
}