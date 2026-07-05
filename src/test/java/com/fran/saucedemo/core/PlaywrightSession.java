package com.fran.saucedemo.core;

import com.fran.saucedemo.config.TestConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import java.nio.file.Path;

public final class PlaywrightSession implements AutoCloseable {
    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;
    private final Page page;

    public PlaywrightSession(Path artifactsDirectory) {
        this.playwright = Playwright.create();
        this.browser = BrowserFactory.create(playwright);
        this.context = browser.newContext(contextOptions(artifactsDirectory));
        this.context.setDefaultTimeout(TestConfig.timeoutMs());
        this.page = context.newPage();

        if (TestConfig.traceEnabled()) {
            this.context
                    .tracing()
                    .start(
                            new Tracing.StartOptions()
                                    .setScreenshots(true)
                                    .setSnapshots(true)
                                    .setSources(true));
        }
    }

    public Page page() {
        return page;
    }

    public void stopTracing(Path tracePath) {
        if (TestConfig.traceEnabled()) {
            context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        }
    }

    @Override
    public void close() {
        context.close();
        browser.close();
        playwright.close();
    }

    private static Browser.NewContextOptions contextOptions(Path artifactsDirectory) {
        Browser.NewContextOptions options =
                new Browser.NewContextOptions().setBaseURL(TestConfig.baseUrl());

        if (TestConfig.videoEnabled()) {
            options.setRecordVideoDir(artifactsDirectory.resolve("videos"));
        }

        return options;
    }
}
