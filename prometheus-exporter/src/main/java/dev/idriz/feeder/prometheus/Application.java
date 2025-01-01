package dev.idriz.feeder.prometheus;

import static dev.idriz.feeder.common.env.Environment.getEnv;

public class Application {

    public static void main(String[] args) {
        int prometheusPort = getEnv("PROMETHEUS_PORT", 8081, Integer::parseInt);
        var kafkaHost = getEnv("KAFKA_HOST", "localhost:9092");
        var sentryDsn = getEnv("SENTRY_DSN", "sentry-dsn");

        var prometheusExporter = new PrometheusExporter(prometheusPort, kafkaHost, sentryDsn).start();
        // Runs on the shutdown of the feeder service.
        Runtime.getRuntime().addShutdownHook(new Thread(prometheusExporter::shutdown));
    }

}