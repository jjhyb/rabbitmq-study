package com.yibo.rabbitmq.consumer;


import com.rabbitmq.client.Channel;
import com.yibo.rabbitmq.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author: huangyibo
 * @Date: 2019/12/5 2:13
 * @Description:
 */

@Component
@Slf4j
public class RabbitReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value = "queue-1",durable = "true"),
            exchange = @Exchange(value = "exchange-1",
                    durable = "true",
                    type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "springboot.*"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
        log.info("消费端收到消息体，message({})",message.getPayload().toString());
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag,false);
    }


    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${}spring.rabbitmq.listener.order.exchange.type",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.key}"
    ))
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, Channel channel, @Headers Map<String,Object> headers) throws IOException {
        log.info("消费端收到消息体，order({})",order);
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag,false);
    }
}
