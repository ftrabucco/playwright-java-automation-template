# Reporting

## Current Decision

This Java template uses Allure as the main test report and Playwright traces as debugging artifacts.

## Why Allure For Java

Playwright Java provides browser automation APIs, tracing, screenshots and videos, but it does not include the same built-in test runner and HTML report available in the TypeScript ecosystem through `@playwright/test`.

Because this framework uses JUnit 5 as the runner, Allure is a better fit for the main report:

- It integrates naturally with JUnit.
- It displays test names, suites, labels, tags and history.
- It supports screenshots and file attachments.
- It can be published to GitHub Pages.
- It keeps the framework aligned with common Java QA Automation/SDET stacks.

## Playwright Trace Viewer

Playwright traces are generated as `trace.zip` files. They are attached to Allure as downloadable artifacts, but Allure does not render them inline.

Open a trace locally with:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace target/artifacts/<test-folder>/trace.zip"
```

Or upload/download a trace and open it with:

```text
https://trace.playwright.dev
```

The Trace Viewer is the best tool for inspecting:

- action timeline
- DOM snapshots
- screenshots per action
- network traffic
- console logs
- Playwright action logs

## Why Not Playwright HTML Report Here

The Playwright HTML report is excellent when using TypeScript/JavaScript with the official `@playwright/test` runner:

```bash
npx playwright test
npx playwright show-report
```

That runner owns the test lifecycle, retries, fixtures, traces and report generation.

In this Java framework, JUnit owns the test lifecycle. Playwright is used as the browser automation library. Therefore, there is no equivalent first-class Playwright HTML report for this exact stack.

## Trade-Offs

| Option | Pros | Cons |
| --- | --- | --- |
| Allure + Playwright traces | Strong Java/JUnit fit, good portfolio report, screenshots visible, traces downloadable | Trace zip is not rendered inline |
| Playwright HTML Report with TypeScript | Best native Playwright reporting experience, integrated trace links, excellent runner fixtures | Requires TypeScript/JavaScript stack instead of Java/JUnit |
| Custom trace links in Allure/GitHub Pages | More convenient trace opening from published reports | More moving parts, requires publishing trace files and handling URL/CORS concerns |

## Future Improvement

A useful enhancement would be publishing trace files alongside the Allure report and adding direct links to `trace.playwright.dev` for each trace. That would preserve Allure as the main Java report while making trace inspection more convenient.
