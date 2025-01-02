package dev.idriz.feeder.ws.channel;

import dev.idriz.feeder.channel.WebSocketChannel;
import dev.idriz.feeder.common.kafka.KafkaManager;
import dev.idriz.feeder.common.sentry.SentryManager;
import dev.idriz.feeder.ws.WebSocketStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SwitchWebSocketChannel implements WebSocketChannel<SwitchWebSocketChannel.ViewWebSocketPayload> {

    private final KafkaManager kafkaManager;
    private final SentryManager sentryManager;
    private final Map<String, String> currentPages = new ConcurrentHashMap<>();
    private final Map<String, Long> lastPageSwitches = new ConcurrentHashMap<>();

    public SwitchWebSocketChannel(final @NotNull KafkaManager kafkaManager, final @NotNull SentryManager sentryManager) {
        this.kafkaManager = kafkaManager;
        this.sentryManager = sentryManager;
    }

    public record ViewWebSocketPayload(@NotNull String destination) {
    }

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
    public WebSocketStatus onMessageReceived(final @NotNull WsMessageContext context, final @NotNull ViewWebSocketPayload data) {
        String destination = data.destination();

        long now = System.currentTimeMillis();
        long spent = lastPageSwitches.getOrDefault(context.sessionId(), now);
        long delta = now - spent;

        if (delta == 0) {
            // We don't want to log this, because we haven't actually switched pages.
            return WebSocketStatus.OK;
        }

        lastPageSwitches.put(context.sessionId(), now);
        String previous = currentPages.put(context.sessionId(), destination);

        if (previous == null) {
            // This is the first time we've seen this session.
            return WebSocketStatus.OK;
        }

        kafkaManager.publish("switch", previous + "|" + destination + "|" + spent);
        return WebSocketStatus.OK;
    }
}
