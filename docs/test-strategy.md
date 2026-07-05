# Test Strategy

## Goal

Provide a maintainable E2E automation template that validates critical user journeys while demonstrating professional SDET practices.

## Scope

Covered:

- Authentication happy and negative paths.
- Inventory sorting and cart behavior.
- Checkout happy path.
- Checkout validation errors.
- Price summary calculations.

Not covered:

- Backend/API contract validation.
- Visual regression.
- Accessibility testing.
- Performance testing.
- Test data creation through APIs.

Those can be added as separate layers when the target product requires them.

## Test Types

Smoke tests validate the smallest high-value subset that should run on every PR.

Regression tests cover broader functional behavior and can run on schedule, before release or manually.

## Tags

Current tags:

- `smoke`
- `login`
- `inventory`
- `cart`
- `checkout`

Examples:

```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=checkout
```

## Data Strategy

Static fixtures are used because SauceDemo provides stable demo users and products. In real products, prefer one of these options:

- API-created data for each test.
- Dedicated seeded test accounts.
- Disposable data with cleanup.

Avoid tests depending on execution order.

## Reliability Strategy

- Use Playwright locators and auto-waits instead of `Thread.sleep`.
- Isolate browser context per test.
- Keep tests independent.
- Capture screenshots and Playwright traces for failed tests by default.
- Allow full-run screenshot/trace evidence through opt-in configuration.
- Use retries only as a CI safety net, not as a replacement for fixing flaky tests.

Parallel execution is documented in [parallel-execution.md](parallel-execution.md).
Reporting trade-offs are documented in [reporting.md](reporting.md).

## Browser Strategy

Local development usually runs on Chromium for speed. CI can run a smoke/regression matrix against Chromium, Firefox and WebKit depending on cost and product risk.
