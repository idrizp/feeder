package dev.idriz.feeder.common.kafka;

import dev.idriz.feeder.common.kafka.factory.DefaultKafkaConsumerFactory;
import dev.idriz.feeder.common.kafka.factory.DefaultKafkaProducerFactory;
import dev.idriz.feeder.common.kafka.factory.KafkaConsumerFactory;
import dev.idriz.feeder.common.kafka.factory.KafkaProducerFactory;
import dev.idriz.feeder.common.kafka.listener.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible for managing Kafka listeners and consumers.
 * It provides methods for registering and unregistering listeners and invoking listeners.
 */
public class KafkaManager {

    private final @NotNull ExecutorService consumerExecutor = Executors.newCachedThreadPool(); // Executor for running consumers

    private final @NotNull Map<String, KafkaListener> listeners = new ConcurrentHashMap<>();
    private final @NotNull Map<String, KafkaConsumer<String, String>> consumers = new ConcurrentHashMap<>();

    private final @NotNull Map<String, KafkaProducer<String, String>> producers = new ConcurrentHashMap<>();
    private final @NotNull KafkaConsumerFactory consumerFactory;
    private final @NotNull KafkaProducerFactory producerFactory;

    /**
     * Initializes the KafkaManager with the specified KafkaConsumerFactory.
     *
     * @param consumerFactory The KafkaConsumerFactory.
     */
    public KafkaManager(final @NotNull KafkaConsumerFactory consumerFactory,
                        final @NotNull KafkaProducerFactory producerFactory) {
        this.producerFactory = Objects.requireNonNull(producerFactory, "producerFactory");
        this.consumerFactory = Objects.requireNonNull(consumerFactory, "consumerFactory");
    }

    /**
     * Creates a new KafkaManager with the specified KafkaHost.
     *
     * @param kafkaHost The host of the Kafka broker.
     * @return The KafkaManager.
     */
    public static KafkaManager createDefaults(final @NotNull String kafkaHost) {
        return new KafkaManager(
                new DefaultKafkaConsumerFactory(kafkaHost),
                new DefaultKafkaProducerFactory(kafkaHost)
        );
    }

    /**
     * Registers a Kafka listener.
     *
     * @param listener The listener.
     */
    public void registerListener(final @NotNull KafkaListener listener) {
        Objects.requireNonNull(listener, "listener");

        if (listeners.containsKey(listener.getGroupId())) {
            throw new IllegalArgumentException("Listener with groupId " + listener.getGroupId() + " already exists. Please use a different groupId.");
        }
        listeners.put(listener.getGroupId(), listener);

        KafkaConsumer<String, String> consumer = consumerFactory.createConsumer(listener.getTopics(), listener.getGroupId());
        consumer.subscribe(listener.getTopics());

        consumers.put(listener.getGroupId(), consumer);
        consumerExecutor.submit(() -> {
            Duration duration = Duration.ofSeconds(1);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(duration);
                if (records == null) {
                    continue;
                }
                for (ConsumerRecord<String, String> record : records) {
                    listener.onMessage(record.topic(), record.value());
                }
            }
        });
    }

    /**
     * Unregisters a Kafka listener.
     *
     * @param listener The listener.
     */
    public void unregisterListener(final @NotNull KafkaListener listener) {
        Objects.requireNonNull(listener, "listener");

        listeners.remove(listener.getGroupId());
        KafkaConsumer<String, String> consumer = consumers.remove(listener.getGroupId());
        if (consumer != null) {
            consumer.close();
        }
    }

    public void publish(final @NotNull String topic, final @NotNull String message) {
        Objects.requireNonNull(topic, "topic");
        Objects.requireNonNull(message, "message");
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        KafkaProducer<String, String> producer = producers.get(topic);
        if (producer == null) {
            producer = producerFactory.createProducer();
            producers.put(topic, producer);
        }

        producer.send(new ProducerRecord<>(topic, message));
    }

}
