package dev.idriz.feeder.service.ws;

import dev.idriz.feeder.service.channel.ChannelManager;
import dev.idriz.feeder.service.channel.WebSocketChannel;
import io.javalin.websocket.WsMessageContext;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class WebSocketHandlerTest {

    // Helper method to create a mock WsMessageContext
    private @NotNull WsMessageContext mockContext(final @NotNull String data) {
        Session session = mock(Session.class);
        return new WsMessageContext(UUID.randomUUID().toString(), session, data);
    }

    // Helper method to create an empty WebSocketHandler
    private @NotNull WebSocketHandler emptyWebSocketHandler(final @NotNull ChannelManager channelManager) {
        return new WebSocketHandler(channelManager);
    }

    private final @NotNull ChannelManager channelManager = new ChannelManager();
    private WebSocketHandler webSocketHandler;

    @BeforeEach
    void setUp() {
        webSocketHandler = emptyWebSocketHandler(channelManager);
    }

    @Test
    void handle_shouldAllowValidInitialPayload() {
        channelManager.registerChannel(new TestWebSocketChannel());
        final WsMessageContext context = mockContext("test-channel|\"data\"");
        assertEquals(WebSocketStatus.OK, webSocketHandler.handle(context));
    }

    @Test
    void handle_shouldAllowValidChannelProvided() {
        channelManager.registerChannel(new TestWebSocketChannel());
        final WsMessageContext context = mockContext("test-channel|\"data\"");
        assertEquals(WebSocketStatus.OK, webSocketHandler.handle(context));
    }

    @Test
    void handle_shouldNotAllowInvalidInitialPayload() {
        final WsMessageContext context = mockContext("badpayload");
        assertEquals(WebSocketStatus.INVALID_INITIAL_PAYLOAD.getCode(), webSocketHandler.handle(context).getCode());
    }

    @Test
    void handle_shouldNotAllowInvalidChannelProvided() {
        final WsMessageContext context = mockContext("somechannel|data");
        assertEquals(WebSocketStatus.INVALID_CHANNEL_PROVIDED, webSocketHandler.handle(context));
    }

    @Test
    void handle_shouldNotAllowPayloadTooLarge() {
        final WsMessageContext context = mockContext(new String(new byte[64_010]));
        assertEquals(WebSocketStatus.PAYLOAD_TOO_LARGE, webSocketHandler.handle(context));
    }

    private static class TestWebSocketChannel implements WebSocketChannel<String> {

        @NotNull
        @Override
        public Class<String> getPayloadType() {
            return String.class;
        }

        @NotNull
        @Override
        public String getName() {
            return "test-channel";
        }

        @Override
        public @NotNull WebSocketStatus onMessageReceived(@NotNull WsMessageContext context, @NotNull String message) {
            return WebSocketStatus.OK;
        }
    }
}