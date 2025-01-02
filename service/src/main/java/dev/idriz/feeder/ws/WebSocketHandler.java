package dev.idriz.feeder.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.idriz.feeder.channel.ChannelManager;
import dev.idriz.feeder.channel.WebSocketChannel;
import io.javalin.websocket.WsCloseStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

/**
 * The main entry-point for all websocket based operations. Will forward any data to specific channels, bar heartbeat polling messages.
 */
public class WebSocketHandler {

    public static final int MAX_PAYLOAD_LENGTH_IN_BYTES = 64_000; // Anything more than 64 KB should be flagged.
    private final ChannelManager channelManager;

    public WebSocketHandler(final @NotNull ChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    /**
     * Handles the websocket input.
     *
     * @param context The context.
     * @return The status.
     */
    public @NotNull WebSocketStatus handle(final @NotNull WsMessageContext context) {
        String message = context.message();

        // Handle the case when the data is empty.
        if (message.isEmpty()) {
            context.closeSession(WsCloseStatus.ABNORMAL_CLOSURE, "Invalid initial payload.");
            return WebSocketStatus.INVALID_INITIAL_PAYLOAD;
        }

        if (message.length() > MAX_PAYLOAD_LENGTH_IN_BYTES) {
            return WebSocketStatus.PAYLOAD_TOO_LARGE;
        }

        if (message.equals("HEARTBEAT")) {
            // This is a simple polling heartbeat. Should be fine.
            return WebSocketStatus.OK;
        }
        String id = ChannelManager.retrieveChannelFromMessage(message);
        if (id == null) {
            context.closeSession(WsCloseStatus.ABNORMAL_CLOSURE, "Invalid channel provided.");
            return WebSocketStatus.INVALID_INITIAL_PAYLOAD;
        }
        @SuppressWarnings("unchecked")
        // This is not optimal, because we are forcefully downcasting the type. In any case, it is fine.
        WebSocketChannel<Object> channel = (WebSocketChannel<Object>) channelManager.getChannel(id);
        if (channel == null) {
            // There may be no actual channel by the id, even if the keyword is valid.
            context.closeSession(WsCloseStatus.ABNORMAL_CLOSURE, "Invalid channel provided.");
            return WebSocketStatus.INVALID_CHANNEL_PROVIDED;
        }

        String stripped = ChannelManager.stripChannel(message);

        JsonMapper mapper = new JsonMapper();
        try {
            Object payload = mapper.readValue(stripped, channel.getPayloadType());
            return channel.onMessageReceived(context, payload);
        } catch (JsonProcessingException e) {
            closeSession(context, "Invalid payload.");
            return WebSocketStatus.INVALID_INITIAL_PAYLOAD;
        }

    }

    /**
     * Closes the session with the specified message.
     *
     * @param context The context.
     * @param message The message.
     */
    public static void closeSession(final @NotNull WsMessageContext context, final String message) {
        context.closeSession(WsCloseStatus.ABNORMAL_CLOSURE, message);
    }

}
