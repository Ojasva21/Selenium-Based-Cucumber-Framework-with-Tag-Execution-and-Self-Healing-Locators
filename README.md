# AI-Assisted Selenium-Cucumber Automation Framework

An intelligent Selenium automation framework built using Java, Cucumber, TestNG, and Maven that automatically recovers from broken web element locators using multiple self-healing strategies.

The framework combines traditional similarity-based locator recovery with LLM-assisted healing and supports enterprise features such as Jenkins integration, Extent Reports, execution history management, and report archiving.

---

## Features

- Selenium WebDriver with Java
- Cucumber (BDD) implementation
- TestNG execution
- Page Object Model (POM)
- Smart Element Finder
- Non-AI Self-Healing
- LLM-Based Self-Healing
- Hybrid Healing Mode
- Configurable healing strategy
- Extent Reports
- AI Healing Reports
- Automatic screenshot capture
- Jenkins CI/CD integration
- Cucumber tag execution
- Execution history tracking
- Automatic report archiving
- Automation dashboard for historical reports

---

## Technology Stack

| Technology | Version |
|------------|----------|
| Java | 17 |
| Selenium WebDriver | 4.x |
| Cucumber | 7.x |
| TestNG | 7.x |
| Maven | Latest |
| Log4j | 2.x |
| Jenkins | Latest |
| Extent Reports | 5.x |
| Ollama | Latest |
| Llama 3 | 8B |

---

## Project Structure

```
src
│
├── main
│   ├── driver
│   ├── healing
│   │   ├── ai
│   │   ├── llm
│   ├── pages
│   ├── reports
│   ├── utils
│
├── test
│   ├── features
│   ├── hooks
│   ├── runner
│   ├── stepDefinitions
│
├── pom.xml
├── Jenkinsfile
└── testng.xml
```

---

## Healing Modes

### Non-AI Healing

Uses similarity analysis to recover broken locators by comparing:

- ID
- Name
- XPath
- CSS Selector
- Class
- Href
- Visible Text

A confidence score is calculated for every candidate element before selecting the best match.

---

### LLM Healing

Uses a Large Language Model to predict alternative locators based on:

- DOM structure
- HTML attributes
- Button text
- Page context
- Element hierarchy

Predicted locators are validated before execution continues.

---

### Hybrid Healing

Combines both approaches.

1. Attempt Non-AI recovery.
2. If confidence is insufficient, invoke the LLM.
3. Validate the predicted locator.
4. Resume execution.

---

## Running the Framework

Clone the repository

```bash
git clone <repository-url>
```

Install dependencies

```bash
mvn clean install
```

Run all scenarios

```bash
mvn clean test
```

Run a specific tag

```bash
mvn clean test -Dcucumber.filter.tags="@novocare"
```

---

## Configuration

Framework configuration is available in:

```
src/test/resources/config.properties
```

Example

```properties
browser=chrome

baseUrl=https://www.novocare.com/diabetes/home.html

implicitWait=10

explicitWait=15

healing.enabled=true

healing.mode=HYBRID
```

Supported healing modes:

- NON_AI
- LLM_AI
- HYBRID

---

## Reporting

The framework automatically generates:

- Extent Report
- AI Healing Report
- Execution History
- Archived Reports
- Automation Dashboard

Reports are available inside:

```
test-output/
```

---

## Jenkins Integration

The project supports parameterized execution using Jenkins.

Example:

```
@novocare

@regression

@home

@products

@offerings
```

Pipeline execution automatically:

- Executes selected scenarios
- Generates reports
- Archives reports
- Stores execution history

---

## Self-Healing Workflow

```
Primary Locator

        │

        ▼

Locator Failure

        │

        ▼

SmartElementFinder

        │

────────────────────────────

Healing Mode Selected

│

├── Non-AI

├── LLM

└── Hybrid

        │

Recovered Locator

        │

Continue Execution

        │

Generate Reports
```

---

## Current Capabilities

✔ Selenium WebDriver

✔ Cucumber BDD

✔ TestNG

✔ Page Object Model

✔ Non-AI Healing

✔ LLM Healing

✔ Hybrid Healing

✔ Jenkins Integration

✔ Extent Reports

✔ AI Healing Reports

✔ Screenshot Capture

✔ Report Archiving

✔ Execution History

✔ Automation Dashboard

---

## Future Enhancements

- BrowserStack integration
- Docker support
- Parallel execution
- Spring Boot dashboard
- Automatic locator update
- Database-backed execution history
- Email notifications
- Slack integration

---

## Author

**Ojasva Singh Ahalawat**

Developed as part of an internship project focusing on AI-assisted web automation and intelligent self-healing techniques.