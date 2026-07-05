package com.fran.saucedemo.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.fran.saucedemo.core.BaseTest;
import com.fran.saucedemo.fixtures.Users;
import com.fran.saucedemo.models.SortOption;
import com.fran.saucedemo.pages.InventoryPage;
import com.fran.saucedemo.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("SauceDemo")
@Feature("Inventory")
@Tag("inventory")
class InventoryTest extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Products can be sorted by price from low to high")
    void productsCanBeSortedByPriceFromLowToHigh() {
        InventoryPage inventoryPage = loggedInInventoryPage().sortBy(SortOption.PRICE_LOW_TO_HIGH);

        List<BigDecimal> actualPrices = inventoryPage.productPrices();

        assertThat(actualPrices).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test
    @DisplayName("Products can be sorted by name from Z to A")
    void productsCanBeSortedByNameFromZToA() {
        InventoryPage inventoryPage = loggedInInventoryPage().sortBy(SortOption.NAME_Z_TO_A);

        assertThat(inventoryPage.productNames()).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("User can add and remove a product from inventory")
    void userCanAddAndRemoveProductFromInventory() {
        InventoryPage inventoryPage =
                loggedInInventoryPage().addProductToCart("Sauce Labs Backpack");

        assertThat(inventoryPage.cartItemsCount()).isEqualTo(1);

        inventoryPage.removeProductFromCart("Sauce Labs Backpack");

        assertThat(inventoryPage.cartItemsCount()).isZero();
    }

    private InventoryPage loggedInInventoryPage() {
        return new LoginPage(page()).open().loginAs(Users.STANDARD);
    }
}
