package dev.idriz.feeder.common.kafka.factory;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Properties;

/**
 * This class is responsible for creating KafkaConsumer instances.
 * It uses the default KafkaConsumerFactory to create KafkaConsumer instances.
 */
public class DefaultKafkaConsumerFactory implements KafkaConsumerFactory {

    private final @NotNull String host;

    /**
     * Initializes the DefaultKafkaConsumerFactory with the specified host.
     *
     * @param host The host.
     */
    public DefaultKafkaConsumerFactory(final @NotNull String host) {
        this.host = host;
    }

    @Override
    public KafkaConsumer<String, String> createConsumer(final @NotNull List<String> topics, final @NotNull String groupId) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.host);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer<>(properties);
    }
}
