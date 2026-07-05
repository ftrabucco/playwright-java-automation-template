package com.fran.saucedemo.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HeaderComponent {
    private final Locator cartBadge;
    private final Locator cartLink;

    public HeaderComponent(Page page) {
        this.cartBadge = page.locator("[data-test='shopping-cart-badge']");
        this.cartLink = page.locator("[data-test='shopping-cart-link']");
    }

    public int cartItemsCount() {
        return cartBadge.isVisible() ? Integer.parseInt(cartBadge.textContent()) : 0;
    }

    public void openCart() {
        cartLink.click();
    }
}
