package com.yibo.rabbitmq.limit;

import com.rabbitmq.client.*;

import java.io.IOException;
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

        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.save";

        //5、发送消息
        String msg = "hello rabbitmq send qos message!";
        channel.basicPublish(exchangeName,routingKey,true,null,msg.getBytes("UTF-8"));
    }
}
