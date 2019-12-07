package com.yibo.rabbitmq.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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


        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("20000")
                .build();

        //5、发送消息
        String msg = "hello rabbitmq send dlx message!";
        channel.basicPublish(exchangeName,routingKey,true,properties,msg.getBytes("UTF-8"));

    }
}
