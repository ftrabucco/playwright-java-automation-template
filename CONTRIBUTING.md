# Contributing

## Local Setup

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
mvn test
```

## Coding Guidelines

- Keep tests behavior-focused.
- Keep locators inside pages/components.
- Prefer Playwright auto-waits over manual sleeps.
- Use typed fixtures/models for reusable test data.
- Add meaningful JUnit tags.
- Capture new framework decisions in `docs/`.

## Pull Request Checklist

- Tests pass locally.
- New behavior has test coverage.
- Documentation is updated when framework behavior changes.
- CI artifacts remain useful for debugging failures.
