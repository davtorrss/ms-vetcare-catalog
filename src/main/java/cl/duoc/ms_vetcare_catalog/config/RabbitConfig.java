package cl.duoc.ms_vetcare_catalog.config;

import cl.duoc.ms_vetcare_catalog.messaging.RabbitNames;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // =========================
    // CONVERSOR JSON
    // =========================

    @Bean
    public DefaultClassMapper classMapper() {

        DefaultClassMapper classMapper = new DefaultClassMapper();

        // Solo confiamos en nuestros DTO de Catalog
        classMapper.setTrustedPackages(
                "cl.duoc.ms_vetcare_catalog.dto"
        );

        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(
            DefaultClassMapper classMapper
    ) {

        JacksonJsonMessageConverter converter =
                new JacksonJsonMessageConverter();

        converter.setClassMapper(classMapper);

        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(
                jsonMessageConverter
        );

        return rabbitTemplate;
    }

    // =========================
    // EXCHANGES
    // =========================

    @Bean
    public DirectExchange directExchange() {

        return new DirectExchange(
                RabbitNames.DIRECT_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public TopicExchange topicExchange() {

        return new TopicExchange(
                RabbitNames.TOPIC_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public DirectExchange deadLetterExchange() {

        return new DirectExchange(
                RabbitNames.DLX_EXCHANGE,
                true,
                false
        );
    }

    // =========================
    // QUEUES
    // =========================

    @Bean
    public Queue catalogQueue() {

        return QueueBuilder
                .durable(RabbitNames.CATALOG_QUEUE)

                // Mensajes rechazados van al DLX
                .deadLetterExchange(
                        RabbitNames.DLX_EXCHANGE
                )

                .deadLetterRoutingKey(
                        RabbitNames.CATALOG_DLQ_ROUTING_KEY
                )

                // TTL de 60 segundos
                .ttl(60000)

                .build();
    }

    @Bean
    public Queue catalogDeadLetterQueue() {

        return QueueBuilder
                .durable(RabbitNames.CATALOG_DLQ)
                .build();
    }

    // =========================
    // BINDINGS
    // =========================

    @Bean
    public Binding catalogCreatedBinding(
            Queue catalogQueue,
            DirectExchange directExchange
    ) {

        return BindingBuilder
                .bind(catalogQueue)
                .to(directExchange)
                .with(RabbitNames.CATALOG_CREATED);
    }

    @Bean
    public Binding catalogTopicBinding(
            Queue catalogQueue,
            TopicExchange topicExchange
    ) {

        return BindingBuilder
                .bind(catalogQueue)
                .to(topicExchange)
                .with("catalog.#");
    }

    @Bean
    public Binding catalogDeadLetterBinding(
            Queue catalogDeadLetterQueue,
            DirectExchange deadLetterExchange
    ) {

        return BindingBuilder
                .bind(catalogDeadLetterQueue)
                .to(deadLetterExchange)
                .with(
                        RabbitNames.CATALOG_DLQ_ROUTING_KEY
                );
    }
}