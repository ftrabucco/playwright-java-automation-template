package com.fran.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.List;

public class CartPage {
    private final Page page;
    private final Locator cartItems;
    private final Locator checkoutButton;

    public CartPage(Page page) {
        this.page = page;
        this.cartItems = page.locator("[data-test='inventory-item']");
        this.checkoutButton = page.locator("[data-test='checkout']");
    }

    public List<String> productNames() {
        return cartItems.locator("[data-test='inventory-item-name']").allTextContents();
    }

    public int itemsCount() {
        return cartItems.count();
    }

    public CartPage removeProduct(String productName) {
        productByName(productName).locator("button").click();
        return this;
    }

    public CheckoutStepOnePage checkout() {
        checkoutButton.click();
        return new CheckoutStepOnePage(page);
    }

    private Locator productByName(String productName) {
        return cartItems.filter(new Locator.FilterOptions().setHasText(productName));
    }
}
