package com.fran.saucedemo.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.fran.saucedemo.core.BaseTest;
import com.fran.saucedemo.fixtures.Users;
import com.fran.saucedemo.models.User;
import com.fran.saucedemo.pages.InventoryPage;
import com.fran.saucedemo.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("SauceDemo")
@Feature("Login")
@Tag("login")
class LoginTest extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Standard user can log in")
    void standardUserCanLogIn() {
        InventoryPage inventoryPage = new LoginPage(page()).open().loginAs(Users.STANDARD);

        assertThat(inventoryPage.isLoaded()).isTrue();
        assertThat(inventoryPage.productNames()).hasSize(6);
    }

    @Test
    @DisplayName("Locked out user sees an access error")
    void lockedOutUserSeesAccessError() {
        LoginPage loginPage = new LoginPage(page()).open().submitInvalidLogin(Users.LOCKED_OUT);

        assertThat(loginPage.errorMessage()).contains("Sorry, this user has been locked out");
    }

    @Test
    @DisplayName("Invalid credentials show an authentication error")
    void invalidCredentialsShowAuthenticationError() {
        LoginPage loginPage =
                new LoginPage(page())
                        .open()
                        .submitInvalidLogin(new User("wrong_user", "wrong_password"));

        assertThat(loginPage.errorMessage()).contains("Username and password do not match");
    }
}
