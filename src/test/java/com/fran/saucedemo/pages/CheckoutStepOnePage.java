package com.fran.saucedemo.pages;

import com.fran.saucedemo.models.CheckoutInformation;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutStepOnePage {
    private final Page page;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator postalCodeInput;
    private final Locator continueButton;
    private final Locator errorMessage;

    public CheckoutStepOnePage(Page page) {
        this.page = page;
        this.firstNameInput = page.locator("[data-test='firstName']");
        this.lastNameInput = page.locator("[data-test='lastName']");
        this.postalCodeInput = page.locator("[data-test='postalCode']");
        this.continueButton = page.locator("[data-test='continue']");
        this.errorMessage = page.locator("[data-test='error']");
    }

    public CheckoutStepTwoPage continueWith(CheckoutInformation information) {
        firstNameInput.fill(information.firstName());
        lastNameInput.fill(information.lastName());
        postalCodeInput.fill(information.postalCode());
        continueButton.click();
        return new CheckoutStepTwoPage(page);
    }

    public CheckoutStepOnePage continueWithoutInformation() {
        continueButton.click();
        return this;
    }

    public String errorMessage() {
        return errorMessage.textContent();
    }
}
