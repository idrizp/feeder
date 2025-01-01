package dev.idriz.feeder.common.env;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Environment {

    public static <T> T getEnv(String key, T defaultValue, Function<String, T> mapper) {
        var result = System.getenv(key);
        if (result != null) {
            return mapper.apply(result);
        }
        return defaultValue;
    }

    public static String getEnv(@NotNull String key, @NotNull String defaultValue) {
        return getEnv(key, defaultValue, k -> k);
    }

}
