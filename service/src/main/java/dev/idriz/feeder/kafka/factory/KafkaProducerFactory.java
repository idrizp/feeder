package dev.idriz.feeder.kafka.factory;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.jetbrains.annotations.NotNull;

public interface KafkaProducerFactory {

    @NotNull KafkaProducer<String, String> createProducer();

}
