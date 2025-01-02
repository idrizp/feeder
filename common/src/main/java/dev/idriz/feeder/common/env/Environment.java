package dev.idriz.feeder.common.env;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Environment {

    public static <T> T getEnv(final @NotNull String key, final @NotNull T defaultValue, final @NotNull Function<String, T> mapper) {
        String result = System.getenv(key);
        if (result != null) {
            return mapper.apply(result);
        }
        return defaultValue;
    }

    public static String getEnv(final @NotNull String key, final @NotNull String defaultValue) {
        return getEnv(key, defaultValue, k -> k);
    }

}
