package dev.idriz.feeder.prometheus.kafka.listener;

import dev.idriz.feeder.common.kafka.listener.KafkaListener;
import dev.idriz.feeder.common.sentry.SentryManager;
import io.prometheus.metrics.core.metrics.Counter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Documents when a user switches between two pages.
 */
public class SwitchListener implements KafkaListener {

    private final SentryManager sentryManager;

    private final Counter timeSpentCounter;
    private final Counter switchCounter;

    public SwitchListener(final @NotNull SentryManager sentryManager,
                          final @NotNull Counter switchCounter,
                          final @NotNull Counter timeSpentCounter
    ) {
        this.sentryManager = sentryManager;
        this.switchCounter = switchCounter;
        this.timeSpentCounter = timeSpentCounter;
    }

    @Override
    public @NotNull List<String> getTopics() {
        return List.of("switch");
    }

    @Override
    public void onMessage(final @NotNull String topic, final @NotNull String message) {
        String[] split = message.split("\\|");
        if (split.length != 3) {
            sentryManager.logException(
                    new IllegalArgumentException("Invalid switch message: " + message)
            );
        }
        String destination = split[1];
        String origin = split[0];
        long spent = Long.parseLong(split[2]); // Includes how long the user spent on the origin page in milliseconds.

        switchCounter.labelValues(destination, origin).inc();
        timeSpentCounter.labelValues(origin, destination).inc(spent);
    }
}
