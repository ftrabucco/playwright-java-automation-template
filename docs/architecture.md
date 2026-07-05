# Architecture

This framework keeps tests readable by separating business behavior from browser mechanics.

## Main Layers

```text
Tests -> Page Objects -> Component Objects -> Playwright
      -> Fixtures / Models
      -> Core / Config
```

## Tests

Test classes describe user behavior and assertions. They should not contain CSS selectors or low-level browser setup.

Example:

```java
CheckoutCompletePage confirmationPage = new LoginPage(page())
        .open()
        .loginAs(Users.STANDARD)
        .addProductToCart("Sauce Labs Backpack")
        .openCart()
        .checkout()
        .continueWith(CheckoutData.DEFAULT_CUSTOMER)
        .finishOrder();
```

## Page Objects

Page Objects encapsulate screen-level behavior. They know the locators and expose methods in user/business language.

Guidelines:

- Keep selectors private.
- Return the next page when navigation happens.
- Return `this` for actions that keep the user on the same page.
- Avoid assertions unless they are page-specific helpers.

## Component Objects

Components model reusable UI fragments, such as headers, sidebars, menus or cart widgets. This prevents duplication across pages.

## Core

The `core` package owns the technical lifecycle:

- Playwright startup and shutdown.
- Browser selection.
- Browser context creation.
- Trace and screenshot evidence.
- JUnit base test integration.

## Configuration

`TestConfig` reads `config.properties` and allows Maven system property overrides. This keeps tests portable across browsers, environments and CI.

## Design Principles

- Single Responsibility: tests assert behavior; pages interact with UI; core manages infrastructure.
- Open/Closed: new pages and flows can be added without changing existing tests.
- Dependency Inversion: tests depend on framework abstractions instead of Playwright setup details.
- DRY with restraint: abstractions are introduced only when they remove real duplication.
