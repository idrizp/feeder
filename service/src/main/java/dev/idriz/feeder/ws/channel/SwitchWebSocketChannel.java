package dev.idriz.feeder.ws.channel;

import dev.idriz.feeder.channel.WebSocketChannel;
import dev.idriz.feeder.common.kafka.KafkaManager;
import dev.idriz.feeder.common.sentry.SentryManager;
import dev.idriz.feeder.ws.WebSocketStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

public class SwitchWebSocketChannel implements WebSocketChannel<SwitchWebSocketChannel.ViewWebSocketPayload> {

    private final KafkaManager kafkaManager;
    private final SentryManager sentryManager;

    public SwitchWebSocketChannel(final @NotNull KafkaManager kafkaManager, final @NotNull SentryManager sentryManager) {
        this.kafkaManager = kafkaManager;
        this.sentryManager = sentryManager;
    }

    public record ViewWebSocketPayload(String origin, String destination, long spent) { }

    @NotNull
    @Override
    public Class<ViewWebSocketPayload> getPayloadType() {
        return ViewWebSocketPayload.class;
    }

    @NotNull
    @Override
    public String getName() {
        return "switch";
    }

    @NotNull
    @Override
    public WebSocketStatus onMessageReceived(@NotNull WsMessageContext context, @NotNull ViewWebSocketPayload data) {
        String url = data.origin();
        long spent = data.spent();
        if (spent < 0) {
            sentryManager.logException(
                    new IllegalArgumentException("Invalid switch message: " + data)
            );
            return WebSocketStatus.INVALID_INITIAL_PAYLOAD;
        }
        kafkaManager.publish("switch", url + "|" + data.destination() + "|" + spent);
        return WebSocketStatus.OK;
    }
}
