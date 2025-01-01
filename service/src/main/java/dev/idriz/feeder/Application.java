package dev.idriz.feeder;

import static dev.idriz.feeder.common.env.Environment.getEnv;

public class Application {

    public static void main(String[] args) {

        var httpPort = getEnv("SERVER_PORT", 8080, Integer::parseInt);
        var kafkaHost = getEnv("KAFKA_HOST", "localhost:9092");
        var sentryDsn = getEnv("SENTRY_DSN", "sentry-dsn");

        var feeder = new Feeder(httpPort, kafkaHost, sentryDsn);
        feeder.start();

        // Runs on the shutdown of the feeder service.
        Runtime.getRuntime().addShutdownHook(new Thread(feeder::shutdown));
    }

}
