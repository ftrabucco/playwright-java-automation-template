package com.fran.saucedemo.core;

import com.fran.saucedemo.config.TestConfig;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public abstract class BaseTest {
    @RegisterExtension
    final TestExecutionExceptionHandler failureArtifacts =
            (context, throwable) -> {
                attachFailureScreenshot();
                throw throwable;
            };

    private PlaywrightSession session;
    private Path artifactsDirectory;

    @BeforeEach
    void createSession(TestInfo testInfo) throws Exception {
        artifactsDirectory =
                Files.createDirectories(Path.of("target", "artifacts", safeName(testInfo)));
        session = new PlaywrightSession(artifactsDirectory);
    }

    @AfterEach
    void closeSession(TestInfo testInfo) {
        try {
            attachTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    protected Page page() {
        return session.page();
    }

    private void attachFailureScreenshot() {
        if (TestConfig.screenshotOnFailure() && session != null) {
            byte[] screenshot = page().screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment(
                    "Failure screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png");
        }
    }

    private void attachTrace() {
        Path tracePath = artifactsDirectory.resolve("trace.zip");
        session.stopTracing(tracePath);

        if (TestConfig.traceEnabled() && Files.exists(tracePath)) {
            try {
                Allure.addAttachment(
                        "Playwright trace",
                        "application/zip",
                        Files.newInputStream(tracePath),
                        ".zip");
            } catch (Exception exception) {
                throw new IllegalStateException("Could not attach Playwright trace", exception);
            }
        }
    }

    private static String safeName(TestInfo testInfo) {
        String displayName =
                testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "-").replaceAll("-+", "-");
        return displayName + "-" + UUID.randomUUID();
    }
}
