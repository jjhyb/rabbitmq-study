package com.yibo.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * @author: huangyibo
 * @Date: 2019/12/5 16:25
 * @Description:
 */

@EnableBinding(Processor.class)
@Service
@Slf4j
public class RabbitmqReceiver {

    @StreamListener(Processor.INPUT_CHANNEL)
    public void receiver(Message message) throws Exception {
        Channel channel = (com.rabbitmq.client.Channel) message.getHeaders().get(AmqpHeaders.CHANNEL);
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("Input Stream 1 接受数据：" + message);
        System.out.println("消费完毕------------");
        channel.basicAck(deliveryTag, false);
    }
}
