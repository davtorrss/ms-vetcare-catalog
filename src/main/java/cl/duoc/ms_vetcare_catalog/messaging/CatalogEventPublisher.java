package cl.duoc.ms_vetcare_catalog.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CatalogEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CatalogEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(Object payload) {
        rabbitTemplate.convertAndSend(
                RabbitNames.DIRECT_EXCHANGE,
                RabbitNames.CATALOG_CREATED,
                payload
        );
    }

    public void publishUpdated(Object payload) {
        rabbitTemplate.convertAndSend(
                RabbitNames.TOPIC_EXCHANGE,
                RabbitNames.CATALOG_UPDATED,
                payload
        );
    }

    public void publishDeleted(Object payload) {
        rabbitTemplate.convertAndSend(
                RabbitNames.TOPIC_EXCHANGE,
                RabbitNames.CATALOG_DELETED,
                payload
        );
    }
}