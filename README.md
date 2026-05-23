# 🧪 Selenium POM Automation Framework

[![Java](https://img.shields.io/badge/Java-11-ED8B00?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18-43B02A?style=flat-square&logo=selenium)](https://selenium.dev)
[![TestNG](https://img.shields.io/badge/TestNG-7.9-FF6C37?style=flat-square)](https://testng.org)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apache-maven)](https://maven.apache.org/)
[![CI](https://img.shields.io/badge/CI-GitHub_Actions-2088FF?style=flat-square&logo=github-actions)](/.github/workflows/selenium-ci.yml)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

> **Production-grade Selenium automation framework** built with Page Object Model (POM), Data-Driven, and Hybrid design patterns. Designed to scale — supports parallel execution, cross-browser testing, CI/CD pipelines, and rich ExtentReports out of the box.

**Author:** Prachi Dahibhate — [LinkedIn](https://linkedin.com/in/prachi-dahibhate-testing) | [Email](mailto:dahibhateprachi@gmail.com)

---

## ✨ Features

| Feature | Detail |
|---|---|
| 🏗️ **Design Pattern** | Page Object Model (POM) + Hybrid |
| 📊 **Reporting** | ExtentReports 5 with screenshots on failure |
| 🔄 **Data-Driven** | Apache POI — Excel-based test data |
| ⚡ **Parallel Execution** | ThreadLocal WebDriver — thread-safe |
| 🌐 **Cross-Browser** | Chrome · Firefox · Edge |
| ☁️ **Remote Execution** | Selenium Grid / remote WebDriver support |
| 🤖 **CI/CD** | GitHub Actions — nightly + PR triggered |
| 🔁 **Retry** | Auto-retry flaky tests (configurable count) |
| 📸 **Screenshots** | Auto-capture on test failure |
| 📝 **Logging** | Log4j2 structured logging |
| 🎭 **Headless** | Headless mode support for CI |

---

## 📁 Project Structure

```
selenium-pom-framework/
├── src/
│   ├── main/
│   │   ├── java/com/prachi/
│   │   │   ├── config/         # ConfigReader (singleton)
│   │   │   ├── pages/          # Page Objects (BasePage + all pages)
│   │   │   ├── utils/          # DriverManager, ExcelUtils, ExtentReportManager, ScreenshotUtils
│   │   │   └── listeners/      # TestListener, RetryAnalyzer
│   │   └── resources/
│   │       └── config.properties
│   └── test/
│       ├── java/com/prachi/
│       │   └── tests/          # BaseTest + all test classes
│       └── resources/
│           ├── testng.xml
│           └── testdata/
│               └── TestData.xlsx
├── reports/                    # ExtentReport output + screenshots
├── .github/workflows/
│   └── selenium-ci.yml
└── pom.xml
```

---

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Chrome / Firefox / Edge

### 1. Clone the repository
```bash
git clone https://github.com/PDahibhate/selenium-pom-framework.git
cd selenium-pom-framework
```

### 2. Run all tests
```bash
mvn clean test
```

### 3. Run with specific browser
```bash
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

### 4. Run in headless mode (for CI)
```bash
mvn clean test -Dheadless=true
```

### 5. Override base URL
```bash
mvn clean test -Dbase.url=https://your-staging-url.com
```

---

## ⚙️ Configuration

All settings in `src/main/resources/config.properties`.
Any property can be overridden via Maven `-D` flags at runtime.

```properties
base.url=https://www.saucedemo.com
browser=chrome          # chrome | firefox | edge
run.mode=local          # local | remote
headless=false
implicit.wait=10
explicit.wait=15
screenshot.on.failure=true
retry.count=1
```

---

## 📊 Test Reports

After test execution, open the HTML report:

```
reports/ExtentReport.html
```

Reports include:
- ✅ Pass / ❌ Fail / ⏭ Skip status per test
- 📸 Embedded screenshots on failure
- ⏱ Execution time per test
- 🖥 System info (browser, OS, Java version)

---

## 📋 Test Suites

| Suite | Tests | Description |
|---|---|---|
| Smoke | 1 | Quick sanity — valid login only |
| Regression Login | 6 | All login scenarios incl. data-driven |
| Regression Checkout | 3 | E2E checkout flow |

---

## 🔧 Key Design Decisions

**ThreadLocal WebDriver** — enables truly parallel test execution without tests interfering with each other.

**Singleton ConfigReader** — config loaded once, available everywhere, CLI args override file properties for flexible CI usage.

**BasePage fluent actions** — all WebElement interactions are wrapped with explicit waits, making tests resilient to timing issues without sleep() calls.

**TestNG Listener** — lifecycle hooks for reporting and screenshots are decoupled from test classes, keeping test code clean.

---

## 📞 Contact

**Prachi Dahibhate** — Senior QA Engineer / SDET  
📧 dahibhateprachi@gmail.com  
💼 [LinkedIn](https://linkedin.com/in/prachi-dahibhate-testing)

> 🚀 Open to Senior QA / SDET opportunities in product-led tech teams.
