# SymBIoT Test Automation Framework

A scalable, maintainable Playwright automation testing framework using Java with Maven for testing the SymBIoT application.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Framework Features](#framework-features)
- [Writing New Tests](#writing-new-tests)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)

## Technology Stack

| Component | Technology |
|-----------|------------|
| Programming Language | Java 11+ |
| Build Tool | Maven |
| Browser Automation | Playwright for Java |
| Test Runner | TestNG |
| Reporting | Allure Reports |
| Logging | Log4j2 |
| Design Pattern | Page Object Model (POM) |

## Project Structure

```
symbiot-test-automation/
├── pom.xml                           # Maven configuration
├── testng.xml                        # Default TestNG suite
├── testng-smoke.xml                  # Smoke tests suite
├── testng-regression.xml             # Regression tests suite
├── testng-crossbrowser.xml           # Cross-browser tests
├── testng-parallel.xml               # Parallel execution suite
├── README.md                         # This file
│
├── src/
│   ├── main/java/com/symbiot/
│   │   ├── base/
│   │   │   ├── BasePage.java        # Base page object class
│   │   │   └── BaseTest.java        # Base test class
│   │   ├── config/
│   │   │   ├── BrowserConfig.java   # Browser configuration
│   │   │   └── ConfigManager.java   # Configuration management
│   │   ├── listeners/
│   │   │   ├── RetryAnalyzer.java   # Test retry mechanism
│   │   │   └── TestListener.java    # TestNG listener
│   │   ├── pages/
│   │   │   ├── HomePage.java        # Home page object
│   │   │   ├── LoginPage.java       # Login page object
│   │   │   └── components/
│   │   │       └── NavigationComponent.java
│   │   └── utils/
│   │       ├── Constants.java       # Framework constants
│   │       ├── ScreenshotUtils.java # Screenshot utilities
│   │       ├── TestDataUtils.java   # Test data generation
│   │       └── WaitUtils.java       # Wait utilities
│   │
│   └── test/
│       ├── java/com/symbiot/tests/
│       │   ├── ApplicationLaunchTest.java
│       │   ├── LoginTest.java
│       │   ├── LogoutTest.java
│       │   └── UIValidationTest.java
│       │
│       └── resources/
│           ├── config/
│           │   ├── config.properties    # Default configuration
│           │   ├── qa.properties        # QA environment
│           │   ├── staging.properties   # Staging environment
│           │   └── prod.properties      # Production environment
│           ├── allure.properties        # Allure configuration
│           ├── categories.json          # Allure test categories
│           ├── environment.properties   # Environment info for reports
│           └── log4j2.xml              # Logging configuration
│
└── target/                          # Build output (generated)
    ├── allure-results/              # Allure raw results
    ├── logs/                        # Test execution logs
    ├── screenshots/                 # Failure screenshots
    ├── videos/                      # Test videos
    └── traces/                      # Playwright traces
```

## Prerequisites

1. **Java 11 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.6 or higher**
   ```bash
   mvn -version
   ```

3. **Allure Command Line (optional, for viewing reports)**
   ```bash
   # Windows (using Scoop)
   scoop install allure

   # macOS (using Homebrew)
   brew install allure

   # Or download from: https://github.com/allure-framework/allure2/releases
   ```

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd symbiot-test-automation
   ```

2. **Install dependencies and Playwright browsers**
   ```bash
   mvn clean install -DskipTests
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```

3. **Verify installation**
   ```bash
   mvn test -Dtest=ApplicationLaunchTest#testApplicationLaunch
   ```

## Configuration

### Environment Configuration

Edit `src/test/resources/config/config.properties` to customize settings:

```properties
# Application URL
base.url=https://symbiot.faberwork.com/

# Browser settings
browser=chromium          # chromium, firefox, webkit
headless=false            # true for CI/CD
slow.motion=0             # milliseconds between actions

# Viewport
viewport.width=1920
viewport.height=1080

# Timeouts (milliseconds)
default.timeout=30000
navigation.timeout=60000

# Retry configuration
retry.count=2

# Screenshots and video
screenshot.on.failure=true
video.on.failure=true
```

### Multi-Environment Support

Run tests against different environments:

```bash
# QA Environment
mvn test -Denv=qa

# Staging Environment
mvn test -Denv=staging

# Production Environment
mvn test -Denv=prod
```

## Running Tests

### Basic Commands

```bash
# Run all tests
mvn test

# Run with specific TestNG suite
mvn test -Dsuite.file=testng-smoke.xml

# Run specific test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#testValidLogin
```

### Browser Selection

```bash
# Run with Chromium (default)
mvn test -Dbrowser=chromium

# Run with Firefox
mvn test -Dbrowser=firefox

# Run with WebKit (Safari)
mvn test -Dbrowser=webkit
```

### Execution Modes

```bash
# Headed mode (with browser UI)
mvn test -Dheadless=false

# Headless mode (no browser UI - for CI/CD)
mvn test -Dheadless=true
```

### Test Profiles

```bash
# Smoke tests
mvn test -Psmoke

# Regression tests
mvn test -Pregression

# Cross-browser tests
mvn test -Pcross-browser

# Parallel execution
mvn test -Pparallel
```

### Test Groups

```bash
# Run only smoke tests
mvn test -Dgroups=smoke

# Run only login tests
mvn test -Dgroups=login

# Run multiple groups
mvn test -Dgroups="smoke,ui"

# Exclude groups
mvn test -DexcludedGroups=regression
```

### Combined Commands

```bash
# Smoke tests on Firefox in headless mode for QA environment
mvn test -Psmoke -Dbrowser=firefox -Dheadless=true -Denv=qa

# Full regression on CI/CD
mvn clean test -Pregression -Dheadless=true -Denv=staging
```

## Test Reports

### Allure Reports

```bash
# Generate and open Allure report
mvn allure:serve

# Generate report without opening
mvn allure:report
# Report will be at: target/site/allure-maven-plugin/index.html
```

### Log Files

Logs are generated in `target/logs/`:
- `test-automation.log` - All logs
- `test-errors.log` - Error logs only
- `test-debug.log` - Debug level logs

### Screenshots and Videos

On test failure:
- Screenshots: `target/screenshots/`
- Videos: `target/videos/`
- Traces: `target/traces/`

## Framework Features

### Browser Support
- Chromium (Chrome, Edge)
- Firefox
- WebKit (Safari)

### Execution Features
- Headed and headless modes
- Parallel execution support
- Cross-browser testing
- Retry mechanism for flaky tests

### Reporting
- Allure Reports with screenshots
- Video recording on failure
- Playwright traces for debugging
- Comprehensive logging

### Configuration
- Multi-environment support (QA, Staging, Prod)
- Centralized configuration via properties files
- Command-line overrides

## Writing New Tests

### 1. Create a Page Object

```java
package com.symbiot.pages;

import com.microsoft.playwright.Page;
import com.symbiot.base.BasePage;
import io.qameta.allure.Step;

public class MyNewPage extends BasePage {

    // Selectors
    private static final String ELEMENT_SELECTOR = "#my-element";

    public MyNewPage(Page page) {
        super(page);
    }

    @Step("Perform action on element")
    public MyNewPage performAction() {
        click(ELEMENT_SELECTOR);
        return this;
    }
}
```

### 2. Create a Test Class

```java
package com.symbiot.tests;

import com.symbiot.base.BaseTest;
import com.symbiot.pages.MyNewPage;
import com.symbiot.utils.Constants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SymBIoT Application")
@Feature("My Feature")
public class MyNewTest extends BaseTest {

    @Test(groups = {Constants.SMOKE})
    @Severity(SeverityLevel.NORMAL)
    @Description("Test description")
    public void testMyFeature() {
        MyNewPage page = new MyNewPage(getPage());
        // Test implementation
    }
}
```

### 3. Add to TestNG Suite

```xml
<test name="My New Tests">
    <classes>
        <class name="com.symbiot.tests.MyNewTest"/>
    </classes>
</test>
```

## Best Practices

### Do's
- Use Page Object Model for all pages
- Use meaningful test names and descriptions
- Add Allure annotations for better reporting
- Use test groups for categorization
- Use data providers for data-driven tests
- Log important actions
- Use Playwright's built-in waits

### Don'ts
- Don't use hard-coded waits (Thread.sleep)
- Don't hard-code test data
- Don't write tests with dependencies on other tests
- Don't ignore test failures
- Don't commit sensitive data (credentials)

## Troubleshooting

### Common Issues

**1. Playwright browsers not installed**
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

**2. Tests failing with timeout**
- Increase timeout in config.properties
- Check network connectivity
- Verify application is accessible

**3. Element not found**
- Verify selectors are correct
- Add appropriate waits
- Check if element is in iframe

**4. Allure report not generating**
```bash
mvn clean test
mvn allure:serve
```

### Debug Mode

Run with slow motion to see actions:
```bash
mvn test -Dslow.motion=1000
```

View Playwright traces:
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace target/traces/trace.zip"
```

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Test Automation

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - name: Install Playwright
        run: mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
      - name: Run Tests
        run: mvn test -Dheadless=true
      - name: Generate Allure Report
        if: always()
        run: mvn allure:report
      - name: Upload Report
        uses: actions/upload-artifact@v3
        with:
          name: allure-report
          path: target/site/allure-maven-plugin/
```

## License

This project is licensed under the MIT License.

## Support

For issues or questions, please create an issue in the repository.