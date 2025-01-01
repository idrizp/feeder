package dev.idriz.feeder.prometheus;

import dev.idriz.feeder.common.kafka.KafkaManager;
import org.jetbrains.annotations.NotNull;

import static dev.idriz.feeder.common.env.Environment.getEnv;

public class PrometheusApp {

    private final KafkaManager kafkaManager;

    private final int prometheusPort;
    private final String kafkaHost;
    private final String sentryDsn;

    public PrometheusApp(int prometheusPort, @NotNull String kafkaHost, @NotNull String sentryDsn) {
        this.prometheusPort = prometheusPort;
        this.kafkaHost = kafkaHost;
        this.sentryDsn = sentryDsn;
        this.kafkaManager = KafkaManager.createDefaults(kafkaHost);
    }

    public static void main(String[] args) {
        int prometheusPort = getEnv("PROMETHEUS_PORT", 8081, Integer::parseInt);
        var kafkaHost = getEnv("KAFKA_HOST", "localhost:9092");
        var sentryDsn = getEnv("SENTRY_DSN", "sentry-dsn");

        new PrometheusApp(prometheusPort, kafkaHost, sentryDsn).start();
    }

    public void start() {

    }

}
