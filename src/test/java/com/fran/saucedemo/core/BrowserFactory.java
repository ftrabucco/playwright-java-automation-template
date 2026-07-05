package com.fran.saucedemo.core;

import com.fran.saucedemo.config.BrowserType;
import com.fran.saucedemo.config.TestConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Playwright;

public final class BrowserFactory {
    private BrowserFactory() {}

    public static Browser create(Playwright playwright) {
        LaunchOptions options =
                new LaunchOptions()
                        .setHeadless(TestConfig.headless())
                        .setSlowMo(TestConfig.slowMotionMs());

        BrowserType browserType = TestConfig.browser();
        return switch (browserType) {
            case CHROMIUM -> playwright.chromium().launch(options);
            case FIREFOX -> playwright.firefox().launch(options);
            case WEBKIT -> playwright.webkit().launch(options);
        };
    }
}
