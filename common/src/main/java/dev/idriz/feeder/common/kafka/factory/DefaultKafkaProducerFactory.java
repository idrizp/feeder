package dev.idriz.feeder.common.kafka.factory;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Properties;

public class DefaultKafkaProducerFactory implements KafkaProducerFactory {

    private final String host;

    public DefaultKafkaProducerFactory(@NotNull String host) {
        this.host = Objects.requireNonNull(host, "host");
    }

    @NotNull
    @Override
    public KafkaProducer<String, String> createProducer() {
        // Create producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Optionally add additional configurations, e.g., retries, batch size, etc.
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensure all replicas acknowledge
        props.put(ProducerConfig.RETRIES_CONFIG, 3);  // Retry up to 3 times

        // Return a new KafkaProducer instance
        return new KafkaProducer<>(props);
    }
}
