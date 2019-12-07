package com.yibo.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 23:48
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testAdmin(){
        rabbitAdmin.declareExchange(new DirectExchange("test.direct",false,false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic",false,false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout",false,false));

        rabbitAdmin.declareQueue(new Queue("test.direct.queue",false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue",false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue",false));

        rabbitAdmin.declareBinding(new Binding("test.direct.queue",Binding.DestinationType.QUEUE,
                "test.direct","driect",new HashMap<>()));

        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test.topic.queue",false))    //直接创建队列
                .to(new TopicExchange("test.topic",false,false)) //直接创建交换机，建立关联关系
                .with("key.#"));    //指定路由key

        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test.fanout.queue",false))    //直接创建队列
                        .to(new FanoutExchange("test.fanout",false,false))); //直接创建交换机，建立关联关系

        //清空队列
        rabbitAdmin.purgeQueue("test.topic.queue",false);
    }

    @Test
    public void testSendMessage(){
        //1、创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc","信息描述");
        messageProperties.getHeaders().put("type","自定义信息类型");
        Message message = new Message("hello rabbitmq".getBytes(),messageProperties);

        //2、发送消息
        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            //MessagePostProcessor 在消息发送完毕后再做一次转换进行再加工，匿名接口，需要重写方法
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.err.println("------添加额外的设置---------");
                message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
                message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
                return message;
            }
        });
    }

    @Test
    public void testSendMessage2(){
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("mq 消息1234".getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.abc", message);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object message send!");
        rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "hello object message send!");

    }
}
