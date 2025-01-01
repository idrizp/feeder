package dev.idriz.feeder.prometheus;

import dev.idriz.feeder.common.kafka.KafkaManager;
import dev.idriz.feeder.common.sentry.SentryManager;
import dev.idriz.feeder.prometheus.kafka.listener.CTRListener;
import dev.idriz.feeder.prometheus.kafka.listener.SwitchListener;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PrometheusExporter {

    private final KafkaManager kafkaManager;
    private final SentryManager sentryManager;

    private final int exporterPort;

    public PrometheusExporter(int exporterPort, @NotNull String kafkaHost, @NotNull String sentryDsn) {
        this.exporterPort = exporterPort;

        this.kafkaManager = KafkaManager.createDefaults(kafkaHost);
        this.sentryManager = new SentryManager(sentryDsn);
    }

    public void start() {
        final Counter clickCounter = Counter.builder()
                .name("click_total")
                .help("Total number of clicks")
                .labelNames("url", "elementDescriptor")
                .register();

        final Counter viewCounter = Counter.builder()
                .name("view_total")
                .help("Total number of views")
                .labelNames("url")
                .register();

        final Counter switchCounter = Counter.builder()
                .name("switch_total")
                .help("Total number of switches from one page to another.")
                .labelNames("destination", "origin")
                .register();

        final Counter timeSpentCounter = Counter.builder()
                .name("time_spent_total")
                .help("Total time spent on a page in milliseconds before switching to another page.")
                .labelNames("page")
                .register();

        kafkaManager.registerListener(new CTRListener(
                sentryManager,
                clickCounter,
                viewCounter
        ));

        kafkaManager.registerListener(new SwitchListener(
                sentryManager,
                switchCounter,
                timeSpentCounter
        ));

        try (HTTPServer server = HTTPServer.builder().port(exporterPort).buildAndStart()) {
            sentryManager.logMessage("Prometheus exporter started on port " + exporterPort);
        } catch (IOException e) {
            sentryManager.logException(e);
            System.exit(1);
        }
    }

    public void shutdown() {
        sentryManager.logMessage("Prometheus exporter shutting down");
    }

}
