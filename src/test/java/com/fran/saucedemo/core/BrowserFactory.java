package com.fran.saucedemo.core;

import com.fran.saucedemo.config.BrowserType;
import com.fran.saucedemo.config.TestConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BrowserFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserFactory.class);

    private BrowserFactory() {}

    public static Browser create(Playwright playwright) {
        LaunchOptions options =
                new LaunchOptions()
                        .setHeadless(TestConfig.headless())
                        .setSlowMo(TestConfig.slowMotionMs());

        BrowserType browserType = TestConfig.browser();
        LOGGER.debug(
                "Launching browser={} headless={} slowMotionMs={}",
                browserType,
                TestConfig.headless(),
                TestConfig.slowMotionMs());

        return switch (browserType) {
            case CHROMIUM -> playwright.chromium().launch(options);
            case FIREFOX -> playwright.firefox().launch(options);
            case WEBKIT -> playwright.webkit().launch(options);
        };
    }
}
