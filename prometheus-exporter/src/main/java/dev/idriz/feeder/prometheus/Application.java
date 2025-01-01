package dev.idriz.feeder.prometheus;

import static dev.idriz.feeder.common.env.Environment.getEnv;

public class Application {

    public static void main(String[] args) {
        int prometheusPort = getEnv("PROMETHEUS_PORT", 8081, Integer::parseInt);
        String kafkaHost = getEnv("KAFKA_HOST", "localhost:9092");
        String sentryDsn = getEnv("SENTRY_DSN", "sentry-dsn");

        PrometheusExporter prometheusExporter = new PrometheusExporter(prometheusPort, kafkaHost, sentryDsn);
        prometheusExporter.start();
        // Runs on the shutdown of the feeder service.
        Runtime.getRuntime().addShutdownHook(new Thread(prometheusExporter::shutdown));
    }

}