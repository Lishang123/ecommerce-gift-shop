package io.github.houcai.gift_shop_backend_notification;

import io.github.houcai.gift_shop_backend_notification.payload.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderCreatedEvent) {
        System.out.println("Received order event: " + orderCreatedEvent);
        // Update Database
        // Send Notification
        // Send Emails
        // Generate Invoice
        // Send Seller Notification
    }
}
