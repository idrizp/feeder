package dev.idriz.feeder.channel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The channel manager is the main entrypoint of the Feeder service.
 * Each websocket message is redirected to a separate "channel", which will process the message differently.
 */
public class ChannelManager {

    public static final String CHANNEL_SEPARATOR = "\\|";

    private final Map<String, WebSocketChannel<?>> channels = new ConcurrentHashMap<>();

    /**
     * Returns the specific channel.
     *
     * @param id The id of the channel.
     * @return The channel under that specific id.
     */
    public @Nullable WebSocketChannel<?> getChannel(@NotNull String id) {
        return channels.get(id.toLowerCase());
    }

    /**
     * Registers the channel by the channel id.
     *
     * @param webSocketChannel The channel.
     * @return The channel manager instance.
     */
    public @NotNull ChannelManager registerChannel(@NotNull WebSocketChannel<?> webSocketChannel) {
        channels.put(webSocketChannel.getName().toLowerCase(), webSocketChannel);
        return this;
    }

    /**
     * Retrieves the channel from a raw string message.
     * @param message The message.
     * @return The channel retrieved from the message.
     */
    public static @Nullable String retrieveChannelFromMessage(@NotNull String message) {
        Objects.requireNonNull(message, "The message is null.");
        if (!message.contains("|")) {
            // The edge case when our message just doesn't have a channel separator at all.
            return null;
        }
        var splitOnAt = message.split(CHANNEL_SEPARATOR);
        if (splitOnAt.length == 0) {
            // We haven't found anything.
            return null;
        }
        var found = splitOnAt[0];
        if (found.isBlank()) {
            return null;
        }
        return found.toLowerCase();
    }

    public static @NotNull String stripChannel(@NotNull String message) {
        return message.substring(message.indexOf("|") + 1);
    }

}
