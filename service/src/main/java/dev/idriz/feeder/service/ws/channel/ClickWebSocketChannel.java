package dev.idriz.feeder.service.ws.channel;

import dev.idriz.feeder.service.channel.WebSocketChannel;
import dev.idriz.feeder.common.kafka.KafkaManager;
import dev.idriz.feeder.service.ws.WebSocketStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

public class ClickWebSocketChannel implements WebSocketChannel<ClickWebSocketChannel.ClickWebSocketPayload> {

    private final KafkaManager kafkaManager;

    public ClickWebSocketChannel(final @NotNull KafkaManager kafkaManager) {
        this.kafkaManager = kafkaManager;
    }

    public record ClickWebSocketPayload(String url, String elementDescriptor) {
    }

    @NotNull
    @Override
    public Class<ClickWebSocketPayload> getPayloadType() {
        return ClickWebSocketPayload.class;
    }

    @NotNull
    @Override
    public String getName() {
        return "click";
    }

    @NotNull
    @Override
    public WebSocketStatus onMessageReceived(final @NotNull WsMessageContext context, final @NotNull ClickWebSocketPayload data) {
        String url = data.url();
        String elementDescriptor = data.elementDescriptor();
        kafkaManager.publish("click", url + "|" + elementDescriptor);
        return WebSocketStatus.OK;
    }
}
