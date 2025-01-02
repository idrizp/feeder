package dev.idriz.feeder.common.kafka.factory;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface is responsible for creating KafkaConsumer instances.
 * It provides a method for creating KafkaConsumer instances.
 */
public interface KafkaConsumerFactory {

    /**
     * Creates a KafkaConsumer for the given topics and groupId.
     * @param topics The topics.
     * @param groupId  The groupId.
     * @return The KafkaConsumer.
     */
    KafkaConsumer<String, String> createConsumer(@NotNull List<String> topics, @NotNull String groupId);

}
