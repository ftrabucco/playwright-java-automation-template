package com.fran.saucedemo.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.fran.saucedemo.core.BaseTest;
import com.fran.saucedemo.fixtures.CheckoutData;
import com.fran.saucedemo.fixtures.Users;
import com.fran.saucedemo.pages.CheckoutCompletePage;
import com.fran.saucedemo.pages.CheckoutStepOnePage;
import com.fran.saucedemo.pages.CheckoutStepTwoPage;
import com.fran.saucedemo.pages.InventoryPage;
import com.fran.saucedemo.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("SauceDemo")
@Feature("Checkout")
@Tag("checkout")
class CheckoutTest extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Standard user can buy a product")
    void standardUserCanBuyProduct() {
        InventoryPage inventoryPage =
                new LoginPage(page())
                        .open()
                        .loginAs(Users.STANDARD)
                        .addProductToCart("Sauce Labs Backpack");

        assertThat(inventoryPage.cartItemsCount()).isEqualTo(1);

        CheckoutCompletePage confirmationPage =
                inventoryPage
                        .openCart()
                        .checkout()
                        .continueWith(CheckoutData.DEFAULT_CUSTOMER)
                        .finishOrder();

        assertThat(confirmationPage.isLoaded()).isTrue();
        assertThat(confirmationPage.confirmationMessage()).isEqualTo("Thank you for your order!");
    }

    @Test
    @DisplayName("Checkout requires customer information")
    void checkoutRequiresCustomerInformation() {
        CheckoutStepOnePage checkoutStepOnePage =
                new LoginPage(page())
                        .open()
                        .loginAs(Users.STANDARD)
                        .addProductToCart("Sauce Labs Backpack")
                        .openCart()
                        .checkout()
                        .continueWithoutInformation();

        assertThat(checkoutStepOnePage.errorMessage()).contains("First Name is required");
    }

    @Test
    @DisplayName("Checkout summary calculates item total, tax and final total")
    void checkoutSummaryCalculatesTotals() {
        CheckoutStepTwoPage checkoutSummaryPage =
                new LoginPage(page())
                        .open()
                        .loginAs(Users.STANDARD)
                        .addProductToCart("Sauce Labs Backpack")
                        .addProductToCart("Sauce Labs Bike Light")
                        .openCart()
                        .checkout()
                        .continueWith(CheckoutData.DEFAULT_CUSTOMER);

        assertThat(checkoutSummaryPage.itemTotal()).isEqualByComparingTo(new BigDecimal("39.98"));
        assertThat(checkoutSummaryPage.tax()).isEqualByComparingTo(new BigDecimal("3.20"));
        assertThat(checkoutSummaryPage.total()).isEqualByComparingTo(new BigDecimal("43.18"));
    }
}
