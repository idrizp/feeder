package dev.idriz.feeder.prometheus.kafka.listener;

import dev.idriz.feeder.common.kafka.listener.KafkaListener;
import dev.idriz.feeder.common.sentry.SentryManager;
import io.prometheus.metrics.core.metrics.Counter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CTRListener implements KafkaListener {

    private final SentryManager sentryManager;
    private final Counter clickCounter;
    private final Counter viewCounter;

    public CTRListener(final @NotNull SentryManager sentryManager, final @NotNull Counter clickCounter, final @NotNull Counter viewCounter) {
        this.sentryManager = sentryManager;
        this.clickCounter = clickCounter;
        this.viewCounter = viewCounter;
    }

    @Override
    public @NotNull List<String> getTopics() {
        return List.of("click", "view");
    }

    @Override
    public void onMessage(@NotNull String topic, @NotNull String message) {
        switch (topic) {
            case "click" -> {
                var split = message.split("\\|");
                if (split.length != 2) {
                    sentryManager.logException(
                            new IllegalArgumentException("Invalid click message: " + message)
                    );
                    return;
                }
                clickCounter.labelValues(split[0], split[1]).inc();
            }
            case "view" -> {
                var split = message.split("\\|");
                if (split.length != 1) {
                    sentryManager.logException(
                            new IllegalArgumentException("Invalid view message: " + message)
                    );
                    return;
                }
                viewCounter.labelValues(split[0]).inc();
            }
            default -> {
                sentryManager.logException(
                        new IllegalArgumentException("Invalid topic: " + topic)
                );
            }
        }
    }
}
