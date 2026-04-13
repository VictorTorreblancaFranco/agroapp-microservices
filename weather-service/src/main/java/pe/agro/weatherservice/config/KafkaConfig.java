package pe.agro.weatherservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic weatherCreatedTopic() {
        return TopicBuilder.name("weather-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic weatherUpdatedTopic() {
        return TopicBuilder.name("weather-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }
}