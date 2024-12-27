package dev.idriz.feeder;

import dev.idriz.feeder.Feeder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Application {

    private static <T> T getEnv(String key, T defaultValue, Function<String, T> mapper) {
        var result = System.getenv(key);
        if (result != null) {
            return mapper.apply(result);
        }
        return defaultValue;
    }

    private static String getEnv(@NotNull String key, @NotNull String defaultValue) {
        return getEnv(key, defaultValue, k -> k);
    }

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
