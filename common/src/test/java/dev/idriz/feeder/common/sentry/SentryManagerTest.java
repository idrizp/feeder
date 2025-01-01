package dev.idriz.feeder.common.sentry;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SentryManagerTest {

    private static final String TEST_DSN = "http://example.com";
    private SentryManager sentryManager;

    private MockedStatic<Sentry> mockedSentry;

    @BeforeEach
    void setUp() {
        mockedSentry = mockStatic(Sentry.class);
        sentryManager = new SentryManager(TEST_DSN);
        mockedSentry.when(() -> Sentry.init(any(Sentry.OptionsConfiguration.class))).then(invocation -> null);
    }

    @AfterEach
    void tearDown() {
        mockedSentry.close();
    }

    @Test
    void logException_shouldCaptureException() {
        Exception exception = new RuntimeException("Test exception");

        sentryManager.logException(exception);

        mockedSentry.verify(() -> Sentry.captureException(exception), times(1));
    }

    @Test
    void logException_shouldNotCaptureNullException() {
        assertThrows(NullPointerException.class, () -> sentryManager.logException(null));
    }

    @Test
    void logMessage_shouldCaptureMessage() {
        String message = "Test message";
        sentryManager.logMessage(message);
        mockedSentry.verify(() -> Sentry.captureMessage(message), times(1));
    }

    @Test
    void logMessage_shouldNotCaptureNullOrEmptyMessage() {
        assertThrows(NullPointerException.class, () -> sentryManager.logMessage(null));
        assertThrows(IllegalArgumentException.class, () -> sentryManager.logMessage(""));
    }

    @Test
    void logEvent_shouldCaptureMessageWithContext() {
        String message = "Test event";
        String context = "Test context";

        sentryManager.logEvent(message, context);

        mockedSentry.verify(() -> Sentry.captureMessage(eq(message), eq(SentryLevel.INFO), any()), times(1));
    }

    @Test
    void logEvent_shouldNotCaptureNullOrEmptyMessage() {
        assertThrows(NullPointerException.class, () -> sentryManager.logEvent(null, "Context"));
        assertThrows(IllegalArgumentException.class, () -> sentryManager.logEvent("", "Context"));
    }

    @Test
    void logMessageWithSeverity_shouldCaptureMessageWithSpecifiedLevel() {
        String message = "Test message with severity";
        SentryLevel level = SentryLevel.WARNING;

        sentryManager.logMessageWithSeverity(message, level);

        mockedSentry.verify(() -> Sentry.captureMessage(message, level), times(1));
    }

    @Test
    void logMessageWithSeverity_shouldNotCaptureNullOrEmptyMessage() {
        assertThrows(NullPointerException.class, () -> sentryManager.logMessageWithSeverity(null, SentryLevel.INFO));
        assertThrows(IllegalArgumentException.class, () -> sentryManager.logMessageWithSeverity("", SentryLevel.INFO));
    }
}
