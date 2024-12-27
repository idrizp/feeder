package dev.idriz.feeder.kafka;

import dev.idriz.feeder.kafka.factory.DefaultKafkaProducerFactory;
import dev.idriz.feeder.kafka.listener.KafkaListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class KafkaManagerTest {

    @Mock
    private KafkaConsumer<String, String> mockConsumer;

    private KafkaManager kafkaManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaManager = new KafkaManager((topics, groupId) -> mockConsumer, new DefaultKafkaProducerFactory("localhost:9092"));
    }

    @Test
    void test_registerListenerSubscribesToTopic() {
        TestListener listener = new TestListener();

        kafkaManager.registerListener(listener);

        verify(mockConsumer, times(1)).subscribe(List.of("test-topic"));
    }

    @Test
    void test_unregisterListenerStopsConsumer() {
        TestListener listener = new TestListener();

        kafkaManager.registerListener(listener);
        kafkaManager.unregisterListener(listener);

        verify(mockConsumer, times(1)).close();
    }

    public static class TestListener implements KafkaListener {

        @NotNull
        @Override
        public List<String> getTopics() {
            return List.of("test-topic");
        }

        @NotNull
        @Override
        public String getGroupId() {
            return "test-group";
        }

        @Override
        public void onMessage(@NotNull String topic, @NotNull String message) {

        }
    }

}