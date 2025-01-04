package dev.idriz.feeder.service;

import static dev.idriz.feeder.common.env.Environment.getEnv;

public class Application {

    public static void main(String[] args) {

        Integer httpPort = getEnv("SERVER_PORT", 8080, Integer::parseInt);
        String kafkaHost = getEnv("KAFKA_HOST", "localhost:9092");
        String sentryDsn = getEnv("SENTRY_DSN", "sentry-dsn");

        System.out.println(kafkaHost);
        Feeder feeder = new Feeder(httpPort, kafkaHost, sentryDsn);
        feeder.start();

        // Runs on the shutdown of the feeder service.
        Runtime.getRuntime().addShutdownHook(new Thread(feeder::shutdown));
    }

}
