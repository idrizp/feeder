package dev.idriz.feeder.service.ws;

import io.javalin.websocket.WsMessageContext;

public enum WebSocketStatus {

    /**
     * The default OK response to a websocket message handler.
     */
    OK(0),

    /**
     * When a heartbeat is received.
     */
    HEARTBEAT(10),

    /**
     * When an invalid channel is provided. This is mostly seen in the {@link WebSocketHandler#handle(WsMessageContext)} function.
     */
    INVALID_CHANNEL_PROVIDED(1),

    /**
     * When an invalid payload is provided. This is mostly seen in the {@link WebSocketHandler#handle(WsMessageContext)} function.
     */
    INVALID_INITIAL_PAYLOAD(2),

    /**
     * When the data payload is far too large. This is mostly seen in the {@link WebSocketHandler#handle(WsMessageContext)} function.
     */
    PAYLOAD_TOO_LARGE(3);

    private final int code;

    WebSocketStatus(final int code) {
        this.code = code;
    }

    /**
     * Returns the code for book-keeping.
     * @return The integer-valued code.
     */
    public int getCode() {
        return code;
    }
}
