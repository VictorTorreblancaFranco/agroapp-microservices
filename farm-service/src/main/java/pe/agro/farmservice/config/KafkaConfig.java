package pe.agro.farmservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic plotCreatedTopic() {
        return TopicBuilder.name("plot-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic plotUpdatedTopic() {
        return TopicBuilder.name("plot-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic plotDeletedTopic() {
        return TopicBuilder.name("plot-deleted")
                .partitions(3)
                .replicas(1)
                .build();
    }
}