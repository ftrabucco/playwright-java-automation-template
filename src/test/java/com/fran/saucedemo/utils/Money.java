package com.fran.saucedemo.utils;

import java.math.BigDecimal;

public final class Money {
    private Money() {}

    public static BigDecimal parse(String amount) {
        return new BigDecimal(amount.replace("$", "").trim());
    }
}
