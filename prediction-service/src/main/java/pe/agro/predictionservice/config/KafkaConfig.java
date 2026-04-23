package pe.agro.predictionservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic predictionCreatedTopic() {
        return TopicBuilder.name("prediction-created").partitions(3).replicas(1).build();
    }
    @Bean
    public NewTopic predictionUpdatedTopic() {
        return TopicBuilder.name("prediction-updated").partitions(3).replicas(1).build();
    }
}
