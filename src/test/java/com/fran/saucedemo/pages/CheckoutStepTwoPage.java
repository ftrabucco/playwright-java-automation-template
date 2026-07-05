package com.fran.saucedemo.pages;

import static com.fran.saucedemo.utils.Money.parse;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.math.BigDecimal;

public class CheckoutStepTwoPage {
    private final Page page;
    private final Locator finishButton;
    private final Locator subtotalLabel;
    private final Locator taxLabel;
    private final Locator totalLabel;

    public CheckoutStepTwoPage(Page page) {
        this.page = page;
        this.finishButton = page.locator("[data-test='finish']");
        this.subtotalLabel = page.locator("[data-test='subtotal-label']");
        this.taxLabel = page.locator("[data-test='tax-label']");
        this.totalLabel = page.locator("[data-test='total-label']");
    }

    public CheckoutCompletePage finishOrder() {
        finishButton.click();
        return new CheckoutCompletePage(page);
    }

    public BigDecimal itemTotal() {
        return parse(amountAfterColon(subtotalLabel.textContent()));
    }

    public BigDecimal tax() {
        return parse(amountAfterColon(taxLabel.textContent()));
    }

    public BigDecimal total() {
        return parse(amountAfterColon(totalLabel.textContent()));
    }

    private static String amountAfterColon(String label) {
        return label.substring(label.indexOf(":") + 1);
    }
}
