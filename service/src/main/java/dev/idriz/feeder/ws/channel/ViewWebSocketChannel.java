package dev.idriz.feeder.ws.channel;

import dev.idriz.feeder.channel.WebSocketChannel;
import dev.idriz.feeder.common.kafka.KafkaManager;
import dev.idriz.feeder.ws.WebSocketStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

public class ViewWebSocketChannel implements WebSocketChannel<ViewWebSocketChannel.ViewWebSocketPayload> {

    private final KafkaManager kafkaManager;

    public ViewWebSocketChannel(KafkaManager kafkaManager) {
        this.kafkaManager = kafkaManager;
    }

    public record ViewWebSocketPayload(String url, String elementDescriptor) {
    }

    @NotNull
    @Override
    public Class<ViewWebSocketPayload> getPayloadType() {
        return ViewWebSocketPayload.class;
    }

    @NotNull
    @Override
    public String getName() {
        return "view";
    }

    @NotNull
    @Override
    public WebSocketStatus onMessageReceived(@NotNull WsMessageContext context, @NotNull ViewWebSocketPayload data) {
        String url = data.url();
        kafkaManager.publish("view", url);
        return WebSocketStatus.OK;
    }
}
