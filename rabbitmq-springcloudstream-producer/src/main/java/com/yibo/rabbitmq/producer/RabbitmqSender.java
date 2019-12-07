package com.yibo.rabbitmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: huangyibo
 * @Date: 2019/12/5 16:15
 * @Description:
 */

@EnableBinding(Processor.class)
@Service
@Slf4j
public class RabbitmqSender {

    @Autowired
    private Processor processor;

    public void sendMessage(Object message,Map<String, Object> properties){
        try {
            MessageHeaders messageHeaders = new MessageHeaders(properties);
            Message<Object> msg = MessageBuilder.createMessage(message, messageHeaders);
            boolean sendStatus = processor.logoutput().send(msg);
            log.info("--------------sending -------------------");
            log.info("发送数据：" + message + ",sendStatus: " + sendStatus);
        } catch (Exception e) {
            log.error("-------------error-------------，e({})",e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
