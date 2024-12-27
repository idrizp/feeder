package dev.idriz.feeder.kafka.listener;

import dev.idriz.feeder.kafka.listener.KafkaListener;
import io.prometheus.metrics.core.metrics.Counter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This listener is responsible for counting the number of clicks and views on a website.
 * It listens to the 'click' and 'view' topics.
 * The 'click' topic contains the url and the element descriptor (image, paragraph, etc.)
 * The 'view' topic contains only the url.
 * <p>
 * The counters are named 'clicks_<url>_<element-descriptor>' and 'views_<url>'.
 * The counters are created using the Prometheus library.
 * They will be displayed in Grafana.
 *
 */
public class CTRListener implements KafkaListener {

    private final Map<String, Counter> clicks = new HashMap<>();
    private final Map<String, Counter> views = new HashMap<>();

    @NotNull
    @Override
    public List<String> getTopics() {
        return List.of("click", "view");
    }

    @Override
    public void onMessage(@NotNull String topic, @NotNull String message) {
        if (topic.equals("click")) {
            onClick(message);
        } else if (topic.equals("view")) {
            onView(message);
        }
    }

    private void onView(@NotNull String message) {
        views.computeIfAbsent(message, k -> Counter.builder()
                        .name("views_" + message)
                        .help("Number of views on the url " + message)
                        .register())
                .inc();
    }

    private void onClick(@NotNull String message) {
        String[] data = message.split("\\|");
        String url = data[0];
        String elementDescriptor = data[1]; // This will be the element descriptor - image, paragraph, etc.
        clicks.computeIfAbsent(url + "|" + elementDescriptor, k -> Counter.builder()
                        .name("clicks_" + url + "_" + elementDescriptor)
                        .help("Number of clicks on the element " + elementDescriptor + " of the url " + url)
                        .register())
                .inc();
    }
}
