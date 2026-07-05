# Parallel Execution

This framework uses JUnit 5 parallel execution. Playwright controls the browser automation, but
JUnit owns the test scheduling.

The execution flow is:

```text
Maven
  -> Surefire
    -> JUnit 5
      -> tests running in parallel
        -> one PlaywrightSession per test
          -> Browser
          -> BrowserContext
          -> Page
```

## Current Configuration

Parallel execution is enabled in `src/test/resources/junit-platform.properties`:

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=concurrent
junit.jupiter.execution.parallel.mode.classes.default=concurrent
```

This means JUnit can run test classes and test methods concurrently.

## Test Isolation

Each test creates its own `PlaywrightSession` through `BaseTest`.

That session owns:

- an isolated `BrowserContext`
- an isolated `Page`
- an isolated artifacts directory under `target/artifacts`

This avoids shared browser state between tests. Cookies, local storage, page navigation and artifacts
belong to the test that created them.

## Change Parallel Workers

Use a fixed parallelism value when you want to control how many tests can run at the same time:

```bash
mvn test \
  -Djunit.jupiter.execution.parallel.config.strategy=fixed \
  -Djunit.jupiter.execution.parallel.config.fixed.parallelism=2
```

Increase the value for faster feedback:

```bash
mvn test \
  -Djunit.jupiter.execution.parallel.config.strategy=fixed \
  -Djunit.jupiter.execution.parallel.config.fixed.parallelism=4
```

Use lower values when debugging, when the machine is under load, or when the application under test
does not tolerate too many concurrent sessions.

## Run One Test At A Time

Disable parallel execution:

```bash
mvn test -Djunit.jupiter.execution.parallel.enabled=false
```

Or run only one selected test class:

```bash
mvn test -Dtest=LoginTest
```

Or run one selected test method:

```bash
mvn test -Dtest=LoginTest#standardUserCanLogIn
```

## Debug With The Browser Visible

Run headed by setting `headless=false`:

```bash
mvn test -Dtest=LoginTest -Dheadless=false
```

For easier visual debugging, combine headed mode, single-thread execution and slow motion:

```bash
mvn test \
  -Dtest=LoginTest \
  -Dheadless=false \
  -Dslow.motion.ms=250 \
  -Djunit.jupiter.execution.parallel.enabled=false
```

## Java Vs Selenium

In Selenium frameworks, parallel execution often requires careful `WebDriver` isolation. A common
approach is `ThreadLocal<WebDriver>` so tests do not share the same browser session.

In this Playwright Java framework, isolation is centered around `BrowserContext` and `Page` per test.
The idea is similar: no shared mutable browser state between concurrent tests.

## Java Vs Playwright TypeScript

With Playwright TypeScript and `@playwright/test`, the official runner provides workers, fixtures,
screenshots, traces and the HTML report out of the box.

In Java, Playwright is used as the browser automation library, while JUnit 5 is the runner. That is
why this framework explicitly owns:

- test lifecycle in `BaseTest`
- browser/session creation in `PlaywrightSession`
- parallel scheduling through JUnit 5
- screenshots, traces and reporting integration

This makes the lifecycle more explicit, which is useful for a reusable Java automation template.
