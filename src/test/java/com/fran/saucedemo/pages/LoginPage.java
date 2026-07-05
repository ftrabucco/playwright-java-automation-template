package com.fran.saucedemo.pages;

import com.fran.saucedemo.models.User;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameInput = page.locator("[data-test='username']");
        this.passwordInput = page.locator("[data-test='password']");
        this.loginButton = page.locator("[data-test='login-button']");
        this.errorMessage = page.locator("[data-test='error']");
    }

    public LoginPage open() {
        page.navigate("/");
        return this;
    }

    public InventoryPage loginAs(User user) {
        usernameInput.fill(user.username());
        passwordInput.fill(user.password());
        loginButton.click();
        return new InventoryPage(page);
    }

    public LoginPage submitInvalidLogin(User user) {
        usernameInput.fill(user.username());
        passwordInput.fill(user.password());
        loginButton.click();
        return this;
    }

    public String errorMessage() {
        return errorMessage.textContent();
    }
}
