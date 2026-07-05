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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    @RegisterExtension
    final TestExecutionExceptionHandler failureArtifacts =
            (context, throwable) -> {
                testFailed = true;
                LOGGER.error("Test failed: {}", context.getDisplayName(), throwable);
                attachFailureScreenshot();
                throw throwable;
            };

    private PlaywrightSession session;
    private Path artifactsDirectory;
    private boolean testFailed;
    private boolean failureScreenshotAttached;

    @BeforeEach
    void createSession(TestInfo testInfo) throws Exception {
        testFailed = false;
        failureScreenshotAttached = false;
        artifactsDirectory =
                Files.createDirectories(Path.of("target", "artifacts", safeName(testInfo)));
        LOGGER.info(
                "START test=\"{}\" browser={} baseUrl={}",
                testInfo.getDisplayName(),
                TestConfig.browser(),
                TestConfig.baseUrl());
        LOGGER.debug("Artifacts directory={}", artifactsDirectory);
        session = new PlaywrightSession(artifactsDirectory);
    }

    @AfterEach
    void closeSession(TestInfo testInfo) {
        try {
            attachFinalScreenshot();
            attachTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            LOGGER.info("{} test=\"{}\"", testStatus(), testInfo.getDisplayName());
        }
    }

    protected Page page() {
        return session.page();
    }

    private void attachFailureScreenshot() {
        if (TestConfig.screenshotOnFailure() && session != null) {
            attachScreenshot("Failure screenshot");
            failureScreenshotAttached = true;
        }
    }

    private void attachFinalScreenshot() {
        if (TestConfig.screenshotAlways() && session != null && !failureScreenshotAttached) {
            attachScreenshot("Final screenshot");
        }
    }

    private void attachScreenshot(String attachmentName) {
        byte[] screenshot = page().screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment(
                attachmentName, "image/png", new ByteArrayInputStream(screenshot), ".png");
        LOGGER.debug("Attached screenshot name={} sizeBytes={}", attachmentName, screenshot.length);
    }

    private boolean shouldAttachTrace() {
        return TestConfig.traceEnabled() && (!TestConfig.traceAttachOnFailureOnly() || testFailed);
    }

    private boolean shouldStopTrace() {
        return TestConfig.traceEnabled() && session != null;
    }

    private void attachTraceIfNeeded(Path tracePath) {
        if (shouldAttachTrace() && Files.exists(tracePath)) {
            try {
                Allure.addAttachment(
                        "Playwright trace",
                        "application/zip",
                        Files.newInputStream(tracePath),
                        ".zip");
                LOGGER.debug("Attached Playwright trace path={}", tracePath);
            } catch (Exception exception) {
                throw new IllegalStateException("Could not attach Playwright trace", exception);
            }
        } else if (TestConfig.traceEnabled() && Files.exists(tracePath)) {
            LOGGER.debug("Trace generated but not attached path={}", tracePath);
        }
    }

    private void attachTrace() {
        Path tracePath = artifactsDirectory.resolve("trace.zip");

        if (shouldStopTrace()) {
            session.stopTracing(tracePath);
        }

        attachTraceIfNeeded(tracePath);
    }

    private static String safeName(TestInfo testInfo) {
        String displayName =
                testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "-").replaceAll("-+", "-");
        return displayName + "-" + UUID.randomUUID();
    }

    private String testStatus() {
        return testFailed ? "FAILED" : "PASSED";
    }
}
