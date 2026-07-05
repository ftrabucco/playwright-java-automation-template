package com.fran.saucedemo.pages;

import com.fran.saucedemo.components.HeaderComponent;
import com.fran.saucedemo.models.SortOption;
import com.fran.saucedemo.utils.Money;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.math.BigDecimal;
import java.util.List;

public class InventoryPage {
    private final Page page;
    private final Locator title;
    private final Locator inventoryItems;
    private final Locator sortSelect;
    private final HeaderComponent header;

    public InventoryPage(Page page) {
        this.page = page;
        this.title = page.locator("[data-test='title']");
        this.inventoryItems = page.locator("[data-test='inventory-item']");
        this.sortSelect = page.locator("[data-test='product-sort-container']");
        this.header = new HeaderComponent(page);
    }

    public boolean isLoaded() {
        return title.textContent().equals("Products");
    }

    public List<String> productNames() {
        return inventoryItems.locator("[data-test='inventory-item-name']").allTextContents();
    }

    public List<BigDecimal> productPrices() {
        return inventoryItems
                .locator("[data-test='inventory-item-price']")
                .allTextContents()
                .stream()
                .map(Money::parse)
                .toList();
    }

    public InventoryPage sortBy(SortOption sortOption) {
        sortSelect.selectOption(sortOption.value());
        return this;
    }

    public InventoryPage addProductToCart(String productName) {
        productByName(productName).locator("button").click();
        return this;
    }

    public InventoryPage removeProductFromCart(String productName) {
        productByName(productName).locator("button").click();
        return this;
    }

    public int cartItemsCount() {
        return header.cartItemsCount();
    }

    public CartPage openCart() {
        header.openCart();
        return new CartPage(page);
    }

    private Locator productByName(String productName) {
        return inventoryItems.filter(new Locator.FilterOptions().setHasText(productName));
    }
}
