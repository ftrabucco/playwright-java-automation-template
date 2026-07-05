package com.fran.saucedemo.fixtures;

import com.fran.saucedemo.models.CheckoutInformation;

public final class CheckoutData {
    public static final CheckoutInformation DEFAULT_CUSTOMER =
            new CheckoutInformation("Francisco", "Automation", "5000");

    private CheckoutData() {}
}
