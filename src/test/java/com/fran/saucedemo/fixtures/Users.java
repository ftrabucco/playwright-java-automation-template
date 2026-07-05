package com.fran.saucedemo.fixtures;

import com.fran.saucedemo.models.User;

public final class Users {
    public static final User STANDARD = new User("standard_user", "secret_sauce");
    public static final User LOCKED_OUT = new User("locked_out_user", "secret_sauce");

    private Users() {}
}
