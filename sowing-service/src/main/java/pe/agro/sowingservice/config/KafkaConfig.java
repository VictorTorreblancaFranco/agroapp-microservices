package pe.agro.sowingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic sowingCreatedTopic() {
        return TopicBuilder.name("sowing-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sowingUpdatedTopic() {
        return TopicBuilder.name("sowing-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sowingStatusChangedTopic() {
        return TopicBuilder.name("sowing-status-changed")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sowingCompletedTopic() {
        return TopicBuilder.name("sowing-completed")
                .partitions(3)
                .replicas(1)
                .build();
    }
}