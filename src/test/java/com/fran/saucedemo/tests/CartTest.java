package com.fran.saucedemo.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.fran.saucedemo.core.BaseTest;
import com.fran.saucedemo.fixtures.Users;
import com.fran.saucedemo.pages.CartPage;
import com.fran.saucedemo.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("SauceDemo")
@Feature("Cart")
@Tag("cart")
class CartTest extends BaseTest {

    @Test
    @DisplayName("User can remove a product from the cart")
    void userCanRemoveProductFromCart() {
        CartPage cartPage =
                new LoginPage(page())
                        .open()
                        .loginAs(Users.STANDARD)
                        .addProductToCart("Sauce Labs Backpack")
                        .addProductToCart("Sauce Labs Bike Light")
                        .openCart()
                        .removeProduct("Sauce Labs Backpack");

        assertThat(cartPage.itemsCount()).isEqualTo(1);
        assertThat(cartPage.productNames())
                .containsExactly("Sauce Labs Bike Light")
                .doesNotContain("Sauce Labs Backpack");
    }
}
