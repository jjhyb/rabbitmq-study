package com.yibo.rabbitmq.producer;

import com.yibo.rabbitmq.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @author: huangyibo
 * @Date: 2019/12/4 17:24
 * @Description:
 */

@Component
@Slf4j
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if (ack) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                //消息可靠性投递，消息发送成功之后的处理
            } else {
                log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                log.info("异常处理......");
                //消息可靠性投递，消息发送失败之后的处理
            }
        }
    };

    //回调函数: return返回
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode,
                                        String replyText, String exchange, String routingKey) {
            log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:({})", exchange, routingKey, replyCode, replyText, message);
            log.info("补偿操作，重试等");
            //exchange路由消息到queue失败,则回调return，消息丢失
            //消息可靠性投递，消息丢失之后的处理
        }
    };

    /**
     * 发送消息方法调用: 构建Message消息
     * @param message
     * @param properties
     */
    public void send(Object message,Map<String,Object> properties){
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message,messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        //id + 时间戳 全局唯一，这里使用uuid演示
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange-1", "springboot.abc", msg,correlationData);
    }

    /**
     * 发送消息方法调用: 构建自定义对象消息
     * @param order
     */
    public void sendOrder(Order order){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        //id + 时间戳 全局唯一，这里使用uuid演示
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange-1", "springboot.abc", order,correlationData);
    }

}
