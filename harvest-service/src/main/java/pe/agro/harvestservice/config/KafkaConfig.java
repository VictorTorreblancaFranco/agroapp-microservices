package pe.agro.harvestservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic harvestCreatedTopic() {
        return TopicBuilder.name("harvest-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic harvestUpdatedTopic() {
        return TopicBuilder.name("harvest-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
