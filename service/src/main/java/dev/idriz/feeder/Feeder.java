package dev.idriz.feeder;

import dev.idriz.feeder.channel.ChannelManager;
import dev.idriz.feeder.common.kafka.KafkaManager;
import dev.idriz.feeder.common.kafka.factory.DefaultKafkaConsumerFactory;
import dev.idriz.feeder.common.kafka.factory.DefaultKafkaProducerFactory;
import dev.idriz.feeder.common.sentry.SentryManager;
import dev.idriz.feeder.ws.WebSocketHandler;
import dev.idriz.feeder.ws.WebSocketStatus;
import dev.idriz.feeder.ws.channel.ClickWebSocketChannel;
import dev.idriz.feeder.ws.channel.SwitchWebSocketChannel;
import dev.idriz.feeder.ws.channel.ViewWebSocketChannel;
import io.javalin.Javalin;
import io.sentry.SentryLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Feeder {

    private final ChannelManager channelManager;
    private final SentryManager sentryManager;
    private final KafkaManager kafkaManager;

    private final int httpPort;
    private final String kafkaHost;

    private Javalin app;

    public Feeder(int httpPort, @NotNull String kafkaHost, @NotNull String sentryDsn) {

        this.httpPort = httpPort;
        this.kafkaHost = Objects.requireNonNull(kafkaHost, "kafkaHost");

        this.channelManager = new ChannelManager();
        this.sentryManager = new SentryManager(Objects.requireNonNull(sentryDsn, "sentryDsn"));
        this.kafkaManager = new KafkaManager(
                new DefaultKafkaConsumerFactory(kafkaHost),
                new DefaultKafkaProducerFactory(kafkaHost)
        );
    }

    public void start() {
        final WebSocketHandler webSocketHandler = new WebSocketHandler(channelManager);
        channelManager
                .registerChannel(new ClickWebSocketChannel(kafkaManager))
                .registerChannel(new ViewWebSocketChannel(kafkaManager))
                .registerChannel(new SwitchWebSocketChannel(kafkaManager, sentryManager));

        app = Javalin.create()
                .ws("/dev/idriz/feeder",
                        config -> {
                            config.onConnect((context) -> {
                                sentryManager.logMessageWithSeverity(
                                        "WebSocket connected by " + context.sessionId(),
                                        SentryLevel.INFO
                                );
                            });
                            config.onMessage((context) -> {
                                var result = webSocketHandler.handle(context);
                                if (result != WebSocketStatus.OK) {
                                    // Log to sentry in case we have any discrepancy.
                                    sentryManager.logMessageWithSeverity(
                                            "Failed WebSocket payload:" + result.name() + " by " + context.sessionId(),
                                            SentryLevel.ERROR
                                    );
                                }
                            });
                        })
                .start(httpPort);
    }

    public void shutdown() {
        assert app != null;
        // Cleans up the app instance.
        app.stop();
    }

    /**
     * Gets the ChannelManager.
     *
     * @return The ChannelManager.
     */
    @NotNull
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
     * Gets the SentryManager.
     *
     * @return The SentryManager.
     */
    @NotNull
    public SentryManager getSentryManager() {
        return sentryManager;
    }

    /**
     * Gets the KafkaManager.
     *
     * @return The KafkaManager.
     */
    @NotNull
    public KafkaManager getKafkaManager() {
        return kafkaManager;
    }

    /**
     * Gets the kafka host.
     *
     * @return The kafka host.
     */
    public String getKafkaHost() {
        return kafkaHost;
    }
}
