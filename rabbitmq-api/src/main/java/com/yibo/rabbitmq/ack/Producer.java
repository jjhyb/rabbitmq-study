package com.yibo.rabbitmq.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: huangyibo
 * @Date: 2019/12/3 17:42
 * @Description:
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2、通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3、通过Connection创建一个Channel
        Channel channel = connection.createChannel();

        //4、指定消息的投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.save";

        Map<String,Object> headers = new HashMap<>();
        headers.put("num",0);
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();
        for (int i = 0; i < 5; i++) {

            //5、发送消息
            String msg = "hello rabbitmq send ack message!"+i;
            channel.basicPublish(exchangeName,routingKey,true,properties,msg.getBytes("UTF-8"));
        }
    }
}
