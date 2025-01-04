package dev.idriz.feeder.service.channel;

import dev.idriz.feeder.service.ws.WebSocketStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

public interface WebSocketChannel<T> {

    @NotNull Class<T> getPayloadType();

    /**
     * The name of the channel. It must be lowercase.
     * @return The name of the channel.
     */
    @NotNull String getName();

    /**
     * Handles a message received to a particular channel.
     *
     * @param context The context of the websocket.
     * @param data    The data.
     * @return A {@link WebSocketStatus} that indicates if the data was processed.
     */
    @NotNull WebSocketStatus onMessageReceived(@NotNull WsMessageContext context, @NotNull T data);

}
