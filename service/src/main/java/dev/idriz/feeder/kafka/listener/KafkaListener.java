package dev.idriz.feeder.kafka.listener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface KafkaListener {

    /**
     * Gets the topic.
     *
     * @return The topic.
     */
    @NotNull List<String> getTopics();

    /**
     * Gets the groupId.
     *
     * @return The groupId.
     */
    default @NotNull String getGroupId() {
        return "consumer-group-" + getClass().getSimpleName();
    }

    /**
     * Handles the message.
     *
     * @param topic   The topic.
     * @param message The message.
     */
    void onMessage(@NotNull String topic, @NotNull String message);

}
