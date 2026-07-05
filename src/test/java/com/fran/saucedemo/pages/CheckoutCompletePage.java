package com.fran.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutCompletePage {
    private final Locator title;
    private final Locator completeHeader;

    public CheckoutCompletePage(Page page) {
        this.title = page.locator("[data-test='title']");
        this.completeHeader = page.locator("[data-test='complete-header']");
    }

    public boolean isLoaded() {
        return title.textContent().equals("Checkout: Complete!");
    }

    public String confirmationMessage() {
        return completeHeader.textContent();
    }
}
