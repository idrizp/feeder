package dev.idriz.feeder.common.sentry;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class is responsible for logging events and messages to Sentry.
 * It uses the Sentry Java SDK to capture events and messages.
 */
public class SentryManager {

    /**
     * Initializes the SentryManager with the specified Sentry DSN.
     *
     * @param sentryDsn The Sentry DSN.
     */
    public SentryManager(@NotNull String sentryDsn) {
        Objects.requireNonNull(sentryDsn, "dsn");
        Sentry.init(options -> {
            options.setDsn(sentryDsn);  // Replace with your actual Sentry DSN
            options.setTracesSampleRate(1.0);         // Adjust as needed for performance monitoring
        });
    }

    /**
     * Logs an exception.
     *
     * @param exception The exception to log.
     */
    public void logException(@NotNull Exception exception) {
        Objects.requireNonNull(exception, "exception");
        Sentry.captureException(exception);  // Capture the exception
    }

    /**
     * Logs a message.
     *
     * @param message The message to log.
     */
    public void logMessage(@NotNull String message) {
        Objects.requireNonNull(message, "message");
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }
        Sentry.captureMessage(message);  // Capture the message as a log entry
    }

    /**
     * Logs an event with the specified context.
     *
     * @param message The message to log.
     * @param context The context to add to the event.
     */
    public void logEvent(@NotNull String message, @NotNull String context) {
        Objects.requireNonNull(message, "message");
        Objects.requireNonNull(context, "context");
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message can not be empty.");
        }
        // Add custom context to the event
        Sentry.captureMessage(message, SentryLevel.INFO, (event) -> {
            if (!context.isEmpty()) {
                event.setContexts("custom_context", context);
            }
        });
    }

    /**
     * Logs a message with the specified severity.
     *
     * @param message The message to log.
     * @param level   The severity level.
     */
    public void logMessageWithSeverity(@NotNull String message, @NotNull SentryLevel level) {
        Objects.requireNonNull(message, "message");
        Objects.requireNonNull(level, "level");
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message can not be empty.");
        }
        Sentry.captureMessage(message, level);
    }
}
