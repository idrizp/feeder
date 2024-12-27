package dev.idriz.feeder.ws.channel;

import dev.idriz.feeder.channel.WebSocketChannel;
import dev.idriz.feeder.kafka.KafkaManager;
import dev.idriz.feeder.ws.WebSocketStatus;
import io.javalin.websocket.WsMessageContext;
import org.jetbrains.annotations.NotNull;

public class ClickWebSocketChannel implements WebSocketChannel<ClickWebSocketChannel.ClickWebSocketPayload> {

    private final KafkaManager kafkaManager;

    public ClickWebSocketChannel(KafkaManager kafkaManager) {
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
    public WebSocketStatus onMessageReceived(@NotNull WsMessageContext context, @NotNull ClickWebSocketPayload data) {
        var url = data.url();
        var elementDescriptor = data.elementDescriptor();
        kafkaManager.publish("click", url + "|" + elementDescriptor);
        return WebSocketStatus.OK;
    }
}
