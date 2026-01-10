package io.github.houcai.gift_shop_backend_notification;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        /*
            A durable queue:
            - is stored on disk
            - is NOT deleted when RabbitMQ restarts
            - still exists after a crash or reboot
         */
        return QueueBuilder.durable(queueName)
                .build();
    }

    @Bean
    public TopicExchange exchange(){
        return ExchangeBuilder.topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    /*
     * Admin: A Spring AMQP infrastructure component that automatically declares
     *  AMQP resources (queues, exchanges, bindings) in RabbitMQ.
     */
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        /*
        When the Spring application context is fully initialized,
        automatically declare all AMQP beans.

        Without RabbitAdmin:
        Queue, Exchange, Binding beans exist only in Spring
        RabbitMQ itself knows nothing about them
        Messages sent to missing exchanges are dropped or rejected
         */
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
