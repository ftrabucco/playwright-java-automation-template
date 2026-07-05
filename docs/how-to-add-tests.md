# How To Add Tests

## 1. Start With Behavior

Write the scenario in plain language first:

```text
Given a standard user is logged in
When the user adds a product to the cart
Then the cart badge shows one item
```

## 2. Add Page Methods

Put selectors and UI interaction inside Page Objects.

Good:

```java
inventoryPage.addProductToCart("Sauce Labs Backpack");
```

Avoid:

```java
page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
```

## 3. Keep Data Out Of Tests

Reusable users, checkout data and product constants belong in fixtures or models.

## 4. Tag The Test

Use feature tags and, when appropriate, execution tags:

```java
@Tag("checkout")
@Tag("smoke")
```

## 5. Run Focused, Then Full

```bash
mvn test -Dgroups=checkout
mvn test
```

## Review Checklist

- The test name describes user behavior.
- There are no selectors in the test class.
- Assertions verify business outcomes.
- The test does not depend on execution order.
- The test can run headless and in CI.
