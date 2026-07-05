package com.fran.saucedemo.config;

public enum BrowserType {
    CHROMIUM,
    FIREFOX,
    WEBKIT;

    public static BrowserType from(String value) {
        return BrowserType.valueOf(value.trim().toUpperCase());
    }
}
