package com.symbiot.listeners;

import com.symbiot.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for automatically retrying failed tests.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private final int maxRetryCount;

    public RetryAnalyzer() {
        this.maxRetryCount = ConfigManager.getInstance().getRetryCount();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("Retrying test: {} - Attempt {} of {}",
                    result.getName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }

    /**
     * Get current retry count.
     * @return Current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Get maximum retry count.
     * @return Maximum retry count
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }
}